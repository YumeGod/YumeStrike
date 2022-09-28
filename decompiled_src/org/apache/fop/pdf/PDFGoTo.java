package org.apache.fop.pdf;

import java.awt.geom.Point2D;

public class PDFGoTo extends PDFAction {
   private String pageReference;
   private String destination = null;
   private float xPosition = 0.0F;
   private float yPosition = 0.0F;

   public PDFGoTo(String pageReference) {
      this.pageReference = pageReference;
   }

   public PDFGoTo(String pageReference, Point2D position) {
      this.pageReference = pageReference;
      this.setPosition(position);
   }

   public void setPageReference(String pageReference) {
      this.pageReference = pageReference;
   }

   public void setPosition(Point2D position) {
      this.xPosition = (float)position.getX();
      this.yPosition = (float)position.getY();
   }

   public void setXPosition(float xPosition) {
      this.xPosition = xPosition;
   }

   public void setYPosition(float yPosition) {
      this.yPosition = yPosition;
   }

   public void setDestination(String dest) {
      this.destination = dest;
   }

   public String getAction() {
      return this.referencePDF();
   }

   public String toPDFString() {
      String dest;
      if (this.destination == null) {
         dest = "/D [" + this.pageReference + " /XYZ " + this.xPosition + " " + this.yPosition + " null]\n";
      } else {
         dest = "/D [" + this.pageReference + " " + this.destination + "]\n";
      }

      return this.getObjectID() + "<< /Type /Action\n/S /GoTo\n" + dest + ">>\nendobj\n";
   }

   protected boolean contentEquals(PDFObject obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof PDFGoTo) {
         PDFGoTo gt = (PDFGoTo)obj;
         if (gt.pageReference == null) {
            if (this.pageReference != null) {
               return false;
            }
         } else if (!gt.pageReference.equals(this.pageReference)) {
            return false;
         }

         if (this.destination == null) {
            if (gt.destination != null || gt.xPosition != this.xPosition || gt.yPosition != this.yPosition) {
               return false;
            }
         } else if (!this.destination.equals(gt.destination)) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }
}
