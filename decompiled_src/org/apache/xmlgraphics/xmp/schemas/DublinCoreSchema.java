package org.apache.xmlgraphics.xmp.schemas;

import org.apache.xmlgraphics.util.QName;
import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.XMPSchema;
import org.apache.xmlgraphics.xmp.merge.ArrayAddPropertyMerger;
import org.apache.xmlgraphics.xmp.merge.MergeRuleSet;

public class DublinCoreSchema extends XMPSchema {
   public static final String NAMESPACE = "http://purl.org/dc/elements/1.1/";
   private static MergeRuleSet dcMergeRuleSet = new MergeRuleSet();

   public DublinCoreSchema() {
      super("http://purl.org/dc/elements/1.1/", "dc");
   }

   public static DublinCoreAdapter getAdapter(Metadata meta) {
      return new DublinCoreAdapter(meta);
   }

   public MergeRuleSet getDefaultMergeRuleSet() {
      return dcMergeRuleSet;
   }

   static {
      dcMergeRuleSet.addRule(new QName("http://purl.org/dc/elements/1.1/", "date"), new ArrayAddPropertyMerger());
   }
}
