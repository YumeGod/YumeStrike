package de.javasoft.plaf.synthetica;

import de.javasoft.util.JavaVersion;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class SyntheticaComboBoxLayoutManager implements LayoutManager2 {
   private int cachedMinWidth;

   public void addLayoutComponent(String var1, Component var2) {
   }

   public void removeLayoutComponent(Component var1) {
   }

   public Dimension preferredLayoutSize(Container var1) {
      return var1.getPreferredSize();
   }

   public Dimension minimumLayoutSize(Container var1) {
      return var1.getMinimumSize();
   }

   public void layoutContainer(Container var1) {
      this.layoutComboBox(var1);
   }

   private void layoutComboBox(Container var1) {
      JComboBox var2 = (JComboBox)var1;
      JButton var3 = (JButton)SyntheticaLookAndFeel.findComponent(JButton.class, var1);
      Insets var5;
      int var7;
      if (var3 != null) {
         Insets var4 = var2.getInsets();
         var5 = var3.getInsets();
         int var6 = var3.getPreferredSize().width + var5.left + var5.right;
         var7 = SyntheticaLookAndFeel.getInt("Synthetica.comboBox.button.xOffset", var2, 0);
         if ((Boolean)SyntheticaLookAndFeel.getClientProperty("JComboBox.isTableCellEditor", var2, false) && !"JFileChooser.DirectoryComboBox".equals(var2.getName())) {
            var7 = SyntheticaLookAndFeel.getInt("Synthetica.comboBox.button.tableCellEditor.xOffset", var2, 0);
         }

         var3.setBounds(var2.getComponentOrientation().isLeftToRight() ? var2.getWidth() - var4.right - var6 + var7 : var4.left - var7, var4.top, var6, var2.getHeight() - var4.top - var4.bottom);
      }

      ComboBoxEditor var10 = var2.getEditor();
      if (var10 != null) {
         var5 = var2.getInsets();
         Rectangle var11 = null;
         var7 = var2.getWidth();
         int var8 = var2.getHeight();
         int var9 = var3 == null ? var8 - (var5.top + var5.bottom) : var3.getWidth();
         if (var2.getComponentOrientation().isLeftToRight()) {
            var11 = new Rectangle(var5.left, var5.top, var7 - (var5.left + var5.right + var9), var8 - (var5.top + var5.bottom));
         } else {
            var11 = new Rectangle(var5.left + var9, var5.top, var7 - (var5.left + var5.right + var9), var8 - (var5.top + var5.bottom));
         }

         var10.getEditorComponent().setBounds(var11);
      }

   }

   public void addLayoutComponent(Component var1, Object var2) {
   }

   public float getLayoutAlignmentX(Container var1) {
      return 0.5F;
   }

   public float getLayoutAlignmentY(Container var1) {
      return 0.5F;
   }

   public void invalidateLayout(Container var1) {
      if (JavaVersion.JAVA5) {
         JComboBox var2 = (JComboBox)var1;
         Dimension var3 = var2.getPreferredSize();
         JButton var4 = (JButton)SyntheticaLookAndFeel.findComponent((Class)JButton.class, var2);
         int var5 = var4 == null ? 0 : var4.getInsets().left + var4.getInsets().right + var4.getPreferredSize().width - 16;
         Insets var6 = UIManager.getInsets("Synthetica.comboPopup.insets");
         if (var6 == null) {
            var6 = new Insets(0, 0, 0, 0);
         }

         int var7 = var6.left + var6.right - var2.getInsets().left - var2.getInsets().right;
         var6 = UIManager.getInsets("Synthetica.comboBox.viewport.border.insets");
         var7 += var6 == null ? 0 : var6.left + var6.right;
         var5 = Math.max(var5, var7);
         if (SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
            BasicComboBoxUI var8 = (BasicComboBoxUI)var2.getUI();

            try {
               Class var9 = Class.forName("javax.swing.plaf.basic.BasicComboBoxUI");
               Field var10 = var9.getDeclaredField("cachedMinimumSize");
               var10.setAccessible(true);
               Dimension var11 = (Dimension)var10.get(var8);
               if (this.cachedMinWidth != var11.width) {
                  this.cachedMinWidth = var3.width + var5;
                  var10.set(var8, new Dimension(this.cachedMinWidth, var3.height));
               }
            } catch (Exception var12) {
               throw new RuntimeException(var12);
            }
         }

      }
   }

   public Dimension maximumLayoutSize(Container var1) {
      return null;
   }
}
