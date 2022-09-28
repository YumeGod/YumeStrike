package org.apache.fop.render.intermediate.extensions;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.render.intermediate.IFDocumentNavigationHandler;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.util.XMLUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DocumentNavigationHandler extends DefaultHandler implements DocumentNavigationExtensionConstants {
   protected static Log log;
   private StringBuffer content = new StringBuffer();
   private Stack objectStack = new Stack();
   private IFDocumentNavigationHandler navHandler;
   private String structurePointer;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public DocumentNavigationHandler(IFDocumentNavigationHandler navHandler) {
      this.navHandler = navHandler;
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      boolean handled = false;
      if ("http://xmlgraphics.apache.org/fop/intermediate/document-navigation".equals(uri)) {
         if (BOOKMARK_TREE.getLocalName().equals(localName)) {
            if (!this.objectStack.isEmpty()) {
               throw new SAXException(localName + " must be the root element!");
            }

            BookmarkTree bookmarkTree = new BookmarkTree();
            this.objectStack.push(bookmarkTree);
         } else {
            String gotoURI;
            String id;
            if (BOOKMARK.getLocalName().equals(localName)) {
               id = attributes.getValue("title");
               gotoURI = attributes.getValue("starting-state");
               boolean show = !"hide".equals(gotoURI);
               Bookmark b = new Bookmark(id, show, (AbstractAction)null);
               Object o = this.objectStack.peek();
               if (o instanceof AbstractAction) {
                  AbstractAction action = (AbstractAction)this.objectStack.pop();
                  o = this.objectStack.peek();
                  ((Bookmark)o).setAction(action);
               }

               if (o instanceof BookmarkTree) {
                  ((BookmarkTree)o).addBookmark(b);
               } else {
                  ((Bookmark)o).addChildBookmark(b);
               }

               this.objectStack.push(b);
            } else if (NAMED_DESTINATION.getLocalName().equals(localName)) {
               if (!this.objectStack.isEmpty()) {
                  throw new SAXException(localName + " must be the root element!");
               }

               id = attributes.getValue("name");
               NamedDestination dest = new NamedDestination(id, (AbstractAction)null);
               this.objectStack.push(dest);
            } else if (LINK.getLocalName().equals(localName)) {
               if (!this.objectStack.isEmpty()) {
                  throw new SAXException(localName + " must be the root element!");
               }

               Rectangle targetRect = XMLUtil.getAttributeAsRectangle(attributes, "rect");
               this.structurePointer = attributes.getValue("ptr");
               Link link = new Link((AbstractAction)null, targetRect);
               this.objectStack.push(link);
            } else {
               String id;
               if (GOTO_XY.getLocalName().equals(localName)) {
                  id = attributes.getValue("idref");
                  GoToXYAction action;
                  if (id != null) {
                     action = new GoToXYAction(id);
                  } else {
                     id = attributes.getValue("id");
                     int pageIndex = XMLUtil.getAttributeAsInt(attributes, "page-index");
                     Point location;
                     if (pageIndex < 0) {
                        location = null;
                     } else {
                        int x = XMLUtil.getAttributeAsInt(attributes, "x");
                        int y = XMLUtil.getAttributeAsInt(attributes, "y");
                        location = new Point(x, y);
                     }

                     action = new GoToXYAction(id, pageIndex, location);
                  }

                  if (this.structurePointer != null) {
                     action.setStructurePointer(this.structurePointer);
                  }

                  this.objectStack.push(action);
               } else {
                  if (!GOTO_URI.getLocalName().equals(localName)) {
                     throw new SAXException("Invalid element '" + localName + "' in namespace: " + uri);
                  }

                  id = attributes.getValue("id");
                  gotoURI = attributes.getValue("uri");
                  id = attributes.getValue("show-destination");
                  boolean newWindow = "new".equals(id);
                  URIAction action = new URIAction(gotoURI, newWindow);
                  if (id != null) {
                     action.setID(id);
                  }

                  if (this.structurePointer != null) {
                     action.setStructurePointer(this.structurePointer);
                  }

                  this.objectStack.push(action);
               }
            }
         }

         handled = true;
      }

      if (!handled) {
         if ("http://xmlgraphics.apache.org/fop/intermediate/document-navigation".equals(uri)) {
            throw new SAXException("Unhandled element '" + localName + "' in namespace: " + uri);
         }

         log.warn("Unhandled element '" + localName + "' in namespace: " + uri);
      }

   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      if ("http://xmlgraphics.apache.org/fop/intermediate/document-navigation".equals(uri)) {
         try {
            if (BOOKMARK_TREE.getLocalName().equals(localName)) {
               BookmarkTree tree = (BookmarkTree)this.objectStack.pop();
               if (this.hasNavigation()) {
                  this.navHandler.renderBookmarkTree(tree);
               }
            } else {
               AbstractAction action;
               if (BOOKMARK.getLocalName().equals(localName)) {
                  if (this.objectStack.peek() instanceof AbstractAction) {
                     action = (AbstractAction)this.objectStack.pop();
                     Bookmark b = (Bookmark)this.objectStack.pop();
                     b.setAction(action);
                  } else {
                     this.objectStack.pop();
                  }
               } else if (NAMED_DESTINATION.getLocalName().equals(localName)) {
                  action = (AbstractAction)this.objectStack.pop();
                  NamedDestination dest = (NamedDestination)this.objectStack.pop();
                  dest.setAction(action);
                  if (this.hasNavigation()) {
                     this.navHandler.renderNamedDestination(dest);
                  }
               } else if (LINK.getLocalName().equals(localName)) {
                  action = (AbstractAction)this.objectStack.pop();
                  Link link = (Link)this.objectStack.pop();
                  link.setAction(action);
                  if (this.hasNavigation()) {
                     this.navHandler.renderLink(link);
                  }
               } else if (localName.startsWith("goto-") && this.objectStack.size() == 1) {
                  action = (AbstractAction)this.objectStack.pop();
                  if (this.hasNavigation()) {
                     this.navHandler.addResolvedAction(action);
                  }
               }
            }
         } catch (IFException var6) {
            throw new SAXException(var6);
         }
      }

      this.content.setLength(0);
   }

   private boolean hasNavigation() {
      return this.navHandler != null;
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      this.content.append(ch, start, length);
   }

   public void endDocument() throws SAXException {
      if (!$assertionsDisabled && !this.objectStack.isEmpty()) {
         throw new AssertionError();
      }
   }

   static {
      $assertionsDisabled = !DocumentNavigationHandler.class.desiredAssertionStatus();
      log = LogFactory.getLog(DocumentNavigationHandler.class);
   }
}
