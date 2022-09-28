package org.apache.batik.util.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.gui.resource.ButtonFactory;
import org.apache.batik.util.gui.resource.MissingListenerException;
import org.apache.batik.util.resources.ResourceManager;

public class MemoryMonitor extends JFrame implements ActionMap {
   protected static final String RESOURCE = "org.apache.batik.util.gui.resources.MemoryMonitorMessages";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.util.gui.resources.MemoryMonitorMessages", Locale.getDefault());
   protected static ResourceManager resources;
   protected Map listeners;
   protected Panel panel;

   public MemoryMonitor() {
      this(1000L);
   }

   public MemoryMonitor(long var1) {
      super(resources.getString("Frame.title"));
      this.listeners = new HashMap();
      this.listeners.put("CollectButtonAction", new CollectButtonAction());
      this.listeners.put("CloseButtonAction", new CloseButtonAction());
      this.panel = new Panel(var1);
      this.getContentPane().add(this.panel);
      this.panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), resources.getString("Frame.border_title")));
      JPanel var3 = new JPanel(new FlowLayout(2));
      ButtonFactory var4 = new ButtonFactory(bundle, this);
      var3.add(var4.createJButton("CollectButton"));
      var3.add(var4.createJButton("CloseButton"));
      this.getContentPane().add(var3, "South");
      this.pack();
      this.addWindowListener(new WindowAdapter() {
         public void windowActivated(WindowEvent var1) {
            RepaintThread var2 = MemoryMonitor.this.panel.getRepaintThread();
            if (!var2.isAlive()) {
               var2.start();
            } else {
               var2.safeResume();
            }

         }

         public void windowClosing(WindowEvent var1) {
            MemoryMonitor.this.panel.getRepaintThread().safeSuspend();
         }

         public void windowDeiconified(WindowEvent var1) {
            MemoryMonitor.this.panel.getRepaintThread().safeResume();
         }

         public void windowIconified(WindowEvent var1) {
            MemoryMonitor.this.panel.getRepaintThread().safeSuspend();
         }
      });
   }

   public Action getAction(String var1) throws MissingListenerException {
      return (Action)this.listeners.get(var1);
   }

   static {
      resources = new ResourceManager(bundle);
   }

   public static class RepaintThread extends Thread {
      protected long timeout;
      protected List components;
      protected Runtime runtime = Runtime.getRuntime();
      protected boolean suspended;
      protected UpdateRunnable updateRunnable;

      public RepaintThread(long var1, List var3) {
         this.timeout = var1;
         this.components = var3;
         this.updateRunnable = this.createUpdateRunnable();
         this.setPriority(1);
      }

      public void run() {
         while(true) {
            try {
               synchronized(this.updateRunnable) {
                  if (!this.updateRunnable.inEventQueue) {
                     EventQueue.invokeLater(this.updateRunnable);
                  }

                  this.updateRunnable.inEventQueue = true;
               }

               sleep(this.timeout);
               synchronized(this) {
                  while(this.suspended) {
                     this.wait();
                  }
               }
            } catch (InterruptedException var6) {
            }
         }
      }

      protected UpdateRunnable createUpdateRunnable() {
         return new UpdateRunnable();
      }

      public synchronized void safeSuspend() {
         if (!this.suspended) {
            this.suspended = true;
         }

      }

      public synchronized void safeResume() {
         if (this.suspended) {
            this.suspended = false;
            this.notify();
         }

      }

      protected class UpdateRunnable implements Runnable {
         public boolean inEventQueue = false;

         public void run() {
            long var1 = RepaintThread.this.runtime.freeMemory();
            long var3 = RepaintThread.this.runtime.totalMemory();
            Iterator var5 = RepaintThread.this.components.iterator();

            while(var5.hasNext()) {
               Component var6 = (Component)var5.next();
               ((MemoryChangeListener)var6).memoryStateChanged(var3, var1);
               var6.repaint();
            }

            synchronized(this) {
               this.inEventQueue = false;
            }
         }
      }
   }

   public interface MemoryChangeListener {
      void memoryStateChanged(long var1, long var3);
   }

   public static class History extends JPanel implements MemoryChangeListener {
      public static final int PREFERRED_WIDTH = 200;
      public static final int PREFERRED_HEIGHT = 100;
      protected static final Stroke GRID_LINES_STROKE = new BasicStroke(1.0F);
      protected static final Stroke CURVE_STROKE = new BasicStroke(2.0F, 1, 1);
      protected static final Stroke BORDER_STROKE = new BasicStroke(2.0F);
      protected Color gridLinesColor = new Color(0, 130, 0);
      protected Color curveColor;
      protected Color borderColor;
      protected List data;
      protected int xShift;
      protected long totalMemory;
      protected long freeMemory;
      protected GeneralPath path;

      public History() {
         this.curveColor = Color.yellow;
         this.borderColor = Color.green;
         this.data = new LinkedList();
         this.xShift = 0;
         this.path = new GeneralPath();
         this.setBackground(Color.black);
         this.setPreferredSize(new Dimension(200, 100));
      }

      public void memoryStateChanged(long var1, long var3) {
         this.totalMemory = var1;
         this.freeMemory = var3;
         this.data.add(new Long(this.totalMemory - this.freeMemory));
         if (this.data.size() > 190) {
            this.data.remove(0);
            this.xShift = (this.xShift + 1) % 10;
         }

         Iterator var5 = this.data.iterator();
         GeneralPath var6 = new GeneralPath();
         long var7 = (Long)var5.next();
         var6.moveTo(5.0F, (float)(this.totalMemory - var7) / (float)this.totalMemory * 80.0F + 10.0F);

         for(int var9 = 6; var5.hasNext(); ++var9) {
            var7 = (Long)var5.next();
            var6.lineTo((float)var9, (float)(this.totalMemory - var7) / (float)this.totalMemory * 80.0F + 10.0F);
         }

         this.path = var6;
      }

      protected void paintComponent(Graphics var1) {
         super.paintComponent(var1);
         Graphics2D var2 = (Graphics2D)var1;
         var2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         Dimension var3 = this.getSize();
         double var4 = (double)var3.width / 200.0;
         double var6 = (double)var3.height / 100.0;
         var2.transform(AffineTransform.getScaleInstance(var4, var6));
         var2.setPaint(this.gridLinesColor);
         var2.setStroke(GRID_LINES_STROKE);

         int var8;
         int var9;
         for(var8 = 1; var8 < 20; ++var8) {
            var9 = var8 * 10 + 5 - this.xShift;
            var2.draw(new Line2D.Double((double)var9, 5.0, (double)var9, 95.0));
         }

         for(var8 = 1; var8 < 9; ++var8) {
            var9 = var8 * 10 + 5;
            var2.draw(new Line2D.Double(5.0, (double)var9, 195.0, (double)var9));
         }

         var2.setPaint(this.curveColor);
         var2.setStroke(CURVE_STROKE);
         var2.draw(this.path);
         var2.setStroke(BORDER_STROKE);
         var2.setPaint(this.borderColor);
         var2.draw(new Rectangle2D.Double(5.0, 5.0, 190.0, 90.0));
      }
   }

   public static class Usage extends JPanel implements MemoryChangeListener {
      public static final int PREFERRED_WIDTH = 90;
      public static final int PREFERRED_HEIGHT = 100;
      protected static final String UNITS;
      protected static final String TOTAL;
      protected static final String USED;
      protected static final boolean POSTFIX;
      protected static final int FONT_SIZE = 9;
      protected static final int BLOCK_MARGIN = 10;
      protected static final int BLOCKS = 15;
      protected static final double BLOCK_WIDTH = 70.0;
      protected static final double BLOCK_HEIGHT = 3.8666666666666667;
      protected static final int[] BLOCK_TYPE;
      protected Color[] usedColors;
      protected Color[] freeColors;
      protected Font font;
      protected Color textColor;
      protected long totalMemory;
      protected long freeMemory;

      public Usage() {
         this.usedColors = new Color[]{Color.red, new Color(255, 165, 0), Color.green};
         this.freeColors = new Color[]{new Color(130, 0, 0), new Color(130, 90, 0), new Color(0, 130, 0)};
         this.font = new Font("SansSerif", 1, 9);
         this.textColor = Color.green;
         this.setBackground(Color.black);
         this.setPreferredSize(new Dimension(90, 100));
      }

      public void memoryStateChanged(long var1, long var3) {
         this.totalMemory = var1;
         this.freeMemory = var3;
      }

      public void setTextColor(Color var1) {
         this.textColor = var1;
      }

      public void setLowUsedMemoryColor(Color var1) {
         this.usedColors[2] = var1;
      }

      public void setMediumUsedMemoryColor(Color var1) {
         this.usedColors[1] = var1;
      }

      public void setHighUsedMemoryColor(Color var1) {
         this.usedColors[0] = var1;
      }

      public void setLowFreeMemoryColor(Color var1) {
         this.freeColors[2] = var1;
      }

      public void setMediumFreeMemoryColor(Color var1) {
         this.freeColors[1] = var1;
      }

      public void setHighFreeMemoryColor(Color var1) {
         this.freeColors[0] = var1;
      }

      protected void paintComponent(Graphics var1) {
         super.paintComponent(var1);
         Graphics2D var2 = (Graphics2D)var1;
         Dimension var3 = this.getSize();
         double var4 = (double)var3.width / 90.0;
         double var6 = (double)var3.height / 100.0;
         var2.transform(AffineTransform.getScaleInstance(var4, var6));
         var2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         int var8 = (int)Math.round(15.0 * (double)this.freeMemory / (double)this.totalMemory);

         int var9;
         Rectangle2D.Double var10;
         for(var9 = 0; var9 < var8; ++var9) {
            var10 = new Rectangle2D.Double(10.0, (double)var9 * 3.8666666666666667 + (double)var9 + 9.0 + 5.0, 70.0, 3.8666666666666667);
            var2.setPaint(this.freeColors[BLOCK_TYPE[var9]]);
            var2.fill(var10);
         }

         for(var9 = var8; var9 < 15; ++var9) {
            var10 = new Rectangle2D.Double(10.0, (double)var9 * 3.8666666666666667 + (double)var9 + 9.0 + 5.0, 70.0, 3.8666666666666667);
            var2.setPaint(this.usedColors[BLOCK_TYPE[var9]]);
            var2.fill(var10);
         }

         var2.setPaint(this.textColor);
         var2.setFont(this.font);
         long var15 = this.totalMemory / 1024L;
         long var11 = (this.totalMemory - this.freeMemory) / 1024L;
         String var13;
         String var14;
         if (POSTFIX) {
            var13 = var15 + UNITS + " " + TOTAL;
            var14 = var11 + UNITS + " " + USED;
         } else {
            var13 = TOTAL + " " + var15 + UNITS;
            var14 = USED + " " + var11 + UNITS;
         }

         var2.drawString(var13, 10, 10);
         var2.drawString(var14, 10, 97);
      }

      static {
         UNITS = MemoryMonitor.resources.getString("Usage.units");
         TOTAL = MemoryMonitor.resources.getString("Usage.total");
         USED = MemoryMonitor.resources.getString("Usage.used");
         POSTFIX = MemoryMonitor.resources.getBoolean("Usage.postfix");
         BLOCK_TYPE = new int[]{0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2};
      }
   }

   public static class Panel extends JPanel {
      protected RepaintThread repaintThread;

      public Panel() {
         this(1000L);
      }

      public Panel(long var1) {
         super(new GridBagLayout());
         ExtendedGridBagConstraints var3 = new ExtendedGridBagConstraints();
         var3.insets = new Insets(5, 5, 5, 5);
         ArrayList var4 = new ArrayList();
         JPanel var5 = new JPanel(new BorderLayout());
         var5.setBorder(BorderFactory.createLoweredBevelBorder());
         Usage var6 = new Usage();
         var5.add(var6);
         var3.weightx = 0.3;
         var3.weighty = 1.0;
         var3.fill = 1;
         var3.setGridBounds(0, 0, 1, 1);
         this.add(var5, var3);
         var4.add(var6);
         var5 = new JPanel(new BorderLayout());
         var5.setBorder(BorderFactory.createLoweredBevelBorder());
         History var7 = new History();
         var5.add(var7);
         var3.weightx = 0.7;
         var3.setGridBounds(1, 0, 1, 1);
         this.add(var5, var3);
         var4.add(var7);
         this.repaintThread = new RepaintThread(var1, var4);
      }

      public RepaintThread getRepaintThread() {
         return this.repaintThread;
      }
   }

   protected class CloseButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         MemoryMonitor.this.panel.getRepaintThread().safeSuspend();
         MemoryMonitor.this.dispose();
      }
   }

   protected class CollectButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         System.gc();
      }
   }
}
