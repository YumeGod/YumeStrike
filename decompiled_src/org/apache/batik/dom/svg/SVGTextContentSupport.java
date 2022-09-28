package org.apache.batik.dom.svg;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGRect;

public class SVGTextContentSupport {
   public static int getNumberOfChars(Element var0) {
      SVGOMElement var1 = (SVGOMElement)var0;
      return ((SVGTextContent)var1.getSVGContext()).getNumberOfChars();
   }

   public static SVGRect getExtentOfChar(Element var0, final int var1) {
      final SVGOMElement var2 = (SVGOMElement)var0;
      if (var1 >= 0 && var1 < getNumberOfChars(var0)) {
         final SVGTextContent var3 = (SVGTextContent)var2.getSVGContext();
         return new SVGRect() {
            public float getX() {
               return (float)SVGTextContentSupport.getExtent(var2, var3, var1).getX();
            }

            public void setX(float var1x) throws DOMException {
               throw var2.createDOMException((short)7, "readonly.rect", (Object[])null);
            }

            public float getY() {
               return (float)SVGTextContentSupport.getExtent(var2, var3, var1).getY();
            }

            public void setY(float var1x) throws DOMException {
               throw var2.createDOMException((short)7, "readonly.rect", (Object[])null);
            }

            public float getWidth() {
               return (float)SVGTextContentSupport.getExtent(var2, var3, var1).getWidth();
            }

            public void setWidth(float var1x) throws DOMException {
               throw var2.createDOMException((short)7, "readonly.rect", (Object[])null);
            }

            public float getHeight() {
               return (float)SVGTextContentSupport.getExtent(var2, var3, var1).getHeight();
            }

            public void setHeight(float var1x) throws DOMException {
               throw var2.createDOMException((short)7, "readonly.rect", (Object[])null);
            }
         };
      } else {
         throw var2.createDOMException((short)1, "", (Object[])null);
      }
   }

   protected static Rectangle2D getExtent(SVGOMElement var0, SVGTextContent var1, int var2) {
      Rectangle2D var3 = var1.getExtentOfChar(var2);
      if (var3 == null) {
         throw var0.createDOMException((short)1, "", (Object[])null);
      } else {
         return var3;
      }
   }

   public static SVGPoint getStartPositionOfChar(Element var0, final int var1) throws DOMException {
      SVGOMElement var2 = (SVGOMElement)var0;
      if (var1 >= 0 && var1 < getNumberOfChars(var0)) {
         final SVGTextContent var3 = (SVGTextContent)var2.getSVGContext();
         return new SVGTextPoint(var2) {
            public float getX() {
               return (float)SVGTextContentSupport.getStartPos(this.svgelt, var3, var1).getX();
            }

            public float getY() {
               return (float)SVGTextContentSupport.getStartPos(this.svgelt, var3, var1).getY();
            }
         };
      } else {
         throw var2.createDOMException((short)1, "", (Object[])null);
      }
   }

   protected static Point2D getStartPos(SVGOMElement var0, SVGTextContent var1, int var2) {
      Point2D var3 = var1.getStartPositionOfChar(var2);
      if (var3 == null) {
         throw var0.createDOMException((short)1, "", (Object[])null);
      } else {
         return var3;
      }
   }

   public static SVGPoint getEndPositionOfChar(Element var0, final int var1) throws DOMException {
      SVGOMElement var2 = (SVGOMElement)var0;
      if (var1 >= 0 && var1 < getNumberOfChars(var0)) {
         final SVGTextContent var3 = (SVGTextContent)var2.getSVGContext();
         return new SVGTextPoint(var2) {
            public float getX() {
               return (float)SVGTextContentSupport.getEndPos(this.svgelt, var3, var1).getX();
            }

            public float getY() {
               return (float)SVGTextContentSupport.getEndPos(this.svgelt, var3, var1).getY();
            }
         };
      } else {
         throw var2.createDOMException((short)1, "", (Object[])null);
      }
   }

   protected static Point2D getEndPos(SVGOMElement var0, SVGTextContent var1, int var2) {
      Point2D var3 = var1.getEndPositionOfChar(var2);
      if (var3 == null) {
         throw var0.createDOMException((short)1, "", (Object[])null);
      } else {
         return var3;
      }
   }

   public static void selectSubString(Element var0, int var1, int var2) {
      SVGOMElement var3 = (SVGOMElement)var0;
      if (var1 >= 0 && var1 < getNumberOfChars(var0)) {
         SVGTextContent var4 = (SVGTextContent)var3.getSVGContext();
         var4.selectSubString(var1, var2);
      } else {
         throw var3.createDOMException((short)1, "", (Object[])null);
      }
   }

   public static float getRotationOfChar(Element var0, int var1) {
      SVGOMElement var2 = (SVGOMElement)var0;
      if (var1 >= 0 && var1 < getNumberOfChars(var0)) {
         SVGTextContent var3 = (SVGTextContent)var2.getSVGContext();
         return var3.getRotationOfChar(var1);
      } else {
         throw var2.createDOMException((short)1, "", (Object[])null);
      }
   }

   public static float getComputedTextLength(Element var0) {
      SVGOMElement var1 = (SVGOMElement)var0;
      SVGTextContent var2 = (SVGTextContent)var1.getSVGContext();
      return var2.getComputedTextLength();
   }

   public static float getSubStringLength(Element var0, int var1, int var2) {
      SVGOMElement var3 = (SVGOMElement)var0;
      if (var1 >= 0 && var1 < getNumberOfChars(var0)) {
         SVGTextContent var4 = (SVGTextContent)var3.getSVGContext();
         return var4.getSubStringLength(var1, var2);
      } else {
         throw var3.createDOMException((short)1, "", (Object[])null);
      }
   }

   public static int getCharNumAtPosition(Element var0, float var1, float var2) throws DOMException {
      SVGOMElement var3 = (SVGOMElement)var0;
      SVGTextContent var4 = (SVGTextContent)var3.getSVGContext();
      return var4.getCharNumAtPosition(var1, var2);
   }

   public static class SVGTextPoint extends SVGOMPoint {
      SVGOMElement svgelt;

      SVGTextPoint(SVGOMElement var1) {
         this.svgelt = var1;
      }

      public void setX(float var1) throws DOMException {
         throw this.svgelt.createDOMException((short)7, "readonly.point", (Object[])null);
      }

      public void setY(float var1) throws DOMException {
         throw this.svgelt.createDOMException((short)7, "readonly.point", (Object[])null);
      }
   }
}
