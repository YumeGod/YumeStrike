package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.JavaVersion;
import de.javasoft.util.OS;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.SynthContext;

class SyntheticaComponentPainter implements Cacheable {
   static final boolean JAVA5;
   static final boolean JAVA6;
   static final boolean JAVA7U8_OR_ABOVE;
   private Object antialiasing_old;
   private Object strokeControl_old;
   private AffineTransform at_old;
   private Paint paint_old;
   private Stroke stroke_old;
   private static final boolean OS_X_QUARTZ_ENABLED;
   protected static HashMap instances;

   static {
      JAVA5 = JavaVersion.JAVA5;
      JAVA6 = JavaVersion.JAVA6;
      JAVA7U8_OR_ABOVE = JavaVersion.JAVA7U8_OR_ABOVE;
      OS_X_QUARTZ_ENABLED = SyntheticaLookAndFeel.isSystemPropertySet("apple.awt.graphics.UseQuartz");
      instances = new HashMap();
   }

   protected static SyntheticaComponentPainter getInstance(SynthContext var0, Class var1, String var2) {
      SyntheticaComponentPainter var3 = null;
      Object var4 = var0 == null ? UIManager.get(var2) : SyntheticaLookAndFeel.get(var2, (Component)var0.getComponent());

      try {
         if (var4 == null && var1 != null) {
            var3 = (SyntheticaComponentPainter)var1.newInstance();
         } else if (var4 == null) {
            String var5 = var2.substring(var2.lastIndexOf(46) + 1);
            var3 = (SyntheticaComponentPainter)Class.forName(SyntheticaComponentPainter.class.getPackage().getName() + "." + var5).newInstance();
         } else if (var4 instanceof SyntheticaComponentPainter) {
            var3 = (SyntheticaComponentPainter)var4;
         } else if (var4 instanceof String) {
            var3 = (SyntheticaComponentPainter)Class.forName((String)var4).newInstance();
         }
      } catch (Exception var6) {
         throw new RuntimeException(var6);
      }

      instances.put(var3.getClass().getName(), var3);
      return var3;
   }

   protected static String getPainterClassName(SynthContext var0, Class var1, String var2) {
      Object var3 = var0 == null ? UIManager.get(var2) : SyntheticaLookAndFeel.get(var2, (Component)var0.getComponent());
      if (var3 == null) {
         return var1.getName();
      } else {
         return var3 instanceof SyntheticaComponentPainter ? var3.getClass().getName() : (String)var3;
      }
   }

   protected Graphics2D prepareGraphics2D(SynthContext var1, Graphics var2, int var3, int var4, boolean var5, boolean var6, BasicStroke var7) {
      Graphics2D var8 = (Graphics2D)var2;
      this.antialiasing_old = var8.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
      this.strokeControl_old = var8.getRenderingHint(RenderingHints.KEY_STROKE_CONTROL);
      this.at_old = var8.getTransform();
      this.paint_old = var8.getPaint();
      this.stroke_old = var8.getStroke();
      if (var5) {
         var8.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      }

      var8.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      if (var7 == null) {
         var7 = this.createStroke(var1);
      }

      var8.setStroke(var7);
      if (var6) {
         double var9 = (double)((float)var3 + var7.getLineWidth() / 2.0F);
         double var11 = (double)((float)var4 + var7.getLineWidth() / 2.0F);
         if (OS.getCurrentOS() == OS.Mac && OS_X_QUARTZ_ENABLED) {
            var9 = (double)Math.round(var9 - 0.5);
            var11 = (double)Math.round(var11 - 0.5);
         }

         var8.translate(var9, var11);
      }

      return var8;
   }

   protected Graphics2D prepareGraphics2D(SynthContext var1, Graphics var2, int var3, int var4, boolean var5) {
      return this.prepareGraphics2D(var1, var2, var3, var4, var5, true, (BasicStroke)null);
   }

   protected Graphics2D prepareGraphics2D(SynthContext var1, Graphics var2, int var3, int var4, boolean var5, boolean var6) {
      return this.prepareGraphics2D(var1, var2, var3, var4, var5, var6, (BasicStroke)null);
   }

   /** @deprecated */
   protected Graphics2D prepareGraphics2D(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, BasicStroke var7) {
      return this.prepareGraphics2D(var1, var2, var3, var4, true, true, var7);
   }

   /** @deprecated */
   protected Graphics2D prepareGraphics2D(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, boolean var7) {
      return this.prepareGraphics2D(var1, var2, var3, var4, true, true, (BasicStroke)null);
   }

   protected void restoreGraphics2D(Graphics2D var1) {
      var1.setStroke(this.stroke_old);
      var1.setPaint(this.paint_old);
      var1.setTransform(this.at_old);
      var1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antialiasing_old);
      var1.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, this.strokeControl_old);
   }

   protected float getScale() {
      return 1.0F;
   }

   protected BasicStroke createStroke(SynthContext var1) {
      return new BasicStroke(1.0F * this.getScale());
   }

   float scaleArc(float var1, float var2, float var3, float var4) {
      float var5 = Math.min(var3, var4);
      if (var5 < var2 * this.getScale()) {
         var1 = var5 - (var2 - var1) / var2 * var5;
      } else {
         var1 = this.scaleArc(var1);
      }

      return var1;
   }

   protected float scaleArc(float var1) {
      return var1 * this.getScale();
   }

   protected float calcRelativeArc(Graphics2D var1, float var2, float var3) {
      float var4 = ((BasicStroke)var1.getStroke()).getLineWidth();
      return var2 + var4 * var3;
   }

   protected float calcRelativePos(Graphics2D var1, float var2, float var3) {
      float var4 = ((BasicStroke)var1.getStroke()).getLineWidth();
      return var2 + var4 * var3;
   }

   protected float calcRelativeGradientPos(Graphics2D var1, float var2, float var3) {
      return this.calcRelativePos(var1, var2, var3) - this.getScale() / 2.0F;
   }

   protected static float calcRelativeLength(Graphics2D var0, float var1, float var2) {
      float var3 = ((BasicStroke)var0.getStroke()).getLineWidth();
      return var1 + var2 * var3 - var3;
   }

   protected static Shape subtractStroke(Graphics2D var0, Shape var1) {
      if (!SyntheticaLookAndFeel.getBoolean("Synthetica.2D.subtractStrokeEnabled", (Component)null)) {
         return var1;
      } else {
         Area var2 = new Area(((BasicStroke)var0.getStroke()).createStrokedShape(var1));
         Area var3 = new Area(var1);
         var3.subtract(var2);
         return var3;
      }
   }

   protected Color getSyntheticaBackgroundColor(JComponent var1) {
      Color var2 = null;
      Color var3 = var1.getBackground();
      if (var3 != null && !(var3 instanceof ColorUIResource)) {
         var2 = var3;
      }

      if (var1.getClientProperty("Synthetica.background") != null) {
         var2 = (Color)var1.getClientProperty("Synthetica.background");
      }

      float var4 = (float)SyntheticaLookAndFeel.getInt("Synthetica.background.alpha", var1, 10) / 100.0F;
      if (var1.getClientProperty("Synthetica.background.alpha") != null) {
         var4 = (Float)var1.getClientProperty("Synthetica.background.alpha");
      }

      return this.createAlphaColor(var2, var4);
   }

   protected Color createAlphaColor(Color var1, Float var2) {
      if (var1 != null) {
         var2 = var2 == null ? 0.1F : var2;
         int var3 = var1.getRGB() & ((int)(var2 * 255.0F) << 24 | 16777215);
         var1 = new Color(var3, true);
      }

      return var1;
   }

   protected Paint createLinearGradientPaint(float var1, float var2, float var3, float var4, float[] var5, Color[] var6) {
      return SyntheticaLookAndFeel.createLinearGradientPaint(var1, var2, var3, var4, var5, var6);
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      JComponent var6 = var1.getComponent();
      Border var7 = var6.getBorder();
      Boolean var8 = (Boolean)var6.getClientProperty("Synthetica.opaque");
      Color var9 = (Color)var6.getClientProperty("Synthetica.background");
      Float var10 = (Float)var6.getClientProperty("Synthetica.background.alpha");
      Color var11 = var6.getBackground();
      String var12 = SyntheticaLookAndFeel.getStyleName(var6);
      Window var13 = SwingUtilities.getWindowAncestor(var6);
      int var14 = var5.hashCode();
      if (this.getCacheScaleType(var5) != Cacheable.ScaleType.NINE_SQUARE) {
         var14 = 31 * var14 + var2;
         var14 = 31 * var14 + var3;
      }

      var14 = 31 * var14 + var4;
      var14 = 31 * var14 + (int)this.getScale() * 100;
      var14 = 31 * var14 + this.getClass().hashCode();
      var14 = 31 * var14 + var6.getClass().hashCode();
      var14 = 31 * var14 + var6.getComponentOrientation().hashCode();
      var14 = 31 * var14 + var1.getComponentState();
      var14 = var7 == null ? var14 : 31 * var14 + this.getBorderHashCode(var7);
      var14 = var8 == null ? var14 : 31 * var14 + (var8 ? 0 : 1);
      var14 = var9 == null ? var14 : var14 * 31 + var9.getRGB();
      var14 = var10 == null ? var14 : var14 * 31 + (int)(var10 * 100.0F);
      var14 = var11 == null ? var14 : var14 * 31 + var11.getRGB();
      var14 = var12 == null ? var14 : 31 * var14 + var12.hashCode();
      var14 = 31 * var14 + (var13 != null && !var13.isActive() ? 1 : 0);
      return var14;
   }

   private int getBorderHashCode(Border var1) {
      int var2 = var1 == null ? 0 : var1.getClass().hashCode();
      if (var1 instanceof CompoundBorder) {
         var2 = 31 * var2 + this.getBorderHashCode(((CompoundBorder)var1).getInsideBorder());
         var2 = 31 * var2 + this.getBorderHashCode(((CompoundBorder)var1).getOutsideBorder());
      }

      return var2;
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NONE;
   }

   public Insets getCacheScaleInsets(SynthContext var1, String var2) {
      JComponent var3 = var1.getComponent();
      int var4 = (int)(this.getScale() * 10.0F);
      Insets var5 = var3 == null ? new Insets(var4, var4, var4, var4) : var3.getInsets();
      return var5;
   }
}
