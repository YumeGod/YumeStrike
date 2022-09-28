package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import de.javasoft.plaf.synthetica.SyntheticaTitlePane;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.WeakHashMap;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

public class ButtonPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.ButtonPainter";
   protected static WeakHashMap buttons = new WeakHashMap();

   protected ButtonPainter() {
   }

   public static ButtonPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static ButtonPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, ButtonPainter.class, "Synthetica.ButtonPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, ButtonPainter.class, "Synthetica.ButtonPainter");
      }

      return (ButtonPainter)var1;
   }

   public void paintButtonBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintButtonBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (this.paintCheck(var1)) {
         JComponent var7 = var1.getComponent();
         String var8 = (String)var7.getClientProperty("JButton.segmentPosition");
         SyntheticaPainterState var9 = new SyntheticaPainterState(var1);
         this.paintButtonBackground(var7, var9, var8, var2, var3, var4, var5, var6);
      }
   }

   public void paintButtonBackground(JComponent var1, SyntheticaState var2, String var3, Graphics var4, int var5, int var6, int var7, int var8) {
      this.paintButtonBackground(var1, var2, var3, 0, var4, var5, var6, var7, var8);
   }

   public void paintButtonBackground(JComponent var1, SyntheticaState var2, String var3, int var4, Graphics var5, int var6, int var7, int var8, int var9) {
      if (this.paintCheck(var1, var2)) {
         if (var2.isSet(SyntheticaState.State.SELECTED)) {
            var2 = new SyntheticaState(var2.getState());
            var2.resetState(SyntheticaState.State.SELECTED);
            var2.setState(SyntheticaState.State.PRESSED);
         }

         String var10 = this.createSuffix(var3);
         UIKey var11 = new UIKey("button.border" + var10, var2);
         int var12 = SyntheticaLookAndFeel.getInt("Synthetica.button.smallSizeThreshold", var1, 12);
         if (var8 <= var12 || var9 <= var12) {
            var11 = new UIKey("button.12x12.border", var2);
            var10 = ".12x12";
         }

         Insets var13 = (Insets)UIKey.findProperty((JComponent)var1, var11.get(), "insets", true, 2);
         if (this.isToolBarButton(var1) && !SyntheticaLookAndFeel.getBoolean("Synthetica.toolBar.buttons.paintBorder", var1)) {
            var11 = new UIKey("toolBar.button.border" + var10, var2);
            if (SyntheticaLookAndFeel.get(var11.get(), (Component)var1) == null) {
               var11 = new UIKey("toolBar.button.border", var2);
            }
         } else if (this.isToolBarButtonBarButton(var1)) {
            UIKey var15 = new UIKey("toolBar.buttonBar.button.border" + var10, var2);
            if (SyntheticaLookAndFeel.get(var15.get(), (Component)var1) != null) {
               var11 = var15;
            }
         }

         String var20 = SyntheticaLookAndFeel.getString(var11.get(), var1);
         if (var1.hasFocus() && SyntheticaLookAndFeel.get(var11.get() + ".focused", (Component)var1) != null) {
            var20 = SyntheticaLookAndFeel.getString(var11.get() + ".focused", var1);
         }

         var11 = new UIKey("button", var2);
         if (var20 != null && SyntheticaLookAndFeel.isOpaque(var1)) {
            int var16 = SyntheticaLookAndFeel.getInt(var11.get("animation.cycles"), var1, 1);
            int var17 = SyntheticaLookAndFeel.getInt(var11.get("animation.delay"), var1, 50);
            int var18 = SyntheticaLookAndFeel.getInt(var11.get("animation.type"), var1, 2);
            if (var2.isSet(SyntheticaState.State.DEFAULT)) {
               var16 = SyntheticaLookAndFeel.getInt(var11.get("animation.cycles"), var1, -1);
               var17 = SyntheticaLookAndFeel.getInt(var11.get("animation.delay"), var1, 70);
               var18 = SyntheticaLookAndFeel.getInt(var11.get("animation.type"), var1, 0);
            } else if (var2.isSet(SyntheticaState.State.HOVER)) {
               var18 = SyntheticaLookAndFeel.getInt(var11.get("animation.type"), var1, 1);
            }

            ImagePainter var19 = new ImagePainter(var1, var16, var17, var18, var2.getState(), var5, var6, var7, var8, var9, var20, var13, var13, 0, 0);
            var19.setAngle(var4);
            var19.draw();
         }

         if ((var1.hasFocus() || var2.isSet(SyntheticaState.State.FOCUSED)) && ((AbstractButton)var1).isFocusPainted()) {
            this.paintFocus(var1, var2, var3, var4, var5, var6, var7, var8, var9);
         }

      }
   }

   protected void paintFocus(JComponent var1, SyntheticaState var2, String var3, int var4, Graphics var5, int var6, int var7, int var8, int var9) {
      String var10 = this.createSuffix(var3);
      int var11 = SyntheticaLookAndFeel.getInt("Synthetica.button.smallSizeThreshold", var1, 12);
      if (var8 <= var11 || var9 <= var11) {
         var10 = ".12x12";
      }

      String var12 = "focus.button" + var10;
      if (this.isToolBarButton(var1)) {
         var12 = var12 + ".toolBar";
      } else if (this.isToolBarButtonBarButton(var1)) {
         var12 = var12 + ".buttonBar.toolBar";
      }

      FocusPainter.paintFocus(var12, var1, var2.getState(), (String)null, var4, var5, var6, var7, var8, var9);
   }

   private String createSuffix(String var1) {
      return var1 != null && var1.length() != 0 ? (var1.startsWith(".") ? var1 : "." + var1) : "";
   }

   protected boolean paintCheck(SynthContext var1) {
      return this.paintCheck(var1.getComponent(), new SyntheticaPainterState(var1));
   }

   protected boolean paintCheck(JComponent var1, SyntheticaState var2) {
      if (!(var1 instanceof AbstractButton)) {
         return false;
      } else {
         AbstractButton var3 = (AbstractButton)var1;
         Container var4 = var3.getParent();
         if (var4 != null) {
            if (var4 instanceof BasicInternalFrameTitlePane && !SyntheticaLookAndFeel.getBoolean("Synthetica.internalFrame.titlePane.buttons.paintBorder", var3)) {
               return false;
            }

            if (var4 instanceof SyntheticaTitlePane && !SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.buttons.paintBorder", var3)) {
               return false;
            }

            if (this.isToolBarButton(var1)) {
               SynthStyle var5 = SynthLookAndFeel.getStyle(var1, Region.BUTTON);
               SynthContext var6 = new SynthContext(var1, Region.BUTTON, var5, 0);
               Insets var7 = (Insets)var6.getStyle().get(var6, "Button.margin");
               Insets var8 = var3.getMargin() == null ? new Insets(0, 0, 0, 0) : var3.getMargin();
               if ((var8.equals(var7) || !buttons.containsKey(var3) && (var3.getText() == null || "".equals(var3.getText().trim()))) && SyntheticaLookAndFeel.getBoolean("Synthetica.toolBar.button.marginCorrection.enabled", var1, true) && var8.equals(var7)) {
                  var3.setMargin(new Insets(var8.bottom - var7.bottom, var8.left - var7.left, var8.top - var7.top, var8.right - var7.right));
               }

               buttons.put(var3, (Object)null);
               boolean var9 = SyntheticaLookAndFeel.getBoolean("Synthetica.toolBar.buttons.paintBorder", var3);
               if (!var9 && !var2.isSet(SyntheticaState.State.HOVER) && SyntheticaLookAndFeel.get("Synthetica.toolBar.button.border", (Component)var3) == null && (!var2.isSet(SyntheticaState.State.PRESSED) || !SyntheticaLookAndFeel.getBoolean("Synthetica.toolBar.button.pressed.paintBorder", var3))) {
                  return false;
               }
            }
         }

         if (!var3.isBorderPainted() && var3.getText() != null && var3.getText().length() != 0) {
            return false;
         } else if (!var3.isBorderPainted() && !var2.isSet(SyntheticaState.State.HOVER)) {
            return false;
         } else {
            return var3.isContentAreaFilled();
         }
      }
   }

   private boolean isToolBarButton(JComponent var1) {
      Boolean var2 = (Boolean)var1.getClientProperty("Synthetica.useToolBarStyle");
      return var2 != null && !var2 ? false : var1.getParent() instanceof JToolBar;
   }

   private boolean isToolBarButtonBarButton(JComponent var1) {
      Container var2 = var1.getParent();
      return var2 != null && var2.getParent() instanceof JToolBar && "ButtonBar".equals(SyntheticaLookAndFeel.getStyleName(var2));
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      AbstractButton var6 = (AbstractButton)var1.getComponent();
      if (var6.getParent() instanceof JToolBar) {
         return -1;
      } else {
         String var7 = (String)var6.getClientProperty("JButton.segmentPosition");
         Border var8 = var6.getBorder();
         int var9 = super.getCacheHash(var1, var2, var3, var4, var5);
         var9 = var7 == null ? var9 : 31 * var9 + var7.hashCode();
         var9 = var8 == null ? var9 : 31 * var9 + (var6.isBorderPainted() ? 0 : 1);
         var9 = 31 * var9 + (var6.isContentAreaFilled() ? 0 : 1);
         return var9;
      }
   }
}
