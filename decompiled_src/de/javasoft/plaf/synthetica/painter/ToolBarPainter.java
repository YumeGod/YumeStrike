package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.synth.SynthContext;

public class ToolBarPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.ToolBarPainter";

   protected ToolBarPainter() {
   }

   public static ToolBarPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static ToolBarPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, ToolBarPainter.class, "Synthetica.ToolBarPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, ToolBarPainter.class, "Synthetica.ToolBarPainter");
      }

      return (ToolBarPainter)var1;
   }

   public void paintToolBarBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintToolBarBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      this.paintToolBarBackground(var1.getComponent(), new SyntheticaState(var1.getComponentState()), var2, var3, var4, var5, var6);
   }

   public void paintToolBarBackground(JComponent var1, SyntheticaState var2, Graphics var3, int var4, int var5, int var6, int var7) {
      Container var8 = var1.getRootPane().getParent();
      boolean var9 = true;
      if (var8 instanceof Window) {
         var9 = ((Window)var8).isActive();
      } else if (var8 instanceof JInternalFrame) {
         var9 = ((JInternalFrame)var8).isSelected();
      }

      String var10 = "Synthetica.toolBar.background";
      if (!var9 && SyntheticaLookAndFeel.get(var10 + ".inactive", (Component)var1) != null) {
         var10 = var10 + ".inactive";
      }

      var10 = SyntheticaLookAndFeel.getString(var10, var1);
      if (var10 != null && SyntheticaLookAndFeel.isOpaque(var1)) {
         Insets var11 = SyntheticaLookAndFeel.getInsets("Synthetica.toolBar.background.insets", var1);
         byte var13 = 0;
         if (SyntheticaLookAndFeel.getBoolean("Synthetica.toolBar.background.horizontalTiled", var1)) {
            var13 = 1;
         }

         byte var14 = 0;
         if (SyntheticaLookAndFeel.getBoolean("Synthetica.toolBar.background.verticalTiled", var1)) {
            var14 = 1;
         }

         UIKey var15 = new UIKey("toolBar.background", var2);
         int var16 = SyntheticaLookAndFeel.getInt(var15.get("animation.cycles"), var1, 1);
         int var17 = SyntheticaLookAndFeel.getInt(var15.get("animation.delay"), var1, 50);
         int var18 = SyntheticaLookAndFeel.getInt(var15.get("animation.type"), var1, var9 ? 0 : 6);
         ImagePainter var19 = new ImagePainter(var1, var16, var17, var18, var2.getState(), var3, var4, var5, var6, var7, var10, var11, var11, var13, var14);
         var19.draw();
         var10 = "Synthetica.toolBar.background.light";
         var10 = SyntheticaLookAndFeel.getString(var10, var1);
         if (var10 != null) {
            var19 = new ImagePainter(var3, var4, var5, var6, var7, var10, var11, var11, 0, 0);
            var19.draw();
         }

         var10 = "Synthetica.toolBar.background.light2";
         var10 = SyntheticaLookAndFeel.getString(var10, var1);
         if (var10 != null) {
            var19 = new ImagePainter(var3, var4, var5, var6, var7, var10, var11, var11, 0, 0);
            var19.draw();
         }
      }

   }

   public void paintToolBarContentBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintToolBarContentBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }
}
