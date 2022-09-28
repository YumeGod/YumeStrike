package aggressor.windows;

import aggressor.DataManager;
import aggressor.DataUtils;
import aggressor.WindowCleanup;
import common.AObject;
import common.Callback;
import common.CommandParser;
import common.CommonUtils;
import common.Do;
import common.LoggedEvent;
import common.TeamQueue;
import common.Timers;
import console.ActivityConsole;
import console.Colors;
import console.Console;
import console.ConsolePopup;
import console.GenericTabCompletion;
import cortana.Cortana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JTextField;
import sleep.runtime.SleepUtils;

public class EventLog extends AObject implements ActionListener, ConsolePopup, Callback, Do {
   protected Console console = null;
   protected TeamQueue conn = null;
   protected Cortana engine = null;
   protected DataManager data = null;
   protected String nick = null;
   protected WindowCleanup state = null;
   protected String lag = "??";

   protected Stack sbarArgs(String var1) {
      Stack var2 = new Stack();
      var2.push(SleepUtils.getScalar(var1));
      return var2;
   }

   public EventLog(DataManager var1, Cortana var2, TeamQueue var3) {
      this.engine = var2;
      this.conn = var3;
      this.data = var1;
      this.nick = DataUtils.getNick(var1);
      this.console = new ActivityConsole(true);
      this.console.updatePrompt(Colors.underline("event") + "> ");
      this.console.getInput().addActionListener(this);
      String var4 = var2.format("EVENT_SBAR_LEFT", this.sbarArgs("00"));
      String var5 = var2.format("EVENT_SBAR_RIGHT", this.sbarArgs("00"));
      this.console.getStatusBar().set(var4, var5);
      StringBuffer var6 = new StringBuffer();
      Iterator var7 = var1.getTranscriptAndSubscribeSafe("eventlog", this).iterator();

      while(var7.hasNext()) {
         var6.append(this.format("eventlog", var7.next()));
      }

      this.console.append(var6.toString());
      this.state = var1.unsubOnClose("eventlog", this);
      new EventLogTabCompleter();
      this.console.setPopupMenu(this);
      Timers.getTimers().every(1000L, "time", this);
      Timers.getTimers().every(10000L, "lag", this);
   }

   public boolean moment(String var1) {
      if ("time".equals(var1) && this.console.isShowing()) {
         String var2 = CommonUtils.padr(this.lag, "0", 2);
         String var3 = this.engine.format("EVENT_SBAR_LEFT", this.sbarArgs(var2));
         String var4 = this.engine.format("EVENT_SBAR_RIGHT", this.sbarArgs(var2));
         this.console.getStatusBar().set(var3, var4);
      } else if ("lag".equals(var1)) {
         this.lag = "??";
         this.conn.call("aggressor.ping", CommonUtils.args(new Long(System.currentTimeMillis())), new Callback() {
            public void result(String var1, Object var2) {
               Long var3 = (Long)var2;
               EventLog.this.lag = (int)((double)(System.currentTimeMillis() - var3) / 1000.0) + "";
            }
         });
      }

      return this.state.isOpen();
   }

   public ActionListener cleanup() {
      return this.state;
   }

   public Console getConsole() {
      return this.console;
   }

   public void result(String var1, Object var2) {
      this.console.append(this.format(var1, var2));
   }

   public String format(String var1, Object var2) {
      LoggedEvent var3 = (LoggedEvent)var2;
      String var4 = var3.eventName();
      Stack var5 = var3.eventArguments();
      String var6 = this.engine.format(var4.toUpperCase(), var5);
      return var6 == null ? "" : var6 + "\n";
   }

   public void showPopup(String var1, MouseEvent var2) {
      this.engine.getMenuBuilder().installMenu(var2, "eventlog", new Stack());
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      ((JTextField)var1.getSource()).setText("");
      CommandParser var3 = new CommandParser(var2);
      if (var3.is("/msg")) {
         if (var3.verify("AZ")) {
            String var4 = var3.popString();
            String var5 = var3.popString();
            this.conn.call("aggressor.event", CommonUtils.args(LoggedEvent.Private(this.nick, var5, var4)), (Callback)null);
         }
      } else if (!var3.is("/names") && !var3.is("/sc")) {
         if (var3.is("/me")) {
            if (var3.verify("Z")) {
               this.conn.call("aggressor.event", CommonUtils.args(LoggedEvent.Action(this.nick, var3.popString())), (Callback)null);
            }
         } else if (var2.length() > 0) {
            this.conn.call("aggressor.event", CommonUtils.args(LoggedEvent.Public(this.nick, var2)), (Callback)null);
         }
      } else {
         LinkedList var6 = new LinkedList(DataUtils.getUsers(this.data));
         Collections.sort(var6);
         Stack var7 = new Stack();
         var7.push(SleepUtils.getArrayWrapper(var6));
         this.console.append(this.engine.format("EVENT_USERS", var7) + "\n");
      }

   }

   private class EventLogTabCompleter extends GenericTabCompletion {
      public EventLogTabCompleter() {
         super(EventLog.this.console);
      }

      public Collection getOptions(String var1) {
         LinkedList var2 = new LinkedList(DataUtils.getUsers(EventLog.this.data));
         LinkedList var3 = new LinkedList();
         var3.add("/me");
         var3.add("/msg");
         var3.add("/names");
         var3.add("/sc");

         String var5;
         for(Iterator var4 = var2.iterator(); var4.hasNext(); var3.add(var5)) {
            var5 = var4.next() + "";
            if (var1.indexOf(" ") > -1) {
               var3.add("/msg " + var5);
            }
         }

         Collections.sort(var3);
         Cortana.filterList(var3, var1);
         return var3;
      }
   }
}
