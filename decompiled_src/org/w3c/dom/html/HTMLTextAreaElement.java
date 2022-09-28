package org.w3c.dom.html;

public interface HTMLTextAreaElement extends HTMLElement {
   String getDefaultValue();

   void setDefaultValue(String var1);

   HTMLFormElement getForm();

   String getAccessKey();

   void setAccessKey(String var1);

   int getCols();

   void setCols(int var1);

   boolean getDisabled();

   void setDisabled(boolean var1);

   String getName();

   void setName(String var1);

   boolean getReadOnly();

   void setReadOnly(boolean var1);

   int getRows();

   void setRows(int var1);

   int getTabIndex();

   void setTabIndex(int var1);

   String getType();

   String getValue();

   void setValue(String var1);

   void blur();

   void focus();

   void select();
}
