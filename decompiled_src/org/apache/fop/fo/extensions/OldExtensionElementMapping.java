package org.apache.fop.fo.extensions;

import java.util.HashMap;
import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.UnknownXMLObj;

public class OldExtensionElementMapping extends ElementMapping {
   public static final String URI = "http://xml.apache.org/fop/extensions";

   public OldExtensionElementMapping() {
      this.namespaceURI = "http://xml.apache.org/fop/extensions";
   }

   protected void initialize() {
      if (this.foObjs == null) {
         this.foObjs = new HashMap();
         this.foObjs.put("outline", new UnknownXMLObj.Maker("http://xml.apache.org/fop/extensions"));
         this.foObjs.put("label", new UnknownXMLObj.Maker("http://xml.apache.org/fop/extensions"));
      }

   }
}
