package org.apache.fop.render.pcl.extensions;

import java.util.HashMap;
import org.apache.fop.fo.ElementMapping;
import org.apache.xmlgraphics.util.QName;

public class PCLElementMapping extends ElementMapping {
   public static final String NAMESPACE = "http://xmlgraphics.apache.org/fop/extensions/pcl";
   public static final String NAMESPACE_PREFIX = "pcl";
   public static final QName PCL_PAPER_SOURCE = new QName("http://xmlgraphics.apache.org/fop/extensions/pcl", (String)null, "paper-source");
   public static final QName PCL_OUTPUT_BIN = new QName("http://xmlgraphics.apache.org/fop/extensions/pcl", (String)null, "output-bin");
   public static final QName PCL_DUPLEX_MODE = new QName("http://xmlgraphics.apache.org/fop/extensions/pcl", (String)null, "duplex-mode");

   public PCLElementMapping() {
      this.namespaceURI = "http://xmlgraphics.apache.org/fop/extensions/pcl";
   }

   protected void initialize() {
      if (this.foObjs == null) {
         this.foObjs = new HashMap();
      }

   }
}
