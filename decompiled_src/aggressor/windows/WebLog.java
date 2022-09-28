package aggressor.windows;

import aggressor.DataManager;
import aggressor.WindowCleanup;
import common.AObject;
import common.Callback;
import common.Scriptable;
import common.TeamQueue;
import console.ActivityConsole;
import console.Console;
import console.ConsolePopup;
import cortana.Cortana;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Stack;

public class WebLog extends AObject implements ConsolePopup, Callback {
   protected Console console = null;
   protected TeamQueue conn = null;
   protected Cortana engine = null;
   protected DataManager data = null;
   protected String nick = null;
   protected WindowCleanup state = null;

   public WebLog(DataManager var1, Cortana var2, TeamQueue var3) {
      this.engine = var2;
      this.conn = var3;
      this.data = var1;
      this.console = new ActivityConsole(false);
      this.console.updatePrompt("> ");
      StringBuffer var4 = new StringBuffer();
      Iterator var5 = var1.getTranscriptSafe("weblog").iterator();

      while(var5.hasNext()) {
         var4.append(this.format("weblog", var5.next()));
      }

      this.console.append(var4.toString());
      var1.subscribe("weblog", this);
      this.console.setPopupMenu(this);
   }

   public ActionListener cleanup() {
      return this.data.unsubOnClose("weblog", this);
   }

   public Console getConsole() {
      return this.console;
   }

   public String format(String var1, Object var2) {
      Scriptable var3 = (Scriptable)var2;
      return this.engine.format(var3.eventName().toUpperCase(), var3.eventArguments());
   }

   public void result(String var1, Object var2) {
      this.console.append(this.format(var1, var2));
   }

   public void showPopup(String var1, MouseEvent var2) {
      this.engine.getMenuBuilder().installMenu(var2, "weblog", new Stack());
   }
}
