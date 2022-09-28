package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.text.JTextComponent;

class TextComponentPainter extends SyntheticaComponentPainter {
   protected void paintBackground(String var1, SynthContext var2, Graphics var3, int var4, int var5, int var6, int var7) {
      JTextComponent var8 = (JTextComponent)var2.getComponent();
      Color var9 = var8.getBackground();
      boolean var10 = var9 == null || var9 instanceof ColorUIResource;
      boolean var11 = SyntheticaLookAndFeel.getBoolean("Synthetica." + var1 + ".keepLockedBorderIfColored", var8);
      boolean var12 = SyntheticaLookAndFeel.getBoolean("Synthetica." + var1 + ".noBorderIfColored", var8);
      boolean var13 = SyntheticaLookAndFeel.isOpaque(var8);
      boolean var14 = !var8.isEditable();
      boolean var15 = var8.getParent() instanceof JViewport;
      boolean var16 = "textArea".equals(var1) || "textPane".equals(var1) || "editorPane".equals(var1);
      if (var13 && (!var16 || !var15)) {
         if (var10) {
            if (var14) {
               var9 = SyntheticaLookAndFeel.getColor("Synthetica." + var1 + ".locked.backgroundColor", var8);
               if (var9 == null) {
                  var9 = SyntheticaLookAndFeel.getColor("Synthetica." + var1 + ".lockedColor", var8);
               }

               if (var9 == null) {
                  if (var8.isEnabled() && (Boolean)SyntheticaLookAndFeel.getClientProperty("Synthetica.MOUSE_OVER", var8, false)) {
                     var2 = new SynthContext(var8, var2.getRegion(), var2.getStyle(), var2.getComponentState() | 2);
                  }

                  var9 = var2.getStyle().getColor(var2, ColorType.BACKGROUND);
               }
            } else {
               if (var8.isEnabled() && (Boolean)SyntheticaLookAndFeel.getClientProperty("Synthetica.MOUSE_OVER", var8, false)) {
                  var2 = new SynthContext(var8, var2.getRegion(), var2.getStyle(), var2.getComponentState() | 2);
               }

               var9 = var2.getStyle().getColor(var2, ColorType.BACKGROUND);
            }
         }

         SyntheticaPainterState var17 = new SyntheticaPainterState(var2);
         UIKey var18 = new UIKey(var1, var17);
         Boolean var19 = (Boolean)SyntheticaLookAndFeel.get(var18.get("fillBackground"), (Component)var8);
         Border var20 = var8.getBorder();
         Border var21 = SyntheticaLookAndFeel.findDefaultBorder(var20);
         boolean var22 = var21 == null ? false : var21.getClass().getName().equals("javax.swing.plaf.synth.SynthBorder");
         boolean var23 = var20 instanceof CompoundBorder && ((CompoundBorder)var20).getInsideBorder() == var21;
         if (var23) {
            Insets var24 = var20.getBorderInsets(var8);
            Insets var25 = var21.getBorderInsets(var8);
            var4 += var24.left - var25.left;
            var5 += var24.top - var25.top;
            var6 -= var24.left - var25.left + var24.right - var25.right;
            var7 -= var24.top - var25.top + var24.bottom - var25.bottom;
         }

         if (var19 == null || var19) {
            this.fillBackground(var8, var1, var20, var21, var22, var16, var3, var9, var4, var5, var6, var7);
         }

         if (var10 || !var12) {
            if (var10 || var11 || !var16) {
               SyntheticaState var31 = new SyntheticaState(var17.getState());
               if (!var10 && !var11) {
                  var31.resetState(SyntheticaState.State.LOCKED);
               }

               var18 = this.getUIKey(var8, var1, var31);
               boolean var32 = SyntheticaLookAndFeel.get(var18.get(), (Component)var8) != null;
               if (var14 && !var32) {
                  var31.resetState(SyntheticaState.State.LOCKED);
                  var18 = this.getUIKey(var8, var1, var31);
               }

               String var26 = (String)var18.findProperty(var2, (String)null, false, 1);
               if (var8.hasFocus() && SyntheticaLookAndFeel.get(var18.get() + ".focused", (Component)var8) != null) {
                  var26 = SyntheticaLookAndFeel.getString(var18.get() + ".focused", var8);
               }

               Insets var27 = SyntheticaLookAndFeel.getInsets("Synthetica." + var1 + ".border.insets", var8);
               Insets var28 = SyntheticaLookAndFeel.get("Synthetica." + var1 + ".border.dInsets", (Component)var8) == null ? var27 : SyntheticaLookAndFeel.getInsets("Synthetica." + var1 + ".border.dInsets", var8);
               Insets var29 = var8.getInsets();
               if (!var22) {
                  var4 += var29.left;
                  var5 += var29.top;
                  var6 += -var29.left - var29.right;
                  var7 += -var29.top - var29.bottom;
                  var28 = new Insets(0, 0, 0, 0);
               } else if (var16) {
                  var28 = new Insets(0, 0, 0, 0);
               }

               ImagePainter var30 = new ImagePainter(var8, var3, var4, var5, var6, var7, var26, var27, var28, 0, 0);
               if (!var10 && !var11) {
                  var30.drawBorder();
               } else if (var16) {
                  var30.drawCenter();
               } else {
                  var30.draw();
               }

            }
         }
      }
   }

   protected void paintBorder(String var1, SynthContext var2, Graphics var3, int var4, int var5, int var6, int var7) {
      JComponent var8 = var2.getComponent();
      if (!(var8 instanceof JTextComponent)) {
         UIKey var9 = this.getUIKey(var8, var1, new SyntheticaState());
         String var10 = (String)var9.findProperty(var2, (String)null, true, 1);
         Insets var11 = SyntheticaLookAndFeel.getInsets("Synthetica." + var1 + ".border.insets", var8);
         Insets var12 = SyntheticaLookAndFeel.get("Synthetica." + var1 + ".border.dInsets", (Component)var8) == null ? var11 : SyntheticaLookAndFeel.getInsets("Synthetica." + var1 + ".border.dInsets", var8);
         ImagePainter var13 = new ImagePainter(var8, var3, var4, var5, var6, var7, var10, var11, var12, 0, 0);
         var13.drawBorder();
      }

      boolean var14 = SyntheticaLookAndFeel.getBoolean("Synthetica.focus.textComponents.enabled", var8, true);
      if (var8.hasFocus() && var14) {
         FocusPainter.paintFocus("focus." + var1, var2, var3, var4, var5, var6, var7);
      }

   }

   protected UIKey getUIKey(JComponent var1, String var2, SyntheticaState var3) {
      return new UIKey(var2 + ".border", var3);
   }

   private void fillBackground(JTextComponent var1, String var2, Border var3, Border var4, boolean var5, boolean var6, Graphics var7, Color var8, int var9, int var10, int var11, int var12) {
      Color var13 = var7.getColor();
      var7.setColor(var8);
      Insets var14 = var1.getInsets();
      if (!var6 && var3 != null && !var14.equals(new Insets(0, 0, 0, 0))) {
         if (!var5) {
            var7.fillRect(var9 + var14.left, var10 + var14.top, var11 - var14.left - var14.right, var12 - var14.top - var14.bottom);
         } else {
            Insets var15 = SyntheticaLookAndFeel.getInsets("Synthetica." + var2 + ".border.fillInsets", var1, false);
            int var16 = SyntheticaLookAndFeel.getInt("Synthetica." + var2 + ".border.arcWidth", var1, 8);
            int var17 = SyntheticaLookAndFeel.getInt("Synthetica." + var2 + ".border.arcHeight", var1, 8);
            Graphics2D var18 = (Graphics2D)var7;
            RenderingHints var19 = var18.getRenderingHints();
            var18.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            var18.fillRoundRect(var9 + var15.left, var10 + var15.top, var11 - var15.left - var15.right, var12 - var15.top - var15.bottom, var16, var17);
            var18.setRenderingHints(var19);
         }
      } else {
         var7.fillRect(var9, var10, var11, var12);
      }

      var7.setColor(var13);
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      JComponent var6 = var1.getComponent();
      int var7 = super.getCacheHash(var1, var2, var3, var4, var5);
      if (var6 instanceof JTextComponent) {
         JTextComponent var8 = (JTextComponent)var6;
         boolean var9 = !var8.isEditable();
         var7 = 31 * var7 + (var9 ? 0 : 1);
      }

      return var7;
   }
}
