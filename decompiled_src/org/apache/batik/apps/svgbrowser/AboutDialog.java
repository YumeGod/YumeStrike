package org.apache.batik.apps.svgbrowser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JWindow;
import org.apache.batik.Version;

public class AboutDialog extends JWindow {
   public static final String ICON_BATIK_SPLASH = "AboutDialog.icon.batik.splash";

   public AboutDialog() {
      this.buildGUI();
   }

   public AboutDialog(Frame var1) {
      super(var1);
      this.buildGUI();
      this.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            if (var1.getKeyCode() == 27) {
               AboutDialog.this.setVisible(false);
               AboutDialog.this.dispose();
            }

         }
      });
      this.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent var1) {
            AboutDialog.this.setVisible(false);
            AboutDialog.this.dispose();
         }
      });
   }

   public void setLocationRelativeTo(Frame var1) {
      Dimension var2 = var1.getSize();
      Point var3 = var1.getLocation();
      Point var4 = new Point(var3.x, var3.y);
      Rectangle var5 = this.getBounds();
      int var6 = var4.x + (var2.width - var5.width) / 2;
      int var7 = var4.y + (var2.height - var5.height) / 2;
      Dimension var8 = this.getToolkit().getScreenSize();
      if (var7 + var5.height > var8.height) {
         var7 = var8.height - var5.height;
         var6 = var4.x < var8.width >> 1 ? var4.x + var2.width : var4.x - var5.width;
      }

      if (var6 + var5.width > var8.width) {
         var6 = var8.width - var5.width;
      }

      if (var6 < 0) {
         var6 = 0;
      }

      if (var7 < 0) {
         var7 = 0;
      }

      this.setLocation(var6, var7);
   }

   protected void buildGUI() {
      this.getContentPane().setBackground(Color.white);
      ClassLoader var1 = this.getClass().getClassLoader();
      URL var2 = var1.getResource(Resources.getString("AboutDialog.icon.batik.splash"));
      ImageIcon var3 = new ImageIcon(var2);
      int var4 = var3.getIconWidth();
      int var5 = var3.getIconHeight();
      JLayeredPane var6 = new JLayeredPane();
      var6.setSize(600, 425);
      this.getContentPane().add(var6);
      JLabel var7 = new JLabel(var3);
      var7.setBounds(0, 0, var4, var5);
      var6.add(var7, new Integer(0));
      JLabel var8 = new JLabel("Batik " + Version.getVersion());
      var8.setForeground(new Color(232, 232, 232, 255));
      var8.setOpaque(false);
      var8.setBackground(new Color(0, 0, 0, 0));
      var8.setHorizontalAlignment(4);
      var8.setVerticalAlignment(3);
      var8.setBounds(var4 - 320, var5 - 117, 300, 100);
      var6.add(var8, new Integer(2));
      ((JComponent)this.getContentPane()).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(0, Color.gray, Color.black), BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), BorderFactory.createLineBorder(Color.black)), BorderFactory.createEmptyBorder(10, 10, 10, 10))));
   }
}
