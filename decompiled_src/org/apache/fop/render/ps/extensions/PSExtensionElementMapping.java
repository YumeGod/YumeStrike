package org.apache.fop.render.ps.extensions;

import java.util.HashMap;
import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.FONode;

public class PSExtensionElementMapping extends ElementMapping {
   public static final String NAMESPACE = "http://xmlgraphics.apache.org/fop/postscript";

   public PSExtensionElementMapping() {
      this.namespaceURI = "http://xmlgraphics.apache.org/fop/postscript";
   }

   protected void initialize() {
      if (this.foObjs == null) {
         this.foObjs = new HashMap();
         this.foObjs.put("ps-setup-code", new PSSetupCodeMaker());
         this.foObjs.put("ps-page-setup-code", new PSPageSetupCodeMaker());
         this.foObjs.put("ps-setpagedevice", new PSSetPageDeviceMaker());
         this.foObjs.put("ps-comment-before", new PSCommentBeforeMaker());
         this.foObjs.put("ps-comment-after", new PSCommentAfterMaker());
      }

   }

   static class PSCommentAfterMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PSCommentAfterElement(parent);
      }
   }

   static class PSCommentBeforeMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PSCommentBeforeElement(parent);
      }
   }

   static class PSSetPageDeviceMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PSSetPageDeviceElement(parent);
      }
   }

   static class PSPageSetupCodeMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PSPageSetupCodeElement(parent);
      }
   }

   static class PSSetupCodeMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new PSSetupCodeElement(parent);
      }
   }
}
