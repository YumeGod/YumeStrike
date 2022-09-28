package console;

import aggressor.Prefs;
import common.CommonUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class StatusBar extends JPanel {
   protected JTextPane left = new JTextPane();
   protected JTextPane right = new JTextPane();
   protected Colors colors = null;

   public StatusBar(Properties var1) {
      this.setLayout(new BorderLayout());
      this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      this.left.setEditable(false);
      this.right.setEditable(false);
      Color var2 = Prefs.getPreferences().getColor("statusbar.foreground.color", "#000000");
      this.left.setForeground(var2);
      this.right.setForeground(var2);
      this.add(this.left, "West");
      this.add(this.right, "East");
      this.colors = new Colors(var1);
      Color var3 = Prefs.getPreferences().getColor("statusbar.background.color", "#d3d3d3");
      this.setBackground(var3);
   }

   public void setBackground(Color var1) {
      super.setBackground(var1);
      if (this.left != null) {
         this.left.setBackground(var1);
      }

      if (this.right != null) {
         this.right.setBackground(var1);
      }

   }

   public void left(String var1) {
      this.updateText(this.left, var1);
   }

   public void right(String var1) {
      this.updateText(this.right, var1);
   }

   public void set(final String var1, final String var2) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            StatusBar.this.colors.setNoHack(StatusBar.this.left, var1);
            StatusBar.this.colors.setNoHack(StatusBar.this.right, var2);
         }
      });
   }

   protected void updateText(final JTextPane var1, final String var2) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            StatusBar.this.colors.set(var1, var2);
         }
      });
   }

   public void setFont(Font var1) {
      if (this.left != null) {
         this.left.setFont(var1);
      }

      if (this.right != null) {
         this.right.setFont(var1);
      }

   }
}
