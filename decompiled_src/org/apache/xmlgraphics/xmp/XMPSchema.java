package org.apache.xmlgraphics.xmp;

import org.apache.xmlgraphics.util.QName;
import org.apache.xmlgraphics.xmp.merge.MergeRuleSet;

public class XMPSchema {
   private static MergeRuleSet defaultMergeRuleSet = new MergeRuleSet();
   private String namespace;
   private String prefix;

   public XMPSchema(String namespace, String preferredPrefix) {
      this.namespace = namespace;
      this.prefix = preferredPrefix;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public String getPreferredPrefix() {
      return this.prefix;
   }

   protected QName getQName(String propName) {
      return new QName(this.getNamespace(), propName);
   }

   public MergeRuleSet getDefaultMergeRuleSet() {
      return defaultMergeRuleSet;
   }
}
