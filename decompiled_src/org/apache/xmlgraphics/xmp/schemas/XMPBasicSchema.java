package org.apache.xmlgraphics.xmp.schemas;

import org.apache.xmlgraphics.util.QName;
import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.XMPSchema;
import org.apache.xmlgraphics.xmp.merge.MergeRuleSet;
import org.apache.xmlgraphics.xmp.merge.NoReplacePropertyMerger;

public class XMPBasicSchema extends XMPSchema {
   public static final String NAMESPACE = "http://ns.adobe.com/xap/1.0/";
   public static final String PREFERRED_PREFIX = "xmp";
   public static final String QUALIFIER_NAMESPACE = "http://ns.adobe.com/xmp/Identifier/qual/1.0/";
   public static final QName SCHEME_QUALIFIER = new QName("http://ns.adobe.com/xmp/Identifier/qual/1.0/", "xmpidq:Scheme");
   private static MergeRuleSet mergeRuleSet = new MergeRuleSet();

   public XMPBasicSchema() {
      super("http://ns.adobe.com/xap/1.0/", "xmp");
   }

   public static XMPBasicAdapter getAdapter(Metadata meta) {
      return new XMPBasicAdapter(meta, "http://ns.adobe.com/xap/1.0/");
   }

   public MergeRuleSet getDefaultMergeRuleSet() {
      return mergeRuleSet;
   }

   static {
      mergeRuleSet.addRule(new QName("http://ns.adobe.com/xap/1.0/", "CreateDate"), new NoReplacePropertyMerger());
   }
}
