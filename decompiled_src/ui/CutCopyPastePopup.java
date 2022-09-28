package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

public class CutCopyPastePopup {
   protected JPopupMenu menu = null;
   protected JTextComponent component = null;

   public CutCopyPastePopup(JTextComponent var1) {
      this.component = var1;
      this.createMenu();
   }

   public void createMenu() {
      if (this.menu == null) {
         this.menu = new JPopupMenu();
         JMenuItem var1 = new JMenuItem("Cut", 67);
         var1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               CutCopyPastePopup.this.component.cut();
            }
         });
         JMenuItem var2 = new JMenuItem("Copy", 111);
         var2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               CutCopyPastePopup.this.component.copy();
            }
         });
         JMenuItem var3 = new JMenuItem("Paste", 112);
         var3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               CutCopyPastePopup.this.component.paste();
            }
         });
         JMenuItem var4 = new JMenuItem("Clear", 108);
         var4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               CutCopyPastePopup.this.component.setText("");
            }
         });
         this.menu.add(var1);
         this.menu.add(var2);
         this.menu.add(var3);
         this.menu.add(var4);
         this.component.addMouseListener(new MouseAdapter() {
            public void handle(MouseEvent var1) {
               if (var1.isPopupTrigger()) {
                  CutCopyPastePopup.this.menu.show((JComponent)var1.getSource(), var1.getX(), var1.getY());
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
