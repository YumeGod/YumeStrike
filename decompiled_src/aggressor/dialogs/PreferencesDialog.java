package aggressor.dialogs;

import aggressor.Prefs;
import common.AObject;
import common.CommonUtils;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import ui.Navigator;

public class PreferencesDialog extends AObject implements DialogListener {
   protected JFrame dialog = null;
   protected DialogManager controller = null;
   protected Navigator options = null;

   public void dialogAction(ActionEvent var1, Map var2) {
      String var3 = var1.getActionCommand();
      if (var3.equals("Save")) {
         Prefs.getPreferences().update(var2);
         Prefs.getPreferences().save();
         DialogUtils.showInfo("Saved preferences");
      }

   }

   public void consolePreferences() {
      this.controller.startGroup("console");
      this.controller.font("console.font.font", "Font:");
      this.controller.color("console.foreground.color", "Foreground:");
      this.controller.color("console.background.color", "Background:");
      this.controller.color("console.highlight.color", "Highlight:");
      this.controller.endGroup();
      this.options.addPage("Console", DialogUtils.getIcon("resources/cc/black/png/monitor_icon&16.png"), "These options control the look of Cobalt Strike's consoles.", this.controller.layout("console"));
   }

   public void statusbarPreferences() {
      this.controller.startGroup("statusbar");
      this.controller.color("statusbar.foreground.color", "Foreground:");
      this.controller.color("statusbar.background.color", "Background:");
      this.controller.endGroup();
      this.options.addPage("Statusbar", DialogUtils.getIcon("resources/cc/black/png/monitor_icon&16.png"), "These options control the look of the statusbars within Cobalt Strike's consoles.", this.controller.layout("statusbar"));
   }

   public void cobaltstrikePreferences() {
      this.controller.startGroup("cobalt");
      this.controller.color("tab.highlight.color", "Tab Activity:");
      this.controller.checkbox_add("client.toolbar.boolean", "Toolbar:", "Show toolbar in main window");
      this.controller.text("client.vncports.string", "VNC Ports:");
      this.controller.font("client.font.font", "GUI Font:");
      this.controller.label("Restart Cobalt Strike for the GUI Font change to take effect");
      this.controller.endGroup();
      this.options.addPage("Cobalt Strike", DialogUtils.getIcon("resources/cc/black/png/wrench_icon&16.png"), "Options to configure Cobalt Strike.", this.controller.layout("cobalt"));
   }

   public void graphPreferences() {
      this.controller.startGroup("graph");
      this.controller.font("graph.font.font", "Font:");
      this.controller.color("graph.foreground.color", "Foreground:");
      this.controller.color("graph.background.color", "Background:");
      this.controller.color("graph.selection.color", "Selection:");
      this.controller.combobox("graph.default_layout.layout", "Auto Layout:", CommonUtils.toArray("circle, none, stack, tree-bottom, tree-left, tree-right, tree-top"));
      this.controller.endGroup();
      this.options.addPage("Graph", DialogUtils.getIcon("resources/cc/black/png/chart_line_icon&16.png"), "These options control the look of Cobalt Strike's graph views.", this.controller.layout("graph"));
   }

   public void reportPreferences() {
      this.controller.startGroup("report");
      this.controller.color("reporting.accent.color", "Accent Color:");
      this.controller.file("reporting.header_image.file", "Logo:");
      this.controller.list_file("reporting.custom_reports", "Reports:");
      this.controller.endGroup();
      this.options.addPage("Reporting", DialogUtils.getIcon("resources/cc/black/png/document_icon&16.png"), "These options allow you to customize Cobalt Strike's report template and load custom reports.", this.controller.layout("report"));
   }

   public void fingerprintPreferences() {
      this.controller.startGroup("fingerprints");
      this.controller.list_text("trusted.servers", "Fingerprints:");
      this.controller.endGroup();
      this.options.addPage("Fingerprints", DialogUtils.getIcon("resources/cc/black/png/cert_icon&16.png"), "This panel is a list of team server SSL cert SHA-1 hashes. You may remove trusted hashes here.", this.controller.layout("fingerprints"));
   }

   public void profilePreferences() {
      this.controller.startGroup("profiles");
      this.controller.list_text("connection.profiles", "Profiles:");
      this.controller.endGroup();
      this.options.addPage("Team Servers", DialogUtils.getIcon("resources/cc/black/png/sq_plus_icon&16.png"), "This panel is a list of saved connection profiles. You may remove stored profiles here.", this.controller.layout("profiles"));
   }

   public void show() {
      this.dialog = DialogUtils.dialog("Preferences", 320, 240);
      this.dialog.setLayout(new BorderLayout());
      this.controller = new DialogManager(this.dialog);
      this.controller.set(Prefs.getPreferences().copy());
      this.controller.addDialogListener(this);
      this.options = new Navigator();
      this.cobaltstrikePreferences();
      this.consolePreferences();
      this.fingerprintPreferences();
      this.graphPreferences();
      this.reportPreferences();
      this.statusbarPreferences();
      this.profilePreferences();
      JButton var1 = this.controller.action_noclose("Save");
      this.dialog.add(this.options, "Center");
      this.dialog.add(DialogUtils.center((JComponent)var1), "South");
      this.dialog.pack();
      this.dialog.setVisible(true);
      this.dialog.show();
   }
}
