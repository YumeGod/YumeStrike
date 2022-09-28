package org.apache.wml;

public interface WMLOptionElement extends WMLElement {
   void setValue(String var1);

   String getValue();

   void setTitle(String var1);

   String getTitle();

   void setOnPick(String var1);

   String getOnPick();

   void setXmlLang(String var1);

   String getXmlLang();
}
