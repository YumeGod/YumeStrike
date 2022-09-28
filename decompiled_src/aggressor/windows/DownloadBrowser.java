package aggressor.windows;

import aggressor.DataManager;
import common.AObject;
import common.AdjustData;
import common.Download;
import common.DownloadFiles;
import common.TeamQueue;
import cortana.Cortana;
import dialog.ActivityPanel;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import ui.ATable;
import ui.GenericTableModel;

public class DownloadBrowser extends AObject implements AdjustData, ActionListener, SafeDialogCallback {
   protected TeamQueue conn = null;
   protected Cortana engine = null;
   protected DataManager data = null;
   protected ActivityPanel dialog = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"host", "name", "path", "size", "date"};

   public DownloadBrowser(DataManager var1, Cortana var2, TeamQueue var3) {
      this.engine = var2;
      this.conn = var3;
      this.data = var1;
   }

   public ActionListener cleanup() {
      return this.data.unsubOnClose("downloads", this);
   }

   public void actionPerformed(ActionEvent var1) {
      SafeDialogs.openFile("Sync downloads to?", (String)null, (String)null, false, true, this);
   }

   public void dialogResult(String var1) {
      if (var1 != null) {
         (new DownloadFiles(this.conn, this.model.getSelectedRows(this.table), new File(var1))).startNextDownload();
      }
   }

   public JComponent getContent() {
      LinkedList var1 = this.data.populateAndSubscribe("downloads", this);
      this.model = DialogUtils.setupModel("lpath", this.cols, var1);
      this.dialog = new ActivityPanel();
      this.dialog.setLayout(new BorderLayout());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      DialogUtils.setupDateRenderer(this.table, "date");
      DialogUtils.setupSizeRenderer(this.table, "size");
      JButton var2 = new JButton("Sync Files");
      JButton var3 = new JButton("Help");
      var2.addActionListener(this);
      var3.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-manage-downloads"));
      this.dialog.add(DialogUtils.FilterAndScroll(this.table), "Center");
      this.dialog.add(DialogUtils.center(var2, var3), "South");
      return this.dialog;
   }

   public Map format(String var1, Object var2) {
      Download var3 = (Download)var2;
      return var3.toMap();
   }

   public void result(String var1, Object var2) {
      DialogUtils.addToTable(this.table, this.model, this.format(var1, var2));
      this.dialog.touch();
   }
}
