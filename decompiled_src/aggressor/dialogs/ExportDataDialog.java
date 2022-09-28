package aggressor.dialogs;

import aggressor.AggressorClient;
import common.CommonUtils;
import common.Keys;
import data.DataAggregate;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;

public class ExportDataDialog implements DialogListener, SafeDialogCallback, Runnable {
   protected AggressorClient client;
   protected String file;
   protected String output;

   public ExportDataDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void dialogAction(ActionEvent var1, Map var2) {
      this.output = DialogUtils.string(var2, "output");
      SafeDialogs.openFile("Save to...", (String)null, (String)null, false, true, this);
   }

   public void dialogResult(String var1) {
      this.file = var1;
      (new File(var1)).mkdirs();
      (new Thread(this, "export " + var1)).start();
   }

   public void dump(List var1, String var2, String[] var3) {
      if ("XML".equals(this.output)) {
         this.dumpXML(var1, var2, var3);
      } else {
         this.dumpTSV(var1, var2, var3);
      }

   }

   public void dumpXML(List var1, String var2, String[] var3) {
      StringBuffer var4 = new StringBuffer();
      var4.append("<" + var2 + ">\n");
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Map var6 = (Map)var5.next();
         var4.append("\t<entry>\n");

         for(int var7 = 0; var7 < var3.length; ++var7) {
            var4.append("\t\t\t<" + var3[var7] + ">");
            var4.append(DialogUtils.string(var6, var3[var7]));
            var4.append("</" + var3[var7] + ">\n");
         }

         var4.append("\t</entry>\n");
      }

      var4.append("</" + var2 + ">\n");
      CommonUtils.writeToFile(new File(this.file, var2 + ".xml"), CommonUtils.toBytes(var4.toString(), "UTF-8"));
   }

   public void dumpTSV(List var1, String var2, String[] var3) {
      StringBuffer var4 = new StringBuffer();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4.append(var3[var5]);
         if (var5 + 1 < var3.length) {
            var4.append("\t");
         }
      }

      var4.append("\n");
      Iterator var8 = var1.iterator();

      while(var8.hasNext()) {
         Map var6 = (Map)var8.next();

         for(int var7 = 0; var7 < var3.length; ++var7) {
            var4.append(DialogUtils.string(var6, var3[var7]));
            if (var7 + 1 < var3.length) {
               var4.append("\t");
            }
         }

         var4.append("\n");
      }

      CommonUtils.writeToFile(new File(this.file, var2 + ".tsv"), CommonUtils.toBytes(var4.toString(), "UTF-8"));
   }

   public static List getKey(List var0, String var1) {
      LinkedList var2 = new LinkedList();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Map var4 = (Map)var3.next();
         String var5 = DialogUtils.string(var4, "type");
         if (var5.equals(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public static List getBeaconStuff(List var0) {
      LinkedList var1 = new LinkedList();
      Iterator var2 = var0.iterator();

      while(true) {
         Map var3;
         String var4;
         do {
            if (!var2.hasNext()) {
               return var1;
            }

            var3 = (Map)var2.next();
            var4 = DialogUtils.string(var3, "type");
         } while(!var4.equals("checkin") && !var4.equals("input") && !var4.equals("output") && !var4.equals("indicator") && !var4.equals("task") && !var4.equals("beacon_initial"));

         var1.add(var3);
      }
   }

   public void run() {
      ProgressMonitor var1 = new ProgressMonitor(this.client, "Export Data", "Starting...", 0, 6 + Keys.size());
      int var2 = 0;
      var1.setNote("Aggregate data...");
      Map var3 = DataAggregate.AllModels(this.client);
      var1.setProgress(1);
      ++var2;
      var1.setNote("webhits");
      this.dump(getKey((List)var3.get("archives"), "webhit"), "webhits", CommonUtils.toArray("when, token, data"));
      var1.setProgress(2);
      ++var2;
      var1.setNote("campaigns");
      this.dump(getKey((List)var3.get("archives"), "sendmail_start"), "campaigns", CommonUtils.toArray("cid, when, url, attachment, template, subject"));
      var1.setProgress(3);
      ++var2;
      var1.setNote("sentemails");
      this.dump(getKey((List)var3.get("archives"), "sendmail_post"), "sentemails", CommonUtils.toArray("token, cid, when, status, data"));
      var1.setProgress(4);
      ++var2;
      var1.setNote("activity");
      this.dump(getBeaconStuff((List)var3.get("archives")), "activity", CommonUtils.toArray("bid, type, when, data, tactic"));
      var1.setProgress(5);
      ++var2;
      var1.setNote("events");
      this.dump(getKey((List)var3.get("archives"), "notify"), "events", CommonUtils.toArray("when, data"));
      var1.setProgress(6);
      ++var2;

      for(Iterator var4 = Keys.getDataModelIterator(); var4.hasNext(); ++var2) {
         String var5 = (String)var4.next();
         var1.setNote(var5);
         this.dump((List)var3.get(var5), var5, Keys.getCols(var5));
         var1.setProgress(var2);
      }

      var1.close();
      DialogUtils.showInfo("Exported data to " + this.file);
   }

   public void show() {
      JFrame var1 = DialogUtils.dialog("Export Data", 640, 480);
      DialogManager var2 = new DialogManager(var1);
      var2.addDialogListener(this);
      var2.combobox("output", "Output:", CommonUtils.toArray("TSV, XML"));
      JButton var3 = var2.action("Export");
      JButton var4 = var2.help("https://www.cobaltstrike.com/help-export-data");
      var1.add(var2.layout(), "Center");
      var1.add(DialogUtils.center(var3, var4), "South");
      var1.pack();
      var1.setVisible(true);
   }
}
