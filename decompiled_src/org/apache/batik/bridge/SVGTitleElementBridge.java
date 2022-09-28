package org.apache.batik.bridge;

public class SVGTitleElementBridge extends SVGDescriptiveElementBridge {
   public String getLocalName() {
      return "title";
   }

   public Bridge getInstance() {
      return new SVGTitleElementBridge();
   }
}
