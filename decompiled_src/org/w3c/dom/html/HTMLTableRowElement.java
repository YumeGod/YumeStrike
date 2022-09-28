package org.w3c.dom.html;

public interface HTMLTableRowElement extends HTMLElement {
   int getRowIndex();

   void setRowIndex(int var1);

   int getSectionRowIndex();

   void setSectionRowIndex(int var1);

   HTMLCollection getCells();

   void setCells(HTMLCollection var1);

   String getAlign();

   void setAlign(String var1);

   String getBgColor();

   void setBgColor(String var1);

   String getCh();

   void setCh(String var1);

   String getChOff();

   void setChOff(String var1);

   String getVAlign();

   void setVAlign(String var1);

   HTMLElement insertCell(int var1);

   void deleteCell(int var1);
}
