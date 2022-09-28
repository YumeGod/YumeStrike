package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.lang.reflect.Method;
import javax.accessibility.Accessible;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;

public class ComboBoxPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.ComboBoxPainter";

   protected ComboBoxPainter() {
   }

   public static ComboBoxPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static ComboBoxPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, ComboBoxPainter.class, "Synthetica.ComboBoxPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, ComboBoxPainter.class, "Synthetica.ComboBoxPainter");
      }

      return (ComboBoxPainter)var1;
   }

   public void paintComboBoxBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (var1.getComponent() instanceof JComboBox) {
         JComboBox var7 = (JComboBox)var1.getComponent();
         if (this.hasFocus(var7)) {
            FocusPainter.paintFocus(var7.isEditable() ? "focus.comboBox" : "focus.comboBox.locked", var1, var2, var3, var4, var5, var6);
         }
      }

   }

   protected boolean hasFocus(JComboBox var1) {
      Component var2 = null;
      if (var1.isEditable()) {
         var2 = var1.getEditor().getEditorComponent();
      }

      return var1.hasFocus() || var2 != null && var2.hasFocus();
   }

   public void paintComboBoxBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JComboBox var7 = (JComboBox)var1.getComponent();
      var7.putClientProperty("Synthetica.flipHorizontal", !var7.getComponentOrientation().isLeftToRight());
      SyntheticaLookAndFeel.setChildrenOpaque(var7, false);
      Color var8 = var7.getBackground();
      boolean var9 = var8 == null || var8 instanceof ColorUIResource;
      boolean var10 = SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.keepLockedBorderIfColored", var7);
      boolean var11 = SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.noBorderIfColored", var7);
      boolean var12 = SyntheticaLookAndFeel.isOpaque(var7);
      boolean var13 = var7.isEnabled();
      boolean var14 = !var7.isEditable();
      if (var12) {
         if (var9) {
            if (this.hasFocus(var7)) {
               var1 = new SynthContext(var7, Region.COMBO_BOX, var1.getStyle(), var1.getComponentState() | 256);
            }

            if (!var13) {
               var8 = SyntheticaLookAndFeel.getColor("Synthetica.comboBox.disabled.backgroundColor", var7);
            } else if (var14) {
               var8 = SyntheticaLookAndFeel.getColor("Synthetica.comboBox.locked.backgroundColor", var7);
               if (var8 == null) {
                  var8 = var1.getStyle().getColor(var1, ColorType.BACKGROUND);
               }
            } else {
               var8 = var1.getStyle().getColor(var1, ColorType.BACKGROUND);
            }
         }

         SyntheticaPainterState var15 = this.createState(var1, var7);
         UIKey var16 = this.getUIKey(var7, "comboBox", var15);
         Boolean var17 = (Boolean)SyntheticaLookAndFeel.get(var16.get("fillBackground"), (Component)var7);
         Border var18 = var7.getBorder();
         Border var19 = SyntheticaLookAndFeel.findDefaultBorder(var18);
         boolean var20 = var19 == null ? false : var19.getClass().getName().equals("javax.swing.plaf.synth.SynthBorder");
         boolean var21 = var18 instanceof CompoundBorder && ((CompoundBorder)var18).getInsideBorder() == var19;
         if (var21) {
            Insets var22 = var18.getBorderInsets(var7);
            Insets var23 = var19.getBorderInsets(var7);
            var3 += var22.left - var23.left;
            var4 += var22.top - var23.top;
            var5 -= var22.left - var23.left + var22.right - var23.right;
            var6 -= var22.top - var23.top + var22.bottom - var23.bottom;
         }

         if (var17 == null || var17) {
            this.fillBackground(var7, var18, var19, var20, var2, var8, var3, var4, var5, var6);
         }

         if (var9 || !var11) {
            SyntheticaState var30 = new SyntheticaState(var15.getState());
            var30.resetState(SyntheticaState.State.HOVER);
            var30.resetState(SyntheticaState.State.PRESSED);
            if (!var9 && !var10) {
               var30.resetState(SyntheticaState.State.LOCKED);
            }

            var16 = this.getUIKey(var7, "comboBox.border", var30);
            String var31 = var16.get();
            boolean var24 = SyntheticaLookAndFeel.get(var31, (Component)var7) != null;
            if (var31.equals("Synthetica.comboBox.border.disabled") && !var24 || var31.equals("Synthetica.comboBox.border.locked") && !var24 || var31.equals("Synthetica.comboBox.border.disabled.locked") && !var24) {
               var16 = this.getUIKey(var7, "textField.border", var30);
            }

            String var25 = (String)var16.findProperty(var1, (String)null, true, 1);
            if (var15.isSet(SyntheticaState.State.FOCUSED) && SyntheticaLookAndFeel.get(var16.get() + ".focused", (Component)var7) != null) {
               var25 = SyntheticaLookAndFeel.getString(var16.get() + ".focused", var7);
            }

            if (var7.isPopupVisible() && SyntheticaLookAndFeel.get(var16.get() + ".opened", (Component)var7) != null) {
               var25 = SyntheticaLookAndFeel.getString(var16.get() + ".opened", var7);
               BasicComboPopup var26 = null;

               try {
                  ComboBoxUI var27 = var7.getUI();
                  Method var35 = var27.getClass().getDeclaredMethod("getPopup", (Class[])null);
                  var26 = (BasicComboPopup)var35.invoke(var27, (Object[])null);
               } catch (Exception var29) {
                  Accessible var28 = var7.getUI().getAccessibleChild(var7, 0);
                  if (var28 instanceof BasicComboPopup) {
                     var26 = (BasicComboPopup)var28;
                  }
               }

               Boolean var33 = var26 == null ? true : MenuPainter.popupIsBelowInvoker(var7, var26);
               if ((var33 == null || !var33) && SyntheticaLookAndFeel.get(var16.get() + ".openedAndPopupNotBelow", (Component)var7) != null) {
                  var25 = SyntheticaLookAndFeel.getString(var16.get() + ".openedAndPopupNotBelow", var7);
               }
            }

            Insets var32 = SyntheticaLookAndFeel.getInsets("Synthetica.comboBox.border.insets", var7);
            Insets var34 = SyntheticaLookAndFeel.get("Synthetica.comboBox.border.dInsets", (Component)var7) == null ? var32 : SyntheticaLookAndFeel.getInsets("Synthetica.comboBox.border.dInsets", var7);
            if (!var20) {
               Insets var36 = var7.getInsets();
               var3 += var36.left;
               var4 += var36.top;
               var5 += -var36.left - var36.right;
               var6 += -var36.top - var36.bottom;
               var34 = new Insets(0, 0, 0, 0);
            }

            ImagePainter var37 = new ImagePainter(var7, var2, var3, var4, var5, var6, var25, var32, var34, 0, 0);
            if (!var9 && !var10) {
               var37.drawBorder();
            } else if (!var20) {
               var37.drawCenter();
            } else {
               var37.draw();
            }

            if (SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.hoverAndPressed.enabled", var7)) {
               this.paintHoverPressed(var7, var15, var18, var19, var21, var2, var3, var4, var5, var6, var32, var34);
            }

         }
      }
   }

   private void fillBackground(JComponent var1, Border var2, Border var3, boolean var4, Graphics var5, Color var6, int var7, int var8, int var9, int var10) {
      Color var11 = var5.getColor();
      var5.setColor(var6);
      Insets var12 = var1.getInsets();
      if (var2 != null && !var12.equals(new Insets(0, 0, 0, 0))) {
         if (!var4) {
            var5.fillRect(var7 + var12.left, var8 + var12.top, var9 - var12.left - var12.right, var10 - var12.top - var12.bottom);
         } else {
            Insets var13 = SyntheticaLookAndFeel.getInsets("Synthetica.comboBox.border.fillInsets", var1, false);
            int var14 = SyntheticaLookAndFeel.getInt("Synthetica.comboBox.border.arcWidth", var1, 8);
            int var15 = SyntheticaLookAndFeel.getInt("Synthetica.comboBox.border.arcHeight", var1, 8);
            Graphics2D var16 = (Graphics2D)var5;
            RenderingHints var17 = var16.getRenderingHints();
            var16.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            var16.fillRoundRect(var7 + var13.left, var8 + var13.top, var9 - var13.left - var13.right, var10 - var13.top - var13.bottom, var14, var15);
            var16.setRenderingHints(var17);
         }
      } else {
         var5.fillRect(var7, var8, var9, var10);
      }

      var5.setColor(var11);
   }

   private SyntheticaPainterState createState(SynthContext var1, JComboBox var2) {
      byte var3 = 0;
      JButton var4 = (JButton)SyntheticaLookAndFeel.findComponent((Class)JButton.class, var2);
      if (var4 != null && var4.getModel().isPressed()) {
         var3 = 4;
      }

      return new SyntheticaPainterState(var1, var3, true);
   }

   private void paintHoverPressed(JComboBox var1, SyntheticaPainterState var2, Border var3, Border var4, boolean var5, Graphics var6, int var7, int var8, int var9, int var10, Insets var11, Insets var12) {
      int var16;
      if (SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.stateChange4ButtonOnly", var1)) {
         boolean var13 = var1.getComponentOrientation().isLeftToRight();
         if (var11.equals(var12)) {
            var12 = new Insets(var11.top, 0, var11.bottom, var11.right);
         }

         int var14 = SyntheticaLookAndFeel.findComponent((Class)JButton.class, var1).getWidth();
         Insets var15 = var5 ? var4.getBorderInsets(var1) : var1.getInsets();
         var16 = SyntheticaLookAndFeel.getInt("Synthetica.comboBox.button.relativeX", var1, 0);
         if (var4 != null) {
            var7 += var13 ? var9 - var15.right - var14 + var16 : 0;
            var9 = var14 + (var13 ? var15.right : var15.left) - var16;
         } else {
            var7 += var13 ? var9 - var14 + var16 : 0;
            var9 = var14 - var16;
         }
      }

      UIKey var20 = this.getUIKey(var1, "comboBox", var2);
      UIKey var21 = this.getUIKey(var1, "comboBox.border", var2);
      String var22 = (String)UIKey.findProperty((JComponent)var1, var21.get(), (String)null, true, 1);
      var16 = SyntheticaLookAndFeel.getInt(var20.get("animation.cycles"), var1, 1);
      int var17 = SyntheticaLookAndFeel.getInt(var20.get("animation.delay"), var1, 50);
      int var18 = SyntheticaLookAndFeel.getInt(var20.get("animation.type"), var1, 2);
      if (!var2.isSet(SyntheticaState.State.HOVER) && !var2.isSet(SyntheticaState.State.PRESSED)) {
         if (!SyntheticaLookAndFeel.getBoolean("Synthetica.animation.enabled", var1, false)) {
            return;
         }
      } else {
         var18 = SyntheticaLookAndFeel.getInt(var20.get("animation.type"), var1, 1);
      }

      ImagePainter var19 = new ImagePainter(var1, var16, var17, var18, var2.getState(), var6, var7, var8, var9, var10, var22, var11, var12, 0, 0);
      if (var4 == null) {
         var19.drawCenter();
      } else {
         var19.draw();
      }

   }

   protected UIKey getUIKey(JComponent var1, String var2, SyntheticaState var3) {
      return new UIKey(var2, var3);
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      JComponent var6 = var1.getComponent();
      int var7 = super.getCacheHash(var1, var2, var3, var4, var5);
      if (var6 instanceof JComboBox) {
         JComboBox var8 = (JComboBox)var1.getComponent();
         boolean var9 = !var8.isEditable();
         boolean var10 = var8.getClientProperty("Synthetica.MOUSE_OVER") == null ? false : (Boolean)var8.getClientProperty("Synthetica.MOUSE_OVER");
         var7 = 31 * var7 + (var9 ? 0 : 1);
         var7 = 31 * var7 + (var10 ? 0 : 1);
         var7 = 31 * var7 + (this.hasFocus(var8) ? 0 : 1);
      }

      return var7;
   }
}
