package ui;

import common.CommonUtils;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ListCopyPopup {
   protected JPopupMenu menu = null;
   protected JList component = null;

   public ListCopyPopup(JList var1) {
      this.component = var1;
      this.createMenu();
   }

   public void createMenu() {
      if (this.menu == null) {
         this.menu = new JPopupMenu();
         JMenuItem var1 = new JMenuItem("Copy", 111);
         var1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               DialogUtils.addToClipboard(CommonUtils.join((Collection)(new LinkedList(ListCopyPopup.this.component.getSelectedValuesList())), (String)", "));
            }
         });
         this.menu.add(var1);
         this.component.addMouseListener(new MouseAdapter() {
            public void handle(MouseEvent var1) {
               if (var1.isPopupTrigger()) {
                  ListCopyPopup.this.menu.show((JComponent)var1.getSource(), var1.getX(), var1.getY());
                  var1.consume();
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
