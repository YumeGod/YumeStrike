package org.apache.fop.render.xml;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.apache.fop.area.BookmarkData;
import org.apache.fop.area.OffDocumentExtensionAttachment;
import org.apache.fop.area.OffDocumentItem;
import org.apache.fop.area.PageViewport;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.fop.render.PrintRenderer;
import org.apache.fop.render.RendererContext;
import org.apache.xmlgraphics.util.QName;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

public abstract class AbstractXMLRenderer extends PrintRenderer {
   public static final String NS = "";
   public static final String CDATA = "CDATA";
   public static final Attributes EMPTY_ATTS = new AttributesImpl();
   protected AttributesImpl atts = new AttributesImpl();
   protected ContentHandler handler;
   protected OutputStream out;
   protected RendererContext context;
   protected List extensionAttachments;

   protected void handleSAXException(SAXException saxe) {
      throw new RuntimeException(saxe.getMessage());
   }

   protected void handlePageExtensionAttachments(PageViewport page) {
      this.handleExtensionAttachments(page.getExtensionAttachments());
   }

   protected void comment(String comment) {
      if (this.handler instanceof LexicalHandler) {
         try {
            ((LexicalHandler)this.handler).comment(comment.toCharArray(), 0, comment.length());
         } catch (SAXException var3) {
            this.handleSAXException(var3);
         }
      }

   }

   protected void startElement(String tagName) {
      this.startElement(tagName, EMPTY_ATTS);
   }

   protected void startElement(String tagName, Attributes atts) {
      try {
         this.handler.startElement("", tagName, tagName, atts);
      } catch (SAXException var4) {
         this.handleSAXException(var4);
      }

   }

   protected void endElement(String tagName) {
      try {
         this.handler.endElement("", tagName, tagName);
      } catch (SAXException var3) {
         this.handleSAXException(var3);
      }

   }

   protected void characters(String text) {
      try {
         char[] ca = text.toCharArray();
         this.handler.characters(ca, 0, ca.length);
      } catch (SAXException var3) {
         this.handleSAXException(var3);
      }

   }

   protected void addAttribute(String name, String value) {
      this.atts.addAttribute("", name, name, "CDATA", value);
   }

   protected void addAttribute(QName name, String value) {
      this.atts.addAttribute(name.getNamespaceURI(), name.getLocalName(), name.getQName(), "CDATA", value);
   }

   protected void addAttribute(String name, int value) {
      this.addAttribute(name, Integer.toString(value));
   }

   private String createString(Rectangle2D rect) {
      return "" + (int)rect.getX() + " " + (int)rect.getY() + " " + (int)rect.getWidth() + " " + (int)rect.getHeight();
   }

   protected void addAttribute(String name, Rectangle2D rect) {
      this.addAttribute(name, this.createString(rect));
   }

   public void startRenderer(OutputStream outputStream) throws IOException {
      if (this.handler == null) {
         SAXTransformerFactory factory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();

         try {
            TransformerHandler transformerHandler = factory.newTransformerHandler();
            this.setContentHandler(transformerHandler);
            StreamResult res = new StreamResult(outputStream);
            transformerHandler.setResult(res);
         } catch (TransformerConfigurationException var6) {
            throw new RuntimeException(var6.getMessage());
         }

         this.out = outputStream;
      }

      try {
         this.handler.startDocument();
      } catch (SAXException var5) {
         this.handleSAXException(var5);
      }

   }

   public void stopRenderer() throws IOException {
      try {
         this.handler.endDocument();
      } catch (SAXException var2) {
         this.handleSAXException(var2);
      }

      if (this.out != null) {
         this.out.flush();
      }

   }

   public void processOffDocumentItem(OffDocumentItem oDI) {
      if (oDI instanceof BookmarkData) {
         this.renderBookmarkTree((BookmarkData)oDI);
      } else if (oDI instanceof OffDocumentExtensionAttachment) {
         ExtensionAttachment attachment = ((OffDocumentExtensionAttachment)oDI).getAttachment();
         if (this.extensionAttachments == null) {
            this.extensionAttachments = new ArrayList();
         }

         this.extensionAttachments.add(attachment);
      } else {
         String warn = "Ignoring OffDocumentItem: " + oDI;
         log.warn(warn);
      }

   }

   protected void handleDocumentExtensionAttachments() {
      if (this.extensionAttachments != null && this.extensionAttachments.size() > 0) {
         this.handleExtensionAttachments(this.extensionAttachments);
         this.extensionAttachments.clear();
      }

   }

   public void setContentHandler(ContentHandler handler) {
      this.handler = handler;
   }

   protected abstract void handleExtensionAttachments(List var1);

   protected abstract void renderBookmarkTree(BookmarkData var1);
}
