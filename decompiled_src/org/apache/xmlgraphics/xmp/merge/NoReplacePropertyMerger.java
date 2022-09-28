package org.apache.xmlgraphics.xmp.merge;

import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.XMPProperty;

public class NoReplacePropertyMerger implements PropertyMerger {
   public void merge(XMPProperty sourceProp, Metadata target) {
      XMPProperty prop = target.getProperty(sourceProp.getName());
      if (prop == null) {
         target.setProperty(sourceProp);
      }

   }
}
