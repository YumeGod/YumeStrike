package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

public class CopyPopup {
   protected JPopupMenu menu = null;
   protected JTextComponent component = null;

   public CopyPopup(JTextComponent var1) {
      this.component = var1;
      this.createMenu();
   }

   public void createMenu() {
      if (this.menu == null) {
         this.menu = new JPopupMenu();
         JMenuItem var1 = new JMenuItem("Copy", 111);
         var1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               CopyPopup.this.component.copy();
            }
         });
         this.menu.add(var1);
         this.component.addMouseListener(new MouseAdapter() {
            public void handle(MouseEvent var1) {
               if (var1.isPopupTrigger()) {
                  CopyPopup.this.menu.show((JComponent)var1.getSource(), var1.getX(), var1.getY());
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
