package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.java2d.Synthetica2DUtils;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.plaf.ColorUIResource;

public class ImagePainter {
   public static final Logger logger = Logger.getLogger(ImagePainter.class.getName());
   public static final int STRETCHED = 0;
   public static final int TILED = 1;
   private Graphics g;
   private int x;
   private int y;
   private int w;
   private int h;
   private int iw;
   private int ih;
   private Image image;
   private Insets sInsets;
   private Insets dInsets;
   private int xPolicy;
   private int yPolicy;
   private WeakReference weakComponent;
   private AnimationThreadFactory.AnimationThread animationThread;
   private String id;
   private int angle;
   private boolean flipHorizontal;
   private boolean flipVertical;
   private boolean animationEnabled;
   private static long initDuration = -1L;
   private static int initOperations = 0;
   private static long paintDuration = 0L;
   private static int paintOperations = 0;
   private static HashMap imageCache = new HashMap();
   private static boolean debug = SyntheticaLookAndFeel.isSystemPropertySet("synthetica.debug");

   public ImagePainter(JComponent var1, int var2, int var3, int var4, int var5, Graphics var6, int var7, int var8, int var9, int var10, String var11, Insets var12, Insets var13, int var14, int var15) {
      this(var1, (String)null, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15);
   }

   public ImagePainter(JComponent var1, String var2, int var3, int var4, int var5, int var6, Graphics var7, int var8, int var9, int var10, int var11, String var12, Insets var13, Insets var14, int var15, int var16) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, false, false);
   }

   public ImagePainter(JComponent var1, String var2, int var3, int var4, int var5, int var6, Graphics var7, int var8, int var9, int var10, int var11, String var12, Insets var13, Insets var14, int var15, int var16, boolean var17, boolean var18) {
      this.angle = 0;
      if (logger.isLoggable(Level.FINE)) {
         logger.log(Level.FINE, "Image Path: " + var12);
      }

      if (debug || var12 != null) {
         if (debug && initDuration == -1L) {
            Frame[] var19 = Frame.getFrames();

            for(int var20 = 0; var20 < var19.length; ++var20) {
               if (var19[var20] instanceof JFrame) {
                  ((JFrame)var19[var20]).getRootPane().registerKeyboardAction(new ActionListener() {
                     public void actionPerformed(ActionEvent var1) {
                        System.out.println("inits: " + ImagePainter.initOperations + " in " + ImagePainter.initDuration + " ms");
                        System.out.println("paint: " + ImagePainter.paintOperations + " in " + ImagePainter.paintDuration + " ms");
                        ImagePainter.initDuration = 0L;
                        ImagePainter.initOperations = 0;
                        ImagePainter.paintDuration = 0L;
                        ImagePainter.paintOperations = 0;
                        Frame[] var2 = Frame.getFrames();

                        for(int var3 = 0; var3 < var2.length; ++var3) {
                           Frame var4 = var2[var3];
                           if (var4 instanceof JFrame && ((JFrame)var4).getRootPane() != null) {
                              var4.setSize(640, 550);
                              var4.validate();
                              ((JFrame)var4).getRootPane().repaint();
                           }
                        }

                     }
                  }, "durationAction", KeyStroke.getKeyStroke(123, 10), 1);
               }
            }
         }

         long var29 = System.currentTimeMillis();
         if (var2 == null) {
            var2 = "null";
         }

         this.id = var2;
         this.flipHorizontal = var17;
         this.flipVertical = var18;
         if (var1 != null) {
            this.weakComponent = new WeakReference(var1);
            if (var1.getClientProperty("Synthetica.flipHorizontal") != null && (Boolean)var1.getClientProperty("Synthetica.flipHorizontal")) {
               this.flipHorizontal = true;
            }

            if (var1.getClientProperty("Synthetica.flipVertical") != null && (Boolean)var1.getClientProperty("Synthetica.flipVertical")) {
               this.flipVertical = true;
            }
         }

         if (this.flipHorizontal) {
            var13 = this.flipLeftRight(var13);
            var14 = this.flipLeftRight(var14);
         }

         if (this.flipVertical) {
            var13 = this.flipTopBottom(var13);
            var14 = this.flipTopBottom(var14);
         }

         String[] var21 = var12.split(",");
         this.animationEnabled = this.isAnimationEnabled(var1);
         if (this.animationEnabled && var1 != null && var4 > 0) {
            AnimationThreadFactory.AnimationThread var22 = AnimationThreadFactory.createThread(var4);
            Rectangle var23 = new Rectangle(var8, var9, var10, var11);
            if (var1 instanceof JTabbedPane && !var2.equals("null")) {
               var23 = ((JTabbedPane)var1).getBoundsAt(Integer.parseInt(var2.split("/")[1]));
            }

            var22.addComponent(var1, this.id, var6, var5, var21, var3, var23);
            this.animationThread = var22;
         }

         this.g = var7;
         this.x = var8;
         this.y = var9;
         this.w = var10;
         this.h = var11;
         String[] var25 = var21;
         int var24 = var21.length;

         for(int var32 = 0; var32 < var24; ++var32) {
            String var30 = var25[var32];
            this.image = (Image)imageCache.get(var30);
            if (this.image == null) {
               URL var26 = SyntheticaLookAndFeel.class.getResource(var30);
               if (var26 == null) {
                  throw new RuntimeException("File not found: " + var30);
               }

               try {
                  this.image = this.toCompatibleImage(ImageIO.read(var26));
               } catch (IOException var28) {
                  throw new RuntimeException(var28);
               }

               imageCache.put(var30, this.image);
            }
         }

         this.iw = this.image.getWidth((ImageObserver)null);
         this.ih = this.image.getHeight((ImageObserver)null);
         this.sInsets = var13;
         this.dInsets = var14;
         this.xPolicy = var15;
         this.yPolicy = var16;
         if (debug) {
            long var31 = System.currentTimeMillis();
            long var33 = var31 - var29;
            initDuration += var33;
            ++initOperations;
         }

      }
   }

   private Insets flipLeftRight(Insets var1) {
      return var1 == null ? null : new Insets(var1.top, var1.right, var1.bottom, var1.left);
   }

   private Insets flipTopBottom(Insets var1) {
      return var1 == null ? null : new Insets(var1.bottom, var1.left, var1.top, var1.right);
   }

   private boolean isAnimationEnabled(JComponent var1) {
      return SyntheticaLookAndFeel.getBoolean("Synthetica.animation.enabled", var1, true);
   }

   public ImagePainter(JComponent var1, Graphics var2, int var3, int var4, int var5, int var6, String var7, Insets var8, Insets var9, int var10, int var11) {
      this(var1, -1, -1, -1, -1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
   }

   public ImagePainter(Graphics var1, int var2, int var3, int var4, int var5, String var6, Insets var7, Insets var8, int var9, int var10) {
      this((JComponent)null, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public ImagePainter(JComponent var1, int var2, int var3, int var4, Insets var5, Graphics var6, int var7, int var8, int var9, int var10, String var11, Insets var12, Insets var13, int var14, int var15) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, false, false);
   }

   public ImagePainter(JComponent var1, int var2, int var3, int var4, Insets var5, Graphics var6, int var7, int var8, int var9, int var10, String var11, Insets var12, Insets var13, int var14, int var15, boolean var16, boolean var17) {
      this(var1, (String)null, -1, -1, -1, -1, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17);
      if (debug || var11 != null) {
         Object var18 = (Image)imageCache.get(var11);
         if (var18 == null || !(var18 instanceof BufferedImage)) {
            try {
               var18 = this.toCompatibleImage(ImageIO.read(SyntheticaLookAndFeel.class.getResource(var11)));
               imageCache.put(var11, var18);
            } catch (Exception var20) {
               var20.printStackTrace();
            }
         }

         this.image = var1.createImage(new FilteredImageSource(((Image)var18).getSource(), new Colorizer(0, var2, var3, var4, ((Image)var18).getWidth((ImageObserver)null), ((Image)var18).getHeight((ImageObserver)null), var5)));
      }
   }

   public ImagePainter(JComponent var1, Graphics var2, int var3, int var4, int var5, int var6, String var7, Insets var8, Insets var9, Insets var10, Insets var11, int var12, int var13) {
      this(var2, var3, var4, var5, var6, var7, var8, var9, var12, var13);
      String var14 = var7 + "_EXCLUDED_OUTER_INSETS";
      Object var15 = (Image)imageCache.get(var14);
      if (var15 == null) {
         int var16 = this.iw - var8.left - var8.right;
         int var17 = this.ih - var8.top - var8.bottom;
         BufferedImage var18 = new BufferedImage(var16, var17, 2);
         Graphics2D var19 = var18.createGraphics();
         this.g = var19;
         this.x = 0;
         this.y = 0;
         this.w = var16;
         this.h = var17;
         this.draw();
         var19.dispose();
         imageCache.put(var14, var18);
         var15 = var18;
         this.g = var2;
         this.x = var3;
         this.y = var4;
         this.w = var5;
         this.h = var6;
      }

      this.image = (Image)var15;
      this.iw = ((Image)var15).getWidth((ImageObserver)null);
      this.ih = ((Image)var15).getHeight((ImageObserver)null);
      this.sInsets = var10;
      this.dInsets = var11;
   }

   public ImagePainter(Image var1, Graphics var2, int var3, int var4, int var5, int var6, Insets var7, Insets var8) {
      this.angle = 0;
      this.image = var1;
      this.iw = var1.getWidth((ImageObserver)null);
      this.ih = var1.getHeight((ImageObserver)null);
      this.g = var2;
      this.x = var3;
      this.y = var4;
      this.w = var5;
      this.h = var6;
      this.sInsets = var7;
      this.dInsets = var8;
   }

   public static void clearImageCache() {
      imageCache.clear();
   }

   public void setAngle(int var1) {
      this.angle = var1;
      if (this.animationThread != null && this.weakComponent != null && !(this.weakComponent.get() instanceof JTabbedPane) && var1 != 0) {
         this.animationThread.rotateRepaintRect((JComponent)this.weakComponent.get(), this.id);
      }

   }

   public void draw() {
      this.drawBorder();
      this.drawCenter();
   }

   public void drawBorder() {
      this.drawTopLeft();
      this.drawTopCenter();
      this.drawTopRight();
      this.drawLeft();
      this.drawRight();
      this.drawBottomLeft();
      this.drawBottomCenter();
      this.drawBottomRight();
   }

   public void drawCenter() {
      if (this.animationEnabled && this.weakComponent != null && this.animationThread != null) {
         this.image = (Image)imageCache.get(this.animationThread.getImagePath((JComponent)this.weakComponent.get(), this.id));
      }

      if (this.dInsets != null && this.sInsets != null) {
         this.drawImage(this.image, this.g, this.x + this.dInsets.left, this.y + this.dInsets.top, this.x + this.w - this.dInsets.right, this.y + this.h - this.dInsets.bottom, this.sInsets.left, this.sInsets.top, this.iw - this.sInsets.right, this.ih - this.sInsets.bottom, this.xPolicy, this.yPolicy);
      }
   }

   public void drawTopLeft() {
      if (this.animationEnabled && this.weakComponent != null && this.animationThread != null) {
         this.image = (Image)imageCache.get(this.animationThread.getImagePath((JComponent)this.weakComponent.get(), this.id));
      }

      this.drawImage(this.image, this.g, this.x, this.y, this.x + this.dInsets.left, Math.min(this.y + this.h - this.dInsets.bottom, this.y + this.dInsets.top), 0, 0, this.sInsets.left, Math.min(this.h - this.sInsets.bottom, this.sInsets.top), 0, 0);
   }

   public void drawTopRight() {
      if (this.animationEnabled && this.weakComponent != null && this.animationThread != null) {
         this.image = (Image)imageCache.get(this.animationThread.getImagePath((JComponent)this.weakComponent.get(), this.id));
      }

      this.drawImage(this.image, this.g, this.x + this.w - this.dInsets.right, this.y, this.x + this.w, Math.min(this.y + this.h - this.dInsets.bottom, this.y + this.dInsets.top), this.iw - this.sInsets.right, 0, this.iw, Math.min(this.h - this.sInsets.bottom, this.sInsets.top), 0, 0);
   }

   public void drawBottomLeft() {
      if (this.animationEnabled && this.weakComponent != null && this.animationThread != null) {
         this.image = (Image)imageCache.get(this.animationThread.getImagePath((JComponent)this.weakComponent.get(), this.id));
      }

      this.drawImage(this.image, this.g, this.x, this.y + this.h - this.dInsets.bottom, this.x + this.dInsets.left, this.y + this.h, 0, this.ih - this.sInsets.bottom, this.sInsets.left, this.ih, 0, 0);
   }

   public void drawBottomRight() {
      if (this.animationEnabled && this.weakComponent != null && this.animationThread != null) {
         this.image = (Image)imageCache.get(this.animationThread.getImagePath((JComponent)this.weakComponent.get(), this.id));
      }

      this.drawImage(this.image, this.g, this.x + this.w - this.dInsets.right, this.y + this.h - this.dInsets.bottom, this.x + this.w, this.y + this.h, this.iw - this.sInsets.right, this.ih - this.sInsets.bottom, this.iw, this.ih, 0, 0);
   }

   public void drawTopCenter() {
      if (this.animationEnabled && this.weakComponent != null && this.animationThread != null) {
         this.image = (Image)imageCache.get(this.animationThread.getImagePath((JComponent)this.weakComponent.get(), this.id));
      }

      this.drawImage(this.image, this.g, this.x + this.dInsets.left, this.y, this.x + this.w - this.dInsets.right, Math.min(this.y + this.h - this.dInsets.bottom, this.y + this.dInsets.top), this.sInsets.left, 0, this.iw - this.sInsets.right, Math.min(this.h - this.sInsets.bottom, this.sInsets.top), this.xPolicy, this.yPolicy);
   }

   public void drawLeft() {
      if (this.animationEnabled && this.weakComponent != null && this.animationThread != null) {
         this.image = (Image)imageCache.get(this.animationThread.getImagePath((JComponent)this.weakComponent.get(), this.id));
      }

      this.drawImage(this.image, this.g, this.x, this.y + this.dInsets.top, this.x + this.dInsets.left, this.y + this.h - this.dInsets.bottom, 0, this.sInsets.top, this.sInsets.left, this.ih - this.sInsets.bottom, this.xPolicy, this.yPolicy);
   }

   public void drawRight() {
      if (this.animationEnabled && this.weakComponent != null && this.animationThread != null) {
         this.image = (Image)imageCache.get(this.animationThread.getImagePath((JComponent)this.weakComponent.get(), this.id));
      }

      this.drawImage(this.image, this.g, this.x + this.w - this.dInsets.right, this.y + this.dInsets.top, this.x + this.w, this.y + this.h - this.dInsets.bottom, this.iw - this.sInsets.right, this.sInsets.top, this.iw, this.ih - this.sInsets.bottom, this.xPolicy, this.yPolicy);
   }

   public void drawBottomCenter() {
      if (this.animationEnabled && this.weakComponent != null && this.animationThread != null) {
         this.image = (Image)imageCache.get(this.animationThread.getImagePath((JComponent)this.weakComponent.get(), this.id));
      }

      this.drawImage(this.image, this.g, this.x + this.dInsets.left, this.y + this.h - this.dInsets.bottom, this.x + this.w - this.dInsets.right, this.y + this.h, this.sInsets.left, this.ih - this.sInsets.bottom, this.iw - this.sInsets.right, this.ih, this.xPolicy, this.yPolicy);
   }

   private void drawImage(Image var1, Graphics var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      if (var1 != null) {
         if (this.angle != 0) {
            this.rotateGraphics(var2, true);
         }

         if (this.flipHorizontal) {
            var1 = Synthetica2DUtils.flipHorizontal((Image)var1);
         }

         if (this.flipVertical) {
            var1 = Synthetica2DUtils.flipVertical((Image)var1);
         }

         if (this.weakComponent != null) {
            JComponent var13 = (JComponent)this.weakComponent.get();
            Color var14 = var13.getBackground();
            float var15 = (float)SyntheticaLookAndFeel.getInt("Synthetica.background.alpha", var13, 10) / 100.0F;
            if (var13.getClientProperty("Synthetica.background") != null || var14 != null && !(var14 instanceof ColorUIResource) && !(var14 instanceof SystemColor)) {
               if (!(var1 instanceof BufferedImage)) {
                  BufferedImage var16 = new BufferedImage(((Image)var1).getWidth((ImageObserver)null), ((Image)var1).getHeight((ImageObserver)null), 2);
                  var16.getGraphics().drawImage((Image)var1, 0, 0, (ImageObserver)null);
                  var1 = var16;
               }

               if (var13.getClientProperty("Synthetica.background") != null) {
                  var14 = (Color)var13.getClientProperty("Synthetica.background");
               }

               if (var13.getClientProperty("Synthetica.background.alpha") != null) {
                  var15 = (Float)var13.getClientProperty("Synthetica.background.alpha");
               }

               var1 = Synthetica2DUtils.createColorizedImage((BufferedImage)var1, var14, var15);
            }
         }

         long var22 = System.currentTimeMillis();
         if (var11 == 0 && var12 == 0) {
            var2.drawImage((Image)var1, var3, var4, var5, var6, var7, var8, var9, var10, (ImageObserver)null);
         } else {
            int var23 = var3;
            int var24 = var9 - var7;

            for(int var17 = var10 - var8; var4 < var6; var4 += var17) {
               while(var3 < var5) {
                  int var18 = var5;
                  if (var11 == 1) {
                     var18 = Math.min(var5, var3 + var24);
                  }

                  int var19 = var6;
                  if (var12 == 1) {
                     var19 = Math.min(var6, var4 + var17);
                  }

                  int var20 = var11 == 1 ? Math.min(var9, var7 + (var18 - var3)) : var9;
                  int var21 = var12 == 1 ? Math.min(var10, var8 + (var19 - var4)) : var10;
                  var2.drawImage((Image)var1, var3, var4, var18, var19, var7, var8, var20, var21, (ImageObserver)null);
                  if (var11 == 0) {
                     break;
                  }

                  var3 += var24;
               }

               if (var12 == 0) {
                  break;
               }

               var3 = var23;
            }
         }

         if (this.angle != 0) {
            this.rotateGraphics(var2, false);
         }

         long var25 = System.currentTimeMillis();
         long var26 = var25 - var22;
         if (debug && var26 > 100L) {
            System.out.println("Paint performance lack: " + var26 + " ms " + this.findImage((Image)var1));
         }

         paintDuration += var26;
         ++paintOperations;
      }
   }

   private void rotateGraphics(Graphics var1, boolean var2) {
      Graphics2D var3 = (Graphics2D)var1;
      if (var2) {
         var3.translate(this.angle > 0 ? this.h : 0, this.angle > 0 ? 0 : this.w);
         var3.rotate(Math.toRadians((double)this.angle), (double)this.x, (double)this.y);
      } else {
         var3.rotate(Math.toRadians((double)(-this.angle)), (double)this.x, (double)this.y);
         var3.translate(this.angle > 0 ? -this.h : 0, this.angle > 0 ? 0 : -this.w);
      }

   }

   private String findImage(Image var1) {
      String var2 = null;
      Set var3 = imageCache.entrySet();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Map.Entry var5 = (Map.Entry)var4.next();
         if (var5.getValue() == var1) {
            var2 = (String)var5.getKey();
            break;
         }
      }

      return var2;
   }

   private BufferedImage toCompatibleImage(Image var1) {
      GraphicsEnvironment var2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice var3 = var2.getDefaultScreenDevice();
      GraphicsConfiguration var4 = var3.getDefaultConfiguration();
      BufferedImage var5 = var4.createCompatibleImage(var1.getWidth((ImageObserver)null), var1.getHeight((ImageObserver)null), 3);
      Graphics var6 = var5.getGraphics();
      var6.drawImage(var1, 0, 0, (ImageObserver)null);
      var6.dispose();
      return var5;
   }

   private static class Colorizer extends RGBImageFilter {
      private int a;
      private int r;
      private int g;
      private int b;
      private int w;
      private int h;
      private Insets insets;

      public Colorizer(int var1, int var2, int var3, int var4, int var5, int var6, Insets var7) {
         this.canFilterIndexColorModel = true;
         this.a = var1;
         this.r = var2;
         this.g = var3;
         this.b = var4;
         this.w = var5;
         this.h = var6;
         this.insets = var7;
      }

      public int filterRGB(int var1, int var2, int var3) {
         if (var1 >= this.insets.left && var1 <= this.w - this.insets.right - 1 && var2 >= this.insets.top && var2 <= this.h - this.insets.bottom - 1) {
            Color var4 = new Color(var3, true);
            int var5 = Math.min(Math.max(var4.getRed() + var4.getRed() * this.r / 100, 0), 255);
            int var6 = Math.min(Math.max(var4.getGreen() + var4.getGreen() * this.g / 100, 0), 255);
            int var7 = Math.min(Math.max(var4.getBlue() + var4.getBlue() * this.b / 100, 0), 255);
            int var8 = Math.min(Math.max(var4.getAlpha() + var4.getAlpha() * this.a / 100, 0), 255);
            return (new Color(var5, var6, var7, var8)).getRGB();
         } else {
            return var3;
         }
      }
   }
}
