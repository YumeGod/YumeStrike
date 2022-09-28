package org.apache.batik.apps.svgbrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import org.apache.batik.util.resources.ResourceManager;

public class StatusBar extends JPanel {
   protected static final String RESOURCES = "org.apache.batik.apps.svgbrowser.resources.StatusBarMessages";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.apps.svgbrowser.resources.StatusBarMessages", Locale.getDefault());
   protected static ResourceManager rManager;
   protected JLabel xPosition;
   protected JLabel yPosition;
   protected JLabel zoom;
   protected JLabel message;
   protected String mainMessage;
   protected String temporaryMessage;
   protected DisplayThread displayThread;

   public StatusBar() {
      super(new BorderLayout(5, 5));
      JPanel var1 = new JPanel(new BorderLayout(0, 0));
      this.add("West", var1);
      this.xPosition = new JLabel();
      BevelBorder var2 = new BevelBorder(1, this.getBackground().brighter().brighter(), this.getBackground(), this.getBackground().darker().darker(), this.getBackground());
      this.xPosition.setBorder(var2);
      this.xPosition.setPreferredSize(new Dimension(110, 16));
      var1.add("West", this.xPosition);
      this.yPosition = new JLabel();
      this.yPosition.setBorder(var2);
      this.yPosition.setPreferredSize(new Dimension(110, 16));
      var1.add("Center", this.yPosition);
      this.zoom = new JLabel();
      this.zoom.setBorder(var2);
      this.zoom.setPreferredSize(new Dimension(70, 16));
      var1.add("East", this.zoom);
      var1 = new JPanel(new BorderLayout(0, 0));
      this.message = new JLabel();
      this.message.setBorder(var2);
      var1.add(this.message);
      this.add(var1);
      this.setMainMessage(rManager.getString("Panel.default_message"));
   }

   public void setXPosition(float var1) {
      this.xPosition.setText("x: " + var1);
   }

   public void setWidth(float var1) {
      this.xPosition.setText(rManager.getString("Position.width_letters") + " " + var1);
   }

   public void setYPosition(float var1) {
      this.yPosition.setText("y: " + var1);
   }

   public void setHeight(float var1) {
      this.yPosition.setText(rManager.getString("Position.height_letters") + " " + var1);
   }

   public void setZoom(float var1) {
      var1 = var1 > 0.0F ? var1 : -var1;
      if (var1 == 1.0F) {
         this.zoom.setText("1:1");
      } else {
         String var2;
         if (var1 >= 1.0F) {
            var2 = Float.toString(var1);
            if (var2.length() > 6) {
               var2 = var2.substring(0, 6);
            }

            this.zoom.setText("1:" + var2);
         } else {
            var2 = Float.toString(1.0F / var1);
            if (var2.length() > 6) {
               var2 = var2.substring(0, 6);
            }

            this.zoom.setText(var2 + ":1");
         }
      }

   }

   public void setMessage(String var1) {
      this.setPreferredSize(new Dimension(0, this.getPreferredSize().height));
      if (this.displayThread != null) {
         this.displayThread.finish();
      }

      this.temporaryMessage = var1;
      DisplayThread var2 = this.displayThread;
      this.displayThread = new DisplayThread(var2);
      this.displayThread.start();
   }

   public void setMainMessage(String var1) {
      this.mainMessage = var1;
      this.message.setText(this.mainMessage = var1);
      if (this.displayThread != null) {
         this.displayThread.finish();
         this.displayThread = null;
      }

      this.setPreferredSize(new Dimension(0, this.getPreferredSize().height));
   }

   static {
      rManager = new ResourceManager(bundle);
   }

   protected class DisplayThread extends Thread {
      static final long DEFAULT_DURATION = 5000L;
      long duration;
      Thread toJoin;

      public DisplayThread() {
         this(5000L, (Thread)null);
      }

      public DisplayThread(long var2) {
         this(var2, (Thread)null);
      }

      public DisplayThread(Thread var2) {
         this(5000L, var2);
      }

      public DisplayThread(long var2, Thread var4) {
         this.duration = var2;
         this.toJoin = var4;
         this.setPriority(1);
      }

      public synchronized void finish() {
         this.duration = 0L;
         this.notifyAll();
      }

      public void run() {
         synchronized(this) {
            if (this.toJoin != null) {
               while(this.toJoin.isAlive()) {
                  try {
                     this.toJoin.join();
                  } catch (InterruptedException var8) {
                  }
               }

               this.toJoin = null;
            }

            StatusBar.this.message.setText(StatusBar.this.temporaryMessage);

            long var4;
            for(long var2 = System.currentTimeMillis(); this.duration > 0L; var2 = var4) {
               try {
                  this.wait(this.duration);
               } catch (InterruptedException var7) {
               }

               var4 = System.currentTimeMillis();
               this.duration -= var4 - var2;
            }

            StatusBar.this.message.setText(StatusBar.this.mainMessage);
         }
      }
   }
}
