package org.apache.batik.ext.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JPanel;

public class JGridBagPanel extends JPanel implements GridBagConstants {
   public static final InsetsManager ZERO_INSETS = new ZeroInsetsManager();
   public static final InsetsManager DEFAULT_INSETS = new DefaultInsetsManager();
   public InsetsManager insetsManager;

   public JGridBagPanel() {
      this(new DefaultInsetsManager());
   }

   public JGridBagPanel(InsetsManager var1) {
      super(new GridBagLayout());
      if (var1 != null) {
         this.insetsManager = var1;
      } else {
         this.insetsManager = new DefaultInsetsManager();
      }

   }

   public void setLayout(LayoutManager var1) {
      if (var1 instanceof GridBagLayout) {
         super.setLayout(var1);
      }

   }

   public void add(Component var1, int var2, int var3, int var4, int var5, int var6, int var7, double var8, double var10) {
      Insets var12 = this.insetsManager.getInsets(var2, var3);
      GridBagConstraints var13 = new GridBagConstraints();
      var13.gridx = var2;
      var13.gridy = var3;
      var13.gridwidth = var4;
      var13.gridheight = var5;
      var13.anchor = var6;
      var13.fill = var7;
      var13.weightx = var8;
      var13.weighty = var10;
      var13.insets = var12;
      this.add(var1, var13);
   }

   private static class DefaultInsetsManager implements InsetsManager {
      int leftInset;
      int topInset;
      public Insets positiveInsets;
      public Insets leftInsets;
      public Insets topInsets;
      public Insets topLeftInsets;

      private DefaultInsetsManager() {
         this.leftInset = 5;
         this.topInset = 5;
         this.positiveInsets = new Insets(this.topInset, this.leftInset, 0, 0);
         this.leftInsets = new Insets(this.topInset, 0, 0, 0);
         this.topInsets = new Insets(0, this.leftInset, 0, 0);
         this.topLeftInsets = new Insets(0, 0, 0, 0);
      }

      public Insets getInsets(int var1, int var2) {
         if (var1 > 0) {
            return var2 > 0 ? this.positiveInsets : this.topInsets;
         } else {
            return var2 > 0 ? this.leftInsets : this.topLeftInsets;
         }
      }

      // $FF: synthetic method
      DefaultInsetsManager(Object var1) {
         this();
      }
   }

   private static class ZeroInsetsManager implements InsetsManager {
      private Insets insets;

      private ZeroInsetsManager() {
         this.insets = new Insets(0, 0, 0, 0);
      }

      public Insets getInsets(int var1, int var2) {
         return this.insets;
      }

      // $FF: synthetic method
      ZeroInsetsManager(Object var1) {
         this();
      }
   }

   public interface InsetsManager {
      Insets getInsets(int var1, int var2);
   }
}
