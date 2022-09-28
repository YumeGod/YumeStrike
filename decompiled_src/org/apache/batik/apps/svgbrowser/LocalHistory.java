package org.apache.batik.apps.svgbrowser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class LocalHistory {
   protected JSVGViewerFrame svgFrame;
   protected JMenu menu;
   protected int index;
   protected List visitedURIs = new ArrayList();
   protected int currentURI = -1;
   protected ButtonGroup group = new ButtonGroup();
   protected ActionListener actionListener = new RadioListener();
   protected int state;
   protected static final int STABLE_STATE = 0;
   protected static final int BACK_PENDING_STATE = 1;
   protected static final int FORWARD_PENDING_STATE = 2;
   protected static final int RELOAD_PENDING_STATE = 3;

   public LocalHistory(JMenuBar var1, JSVGViewerFrame var2) {
      this.svgFrame = var2;
      int var3 = var1.getMenuCount();

      for(int var4 = 0; var4 < var3; ++var4) {
         JMenu var5 = var1.getMenu(var4);
         int var6 = var5.getItemCount();

         for(int var7 = 0; var7 < var6; ++var7) {
            JMenuItem var8 = var5.getItem(var7);
            if (var8 != null) {
               String var9 = var8.getText();
               if ("@@@".equals(var9)) {
                  this.menu = var5;
                  this.index = var7;
                  var5.remove(var7);
                  return;
               }
            }
         }
      }

      throw new IllegalArgumentException("No '@@@' marker found");
   }

   public void back() {
      this.update();
      this.state = 1;
      this.currentURI -= 2;
      this.svgFrame.showSVGDocument((String)this.visitedURIs.get(this.currentURI + 1));
   }

   public boolean canGoBack() {
      return this.currentURI > 0;
   }

   public void forward() {
      this.update();
      this.state = 2;
      this.svgFrame.showSVGDocument((String)this.visitedURIs.get(this.currentURI + 1));
   }

   public boolean canGoForward() {
      return this.currentURI < this.visitedURIs.size() - 1;
   }

   public void reload() {
      this.update();
      this.state = 3;
      --this.currentURI;
      this.svgFrame.showSVGDocument((String)this.visitedURIs.get(this.currentURI + 1));
   }

   public void update(String var1) {
      if (this.currentURI < -1) {
         throw new IllegalStateException("Unexpected currentURI:" + this.currentURI);
      } else {
         this.state = 0;
         int var3;
         JMenuItem var5;
         if (++this.currentURI < this.visitedURIs.size()) {
            if (!this.visitedURIs.get(this.currentURI).equals(var1)) {
               int var2 = this.menu.getItemCount();

               for(var3 = var2 - 1; var3 >= this.index + this.currentURI + 1; --var3) {
                  JMenuItem var4 = this.menu.getItem(var3);
                  this.group.remove(var4);
                  this.menu.remove(var3);
               }

               this.visitedURIs = this.visitedURIs.subList(0, this.currentURI + 1);
            }

            var5 = this.menu.getItem(this.index + this.currentURI);
            this.group.remove(var5);
            this.menu.remove(this.index + this.currentURI);
            this.visitedURIs.set(this.currentURI, var1);
         } else {
            if (this.visitedURIs.size() >= 15) {
               this.visitedURIs.remove(0);
               var5 = this.menu.getItem(this.index);
               this.group.remove(var5);
               this.menu.remove(this.index);
               --this.currentURI;
            }

            this.visitedURIs.add(var1);
         }

         String var6 = var1;
         var3 = var1.lastIndexOf(47);
         if (var3 == -1) {
            var3 = var1.lastIndexOf(92);
         }

         if (var3 != -1) {
            var6 = var1.substring(var3 + 1);
         }

         JRadioButtonMenuItem var7 = new JRadioButtonMenuItem(var6);
         var7.setToolTipText(var1);
         var7.setActionCommand(var1);
         var7.addActionListener(this.actionListener);
         this.group.add(var7);
         var7.setSelected(true);
         this.menu.insert(var7, this.index + this.currentURI);
      }
   }

   protected void update() {
      switch (this.state) {
         case 0:
         case 2:
         default:
            break;
         case 1:
            this.currentURI += 2;
            break;
         case 3:
            ++this.currentURI;
      }

   }

   protected class RadioListener implements ActionListener {
      public void actionPerformed(ActionEvent var1) {
         String var2 = var1.getActionCommand();
         LocalHistory.this.currentURI = this.getItemIndex((JMenuItem)var1.getSource()) - 1;
         LocalHistory.this.svgFrame.showSVGDocument(var2);
      }

      public int getItemIndex(JMenuItem var1) {
         int var2 = LocalHistory.this.menu.getItemCount();

         for(int var3 = LocalHistory.this.index; var3 < var2; ++var3) {
            if (LocalHistory.this.menu.getItem(var3) == var1) {
               return var3 - LocalHistory.this.index;
            }
         }

         throw new IllegalArgumentException("MenuItem is not from my menu!");
      }
   }
}
