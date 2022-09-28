package org.apache.wml;

public interface WMLInputElement extends WMLElement {
   void setName(String var1);

   String getName();

   void setValue(String var1);

   String getValue();

   void setType(String var1);

   String getType();

   void setFormat(String var1);

   String getFormat();

   void setEmptyOk(boolean var1);

   boolean getEmptyOk();

   void setSize(int var1);

   int getSize();

   void setMaxLength(int var1);

   int getMaxLength();

   void setTitle(String var1);

   String getTitle();

   void setTabIndex(int var1);

   int getTabIndex();

   void setXmlLang(String var1);

   String getXmlLang();
}
