package de.javasoft.plaf.synthetica;

import com.sun.awt.AWTUtilities;
import com.sun.awt.AWTUtilities.Translucency;
import de.javasoft.util.JavaVersion;
import de.javasoft.util.OS;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JToolTip;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboPopup;

public class Popup extends javax.swing.Popup {
   public static final String POPUP_BACKGROUND = "POPUP_BACKGROUND";
   public static final String POPUP_LIGHTWEIGHT = "POPUP_LIGHTWEIGHT";
   private Component contents;
   private int x;
   private int y;
   private javax.swing.Popup popup;
   private Container heavyWeightContainer;
   private boolean lightWeight;

   public Popup(Component var1, Component var2, int var3, int var4, javax.swing.Popup var5) {
      this.contents = var2;
      this.popup = var5;
      this.x = var3;
      this.y = var4;
      Container var6 = var2.getParent();
      ((JComponent)var6).putClientProperty("POPUP_BACKGROUND", (Object)null);
      ((JComponent)var6).putClientProperty("POPUP_LIGHTWEIGHT", (Object)null);
      if (!this.isWindowOpacityEnabled((Window)null)) {
         ((JComponent)var6).setDoubleBuffered(false);
      }

      for(this.lightWeight = true; var6 != null; var6 = var6.getParent()) {
         if (var6 instanceof JWindow || var6 instanceof Panel || var6 instanceof Window) {
            this.heavyWeightContainer = var6;
            this.lightWeight = false;
            break;
         }
      }

      this.internalFrameCursorBugfix(var1);
      if (this.heavyWeightContainer != null && OS.getCurrentOS() == OS.Mac && !UIManager.getBoolean("Synthetica.popup.osShadow.enabled")) {
         this.heavyWeightContainer.setBackground(new Color(16777216, true));
         this.heavyWeightContainer.setBackground(new Color(0, true));
         if (this.heavyWeightContainer instanceof JWindow) {
            ((JWindow)this.heavyWeightContainer).getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
            ((JWindow)this.heavyWeightContainer).getRootPane().putClientProperty("Window.shadow", Boolean.FALSE);
         }
      }

   }

   private void internalFrameCursorBugfix(Component var1) {
      if (var1 != null && var1 instanceof JInternalFrame) {
         Container var2 = ((JInternalFrame)var1).getTopLevelAncestor();
         Cursor var3 = Cursor.getPredefinedCursor(0);
         if (var2 instanceof JFrame) {
            ((JFrame)var2).getGlassPane().setCursor(var3);
            ((JFrame)var2).getGlassPane().setVisible(false);
         } else if (var2 instanceof JWindow) {
            ((JWindow)var2).getGlassPane().setCursor(var3);
            ((JWindow)var2).getGlassPane().setVisible(false);
         } else if (var2 instanceof JDialog) {
            ((JDialog)var2).getGlassPane().setCursor(var3);
            ((JDialog)var2).getGlassPane().setVisible(false);
         } else if (var2 instanceof JApplet) {
            ((JApplet)var2).getGlassPane().setCursor(var3);
            ((JApplet)var2).getGlassPane().setVisible(false);
         }
      }

   }

   public void hide() {
      if (this.contents instanceof BasicComboPopup && SyntheticaLookAndFeel.getBoolean("Synthetica.workaround.8173744.enabled", (Component)null, false)) {
         try {
            Field var1 = BasicComboPopup.class.getDeclaredField("comboBox");
            var1.setAccessible(true);
            final JComboBox var2 = (JComboBox)var1.get(this.contents);
            String var3 = "Synthetica.comboBox.popupWorkaround";
            if (this.isBugPosition(var2)) {
               long var4 = System.currentTimeMillis();
               long var6 = var4 - (Long)SyntheticaLookAndFeel.getClientProperty("Synthetica.comboBox.popupWorkaround", var2, var4);
               if (var6 < 100L) {
                  if (this.heavyWeightContainer instanceof Window) {
                     this.popup.hide();
                  }

                  SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        var2.setPopupVisible(true);
                        var2.putClientProperty("Synthetica.comboBox.popupWorkaround", System.currentTimeMillis());
                     }
                  });
                  return;
               }
            }

            var2.putClientProperty("Synthetica.comboBox.popupWorkaround", (Object)null);
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      }

      if (this.heavyWeightContainer instanceof Window && SyntheticaLookAndFeel.getBoolean("Synthetica.popupMenu.fade-out.enabled", this.contents)) {
         int var9 = SyntheticaLookAndFeel.getInt("Synthetica.popupMenu.fade-out.delay", this.contents, 25);
         int var11 = SyntheticaLookAndFeel.getInt("Synthetica.popupMenu.fade-out.duration", this.contents, 150);
         WindowFader var12 = new WindowFader((Window)this.heavyWeightContainer, this.popup, var9, var11, false, true);
         var12.start();
      } else if (this.popup != null) {
         this.popup.hide();
      }

      Container var10 = this.contents == null ? null : this.contents.getParent();
      if (var10 instanceof JComponent) {
         ((JComponent)var10).putClientProperty("POPUP_BACKGROUND", (Object)null);
      }

      if (this.heavyWeightContainer != null) {
         this.heavyWeightContainer = null;
         if (JavaVersion.JAVA5) {
            for(; var10 != null; var10 = var10.getParent()) {
               if (var10 instanceof JFrame) {
                  ((JFrame)var10).update(var10.getGraphics());
               }
            }
         }
      }

      this.contents = null;
      this.popup = null;
   }

   private boolean isBugPosition(JComponent var1) {
      PointerInfo var2 = MouseInfo.getPointerInfo();
      Point var3 = (Point)var2.getLocation().clone();
      SwingUtilities.convertPointFromScreen(var3, var1);
      if (var3.x != var1.getWidth() - 1 && var3.y != var1.getHeight() - 1) {
         var1 = (JComponent)SyntheticaLookAndFeel.findComponent((String)"ComboBox.arrowButton", var1);
         if (var1 == null) {
            return false;
         } else {
            var3 = var2.getLocation();
            SwingUtilities.convertPointFromScreen(var3, var1);
            Rectangle var4 = new Rectangle(0, 0, var1.getWidth(), var1.getHeight());
            return var4.contains(var3) && (var3.x == var4.width - 1 || var3.y == var4.height - 1);
         }
      } else {
         return true;
      }
   }

   public javax.swing.Popup getDelegate() {
      return this.popup;
   }

   public Component getContents() {
      return this.contents;
   }

   public void show() {
      final boolean var1 = this.contents instanceof JPopupMenu && SyntheticaLookAndFeel.getBoolean("Synthetica.popupMenu.blur.enabled", this.contents);
      if (var1 && this.heavyWeightContainer == null) {
         this.heavyWeightContainer = this.contents.getParent();
      }

      if (this.heavyWeightContainer == null) {
         this.popup.show();
      } else {
         if (this.showPopupMenuLater()) {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  Popup.this.showPopup(var1);
               }
            });
         } else {
            this.showPopup(var1);
         }

      }
   }

   private void showPopup(boolean var1) {
      if (this.heavyWeightContainer != null) {
         if (JavaVersion.JAVA7U8_OR_ABOVE && OS.getCurrentOS() == OS.Mac && this.contents instanceof JToolTip) {
            SyntheticaLookAndFeel.setChildrenOpaque(this.heavyWeightContainer, false);
         }

         boolean var2 = SyntheticaLookAndFeel.getBoolean("Synthetica.popupRobot.enabled", this.contents.getParent(), true);
         if (var2 && this.isSnapshotRequired(this.heavyWeightContainer instanceof Window ? (Window)this.heavyWeightContainer : null, var1)) {
            if (this.showPopupLater(this.heavyWeightContainer, var1)) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     if (Popup.this.contents != null) {
                        Container var1 = Popup.this.contents.getParent();
                        if (var1 instanceof JComponent) {
                           ((JComponent)var1).putClientProperty("POPUP_BACKGROUND", Popup.this.snapshot());
                           ((JComponent)var1).putClientProperty("POPUP_LIGHTWEIGHT", Popup.this.lightWeight);
                        }
                     }

                  }
               });
            } else {
               ((JComponent)this.contents.getParent()).putClientProperty("POPUP_BACKGROUND", this.snapshot());
               ((JComponent)this.contents.getParent()).putClientProperty("POPUP_LIGHTWEIGHT", this.lightWeight);
            }
         }

         if (this.heavyWeightContainer instanceof Window && SyntheticaLookAndFeel.getBoolean("Synthetica.popupMenu.fade-in.enabled", this.contents)) {
            int var3 = SyntheticaLookAndFeel.getInt("Synthetica.popupMenu.fade-in.delay", this.contents, 25);
            int var4 = SyntheticaLookAndFeel.getInt("Synthetica.popupMenu.fade-in.duration", this.contents, 200);
            WindowFader var5 = new WindowFader((Window)this.heavyWeightContainer, (javax.swing.Popup)null, var3, var4, true, false);
            var5.start();
         }

         if (this.showPopupLater(this.heavyWeightContainer, var1)) {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  if (Popup.this.popup != null) {
                     Popup.this.popup.show();
                  }

               }
            });
         } else {
            this.popup.show();
         }

         if (this.heavyWeightContainer instanceof Window && !this.isWindowOpacityEnabled((Window)this.heavyWeightContainer) && !var1) {
            if (OS.getCurrentOS() == OS.Windows && JavaVersion.JAVA7_OR_ABOVE && !this.showPopupMenuLater()) {
               SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                     SyntheticaLookAndFeel.setWindowOpaque((Window)Popup.this.heavyWeightContainer, false);
                  }
               });
            } else {
               SyntheticaLookAndFeel.setWindowOpaque((Window)this.heavyWeightContainer, false);
            }
         }

      }
   }

   private boolean showPopupLater(Container var1, boolean var2) {
      return var1 instanceof Window && this.isSnapshotRequired((Window)this.heavyWeightContainer, var2) && this.showPopupMenuLater();
   }

   private boolean isSnapshotRequired(Window var1, boolean var2) {
      return OS.getCurrentOS() != OS.Mac && (this.isWindowOpacityEnabled(var1) || var2) || OS.getCurrentOS() == OS.Mac && var2;
   }

   private boolean isWindowOpacityEnabled(Window var1) {
      boolean var2 = SyntheticaLookAndFeel.isWindowOpacityEnabled(var1);
      return !var2 ? false : this.isPopupMenuWindowOpaque();
   }

   private boolean isPopupMenuWindowOpaque() {
      return SyntheticaLookAndFeel.getBoolean("Synthetica.popupMenu.window.opaque", this.contents, true);
   }

   private boolean showPopupMenuLater() {
      return SyntheticaLookAndFeel.getBoolean("Synthetica.popupMenu.showLater", this.contents, true);
   }

   private BufferedImage snapshot() {
      BufferedImage var1 = null;

      try {
         Dimension var2 = this.heavyWeightContainer.getPreferredSize();
         if (var2.width > 0 && var2.height > 0) {
            Rectangle var3 = new Rectangle(this.x, this.y, var2.width, var2.height);
            var1 = this.createScreenCapture(var3);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return var1;
   }

   private BufferedImage createScreenCapture(Rectangle var1) throws AWTException {
      return (new Robot()).createScreenCapture(var1);
   }

   private static class WindowFader extends Timer {
      private static ActionListener listener = new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            WindowFader var2 = (WindowFader)var1.getSource();
            float var3 = (float)var2.counter / var2.repeats;
            Popup.WindowFader.setWindowOpacity(var2.window, var2.fadein ? var3 : 1.0F - var3);
            if ((float)var2.counter == var2.repeats) {
               var2.counter = 0;
               var2.stop();
               if (var2.hidePopup) {
                  if (var2.popup != null) {
                     var2.popup.hide();
                  }

                  Popup.WindowFader.setWindowOpacity(var2.window, 1.0F);
               }

               var2.popup = null;
               var2.window = null;
            } else {
               var2.counter = var2.counter + 1;
            }

         }
      };
      private Window window;
      private javax.swing.Popup popup;
      private boolean fadein;
      private boolean hidePopup;
      private float repeats;
      private int counter;

      public WindowFader(Window var1, javax.swing.Popup var2, int var3, int var4, boolean var5, boolean var6) {
         super(0, listener);
         this.window = var1;
         this.popup = var2;
         this.setDelay(var3);
         this.repeats = (float)(var4 / var3);
         this.fadein = var5;
         this.hidePopup = var6;
         setWindowOpacity(var1, var5 ? 0.01F : 1.0F);
      }

      private static void setWindowOpacity(Window var0, float var1) {
         if (JavaVersion.JAVA6U10_OR_ABOVE && AWTUtilities.isTranslucencySupported(Translucency.TRANSLUCENT)) {
            Class var2;
            Method var3;
            if (JavaVersion.JAVA6U10_OR_ABOVE && !JavaVersion.JAVA7) {
               try {
                  var2 = Class.forName("com.sun.awt.AWTUtilities");
                  var3 = var2.getMethod("setWindowOpacity", Window.class, Float.TYPE);
                  var3.invoke((Object)null, var0, var1);
               } catch (Exception var5) {
                  var5.printStackTrace();
               }
            } else {
               try {
                  var2 = Class.forName("java.awt.Window");
                  var3 = var2.getMethod("setOpacity", Float.TYPE);
                  var3.invoke(var0, var1);
               } catch (Exception var4) {
                  var4.printStackTrace();
               }
            }
         }

      }
   }
}
