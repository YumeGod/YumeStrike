package aggressor;

import aggressor.dialogs.PreferencesDialog;
import aggressor.dialogs.SessionChooser;
import common.AObject;
import common.BeaconEntry;
import common.Callback;
import common.CommonUtils;
import common.TabScreenshot;
import console.Activity;
import console.Associated;
import console.Console;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SolidIcon;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import ui.ColorPanel;
import ui.DraggableTabbedPane;
import ui.KeyHandler;

public class TabManager extends AObject implements Callback {
   protected JTabbedPane tabs = new DraggableTabbedPane();
   protected ApplicationTab docked = null;
   protected LinkedList apptabs = new LinkedList();
   protected AggressorClient client = null;
   protected ColorPanel colors = new ColorPanel();

   public boolean activate(String var1) {
      CommonUtils.Guard();
      Iterator var2 = this.apptabs.iterator();

      ApplicationTab var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (ApplicationTab)var2.next();
      } while(!var1.equals(var3.title));

      this.tabs.setSelectedComponent(var3.component);
      return true;
   }

   public boolean activateConsole(String var1) {
      CommonUtils.Guard();
      Iterator var2 = this.apptabs.iterator();

      ApplicationTab var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (ApplicationTab)var2.next();
      } while(!var1.equals(var3.bid) || !(var3.component instanceof Console));

      this.tabs.setSelectedComponent(var3.component);
      ((Console)var3.component).requestFocusInWindow();
      return true;
   }

   public void bindShortcuts() {
      this.client.bindKey("Ctrl+I", new KeyHandler() {
         public void key_pressed(String var1) {
            (new SessionChooser(TabManager.this.client, new SafeDialogCallback() {
               public void dialogResult(String var1) {
                  DialogUtils.openOrActivate(TabManager.this.client, var1);
               }
            })).show();
         }
      });
      this.client.bindKey("Ctrl+W", new KeyHandler() {
         public void key_pressed(String var1) {
            TabManager.this.openActiveTab();
         }
      });
      this.client.bindKey("Ctrl+B", new KeyHandler() {
         public void key_pressed(String var1) {
            TabManager.this.dockActiveTab();
         }
      });
      this.client.bindKey("Ctrl+E", new KeyHandler() {
         public void key_pressed(String var1) {
            TabManager.this.noDock();
         }
      });
      this.client.bindKey("Ctrl+D", new KeyHandler() {
         public void key_pressed(String var1) {
            TabManager.this.closeActiveTab();
         }
      });
      this.client.bindKey("Shift+Ctrl+D", new KeyHandler() {
         public void key_pressed(String var1) {
            TabManager.this.closeAllButActiveTab();
         }
      });
      this.client.bindKey("Ctrl+R", new KeyHandler() {
         public void key_pressed(String var1) {
            TabManager.this.renameActiveTab();
         }
      });
      this.client.bindKey("Ctrl+T", new KeyHandler() {
         public void key_pressed(String var1) {
            TabManager.this.snapActiveTab();
            DialogUtils.showInfo("Pushed screenshot to team server (active tab)");
         }
      });
      this.client.bindKey("Shift+Ctrl+T", new KeyHandler() {
         public void key_pressed(String var1) {
            TabManager.this.snapActiveWindow();
            DialogUtils.showInfo("Pushed screenshot to team server (window)");
         }
      });
      this.client.bindKey("Ctrl+Left", new KeyHandler() {
         public void key_pressed(String var1) {
            TabManager.this.previousTab();
         }
      });
      this.client.bindKey("Ctrl+Right", new KeyHandler() {
         public void key_pressed(String var1) {
            TabManager.this.nextTab();
         }
      });
      this.client.bindKey("Ctrl+O", new KeyHandler() {
         public void key_pressed(String var1) {
            (new PreferencesDialog()).show();
         }
      });
   }

   public TabManager(AggressorClient var1) {
      this.client = var1;
      this.bindShortcuts();
   }

   public JTabbedPane getTabbedPane() {
      return this.tabs;
   }

   public void _removeTab(JComponent var1) {
      this.tabs.remove(var1);
      this.tabs.validate();
   }

   public void removeTab(final JComponent var1) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            TabManager.this._removeTab(var1);
         }
      });
   }

   public void nextTab() {
      this.tabs.setSelectedIndex((this.tabs.getSelectedIndex() + 1) % this.tabs.getTabCount());
   }

   public void previousTab() {
      if (this.tabs.getSelectedIndex() == 0) {
         this.tabs.setSelectedIndex(this.tabs.getTabCount() - 1);
      } else {
         this.tabs.setSelectedIndex((this.tabs.getSelectedIndex() - 1) % this.tabs.getTabCount());
      }

   }

   public void addTab(final String var1, final JComponent var2, final ActionListener var3) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            TabManager.this._addTab(var1, var2, var3, (String)null);
         }
      });
   }

   public void addTab(final String var1, final JComponent var2, final ActionListener var3, final String var4) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            TabManager.this._addTab(var1, var2, var3, var4);
         }
      });
   }

   public void closeActiveTab() {
      CommonUtils.Guard();
      JComponent var1 = (JComponent)this.tabs.getSelectedComponent();
      if (var1 != null) {
         this.removeAppTab(var1, (String)null, new ActionEvent(var1, 0, "boo!"));
      }

   }

   public void closeAllButActiveTab() {
      CommonUtils.Guard();
      JComponent var1 = (JComponent)this.tabs.getSelectedComponent();
      Iterator var2 = (new LinkedList(this.apptabs)).iterator();

      while(var2.hasNext()) {
         ApplicationTab var3 = (ApplicationTab)var2.next();
         if (var3.component != var1) {
            this.removeAppTab(var3.component, (String)null, new ActionEvent(var3.component, 0, "boo!"));
         }
      }

   }

   public void openActiveTab() {
      CommonUtils.Guard();
      JComponent var1 = (JComponent)this.tabs.getSelectedComponent();
      if (var1 != null) {
         this.popAppTab(var1);
      }

   }

   public void noDock() {
      CommonUtils.Guard();
      if (this.docked != null) {
         if (this.docked.removeListener != null) {
            this.docked.removeListener.actionPerformed(new ActionEvent(this.docked.component, 0, "close"));
         }

         this.client.noDock();
         this.docked = null;
      }

   }

   public void dockActiveTab() {
      CommonUtils.Guard();
      JComponent var1 = (JComponent)this.tabs.getSelectedComponent();
      if (var1 != null) {
         this.dockAppTab(var1);
      }

   }

   public void snapActiveWindow() {
      CommonUtils.Guard();
      byte[] var1 = DialogUtils.screenshot(this.client.getWindow());
      this.client.getConnection().call("aggressor.screenshot", CommonUtils.args(new TabScreenshot(this.client.getWindow().getTitle(), var1)));
   }

   public void renameActiveTab() {
      CommonUtils.Guard();
      JComponent var1 = (JComponent)this.tabs.getSelectedComponent();
      Iterator var2 = this.apptabs.iterator();

      ApplicationTab var3;
      do {
         if (!var2.hasNext()) {
            return;
         }

         var3 = (ApplicationTab)var2.next();
      } while(var3.component != var1);

      this.renameAppTab(var3.label);
   }

   public void snapActiveTab() {
      CommonUtils.Guard();
      JComponent var1 = (JComponent)this.tabs.getSelectedComponent();
      Iterator var2 = this.apptabs.iterator();

      while(var2.hasNext()) {
         ApplicationTab var3 = (ApplicationTab)var2.next();
         if (var3.component == var1) {
            this.snapAppTab(var3.title, var1);
         }
      }

   }

   public void addAppTab(String var1, JComponent var2, JLabel var3, String var4, ActionListener var5) {
      CommonUtils.Guard();
      ApplicationTab var6 = new ApplicationTab();
      var6.title = var1;
      var6.component = var2;
      var6.removeListener = var5;
      var6.label = var3;
      var6.bid = var4;
      this.apptabs.add(var6);
      if (!"".equals(var4)) {
         BeaconEntry var7 = DataUtils.getBeacon(this.client.getData(), var4);
         if (var7 != null) {
            this.processBeacon(var6, var7);
         }
      }

   }

   public void popAppTab(Component var1) {
      CommonUtils.Guard();
      Iterator var2 = this.apptabs.iterator();

      while(var2.hasNext()) {
         final ApplicationTab var3 = (ApplicationTab)var2.next();
         if (var3.component == var1) {
            this.tabs.remove(var3.component);
            var2.remove();
            final JFrame var4 = new JFrame(var3.title);
            var4.setLayout(new BorderLayout());
            var4.add(var3.component, "Center");
            var4.pack();
            var3.component.validate();
            var4.addWindowListener(new WindowAdapter() {
               public void windowClosing(WindowEvent var1) {
                  if (var3.removeListener != null) {
                     var3.removeListener.actionPerformed(new ActionEvent(var1.getSource(), 0, "close"));
                  }

               }

               public void windowOpened(WindowEvent var1) {
                  var4.setState(0);
                  var3.component.requestFocusInWindow();
               }

               public void windowActivated(WindowEvent var1) {
                  var3.component.requestFocusInWindow();
               }
            });
            var4.setState(1);
            var4.setVisible(true);
         }
      }

   }

   public void dockAppTab(Component var1) {
      CommonUtils.Guard();
      Iterator var2 = this.apptabs.iterator();

      while(var2.hasNext()) {
         ApplicationTab var3 = (ApplicationTab)var2.next();
         if (var3.component == var1) {
            this.tabs.remove(var3.component);
            var2.remove();
            Dimension var4 = new Dimension(100, 150);
            if (this.docked != null) {
               var4 = this.docked.component.getSize();
               if (this.docked.removeListener != null) {
                  this.docked.removeListener.actionPerformed(new ActionEvent(this.docked.component, 0, "close"));
               }
            }

            this.client.dock(var3.component, var4);
            this.docked = var3;
         }
      }

   }

   public void snapAppTab(String var1, Component var2) {
      byte[] var3 = DialogUtils.screenshot(var2);
      this.client.getConnection().call("aggressor.screenshot", CommonUtils.args(new TabScreenshot(var1, var3)));
   }

   public void renameAppTab(JLabel var1) {
      String var2 = JOptionPane.showInputDialog("Rename tab to:", (var1.getText() + "").trim());
      if (var2 != null) {
         var1.setText(var2 + "   ");
      }

   }

   public void removeAppTab(Component var1, String var2, ActionEvent var3) {
      CommonUtils.Guard();
      Iterator var4 = this.apptabs.iterator();
      String var5 = var2 != null ? var2.split(" ")[0] : "%b%";

      while(true) {
         ApplicationTab var6;
         String var7;
         do {
            if (!var4.hasNext()) {
               return;
            }

            var6 = (ApplicationTab)var4.next();
            var7 = var6.title != null ? var6.title.split(" ")[0] : "%a%";
         } while(var6.component != var1 && !var7.equals(var5));

         this.tabs.remove(var6.component);
         if (var6.removeListener != null) {
            var6.removeListener.actionPerformed(var3);
         }

         var4.remove();
      }
   }

   public void _addTab(final String var1, final JComponent var2, ActionListener var3, String var4) {
      if (var3 == null) {
         CommonUtils.print_error("Opened: " + var1 + " with no remove listener");
      }

      final Component var5 = this.tabs.add("", var2);
      final JLabel var6 = new JLabel(var1 + "   ");
      JPanel var7 = new JPanel();
      var7.setOpaque(false);
      var7.setLayout(new BorderLayout());
      var7.add(var6, "Center");
      if (var2 instanceof Activity) {
         ((Activity)var2).registerLabel(var6);
      }

      String var8 = "";
      if (var2 instanceof Associated) {
         var8 = ((Associated)var2).getBeaconID();
      }

      JButton var9 = new JButton("X");
      var9.setOpaque(false);
      var9.setContentAreaFilled(false);
      var9.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      var7.add(var9, "East");
      if (var4 != null) {
         var9.setToolTipText(var4);
      }

      int var10 = this.tabs.indexOfComponent(var5);
      this.tabs.setTabComponentAt(var10, var7);
      this.addAppTab(var1, var2, var6, var8, var3);
      var9.addMouseListener(new MouseAdapter() {
         public void check(MouseEvent var1x) {
            if (var1x.isPopupTrigger()) {
               JPopupMenu var2x = new JPopupMenu();
               JMenuItem var3 = new JMenuItem("Open in window", 79);
               var3.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent var1x) {
                     TabManager.this.popAppTab(var5);
                  }
               });
               JMenuItem var4 = new JMenuItem("Close like tabs", 67);
               var4.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent var1x) {
                     TabManager.this.removeAppTab((Component)null, var1, var1x);
                  }
               });
               JMenuItem var5x = new JMenuItem("Save screenshot", 83);
               var5x.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent var1x) {
                     TabManager.this.snapAppTab(var1, var2);
                     DialogUtils.showInfo("Pushed screenshot to team server");
                  }
               });
               JMenuItem var6x = new JMenuItem("Send to bottom", 98);
               var6x.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent var1x) {
                     TabManager.this.dockAppTab(var5);
                  }
               });
               JMenuItem var7 = new JMenuItem("Rename Tab", 82);
               var7.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent var1x) {
                     TabManager.this.renameAppTab(var6);
                  }
               });
               var2x.add(var3);
               var2x.add(var5x);
               var2x.add(var6x);
               var2x.add(var7);
               var2x.addSeparator();
               var2x.add(var4);
               var2x.show((Component)var1x.getSource(), var1x.getX(), var1x.getY());
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
      var9.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            if ((var1x.getModifiers() & 2) == 2) {
               TabManager.this.popAppTab(var5);
            } else if ((var1x.getModifiers() & 1) == 1) {
               TabManager.this.removeAppTab((Component)null, var1, var1x);
            } else {
               TabManager.this.removeAppTab(var5, (String)null, var1x);
            }

            System.gc();
         }
      });
      var5.addComponentListener(new ComponentAdapter() {
         public void componentShown(ComponentEvent var1) {
            if (var5 instanceof Activity) {
               ((Activity)var5).resetNotification();
            }

            var5.requestFocusInWindow();
            System.gc();
         }
      });
      this.tabs.setSelectedIndex(var10);
      var5.requestFocusInWindow();
   }

   public void touch() {
      CommonUtils.Guard();
      Component var1 = this.tabs.getSelectedComponent();
      if (var1 != null) {
         if (var1 instanceof Activity) {
            ((Activity)var1).resetNotification();
         }

         var1.requestFocusInWindow();
      }
   }

   public void start() {
      this.client.getData().subscribe("beacons", this);
   }

   public void stop() {
      this.client.getData().unsub("beacons", this);
   }

   public void processBeacon(ApplicationTab var1, BeaconEntry var2) {
      String var3 = var2.title() + "   ";
      if (!var1.title.equals(var3) && var1.component instanceof Console) {
         var1.label.setText(var3);
         var1.title = var3;
      }

      String var4 = var2.getAccent();
      if ("".equals(var4)) {
         var1.label.setIcon((Icon)null);
      } else {
         var1.label.setIcon(new SolidIcon(this.colors.getBackColor(var4), 12, 12));
      }

   }

   public void result(String var1, final Object var2) {
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            Iterator var1 = TabManager.this.apptabs.iterator();

            while(var1.hasNext()) {
               ApplicationTab var2x = (ApplicationTab)var1.next();
               if (!"".equals(var2x.bid)) {
                  BeaconEntry var3 = DataUtils.getBeaconFromResult(var2, var2x.bid);
                  if (var3 != null) {
                     TabManager.this.processBeacon(var2x, var3);
                  }
               }
            }

         }
      });
   }

   private static class ApplicationTab {
      public String title;
      public JComponent component;
      public ActionListener removeListener;
      public JLabel label;
      public String bid;

      private ApplicationTab() {
      }

      public String toString() {
         return this.title;
      }

      // $FF: synthetic method
      ApplicationTab(Object var1) {
         this();
      }
   }
}
