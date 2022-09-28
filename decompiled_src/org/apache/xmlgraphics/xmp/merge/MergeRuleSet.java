package org.apache.xmlgraphics.xmp.merge;

import java.util.HashMap;
import java.util.Map;
import org.apache.xmlgraphics.util.QName;
import org.apache.xmlgraphics.xmp.XMPProperty;

public class MergeRuleSet {
   private Map rules = new HashMap();
   private PropertyMerger defaultMerger = new ReplacePropertyMerger();

   public PropertyMerger getPropertyMergerFor(XMPProperty prop) {
      PropertyMerger merger = (PropertyMerger)this.rules.get(prop.getName());
      return merger != null ? merger : this.defaultMerger;
   }

   public void addRule(QName propName, PropertyMerger merger) {
      this.rules.put(propName, merger);
   }
}
