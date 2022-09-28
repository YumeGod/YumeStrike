package aggressor.dialogs;

import aggressor.DataManager;
import aggressor.DataUtils;
import aggressor.GenericDataManager;
import aggressor.GlobalDataManager;
import common.AObject;
import common.AdjustData;
import common.CommonUtils;
import common.TeamQueue;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import ui.ATable;
import ui.GenericTableModel;

public class SiteChooser extends AObject implements AdjustData, ActionListener {
   protected JFrame dialog = null;
   protected TeamQueue conn = null;
   protected SafeDialogCallback callback = null;
   protected GenericDataManager data = GlobalDataManager.getGlobalDataManager();
   protected DataManager datal = null;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"Host", "URI", "Port", "Type", "Description"};

   public SiteChooser(TeamQueue var1, DataManager var2, SafeDialogCallback var3) {
      this.conn = var1;
      this.callback = var3;
      this.datal = var2;
      this.model = DialogUtils.setupModel("URI", this.cols, CommonUtils.apply("sites", DataUtils.getSites(this.data), this));
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = (String)this.model.getSelectedValue(this.table);
      String var3 = (String)this.model.getSelectedValueFromColumn(this.table, "Port");
      String var4 = (String)this.model.getSelectedValueFromColumn(this.table, "Host");
      String var5 = (String)this.model.getSelectedValueFromColumn(this.table, "Proto");
      String var6 = var5 + var4 + ":" + var3 + var2;
      this.dialog.setVisible(false);
      this.dialog.dispose();
      this.callback.dialogResult(var6);
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Choose a site", 640, 240);
      this.dialog.setLayout(new BorderLayout());
      this.dialog.addWindowListener(this.data.unsubOnClose("sites", this));
      this.table = DialogUtils.setupTable(this.model, this.cols, false);
      DialogUtils.setTableColumnWidths(this.table, DialogUtils.toMap("Host: 125, URI: 125, Port: 60, Type: 60, Description: 250"));
      JButton var1 = new JButton("Choose");
      var1.addActionListener(this);
      this.dialog.add(DialogUtils.FilterAndScroll(this.table), "Center");
      this.dialog.add(DialogUtils.center((JComponent)var1), "South");
      this.dialog.setVisible(true);
      this.dialog.show();
   }

   public Map format(String var1, Object var2) {
      Map var3 = (Map)var2;
      if ("".equals(var3.get("Host"))) {
         return null;
      } else {
         return "beacon".equals(var3.get("Type")) ? null : var3;
      }
   }

   public void result(String var1, Object var2) {
   }
}
