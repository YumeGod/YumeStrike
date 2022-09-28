package ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

public class DraggableTabbedPane extends JTabbedPane {
   private boolean dragging = false;
   private Image tabImage = null;
   private Point currentMouseLocation = null;
   private int draggedTabIndex = 0;

   public DraggableTabbedPane() {
      this.addMouseMotionListener(new MouseMotionAdapter() {
         public void mouseDragged(MouseEvent var1) {
            if (!DraggableTabbedPane.this.dragging) {
               int var2 = DraggableTabbedPane.this.getUI().tabForCoordinate(DraggableTabbedPane.this, var1.getX(), var1.getY());
               if (var2 >= 0) {
                  DraggableTabbedPane.this.draggedTabIndex = var2;
                  Rectangle var3 = DraggableTabbedPane.this.getUI().getTabBounds(DraggableTabbedPane.this, var2);
                  BufferedImage var4 = new BufferedImage(DraggableTabbedPane.this.getWidth(), DraggableTabbedPane.this.getHeight(), 2);
                  Graphics var5 = var4.getGraphics();
                  var5.setClip(var3);
                  DraggableTabbedPane.this.setDoubleBuffered(false);
                  DraggableTabbedPane.this.paint(var5);
                  DraggableTabbedPane.this.tabImage = new BufferedImage(var3.width, var3.height, 2);
                  Graphics var6 = DraggableTabbedPane.this.tabImage.getGraphics();
                  var6.drawImage(var4, 0, 0, var3.width, var3.height, var3.x, var3.y, var3.x + var3.width, var3.y + var3.height, DraggableTabbedPane.this);
                  DraggableTabbedPane.this.dragging = true;
                  DraggableTabbedPane.this.repaint();
                  var6.dispose();
                  var5.dispose();
               }
            } else {
               DraggableTabbedPane.this.currentMouseLocation = var1.getPoint();
               DraggableTabbedPane.this.repaint();
            }

            super.mouseDragged(var1);
         }
      });
      this.addMouseListener(new MouseAdapter() {
         public void mouseReleased(MouseEvent var1) {
            if (DraggableTabbedPane.this.dragging) {
               int var2 = DraggableTabbedPane.this.getUI().tabForCoordinate(DraggableTabbedPane.this, var1.getX(), 10);
               if (var1.getX() < 0) {
                  var2 = 0;
               } else if (var2 == -1) {
                  var2 = DraggableTabbedPane.this.getTabCount() - 1;
               }

               if (var2 >= 0) {
                  Component var3 = DraggableTabbedPane.this.getComponentAt(DraggableTabbedPane.this.draggedTabIndex);
                  Component var4 = DraggableTabbedPane.this.getTabComponentAt(DraggableTabbedPane.this.draggedTabIndex);
                  DraggableTabbedPane.this.removeTabAt(DraggableTabbedPane.this.draggedTabIndex);
                  DraggableTabbedPane.this.insertTab("", (Icon)null, var3, (String)null, var2);
                  DraggableTabbedPane.this.setTabComponentAt(var2, var4);
                  DraggableTabbedPane.this.setSelectedIndex(var2);
               }
            }

            DraggableTabbedPane.this.dragging = false;
            DraggableTabbedPane.this.tabImage = null;
         }
      });
   }

   protected void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      if (this.dragging && this.currentMouseLocation != null && this.tabImage != null) {
         var1.drawImage(this.tabImage, this.currentMouseLocation.x, this.currentMouseLocation.y, this);
      }

   }
}
