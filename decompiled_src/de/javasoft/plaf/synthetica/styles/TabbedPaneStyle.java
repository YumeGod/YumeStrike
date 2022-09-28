package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class TabbedPaneStyle extends StyleWrapper {
   private static TabbedPaneStyle instance = new TabbedPaneStyle();

   private TabbedPaneStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
         installTabLayoutManager((JTabbedPane)var1);
      }

      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         TabbedPaneStyle var3 = new TabbedPaneStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Font getFont(SynthContext var1) {
      return SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tab.selected.bold", var1.getComponent()) ? this.synthStyle.getFont(var1).deriveFont(1) : this.synthStyle.getFont(var1);
   }

   private static void installTabLayoutManager(JTabbedPane var0) {
      if (!var0.getLayout().getClass().getName().contains(TabbedPaneStyle.class.getName())) {
         if (SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tabs.stretch", var0) || SyntheticaLookAndFeel.get("Synthetica.tabbedPane.rotateTabRuns", (Component)var0) != null && !SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.rotateTabRuns", var0)) {
            if (var0.getTabLayoutPolicy() == 1) {
               installScrollLayout(var0);
            } else {
               installWrapLayout(var0);
            }

         }
      }
   }

   private static void installWrapLayout(final JTabbedPane var0) {
      final BasicTabbedPaneUI var1 = (BasicTabbedPaneUI)var0.getUI();
      var0.setLayout(new BasicTabbedPaneUI.TabbedPaneLayout(var1) {
         protected void calculateTabRects(int var1x, int var2) {
            super.calculateTabRects(var1x, var2);
            if (SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.tabs.stretch", var0) && var2 > 0) {
               Rectangle[] var3 = null;

               try {
                  Class var4 = Class.forName("javax.swing.plaf.basic.BasicTabbedPaneUI");
                  Field var5 = var4.getDeclaredField("rects");
                  var5.setAccessible(true);
                  var3 = (Rectangle[])var5.get(var1);
               } catch (Exception var21) {
                  throw new RuntimeException(var21);
               }

               Insets var22 = var0.getInsets();
               new Insets(0, 0, 0, 0);

               Insets var23;
               try {
                  Class var6 = Class.forName("javax.swing.plaf.basic.BasicTabbedPaneUI");
                  Method var7 = var6.getDeclaredMethod("getTabAreaInsets", Integer.TYPE);
                  var7.setAccessible(true);
                  var23 = (Insets)var7.invoke(var1, var1x);
               } catch (Exception var20) {
                  throw new RuntimeException(var20);
               }

               int var24 = var0.getWidth() - (var22.right + var23.right + var22.left + var23.left);
               int var25 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tabs.maxStretchFactor", var0, 3);
               byte var8 = 0;
               int var9 = var2 - 1;
               Rectangle var10 = var3[var9];
               int var11 = var10.x + var10.width - var3[var8].x;
               if (var11 >= var24 || var11 < 1) {
                  return;
               }

               boolean var12 = var24 / var11 > var25;
               var0.putClientProperty("Synthetica.maxStretchExceeded", var12);
               int var13 = SyntheticaLookAndFeel.getInt("Synthetica.tabbedPane.tabs.centeredTabsFactor", var0, var25);
               int var14 = var12 ? var11 * var13 : var24;
               int var15 = var14 - (var10.x + var10.width) + var22.left + var23.left;
               int var16 = (var24 - var14) / 2;
               float var17 = (float)var15 / (float)var11;

               for(int var18 = var8; var18 <= var9; ++var18) {
                  Rectangle var19 = var3[var18];
                  if (var18 == var8) {
                     var19.x += var16;
                  } else {
                     var19.x = var3[var18 - 1].x + var3[var18 - 1].width;
                  }

                  var19.width += Math.round((float)var19.width * var17);
               }
            }

         }

         protected void rotateTabRuns(int var1x, int var2) {
            if (SyntheticaLookAndFeel.get("Synthetica.tabbedPane.rotateTabRuns", (Component)var0) == null || SyntheticaLookAndFeel.getBoolean("Synthetica.tabbedPane.rotateTabRuns", var0)) {
               super.rotateTabRuns(var1x, var2);
            }

         }
      });
   }

   private static void installScrollLayout(JTabbedPane var0) {
   }
}
