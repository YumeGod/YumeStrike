package aggressor.windows;

import aggressor.Aggressor;
import aggressor.AggressorClient;
import aggressor.Prefs;
import common.AObject;
import common.CommonUtils;
import common.MudgeSanity;
import cortana.Cortana;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import sleep.error.YourCodeSucksException;
import ui.ATable;
import ui.GenericTableModel;

public class ScriptManager extends AObject implements ActionListener, SafeDialogCallback {
   protected GenericTableModel model = null;
   protected ATable table = null;
   protected String[] cols = new String[]{"path", "ready"};
   protected AggressorClient client = null;

   public ScriptManager(AggressorClient var1) {
      this.client = var1;
      this.model = DialogUtils.setupModel("path", this.cols, this.toModel());
   }

   public void actionPerformed(ActionEvent var1) {
      if ("Load".equals(var1.getActionCommand())) {
         SafeDialogs.openFile("Load a script", (String)null, (String)null, false, false, this);
      } else {
         String var2;
         Iterator var3;
         Cortana var4;
         if ("Unload".equals(var1.getActionCommand())) {
            var2 = this.model.getSelectedValue(this.table) + "";
            var3 = Aggressor.getFrame().getScriptEngines().iterator();

            while(var3.hasNext()) {
               var4 = (Cortana)var3.next();
               var4.unloadScript(var2);
            }

            List var8 = Prefs.getPreferences().getList("cortana.scripts");
            var8.remove(var2);
            Prefs.getPreferences().setList("cortana.scripts", var8);
            Prefs.getPreferences().save();
            this.refresh();
         } else if ("Reload".equals(var1.getActionCommand())) {
            var2 = this.model.getSelectedValue(this.table) + "";

            try {
               this.client.getScriptEngine().unloadScript(var2);
               this.client.getScriptEngine().loadScript(var2);
               DialogUtils.showInfo("Reloaded " + var2);
            } catch (YourCodeSucksException var5) {
               MudgeSanity.logException("Load " + var2, var5, true);
               DialogUtils.showError("Could not load " + var2 + ":\n\n" + var5.formatErrors());
            } catch (Exception var6) {
               MudgeSanity.logException("Load " + var2, var6, false);
               DialogUtils.showError("Could not load " + var2 + "\n" + var6.getMessage());
            }

            try {
               var3 = Aggressor.getFrame().getOtherScriptEngines(this.client).iterator();

               while(var3.hasNext()) {
                  var4 = (Cortana)var3.next();
                  var4.unloadScript(var2);
                  var4.loadScript(var2);
               }
            } catch (Exception var7) {
               MudgeSanity.logException("Load " + var2, var7, false);
            }

            this.refresh();
         }
      }

   }

   public void dialogResult(String var1) {
      try {
         this.client.getScriptEngine().loadScript(var1);
         Iterator var2 = Aggressor.getFrame().getOtherScriptEngines(this.client).iterator();

         while(var2.hasNext()) {
            Cortana var3 = (Cortana)var2.next();
            var3.loadScript(var1);
         }

         List var6 = Prefs.getPreferences().getList("cortana.scripts");
         var6.add(var1);
         Prefs.getPreferences().setList("cortana.scripts", var6);
         Prefs.getPreferences().save();
         this.refresh();
      } catch (YourCodeSucksException var4) {
         MudgeSanity.logException("Load " + var1, var4, true);
         DialogUtils.showError("Could not load " + var1 + ":\n\n" + var4.formatErrors());
      } catch (Exception var5) {
         MudgeSanity.logException("Load " + var1, var5, false);
         DialogUtils.showError("Could not load " + var1 + "\n" + var5.getMessage());
      }

   }

   public void refresh() {
      DialogUtils.setTable(this.table, this.model, this.toModel());
   }

   public LinkedList toModel() {
      HashSet var1 = new HashSet(this.client.getScriptEngine().getScripts());
      Iterator var2 = Prefs.getPreferences().getList("cortana.scripts").iterator();
      LinkedList var3 = new LinkedList();

      while(var2.hasNext()) {
         String var4 = (String)var2.next();
         if (var1.contains((new File(var4)).getName())) {
            var3.add(CommonUtils.toMap("path", var4, "ready", "✓"));
         } else {
            var3.add(CommonUtils.toMap("path", var4, "ready", ""));
         }
      }

      return var3;
   }

   public JComponent getContent() {
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      this.table = DialogUtils.setupTable(this.model, this.cols, true);
      DialogUtils.setTableColumnWidths(this.table, DialogUtils.toMap("path: 240, ready: 64"));
      this.table.getColumn("ready").setPreferredWidth(64);
      this.table.getColumn("ready").setMaxWidth(64);
      JButton var2 = new JButton("Load");
      JButton var3 = new JButton("Unload");
      JButton var4 = new JButton("Reload");
      JButton var5 = new JButton("Help");
      var2.addActionListener(this);
      var3.addActionListener(this);
      var4.addActionListener(this);
      var5.addActionListener(DialogUtils.gotoURL("https://www.cobaltstrike.com/help-scripting"));
      var1.add(new JScrollPane(this.table), "Center");
      var1.add(DialogUtils.center(var2, var3, var4, var5), "South");
      return var1;
   }
}
