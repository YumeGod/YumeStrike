package org.w3c.dom.html;

public interface HTMLInputElement extends HTMLElement {
   String getDefaultValue();

   void setDefaultValue(String var1);

   boolean getDefaultChecked();

   void setDefaultChecked(boolean var1);

   HTMLFormElement getForm();

   String getAccept();

   void setAccept(String var1);

   String getAccessKey();

   void setAccessKey(String var1);

   String getAlign();

   void setAlign(String var1);

   String getAlt();

   void setAlt(String var1);

   boolean getChecked();

   void setChecked(boolean var1);

   boolean getDisabled();

   void setDisabled(boolean var1);

   int getMaxLength();

   void setMaxLength(int var1);

   String getName();

   void setName(String var1);

   boolean getReadOnly();

   void setReadOnly(boolean var1);

   String getSize();

   void setSize(String var1);

   String getSrc();

   void setSrc(String var1);

   int getTabIndex();

   void setTabIndex(int var1);

   String getType();

   String getUseMap();

   void setUseMap(String var1);

   String getValue();

   void setValue(String var1);

   void blur();

   void focus();

   void select();

   void click();
}
