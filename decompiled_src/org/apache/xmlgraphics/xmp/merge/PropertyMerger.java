package org.apache.xmlgraphics.xmp.merge;

import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.XMPProperty;

public interface PropertyMerger {
   void merge(XMPProperty var1, Metadata var2);
}
