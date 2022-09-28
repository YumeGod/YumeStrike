package org.apache.fop.render.pdf;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.fop.pdf.PDFAction;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFFactory;
import org.apache.fop.pdf.PDFGoTo;
import org.apache.fop.pdf.PDFLink;
import org.apache.fop.pdf.PDFOutline;
import org.apache.fop.render.intermediate.IFDocumentNavigationHandler;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.extensions.AbstractAction;
import org.apache.fop.render.intermediate.extensions.Bookmark;
import org.apache.fop.render.intermediate.extensions.BookmarkTree;
import org.apache.fop.render.intermediate.extensions.GoToXYAction;
import org.apache.fop.render.intermediate.extensions.Link;
import org.apache.fop.render.intermediate.extensions.NamedDestination;
import org.apache.fop.render.intermediate.extensions.URIAction;

public class PDFDocumentNavigationHandler implements IFDocumentNavigationHandler {
   private final PDFDocumentHandler documentHandler;
   private final Map incompleteActions = new HashMap();
   private final Map completeActions = new HashMap();
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public PDFDocumentNavigationHandler(PDFDocumentHandler documentHandler) {
      this.documentHandler = documentHandler;
   }

   PDFDocument getPDFDoc() {
      return this.documentHandler.pdfDoc;
   }

   public void renderNamedDestination(NamedDestination destination) throws IFException {
      PDFAction action = this.getAction(destination.getAction());
      this.getPDFDoc().getFactory().makeDestination(destination.getName(), action.makeReference());
   }

   public void renderBookmarkTree(BookmarkTree tree) throws IFException {
      Iterator iter = tree.getBookmarks().iterator();

      while(iter.hasNext()) {
         Bookmark b = (Bookmark)iter.next();
         this.renderBookmark(b, (PDFOutline)null);
      }

   }

   private void renderBookmark(Bookmark bookmark, PDFOutline parent) {
      if (parent == null) {
         parent = this.getPDFDoc().getOutlineRoot();
      }

      PDFAction action = this.getAction(bookmark.getAction());
      String actionRef = action != null ? action.makeReference().toString() : null;
      PDFOutline pdfOutline = this.getPDFDoc().getFactory().makeOutline(parent, bookmark.getTitle(), actionRef, bookmark.isShown());
      Iterator iter = bookmark.getChildBookmarks().iterator();

      while(iter.hasNext()) {
         Bookmark b = (Bookmark)iter.next();
         this.renderBookmark(b, pdfOutline);
      }

   }

   public void renderLink(Link link) throws IFException {
      Rectangle targetRect = link.getTargetRect();
      int pageHeight = this.documentHandler.currentPageRef.getPageDimension().height;
      Rectangle2D targetRect2D = new Rectangle2D.Double(targetRect.getMinX() / 1000.0, ((double)pageHeight - targetRect.getMinY() - targetRect.getHeight()) / 1000.0, targetRect.getWidth() / 1000.0, targetRect.getHeight() / 1000.0);
      PDFAction pdfAction = this.getAction(link.getAction());
      PDFLink pdfLink = this.getPDFDoc().getFactory().makeLink(targetRect2D, pdfAction);
      if (pdfLink != null) {
         String ptr = link.getAction().getStructurePointer();
         if (this.documentHandler.getUserAgent().isAccessibilityEnabled() && ptr != null && ptr.length() > 0) {
            this.documentHandler.getLogicalStructureHandler().addLinkContentItem(pdfLink, ptr);
         }

         this.documentHandler.currentPage.addAnnotation(pdfLink);
      }

   }

   public void commit() {
   }

   public void addResolvedAction(AbstractAction action) throws IFException {
      if (!$assertionsDisabled && !action.isComplete()) {
         throw new AssertionError();
      } else {
         PDFAction pdfAction = (PDFAction)this.incompleteActions.remove(action.getID());
         if (pdfAction == null) {
            this.getAction(action);
         } else {
            if (!(pdfAction instanceof PDFGoTo)) {
               throw new UnsupportedOperationException("Action type not supported: " + pdfAction.getClass().getName());
            }

            PDFGoTo pdfGoTo = (PDFGoTo)pdfAction;
            this.updateTargetLocation(pdfGoTo, (GoToXYAction)action);
         }

      }
   }

   private PDFAction getAction(AbstractAction action) {
      if (action == null) {
         return null;
      } else {
         PDFAction pdfAction = (PDFAction)this.completeActions.get(action.getID());
         if (pdfAction != null) {
            return pdfAction;
         } else if (action instanceof GoToXYAction) {
            pdfAction = (PDFAction)this.incompleteActions.get(action.getID());
            if (pdfAction != null) {
               return pdfAction;
            } else {
               GoToXYAction a = (GoToXYAction)action;
               PDFGoTo pdfGoTo = new PDFGoTo((String)null);
               this.getPDFDoc().assignObjectNumber(pdfGoTo);
               if (action.isComplete()) {
                  this.updateTargetLocation(pdfGoTo, a);
               } else {
                  this.incompleteActions.put(action.getID(), pdfGoTo);
               }

               return pdfGoTo;
            }
         } else if (action instanceof URIAction) {
            URIAction u = (URIAction)action;
            if (!$assertionsDisabled && !u.isComplete()) {
               throw new AssertionError();
            } else {
               PDFFactory factory = this.getPDFDoc().getFactory();
               pdfAction = factory.getExternalAction(u.getURI(), u.isNewWindow());
               if (!pdfAction.hasObjectNumber()) {
                  this.getPDFDoc().registerObject(pdfAction);
               }

               this.completeActions.put(action.getID(), pdfAction);
               return pdfAction;
            }
         } else {
            throw new UnsupportedOperationException("Unsupported action type: " + action + " (" + action.getClass().getName() + ")");
         }
      }
   }

   private void updateTargetLocation(PDFGoTo pdfGoTo, GoToXYAction action) {
      PDFDocumentHandler.PageReference pageRef = this.documentHandler.getPageReference(action.getPageIndex());
      Point2D p2d = null;
      p2d = new Point2D.Double((double)action.getTargetLocation().x / 1000.0, (double)(pageRef.getPageDimension().height - action.getTargetLocation().y) / 1000.0);
      String pdfPageRef = pageRef.getPageRef().toString();
      pdfGoTo.setPageReference(pdfPageRef);
      pdfGoTo.setPosition(p2d);
      this.getPDFDoc().addObject(pdfGoTo);
      this.completeActions.put(action.getID(), pdfGoTo);
   }

   static {
      $assertionsDisabled = !PDFDocumentNavigationHandler.class.desiredAssertionStatus();
   }
}
