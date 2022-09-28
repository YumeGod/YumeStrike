package de.javasoft.plaf.synthetica;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class SyntheticaScrollPaneLayoutManager extends ScrollPaneLayout.UIResource {
   public void layoutContainer(Container var1) {
      super.layoutContainer(var1);
      Border var2 = var1 instanceof JComponent ? ((JComponent)var1).getBorder() : null;
      boolean var3 = var2 instanceof TitledBorder;
      boolean var4 = SyntheticaLookAndFeel.findDefaultBorder(var2) != null;
      if (var3 || var4) {
         JScrollPane var5 = (JScrollPane)var1;
         Component var6 = var5.getViewport().getView();
         Insets var7 = var5.getBorder().getBorderInsets(var5);
         Rectangle var8 = this.viewport.getBounds();
         boolean var9 = var5.getComponentOrientation().isLeftToRight();
         int var10 = 0;
         int var11 = 0;
         boolean var12 = var6 instanceof JTextComponent;
         boolean var13 = var6 instanceof JTable;
         Rectangle var14 = (Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.vertical.relativeBounds", (Component)var5);
         Rectangle var16;
         if (var14 != null) {
            Rectangle var15 = (Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.textComponent.vertical.relativeBounds", (Component)var5);
            var16 = (Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.table.vertical.relativeBounds", (Component)var5);
            var10 = var12 && var15 != null ? var15.x : (var13 && var16 != null ? var16.x : var14.x);
            var14 = (Rectangle)var14.clone();
         }

         if (var3 && SyntheticaLookAndFeel.get("Synthetica.scrollBar.titledBorder.vertical.relativeBounds", (Component)var5) != null) {
            var14 = (Rectangle)((Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.titledBorder.vertical.relativeBounds", (Component)var5)).clone();
         } else if (var12 && SyntheticaLookAndFeel.get("Synthetica.scrollBar.textComponent.vertical.relativeBounds", (Component)var5) != null) {
            var14 = (Rectangle)((Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.textComponent.vertical.relativeBounds", (Component)var5)).clone();
         } else if (var13 && SyntheticaLookAndFeel.get("Synthetica.scrollBar.table.vertical.relativeBounds", (Component)var5) != null) {
            var14 = (Rectangle)((Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.table.vertical.relativeBounds", (Component)var5)).clone();
         }

         boolean var18 = var6 instanceof JTable ? ((JTable)var6).getTableHeader() != null : false;
         if (var18 && var14 != null) {
            var14.y = 0;
            var14.height /= 2;
         }

         if (this.vsb != null && this.vsb.isVisible() && var14 != null && var7.right >= var14.x) {
            this.applyScrollBarBounds(this.vsb, var14, var9);
         }

         var14 = (Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.horizontal.relativeBounds", (Component)var5);
         if (var14 != null) {
            var16 = (Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.textComponent.horizontal.relativeBounds", (Component)var5);
            Rectangle var17 = (Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.table.horizontal.relativeBounds", (Component)var5);
            var11 = var12 && var16 != null ? var16.y : (var13 && var17 != null ? var17.y : var14.y);
            var14 = (Rectangle)var14.clone();
         }

         if (var3 && SyntheticaLookAndFeel.get("Synthetica.scrollBar.titledBorder.horizontal.relativeBounds", (Component)var5) != null) {
            var14 = (Rectangle)((Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.titledBorder.horizontal.relativeBounds", (Component)var5)).clone();
         } else if (var12 && SyntheticaLookAndFeel.get("Synthetica.scrollBar.textComponent.horizontal.relativeBounds", (Component)var5) != null) {
            var14 = (Rectangle)((Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.textComponent.horizontal.relativeBounds", (Component)var5)).clone();
         } else if (var13 && SyntheticaLookAndFeel.get("Synthetica.scrollBar.table.horizontal.relativeBounds", (Component)var5) != null) {
            var14 = (Rectangle)((Rectangle)SyntheticaLookAndFeel.get("Synthetica.scrollBar.table.horizontal.relativeBounds", (Component)var5)).clone();
         }

         if (this.hsb != null && this.hsb.isVisible() && var14 != null && var7.bottom >= var14.y) {
            this.applyScrollBarBounds(this.hsb, var14, true);
            var8.height += var14.y;
         }

         if (!var8.equals(this.viewport.getBounds())) {
            this.viewport.setBounds(var8);
         }

         if (this.lowerLeft != null) {
            this.lowerLeft.setLocation(this.lowerLeft.getX() - var10, this.lowerLeft.getY() + var11);
         }

         if (this.lowerRight != null) {
            this.lowerRight.setLocation(this.lowerRight.getX() + var10, this.lowerRight.getY() + var11);
         }
      }

   }

   private void applyScrollBarBounds(JScrollBar var1, Rectangle var2, boolean var3) {
      Rectangle var4 = var1.getBounds();
      var4.x += var3 ? var2.x : -var2.x;
      var4.y += var2.y;
      var4.width += var2.width;
      var4.height += var2.height;
      var1.setBounds(var4);
   }
}
