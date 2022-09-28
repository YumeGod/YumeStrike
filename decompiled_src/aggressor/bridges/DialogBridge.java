package aggressor.bridges;

import aggressor.AggressorClient;
import common.CommonUtils;
import cortana.Cortana;
import dialog.DialogListener;
import dialog.DialogManager;
import dialog.DialogUtils;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class DialogBridge implements Function, Loadable {
   protected AggressorClient client;

   public DialogBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&dialog", this);
      Cortana.put(var1, "&dialog_show", this);
      Cortana.put(var1, "&dialog_description", this);
      Cortana.put(var1, "&drow_checkbox", this);
      Cortana.put(var1, "&drow_combobox", this);
      Cortana.put(var1, "&drow_file", this);
      Cortana.put(var1, "&drow_text", this);
      Cortana.put(var1, "&drow_text_big", this);
      Cortana.put(var1, "&drow_beacon", this);
      Cortana.put(var1, "&drow_exploits", this);
      Cortana.put(var1, "&drow_interface", this);
      Cortana.put(var1, "&drow_krbtgt", this);
      Cortana.put(var1, "&drow_listener", this);
      Cortana.put(var1, "&drow_listener_stage", this);
      Cortana.put(var1, "&drow_listener_smb", this);
      Cortana.put(var1, "&drow_mailserver", this);
      Cortana.put(var1, "&drow_proxyserver", this);
      Cortana.put(var1, "&drow_site", this);
      Cortana.put(var1, "&dbutton_action", this);
      Cortana.put(var1, "&dbutton_help", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&dialog")) {
         String var15 = BridgeUtilities.getString(var3, "");
         Map var16 = SleepUtils.getMapFromHash(BridgeUtilities.getHash(var3));
         SleepClosure var18 = BridgeUtilities.getFunction(var3, var2);
         ScriptedDialog var14 = new ScriptedDialog(var15, 640, 480, var18);
         Iterator var8 = var16.entrySet().iterator();

         while(var8.hasNext()) {
            Map.Entry var9 = (Map.Entry)var8.next();
            String var10 = var9.getKey().toString();
            String var11 = var9.getValue().toString();
            var14.controller.set(var10, var11);
         }

         return SleepUtils.getScalar((Object)var14);
      } else {
         ScriptedDialog var4;
         if (var1.equals("&dialog_description")) {
            var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
            var4.description = BridgeUtilities.getString(var3, "");
         } else if (var1.equals("&dialog_show")) {
            var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
            var4.show();
         } else {
            String var5;
            String var6;
            if (var1.equals("&drow_checkbox")) {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               String var7 = BridgeUtilities.getString(var3, "");
               var4.controller.checkbox_add(var5, var6, var7);
            } else if (var1.equals("&drow_combobox")) {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               String[] var12 = CommonUtils.toArray((Collection)SleepUtils.getListFromArray(BridgeUtilities.getArray(var3)));
               var4.controller.combobox(var5, var6, var12);
            } else if (var1.equals("&drow_file")) {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.controller.file(var5, var6);
            } else if (var1.equals("&drow_text")) {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               int var13 = BridgeUtilities.getInt(var3, 20);
               var4.controller.text(var5, var6, var13);
            } else if (var1.equals("&drow_text_big")) {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.controller.text_big(var5, var6);
            } else if (var1.equals("&drow_beacon")) {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.controller.beacon(var5, var6, this.client);
            } else if (var1.equals("&drow_exploits")) {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.controller.exploits(var5, var6, this.client);
            } else if (var1.equals("&drow_interface")) {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.controller.interfaces(var5, var6, this.client.getConnection(), this.client.getData());
            } else if (var1.equals("&drow_krbtgt")) {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.controller.krbtgt(var5, var6, this.client);
            } else if (var1.equals("&drow_listener")) {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.controller.sc_listener_stagers(var5, var6, this.client);
            } else if (!var1.equals("&drow_listener_stage") && !var1.equals("&drow_listener_smb")) {
               if (var1.equals("&drow_mailserver")) {
                  var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
                  var5 = BridgeUtilities.getString(var3, "");
                  var6 = BridgeUtilities.getString(var3, "");
                  var4.controller.mailserver(var5, var6);
               } else if (var1.equals("&drow_proxyserver")) {
                  var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
                  var5 = BridgeUtilities.getString(var3, "");
                  var6 = BridgeUtilities.getString(var3, "");
                  var4.controller.proxyserver(var5, var6, this.client);
               } else if (var1.equals("&drow_site")) {
                  var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
                  var5 = BridgeUtilities.getString(var3, "");
                  var6 = BridgeUtilities.getString(var3, "");
                  var4.controller.site(var5, var6, this.client.getConnection(), this.client.getData());
               } else {
                  JButton var17;
                  if (var1.equals("&dbutton_action")) {
                     var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
                     var5 = BridgeUtilities.getString(var3, "");
                     var17 = var4.controller.action(var5);
                     var4.buttons.add(var17);
                  } else if (var1.equals("&dbutton_help")) {
                     var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
                     var5 = BridgeUtilities.getString(var3, "");
                     var17 = var4.controller.help(var5);
                     var4.buttons.add(var17);
                  }
               }
            } else {
               var4 = (ScriptedDialog)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               var4.controller.sc_listener_all(var5, var6, this.client);
            }
         }

         return SleepUtils.getEmptyScalar();
      }
   }

   private class ScriptedDialog implements DialogListener {
      protected DialogManager controller;
      protected JFrame body;
      protected LinkedList buttons = new LinkedList();
      protected String description = "";
      protected SleepClosure callback;

      public ScriptedDialog(String var2, int var3, int var4, SleepClosure var5) {
         this.body = DialogUtils.dialog(var2, var3, var4);
         this.controller = new DialogManager(this.body);
         this.controller.addDialogListener(this);
         this.callback = var5;
      }

      public void dialogAction(ActionEvent var1, Map var2) {
         Stack var3 = new Stack();
         var3.push(SleepUtils.getHashWrapper(var2));
         var3.push(SleepUtils.getScalar(var1.getActionCommand()));
         var3.push(SleepUtils.getScalar((Object)this));
         SleepUtils.runCode((SleepClosure)this.callback, "", (ScriptInstance)null, var3);
      }

      public void show() {
         if (!"".equals(this.description)) {
            this.body.add(DialogUtils.description(this.description), "North");
         }

         this.body.add(this.controller.layout(), "Center");
         if (this.buttons.size() > 0) {
            this.body.add(DialogUtils.center((List)this.buttons), "South");
         }

         this.body.pack();
         this.body.setVisible(true);
      }
   }
}
