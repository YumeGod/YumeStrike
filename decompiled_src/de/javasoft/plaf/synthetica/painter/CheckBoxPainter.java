package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.GraphicsUtils;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.View;

public class CheckBoxPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.CheckBoxPainter";
   private static boolean cellRendererSelectionBackgroundEnabled;
   private static SynthStyle tableStyle;
   private static int tableHash;
   private static SynthStyle listStyle;
   private static int listHash;

   protected CheckBoxPainter() {
   }

   public static CheckBoxPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static CheckBoxPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, CheckBoxPainter.class, "Synthetica.CheckBoxPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, CheckBoxPainter.class, "Synthetica.CheckBoxPainter");
      }

      return (CheckBoxPainter)var1;
   }

   public static void reinitialize() {
      cellRendererSelectionBackgroundEnabled = SyntheticaLookAndFeel.getBoolean("Synthetica.cellRenderer.selectionBackground.enabled", (Component)null, false);
      tableHash = 0;
   }

   public void paintCheckBoxBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      AbstractButton var7 = (AbstractButton)var1.getComponent();
      SyntheticaPainterState var8 = new SyntheticaPainterState(var1);
      Container var9 = var7.getParent();
      Container var10 = var9 == null ? null : var9.getParent();
      String var11 = var7.getName();
      int var13;
      SynthStyle var14;
      SynthContext var15;
      if (cellRendererSelectionBackgroundEnabled && "Table.cellRenderer".equals(var11) && var10 instanceof JTable) {
         JTable var28 = (JTable)var10;
         var13 = this.getHashCode(var28);
         if (var13 != tableHash) {
            tableHash = var13;
            tableStyle = SynthLookAndFeel.getStyle(var28, Region.TABLE);
         }

         var14 = tableStyle;
         var15 = new SynthContext(var28, Region.TABLE, var14, 512);
         if (var7.getBackground().equals(var14.getColor(var15, ColorType.TEXT_BACKGROUND))) {
            ((GraphicsUtils)var14.getGraphicsUtils(var15)).paintTableCellRendererSelectionBackground(var28, var7, var2, var3, var4, var5, var6);
         }
      } else if (cellRendererSelectionBackgroundEnabled && (this.isListCellRenderer(var11) || JAVA5) && var10 instanceof JList) {
         JList var12 = (JList)var10;
         var13 = this.getHashCode(var12);
         if (var13 != listHash) {
            listHash = var13;
            listStyle = SynthLookAndFeel.getStyle(var12, Region.LIST);
         }

         var14 = listStyle;
         var15 = new SynthContext(var12, Region.LIST, var14, 512);
         if (var7.getBackground().equals(var14.getColor(var15, ColorType.TEXT_BACKGROUND))) {
            ((GraphicsUtils)var14.getGraphicsUtils(var15)).paintListCellRendererSelectionBackground(var12, var7, var2, var3, var4, var5, var6);
         }
      }

      if (SyntheticaLookAndFeel.get("Synthetica.checkBox.image", (Component)var7) != null) {
         UIKey var29 = new UIKey("checkBox", var8);
         Insets var31 = new Insets(0, 0, 0, 0);
         Insets var34 = var7.getInsets();
         String var16 = SyntheticaLookAndFeel.getString(var29.get("image"), var7);
         if (var7.hasFocus() && SyntheticaLookAndFeel.get(var29.get() + ".focused.image", (Component)var7) != null) {
            var16 = SyntheticaLookAndFeel.getString(var29.get() + ".focused.image", var7);
         }

         int var17 = SyntheticaLookAndFeel.getInt(var29.get("animation.cycles"), var7, 1);
         int var18 = SyntheticaLookAndFeel.getInt(var29.get("animation.delay"), var7, 50);
         int var19 = SyntheticaLookAndFeel.getInt(var29.get("animation.type"), var7, 2);
         if (var8.isSet(SyntheticaState.State.HOVER) || var8.isSet(SyntheticaState.State.PRESSED)) {
            var19 = SyntheticaLookAndFeel.getInt(var29.get("animation.type"), var7, 1);
         }

         Rectangle var20 = new Rectangle(var3 + var34.left, var4 + var34.top, var5 - var34.left - var34.right, var6 - var34.top - var34.bottom);
         Rectangle var21 = new Rectangle();
         Rectangle var22 = new Rectangle();
         Icon var23 = var1.getStyle().getIcon(var1, "CheckBox.icon");
         var1.getStyle().getGraphicsUtils(var1).layoutText(var1, var2.getFontMetrics(), var7.getText(), var23, var7.getHorizontalAlignment(), var7.getVerticalAlignment(), var7.getHorizontalTextPosition(), var7.getVerticalTextPosition(), var20, var22, var21, var7.getIconTextGap());
         boolean var24 = var7.getIcon() != null;
         if (!var24) {
            ImagePainter var25 = new ImagePainter(var7, var17, var18, var19, var8.getState(), var2, var22.x, var22.y, var22.width, var22.height, var16, var31, var31, 0, 0);
            if (!var8.isSet(SyntheticaState.State.PRESSED) || var7.getPressedIcon() == null) {
               var25.drawCenter();
            }
         }

         if (var8.isSet(SyntheticaState.State.FOCUSED) && var7.isFocusPainted() && SyntheticaLookAndFeel.get("Synthetica.checkBox.focus", (Component)var7) != null) {
            String var38 = SyntheticaLookAndFeel.getStyleName(var7);
            String var26 = "focus.checkBox";
            var17 = SyntheticaLookAndFeel.getInt(var26, "animation.cycles", var38, true, -1);
            var18 = SyntheticaLookAndFeel.getInt(var26, "animation.delay", var38, true, 60);
            var19 = SyntheticaLookAndFeel.getInt(var26, "animation.type", var38, true, 3);
            var16 = SyntheticaLookAndFeel.getString("Synthetica.checkBox.focus", var7);
            ImagePainter var27 = new ImagePainter(var7, "", var17, var18, var19, var8.getState(), var2, var22.x, var22.y, var22.width, var22.height, var16, var31, var31, 0, 0);
            var27.drawCenter();
         }
      }

      if (var8.isSet(SyntheticaState.State.FOCUSED) && var7.isFocusPainted()) {
         Icon var30 = var7.getIcon() == null ? var1.getStyle().getIcon(var1, "CheckBox.icon") : var7.getIcon();
         String var32 = var7.getText();
         int var33 = var32 != null && var32.length() != 0 ? var7.getIconTextGap() : 0;
         int var35 = var7.getInsets().left + var7.getInsets().right + var33 + (var30 == null ? 0 : var30.getIconWidth());
         boolean var36 = var7.getClientProperty("html") != null;
         if (!var36) {
            FontMetrics var37 = var7.getFontMetrics(var7.getFont());
            if (var32 != null) {
               var35 += var37.stringWidth(var32);
            }
         } else {
            var35 = (int)((float)var35 + ((View)var7.getClientProperty("html")).getPreferredSpan(0));
         }

         FocusPainter.paintFocus("focus.checkBox", var1, var2, var3, var4, var35, var6);
      }

   }

   private boolean isListCellRenderer(String var1) {
      return var1 == null ? false : var1.startsWith("List.cellRenderer");
   }

   public void paintCheckBoxBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JCheckBox var7 = (JCheckBox)var1.getComponent();
      if (var7.isBorderPainted()) {
         Insets var8 = SyntheticaLookAndFeel.getInsets("Synthetica.checkBox.border.insets", var7);
         String var10 = SyntheticaLookAndFeel.getString("Synthetica.checkBox.border", var7);
         ImagePainter var11 = new ImagePainter(var2, var3, var4, var5, var6, var10, var8, var8, 0, 0);
         var11.drawBorder();
      }

   }

   private int getHashCode(JComponent var1) {
      int var2 = var1.hashCode();
      String var3 = SyntheticaLookAndFeel.getStyleName(var1);
      return var3 == null ? var2 : 31 * var2 + var3.hashCode();
   }
}
