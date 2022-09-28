package org.apache.xerces.impl.xs.traversers;

class OneElement {
   public Container attrList;
   public boolean allowNonSchemaAttr;

   public OneElement(Container var1) {
      this(var1, true);
   }

   public OneElement(Container var1, boolean var2) {
      this.attrList = var1;
      this.allowNonSchemaAttr = var2;
   }
}
