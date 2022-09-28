package org.apache.wml;

import org.w3c.dom.Element;

public interface WMLElement extends Element {
   void setId(String var1);

   String getId();

   void setClassName(String var1);

   String getClassName();
}
