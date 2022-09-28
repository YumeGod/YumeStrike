package org.apache.xmlgraphics.xmp.merge;

import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.XMPProperty;

public class ReplacePropertyMerger implements PropertyMerger {
   public void merge(XMPProperty sourceProp, Metadata target) {
      target.setProperty(sourceProp);
   }
}
