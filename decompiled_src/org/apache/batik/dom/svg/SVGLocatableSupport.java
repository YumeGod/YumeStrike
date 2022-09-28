package org.apache.batik.dom.svg;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import org.apache.batik.css.engine.SVGCSSEngine;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGFitToViewBox;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGRect;

public class SVGLocatableSupport {
   public static SVGElement getNearestViewportElement(Element var0) {
      Object var1 = var0;

      while(var1 != null) {
         var1 = SVGCSSEngine.getParentCSSStylableElement((Element)var1);
         if (var1 instanceof SVGFitToViewBox) {
            break;
         }
      }

      return (SVGElement)var1;
   }

   public static SVGElement getFarthestViewportElement(Element var0) {
      return (SVGElement)var0.getOwnerDocument().getDocumentElement();
   }

   public static SVGRect getBBox(Element var0) {
      final SVGOMElement var1 = (SVGOMElement)var0;
      SVGContext var2 = var1.getSVGContext();
      if (var2 == null) {
         return null;
      } else {
         return var2.getBBox() == null ? null : new SVGRect() {
            public float getX() {
               return (float)var1.getSVGContext().getBBox().getX();
            }

            public void setX(float var1x) throws DOMException {
               throw var1.createDOMException((short)7, "readonly.rect", (Object[])null);
            }

            public float getY() {
               return (float)var1.getSVGContext().getBBox().getY();
            }

            public void setY(float var1x) throws DOMException {
               throw var1.createDOMException((short)7, "readonly.rect", (Object[])null);
            }

            public float getWidth() {
               return (float)var1.getSVGContext().getBBox().getWidth();
            }

            public void setWidth(float var1x) throws DOMException {
               throw var1.createDOMException((short)7, "readonly.rect", (Object[])null);
            }

            public float getHeight() {
               return (float)var1.getSVGContext().getBBox().getHeight();
            }

            public void setHeight(float var1x) throws DOMException {
               throw var1.createDOMException((short)7, "readonly.rect", (Object[])null);
            }
         };
      }
   }

   public static SVGMatrix getCTM(Element var0) {
      final SVGOMElement var1 = (SVGOMElement)var0;
      return new AbstractSVGMatrix() {
         protected AffineTransform getAffineTransform() {
            return var1.getSVGContext().getCTM();
         }
      };
   }

   public static SVGMatrix getScreenCTM(Element var0) {
      final SVGOMElement var1 = (SVGOMElement)var0;
      return new AbstractSVGMatrix() {
         protected AffineTransform getAffineTransform() {
            SVGContext var1x = var1.getSVGContext();
            AffineTransform var2 = var1x.getGlobalTransform();
            AffineTransform var3 = var1x.getScreenTransform();
            if (var3 != null) {
               var2.preConcatenate(var3);
            }

            return var2;
         }
      };
   }

   public static SVGMatrix getTransformToElement(Element var0, SVGElement var1) throws SVGException {
      final SVGOMElement var2 = (SVGOMElement)var0;
      final SVGOMElement var3 = (SVGOMElement)var1;
      return new AbstractSVGMatrix() {
         protected AffineTransform getAffineTransform() {
            AffineTransform var1 = var2.getSVGContext().getGlobalTransform();
            if (var1 == null) {
               var1 = new AffineTransform();
            }

            AffineTransform var2x = var3.getSVGContext().getGlobalTransform();
            if (var2x == null) {
               var2x = new AffineTransform();
            }

            AffineTransform var3x = new AffineTransform(var1);

            try {
               var3x.preConcatenate(var2x.createInverse());
               return var3x;
            } catch (NoninvertibleTransformException var5) {
               throw var2.createSVGException((short)2, "noninvertiblematrix", (Object[])null);
            }
         }
      };
   }
}
