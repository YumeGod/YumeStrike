package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.OS;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

public class SyntheticaBasicIconPainter extends SyntheticaIconPainter implements Cacheable {
   private SynthContext sc;
   private int width;
   private int height;
   private static final boolean OS_X_QUARTZ_ENABLED = SyntheticaLookAndFeel.isSystemPropertySet("apple.awt.graphics.UseQuartz");

   public SyntheticaBasicIconPainter() {
      this((SynthContext)null, 16, 16);
   }

   public SyntheticaBasicIconPainter(SynthContext var1, int var2, int var3) {
      this.sc = var1;
      this.width = var2;
      this.height = var3;
   }

   protected float getScale() {
      return 1.0F;
   }

   protected BasicStroke createStroke(SynthContext var1) {
      return new BasicStroke(1.0F);
   }

   public int getIconHeight() {
      return (int)((float)this.height * this.getScale());
   }

   public int getIconWidth() {
      return (int)((float)this.width * this.getScale());
   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      SynthContext var5 = this.sc;
      if (var5 == null && var1 instanceof AbstractButton && UIManager.getLookAndFeel() instanceof SynthLookAndFeel) {
         AbstractButton var6 = (AbstractButton)var1;
         SynthStyle var7 = SyntheticaLookAndFeel.getStyle(var6, Region.BUTTON);
         int var8 = 0;
         var8 |= var6.getModel().isPressed() ? 4 : 0;
         var8 |= var6.getModel().isSelected() ? 512 : 0;
         var8 |= !var6.getModel().isEnabled() ? 8 : 0;
         var8 |= var6.getModel().isRollover() ? 2 : 0;
         var5 = new SynthContext(var6, Region.BUTTON, var7, var8);
      }

      this.paintIcon(var5, var2, var3, var4, this.getIconWidth(), this.getIconHeight());
   }

   public void paintIcon(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (!this.paintCachedImage(var1, var2, var3, var4, var5, var6)) {
         this.paintIcon_(var1, var2, var3, var4, var5, var6);
      }

   }

   private void paintIcon_(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      Graphics2D var7 = (Graphics2D)var2;
      Object var8 = var7.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
      Object var9 = var7.getRenderingHint(RenderingHints.KEY_STROKE_CONTROL);
      AffineTransform var10 = var7.getTransform();
      Paint var11 = var7.getPaint();
      Stroke var12 = var7.getStroke();
      var7.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      var7.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      BasicStroke var13 = this.createStroke(var1);
      var7.setStroke(var13);
      AffineTransform var14 = new AffineTransform();
      if (OS.getCurrentOS() == OS.Mac && OS_X_QUARTZ_ENABLED) {
         var14.translate((double)((long)var3 + Math.round((double)(this.getScale() / 2.0F) - 0.5)), (double)((long)var4 + Math.round((double)(this.getScale() / 2.0F) - 0.5)));
      } else {
         var14.translate((double)((float)var3 + this.getScale() / 2.0F), (double)((float)var4 + this.getScale() / 2.0F));
      }

      var14.scale((double)this.getScale(), (double)this.getScale());
      var7.transform(var14);
      this.paint2D(var1, (Graphics2D)var2, (float)var3, (float)var4, (float)(this.width - 1), (float)(this.height - 1));
      var7.setStroke(var12);
      var7.setPaint(var11);
      var7.setTransform(var10);
      var7.setRenderingHint(RenderingHints.KEY_ANTIALIASING, var8);
      var7.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, var9);
   }

   private boolean paintCachedImage(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      boolean var7 = false;
      int var10;
      if (var1 != null && UIManager.getBoolean("Synthetica.cache.enabled") && (var10 = this.getCacheHash(var1, var5, var6, 0, "")) != -1 && SyntheticaSoftCache.getInstance().isCacheable(var5, var6)) {
         VolatileImage var8 = this.getCachedImage(var10, var1, var2, var5, var6);
         boolean var9 = var8 == null ? false : var2.drawImage(var8, var3, var4, (ImageObserver)null);
         return var9;
      } else {
         return false;
      }
   }

   private VolatileImage getCachedImage(int var1, SynthContext var2, Graphics var3, int var4, int var5) {
      GraphicsConfiguration var6 = ((Graphics2D)var3).getDeviceConfiguration();
      SyntheticaSoftCache var7 = SyntheticaSoftCache.getInstance();
      VolatileImage var8 = (VolatileImage)var7.getImage(var1);

      do {
         int var9 = var8 == null ? 2 : var8.validate(var6);
         if (var9 == 2 || var9 == 1) {
            if (var9 == 2 || var8.getWidth() != var4 || var8.getHeight() != var5) {
               if (var8 != null) {
                  var8.flush();
                  var8 = null;
               }

               var8 = var6.createCompatibleVolatileImage(var4, var5, 3);
               var7.setImage(var8, var1);
            }

            Graphics2D var10 = var8.createGraphics();
            var10.setComposite(AlphaComposite.Clear);
            var10.fillRect(0, 0, var4, var5);
            var10.setComposite(AlphaComposite.SrcOver);
            this.paintIcon_(var2, var10, 0, 0, var4, var5);
            var10.dispose();
         }
      } while(var8.contentsLost());

      return var8;
   }

   public void paint2D(SynthContext var1, Graphics2D var2, float var3, float var4, float var5, float var6) {
   }

   protected Rectangle2D.Float calc2DBounds(Graphics2D var1, float var2, float var3) {
      float var4 = ((BasicStroke)var1.getStroke()).getLineWidth();
      float var5 = var4 / 2.0F - 0.5F;
      return new Rectangle2D.Float(var5, var5, var2 - var4 + 1.0F, var3 - var4 + 1.0F);
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
      Color var2 = (Color)var1.getClientProperty("Synthetica.background");
      if (var2 != null) {
         float var3 = var1.getClientProperty("Synthetica.background.alpha") == null ? 0.1F : (Float)var1.getClientProperty("Synthetica.background.alpha");
         int var4 = var2.getRGB() & ((int)(var3 * 255.0F) << 24 | 16777215);
         var2 = new Color(var4, true);
      }

      return var2;
   }

   protected Paint createLinearGradientPaint(float var1, float var2, float var3, float var4, float[] var5, Color[] var6) {
      return SyntheticaLookAndFeel.createLinearGradientPaint(var1, var2, var3, var4, var5, var6);
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      int var6 = var5.hashCode();
      var6 = 31 * var6 + var2;
      var6 = 31 * var6 + var3;
      var6 = 31 * var6 + var4;
      var6 = 31 * var6 + (int)this.getScale() * 100;
      var6 = 31 * var6 + this.getClass().hashCode();
      if (var1 != null) {
         JComponent var7 = var1.getComponent();
         String var8 = SyntheticaLookAndFeel.getStyleName(var7);
         Color var9 = (Color)var7.getClientProperty("Synthetica.background");
         Float var10 = (Float)var7.getClientProperty("Synthetica.background.alpha");
         Window var11 = SwingUtilities.getWindowAncestor(var7);
         var6 = 31 * var6 + var7.getClass().hashCode();
         var6 = 31 * var6 + var1.getComponentState();
         var6 = var8 == null ? var6 : 31 * var6 + var8.hashCode();
         var6 = var9 == null ? var6 : var6 * 31 + var9.getRGB();
         var6 = var10 == null ? var6 : var6 * 31 + (int)(var10 * 100.0F);
         var6 = 31 * var6 + (var11 != null && !var11.isActive() ? 1 : 0);
      }

      return var6;
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NONE;
   }

   public Insets getCacheScaleInsets(SynthContext var1, String var2) {
      int var3 = (int)(this.getScale() * 10.0F);
      return new Insets(var3, var3, var3, var3);
   }
}
