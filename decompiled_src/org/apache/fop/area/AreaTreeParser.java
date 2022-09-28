package org.apache.fop.area;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.accessibility.AccessibilityEventProducer;
import org.apache.fop.accessibility.StructureTreeBuilder;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.inline.AbstractTextArea;
import org.apache.fop.area.inline.ForeignObject;
import org.apache.fop.area.inline.Image;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.InlineBlockParent;
import org.apache.fop.area.inline.InlineParent;
import org.apache.fop.area.inline.Leader;
import org.apache.fop.area.inline.Space;
import org.apache.fop.area.inline.SpaceArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.area.inline.Viewport;
import org.apache.fop.area.inline.WordArea;
import org.apache.fop.fo.ElementMappingRegistry;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.util.ColorUtil;
import org.apache.fop.util.ContentHandlerFactory;
import org.apache.fop.util.ContentHandlerFactoryRegistry;
import org.apache.fop.util.ConversionUtils;
import org.apache.fop.util.DefaultErrorListener;
import org.apache.fop.util.DelegatingContentHandler;
import org.apache.fop.util.XMLUtil;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.util.QName;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class AreaTreeParser {
   protected static Log log;
   private static SAXTransformerFactory tFactory;

   public void parse(Source src, AreaTreeModel treeModel, FOUserAgent userAgent) throws TransformerException {
      Transformer transformer = tFactory.newTransformer();
      transformer.setErrorListener(new DefaultErrorListener(log));
      SAXResult res = new SAXResult(this.getContentHandler(treeModel, userAgent));
      transformer.transform(src, res);
   }

   public ContentHandler getContentHandler(AreaTreeModel treeModel, FOUserAgent userAgent) {
      ElementMappingRegistry elementMappingRegistry = userAgent.getFactory().getElementMappingRegistry();
      return new Handler(treeModel, userAgent, elementMappingRegistry);
   }

   static {
      log = LogFactory.getLog(AreaTreeParser.class);
      tFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
   }

   private static class Handler extends DefaultHandler {
      private Map makers = new HashMap();
      private AreaTreeModel treeModel;
      private FOUserAgent userAgent;
      private ElementMappingRegistry elementMappingRegistry;
      private Attributes lastAttributes;
      private CharBuffer content = CharBuffer.allocate(64);
      private boolean ignoreCharacters = true;
      private PageViewport currentPageViewport;
      private Map pageViewportsByKey = new HashMap();
      private Set idFirstsAssigned = new HashSet();
      private Stack areaStack = new Stack();
      private boolean firstFlow;
      private Stack delegateStack = new Stack();
      private ContentHandler delegate;
      private DOMImplementation domImplementation;
      private Locator locator;
      private StructureTreeBuilder structureTreeBuilder;
      private ContentHandler structureTreeBuilderWrapper;
      private Attributes pageSequenceAttributes;
      private static final Object[] SUBSET_COMMON;
      private static final Object[] SUBSET_LINK;
      private static final Object[] SUBSET_COLOR;
      private static final Object[] SUBSET_FONT;
      private static final Object[] SUBSET_BOX;
      private static final Object[] SUBSET_BORDER_PADDING;

      public Handler(AreaTreeModel treeModel, FOUserAgent userAgent, ElementMappingRegistry elementMappingRegistry) {
         this.treeModel = treeModel;
         this.userAgent = userAgent;
         this.elementMappingRegistry = elementMappingRegistry;
         this.makers.put("areaTree", new AreaTreeMaker());
         this.makers.put("page", new PageMaker());
         this.makers.put("pageSequence", new PageSequenceMaker());
         this.makers.put("title", new TitleMaker());
         this.makers.put("pageViewport", new PageViewportMaker());
         this.makers.put("regionViewport", new RegionViewportMaker());
         this.makers.put("regionBefore", new RegionBeforeMaker());
         this.makers.put("regionAfter", new RegionAfterMaker());
         this.makers.put("regionStart", new RegionStartMaker());
         this.makers.put("regionEnd", new RegionEndMaker());
         this.makers.put("regionBody", new RegionBodyMaker());
         this.makers.put("flow", new FlowMaker());
         this.makers.put("mainReference", new MainReferenceMaker());
         this.makers.put("span", new SpanMaker());
         this.makers.put("footnote", new FootnoteMaker());
         this.makers.put("beforeFloat", new BeforeFloatMaker());
         this.makers.put("block", new BlockMaker());
         this.makers.put("lineArea", new LineAreaMaker());
         this.makers.put("inline", new InlineMaker());
         this.makers.put("inlineparent", new InlineParentMaker());
         this.makers.put("inlineblockparent", new InlineBlockParentMaker());
         this.makers.put("text", new TextMaker());
         this.makers.put("word", new WordMaker());
         this.makers.put("space", new SpaceMaker());
         this.makers.put("leader", new LeaderMaker());
         this.makers.put("viewport", new ViewportMaker());
         this.makers.put("image", new ImageMaker());
         this.makers.put("foreignObject", new ForeignObjectMaker());
         this.makers.put("bookmarkTree", new BookmarkTreeMaker());
         this.makers.put("bookmark", new BookmarkMaker());
         this.makers.put("destination", new DestinationMaker());
         if (userAgent.isAccessibilityEnabled()) {
            this.structureTreeBuilder = new StructureTreeBuilder(AreaTreeParser.tFactory);
            userAgent.setStructureTree(this.structureTreeBuilder.getStructureTree());
         }

      }

      private Area findAreaType(Class clazz) {
         if (this.areaStack.size() > 0) {
            int pos = this.areaStack.size() - 1;

            Object obj;
            for(obj = null; pos >= 0 && !clazz.isInstance(obj = this.areaStack.get(pos)); --pos) {
            }

            if (pos >= 0) {
               return (Area)obj;
            }
         }

         return null;
      }

      private RegionViewport getCurrentRegionViewport() {
         return (RegionViewport)this.findAreaType(RegionViewport.class);
      }

      private BodyRegion getCurrentBodyRegion() {
         return (BodyRegion)this.findAreaType(BodyRegion.class);
      }

      private BlockParent getCurrentBlockParent() {
         return (BlockParent)this.findAreaType(BlockParent.class);
      }

      private AbstractTextArea getCurrentText() {
         return (AbstractTextArea)this.findAreaType(AbstractTextArea.class);
      }

      private Viewport getCurrentViewport() {
         return (Viewport)this.findAreaType(Viewport.class);
      }

      public void setDocumentLocator(Locator locator) {
         this.locator = locator;
      }

      private Locator getLocator() {
         return this.locator;
      }

      public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
         if (this.delegate != null) {
            this.delegateStack.push(qName);
            this.delegate.startElement(uri, localName, qName, attributes);
         } else if (this.domImplementation != null) {
            TransformerHandler handler;
            try {
               handler = AreaTreeParser.tFactory.newTransformerHandler();
            } catch (TransformerConfigurationException var8) {
               throw new SAXException("Error creating a new TransformerHandler", var8);
            }

            Document doc = this.domImplementation.createDocument(uri, qName, (DocumentType)null);
            doc.removeChild(doc.getDocumentElement());
            handler.setResult(new DOMResult(doc));
            Area parent = (Area)this.areaStack.peek();
            ((ForeignObject)parent).setDocument(doc);
            this.domImplementation = null;
            this.delegate = handler;
            this.delegateStack.push(qName);
            this.delegate.startDocument();
            this.delegate.startElement(uri, localName, qName, attributes);
         } else {
            boolean handled = true;
            if ("".equals(uri)) {
               if (localName.equals("pageSequence") && this.userAgent.isAccessibilityEnabled()) {
                  this.structureTreeBuilderWrapper = new StructureTreeBuilderWrapper();
                  this.pageSequenceAttributes = new AttributesImpl(attributes);
               } else if (localName.equals("structureTree")) {
                  if (this.userAgent.isAccessibilityEnabled()) {
                     this.delegate = this.structureTreeBuilderWrapper;
                  } else {
                     this.delegate = new DefaultHandler();
                  }

                  this.delegateStack.push(qName);
                  this.delegate.startDocument();
                  this.delegate.startElement(uri, localName, qName, attributes);
               } else {
                  if (this.pageSequenceAttributes != null) {
                     AccessibilityEventProducer.Provider.get(this.userAgent.getEventBroadcaster()).noStructureTreeInXML(this);
                  }

                  handled = this.startAreaTreeElement(localName, attributes);
               }
            } else {
               ContentHandlerFactoryRegistry registry = this.userAgent.getFactory().getContentHandlerFactoryRegistry();
               ContentHandlerFactory factory = registry.getFactory(uri);
               if (factory != null) {
                  this.delegate = factory.createContentHandler();
                  this.delegateStack.push(qName);
                  this.delegate.startDocument();
                  this.delegate.startElement(uri, localName, qName, attributes);
               } else {
                  handled = false;
               }
            }

            if (!handled) {
               if (uri == null || uri.length() == 0) {
                  throw new SAXException("Unhandled element " + localName + " in namespace: " + uri);
               }

               AreaTreeParser.log.warn("Unhandled element " + localName + " in namespace: " + uri);
            }
         }

      }

      private boolean startAreaTreeElement(String localName, Attributes attributes) throws SAXException {
         this.lastAttributes = new AttributesImpl(attributes);
         Maker maker = (Maker)this.makers.get(localName);
         this.content.clear();
         this.ignoreCharacters = true;
         if (maker != null) {
            this.ignoreCharacters = maker.ignoreCharacters();
            maker.startElement(attributes);
         } else if (!"extension-attachments".equals(localName)) {
            return false;
         }

         return true;
      }

      public void endElement(String uri, String localName, String qName) throws SAXException {
         if (this.delegate != null) {
            this.delegate.endElement(uri, localName, qName);
            this.delegateStack.pop();
            if (this.delegateStack.size() == 0) {
               this.delegate.endDocument();
               if (this.delegate instanceof ContentHandlerFactory.ObjectSource) {
                  Object obj = ((ContentHandlerFactory.ObjectSource)this.delegate).getObject();
                  this.handleExternallyGeneratedObject(obj);
               }

               this.delegate = null;
            }
         } else if ("".equals(uri)) {
            Maker maker = (Maker)this.makers.get(localName);
            if (maker != null) {
               maker.endElement();
               this.content.clear();
            }

            this.ignoreCharacters = true;
         }

      }

      private void pushNewRegionReference(Attributes attributes, int side) {
         String regionName = attributes.getValue("name");
         RegionViewport rv = this.getCurrentRegionViewport();
         RegionReference reg = new RegionReference(side, regionName, rv);
         transferForeignObjects(attributes, reg);
         reg.setCTM(getAttributeAsCTM(attributes, "ctm"));
         this.setAreaAttributes(attributes, reg);
         this.setTraits(attributes, reg, SUBSET_BORDER_PADDING);
         rv.setRegionReference(reg);
         this.currentPageViewport.getPage().setRegionViewport(side, rv);
         this.areaStack.push(reg);
      }

      private void assertObjectOfClass(Object obj, Class clazz) {
         if (!clazz.isInstance(obj)) {
            throw new IllegalStateException("Object is not an instance of " + clazz.getName() + " but of " + obj.getClass().getName());
         }
      }

      protected void handleExternallyGeneratedObject(Object obj) {
         if (this.areaStack.size() == 0 && obj instanceof ExtensionAttachment) {
            ExtensionAttachment attachment = (ExtensionAttachment)obj;
            if (this.currentPageViewport == null) {
               this.treeModel.handleOffDocumentItem(new OffDocumentExtensionAttachment(attachment));
            } else {
               this.currentPageViewport.addExtensionAttachment(attachment);
            }
         } else {
            Object o = this.areaStack.peek();
            if (o instanceof AreaTreeObject && obj instanceof ExtensionAttachment) {
               AreaTreeObject ato = (AreaTreeObject)o;
               ExtensionAttachment attachment = (ExtensionAttachment)obj;
               ato.addExtensionAttachment(attachment);
            } else {
               AreaTreeParser.log.warn("Don't know how to handle externally generated object: " + obj);
            }
         }

      }

      private void setAreaAttributes(Attributes attributes, Area area) {
         area.setIPD(Integer.parseInt(attributes.getValue("ipd")));
         area.setBPD(Integer.parseInt(attributes.getValue("bpd")));
      }

      private void setTraits(Attributes attributes, Area area, Object[] traitSubset) {
         int i = traitSubset.length;

         while(true) {
            --i;
            if (i < 0) {
               return;
            }

            Object trait = traitSubset[i];
            String traitName = Trait.getTraitName(trait);
            String value = attributes.getValue(traitName);
            if (value != null) {
               Class cl = Trait.getTraitClass(trait);
               if (cl == Integer.class) {
                  area.addTrait(trait, new Integer(value));
               } else if (cl == Boolean.class) {
                  area.addTrait(trait, Boolean.valueOf(value));
               } else if (cl == String.class) {
                  area.addTrait(trait, value);
                  if (trait == Trait.PROD_ID && !this.idFirstsAssigned.contains(value) && this.currentPageViewport != null) {
                     this.currentPageViewport.setFirstWithID(value);
                     this.idFirstsAssigned.add(value);
                  }
               } else if (cl == Color.class) {
                  try {
                     area.addTrait(trait, ColorUtil.parseColorString(this.userAgent, value));
                  } catch (PropertyException var18) {
                     throw new IllegalArgumentException(var18.getMessage());
                  }
               } else if (cl == Trait.InternalLink.class) {
                  area.addTrait(trait, new Trait.InternalLink(value));
               } else if (cl == Trait.ExternalLink.class) {
                  area.addTrait(trait, Trait.ExternalLink.makeFromTraitValue(value));
               } else if (cl == Trait.Background.class) {
                  Trait.Background bkg = new Trait.Background();

                  try {
                     Color col = ColorUtil.parseColorString(this.userAgent, attributes.getValue("bkg-color"));
                     bkg.setColor(col);
                  } catch (PropertyException var17) {
                     throw new IllegalArgumentException(var17.getMessage());
                  }

                  String uri = attributes.getValue("bkg-img");
                  if (uri != null) {
                     bkg.setURL(uri);

                     ResourceEventProducer eventProducer;
                     try {
                        ImageManager manager = this.userAgent.getFactory().getImageManager();
                        ImageSessionContext sessionContext = this.userAgent.getImageSessionContext();
                        ImageInfo info = manager.getImageInfo(uri, sessionContext);
                        bkg.setImageInfo(info);
                     } catch (ImageException var14) {
                        eventProducer = ResourceEventProducer.Provider.get(this.userAgent.getEventBroadcaster());
                        eventProducer.imageError(this, uri, var14, this.getLocator());
                     } catch (FileNotFoundException var15) {
                        eventProducer = ResourceEventProducer.Provider.get(this.userAgent.getEventBroadcaster());
                        eventProducer.imageNotFound(this, uri, var15, this.getLocator());
                     } catch (IOException var16) {
                        eventProducer = ResourceEventProducer.Provider.get(this.userAgent.getEventBroadcaster());
                        eventProducer.imageIOError(this, uri, var16, this.getLocator());
                     }

                     String repeat = attributes.getValue("bkg-repeat");
                     if (repeat != null) {
                        bkg.setRepeat(repeat);
                     }

                     bkg.setHoriz(XMLUtil.getAttributeAsInt(attributes, "bkg-horz-offset", 0));
                     bkg.setVertical(XMLUtil.getAttributeAsInt(attributes, "bkg-vert-offset", 0));
                  }

                  area.addTrait(trait, bkg);
               } else if (cl == BorderProps.class) {
                  area.addTrait(trait, BorderProps.valueOf(this.userAgent, value));
               }
            } else if (trait == Trait.FONT) {
               String fontName = attributes.getValue("font-name");
               if (fontName != null) {
                  String fontStyle = attributes.getValue("font-style");
                  int fontWeight = XMLUtil.getAttributeAsInt(attributes, "font-weight", 400);
                  area.addTrait(trait, FontInfo.createFontKey(fontName, fontStyle, fontWeight));
               }
            }
         }
      }

      private static CTM getAttributeAsCTM(Attributes attributes, String name) {
         String s = attributes.getValue(name).trim();
         if (s.startsWith("[") && s.endsWith("]")) {
            s = s.substring(1, s.length() - 1);
            double[] values = ConversionUtils.toDoubleArray(s, "\\s");
            if (values.length != 6) {
               throw new IllegalArgumentException("CTM must consist of 6 double values!");
            } else {
               return new CTM(values[0], values[1], values[2], values[3], values[4], values[5]);
            }
         } else {
            throw new IllegalArgumentException("CTM must be surrounded by square brackets!");
         }
      }

      private static void transferForeignObjects(Attributes atts, AreaTreeObject ato) {
         int i = 0;

         for(int c = atts.getLength(); i < c; ++i) {
            String ns = atts.getURI(i);
            if (ns.length() > 0 && !"http://www.w3.org/2000/xmlns/".equals(ns)) {
               QName qname = new QName(ns, atts.getQName(i));
               ato.setForeignAttribute(qname, atts.getValue(i));
            }
         }

      }

      private void setPtr(Area area, Attributes attributes) {
         String ptr = attributes.getValue("ptr");
         if (ptr != null) {
            area.addTrait(Trait.PTR, ptr);
         }

      }

      public void characters(char[] ch, int start, int length) throws SAXException {
         if (this.delegate != null) {
            this.delegate.characters(ch, start, length);
         } else if (!this.ignoreCharacters) {
            int maxLength = this.content.capacity() - this.content.position();
            if (maxLength < length) {
               CharBuffer newContent = CharBuffer.allocate(this.content.position() + length);
               this.content.flip();
               newContent.put(this.content);
               this.content = newContent;
            }

            this.content.limit(this.content.capacity());
            this.content.put(ch, start, length);
            if (this.content.position() < this.content.limit()) {
               this.content.limit(this.content.position());
            }
         }

      }

      static {
         SUBSET_COMMON = new Object[]{Trait.PROD_ID};
         SUBSET_LINK = new Object[]{Trait.INTERNAL_LINK, Trait.EXTERNAL_LINK};
         SUBSET_COLOR = new Object[]{Trait.BACKGROUND, Trait.COLOR};
         SUBSET_FONT = new Object[]{Trait.FONT, Trait.FONT_SIZE, Trait.BLINK, Trait.OVERLINE, Trait.OVERLINE_COLOR, Trait.LINETHROUGH, Trait.LINETHROUGH_COLOR, Trait.UNDERLINE, Trait.UNDERLINE_COLOR};
         SUBSET_BOX = new Object[]{Trait.BORDER_BEFORE, Trait.BORDER_AFTER, Trait.BORDER_START, Trait.BORDER_END, Trait.SPACE_BEFORE, Trait.SPACE_AFTER, Trait.SPACE_START, Trait.SPACE_END, Trait.PADDING_BEFORE, Trait.PADDING_AFTER, Trait.PADDING_START, Trait.PADDING_END, Trait.START_INDENT, Trait.END_INDENT, Trait.IS_REFERENCE_AREA, Trait.IS_VIEWPORT_AREA};
         SUBSET_BORDER_PADDING = new Object[]{Trait.BORDER_BEFORE, Trait.BORDER_AFTER, Trait.BORDER_START, Trait.BORDER_END, Trait.PADDING_BEFORE, Trait.PADDING_AFTER, Trait.PADDING_START, Trait.PADDING_END};
      }

      private class DestinationMaker extends AbstractMaker {
         private DestinationMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            String[] linkdata = Trait.InternalLink.parseXMLAttribute(Handler.this.lastAttributes.getValue("internal-link"));
            PageViewport pv = (PageViewport)Handler.this.pageViewportsByKey.get(linkdata[0]);
            DestinationData dest = new DestinationData(linkdata[1]);
            List pages = new ArrayList();
            pages.add(pv);
            dest.resolveIDRef(linkdata[1], pages);
            Handler.this.areaStack.push(dest);
         }

         public void endElement() {
            Object tos = Handler.this.areaStack.pop();
            Handler.this.assertObjectOfClass(tos, DestinationData.class);
            Handler.this.treeModel.handleOffDocumentItem((DestinationData)tos);
         }

         // $FF: synthetic method
         DestinationMaker(Object x1) {
            this();
         }
      }

      private class BookmarkMaker extends AbstractMaker {
         private BookmarkMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            String title = attributes.getValue("title");
            boolean showChildren = XMLUtil.getAttributeAsBoolean(attributes, "show-children", false);
            String[] linkdata = Trait.InternalLink.parseXMLAttribute(attributes.getValue("internal-link"));
            PageViewport pv = (PageViewport)Handler.this.pageViewportsByKey.get(linkdata[0]);
            BookmarkData bm = new BookmarkData(title, showChildren, pv, linkdata[1]);
            Object tos = Handler.this.areaStack.peek();
            if (tos instanceof BookmarkData) {
               BookmarkData parent = (BookmarkData)tos;
               parent.addSubData(bm);
            }

            Handler.this.areaStack.push(bm);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), BookmarkData.class);
         }

         // $FF: synthetic method
         BookmarkMaker(Object x1) {
            this();
         }
      }

      private class BookmarkTreeMaker extends AbstractMaker {
         private BookmarkTreeMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            BookmarkData bm = new BookmarkData();
            Handler.this.areaStack.push(bm);
         }

         public void endElement() {
            Object tos = Handler.this.areaStack.pop();
            Handler.this.assertObjectOfClass(tos, BookmarkData.class);
            Handler.this.treeModel.handleOffDocumentItem((BookmarkData)tos);
         }

         // $FF: synthetic method
         BookmarkTreeMaker(Object x1) {
            this();
         }
      }

      private class ForeignObjectMaker extends AbstractMaker {
         private ForeignObjectMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) throws SAXException {
            String ns = attributes.getValue("ns");
            Handler.this.domImplementation = Handler.this.elementMappingRegistry.getDOMImplementationForNamespace(ns);
            if (Handler.this.domImplementation == null) {
               throw new SAXException("No DOMImplementation could be identified to handle namespace: " + ns);
            } else {
               ForeignObject foreign = new ForeignObject(ns);
               AreaTreeParser.Handler.transferForeignObjects(attributes, foreign);
               Handler.this.setAreaAttributes(attributes, foreign);
               Handler.this.setTraits(attributes, foreign, AreaTreeParser.Handler.SUBSET_COMMON);
               Handler.this.getCurrentViewport().setContent(foreign);
               Handler.this.areaStack.push(foreign);
            }
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), ForeignObject.class);
         }

         // $FF: synthetic method
         ForeignObjectMaker(Object x1) {
            this();
         }
      }

      private class ImageMaker extends AbstractMaker {
         private ImageMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            String url = attributes.getValue("url");
            Image image = new Image(url);
            AreaTreeParser.Handler.transferForeignObjects(attributes, image);
            Handler.this.setAreaAttributes(attributes, image);
            Handler.this.setTraits(attributes, image, AreaTreeParser.Handler.SUBSET_COMMON);
            Handler.this.setPtr(image, attributes);
            Handler.this.getCurrentViewport().setContent(image);
         }

         // $FF: synthetic method
         ImageMaker(Object x1) {
            this();
         }
      }

      private class ViewportMaker extends AbstractMaker {
         private ViewportMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            Viewport viewport = new Viewport((Area)null);
            AreaTreeParser.Handler.transferForeignObjects(attributes, viewport);
            Handler.this.setAreaAttributes(attributes, viewport);
            Handler.this.setTraits(attributes, viewport, AreaTreeParser.Handler.SUBSET_COMMON);
            Handler.this.setTraits(attributes, viewport, AreaTreeParser.Handler.SUBSET_BOX);
            Handler.this.setTraits(attributes, viewport, AreaTreeParser.Handler.SUBSET_COLOR);
            viewport.setContentPosition(XMLUtil.getAttributeAsRectangle2D(attributes, "pos"));
            viewport.setClip(XMLUtil.getAttributeAsBoolean(attributes, "clip", false));
            viewport.setOffset(XMLUtil.getAttributeAsInt(attributes, "offset", 0));
            Handler.this.setPtr(viewport, attributes);
            Area parent = (Area)Handler.this.areaStack.peek();
            parent.addChildArea(viewport);
            Handler.this.areaStack.push(viewport);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), Viewport.class);
         }

         // $FF: synthetic method
         ViewportMaker(Object x1) {
            this();
         }
      }

      private class LeaderMaker extends AbstractMaker {
         private LeaderMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            Leader leader = new Leader();
            AreaTreeParser.Handler.transferForeignObjects(attributes, leader);
            Handler.this.setAreaAttributes(attributes, leader);
            Handler.this.setTraits(attributes, leader, AreaTreeParser.Handler.SUBSET_COMMON);
            Handler.this.setTraits(attributes, leader, AreaTreeParser.Handler.SUBSET_BOX);
            Handler.this.setTraits(attributes, leader, AreaTreeParser.Handler.SUBSET_COLOR);
            Handler.this.setTraits(attributes, leader, AreaTreeParser.Handler.SUBSET_FONT);
            leader.setOffset(XMLUtil.getAttributeAsInt(attributes, "offset", 0));
            String ruleStyle = attributes.getValue("ruleStyle");
            if (ruleStyle != null) {
               leader.setRuleStyle(ruleStyle);
            }

            leader.setRuleThickness(XMLUtil.getAttributeAsInt(attributes, "ruleThickness", 0));
            Area parent = (Area)Handler.this.areaStack.peek();
            parent.addChildArea(leader);
         }

         // $FF: synthetic method
         LeaderMaker(Object x1) {
            this();
         }
      }

      private class SpaceMaker extends AbstractMaker {
         private SpaceMaker() {
            super(null);
         }

         public void endElement() {
            int offset = XMLUtil.getAttributeAsInt(Handler.this.lastAttributes, "offset", 0);
            if (Handler.this.content.position() > 0) {
               Handler.this.content.flip();
               boolean adjustable = XMLUtil.getAttributeAsBoolean(Handler.this.lastAttributes, "adj", true);
               SpaceArea space = new SpaceArea(Handler.this.content.charAt(0), offset, adjustable);
               AbstractTextArea text = Handler.this.getCurrentText();
               space.setParentArea(text);
               text.addChildArea(space);
            } else {
               Space spacex = new Space();
               Handler.this.setAreaAttributes(Handler.this.lastAttributes, spacex);
               Handler.this.setTraits(Handler.this.lastAttributes, spacex, AreaTreeParser.Handler.SUBSET_COMMON);
               Handler.this.setTraits(Handler.this.lastAttributes, spacex, AreaTreeParser.Handler.SUBSET_BOX);
               Handler.this.setTraits(Handler.this.lastAttributes, spacex, AreaTreeParser.Handler.SUBSET_COLOR);
               spacex.setOffset(offset);
               Area parent = (Area)Handler.this.areaStack.peek();
               parent.addChildArea(spacex);
            }

         }

         public boolean ignoreCharacters() {
            return false;
         }

         // $FF: synthetic method
         SpaceMaker(Object x1) {
            this();
         }
      }

      private class WordMaker extends AbstractMaker {
         private WordMaker() {
            super(null);
         }

         public void endElement() {
            int offset = XMLUtil.getAttributeAsInt(Handler.this.lastAttributes, "offset", 0);
            int[] letterAdjust = ConversionUtils.toIntArray(Handler.this.lastAttributes.getValue("letter-adjust"), "\\s");
            Handler.this.content.flip();
            WordArea word = new WordArea(Handler.this.content.toString().trim(), offset, letterAdjust);
            AbstractTextArea text = Handler.this.getCurrentText();
            word.setParentArea(text);
            text.addChildArea(word);
         }

         public boolean ignoreCharacters() {
            return false;
         }

         // $FF: synthetic method
         WordMaker(Object x1) {
            this();
         }
      }

      private class TextMaker extends AbstractMaker {
         private TextMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            if (Handler.this.getCurrentText() != null) {
               throw new IllegalStateException("Current Text must be null");
            } else {
               TextArea text = new TextArea();
               Handler.this.setAreaAttributes(attributes, text);
               Handler.this.setTraits(attributes, text, AreaTreeParser.Handler.SUBSET_COMMON);
               Handler.this.setTraits(attributes, text, AreaTreeParser.Handler.SUBSET_BOX);
               Handler.this.setTraits(attributes, text, AreaTreeParser.Handler.SUBSET_COLOR);
               Handler.this.setTraits(attributes, text, AreaTreeParser.Handler.SUBSET_FONT);
               text.setBaselineOffset(XMLUtil.getAttributeAsInt(attributes, "baseline", 0));
               text.setOffset(XMLUtil.getAttributeAsInt(attributes, "offset", 0));
               text.setTextLetterSpaceAdjust(XMLUtil.getAttributeAsInt(attributes, "tlsadjust", 0));
               text.setTextWordSpaceAdjust(XMLUtil.getAttributeAsInt(attributes, "twsadjust", 0));
               Handler.this.setPtr(text, attributes);
               Area parent = (Area)Handler.this.areaStack.peek();
               parent.addChildArea(text);
               Handler.this.areaStack.push(text);
            }
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), TextArea.class);
         }

         // $FF: synthetic method
         TextMaker(Object x1) {
            this();
         }
      }

      private class InlineBlockParentMaker extends AbstractMaker {
         private InlineBlockParentMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            InlineBlockParent ibp = new InlineBlockParent();
            AreaTreeParser.Handler.transferForeignObjects(attributes, ibp);
            ibp.setOffset(XMLUtil.getAttributeAsInt(attributes, "offset", 0));
            Handler.this.setAreaAttributes(attributes, ibp);
            Handler.this.setTraits(attributes, ibp, AreaTreeParser.Handler.SUBSET_COMMON);
            Handler.this.setTraits(attributes, ibp, AreaTreeParser.Handler.SUBSET_BOX);
            Handler.this.setTraits(attributes, ibp, AreaTreeParser.Handler.SUBSET_COLOR);
            Area parent = (Area)Handler.this.areaStack.peek();
            parent.addChildArea(ibp);
            Handler.this.areaStack.push(ibp);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), InlineBlockParent.class);
         }

         // $FF: synthetic method
         InlineBlockParentMaker(Object x1) {
            this();
         }
      }

      private class InlineParentMaker extends AbstractMaker {
         private InlineParentMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            InlineParent ip = new InlineParent();
            AreaTreeParser.Handler.transferForeignObjects(attributes, ip);
            ip.setOffset(XMLUtil.getAttributeAsInt(attributes, "offset", 0));
            Handler.this.setAreaAttributes(attributes, ip);
            Handler.this.setTraits(attributes, ip, AreaTreeParser.Handler.SUBSET_COMMON);
            Handler.this.setTraits(attributes, ip, AreaTreeParser.Handler.SUBSET_BOX);
            Handler.this.setTraits(attributes, ip, AreaTreeParser.Handler.SUBSET_COLOR);
            Handler.this.setTraits(attributes, ip, AreaTreeParser.Handler.SUBSET_LINK);
            Handler.this.setPtr(ip, attributes);
            Area parent = (Area)Handler.this.areaStack.peek();
            parent.addChildArea(ip);
            Handler.this.areaStack.push(ip);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), InlineParent.class);
         }

         // $FF: synthetic method
         InlineParentMaker(Object x1) {
            this();
         }
      }

      private class InlineMaker extends AbstractMaker {
         private InlineMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            InlineArea inl = new InlineArea();
            AreaTreeParser.Handler.transferForeignObjects(attributes, inl);
            inl.setOffset(XMLUtil.getAttributeAsInt(attributes, "offset", 0));
            Handler.this.setAreaAttributes(attributes, inl);
            Handler.this.setTraits(attributes, inl, AreaTreeParser.Handler.SUBSET_COMMON);
            Handler.this.setTraits(attributes, inl, AreaTreeParser.Handler.SUBSET_BOX);
            Handler.this.setTraits(attributes, inl, AreaTreeParser.Handler.SUBSET_COLOR);
            Area parent = (Area)Handler.this.areaStack.peek();
            parent.addChildArea(inl);
            Handler.this.areaStack.push(inl);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), InlineArea.class);
         }

         // $FF: synthetic method
         InlineMaker(Object x1) {
            this();
         }
      }

      private class LineAreaMaker extends AbstractMaker {
         private LineAreaMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            LineArea line = new LineArea();
            Handler.this.setAreaAttributes(attributes, line);
            Handler.this.setTraits(attributes, line, AreaTreeParser.Handler.SUBSET_COMMON);
            Handler.this.setTraits(attributes, line, AreaTreeParser.Handler.SUBSET_BOX);
            Handler.this.setTraits(attributes, line, AreaTreeParser.Handler.SUBSET_COLOR);
            BlockParent parent = Handler.this.getCurrentBlockParent();
            parent.addChildArea(line);
            Handler.this.areaStack.push(line);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), LineArea.class);
         }

         // $FF: synthetic method
         LineAreaMaker(Object x1) {
            this();
         }
      }

      private class BlockMaker extends AbstractMaker {
         private BlockMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            boolean isViewport = XMLUtil.getAttributeAsBoolean(attributes, "is-viewport-area", false);
            Object block;
            if (isViewport) {
               BlockViewport bv = new BlockViewport();
               bv.setClip(XMLUtil.getAttributeAsBoolean(attributes, "clipped", false));
               bv.setCTM(AreaTreeParser.Handler.getAttributeAsCTM(attributes, "ctm"));
               if (bv.getPositioning() != 1) {
                  bv.setXOffset(XMLUtil.getAttributeAsInt(attributes, "left-position", 0));
                  bv.setYOffset(XMLUtil.getAttributeAsInt(attributes, "top-position", 0));
               }

               block = bv;
            } else {
               block = new Block();
            }

            String positioning = attributes.getValue("positioning");
            if ("absolute".equalsIgnoreCase(positioning)) {
               ((Block)block).setPositioning(2);
            } else if ("fixed".equalsIgnoreCase(positioning)) {
               ((Block)block).setPositioning(3);
            } else if ("relative".equalsIgnoreCase(positioning)) {
               ((Block)block).setPositioning(1);
            } else {
               ((Block)block).setPositioning(0);
            }

            if (attributes.getValue("left-offset") != null) {
               ((Block)block).setXOffset(XMLUtil.getAttributeAsInt(attributes, "left-offset", 0));
            }

            if (attributes.getValue("top-offset") != null) {
               ((Block)block).setYOffset(XMLUtil.getAttributeAsInt(attributes, "top-offset", 0));
            }

            AreaTreeParser.Handler.transferForeignObjects(attributes, (AreaTreeObject)block);
            Handler.this.setAreaAttributes(attributes, (Area)block);
            Handler.this.setTraits(attributes, (Area)block, AreaTreeParser.Handler.SUBSET_COMMON);
            Handler.this.setTraits(attributes, (Area)block, AreaTreeParser.Handler.SUBSET_BOX);
            Handler.this.setTraits(attributes, (Area)block, AreaTreeParser.Handler.SUBSET_COLOR);
            Area parent = (Area)Handler.this.areaStack.peek();
            parent.addChildArea((Area)block);
            Handler.this.areaStack.push(block);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), Block.class);
         }

         // $FF: synthetic method
         BlockMaker(Object x1) {
            this();
         }
      }

      private class BeforeFloatMaker extends AbstractMaker {
         private BeforeFloatMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            BeforeFloat bf = Handler.this.getCurrentBodyRegion().getBeforeFloat();
            AreaTreeParser.Handler.transferForeignObjects(attributes, bf);
            Handler.this.areaStack.push(bf);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), BeforeFloat.class);
         }

         // $FF: synthetic method
         BeforeFloatMaker(Object x1) {
            this();
         }
      }

      private class FootnoteMaker extends AbstractMaker {
         private FootnoteMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            Footnote fn = Handler.this.getCurrentBodyRegion().getFootnote();
            AreaTreeParser.Handler.transferForeignObjects(attributes, fn);
            fn.setTop(XMLUtil.getAttributeAsInt(attributes, "top-offset", 0));
            Handler.this.areaStack.push(fn);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), Footnote.class);
         }

         // $FF: synthetic method
         FootnoteMaker(Object x1) {
            this();
         }
      }

      private class SpanMaker extends AbstractMaker {
         private SpanMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            int ipd = XMLUtil.getAttributeAsInt(attributes, "ipd", 0);
            int columnCount = XMLUtil.getAttributeAsInt(attributes, "columnCount", 1);
            BodyRegion body = Handler.this.getCurrentBodyRegion();
            Span span = new Span(columnCount, body.getColumnGap(), ipd);
            AreaTreeParser.Handler.transferForeignObjects(attributes, span);
            Handler.this.setAreaAttributes(attributes, span);
            body.getMainReference().getSpans().add(span);
            Handler.this.firstFlow = true;
         }

         // $FF: synthetic method
         SpanMaker(Object x1) {
            this();
         }
      }

      private class MainReferenceMaker extends AbstractMaker {
         private MainReferenceMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            MainReference mr = Handler.this.getCurrentBodyRegion().getMainReference();
            AreaTreeParser.Handler.transferForeignObjects(attributes, mr);
            Handler.this.setAreaAttributes(attributes, mr);
         }

         // $FF: synthetic method
         MainReferenceMaker(Object x1) {
            this();
         }
      }

      private class FlowMaker extends AbstractMaker {
         private FlowMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            BodyRegion body = Handler.this.getCurrentBodyRegion();
            if (!Handler.this.firstFlow) {
               body.getMainReference().getCurrentSpan().moveToNextFlow();
            } else {
               Handler.this.firstFlow = false;
            }

            NormalFlow flow = body.getMainReference().getCurrentSpan().getCurrentFlow();
            AreaTreeParser.Handler.transferForeignObjects(attributes, flow);
            Handler.this.setAreaAttributes(attributes, flow);
            Handler.this.areaStack.push(flow);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), NormalFlow.class);
         }

         // $FF: synthetic method
         FlowMaker(Object x1) {
            this();
         }
      }

      private class RegionBodyMaker extends AbstractMaker {
         private RegionBodyMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            BodyRegion body = Handler.this.getCurrentBodyRegion();
            if (body != null) {
               throw new IllegalStateException("Current BodyRegion must be null");
            } else {
               String regionName = attributes.getValue("name");
               int columnCount = XMLUtil.getAttributeAsInt(attributes, "columnCount", 1);
               int columnGap = XMLUtil.getAttributeAsInt(attributes, "columnGap", 0);
               RegionViewport rv = Handler.this.getCurrentRegionViewport();
               body = new BodyRegion(58, regionName, rv, columnCount, columnGap);
               AreaTreeParser.Handler.transferForeignObjects(attributes, body);
               body.setCTM(AreaTreeParser.Handler.getAttributeAsCTM(attributes, "ctm"));
               Handler.this.setAreaAttributes(attributes, body);
               Handler.this.setTraits(attributes, body, AreaTreeParser.Handler.SUBSET_BORDER_PADDING);
               rv.setRegionReference(body);
               Handler.this.currentPageViewport.getPage().setRegionViewport(58, rv);
               Handler.this.areaStack.push(body);
            }
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), BodyRegion.class);
         }

         // $FF: synthetic method
         RegionBodyMaker(Object x1) {
            this();
         }
      }

      private class RegionEndMaker extends AbstractMaker {
         private RegionEndMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            Handler.this.pushNewRegionReference(attributes, 59);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), RegionReference.class);
         }

         // $FF: synthetic method
         RegionEndMaker(Object x1) {
            this();
         }
      }

      private class RegionStartMaker extends AbstractMaker {
         private RegionStartMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            Handler.this.pushNewRegionReference(attributes, 61);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), RegionReference.class);
         }

         // $FF: synthetic method
         RegionStartMaker(Object x1) {
            this();
         }
      }

      private class RegionAfterMaker extends AbstractMaker {
         private RegionAfterMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            Handler.this.pushNewRegionReference(attributes, 56);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), RegionReference.class);
         }

         // $FF: synthetic method
         RegionAfterMaker(Object x1) {
            this();
         }
      }

      private class RegionBeforeMaker extends AbstractMaker {
         private RegionBeforeMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            Handler.this.pushNewRegionReference(attributes, 57);
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), RegionReference.class);
         }

         // $FF: synthetic method
         RegionBeforeMaker(Object x1) {
            this();
         }
      }

      private class RegionViewportMaker extends AbstractMaker {
         private RegionViewportMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            RegionViewport rv = Handler.this.getCurrentRegionViewport();
            if (rv != null) {
               throw new IllegalStateException("Current RegionViewport must be null");
            } else {
               Rectangle2D viewArea = XMLUtil.getAttributeAsRectangle2D(attributes, "rect");
               rv = new RegionViewport(viewArea);
               AreaTreeParser.Handler.transferForeignObjects(attributes, rv);
               rv.setClip(XMLUtil.getAttributeAsBoolean(attributes, "clipped", false));
               Handler.this.setAreaAttributes(attributes, rv);
               Handler.this.setTraits(attributes, rv, AreaTreeParser.Handler.SUBSET_COMMON);
               Handler.this.setTraits(attributes, rv, AreaTreeParser.Handler.SUBSET_BOX);
               Handler.this.setTraits(attributes, rv, AreaTreeParser.Handler.SUBSET_COLOR);
               Handler.this.areaStack.push(rv);
            }
         }

         public void endElement() {
            Handler.this.assertObjectOfClass(Handler.this.areaStack.pop(), RegionViewport.class);
         }

         // $FF: synthetic method
         RegionViewportMaker(Object x1) {
            this();
         }
      }

      private class PageMaker extends AbstractMaker {
         private PageMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            Page p = new Page();
            Handler.this.currentPageViewport.setPage(p);
         }

         public void endElement() {
            Handler.this.treeModel.addPage(Handler.this.currentPageViewport);
            Handler.this.currentPageViewport = null;
         }

         // $FF: synthetic method
         PageMaker(Object x1) {
            this();
         }
      }

      private class PageViewportMaker extends AbstractMaker {
         private PageViewportMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            if (!Handler.this.areaStack.isEmpty()) {
               PageSequence pageSequence = (PageSequence)Handler.this.areaStack.peek();
               Handler.this.treeModel.startPageSequence(pageSequence);
               Handler.this.areaStack.pop();
            }

            if (Handler.this.currentPageViewport != null) {
               throw new IllegalStateException("currentPageViewport must be null");
            } else {
               Rectangle viewArea = XMLUtil.getAttributeAsRectangle(attributes, "bounds");
               int pageNumber = XMLUtil.getAttributeAsInt(attributes, "nr", -1);
               String key = attributes.getValue("key");
               String pageNumberString = attributes.getValue("formatted-nr");
               String pageMaster = attributes.getValue("simple-page-master-name");
               boolean blank = XMLUtil.getAttributeAsBoolean(attributes, "blank", false);
               Handler.this.currentPageViewport = new PageViewport(viewArea, pageNumber, pageNumberString, pageMaster, blank);
               AreaTreeParser.Handler.transferForeignObjects(attributes, Handler.this.currentPageViewport);
               Handler.this.currentPageViewport.setKey(key);
               Handler.this.pageViewportsByKey.put(key, Handler.this.currentPageViewport);
            }
         }

         // $FF: synthetic method
         PageViewportMaker(Object x1) {
            this();
         }
      }

      private class TitleMaker extends AbstractMaker {
         private TitleMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            LineArea line = new LineArea();
            AreaTreeParser.Handler.transferForeignObjects(attributes, line);
            Handler.this.areaStack.push(line);
         }

         public void endElement() {
            LineArea line = (LineArea)Handler.this.areaStack.pop();
            PageSequence pageSequence = (PageSequence)Handler.this.areaStack.peek();
            pageSequence.setTitle(line);
         }

         // $FF: synthetic method
         TitleMaker(Object x1) {
            this();
         }
      }

      private class PageSequenceMaker extends AbstractMaker {
         private PageSequenceMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            PageSequence pageSequence = new PageSequence((LineArea)null);
            String lang = attributes.getValue("language");
            pageSequence.setLanguage(lang);
            String country = attributes.getValue("country");
            pageSequence.setCountry(country);
            AreaTreeParser.Handler.transferForeignObjects(attributes, pageSequence);
            Handler.this.areaStack.push(pageSequence);
         }

         // $FF: synthetic method
         PageSequenceMaker(Object x1) {
            this();
         }
      }

      private class AreaTreeMaker extends AbstractMaker {
         private AreaTreeMaker() {
            super(null);
         }

         public void startElement(Attributes attributes) {
            Handler.this.idFirstsAssigned.clear();
         }

         // $FF: synthetic method
         AreaTreeMaker(Object x1) {
            this();
         }
      }

      private abstract class AbstractMaker implements Maker {
         private AbstractMaker() {
         }

         public void startElement(Attributes attributes) throws SAXException {
         }

         public void endElement() {
         }

         public boolean ignoreCharacters() {
            return true;
         }

         // $FF: synthetic method
         AbstractMaker(Object x1) {
            this();
         }
      }

      private interface Maker {
         void startElement(Attributes var1) throws SAXException;

         void endElement();

         boolean ignoreCharacters();
      }

      private final class StructureTreeBuilderWrapper extends DelegatingContentHandler {
         private StructureTreeBuilderWrapper() throws SAXException {
            super(Handler.this.structureTreeBuilder.getHandlerForNextPageSequence());
         }

         public void endDocument() throws SAXException {
            super.endDocument();
            Handler.this.startAreaTreeElement("pageSequence", Handler.this.pageSequenceAttributes);
            Handler.this.pageSequenceAttributes = null;
         }

         // $FF: synthetic method
         StructureTreeBuilderWrapper(Object x1) throws SAXException {
            this();
         }
      }
   }
}
