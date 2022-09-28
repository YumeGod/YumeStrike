package de.javasoft.plaf.synthetica;

import de.javasoft.util.JavaVersion;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;

public class SyntheticaDefaultBooleanTableCellRenderer extends JCheckBox implements TableCellRenderer, UIResource {
   private static final long serialVersionUID = 2605436985980373338L;
   private boolean isRowSelected;
   private Border noFocusBorder;
   private Border focusBorder;
   private Color alternateColor;

   public SyntheticaDefaultBooleanTableCellRenderer(TableCellRenderer var1) {
      this.setName("Table.cellRenderer");
      this.setHorizontalAlignment(0);
      Object var2 = var1 == null ? new EmptyBorder(0, 0, 0, 0) : ((JComponent)var1).getBorder();
      Insets var3 = var2 == null ? new Insets(0, 0, 0, 0) : ((Border)var2).getBorderInsets((Component)null);
      Border var4 = UIManager.getBorder("Table.focusCellHighlightBorder");
      Object var5 = var4 == null ? new EmptyBorder(0, 0, 0, 0) : var4;
      Insets var6 = ((Border)var5).getBorderInsets((Component)null);
      Insets var7 = new Insets(var3.top - var6.top, var3.left - var6.left, var3.bottom - var6.bottom, var3.right - var6.right);
      this.focusBorder = new CompoundBorder((Border)var5, new EmptyBorder(var7));
      this.noFocusBorder = new CompoundBorder(new EmptyBorder(var6), new EmptyBorder(var7));
      this.alternateColor = SyntheticaLookAndFeel.getColor("Table.alternateRowColor", (Component)null);
   }

   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      this.isRowSelected = var3;
      if (var3) {
         this.setForeground(this.unwrap(var1.getSelectionForeground()));
         this.setBackground(this.unwrap(var1.getSelectionBackground()));
      } else {
         this.setForeground(this.unwrap(var1.getForeground()));
         if (this.alternateColor != null && var5 % 2 == (JavaVersion.JAVA7_OR_ABOVE ? 1 : 0)) {
            this.setBackground(this.alternateColor);
         } else {
            this.setBackground(this.unwrap(var1.getBackground()));
         }
      }

      this.setSelected(var2 != null && (Boolean)var2);
      if (var4) {
         this.setBorder(this.focusBorder);
      } else {
         this.setBorder(this.noFocusBorder);
      }

      return this;
   }

   private Color unwrap(Color var1) {
      return var1 instanceof UIResource ? new Color(var1.getRGB()) : var1;
   }

   public boolean isOpaque() {
      return this.isRowSelected ? true : super.isOpaque();
   }

   public boolean isBorderPainted() {
      return true;
   }

   public void invalidate() {
   }

   public void validate() {
   }

   public void revalidate() {
   }

   public void repaint(long var1, int var3, int var4, int var5, int var6) {
   }

   public void repaint(Rectangle var1) {
   }

   public void repaint() {
   }

   public void firePropertyChange(String var1, boolean var2, boolean var3) {
   }
}
