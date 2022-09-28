package org.apache.batik.bridge;

import org.apache.batik.util.SVGConstants;

public abstract class AbstractSVGBridge implements Bridge, SVGConstants {
   protected AbstractSVGBridge() {
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/2000/svg";
   }

   public Bridge getInstance() {
      return this;
   }
}
