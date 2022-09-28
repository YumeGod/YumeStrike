package org.w3c.dom.html;

public interface HTMLTableElement extends HTMLElement {
   HTMLTableCaptionElement getCaption();

   void setCaption(HTMLTableCaptionElement var1);

   HTMLTableSectionElement getTHead();

   void setTHead(HTMLTableSectionElement var1);

   HTMLTableSectionElement getTFoot();

   void setTFoot(HTMLTableSectionElement var1);

   HTMLCollection getRows();

   HTMLCollection getTBodies();

   String getAlign();

   void setAlign(String var1);

   String getBgColor();

   void setBgColor(String var1);

   String getBorder();

   void setBorder(String var1);

   String getCellPadding();

   void setCellPadding(String var1);

   String getCellSpacing();

   void setCellSpacing(String var1);

   String getFrame();

   void setFrame(String var1);

   String getRules();

   void setRules(String var1);

   String getSummary();

   void setSummary(String var1);

   String getWidth();

   void setWidth(String var1);

   HTMLElement createTHead();

   void deleteTHead();

   HTMLElement createTFoot();

   void deleteTFoot();

   HTMLElement createCaption();

   void deleteCaption();

   HTMLElement insertRow(int var1);

   void deleteRow(int var1);
}
