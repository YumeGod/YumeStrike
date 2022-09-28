package ui;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ZoomableImage extends JLabel {
   protected Icon original = null;
   protected double zoom = 1.0;

   private JMenuItem zoomMenu(String var1, final double var2) {
      JMenuItem var4 = new JMenuItem(var1);
      var4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ZoomableImage.this.zoom = var2;
            ZoomableImage.this.updateIcon();
         }
      });
      return var4;
   }

   public ZoomableImage() {
      final JPopupMenu var1 = new JPopupMenu();
      var1.add(this.zoomMenu("25%", 0.25));
      var1.add(this.zoomMenu("50%", 0.5));
      var1.add(this.zoomMenu("75%", 0.75));
      var1.add(this.zoomMenu("100%", 1.0));
      var1.add(this.zoomMenu("125%", 1.25));
      var1.add(this.zoomMenu("150%", 1.5));
      var1.add(this.zoomMenu("200%", 2.0));
      var1.add(this.zoomMenu("250%", 2.5));
      this.addMouseListener(new MouseAdapter() {
         public void check(MouseEvent var1x) {
            if (var1x.isPopupTrigger()) {
               var1.show((JComponent)var1x.getSource(), var1x.getX(), var1x.getY());
               var1x.consume();
            }

         }

         public void mouseClicked(MouseEvent var1x) {
            this.check(var1x);
         }

         public void mousePressed(MouseEvent var1x) {
            this.check(var1x);
         }

         public void mouseReleased(MouseEvent var1x) {
            this.check(var1x);
         }
      });
      this.setHorizontalAlignment(0);
   }

   protected void updateIcon() {
      super.setIcon(this.resizeImage((ImageIcon)this.original));
   }

   public void setIcon(Icon var1) {
      this.original = var1;
      this.updateIcon();
   }

   protected Icon resizeImage(ImageIcon var1) {
      if (this.zoom != 1.0 && var1 != null) {
         int var2 = var1.getIconWidth();
         int var3 = var1.getIconHeight();
         BufferedImage var4 = new BufferedImage(var2, var3, 2);
         Graphics2D var5 = var4.createGraphics();
         var5.drawImage(var1.getImage(), 0, 0, var2, var3, (ImageObserver)null);
         var5.dispose();
         return new ImageIcon(var4.getScaledInstance((int)((double)var2 * this.zoom), (int)((double)var3 * this.zoom), 4));
      } else {
         return var1;
      }
   }
}
