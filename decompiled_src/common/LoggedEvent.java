package common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import sleep.runtime.SleepUtils;

public class LoggedEvent implements Serializable, Scriptable, Transcript, Loggable, Informant {
   public static final short PUBLIC_CHAT_EVENT = 0;
   public static final short PRIVATE_CHAT_EVENT = 1;
   public static final short JOIN_EVENT = 2;
   public static final short QUIT_EVENT = 3;
   public static final short ACTION_EVENT = 4;
   public static final short NOTIFY_EVENT = 5;
   public static final short NOUSER_ERROR = 6;
   public static final short NEW_SITE = 7;
   public static final short BEACON_INITIAL_EVENT = 8;
   public static final short SSH_INITIAL_EVENT = 9;
   public String from = null;
   public String to = null;
   public String text = null;
   public long when = 0L;
   public short type = 0;

   public static final LoggedEvent NoUser(LoggedEvent var0) {
      LoggedEvent var1 = new LoggedEvent((String)null, var0.to, (short)6, (String)null);
      var1.when = var0.when;
      return var1;
   }

   public static final LoggedEvent Join(String var0) {
      return new LoggedEvent(var0, (String)null, (short)2, (String)null);
   }

   public static final LoggedEvent Quit(String var0) {
      return new LoggedEvent(var0, (String)null, (short)3, (String)null);
   }

   public static final LoggedEvent Public(String var0, String var1) {
      return new LoggedEvent(var0, (String)null, (short)0, var1);
   }

   public static final LoggedEvent Private(String var0, String var1, String var2) {
      return new LoggedEvent(var0, var1, (short)1, var2);
   }

   public static final LoggedEvent Action(String var0, String var1) {
      return new LoggedEvent(var0, (String)null, (short)4, var1);
   }

   public static final LoggedEvent Notify(String var0) {
      return new LoggedEvent((String)null, (String)null, (short)5, var0);
   }

   public static final LoggedEvent NewSite(String var0, String var1, String var2) {
      return new LoggedEvent(var0, (String)null, (short)7, "hosted " + var2 + " @ " + var1);
   }

   public static final LoggedEvent BeaconInitial(BeaconEntry var0) {
      return var0.isBeacon() ? new LoggedEvent((String)null, var0.getId(), (short)8, var0.getUser() + "@" + var0.getInternal() + " (" + var0.getComputer() + ")") : new LoggedEvent((String)null, var0.getId(), (short)9, var0.getUser() + "@" + var0.getInternal() + " (" + var0.getComputer() + ")");
   }

   public LoggedEvent(String var1, String var2, short var3, String var4) {
      this.from = var1;
      this.to = var2;
      this.text = var4;
      this.type = var3;
      this.when = System.currentTimeMillis();
   }

   public void touch() {
      this.when = System.currentTimeMillis();
   }

   public Stack eventArguments() {
      Stack var1 = new Stack();
      switch (this.type) {
         case 0:
         case 4:
         case 7:
            var1.push(SleepUtils.getScalar(this.when));
            var1.push(SleepUtils.getScalar(this.text));
            var1.push(SleepUtils.getScalar(this.from));
            break;
         case 1:
            var1.push(SleepUtils.getScalar(this.when));
            var1.push(SleepUtils.getScalar(this.text));
            var1.push(SleepUtils.getScalar(this.to));
            var1.push(SleepUtils.getScalar(this.from));
            break;
         case 2:
         case 3:
            var1.push(SleepUtils.getScalar(this.when));
            var1.push(SleepUtils.getScalar(this.from));
            break;
         case 5:
            var1.push(SleepUtils.getScalar(this.when));
            var1.push(SleepUtils.getScalar(this.text));
            break;
         case 6:
            var1.push(SleepUtils.getScalar(this.when));
            var1.push(SleepUtils.getScalar(this.to));
            break;
         case 8:
            var1.push(SleepUtils.getScalar(this.when));
            var1.push(SleepUtils.getScalar(this.text));
         case 9:
            var1.push(SleepUtils.getScalar(this.when));
            var1.push(SleepUtils.getScalar(this.text));
      }

      return var1;
   }

   public String eventName() {
      switch (this.type) {
         case 0:
            return "event_public";
         case 1:
            return "event_private";
         case 2:
            return "event_join";
         case 3:
            return "event_quit";
         case 4:
            return "event_action";
         case 5:
            return "event_notify";
         case 6:
            return "event_nouser";
         case 7:
            return "event_newsite";
         case 8:
            return "event_beacon_initial";
         case 9:
            return "event_ssh_initial";
         default:
            return "event_unknown";
      }
   }

   public String getBeaconId() {
      return null;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      var2.append(CommonUtils.formatLogDate(this.when));
      var2.append(" ");
      switch (this.type) {
         case 0:
            var2.append("<" + this.from + "> " + this.text + "\n");
            break;
         case 1:
            return;
         case 2:
            var2.append("*** " + this.from + " joined\n");
            break;
         case 3:
            var2.append("*** " + this.from + " quit\n");
            break;
         case 4:
            var2.append("* " + this.from + " " + this.text + "\n");
            break;
         case 5:
            var2.append("*** " + this.text + "\n");
            break;
         case 6:
            return;
         case 7:
            var2.append("*** " + this.from + " " + this.text + "\n");
            break;
         case 8:
            var2.append("*** initial beacon from " + this.text + "\n");
            break;
         case 9:
            var2.append("*** new ssh session " + this.text + "\n");
      }

      CommonUtils.writeUTF8(var1, var2.toString());
   }

   public String getLogFile() {
      return "events.log";
   }

   public String getLogFolder() {
      return null;
   }

   public boolean hasInformation() {
      return this.type == 8 || this.type == 5 || this.type == 7 || this.type == 9;
   }

   public Map archive() {
      HashMap var1 = new HashMap();
      var1.put("when", this.when);
      if (this.type == 8) {
         var1.put("type", "beacon_initial");
         var1.put("data", "initial beacon");
         var1.put("bid", this.to);
      } else if (this.type == 9) {
         var1.put("type", "ssh_initial");
         var1.put("data", "new ssh session");
         var1.put("bid", this.to);
      } else if (this.type == 5 || this.type == 7) {
         var1.put("type", "notify");
         var1.put("data", this.text);
      }

      return var1;
   }
}
