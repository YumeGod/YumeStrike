package org.apache.fop.svg;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.StringTokenizer;
import org.apache.batik.gvt.CompositeGraphicsNode;

public class PDFANode extends CompositeGraphicsNode {
   private String destination;
   private AffineTransform transform;

   public void setDestination(String dest) {
      this.destination = dest;
   }

   public void setTransform(AffineTransform tf) {
      this.transform = tf;
   }

   public void paint(Graphics2D g2d) {
      if (this.isVisible) {
         super.paint(g2d);
         if (g2d instanceof PDFGraphics2D) {
            PDFGraphics2D pdfg = (PDFGraphics2D)g2d;
            int type = 0;
            Shape outline = this.getOutline();
            if (this.destination.startsWith("#svgView(viewBox(")) {
               type = 1;
               String nums = this.destination.substring(17, this.destination.length() - 2);
               float x = 0.0F;
               float y = 0.0F;
               float width = 0.0F;
               float height = 0.0F;
               int count = 0;

               try {
                  StringTokenizer st = new StringTokenizer(nums, ",");

                  while(st.hasMoreTokens()) {
                     String tok = st.nextToken();
                     ++count;
                     switch (count) {
                        case 1:
                           x = Float.parseFloat(tok);
                           break;
                        case 2:
                           y = Float.parseFloat(tok);
                           break;
                        case 3:
                           width = Float.parseFloat(tok);
                           break;
                        case 4:
                           height = Float.parseFloat(tok);
                     }
                  }
               } catch (Exception var13) {
                  var13.printStackTrace();
               }

               Rectangle2D destRect = new Rectangle2D.Float(x, y, width, height);
               Rectangle2D destRect = this.transform.createTransformedShape(destRect).getBounds();
               x = (float)destRect.getX();
               y = (float)destRect.getY();
               width = (float)destRect.getWidth();
               height = (float)destRect.getHeight();
               this.destination = "" + x + " " + y + " " + (x + width) + " " + (y + height);
            }

            pdfg.addLink(this.getBounds(), this.transform, this.destination, type);
         }
      }

   }
}
