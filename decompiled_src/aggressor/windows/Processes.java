package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.AObject;
import common.Callback;
import common.CommonUtils;
import common.MudgeSanity;
import common.ScriptUtils;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import table.FilterAndScroll;
import ui.ATable;
import ui.GenericTableModel;
import ui.TableClickListener;
import ui.TablePopup;

public class Processes extends AObject implements Callback, TablePopup, TableModelListener {
   protected String bid = "";
   protected AggressorClient client = null;
   protected String mypid = "";
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected JTree tree = null;
   protected String[] cols = new String[]{"PID", "PPID", "Name", "Arch", "Session", "User"};
   protected JSplitPane split = new JSplitPane(1);
   Icon ic_default = DialogUtils.getIcon("resources/cc/black/png/app_window&16.png");
   Icon ic_shell = DialogUtils.getIcon("resources/cc/black/png/app_window_shell_icon&16.png");
   Icon ic_explorer = DialogUtils.getIcon("resources/cc/black/png/globe_3_icon&16.png");
   Icon ic_printer = DialogUtils.getIcon("resources/cc/black/png/print_icon&16.png");
   Icon ic_lsass = DialogUtils.getIcon("resources/cc/black/png/vault_icon&16.png");
   Icon ic_winlogon = DialogUtils.getIcon("resources/cc/black/png/key_icon&16.png");
   Icon ic_browser = DialogUtils.getIcon("resources/cc/black/png/browser_icon&16.png");
   Icon ic_java = DialogUtils.getIcon("resources/cc/black/png/coffe_cup_icon&16.png");
   Icon ic_putty = DialogUtils.getIcon("resources/cc/black/png/net_comp_icon&16.png");
   Icon ic_services = DialogUtils.getIcon("resources/cc/black/png/cogs_icon&16.png");

   public Processes(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
      this.mypid = DataUtils.getBeaconPid(var1.getData(), var2);
      this.model = DialogUtils.setupModel("PID", this.cols, new LinkedList());
   }

   public void refresh() {
      this.client.getConnection().call("beacons.task_ps", CommonUtils.args(this.bid), this);
   }

   public void showPopup(MouseEvent var1) {
      Stack var2 = new Stack();
      var2.push(ScriptUtils.convertAll(this));
      var2.push(ScriptUtils.convertAll(this.model.getSelectedRows(this.table)));
      var2.push(ScriptUtils.convertAll(this.bid));
      this.client.getScriptEngine().getMenuBuilder().installMenu(var1, "processbrowser", var2);
   }

   public JComponent setup() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      DialogUtils.setTableColumnWidths(this.table, DialogUtils.toMap("PID: 60, PPID: 60, Name: 180, Arch: 60, Session: 60, User: 180"));
      this.table.setPopupMenu(this);
      this.tree = new JTree(new DefaultMutableTreeNode());
      this.tree.setRootVisible(false);
      this.tree.setCellRenderer(this.getNewRenderer());
      this.tree.setScrollsOnExpand(true);
      this.tree.setPreferredSize((Dimension)null);
      this.tree.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent var1) {
            Processes.this.doMouseClicked(var1);
         }
      });
      TableClickListener var2 = new TableClickListener();
      var2.setPopup(this);
      this.tree.addMouseListener(var2);
      this.model.addTableModelListener(this);
      this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent var1) {
            Processes.this.tree.repaint();
         }
      });
      this.split.add(new JScrollPane(this.tree));
      this.split.add(new FilterAndScroll(this.table, var1));
      this.split.setDividerLocation(320);
      var1.add(this.split, "Center");
      this.refresh();
      return var1;
   }

   public void doMouseClicked(MouseEvent var1) {
      TreePath var2 = this.tree.getPathForLocation(var1.getX(), var1.getY());
      if (var2 != null) {
         DefaultMutableTreeNode var3 = (DefaultMutableTreeNode)var2.getLastPathComponent();
         if (var3 != null) {
            ProcessNode var4 = (ProcessNode)var3.getUserObject();
            if (var4 != null) {
               this.model.activateRow(this.table, var4.row);
            }
         }
      }
   }

   public TreeCellRenderer getNewRenderer() {
      DefaultTreeCellRenderer var1 = new DefaultTreeCellRenderer() {
         public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
            DefaultMutableTreeNode var8 = (DefaultMutableTreeNode)var2;
            ProcessNode var9 = (ProcessNode)var8.getUserObject();
            if (var9 == null) {
               super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, false);
               return this;
            } else {
               var3 = Processes.this.model.isSelected(Processes.this.table, var9.row);
               super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, false);
               this.setIcon(var9.icon);
               if (var3) {
                  this.setForeground(Color.WHITE);
               } else {
                  this.setForeground(Color.BLACK);
               }

               if (var9.mine) {
                  if (var3) {
                     this.setForeground(Color.YELLOW);
                  } else {
                     this.setBackground(Color.YELLOW);
                  }
               } else {
                  this.setBackground((Color)null);
               }

               if (var9.visible && var9.admin) {
                  if (var3 && var9.mine) {
                     this.setForeground(Color.ORANGE);
                  } else if (var3) {
                     this.setForeground(Color.CYAN);
                  } else if (var9.mine) {
                     this.setBackground(Color.ORANGE);
                  } else {
                     this.setForeground(Color.BLUE);
                  }
               }

               this.setEnabled(var9.visible);
               return this;
            }
         }
      };
      var1.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      return var1;
   }

   public ProcessNode getNode(int var1, String var2, String var3, String var4) {
      ProcessNode var5 = new ProcessNode();
      var5.row = var1;
      var5.pid = var2;
      var5.desc = var3;
      var5.mine = var2.equals(this.mypid);
      var5.admin = var4.endsWith(" *");
      var5.visible = var4.length() > 0;
      if (!var3.equals("cmd.exe") && !var3.equals("powershell.exe")) {
         if (!var3.equals("firefox.exe") && !var3.equals("iexplore.exe") && !var3.equals("chrome.exe") && !var3.equals("MicrosoftEdgeCP.exe")) {
            if (var3.equals("explorer.exe")) {
               var5.icon = this.ic_explorer;
            } else if (var3.equals("spoolsv.exe")) {
               var5.icon = this.ic_printer;
            } else if (var3.equals("lsass.exe")) {
               var5.icon = this.ic_lsass;
            } else if (!var3.equals("jusched.exe") && !var3.equals("java.exe") && !var3.equals("javaw.exe")) {
               if (var3.equals("winlogon.exe")) {
                  var5.icon = this.ic_winlogon;
               } else if (var3.equals("putty.exe")) {
                  var5.icon = this.ic_putty;
               } else if (var3.equals("services.exe")) {
                  var5.icon = this.ic_services;
               } else {
                  var5.icon = this.ic_default;
               }
            } else {
               var5.icon = this.ic_java;
            }
         } else {
            var5.icon = this.ic_browser;
         }
      } else {
         var5.icon = this.ic_shell;
      }

      return var5;
   }

   public void safeAdd(DefaultMutableTreeNode var1, DefaultMutableTreeNode var2, DefaultMutableTreeNode var3) {
      try {
         var1.add(var2);
      } catch (Exception var5) {
         MudgeSanity.logException("Could not add: " + var2 + " to " + var1, var5, false);
         if (var3 != null) {
            this.safeAdd(var3, var2, (DefaultMutableTreeNode)null);
         }

      }
   }

   public void tableChanged(TableModelEvent var1) {
      List var2 = this.model.getRows();
      DefaultMutableTreeNode var3 = new DefaultMutableTreeNode();
      HashMap var4 = new HashMap();
      int var5 = 0;

      Iterator var6;
      Map var7;
      String var8;
      String var9;
      DefaultMutableTreeNode var11;
      for(var6 = var2.iterator(); var6.hasNext(); ++var5) {
         var7 = (Map)var6.next();
         var8 = DialogUtils.string(var7, "Name");
         var9 = DialogUtils.string(var7, "PID");
         String var10 = DialogUtils.string(var7, "User");
         var11 = new DefaultMutableTreeNode(this.getNode(var5, var9, var8, var10), true);
         var4.put(var9, var11);
         this.safeAdd(var3, var11, (DefaultMutableTreeNode)null);
      }

      var6 = var2.iterator();

      while(var6.hasNext()) {
         var7 = (Map)var6.next();
         var8 = DialogUtils.string(var7, "PID");
         var9 = DialogUtils.string(var7, "PPID");
         DefaultMutableTreeNode var14 = (DefaultMutableTreeNode)var4.get(var9);
         var11 = (DefaultMutableTreeNode)var4.get(var8);
         if (var14 != var11 && var14 != null && var11 != null) {
            this.safeAdd(var14, var11, var3);
         }
      }

      ((DefaultTreeModel)this.tree.getModel()).setRoot(var3);
      int var12 = var2.size() + 1;

      for(int var13 = 0; var13 < var12; ++var13) {
         this.tree.expandRow(var13);
      }

   }

   public void result(String var1, Object var2) {
      LinkedList var3 = new LinkedList();
      String[] var4 = var2.toString().trim().split("\n");

      for(int var5 = 0; var5 < var4.length; ++var5) {
         String[] var6 = var4[var5].split("\t");
         HashMap var7 = new HashMap();
         if (var6.length >= 1) {
            var7.put("Name", var6[0]);
         }

         if (var6.length >= 2) {
            var7.put("PPID", var6[1]);
         }

         if (var6.length >= 3) {
            var7.put("PID", var6[2]);
         }

         if (var6.length >= 4) {
            var7.put("Arch", var6[3]);
         }

         if (var6.length >= 5) {
            var7.put("User", var6[4]);
         }

         if (var6.length >= 6) {
            var7.put("Session", var6[5]);
         }

         var3.add(var7);
      }

      DialogUtils.setTable(this.table, this.model, var3);
   }

   public GenericTableModel getModel() {
      return this.model;
   }

   public Object[] getSelectedPIDs() {
      return this.model.getSelectedValues(this.table);
   }

   public Object[][] getSelectedValuesFromColumns(String[] var1) {
      return this.model.getSelectedValuesFromColumns(this.table, var1);
   }

   private static class ProcessNode {
      public int row;
      public String desc;
      public String pid;
      public Icon icon;
      public boolean mine;
      public boolean admin;
      public boolean visible;

      private ProcessNode() {
      }

      public String toString() {
         return this.pid + ": " + this.desc;
      }

      // $FF: synthetic method
      ProcessNode(Object var1) {
         this();
      }
   }
}
