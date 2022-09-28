package de.javasoft.plaf.synthetica;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.plaf.UIResource;

public class SyntheticaSpinnerLayoutManager implements LayoutManager, UIResource {
   private static Insets editorInsets = (new JFormattedTextField(0)).getInsets();
   private JComponent nextButton = null;
   private JComponent previousButton = null;
   private JComponent editor = null;

   public void addLayoutComponent(String var1, Component var2) {
      if ("Next".equals(var1)) {
         this.nextButton = (JComponent)var2;
      } else if ("Previous".equals(var1)) {
         this.previousButton = (JComponent)var2;
      } else if ("Editor".equals(var1)) {
         this.editor = (JComponent)var2;
      }

   }

   public void removeLayoutComponent(Component var1) {
      if (var1 == this.nextButton) {
         this.nextButton = null;
      } else if (var1 == this.previousButton) {
         this.previousButton = null;
      } else if (var1 == this.editor) {
         this.editor = null;
      }

   }

   public Dimension preferredLayoutSize(Container var1) {
      Dimension var2 = this.getPrefSize(this.nextButton);
      Dimension var3 = this.getPrefSize(this.previousButton);
      Dimension var4 = this.getPrefSize(this.editor);
      var4.height = (var4.height + 1) / 2 * 2 + editorInsets.top + editorInsets.bottom;
      Dimension var5 = new Dimension(var4.width, var4.height);
      var5.width += Math.max(var2.width, var3.width);
      Insets var6 = var1.getInsets();
      var5.width += var6.left + var6.right;
      return var5;
   }

   private Dimension getPrefSize(Component var1) {
      return var1 == null ? new Dimension(0, 0) : var1.getPreferredSize();
   }

   public Dimension minimumLayoutSize(Container var1) {
      return this.preferredLayoutSize(var1);
   }

   public void layoutContainer(Container var1) {
      Insets var2 = var1.getInsets();
      int var3 = var1.getWidth() - var2.left - var2.right;
      int var4 = var1.getHeight() - var2.top - var2.bottom;
      int var5 = Math.max(this.getPrefSize(this.nextButton).width, this.getPrefSize(this.previousButton).width);
      int var6 = var3 - var5;
      int var7;
      int var8;
      if (var1.getComponentOrientation().isLeftToRight()) {
         var7 = var2.left;
         var8 = var7 + var6;
      } else {
         var8 = var2.left;
         var7 = var8 + var5;
      }

      this.setChildBounds(this.editor, var7, var2.top, var6, var4);
      int var9 = var4 / 2;
      int var10 = var4 - var9;
      this.setChildBounds(this.nextButton, var8, var2.top, var5, var9);
      this.setChildBounds(this.previousButton, var8, var2.top + var9, var5, var10);
   }

   private void setChildBounds(Component var1, int var2, int var3, int var4, int var5) {
      if (var1 != null) {
         var1.setBounds(var2, var3, var4, var5);
      }

   }
}
