package org.apache.fop.render.intermediate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.fop.accessibility.StructureTree;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.render.PrintRendererConfigurator;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.render.intermediate.extensions.AbstractAction;
import org.apache.fop.render.intermediate.extensions.Bookmark;
import org.apache.fop.render.intermediate.extensions.BookmarkTree;
import org.apache.fop.render.intermediate.extensions.DocumentNavigationExtensionConstants;
import org.apache.fop.render.intermediate.extensions.Link;
import org.apache.fop.render.intermediate.extensions.NamedDestination;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.RuleStyle;
import org.apache.fop.util.ColorUtil;
import org.apache.fop.util.DOM2SAX;
import org.apache.fop.util.XMLConstants;
import org.apache.fop.util.XMLUtil;
import org.apache.xmlgraphics.util.QName;
import org.apache.xmlgraphics.util.XMLizable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class IFSerializer extends AbstractXMLWritingIFDocumentHandler implements IFConstants, IFPainter, IFDocumentNavigationHandler {
   private IFDocumentHandler mimicHandler;
   private int pageSequenceIndex;
   private IFState state;
   private Map incompleteActions = new HashMap();
   private List completeActions = new LinkedList();
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   protected String getMainNamespace() {
      return "http://xmlgraphics.apache.org/fop/intermediate";
   }

   public boolean supportsPagesOutOfOrder() {
      return false;
   }

   public String getMimeType() {
      return "application/X-fop-intermediate-format";
   }

   public IFDocumentHandlerConfigurator getConfigurator() {
      return (IFDocumentHandlerConfigurator)(this.mimicHandler != null ? this.getMimickedDocumentHandler().getConfigurator() : new PrintRendererConfigurator(this.getUserAgent()));
   }

   public IFDocumentNavigationHandler getDocumentNavigationHandler() {
      return this;
   }

   public void mimicDocumentHandler(IFDocumentHandler targetHandler) {
      this.mimicHandler = targetHandler;
   }

   public IFDocumentHandler getMimickedDocumentHandler() {
      return this.mimicHandler;
   }

   public FontInfo getFontInfo() {
      return this.mimicHandler != null ? this.mimicHandler.getFontInfo() : null;
   }

   public void setFontInfo(FontInfo fontInfo) {
      if (this.mimicHandler != null) {
         this.mimicHandler.setFontInfo(fontInfo);
      }

   }

   public void setDefaultFontInfo(FontInfo fontInfo) {
      if (this.mimicHandler != null) {
         this.mimicHandler.setDefaultFontInfo(fontInfo);
      }

   }

   public void startDocument() throws IFException {
      super.startDocument();

      try {
         this.handler.startDocument();
         this.handler.startPrefixMapping("", "http://xmlgraphics.apache.org/fop/intermediate");
         this.handler.startPrefixMapping("xlink", "http://www.w3.org/1999/xlink");
         this.handler.startPrefixMapping("nav", "http://xmlgraphics.apache.org/fop/intermediate/document-navigation");
         this.handler.startElement("document");
      } catch (SAXException var2) {
         throw new IFException("SAX error in startDocument()", var2);
      }
   }

   public void startDocumentHeader() throws IFException {
      try {
         this.handler.startElement("header");
      } catch (SAXException var2) {
         throw new IFException("SAX error in startDocumentHeader()", var2);
      }
   }

   public void endDocumentHeader() throws IFException {
      try {
         this.handler.endElement("header");
      } catch (SAXException var2) {
         throw new IFException("SAX error in startDocumentHeader()", var2);
      }
   }

   public void startDocumentTrailer() throws IFException {
      try {
         this.handler.startElement("trailer");
      } catch (SAXException var2) {
         throw new IFException("SAX error in startDocumentTrailer()", var2);
      }
   }

   public void endDocumentTrailer() throws IFException {
      try {
         this.handler.endElement("trailer");
      } catch (SAXException var2) {
         throw new IFException("SAX error in endDocumentTrailer()", var2);
      }
   }

   public void endDocument() throws IFException {
      try {
         this.handler.endElement("document");
         this.handler.endDocument();
         this.finishDocumentNavigation();
      } catch (SAXException var2) {
         throw new IFException("SAX error in endDocument()", var2);
      }
   }

   public void startPageSequence(String id) throws IFException {
      try {
         AttributesImpl atts = new AttributesImpl();
         if (id != null) {
            atts.addAttribute("http://www.w3.org/XML/1998/namespace", "id", "xml:id", "CDATA", id);
         }

         Locale lang = this.getContext().getLanguage();
         if (lang != null) {
            atts.addAttribute("http://www.w3.org/XML/1998/namespace", "lang", "xml:lang", "CDATA", XMLUtil.toRFC3066(lang));
         }

         XMLUtil.addAttribute(atts, XMLConstants.XML_SPACE, "preserve");
         this.addForeignAttributes(atts);
         this.handler.startElement((String)"page-sequence", atts);
         if (this.getUserAgent().isAccessibilityEnabled()) {
            StructureTree structureTree = this.getUserAgent().getStructureTree();
            this.handler.startElement("structure-tree");
            NodeList nodes = structureTree.getPageSequence(this.pageSequenceIndex++);
            int i = 0;

            for(int n = nodes.getLength(); i < n; ++i) {
               Node node = nodes.item(i);
               (new DOM2SAX(this.handler)).writeFragment(node);
            }

            this.handler.endElement("structure-tree");
         }

      } catch (SAXException var9) {
         throw new IFException("SAX error in startPageSequence()", var9);
      }
   }

   public void endPageSequence() throws IFException {
      try {
         this.handler.endElement("page-sequence");
      } catch (SAXException var2) {
         throw new IFException("SAX error in endPageSequence()", var2);
      }
   }

   public void startPage(int index, String name, String pageMasterName, Dimension size) throws IFException {
      try {
         AttributesImpl atts = new AttributesImpl();
         this.addAttribute(atts, "index", Integer.toString(index));
         this.addAttribute(atts, "name", name);
         this.addAttribute(atts, "page-master-name", pageMasterName);
         this.addAttribute(atts, "width", Integer.toString(size.width));
         this.addAttribute(atts, "height", Integer.toString(size.height));
         this.addForeignAttributes(atts);
         this.handler.startElement((String)"page", atts);
      } catch (SAXException var6) {
         throw new IFException("SAX error in startPage()", var6);
      }
   }

   public void startPageHeader() throws IFException {
      try {
         this.handler.startElement("page-header");
      } catch (SAXException var2) {
         throw new IFException("SAX error in startPageHeader()", var2);
      }
   }

   public void endPageHeader() throws IFException {
      try {
         this.handler.endElement("page-header");
      } catch (SAXException var2) {
         throw new IFException("SAX error in endPageHeader()", var2);
      }
   }

   public IFPainter startPageContent() throws IFException {
      try {
         this.handler.startElement("content");
         this.state = IFState.create();
         return this;
      } catch (SAXException var2) {
         throw new IFException("SAX error in startPageContent()", var2);
      }
   }

   public void endPageContent() throws IFException {
      try {
         this.state = null;
         this.handler.endElement("content");
      } catch (SAXException var2) {
         throw new IFException("SAX error in endPageContent()", var2);
      }
   }

   public void startPageTrailer() throws IFException {
      try {
         this.handler.startElement("page-trailer");
      } catch (SAXException var2) {
         throw new IFException("SAX error in startPageTrailer()", var2);
      }
   }

   public void endPageTrailer() throws IFException {
      try {
         this.commitNavigation();
         this.handler.endElement("page-trailer");
      } catch (SAXException var2) {
         throw new IFException("SAX error in endPageTrailer()", var2);
      }
   }

   public void endPage() throws IFException {
      try {
         this.handler.endElement("page");
      } catch (SAXException var2) {
         throw new IFException("SAX error in endPage()", var2);
      }
   }

   public void startViewport(AffineTransform transform, Dimension size, Rectangle clipRect) throws IFException {
      this.startViewport(IFUtil.toString(transform), size, clipRect);
   }

   public void startViewport(AffineTransform[] transforms, Dimension size, Rectangle clipRect) throws IFException {
      this.startViewport(IFUtil.toString(transforms), size, clipRect);
   }

   private void startViewport(String transform, Dimension size, Rectangle clipRect) throws IFException {
      try {
         AttributesImpl atts = new AttributesImpl();
         if (transform != null && transform.length() > 0) {
            this.addAttribute(atts, "transform", transform);
         }

         this.addAttribute(atts, "width", Integer.toString(size.width));
         this.addAttribute(atts, "height", Integer.toString(size.height));
         if (clipRect != null) {
            this.addAttribute(atts, "clip-rect", IFUtil.toString(clipRect));
         }

         this.handler.startElement((String)"viewport", atts);
      } catch (SAXException var5) {
         throw new IFException("SAX error in startViewport()", var5);
      }
   }

   public void endViewport() throws IFException {
      try {
         this.handler.endElement("viewport");
      } catch (SAXException var2) {
         throw new IFException("SAX error in endViewport()", var2);
      }
   }

   public void startGroup(AffineTransform[] transforms) throws IFException {
      this.startGroup(IFUtil.toString(transforms));
   }

   public void startGroup(AffineTransform transform) throws IFException {
      this.startGroup(IFUtil.toString(transform));
   }

   private void startGroup(String transform) throws IFException {
      try {
         AttributesImpl atts = new AttributesImpl();
         if (transform != null && transform.length() > 0) {
            this.addAttribute(atts, "transform", transform);
         }

         this.handler.startElement((String)"g", atts);
      } catch (SAXException var3) {
         throw new IFException("SAX error in startGroup()", var3);
      }
   }

   public void endGroup() throws IFException {
      try {
         this.handler.endElement("g");
      } catch (SAXException var2) {
         throw new IFException("SAX error in endGroup()", var2);
      }
   }

   public void drawImage(String uri, Rectangle rect) throws IFException {
      try {
         AttributesImpl atts = new AttributesImpl();
         this.addAttribute(atts, XLINK_HREF, uri);
         this.addAttribute(atts, "x", Integer.toString(rect.x));
         this.addAttribute(atts, "y", Integer.toString(rect.y));
         this.addAttribute(atts, "width", Integer.toString(rect.width));
         this.addAttribute(atts, "height", Integer.toString(rect.height));
         this.addForeignAttributes(atts);
         this.addStructurePointerAttribute(atts);
         this.handler.element((String)"image", atts);
      } catch (SAXException var4) {
         throw new IFException("SAX error in startGroup()", var4);
      }
   }

   private void addForeignAttributes(AttributesImpl atts) throws SAXException {
      Map foreignAttributes = this.getContext().getForeignAttributes();
      if (!foreignAttributes.isEmpty()) {
         Iterator iter = foreignAttributes.entrySet().iterator();

         while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            this.addAttribute(atts, (QName)entry.getKey(), entry.getValue().toString());
         }
      }

   }

   public void drawImage(Document doc, Rectangle rect) throws IFException {
      try {
         AttributesImpl atts = new AttributesImpl();
         this.addAttribute(atts, "x", Integer.toString(rect.x));
         this.addAttribute(atts, "y", Integer.toString(rect.y));
         this.addAttribute(atts, "width", Integer.toString(rect.width));
         this.addAttribute(atts, "height", Integer.toString(rect.height));
         this.addForeignAttributes(atts);
         this.addStructurePointerAttribute(atts);
         this.handler.startElement((String)"image", atts);
         (new DOM2SAX(this.handler)).writeDocument(doc, true);
         this.handler.endElement("image");
      } catch (SAXException var4) {
         throw new IFException("SAX error in startGroup()", var4);
      }
   }

   private static String toString(Paint paint) {
      if (paint instanceof Color) {
         return ColorUtil.colorToString((Color)paint);
      } else {
         throw new UnsupportedOperationException("Paint not supported: " + paint);
      }
   }

   public void clipRect(Rectangle rect) throws IFException {
      try {
         AttributesImpl atts = new AttributesImpl();
         this.addAttribute(atts, "x", Integer.toString(rect.x));
         this.addAttribute(atts, "y", Integer.toString(rect.y));
         this.addAttribute(atts, "width", Integer.toString(rect.width));
         this.addAttribute(atts, "height", Integer.toString(rect.height));
         this.handler.element((String)"clip-rect", atts);
      } catch (SAXException var3) {
         throw new IFException("SAX error in clipRect()", var3);
      }
   }

   public void fillRect(Rectangle rect, Paint fill) throws IFException {
      if (fill != null) {
         try {
            AttributesImpl atts = new AttributesImpl();
            this.addAttribute(atts, "x", Integer.toString(rect.x));
            this.addAttribute(atts, "y", Integer.toString(rect.y));
            this.addAttribute(atts, "width", Integer.toString(rect.width));
            this.addAttribute(atts, "height", Integer.toString(rect.height));
            this.addAttribute(atts, "fill", toString(fill));
            this.handler.element((String)"rect", atts);
         } catch (SAXException var4) {
            throw new IFException("SAX error in fillRect()", var4);
         }
      }
   }

   public void drawBorderRect(Rectangle rect, BorderProps before, BorderProps after, BorderProps start, BorderProps end) throws IFException {
      if (before != null || after != null || start != null || end != null) {
         try {
            AttributesImpl atts = new AttributesImpl();
            this.addAttribute(atts, "x", Integer.toString(rect.x));
            this.addAttribute(atts, "y", Integer.toString(rect.y));
            this.addAttribute(atts, "width", Integer.toString(rect.width));
            this.addAttribute(atts, "height", Integer.toString(rect.height));
            if (before != null) {
               this.addAttribute(atts, "before", before.toString());
            }

            if (after != null) {
               this.addAttribute(atts, "after", after.toString());
            }

            if (start != null) {
               this.addAttribute(atts, "start", start.toString());
            }

            if (end != null) {
               this.addAttribute(atts, "end", end.toString());
            }

            this.handler.element((String)"border-rect", atts);
         } catch (SAXException var7) {
            throw new IFException("SAX error in drawBorderRect()", var7);
         }
      }
   }

   public void drawLine(Point start, Point end, int width, Color color, RuleStyle style) throws IFException {
      try {
         AttributesImpl atts = new AttributesImpl();
         this.addAttribute(atts, "x1", Integer.toString(start.x));
         this.addAttribute(atts, "y1", Integer.toString(start.y));
         this.addAttribute(atts, "x2", Integer.toString(end.x));
         this.addAttribute(atts, "y2", Integer.toString(end.y));
         this.addAttribute(atts, "stroke-width", Integer.toString(width));
         this.addAttribute(atts, "color", ColorUtil.colorToString(color));
         this.addAttribute(atts, "style", style.getName());
         this.handler.element((String)"line", atts);
      } catch (SAXException var7) {
         throw new IFException("SAX error in drawLine()", var7);
      }
   }

   public void drawText(int x, int y, int letterSpacing, int wordSpacing, int[] dx, String text) throws IFException {
      try {
         AttributesImpl atts = new AttributesImpl();
         this.addAttribute(atts, "x", Integer.toString(x));
         this.addAttribute(atts, "y", Integer.toString(y));
         if (letterSpacing != 0) {
            this.addAttribute(atts, "letter-spacing", Integer.toString(letterSpacing));
         }

         if (wordSpacing != 0) {
            this.addAttribute(atts, "word-spacing", Integer.toString(wordSpacing));
         }

         if (dx != null) {
            this.addAttribute(atts, "dx", IFUtil.toString(dx));
         }

         this.addStructurePointerAttribute(atts);
         this.handler.startElement((String)"text", atts);
         char[] chars = text.toCharArray();
         this.handler.characters(chars, 0, chars.length);
         this.handler.endElement("text");
      } catch (SAXException var9) {
         throw new IFException("SAX error in setFont()", var9);
      }
   }

   public void setFont(String family, String style, Integer weight, String variant, Integer size, Color color) throws IFException {
      try {
         AttributesImpl atts = new AttributesImpl();
         boolean changed;
         if (family != null) {
            changed = !family.equals(this.state.getFontFamily());
            if (changed) {
               this.state.setFontFamily(family);
               this.addAttribute(atts, "family", family);
            }
         }

         if (style != null) {
            changed = !style.equals(this.state.getFontStyle());
            if (changed) {
               this.state.setFontStyle(style);
               this.addAttribute(atts, "style", style);
            }
         }

         if (weight != null) {
            changed = weight != this.state.getFontWeight();
            if (changed) {
               this.state.setFontWeight(weight);
               this.addAttribute(atts, "weight", weight.toString());
            }
         }

         if (variant != null) {
            changed = !variant.equals(this.state.getFontVariant());
            if (changed) {
               this.state.setFontVariant(variant);
               this.addAttribute(atts, "variant", variant);
            }
         }

         if (size != null) {
            changed = size != this.state.getFontSize();
            if (changed) {
               this.state.setFontSize(size);
               this.addAttribute(atts, "size", size.toString());
            }
         }

         if (color != null) {
            changed = !color.equals(this.state.getTextColor());
            if (changed) {
               this.state.setTextColor(color);
               this.addAttribute(atts, "color", toString(color));
            }
         }

         if (atts.getLength() > 0) {
            this.handler.element((String)"font", atts);
         }

      } catch (SAXException var9) {
         throw new IFException("SAX error in setFont()", var9);
      }
   }

   public void handleExtensionObject(Object extension) throws IFException {
      if (extension instanceof XMLizable) {
         try {
            ((XMLizable)extension).toSAX(this.handler);
         } catch (SAXException var3) {
            throw new IFException("SAX error while handling extension object", var3);
         }
      } else {
         throw new UnsupportedOperationException("Extension must implement XMLizable: " + extension + " (" + extension.getClass().getName() + ")");
      }
   }

   protected RenderingContext createRenderingContext() {
      throw new IllegalStateException("Should never be called!");
   }

   private void addAttribute(AttributesImpl atts, QName attribute, String value) throws SAXException {
      this.handler.startPrefixMapping(attribute.getPrefix(), attribute.getNamespaceURI());
      XMLUtil.addAttribute(atts, attribute, value);
   }

   private void addAttribute(AttributesImpl atts, String localName, String value) {
      XMLUtil.addAttribute(atts, localName, value);
   }

   private void addStructurePointerAttribute(AttributesImpl atts) {
      String ptr = this.getContext().getStructurePointer();
      if (ptr != null) {
         this.addAttribute(atts, "ptr", ptr);
      }

   }

   private void noteAction(AbstractAction action) {
      if (action == null) {
         throw new NullPointerException("action must not be null");
      } else {
         if (!action.isComplete()) {
            if (!$assertionsDisabled && !action.hasID()) {
               throw new AssertionError();
            }

            this.incompleteActions.put(action.getID(), action);
         }

      }
   }

   public void renderNamedDestination(NamedDestination destination) throws IFException {
      this.noteAction(destination.getAction());
      AttributesImpl atts = new AttributesImpl();
      atts.addAttribute((String)null, "name", "name", "CDATA", destination.getName());

      try {
         this.handler.startElement((QName)DocumentNavigationExtensionConstants.NAMED_DESTINATION, atts);
         this.serializeXMLizable(destination.getAction());
         this.handler.endElement(DocumentNavigationExtensionConstants.NAMED_DESTINATION);
      } catch (SAXException var4) {
         throw new IFException("SAX error serializing named destination", var4);
      }
   }

   public void renderBookmarkTree(BookmarkTree tree) throws IFException {
      AttributesImpl atts = new AttributesImpl();

      try {
         this.handler.startElement((QName)DocumentNavigationExtensionConstants.BOOKMARK_TREE, atts);
         Iterator iter = tree.getBookmarks().iterator();

         while(iter.hasNext()) {
            Bookmark b = (Bookmark)iter.next();
            this.serializeBookmark(b);
         }

         this.handler.endElement(DocumentNavigationExtensionConstants.BOOKMARK_TREE);
      } catch (SAXException var5) {
         throw new IFException("SAX error serializing bookmark tree", var5);
      }
   }

   private void serializeBookmark(Bookmark bookmark) throws SAXException, IFException {
      this.noteAction(bookmark.getAction());
      AttributesImpl atts = new AttributesImpl();
      atts.addAttribute((String)null, "title", "title", "CDATA", bookmark.getTitle());
      atts.addAttribute((String)null, "starting-state", "starting-state", "CDATA", bookmark.isShown() ? "show" : "hide");
      this.handler.startElement((QName)DocumentNavigationExtensionConstants.BOOKMARK, atts);
      this.serializeXMLizable(bookmark.getAction());
      Iterator iter = bookmark.getChildBookmarks().iterator();

      while(iter.hasNext()) {
         Bookmark b = (Bookmark)iter.next();
         this.serializeBookmark(b);
      }

      this.handler.endElement(DocumentNavigationExtensionConstants.BOOKMARK);
   }

   public void renderLink(Link link) throws IFException {
      this.noteAction(link.getAction());
      AttributesImpl atts = new AttributesImpl();
      atts.addAttribute((String)null, "rect", "rect", "CDATA", IFUtil.toString(link.getTargetRect()));
      if (this.getUserAgent().isAccessibilityEnabled()) {
         this.addAttribute(atts, "ptr", link.getAction().getStructurePointer());
      }

      try {
         this.handler.startElement((QName)DocumentNavigationExtensionConstants.LINK, atts);
         this.serializeXMLizable(link.getAction());
         this.handler.endElement(DocumentNavigationExtensionConstants.LINK);
      } catch (SAXException var4) {
         throw new IFException("SAX error serializing link", var4);
      }
   }

   public void addResolvedAction(AbstractAction action) throws IFException {
      if (!$assertionsDisabled && !action.isComplete()) {
         throw new AssertionError();
      } else if (!$assertionsDisabled && !action.hasID()) {
         throw new AssertionError();
      } else {
         AbstractAction noted = (AbstractAction)this.incompleteActions.remove(action.getID());
         if (noted != null) {
            this.completeActions.add(action);
         }

      }
   }

   private void commitNavigation() throws IFException {
      Iterator iter = this.completeActions.iterator();

      while(iter.hasNext()) {
         AbstractAction action = (AbstractAction)iter.next();
         iter.remove();
         this.serializeXMLizable(action);
      }

      if (!$assertionsDisabled && this.completeActions.size() != 0) {
         throw new AssertionError();
      }
   }

   private void finishDocumentNavigation() {
      if (!$assertionsDisabled && this.incompleteActions.size() != 0) {
         throw new AssertionError("Still holding incomplete actions!");
      }
   }

   private void serializeXMLizable(XMLizable object) throws IFException {
      try {
         object.toSAX(this.handler);
      } catch (SAXException var3) {
         throw new IFException("SAX error serializing object", var3);
      }
   }

   static {
      $assertionsDisabled = !IFSerializer.class.desiredAssertionStatus();
   }
}
