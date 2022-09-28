package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.text.Document;

public class APasswordField extends JPasswordField {
   protected JPopupMenu menu = null;

   public APasswordField(int var1) {
      super(var1);
      this.createMenu();
   }

   public APasswordField(Document var1, String var2, int var3) {
      super(var1, var2, var3);
      this.createMenu();
   }

   public APasswordField(String var1, int var2) {
      super(var1, var2);
      this.createMenu();
   }

   public APasswordField() {
      this.createMenu();
   }

   public String getPass() {
      return new String(this.getPassword());
   }

   public void createMenu() {
      if (this.menu == null) {
         this.menu = new JPopupMenu();
         JMenuItem var1 = new JMenuItem("Cut", 67);
         JMenuItem var2 = new JMenuItem("Copy", 111);
         JMenuItem var3 = new JMenuItem("Paste", 80);
         JMenuItem var4 = new JMenuItem("Clear", 108);
         var1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               APasswordField.this.cut();
            }
         });
         var2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               APasswordField.this.copy();
            }
         });
         var3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               APasswordField.this.paste();
            }
         });
         var4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               APasswordField.this.setText("");
            }
         });
         this.menu.add(var1);
         this.menu.add(var2);
         this.menu.add(var3);
         this.menu.add(var4);
         this.addMouseListener(new MouseAdapter() {
            public void handle(MouseEvent var1) {
               if (var1.isPopupTrigger()) {
                  APasswordField.this.menu.show((JComponent)var1.getSource(), var1.getX(), var1.getY());
               }

            }

            public void mousePressed(MouseEvent var1) {
               this.handle(var1);
            }

            public void mouseClicked(MouseEvent var1) {
               this.handle(var1);
            }

            public void mouseReleased(MouseEvent var1) {
               this.handle(var1);
            }
         });
      }
   }
}
