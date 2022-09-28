package org.apache.batik.dom.svg;

import java.awt.geom.AffineTransform;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGMatrix;

public class SVGOMTransform extends AbstractSVGTransform {
   public SVGOMTransform() {
      this.affineTransform = new AffineTransform();
   }

   protected SVGMatrix createMatrix() {
      return new AbstractSVGMatrix() {
         protected AffineTransform getAffineTransform() {
            return SVGOMTransform.this.affineTransform;
         }

         public void setA(float var1) throws DOMException {
            SVGOMTransform.this.setType((short)1);
            super.setA(var1);
         }

         public void setB(float var1) throws DOMException {
            SVGOMTransform.this.setType((short)1);
            super.setB(var1);
         }

         public void setC(float var1) throws DOMException {
            SVGOMTransform.this.setType((short)1);
            super.setC(var1);
         }

         public void setD(float var1) throws DOMException {
            SVGOMTransform.this.setType((short)1);
            super.setD(var1);
         }

         public void setE(float var1) throws DOMException {
            SVGOMTransform.this.setType((short)1);
            super.setE(var1);
         }

         public void setF(float var1) throws DOMException {
            SVGOMTransform.this.setType((short)1);
            super.setF(var1);
         }
      };
   }
}
