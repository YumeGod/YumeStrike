package aggressor.windows;

import aggressor.DataManager;
import aggressor.WindowCleanup;
import common.AObject;
import common.Callback;
import common.CommonUtils;
import common.Scriptable;
import common.TeamQueue;
import console.ActivityConsole;
import console.Console;
import console.ConsolePopup;
import cortana.Cortana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class PhishLog extends AObject implements ConsolePopup, Callback, ActionListener {
   protected Console console = null;
   protected TeamQueue conn = null;
   protected Cortana engine = null;
   protected DataManager data = null;
   protected WindowCleanup state = null;
   protected String sid = null;

   public PhishLog(String var1, DataManager var2, Cortana var3, TeamQueue var4) {
      this.engine = var3;
      this.conn = var4;
      this.data = var2;
      this.sid = var1;
      this.console = new ActivityConsole(false);
      this.console.updatePrompt("");
      this.console.getInput().setEditable(false);
      var2.subscribe("phishlog." + var1, this);
      var2.subscribe("phishstatus." + var1, this);
      this.console.setPopupMenu(this);
   }

   public ActionListener cleanup() {
      return this;
   }

   public void actionPerformed(ActionEvent var1) {
      this.data.unsub("phishlog." + this.sid, this);
      this.data.unsub("phishstatus." + this.sid, this);
      this.conn.call("cloudstrike.stop_phish", CommonUtils.args(this.sid));
   }

   public Console getConsole() {
      return this.console;
   }

   public String format(String var1, Object var2) {
      Scriptable var3 = (Scriptable)var2;
      return this.engine.format(var3.eventName().toUpperCase(), var3.eventArguments());
   }

   public void result(String var1, Object var2) {
      if (var1.startsWith("phishstatus")) {
         this.console.updatePrompt(var2 + "");
      } else {
         this.console.append(this.format(var1, var2));
      }

   }

   public void showPopup(String var1, MouseEvent var2) {
      this.engine.getMenuBuilder().installMenu(var2, "phishlog", new Stack());
   }
}
