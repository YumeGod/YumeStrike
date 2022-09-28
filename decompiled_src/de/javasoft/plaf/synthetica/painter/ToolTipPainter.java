package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.OS;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.WeakHashMap;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.synth.SynthContext;

public class ToolTipPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.ToolTipPainter";
   private static WeakHashMap opaqued = new WeakHashMap();

   protected ToolTipPainter() {
   }

   public static ToolTipPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static ToolTipPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, ToolTipPainter.class, "Synthetica.ToolTipPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, ToolTipPainter.class, "Synthetica.ToolTipPainter");
      }

      return (ToolTipPainter)var1;
   }

   public void paintToolTipBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintToolTipBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      this.prepareBackground(var1, var2, var3, var4, var5, var6);
      JToolTip var7 = (JToolTip)var1.getComponent();
      int var8 = var1.getComponentState();
      Insets var9 = SyntheticaLookAndFeel.getInsets("Synthetica.toolTip.background.insets", var7);
      String var11 = SyntheticaLookAndFeel.getString("Synthetica.toolTip.background", var7);
      int var12 = SyntheticaLookAndFeel.getInt("Synthetica.toolTip.animation.cycles", var7, 1);
      int var13 = SyntheticaLookAndFeel.getInt("Synthetica.toolTip.animation.delay", var7, 50);
      int var14 = SyntheticaLookAndFeel.getInt("Synthetica.toolTip.animation.type", var7, 4);
      ImagePainter var15 = new ImagePainter(var7, var12, var13, var14, var8, var2, var3, var4, var5, var6, var11, var9, var9, 0, 0);
      var15.draw();
   }

   protected void prepareBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JToolTip var7 = (JToolTip)var1.getComponent();
      JComponent var8 = (JComponent)var7.getParent();
      if (var8 != null) {
         Window var9 = SwingUtilities.getWindowAncestor(var7);
         if (!JAVA7U8_OR_ABOVE && OS.getCurrentOS() == OS.Mac && var9.getClass().getName().contains("$HeavyWeightWindow")) {
            var2.clearRect(var3, var4, var5, var6);
         }

         if (JAVA5 && !opaqued.containsKey(var7)) {
            opaqued.put(var7, (Object)null);
            var7.setOpaque(false);
            var8.setOpaque(false);
            var8.repaint();
         }

         BufferedImage var10 = (BufferedImage)var8.getClientProperty("POPUP_BACKGROUND");
         if (var10 != null) {
            var2.drawImage(var10, var3, var4, (ImageObserver)null);
         }

      }
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      return -1;
   }
}
