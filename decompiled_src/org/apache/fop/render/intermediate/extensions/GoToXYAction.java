package org.apache.fop.render.intermediate.extensions;

import java.awt.Point;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class GoToXYAction extends AbstractAction implements DocumentNavigationExtensionConstants {
   private int pageIndex;
   private Point targetLocation;

   public GoToXYAction(String id) {
      this(id, -1, (Point)null);
   }

   public GoToXYAction(String id, int pageIndex, Point targetLocation) {
      this.pageIndex = -1;
      this.setID(id);
      if (pageIndex < 0 && targetLocation != null) {
         throw new IllegalArgumentException("Page index may not be null if target location is known!");
      } else {
         this.setPageIndex(pageIndex);
         this.setTargetLocation(targetLocation);
      }
   }

   public void setPageIndex(int pageIndex) {
      this.pageIndex = pageIndex;
   }

   public int getPageIndex() {
      return this.pageIndex >= 0 ? this.pageIndex : 0;
   }

   public Point getTargetLocation() {
      return this.targetLocation == null ? new Point(0, 0) : this.targetLocation;
   }

   public void setTargetLocation(Point location) {
      this.targetLocation = location;
   }

   private boolean isCompleteExceptTargetLocation() {
      return this.getPageIndex() >= 0;
   }

   public boolean isComplete() {
      return this.isCompleteExceptTargetLocation() && this.targetLocation != null;
   }

   public boolean isSame(AbstractAction other) {
      if (other == null) {
         throw new NullPointerException("other must not be null");
      } else if (!(other instanceof GoToXYAction)) {
         return false;
      } else {
         GoToXYAction otherAction = (GoToXYAction)other;
         if (this.pageIndex != otherAction.pageIndex) {
            return false;
         } else if (this.targetLocation != null && otherAction.targetLocation != null) {
            return this.getTargetLocation().equals(otherAction.getTargetLocation());
         } else {
            return false;
         }
      }
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      if (this.isCompleteExceptTargetLocation()) {
         Point reportedTargetLocation = this.getTargetLocation();
         atts.addAttribute((String)null, "id", "id", "CDATA", this.getID());
         atts.addAttribute((String)null, "page-index", "page-index", "CDATA", Integer.toString(this.pageIndex));
         atts.addAttribute((String)null, "x", "x", "CDATA", Integer.toString(reportedTargetLocation.x));
         atts.addAttribute((String)null, "y", "y", "CDATA", Integer.toString(reportedTargetLocation.y));
      } else {
         atts.addAttribute((String)null, "idref", "idref", "CDATA", this.getID());
      }

      handler.startElement(GOTO_XY.getNamespaceURI(), GOTO_XY.getLocalName(), GOTO_XY.getQName(), atts);
      handler.endElement(GOTO_XY.getNamespaceURI(), GOTO_XY.getLocalName(), GOTO_XY.getQName());
   }

   public String toString() {
      return "GoToXY: ID=" + this.getID() + ", page=" + this.getPageIndex() + ", loc=" + this.getTargetLocation() + ", " + (this.isComplete() ? "complete" : "INCOMPLETE");
   }
}
