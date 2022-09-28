package org.apache.fop.render.afp.extensions;

import java.util.HashMap;
import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.FONode;

public class AFPElementMapping extends ElementMapping {
   public static final String TAG_LOGICAL_ELEMENT = "tag-logical-element";
   public static final String INCLUDE_PAGE_OVERLAY = "include-page-overlay";
   public static final String INCLUDE_PAGE_SEGMENT = "include-page-segment";
   public static final String INCLUDE_FORM_MAP = "include-form-map";
   public static final String NO_OPERATION = "no-operation";
   public static final String INVOKE_MEDIUM_MAP = "invoke-medium-map";
   public static final String NAMESPACE = "http://xmlgraphics.apache.org/fop/extensions/afp";
   public static final String NAMESPACE_PREFIX = "afp";

   public AFPElementMapping() {
      this.namespaceURI = "http://xmlgraphics.apache.org/fop/extensions/afp";
   }

   protected void initialize() {
      if (this.foObjs == null) {
         super.foObjs = new HashMap();
         this.foObjs.put("tag-logical-element", new AFPTagLogicalElementMaker());
         this.foObjs.put("include-page-segment", new AFPIncludePageSegmentMaker());
         this.foObjs.put("include-page-overlay", new AFPIncludePageOverlayMaker());
         this.foObjs.put("include-form-map", new AFPIncludeFormMapMaker());
         this.foObjs.put("no-operation", new AFPNoOperationMaker());
         this.foObjs.put("invoke-medium-map", new AFPInvokeMediumMapMaker());
      }

   }

   static class AFPInvokeMediumMapMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new AFPInvokeMediumMapElement(parent);
      }
   }

   static class AFPNoOperationMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new AFPPageSetupElement(parent, "no-operation");
      }
   }

   static class AFPTagLogicalElementMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new AFPPageSetupElement(parent, "tag-logical-element");
      }
   }

   static class AFPIncludeFormMapMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new AFPIncludeFormMapElement(parent, "include-form-map");
      }
   }

   static class AFPIncludePageSegmentMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new AFPPageSetupElement(parent, "include-page-segment");
      }
   }

   static class AFPIncludePageOverlayMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new AFPPageOverlayElement(parent, "include-page-overlay");
      }
   }
}
