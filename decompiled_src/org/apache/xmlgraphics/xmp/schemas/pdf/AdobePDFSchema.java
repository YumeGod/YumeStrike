package org.apache.xmlgraphics.xmp.schemas.pdf;

import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.XMPSchema;
import org.apache.xmlgraphics.xmp.merge.MergeRuleSet;

public class AdobePDFSchema extends XMPSchema {
   public static final String NAMESPACE = "http://ns.adobe.com/pdf/1.3/";
   private static MergeRuleSet mergeRuleSet = new MergeRuleSet();

   public AdobePDFSchema() {
      super("http://ns.adobe.com/pdf/1.3/", "pdf");
   }

   public static AdobePDFAdapter getAdapter(Metadata meta) {
      return new AdobePDFAdapter(meta, "http://ns.adobe.com/pdf/1.3/");
   }

   public MergeRuleSet getDefaultMergeRuleSet() {
      return mergeRuleSet;
   }
}
