package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.GraphicsUtils;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.lang.reflect.Field;
import javax.swing.CellRendererPane;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultTreeCellRenderer;

public class LabelPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.LabelPainter";
   private static boolean cellRendererSelectionBackgroundEnabled;
   private static boolean listSelectionBackgroundEnabled;
   private static boolean treeSelectionBackgroundEnabled;
   private static boolean tableSelectionBackgroundEnabled;
   private static SynthStyle listStyle;
   private static int listHash;
   private static SynthStyle treeStyle;
   private static int treeHash;
   private static SynthStyle tableStyle;
   private static int tableHash;

   protected LabelPainter() {
   }

   public static LabelPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static LabelPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, LabelPainter.class, "Synthetica.LabelPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, LabelPainter.class, "Synthetica.LabelPainter");
      }

      return (LabelPainter)var1;
   }

   public static void reinitialize() {
      cellRendererSelectionBackgroundEnabled = SyntheticaLookAndFeel.getBoolean("Synthetica.cellRenderer.selectionBackground.enabled", (Component)null, false);
      listSelectionBackgroundEnabled = SyntheticaLookAndFeel.getBoolean("Synthetica.list.labelCellRenderer.selectionBackground.enabled", (Component)null, true);
      treeSelectionBackgroundEnabled = SyntheticaLookAndFeel.getBoolean("Synthetica.tree.labelCellRenderer.selectionBackground.enabled", (Component)null, true);
      tableSelectionBackgroundEnabled = SyntheticaLookAndFeel.getBoolean("Synthetica.table.labelCellRenderer.selectionBackground.enabled", (Component)null, true);
      listHash = 0;
      treeHash = 0;
      tableHash = 0;
   }

   public void paintLabelBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintLabelBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JLabel var7 = (JLabel)var1.getComponent();
      Container var8 = var7.getParent();
      Container var9 = var8 == null ? null : var8.getParent();
      String var10 = var7.getName();
      Color var13;
      if (var8 instanceof CellRendererPane) {
         if (var10 != null && var10.startsWith("ComboBox.") && !(var9 instanceof JComboBox)) {
            if (cellRendererSelectionBackgroundEnabled && var9 instanceof JComponent) {
               JComponent var23 = (JComponent)var9;
               SynthStyle var25 = SynthLookAndFeel.getStyle(var23, Region.LIST);
               SynthContext var28 = new SynthContext(var23, Region.LIST, var25, 512);
               String var29 = SyntheticaLookAndFeel.getString("Synthetica.comboBox.listSelectionBackground", var7);
               if (var29 != null && var7.getBackground().equals(var25.getColor(var28, ColorType.TEXT_BACKGROUND))) {
                  Insets var32 = SyntheticaLookAndFeel.getInsets("Synthetica.comboBox.listSelectionBackground.insets", var7, false);
                  ImagePainter var33 = new ImagePainter(var2, var3, var4, var5, var6, var29, var32, var32, 0, 0);
                  var33.draw();
               } else {
                  Color var30 = var2.getColor();
                  var2.setColor(var7.getBackground());
                  var2.fillRect(var3, var4, var5, var6);
                  var2.setColor(var30);
               }
            } else {
               Color var21 = var2.getColor();
               var2.setColor(var7.getBackground());
               var2.fillRect(var3, var4, var5, var6);
               var2.setColor(var21);
            }
         } else {
            SynthContext var14;
            int var22;
            SynthStyle var26;
            if (cellRendererSelectionBackgroundEnabled && listSelectionBackgroundEnabled && (this.isListCellRenderer(var10) || JAVA5) && var9 instanceof JList) {
               JList var20 = (JList)var9;
               var22 = this.getHashCode(var20);
               if (var22 != listHash) {
                  listHash = var22;
                  listStyle = SynthLookAndFeel.getStyle(var20, Region.LIST);
               }

               var26 = listStyle;
               var14 = new SynthContext(var20, Region.LIST, var26, 512);
               if (var7.getBackground().equals(var26.getColor(var14, ColorType.TEXT_BACKGROUND))) {
                  ((GraphicsUtils)var26.getGraphicsUtils(var14)).paintListCellRendererSelectionBackground(var20, var7, var2, var3, var4, var5, var6);
               }
            } else if (cellRendererSelectionBackgroundEnabled && treeSelectionBackgroundEnabled && (this.isTreeCellRenderer(var10) || JAVA5) && var9 instanceof JTree && var7 instanceof DefaultTreeCellRenderer) {
               JTree var19 = (JTree)var9;
               var22 = this.getHashCode(var19);
               if (var22 != treeHash) {
                  treeHash = var22;
                  treeStyle = SynthLookAndFeel.getStyle(var19, Region.TREE);
               }

               var26 = treeStyle;
               var14 = new SynthContext(var19, Region.TREE, var26, 512);

               try {
                  Field var15 = DefaultTreeCellRenderer.class.getDeclaredField("selected");
                  var15.setAccessible(true);
                  boolean var16 = (Boolean)var15.get(var7);
                  if (var16) {
                     ((GraphicsUtils)var26.getGraphicsUtils(var14)).paintTreeCellRendererSelectionBackground(var19, var7, var2, var3, var4, var5, var6);
                  }
               } catch (Exception var17) {
                  throw new RuntimeException(var17);
               }
            } else if (cellRendererSelectionBackgroundEnabled && tableSelectionBackgroundEnabled && "Table.cellRenderer".equals(var10) && var9 instanceof JTable) {
               JTable var18 = (JTable)var9;
               var22 = this.getHashCode(var18);
               if (var22 != tableHash) {
                  tableHash = var22;
                  tableStyle = SynthLookAndFeel.getStyle(var18, Region.TABLE);
               }

               var26 = tableStyle;
               var14 = new SynthContext(var18, Region.TABLE, var26, 512);
               if (var7.getBackground().equals(var26.getColor(var14, ColorType.TEXT_BACKGROUND))) {
                  ((GraphicsUtils)var26.getGraphicsUtils(var14)).paintTableCellRendererSelectionBackground(var18, var7, var2, var3, var4, var5, var6);
               }
            } else if ((var10 != null && var10.startsWith("TableHeader.") || "Table.cellRenderer".equals(var10)) && var9 instanceof JTableHeader) {
               JTableHeader var11 = (JTableHeader)var9;
               var7.setFont(var11.getFont());
               if (SyntheticaLookAndFeel.getBoolean("Synthetica.tableHeader.showVerticalGrid", var11, true)) {
                  Color var12 = var2.getColor();
                  var13 = SyntheticaLookAndFeel.getColor("Synthetica.tableHeader.gridColor", var11);
                  var2.setColor(var13);
                  var2.drawLine(var3 + var5 - 1, var4, var3 + var5 - 1, var4 + var6 - 1);
                  var2.setColor(var12);
               }

               return;
            }
         }
      }

      if (var8 instanceof CellRendererPane && var9 instanceof JComboBox) {
         JComboBox var24 = (JComboBox)var9;
         boolean var27 = SyntheticaLookAndFeel.isOpaque(var24);
         var13 = SyntheticaLookAndFeel.get("Synthetica.comboBox.border.locked", (Component)var24) == null ? SyntheticaLookAndFeel.getColor("Synthetica.comboBox.focused.backgroundColor", var24) : null;
         if (var24.isEnabled() && !var24.isEditable() && var13 == null || var27 && var13 == null) {
            return;
         }

         if (var24.hasFocus() && !var24.isEditable()) {
            if (!(var24.getBackground() instanceof UIResource) && SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.useBackgroundColorAsFocus", var24, false)) {
               return;
            }

            Color var31 = var2.getColor();
            var2.setColor(var13);
            var2.fillRect(var3, var4, var5, var6);
            var2.setColor(var31);
         }
      }

   }

   private boolean isListCellRenderer(String var1) {
      return var1 == null ? false : var1.startsWith("List.cellRenderer");
   }

   private boolean isTreeCellRenderer(String var1) {
      return var1 == null ? false : var1.startsWith("Tree.cellRenderer");
   }

   private int getHashCode(JComponent var1) {
      int var2 = var1.hashCode();
      String var3 = SyntheticaLookAndFeel.getStyleName(var1);
      return var3 == null ? var2 : 31 * var2 + var3.hashCode();
   }
}
