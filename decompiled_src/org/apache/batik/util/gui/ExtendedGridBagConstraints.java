package org.apache.batik.util.gui;

import java.awt.GridBagConstraints;

public class ExtendedGridBagConstraints extends GridBagConstraints {
   public void setGridBounds(int var1, int var2, int var3, int var4) {
      this.gridx = var1;
      this.gridy = var2;
      this.gridwidth = var3;
      this.gridheight = var4;
   }

   public void setWeight(double var1, double var3) {
      this.weightx = var1;
      this.weighty = var3;
   }
}
