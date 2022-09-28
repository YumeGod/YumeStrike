package de.javasoft.plaf.synthetica;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

class SyntheticaInternalFrameTitlePaneLayout5 implements LayoutManager {
   private JInternalFrame frame;
   private BasicInternalFrameTitlePane titlePane;
   private JButton menuButton;
   private JButton iconButton;
   private JButton maxButton;
   private JButton closeButton;
   private int buttonSpacing = 2;
   private int titleSpacing = 2;

   public SyntheticaInternalFrameTitlePaneLayout5(BasicInternalFrameTitlePane var1) {
      this.titlePane = var1;
      Container var2 = var1.getParent();
      if (var2 instanceof JInternalFrame) {
         this.frame = (JInternalFrame)var2;
      } else {
         this.frame = ((JInternalFrame.JDesktopIcon)var2).getInternalFrame();
      }

      this.menuButton = (JButton)SyntheticaLookAndFeel.findComponent((String)"InternalFrameTitlePane.menuButton", var1);
      this.iconButton = (JButton)SyntheticaLookAndFeel.findComponent((String)"InternalFrameTitlePane.iconifyButton", var1);
      this.maxButton = (JButton)SyntheticaLookAndFeel.findComponent((String)"InternalFrameTitlePane.maximizeButton", var1);
      this.closeButton = (JButton)SyntheticaLookAndFeel.findComponent((String)"InternalFrameTitlePane.closeButton", var1);
   }

   public void addLayoutComponent(String var1, Component var2) {
   }

   public void removeLayoutComponent(Component var1) {
   }

   public Dimension preferredLayoutSize(Container var1) {
      return this.minimumLayoutSize(var1);
   }

   public Dimension minimumLayoutSize(Container var1) {
      int var2 = 0;
      int var3 = 0;
      int var4 = 0;
      Dimension var5 = this.menuButton.getPreferredSize();
      var2 += var5.width;
      var3 = Math.max(var5.height, var3);
      if (this.frame.isClosable()) {
         ++var4;
         var5 = this.closeButton.getPreferredSize();
         var2 += var5.width;
         var3 = Math.max(var5.height, var3);
      }

      if (this.frame.isMaximizable()) {
         ++var4;
         var5 = this.maxButton.getPreferredSize();
         var2 += var5.width;
         var3 = Math.max(var5.height, var3);
      }

      if (this.frame.isIconifiable()) {
         ++var4;
         var5 = this.iconButton.getPreferredSize();
         var2 += var5.width;
         var3 = Math.max(var5.height, var3);
      }

      var2 += var4 > 0 ? (var4 - 1) * this.buttonSpacing : 0;
      FontMetrics var6 = this.titlePane.getFontMetrics(this.titlePane.getFont());
      var3 = Math.max(var6.getHeight() + 2, var3);
      String var7 = this.frame.getTitle();
      int var8 = var7 != null ? SwingUtilities.computeStringWidth(var6, var7) : 0;
      int var9 = var7 != null ? var7.length() : 0;
      if (var9 > 3) {
         int var10 = SwingUtilities.computeStringWidth(var6, var7.substring(0, 3) + "...");
         var2 += var8 < var10 ? var8 : var10;
      } else {
         var2 += var8;
      }

      var2 += this.titleSpacing * 2;
      Insets var11 = this.titlePane.getInsets();
      var3 += var11.top + var11.bottom;
      var2 += var11.left + var11.right;
      return new Dimension(var2, var3);
   }

   public void layoutContainer(Container var1) {
      Insets var2 = var1.getInsets();
      int var3;
      if (this.frame.getComponentOrientation().isLeftToRight()) {
         this.setButtonBounds(this.menuButton, var2.left, false);
         var3 = this.titlePane.getWidth() - var2.right;
         if (this.frame.isClosable()) {
            var3 = this.setButtonBounds(this.closeButton, var3, true);
         }

         if (this.frame.isMaximizable()) {
            var3 = this.setButtonBounds(this.maxButton, var3, true);
         }

         if (this.frame.isIconifiable()) {
            this.setButtonBounds(this.iconButton, var3, true);
         }
      } else {
         this.setButtonBounds(this.menuButton, this.titlePane.getWidth() - var2.right, true);
         var3 = var2.left;
         if (this.frame.isClosable()) {
            var3 = this.setButtonBounds(this.closeButton, var3, false);
         }

         if (this.frame.isMaximizable()) {
            var3 = this.setButtonBounds(this.maxButton, var3, false);
         }

         if (this.frame.isIconifiable()) {
            this.setButtonBounds(this.iconButton, var3, false);
         }
      }

   }

   private int setButtonBounds(JButton var1, int var2, boolean var3) {
      Insets var4 = this.titlePane.getInsets();
      Dimension var5 = var1.getPreferredSize();
      if (var3) {
         var2 -= var5.width;
      }

      var1.setBounds(var2, var4.top + (this.titlePane.getHeight() - var4.top - var4.bottom - var5.height) / 2, var5.width, var5.height);
      return var3 ? var2 - this.buttonSpacing : var2 + var5.width + this.buttonSpacing;
   }
}
