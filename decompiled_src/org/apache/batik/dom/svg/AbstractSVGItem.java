package org.apache.batik.dom.svg;

public abstract class AbstractSVGItem implements SVGItem {
   protected AbstractSVGList parent;
   protected String itemStringValue;

   protected abstract String getStringValue();

   protected AbstractSVGItem() {
   }

   public void setParent(AbstractSVGList var1) {
      this.parent = var1;
   }

   public AbstractSVGList getParent() {
      return this.parent;
   }

   protected void resetAttribute() {
      if (this.parent != null) {
         this.itemStringValue = null;
         this.parent.itemChanged();
      }

   }

   public String getValueAsString() {
      if (this.itemStringValue == null) {
         this.itemStringValue = this.getStringValue();
      }

      return this.itemStringValue;
   }
}
