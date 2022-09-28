package de.javasoft.plaf.synthetica;

import de.javasoft.util.JavaVersion;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;

public class SyntheticaDefaultTableCellRenderer extends JLabel implements TableCellRenderer, Serializable, UIResource {
   private static final long serialVersionUID = 9059722839161202006L;
   private Border noFocusBorder;
   private Border focusBorder;
   private NumberFormat numberFormat;
   private DateFormat dateFormat;
   private Color alternateColor;
   private boolean colorCachingEnabled;
   private Color unselectedForeground;
   private Color unselectedBackground;

   public SyntheticaDefaultTableCellRenderer(TableCellRenderer var1) {
      this.setOpaque(true);
      Object var2 = var1 == null ? new EmptyBorder(0, 0, 0, 0) : ((JComponent)var1).getBorder();
      Insets var3 = var2 == null ? new Insets(0, 0, 0, 0) : ((Border)var2).getBorderInsets((Component)null);
      Border var4 = UIManager.getBorder("Table.focusCellHighlightBorder");
      Object var5 = var4 == null ? new EmptyBorder(0, 0, 0, 0) : var4;
      Insets var6 = ((Border)var5).getBorderInsets((Component)null);
      Insets var7 = new Insets(var3.top - var6.top, var3.left - var6.left, var3.bottom - var6.bottom, var3.right - var6.right);
      this.focusBorder = new CompoundBorder((Border)var5, new EmptyBorder(var7));
      this.noFocusBorder = new CompoundBorder(new EmptyBorder(var6), new EmptyBorder(var7));
      this.alternateColor = UIManager.getColor("Table.alternateRowColor");
      this.colorCachingEnabled = SyntheticaLookAndFeel.getBoolean("Synthetica.table.cellRenderer.colorCache.enabled", (Component)null, false);
   }

   public String getName() {
      String var1 = super.getName();
      if (var1 == null) {
         var1 = "Table.cellRenderer";
      }

      return var1;
   }

   public void setForeground(Color var1) {
      super.setForeground(var1);
      if (this.colorCachingEnabled) {
         this.unselectedForeground = var1;
      }

   }

   public void setBackground(Color var1) {
      super.setBackground(var1);
      if (this.colorCachingEnabled) {
         this.unselectedBackground = var1;
      }

   }

   public void updateUI() {
      super.updateUI();
      this.setForeground((Color)null);
      this.setBackground((Color)null);
   }

   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      Color var7 = null;
      Color var8 = null;
      if (!JavaVersion.JAVA5) {
         JTable.DropLocation var9 = var1.getDropLocation();
         if (var9 != null && !var9.isInsertRow() && !var9.isInsertColumn() && var9.getRow() == var5 && var9.getColumn() == var6) {
            var7 = UIManager.getColor("Table.dropCellForeground");
            var8 = UIManager.getColor("Table.dropCellBackground");
            var3 = true;
         }
      }

      if (var3) {
         if (var7 == null) {
            var7 = var1.getSelectionForeground();
         }

         if (var8 == null) {
            var8 = var1.getSelectionBackground();
         }
      } else {
         var7 = this.unselectedForeground == null ? var1.getForeground() : this.unselectedForeground;
         var8 = this.unselectedBackground == null ? var1.getBackground() : this.unselectedBackground;
         if ((var8 == null || var8 instanceof UIResource) && this.alternateColor != null && var5 % 2 == (JavaVersion.JAVA7_OR_ABOVE ? 1 : 0)) {
            var8 = this.alternateColor;
         }

         if (var4 && var1.isCellEditable(var5, var6)) {
            var7 = UIManager.get("Table.focusCellForeground") == null ? var7 : UIManager.getColor("Table.focusCellForeground");
            var8 = UIManager.get("Table.focusCellBackground") == null ? var8 : UIManager.getColor("Table.focusCellBackground");
         }
      }

      super.setForeground(var7);
      super.setBackground(var8);
      if (var4) {
         this.setBorder(this.focusBorder);
      } else {
         this.setBorder(this.noFocusBorder);
      }

      this.setFont(var1.getFont());
      this.setValue(var2);
      this.setIcon((Icon)null);
      this.configureValue(var2, var1.getColumnClass(var6));
      return this;
   }

   protected void setValue(Object var1) {
      this.setText(var1 == null ? "" : var1.toString());
   }

   private void configureValue(Object var1, Class var2) {
      if (var2 != Object.class && var2 != null) {
         if (var2 != Float.class && var2 != Double.class) {
            if (var2 == Number.class) {
               this.setHorizontalAlignment(11);
            } else if (var2 == Date.class) {
               if (this.dateFormat == null) {
                  this.dateFormat = DateFormat.getDateInstance();
               }

               this.setHorizontalAlignment(10);
               this.setText(var1 == null ? "" : this.dateFormat.format(var1));
            } else if (var2 != Icon.class && var2 != ImageIcon.class) {
               this.configureValue(var1, var2.getSuperclass());
            } else {
               this.setHorizontalAlignment(0);
               this.setIcon(var1 instanceof Icon ? (Icon)var1 : null);
               this.setText("");
            }
         } else {
            if (this.numberFormat == null) {
               this.numberFormat = NumberFormat.getInstance();
            }

            this.setHorizontalAlignment(11);
            this.setText(var1 == null ? "" : this.numberFormat.format(var1));
         }
      } else {
         this.setHorizontalAlignment(10);
      }

   }

   public boolean isOpaque() {
      Color var1 = this.getBackground();
      Container var2 = this.getParent();
      if (var2 != null) {
         var2 = var2.getParent();
      }

      boolean var3 = var1 != null && var2 != null && var1.equals(var2.getBackground()) && var2.isOpaque();
      return !var3 && super.isOpaque();
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

   protected void firePropertyChange(String var1, Object var2, Object var3) {
      if (var1 == "text" || var1 == "labelFor" || var1 == "displayedMnemonic" || (var1 == "font" || var1 == "foreground") && var2 != var3 && this.getClientProperty("html") != null) {
         super.firePropertyChange(var1, var2, var3);
      }

   }

   public void firePropertyChange(String var1, boolean var2, boolean var3) {
   }
}
