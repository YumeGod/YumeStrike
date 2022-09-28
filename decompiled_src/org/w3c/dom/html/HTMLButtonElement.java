package org.w3c.dom.html;

public interface HTMLButtonElement extends HTMLElement {
   HTMLFormElement getForm();

   String getAccessKey();

   void setAccessKey(String var1);

   boolean getDisabled();

   void setDisabled(boolean var1);

   String getName();

   void setName(String var1);

   int getTabIndex();

   void setTabIndex(int var1);

   String getType();

   String getValue();

   void setValue(String var1);
}
