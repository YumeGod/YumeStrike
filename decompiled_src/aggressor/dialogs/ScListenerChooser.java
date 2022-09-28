package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.SelectOnChange;
import common.AObject;
import common.Callback;
import common.ListenerUtils;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import ui.ATable;
import ui.GenericTableModel;

public class ScListenerChooser extends AObject implements Callback, ActionListener {
   public static final int CHOOSE_ALL = 0;
   public static final int CHOOSE_STAGERS = 1;
   protected JFrame dialog = null;
   protected SafeDialogCallback callback = null;
   protected AggressorClient client = null;
   protected int behavior = 0;
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"name", "payload", "host", "port"};

   public static ScListenerChooser ListenersAll(AggressorClient var0, SafeDialogCallback var1) {
      return new ScListenerChooser(var0, var1, 0);
   }

   public static ScListenerChooser ListenersWithStagers(AggressorClient var0, SafeDialogCallback var1) {
      return new ScListenerChooser(var0, var1, 1);
   }

   protected ScListenerChooser(AggressorClient var1, SafeDialogCallback var2, int var3) {
      this.client = var1;
      this.behavior = var3;
      this.callback = var2;
      if (var3 == 0) {
         this.model = DialogUtils.setupModel("name", this.cols, ListenerUtils.getAllListeners(var1));
      } else {
         this.model = DialogUtils.setupModel("name", this.cols, ListenerUtils.getListenersWithStagers(var1));
      }

      var1.getData().subscribe("listeners", this);
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Add".equals(var1.getActionCommand())) {
         ScListenerDialog var2 = new ScListenerDialog(this.client);
         var2.setObserver(new SelectOnChange(this.client, this.table, this.model, "name"));
         var2.show();
      } else if ("Choose".equals(var1.getActionCommand())) {
         String var3 = (String)this.model.getSelectedValue(this.table);
         this.client.getData().unsub("listeners", this);
         this.dialog.setVisible(false);
         this.dialog.dispose();
         if (var3 != null) {
            this.callback.dialogResult(var3);
         }
      }

   }

   public void show() {
      this.dialog = DialogUtils.dialog(this.behavior == 0 ? "Choose a payload" : "Choose a payload to stage", 640, 240);
      this.dialog.setLayout(new BorderLayout());
      this.dialog.addWindowListener(this.client.getData().unsubOnClose("listeners", this));
      this.table = DialogUtils.setupTable(this.model, this.cols, false);
      DialogUtils.setTableColumnWidths(this.table, DialogUtils.toMap("name: 125, payload: 250, host: 125, port: 60"));
      JButton var1 = new JButton("Choose");
      JButton var2 = new JButton("Add");
      JButton var3 = new JButton("Help");
      var1.addActionListener(this);
      var2.addActionListener(this);
      var3.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-listener-management"));
      this.dialog.add(DialogUtils.FilterAndScroll(this.table), "Center");
      this.dialog.add(DialogUtils.center(var1, var2, var3), "South");
      this.dialog.setVisible(true);
      this.dialog.show();
   }

   public void result(String var1, Object var2) {
      if (this.behavior == 0) {
         DialogUtils.setTable(this.table, this.model, ListenerUtils.getAllListeners(this.client));
      } else {
         DialogUtils.setTable(this.table, this.model, ListenerUtils.getListenersWithStagers(this.client));
      }

   }
}
