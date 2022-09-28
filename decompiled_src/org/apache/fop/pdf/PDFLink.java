package org.apache.fop.pdf;

import java.awt.geom.Rectangle2D;

public class PDFLink extends PDFObject {
   public static final int EXTERNAL = 0;
   public static final int INTERNAL = 1;
   private float ulx;
   private float uly;
   private float brx;
   private float bry;
   private String color;
   private PDFAction action;
   private Integer structParent;

   public PDFLink(Rectangle2D r) {
      this.ulx = (float)r.getX();
      this.uly = (float)r.getY();
      this.brx = (float)(r.getX() + r.getWidth());
      this.bry = (float)(r.getY() + r.getHeight());
      this.color = "0 0 0";
   }

   public void setAction(PDFAction action) {
      this.action = action;
   }

   public void setStructParent(int structParent) {
      this.structParent = new Integer(structParent);
   }

   public String toPDFString() {
      this.getDocumentSafely().getProfile().verifyAnnotAllowed();
      String fFlag = "";
      if (this.getDocumentSafely().getProfile().getPDFAMode().isPDFA1LevelB()) {
         int f = 0;
         f |= 4;
         f |= 8;
         f |= 16;
         fFlag = "/F " + f;
      }

      String s = this.getObjectID() + "<< /Type /Annot\n" + "/Subtype /Link\n" + "/Rect [ " + this.ulx + " " + this.uly + " " + this.brx + " " + this.bry + " ]\n" + "/C [ " + this.color + " ]\n" + "/Border [ 0 0 0 ]\n" + "/A " + this.action.getAction() + "\n" + "/H /I\n" + (this.structParent != null ? "/StructParent " + this.structParent.toString() + "\n" : "") + fFlag + "\n>>\nendobj\n";
      return s;
   }

   protected boolean contentEquals(PDFObject obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof PDFLink) {
         PDFLink link = (PDFLink)obj;
         if (link.ulx == this.ulx && link.uly == this.uly && link.brx == this.brx && link.bry == this.bry) {
            return link.color.equals(this.color) && link.action.getAction().equals(this.action.getAction());
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
