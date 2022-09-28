package org.apache.fop.fo.extensions.svg;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.URL;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.parser.UnitProcessor;
import org.apache.fop.fo.FONode;
import org.apache.fop.util.ContentHandlerFactory;
import org.w3c.dom.Element;

public class SVGElement extends SVGObj {
   public SVGElement(FONode parent) {
      super(parent);
   }

   public ContentHandlerFactory getContentHandlerFactory() {
      return new SVGDOMContentHandlerFactory();
   }

   public Point2D getDimension(final Point2D view) {
      Element svgRoot = this.element;

      try {
         URL baseURL = new URL(this.getUserAgent().getBaseURL() == null ? (new File("")).toURI().toURL().toExternalForm() : this.getUserAgent().getBaseURL());
         if (baseURL != null) {
            SVGOMDocument svgdoc = (SVGOMDocument)this.doc;
            svgdoc.setURLObject(baseURL);
         }
      } catch (Exception var8) {
         log.error("Could not set base URL for svg", var8);
      }

      final float ptmm = this.getUserAgent().getSourcePixelUnitToMillimeter();
      SVGContext dc = new SVGContext() {
         public float getPixelToMM() {
            return ptmm;
         }

         public float getPixelUnitToMillimeter() {
            return ptmm;
         }

         public Rectangle2D getBBox() {
            return new Rectangle2D.Double(0.0, 0.0, view.getX(), view.getY());
         }

         public AffineTransform getScreenTransform() {
            throw new UnsupportedOperationException("NYI");
         }

         public void setScreenTransform(AffineTransform at) {
            throw new UnsupportedOperationException("NYI");
         }

         public AffineTransform getCTM() {
            return new AffineTransform();
         }

         public AffineTransform getGlobalTransform() {
            return new AffineTransform();
         }

         public float getViewportWidth() {
            return (float)view.getX();
         }

         public float getViewportHeight() {
            return (float)view.getY();
         }

         public float getFontSize() {
            return 12.0F;
         }

         public void deselectAll() {
         }
      };
      SVGOMElement e = (SVGOMElement)svgRoot;
      e.setSVGContext(dc);
      e.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/svg");
      int fontSize = 12;
      Point2D p2d = getSize(fontSize, svgRoot, this.getUserAgent().getSourcePixelUnitToMillimeter());
      e.setSVGContext((SVGContext)null);
      return p2d;
   }

   public static Point2D getSize(int size, Element svgRoot, float ptmm) {
      UnitProcessor.Context ctx = new PDFUnitContext(size, svgRoot, ptmm);
      String str = svgRoot.getAttributeNS((String)null, "width");
      if (str.length() == 0) {
         str = "100%";
      }

      float width = org.apache.batik.bridge.UnitProcessor.svgHorizontalLengthToUserSpace(str, "width", ctx);
      str = svgRoot.getAttributeNS((String)null, "height");
      if (str.length() == 0) {
         str = "100%";
      }

      float height = org.apache.batik.bridge.UnitProcessor.svgVerticalLengthToUserSpace(str, "height", ctx);
      return new Point2D.Float(width, height);
   }

   public static class PDFUnitContext implements UnitProcessor.Context {
      private Element e;
      private int fontSize;
      private float pixeltoMM;

      public PDFUnitContext(int size, Element e, float ptmm) {
         this.e = e;
         this.fontSize = size;
         this.pixeltoMM = ptmm;
      }

      public Element getElement() {
         return this.e;
      }

      public UnitProcessor.Context getParentElementContext() {
         return null;
      }

      public float getPixelToMM() {
         return this.pixeltoMM;
      }

      public float getPixelUnitToMillimeter() {
         return this.pixeltoMM;
      }

      public float getFontSize() {
         return (float)this.fontSize;
      }

      public float getXHeight() {
         return 0.5F;
      }

      public float getViewportWidth() {
         return 100.0F;
      }

      public float getViewportHeight() {
         return 100.0F;
      }
   }
}
