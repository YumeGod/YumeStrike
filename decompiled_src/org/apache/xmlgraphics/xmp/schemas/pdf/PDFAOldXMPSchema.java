package org.apache.xmlgraphics.xmp.schemas.pdf;

import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.XMPSchema;
import org.apache.xmlgraphics.xmp.merge.MergeRuleSet;

/** @deprecated */
public class PDFAOldXMPSchema extends XMPSchema {
   public static final String NAMESPACE = "http://www.aiim.org/pdfa/ns/id.html";
   private static MergeRuleSet mergeRuleSet = new MergeRuleSet();

   public PDFAOldXMPSchema() {
      super("http://www.aiim.org/pdfa/ns/id.html", "pdfaid_1");
   }

   public static PDFAAdapter getAdapter(Metadata meta) {
      return new PDFAAdapter(meta, "http://www.aiim.org/pdfa/ns/id.html");
   }

   public MergeRuleSet getDefaultMergeRuleSet() {
      return mergeRuleSet;
   }
}
