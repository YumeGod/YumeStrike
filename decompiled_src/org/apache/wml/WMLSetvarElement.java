package org.apache.wml;

public interface WMLSetvarElement extends WMLElement {
   void setValue(String var1);

   String getValue();

   void setName(String var1);

   String getName();
}
