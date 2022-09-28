package aggressor;

import common.CommonUtils;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class MultiFrame extends JFrame implements KeyEventDispatcher {
   protected JToolBar toolbar;
   protected JPanel content;
   protected CardLayout cards;
   protected LinkedList buttons;
   protected AggressorClient active;

   public Collection getOtherScriptEngines(AggressorClient var1) {
      Collection var2 = this.getScriptEngines();
      var2.remove(var1.getScriptEngine());
      return var2;
   }

   public Collection getScriptEngines() {
      synchronized(this.buttons) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = this.buttons.iterator();

         while(var3.hasNext()) {
            ClientInstance var4 = (ClientInstance)var3.next();
            var2.add(var4.app.getScriptEngine());
         }

         return var2;
      }
   }

   public Map getClients() {
      synchronized(this.buttons) {
         HashMap var2 = new HashMap();
         Iterator var3 = this.buttons.iterator();

         while(var3.hasNext()) {
            ClientInstance var4 = (ClientInstance)var3.next();
            var2.put(var4.button.getText(), var4.app);
         }

         return var2;
      }
   }

   public void setTitle(AggressorClient var1, String var2) {
      if (this.active == var1) {
         this.setTitle(var2);
      }

   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      return this.active != null ? this.active.getBindings().dispatchKeyEvent(var1) : false;
   }

   public void closeConnect() {
      synchronized(this.buttons) {
         if (this.buttons.size() == 0) {
            System.exit(0);
         }

      }
   }

   public void quit() {
      CommonUtils.Guard();
      synchronized(this.buttons) {
         ClientInstance var2 = null;
         this.content.remove(this.active);
         Iterator var3 = this.buttons.iterator();

         while(true) {
            if (var3.hasNext()) {
               var2 = (ClientInstance)var3.next();
               if (var2.app != this.active) {
                  continue;
               }

               this.toolbar.remove(var2.button);
               var3.remove();
               this.toolbar.validate();
               this.toolbar.repaint();
            }

            if (this.buttons.size() == 0) {
               System.exit(0);
            } else if (this.buttons.size() == 1) {
               this.getContentPane().remove(this.toolbar);
               this.validate();
            }

            if (var3.hasNext()) {
               var2 = (ClientInstance)var3.next();
            } else {
               var2 = (ClientInstance)this.buttons.getFirst();
            }

            this.set(var2.button);
            break;
         }
      }

      System.gc();
   }

   public MultiFrame() {
      super("");
      this.getContentPane().setLayout(new BorderLayout());
      this.toolbar = new JToolBar();
      this.content = new JPanel();
      this.cards = new CardLayout();
      this.content.setLayout(this.cards);
      this.getContentPane().add(this.content, "Center");
      this.buttons = new LinkedList();
      this.setDefaultCloseOperation(3);
      this.setSize(800, 600);
      this.setExtendedState(6);
      this.setIconImage(DialogUtils.getImage("resources/armitage-icon.gif"));
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
   }

   protected void set(JToggleButton var1) {
      CommonUtils.Guard();
      synchronized(this.buttons) {
         Iterator var3 = this.buttons.iterator();

         while(var3.hasNext()) {
            ClientInstance var4 = (ClientInstance)var3.next();
            if (var4.button.getText().equals(var1.getText())) {
               var4.button.setSelected(true);
               this.active = var4.app;
               this.setTitle(this.active.getTitle());
            } else {
               var4.button.setSelected(false);
            }
         }

         this.cards.show(this.content, var1.getText());
         this.active.touch();
      }
   }

   public boolean checkCollision(String var1) {
      synchronized(this.buttons) {
         Iterator var3 = this.buttons.iterator();

         ClientInstance var4;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            var4 = (ClientInstance)var3.next();
         } while(!var1.equals(var4.button.getText()));

         return true;
      }
   }

   public void addButton(String var1, final AggressorClient var2) {
      CommonUtils.Guard();
      if (this.checkCollision(var1)) {
         this.addButton(var1 + " (2)", var2);
      } else {
         synchronized(this.buttons) {
            final ClientInstance var4 = new ClientInstance();
            var4.button = new JToggleButton(var1);
            var4.button.setToolTipText(var1);
            var4.app = var2;
            var4.button.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  MultiFrame.this.set((JToggleButton)var1.getSource());
               }
            });
            var4.button.addMouseListener(new MouseAdapter() {
               public void check(MouseEvent var1) {
                  if (var1.isPopupTrigger()) {
                     final JToggleButton var2x = var4.button;
                     JPopupMenu var3 = new JPopupMenu();
                     JMenuItem var4x = new JMenuItem("Rename");
                     var4x.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent var1) {
                           String var2xx = JOptionPane.showInputDialog("Rename to?", var2x.getText());
                           if (var2xx != null) {
                              MultiFrame.this.content.remove(var2);
                              MultiFrame.this.content.add(var2, var2xx);
                              var2x.setText(var2xx);
                              MultiFrame.this.set(var2x);
                           }

                        }
                     });
                     var3.add(var4x);
                     JMenuItem var5 = new JMenuItem("Disconnect");
                     var5.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent var1) {
                           var4.app.kill();
                        }
                     });
                     var3.add(var5);
                     var3.show((JComponent)var1.getSource(), var1.getX(), var1.getY());
                     var1.consume();
                  }

               }

               public void mouseClicked(MouseEvent var1) {
                  this.check(var1);
               }

               public void mousePressed(MouseEvent var1) {
                  this.check(var1);
               }

               public void mouseReleased(MouseEvent var1) {
                  this.check(var1);
               }
            });
            this.toolbar.add(var4.button);
            this.content.add(var2, var1);
            this.buttons.add(var4);
            this.set(var4.button);
            if (this.buttons.size() == 1) {
               this.show();
            } else if (this.buttons.size() == 2) {
               this.getContentPane().add(this.toolbar, "South");
            }

            this.validate();
         }
      }
   }

   private static class ClientInstance {
      public AggressorClient app;
      public JToggleButton button;
      public boolean serviced;

      private ClientInstance() {
         this.serviced = false;
      }

      // $FF: synthetic method
      ClientInstance(Object var1) {
         this();
      }
   }
}
