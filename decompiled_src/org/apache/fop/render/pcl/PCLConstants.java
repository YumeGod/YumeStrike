package org.apache.fop.render.pcl;

import org.apache.xmlgraphics.util.QName;

interface PCLConstants {
   QName SRC_TRANSPARENCY = new QName("http://xmlgraphics.apache.org/fop/extensions", (String)null, "source-transparency");
   Object DISABLE_CLIPPING = new QName("http://xmlgraphics.apache.org/fop/extensions", (String)null, "disable-clipping");
   Object COLOR_CANVAS = new QName("http://xmlgraphics.apache.org/fop/extensions", (String)null, "color-canvas");
}
