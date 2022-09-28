package aggressor.dialogs;

import aggressor.AggressorClient;
import common.CommonUtils;
import common.ListenerUtils;
import common.ResourceUtils;
import common.ScListener;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class OfficeMacroDialog implements DialogListener, ActionListener {
   protected AggressorClient client;
   protected String macro;

   public OfficeMacroDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void macroDialog(byte[] var1) {
      JFrame var2 = DialogUtils.dialog("Macro Instructions", 640, 480);
      JLabel var3 = new JLabel();
      var3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      var3.setText(CommonUtils.bString(CommonUtils.readResource("resources/macro.html")));
      this.macro = CommonUtils.bString((new ResourceUtils(this.client)).buildMacro(var1));
      JButton var4 = new JButton("Copy Macro");
      var4.addActionListener(this);
      var2.add(var3, "Center");
      var2.add(DialogUtils.center((JComponent)var4), "South");
      var2.pack();
      var2.setVisible(true);
   }

   public void actionPerformed(ActionEvent var1) {
      DialogUtils.addToClipboard(this.macro);
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = DialogUtils.string(var2, "listener");
      ScListener var4 = ListenerUtils.getListener(this.client, var3);
      byte[] var5 = var4.getPayloadStager("x86");
      this.macroDialog(var5);
   }

   public void show() {
      JFrame var1 = DialogUtils.dialog("MS Office Macro", 640, 480);
      DialogManager var2 = new DialogManager(var1);
      var2.addDialogListener(this);
      var2.sc_listener_stagers("listener", "Listener:", this.client);
      JButton var3 = var2.action("Generate");
      JButton var4 = var2.help("https://www.cobaltstrike.com/help-office-macro-attack");
      var1.add(DialogUtils.description("This package generates a VBA macro that you may embed into a Microsoft Word or Excel document. This attack works in x86 and x64 Office on Windows."), "North");
      var1.add(var2.layout(), "Center");
      var1.add(DialogUtils.center(var3, var4), "South");
      var1.pack();
      var1.setVisible(true);
   }
}
