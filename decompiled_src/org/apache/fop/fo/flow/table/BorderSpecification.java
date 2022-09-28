package org.apache.fop.fo.flow.table;

import org.apache.fop.fo.properties.CommonBorderPaddingBackground;

public class BorderSpecification {
   private static BorderSpecification defaultBorder;
   private CommonBorderPaddingBackground.BorderInfo borderInfo;
   private int holder;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public BorderSpecification(CommonBorderPaddingBackground.BorderInfo borderInfo, int holder) {
      this.borderInfo = borderInfo;
      this.holder = holder;
   }

   static synchronized BorderSpecification getDefaultBorder() {
      if (defaultBorder == null) {
         defaultBorder = new BorderSpecification(CommonBorderPaddingBackground.getDefaultBorderInfo(), 75);
      }

      return defaultBorder;
   }

   public CommonBorderPaddingBackground.BorderInfo getBorderInfo() {
      return this.borderInfo;
   }

   public int getHolder() {
      return this.holder;
   }

   public String toString() {
      String holderName = "";
      switch (this.holder) {
         case 71:
            holderName = "table";
            break;
         case 72:
         case 74:
         default:
            if (!$assertionsDisabled) {
               throw new AssertionError();
            }
            break;
         case 73:
            holderName = "table-body";
            break;
         case 75:
            holderName = "table-cell";
            break;
         case 76:
            holderName = "table-column";
            break;
         case 77:
            holderName = "table-footer";
            break;
         case 78:
            holderName = "table-header";
            break;
         case 79:
            holderName = "table-row";
      }

      return "{" + this.borderInfo + ", " + holderName + "}";
   }

   static {
      $assertionsDisabled = !BorderSpecification.class.desiredAssertionStatus();
   }
}
