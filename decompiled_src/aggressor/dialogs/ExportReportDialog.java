package aggressor.dialogs;

import aggressor.AggressorClient;
import common.CommonUtils;
import data.DataAggregate;
import data.FieldSorter;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import report.Document;
import sleep.runtime.SleepUtils;
import ui.Sorters;

public class ExportReportDialog implements DialogListener, SafeDialogCallback, Runnable {
   protected AggressorClient client;
   protected String report;
   protected Map options;
   protected String file;

   public ExportReportDialog(AggressorClient var1, String var2) {
      this.client = var1;
      this.report = var2;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.options = var2;
      String var3 = DialogUtils.string(var2, "output");
      String var4 = CommonUtils.strrep(this.report.toLowerCase(), " ", "");
      var4 = CommonUtils.strrep(var4, "&", "_");
      var4 = CommonUtils.strrep(var4, ",", "");
      if ("PDF".equals(var3)) {
         var4 = var4 + ".pdf";
      } else if ("MS Word".equals(var3)) {
         var4 = var4 + ".docx";
      }

      SafeDialogs.saveFile((JFrame)null, var4, this);
   }

   public void dialogResult(String var1) {
      this.file = var1;
      (new Thread(this, "export " + var1)).start();
   }

   public void sort(Map var1, String var2, String var3, Comparator var4) {
      List var5 = (List)var1.get(var2);
      if (var5 == null) {
         CommonUtils.print_error("Model '" + var2 + "' doesn't exist. Can't sort by: '" + var3 + "'");
         Thread.currentThread();
         Thread.dumpStack();
      } else {
         Collections.sort(var5, new FieldSorter(var3, var4));
      }

   }

   public void mask(Map var1, String var2, String var3) {
      List var4 = (List)var1.get(var2);
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Map var6 = (Map)var5.next();
         String var7 = DialogUtils.string(var6, var3);
         if (var7.length() == 32) {
            var6.put(var3, var7.replaceAll(".", "*"));
         } else {
            var6.put(var3, "********");
         }
      }

   }

   public void maskemail(Map var1, String var2, String var3) {
      List var4 = (List)var1.get(var2);
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Map var6 = (Map)var5.next();
         String var7 = DialogUtils.string(var6, var3);
         if (var7 != null) {
            String[] var8 = var7.split("@");
            var6.put(var3, CommonUtils.garbage(var8[0]) + "@" + var8[1]);
         }
      }

   }

   public void run() {
      String var1 = DialogUtils.string(this.options, "output");
      String var2 = DialogUtils.string(this.options, "short");
      String var3 = DialogUtils.string(this.options, "long");
      String var4 = DialogUtils.string(this.options, "description");
      boolean var5 = DialogUtils.bool(this.options, "mask");
      ProgressMonitor var6 = new ProgressMonitor(this.client, "Export Report", "Starting...", 0, 5);
      var6.setNote("Aggregate data...");
      Map var7 = DataAggregate.AllModels(this.client);
      var6.setProgress(1);
      var6.setNote("Sort targets");
      this.sort(var7, "targets", "address", Sorters.getHostSorter());
      var6.setNote("Sort services");
      this.sort(var7, "services", "port", Sorters.getNumberSorter());
      var6.setNote("Sort credentials");
      this.sort(var7, "credentials", "password", Sorters.getStringSorter());
      this.sort(var7, "credentials", "realm", Sorters.getStringSorter());
      this.sort(var7, "credentials", "user", Sorters.getNumberSorter());
      var6.setNote("Sort applications");
      this.sort(var7, "applications", "application", Sorters.getStringSorter());
      var6.setNote("Sort sessions");
      this.sort(var7, "sessions", "opened", Sorters.getNumberSorter());
      var6.setNote("Sort archives");
      this.sort(var7, "archives", "when", Sorters.getNumberSorter());
      var6.setProgress(2);
      if (var5) {
         this.mask(var7, "credentials", "password");
         this.maskemail(var7, "tokens", "email");
      }

      Stack var8 = new Stack();
      var8.push(SleepUtils.getScalar((Object)var7));
      var8.push(CommonUtils.convertAll(var7));
      var8.push(CommonUtils.convertAll(this.options));
      var6.setNote("Build document...");
      Document var9 = this.client.getReportEngine().buildReport(this.report, var2, var8);
      var6.setProgress(3);
      var6.setNote("Export document...");
      if ("PDF".equals(var1)) {
         var9.toPDF(new File(this.file));
      } else if ("MS Word".equals(var1)) {
         var9.toWord(new File(this.file));
      }

      var6.setProgress(4);
      var6.close();
      DialogUtils.showInfo("Report " + this.file + " saved");
   }

   public void show() {
      JFrame var1 = DialogUtils.dialog("Export Report", 640, 480);
      DialogManager var2 = new DialogManager(var1);
      var2.addDialogListener(this);
      var2.set("output", "PDF");
      var2.set("short", this.report);
      var2.set("long", this.report);
      var2.set("description", this.client.getReportEngine().describe(this.report));
      var2.text("short", "Short Title:", 20);
      var2.text("long", "Long Title:", 20);
      var2.text_big("description", "Description:");
      var2.combobox("output", "Output:", CommonUtils.toArray("MS Word, PDF"));
      JComponent var3 = var2.layout();
      JCheckBox var4 = var2.checkbox("mask", "Mask email addresses and passwords");
      JButton var5 = var2.action("Export");
      JButton var6 = var2.help("https://www.cobaltstrike.com/help-reporting");
      var1.add(DialogUtils.stack(var3, var4), "Center");
      var1.add(DialogUtils.center(var5, var6), "South");
      var1.pack();
      var1.setVisible(true);
   }
}
