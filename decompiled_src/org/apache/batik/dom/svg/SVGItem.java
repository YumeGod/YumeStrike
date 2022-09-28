package org.apache.batik.dom.svg;

public interface SVGItem {
   void setParent(AbstractSVGList var1);

   AbstractSVGList getParent();

   String getValueAsString();
}
