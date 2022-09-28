package org.apache.fop.svg;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import javax.xml.parsers.SAXParserFactory;
import org.apache.batik.bridge.UserAgentAdapter;

public class SimpleSVGUserAgent extends UserAgentAdapter {
   private AffineTransform currentTransform = null;
   private float pixelUnitToMillimeter = 0.0F;

   public SimpleSVGUserAgent(float pixelUnitToMM, AffineTransform at) {
      this.pixelUnitToMillimeter = pixelUnitToMM;
      this.currentTransform = at;
   }

   public float getPixelUnitToMillimeter() {
      return this.pixelUnitToMillimeter;
   }

   public String getLanguages() {
      return "en";
   }

   public String getMedia() {
      return "print";
   }

   public String getUserStyleSheetURI() {
      return null;
   }

   public String getXMLParserClassName() {
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         return factory.newSAXParser().getXMLReader().getClass().getName();
      } catch (Exception var2) {
         return null;
      }
   }

   public boolean isXMLParserValidating() {
      return false;
   }

   public AffineTransform getTransform() {
      return this.currentTransform;
   }

   public void setTransform(AffineTransform at) {
      this.currentTransform = at;
   }

   public Dimension2D getViewportSize() {
      return new Dimension(100, 100);
   }
}
