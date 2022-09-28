package org.apache.batik.apps.slideshow;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JWindow;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.renderer.StaticRenderer;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

public class Main extends JComponent {
   StaticRenderer renderer;
   UserAgent userAgent;
   DocumentLoader loader;
   BridgeContext ctx;
   BufferedImage image;
   BufferedImage display;
   File[] files;
   static int duration = 3000;
   static int frameDelay;
   volatile boolean done = false;
   volatile Thread transitionThread = null;
   long startLastTransition = 0L;
   volatile boolean paused = false;

   public Main(File[] var1, Dimension var2) {
      this.setBackground(Color.black);
      this.files = var1;
      UserAgentAdapter var3 = new UserAgentAdapter();
      this.renderer = new StaticRenderer();
      this.userAgent = var3;
      this.loader = new DocumentLoader(this.userAgent);
      this.ctx = new BridgeContext(this.userAgent, this.loader);
      var3.setBridgeContext(this.ctx);
      if (var2 == null) {
         var2 = Toolkit.getDefaultToolkit().getScreenSize();
      }

      this.setPreferredSize(var2);
      this.setDoubleBuffered(false);
      this.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent var1) {
            if (Main.this.done) {
               System.exit(0);
            } else {
               Main.this.togglePause();
            }

         }
      });
      var2.width += 2;
      var2.height += 2;
      this.display = new BufferedImage(var2.width, var2.height, 4);
      RenderThread var4 = new RenderThread();
      var4.start();
      JWindow var5 = new JWindow();
      var5.setBackground(Color.black);
      var5.getContentPane().setBackground(Color.black);
      var5.getContentPane().add(this);
      var5.pack();
      var5.setLocation(new Point(-1, -1));
      var5.setVisible(true);
   }

   public void setTransition(BufferedImage var1) {
      synchronized(this) {
         while(this.transitionThread != null) {
            try {
               this.wait();
            } catch (InterruptedException var5) {
            }
         }

         this.transitionThread = new TransitionThread(var1);
         this.transitionThread.start();
      }
   }

   public void togglePause() {
      synchronized(this) {
         this.paused = !this.paused;
         Cursor var2;
         if (this.paused) {
            var2 = new Cursor(3);
         } else {
            var2 = new Cursor(0);
            if (this.transitionThread != null) {
               synchronized(this.transitionThread) {
                  this.transitionThread.notifyAll();
               }
            }
         }

         this.setCursor(var2);
      }
   }

   public void paint(Graphics var1) {
      Graphics2D var2 = (Graphics2D)var1;
      if (this.display != null) {
         var2.drawImage(this.display, (BufferedImageOp)null, 0, 0);
      }
   }

   public static void readFileList(String var0, List var1) {
      BufferedReader var2;
      try {
         var2 = new BufferedReader(new FileReader(var0));
      } catch (FileNotFoundException var19) {
         System.err.println("Unable to open file-list: " + var0);
         return;
      }

      try {
         URL var3 = (new File(var0)).toURL();

         String var4;
         while((var4 = var2.readLine()) != null) {
            String var5 = var4;
            int var6 = var4.indexOf(35);
            if (var6 != -1) {
               var5 = var4.substring(0, var6);
            }

            var5 = var5.trim();
            if (var5.length() != 0) {
               try {
                  URL var7 = new URL(var3, var5);
                  var1.add(var7.getFile());
               } catch (MalformedURLException var18) {
                  System.err.println("Can't make sense of line:\n  " + var4);
               }
            }
         }
      } catch (IOException var20) {
         System.err.println("Error while reading file-list: " + var0);
      } finally {
         try {
            var2.close();
         } catch (IOException var17) {
         }

      }

   }

   public static void main(String[] var0) {
      ArrayList var1 = new ArrayList();
      Dimension var2 = null;
      if (var0.length == 0) {
         showUsage();
      } else {
         int var4;
         label114:
         for(int var3 = 0; var3 < var0.length; ++var3) {
            if (var0[var3].equals("-h") || var0[var3].equals("-help") || var0[var3].equals("--help")) {
               showUsage();
               return;
            }

            if (var0[var3].equals("--")) {
               ++var3;

               while(true) {
                  if (var3 >= var0.length) {
                     break label114;
                  }

                  var1.add(var0[var3++]);
               }
            }

            if (!var0[var3].equals("-fl") && !var0[var3].equals("--file-list")) {
               if (!var0[var3].equals("-ft") && !var0[var3].equals("--frame-time")) {
                  if (!var0[var3].equals("-tt") && !var0[var3].equals("--transition-time")) {
                     if (!var0[var3].equals("-ws") && !var0[var3].equals("--window-size")) {
                        var1.add(var0[var3]);
                     } else {
                        if (var3 + 1 == var0.length) {
                           System.err.println("Must provide window size [w,h] after " + var0[var3]);
                           break;
                        }

                        try {
                           var4 = var0[var3 + 1].indexOf(44);
                           int var5;
                           int var6;
                           if (var4 == -1) {
                              var5 = var6 = Integer.decode(var0[var3 + 1]);
                           } else {
                              String var7 = var0[var3 + 1].substring(0, var4);
                              String var8 = var0[var3 + 1].substring(var4 + 1);
                              var5 = Integer.decode(var7);
                              var6 = Integer.decode(var8);
                           }

                           var2 = new Dimension(var5, var6);
                           ++var3;
                        } catch (NumberFormatException var10) {
                           System.err.println("Can't parse window size: " + var0[var3 + 1]);
                        }
                     }
                  } else {
                     if (var3 + 1 == var0.length) {
                        System.err.println("Must provide time in millis after " + var0[var3]);
                        break;
                     }

                     try {
                        duration = Integer.decode(var0[var3 + 1]);
                        ++var3;
                     } catch (NumberFormatException var11) {
                        System.err.println("Can't parse transition time: " + var0[var3 + 1]);
                     }
                  }
               } else {
                  if (var3 + 1 == var0.length) {
                     System.err.println("Must provide time in millis after " + var0[var3]);
                     break;
                  }

                  try {
                     frameDelay = Integer.decode(var0[var3 + 1]);
                     ++var3;
                  } catch (NumberFormatException var12) {
                     System.err.println("Can't parse frame time: " + var0[var3 + 1]);
                  }
               }
            } else {
               if (var3 + 1 == var0.length) {
                  System.err.println("Must provide name of file list file after " + var0[var3]);
                  break;
               }

               readFileList(var0[var3 + 1], var1);
               ++var3;
            }
         }

         File[] var13 = new File[var1.size()];

         for(var4 = 0; var4 < var1.size(); ++var4) {
            try {
               var13[var4] = new File((String)var1.get(var4));
            } catch (Exception var9) {
               var9.printStackTrace();
            }
         }

         new Main(var13, var2);
      }
   }

   public static void showUsage() {
      System.out.println("Options:\n                                 -- : Remaining args are file names\n                         -fl <file>\n                 --file-list <file> : file contains list of images to\n                                      show one per line\n             -ws <width>[,<height>]\n    -window-size <width>[,<height>] : Set the size of slideshow window\n                                      defaults to full screen\n                          -ft <int>\n                 --frame-time <int> : Amount of time in millisecs to\n                                      show each frame.\n                                      Includes transition time.\n                          -tt <int>\n            --transition-time <int> : Amount of time in millisecs to\n                                      transition between frames.\n                             <file> : SVG file to display");
   }

   static {
      frameDelay = duration + 7000;
   }

   class TransitionThread extends Thread {
      BufferedImage src;
      int blockw = 75;
      int blockh = 75;

      public TransitionThread(BufferedImage var2) {
         super("TransitionThread");
         this.setDaemon(true);
         this.src = var2;
      }

      public void run() {
         int var1 = (Main.this.display.getWidth() + this.blockw - 1) / this.blockw;
         int var2 = (Main.this.display.getHeight() + this.blockh - 1) / this.blockh;
         int var3 = var1 * var2;
         int var4 = Main.duration / var3;
         Point[] var5 = new Point[var3];

         for(int var6 = 0; var6 < var2; ++var6) {
            for(int var7 = 0; var7 < var1; ++var7) {
               var5[var6 * var1 + var7] = new Point(var7, var6);
            }
         }

         Graphics2D var29 = Main.this.display.createGraphics();
         var29.setColor(Color.black);

         long var9;
         for(long var30 = System.currentTimeMillis(); var30 - Main.this.startLastTransition < (long)Main.frameDelay; var30 = System.currentTimeMillis()) {
            try {
               var9 = (long)Main.frameDelay - (var30 - Main.this.startLastTransition);
               if (var9 > 500L) {
                  System.gc();
                  var30 = System.currentTimeMillis();
                  var9 = (long)Main.frameDelay - (var30 - Main.this.startLastTransition);
               }

               if (var9 > 0L) {
                  sleep(var9);
               }
            } catch (InterruptedException var27) {
            }
         }

         synchronized(this) {
            while(Main.this.paused) {
               try {
                  this.wait();
               } catch (InterruptedException var26) {
               }
            }
         }

         var9 = Main.this.startLastTransition = System.currentTimeMillis();

         for(int var11 = 0; var11 < var5.length; ++var11) {
            int var12 = (int)(Math.random() * (double)(var5.length - var11));
            Point var13 = var5[var12];
            System.arraycopy(var5, var12 + 1, var5, var12 + 1 - 1, var5.length - var11 - var12 - 1);
            int var14 = var13.x * this.blockw;
            int var15 = var13.y * this.blockh;
            int var16 = this.blockw;
            int var17 = this.blockh;
            if (var14 + var16 > this.src.getWidth()) {
               var16 = this.src.getWidth() - var14;
            }

            if (var15 + var17 > this.src.getHeight()) {
               var17 = this.src.getHeight() - var15;
            }

            synchronized(Main.this.display) {
               var29.fillRect(var14, var15, var16, var17);
               BufferedImage var19 = this.src.getSubimage(var14, var15, var16, var17);
               var29.drawImage(var19, (BufferedImageOp)null, var14, var15);
            }

            Main.this.repaint(var14, var15, var16, var17);
            long var18 = System.currentTimeMillis();

            try {
               long var20 = var18 - var9;
               if (var20 < (long)var4) {
                  sleep((long)var4 - var20);
               }
            } catch (InterruptedException var24) {
            }

            var9 = var18;
         }

         synchronized(Main.this) {
            Main.this.transitionThread = null;
            Main.this.notifyAll();
         }
      }
   }

   class RenderThread extends Thread {
      RenderThread() {
         super("RenderThread");
         this.setDaemon(true);
      }

      public void run() {
         Main.this.renderer.setDoubleBuffered(true);

         for(int var1 = 0; var1 < Main.this.files.length; ++var1) {
            GraphicsNode var2 = null;
            GVTBuilder var3 = new GVTBuilder();

            try {
               String var4 = Main.this.files[var1].toURL().toString();
               System.out.println("Reading: " + var4);
               Document var5 = Main.this.loader.loadDocument(var4);
               System.out.println("Building: " + var4);
               var2 = var3.build(Main.this.ctx, var5);
               System.out.println("Rendering: " + var4);
               Main.this.renderer.setTree(var2);
               SVGSVGElement var6 = ((SVGDocument)var5).getRootElement();
               Main.this.renderer.setTransform(ViewBox.getViewTransform((String)null, var6, (float)Main.this.display.getWidth(), (float)Main.this.display.getHeight(), Main.this.ctx));
               Main.this.renderer.updateOffScreen(Main.this.display.getWidth(), Main.this.display.getHeight());
               Rectangle var7 = new Rectangle(0, 0, Main.this.display.getWidth(), Main.this.display.getHeight());
               Main.this.renderer.repaint((Shape)var7);
               System.out.println("Painting: " + var4);
               Main.this.image = Main.this.renderer.getOffScreen();
               Main.this.setTransition(Main.this.image);
            } catch (Exception var9) {
               var9.printStackTrace();
            }
         }

         if (Main.this.transitionThread != null) {
            try {
               Main.this.transitionThread.join();
            } catch (InterruptedException var8) {
            }

            Main.this.done = true;
            Main.this.setCursor(new Cursor(3));
         }

      }
   }
}
