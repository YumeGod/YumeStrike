package org.w3c.dom.html;

public interface HTMLSelectElement extends HTMLElement {
   String getType();

   int getSelectedIndex();

   void setSelectedIndex(int var1);

   String getValue();

   void setValue(String var1);

   int getLength();

   HTMLFormElement getForm();

   HTMLCollection getOptions();

   boolean getDisabled();

   void setDisabled(boolean var1);

   boolean getMultiple();

   void setMultiple(boolean var1);

   String getName();

   void setName(String var1);

   int getSize();

   void setSize(int var1);

   int getTabIndex();

   void setTabIndex(int var1);

   void add(HTMLElement var1, HTMLElement var2);

   void remove(int var1);

   void blur();

   void focus();
}
