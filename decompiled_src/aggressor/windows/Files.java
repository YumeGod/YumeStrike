package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.TaskBeacon;
import common.AObject;
import common.Callback;
import common.CommonUtils;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreeModel;
import sleep.runtime.SleepUtils;
import ui.ATable;
import ui.ATextField;
import ui.DoubleClickListener;
import ui.DoubleClickWatch;
import ui.FileBrowserNode;
import ui.FileBrowserNodes;
import ui.GenericTableModel;
import ui.Sorters;
import ui.TablePopup;

public class Files extends AObject implements Callback, ActionListener, TablePopup, DoubleClickListener {
   protected String bid = "";
   protected AggressorClient client = null;
   protected JTree tree = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"D", "Name", "Size", "Modified"};
   protected ATextField folder = null;
   protected JButton up = null;
   protected FileBrowserNodes nodes = new FileBrowserNodes();

   public void setTree(JTree var1) {
      this.tree = var1;
   }

   public Files(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
   }

   public void gotof(String var1) {
      FileBrowserNode var2 = this.getNodes().getNodeFromCache(var1);
      if (var2 != null) {
         this.gotof(var2);
      } else {
         this.ls(var1);
      }

   }

   public void clearSelection() {
      this.getNodes().setSelected((FileBrowserNode)null);
      this.tree.repaint();
   }

   public void gotof(FileBrowserNode var1) {
      this.getNodes().setSelected(var1);
      if (var1.hasCache()) {
         this.result((String)null, var1.getCache());
      } else {
         this.ls(var1.getPath());
      }

      this.tree.repaint();
   }

   public void ls(String var1) {
      DialogUtils.setText(this.folder, var1);
      DialogUtils.setTable(this.table, this.model, new LinkedList());
      this.ls_refresh(var1);
   }

   public void ls_refresh(String var1) {
      String var2 = CommonUtils.bString(DataUtils.encodeForBeacon(this.client.getData(), this.bid, var1));
      this.client.getConnection().call("beacons.task_ls", CommonUtils.args(this.bid, var2), this);
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      String var3;
      if (var1.getSource() == this.folder) {
         var3 = this.folder.getText().trim();
         if (!"".equals(var3)) {
            this.clearSelection();
            this.gotof(var3);
         }
      } else if (var1.getSource() == this.up && !this.nodes.isSelected((FileBrowserNode)null)) {
         var3 = this.nodes.getSelected().getParent();
         if (!"".equals(var3)) {
            this.gotof(var3);
         }
      } else if ("Upload...".equals(var2)) {
         SafeDialogs.openFile("Upload...", (String)null, (String)null, false, false, new SafeDialogCallback() {
            public void dialogResult(String var1) {
               String var2 = Files.this.nodes.getSelected().getChild((new File(var1)).getName());
               TaskBeacon var3 = new TaskBeacon(Files.this.client, Files.this.client.getData(), Files.this.client.getConnection(), new String[]{Files.this.bid});
               var3.input("upload " + var1 + " (" + var2 + ")");
               var3.Upload(var1, var2);
               Files.this.ls_refresh(Files.this.nodes.getSelected().getPath());
            }
         });
      } else if ("Make Directory".equals(var2)) {
         SafeDialogs.ask("Which folder?", "", new SafeDialogCallback() {
            public void dialogResult(String var1) {
               String var2 = Files.this.nodes.getSelected().getChild(var1);
               TaskBeacon var3 = new TaskBeacon(Files.this.client, Files.this.client.getData(), Files.this.client.getConnection(), new String[]{Files.this.bid});
               var3.input("mkdir " + var2);
               var3.MkDir(var2);
               Files.this.ls_refresh(Files.this.nodes.getSelected().getPath());
            }
         });
      } else if ("List Drives".equals(var2)) {
         DialogUtils.setText(this.folder, "");
         this.clearSelection();
         this.client.getConnection().call("beacons.task_drives", CommonUtils.args(this.bid), new Callback() {
            public void result(String var1, Object var2) {
               String[] var3 = CommonUtils.toArray(CommonUtils.drives(var2 + ""));
               LinkedList var4 = new LinkedList();

               for(int var5 = 0; var5 < var3.length; ++var5) {
                  HashMap var6 = new HashMap();
                  var6.put("D", "drive");
                  var6.put("Name", var3[var5]);
                  var4.add(var6);
                  Files.this.nodes.getNode(var3[var5]);
               }

               DialogUtils.setTable(Files.this.table, Files.this.model, var4);
               Files.this.nodes.refresh(Files.this.tree);
            }
         });
      } else if ("Refresh".equals(var2)) {
         this.ls_refresh(this.nodes.getSelected().getPath());
      }

   }

   public void doubleClicked(MouseEvent var1) {
      String var2 = (String)this.model.getSelectedValue(this.table);
      String var3 = (String)this.model.getSelectedValueFromColumn(this.table, "D");
      if (var3.equals("dir")) {
         String var4 = this.nodes.getSelected().getChild(var2);
         this.gotof(var4);
      } else if (var3.equals("drive")) {
         this.gotof(var2);
      }

   }

   public void showPopup(MouseEvent var1) {
      if (!this.nodes.isSelected((FileBrowserNode)null)) {
         Stack var2 = new Stack();
         var2.push(SleepUtils.getScalar((Object)this));
         var2.push(CommonUtils.toSleepArray(this.model.getSelectedValues(this.table)));
         var2.push(SleepUtils.getScalar(this.nodes.getSelected().getPathNoTrailingSlash()));
         var2.push(SleepUtils.getScalar(this.bid));
         this.client.getScriptEngine().getMenuBuilder().installMenu(var1, "filebrowser", var2);
      }
   }

   public JComponent getButtons() {
      JButton var1 = DialogUtils.Button("Upload...", this);
      JButton var2 = DialogUtils.Button("Make Directory", this);
      JButton var3 = DialogUtils.Button("List Drives", this);
      JButton var4 = DialogUtils.Button("Refresh", this);
      JButton var5 = DialogUtils.Button("Help", this);
      var5.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-file-browser"));
      return DialogUtils.center(var1, var2, var3, var4, var5);
   }

   public JComponent getContent() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      this.model = DialogUtils.setupModel("Name", this.cols, new LinkedList());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      this.table.getColumn("D").setMaxWidth(38);
      this.table.getColumn("Size").setCellRenderer(ATable.getSizeTableRenderer());
      this.table.getColumn("D").setCellRenderer(ATable.getFileTypeTableRenderer(this.model));
      this.table.getColumn("Name").setCellRenderer(ATable.getSimpleTableRenderer());
      TableRowSorter var2 = new TableRowSorter(this.model);
      var2.toggleSortOrder(0);
      this.table.setRowSorter(var2);
      var2.setComparator(2, Sorters.getNumberSorter());
      var2.setComparator(3, Sorters.getDateSorter("MM/dd/yyyy HH:mm:ss"));
      this.table.setPopupMenu(this);
      this.table.addMouseListener(new DoubleClickWatch(this));
      this.folder = new ATextField("", 80);
      this.folder.addActionListener(this);
      FileSystemView var3 = FileSystemView.getFileSystemView();
      Icon var4 = var3.getSystemIcon(var3.getDefaultDirectory());
      this.up = new JButton(var4);
      this.up.addActionListener(this);
      JPanel var5 = new JPanel();
      var5.setLayout(new BorderLayout());
      var5.add(this.folder, "Center");
      var5.add(DialogUtils.pad(this.up, 0, 0, 0, 4), "West");
      var1.add(DialogUtils.pad(var5, 3, 3, 3, 3), "North");
      var1.add(DialogUtils.FilterAndScroll(this.table), "Center");
      return var1;
   }

   public TreeModel getTreeModel() {
      return this.nodes.getModel();
   }

   public FileBrowserNodes getNodes() {
      return this.nodes;
   }

   public boolean updateTreeModel(String var1, String var2, LinkedList var3) {
      FileBrowserNode var4 = this.nodes.getNode(var1);
      var4.setCache(var2);
      if (this.nodes.isSelected((FileBrowserNode)null)) {
         this.nodes.setSelected(var4);
      }

      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         Map var6 = (Map)var5.next();
         if ("dir".equals(var6.get("D"))) {
            FileBrowserNode var7 = this.nodes.addNode(var1, (String)var6.get("Name"));
            if (var7.hasCache()) {
               var6.put("cache", Boolean.TRUE);
            } else {
               var6.put("cache", Boolean.FALSE);
            }
         }
      }

      this.nodes.refresh(this.tree);
      return this.nodes.isSelected(var4);
   }

   public void result(String var1, Object var2) {
      LinkedList var3 = new LinkedList();
      String[] var4 = var2.toString().trim().split("\n");
      String var5 = var4[0].substring(0, var4[0].length() - 2);

      for(int var6 = 1; var6 < var4.length; ++var6) {
         String[] var7 = var4[var6].split("\t");
         HashMap var8 = new HashMap();
         if (var7[0].equals("D") && !".".equals(var7[3]) && !"..".equals(var7[3])) {
            var8.put("D", "dir");
            var8.put("Modified", var7[2]);
            var8.put("Name", var7[3]);
            var3.add(var8);
         } else if (var7[0].equals("F")) {
            var8.put("D", "fil");
            var8.put("Size", var7[1]);
            var8.put("Modified", var7[2]);
            var8.put("Name", var7[3]);
            var3.add(var8);
         }
      }

      if (this.updateTreeModel(var5, var2.toString(), var3)) {
         DialogUtils.setText(this.folder, var5);
         DialogUtils.setTable(this.table, this.model, var3);
      }

   }
}
