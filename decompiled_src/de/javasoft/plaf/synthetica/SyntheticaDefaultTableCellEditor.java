package de.javasoft.plaf.synthetica;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Constructor;
import java.text.NumberFormat;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

public class SyntheticaDefaultTableCellEditor extends DefaultCellEditor {
   private static final long serialVersionUID = -4003145629974364170L;
   private Constructor constructor;
   private Object value;
   private boolean respectNumberLocale;

   public SyntheticaDefaultTableCellEditor() {
      this(new JTextField());
   }

   public SyntheticaDefaultTableCellEditor(JTextField var1) {
      this(var1, (Boolean)null);
   }

   public SyntheticaDefaultTableCellEditor(JTextField var1, Boolean var2) {
      super(var1);
      this.getComponent().setName("Table.editor");
      this.respectNumberLocale = var2 != null ? var2 : SyntheticaLookAndFeel.getBoolean("Synthetica.table.cellEditor.respectNumberLocale", (Component)null, false);
   }

   public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
      this.value = null;
      JComponent var6 = (JComponent)this.getComponent();
      var6.setBorder(this.createCellFocusBorder(var1));
      var6.setOpaque(SyntheticaLookAndFeel.getBoolean("Synthetica.table.cellEditor.opaque", var1));

      try {
         Class var7 = var1.getColumnClass(var5);
         if (this.respectNumberLocale && Number.class.isAssignableFrom(var7)) {
            var2 = NumberFormat.getInstance().format(var2);
         }

         if (var7 == Object.class) {
            var7 = String.class;
         }

         this.constructor = var7.getConstructor(String.class);
      } catch (Exception var8) {
      }

      return super.getTableCellEditorComponent(var1, var2, var3, var4, var5);
   }

   public Object getCellEditorValue() {
      return this.value;
   }

   public boolean stopCellEditing() {
      String var1 = (String)super.getCellEditorValue();
      if (var1 != null && var1.length() == 0) {
         if (this.constructor.getDeclaringClass() == String.class) {
            this.value = "";
         }

         super.stopCellEditing();
      }

      try {
         if (this.respectNumberLocale && Number.class.isAssignableFrom(this.constructor.getDeclaringClass())) {
            var1 = "" + NumberFormat.getNumberInstance().parse(var1);
         }

         this.value = this.constructor.newInstance(var1);
      } catch (Exception var4) {
         JComponent var3 = (JComponent)this.getComponent();
         var3.setBorder(this.createErrorCellFocusBorder((JTable)var3.getParent()));
         return false;
      }

      return super.stopCellEditing();
   }

   protected Border createCellFocusBorder(JTable var1) {
      Color var2 = SyntheticaLookAndFeel.getColor("Synthetica.table.focusCellHighlightBorder.color", var1);
      return new CellFocusBorder(SyntheticaLookAndFeel.getColor("Synthetica.table.cellEditor.focus.color", var1, var2));
   }

   protected Border createErrorCellFocusBorder(JTable var1) {
      return new CellFocusBorder(SyntheticaLookAndFeel.getColor("Synthetica.table.cellEditor.focus.errorColor", var1, new Color(15138816)));
   }

   public static class CellFocusBorder implements Border {
      private Color color;

      public CellFocusBorder(Color var1) {
         this.color = Color.GRAY;
         if (var1 != null) {
            this.color = var1;
         }

      }

      public Insets getBorderInsets(Component var1) {
         int var2 = this.getBorderSize(var1);
         if (var1.getParent() instanceof JTable) {
            JLabel var3 = new JLabel();
            var3.setName("Table.cellRenderer");
            SynthStyle var4 = SynthLookAndFeel.getStyleFactory().getStyle(var3, Region.LABEL);
            Insets var5 = var4.getInsets(new SynthContext(var3, Region.LABEL, var4, 0), (Insets)null);
            return var5;
         } else {
            return new Insets(var2, var2, var2, var2);
         }
      }

      public boolean isBorderOpaque() {
         return false;
      }

      public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
         Graphics2D var7 = (Graphics2D)var2;
         var7.setPaint(this.color);
         int var8 = this.getBorderSize(var1);

         for(int var9 = 0; var9 < var8; ++var9) {
            var7.draw(this.createShape((float)(var3 + var9), (float)(var4 + var9), (float)(var5 - var9 * 2 - 1), (float)(var6 - var9 * 2 - 1)));
         }

      }

      private int getBorderSize(Component var1) {
         return SyntheticaLookAndFeel.getInt("Synthetica.table.cellEditor.focus.size", var1, 1);
      }

      private Shape createShape(float var1, float var2, float var3, float var4) {
         return new Rectangle2D.Float(var1, var2, var3, var4);
      }
   }

   public static class NumberEditor extends SyntheticaDefaultTableCellEditor {
      private static final long serialVersionUID = 966057265129092767L;

      public NumberEditor() {
         ((JTextField)this.getComponent()).setHorizontalAlignment(4);
      }
   }
}
