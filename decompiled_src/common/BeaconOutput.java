package common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import sleep.runtime.SleepUtils;

public class BeaconOutput implements Serializable, Transcript, Loggable, Informant, Scriptable {
   public static final short ERROR = 0;
   public static final short TASK = 1;
   public static final short OUTPUT = 2;
   public static final short CHECKIN = 3;
   public static final short INPUT = 4;
   public static final short MODE = 5;
   public static final short OUTPUT_PS = 6;
   public static final short OUTPUTB = 7;
   public static final short OUTPUT_JOBS = 8;
   public static final short OUTPUT_LS = 9;
   public static final short INDICATOR = 10;
   public static final short ACTIVITY = 11;
   public String from;
   public long when;
   public short type;
   public String text;
   public String bid;
   public String tactic;

   public boolean is(String var1) {
      return this.bid.equals(var1);
   }

   public boolean isSSH() {
      return "session".equals(CommonUtils.session(this.bid));
   }

   public boolean isBeacon() {
      return !this.isSSH();
   }

   public String prefix(String var1) {
      return this.isSSH() ? "ssh_" + var1 : "beacon_" + var1;
   }

   public String eventName() {
      switch (this.type) {
         case 0:
            return this.prefix("error");
         case 1:
            return this.prefix("tasked");
         case 2:
            return this.prefix("output");
         case 3:
            return this.prefix("checkin");
         case 4:
            return this.prefix("input");
         case 5:
            return this.prefix("mode");
         case 6:
            return this.prefix("output_ps");
         case 7:
            return this.prefix("output_alt");
         case 8:
            return this.prefix("output_jobs");
         case 9:
            return this.prefix("output_ls");
         case 10:
            return this.prefix("indicator");
         default:
            return this.prefix("generic");
      }
   }

   public Stack eventArguments() {
      Stack var1 = new Stack();
      switch (this.type) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
            var1.push(SleepUtils.getScalar(this.when));
            var1.push(SleepUtils.getScalar(this.text));
            var1.push(SleepUtils.getScalar(this.bid));
            break;
         case 4:
         case 10:
            var1.push(SleepUtils.getScalar(this.when));
            var1.push(SleepUtils.getScalar(this.text));
            var1.push(SleepUtils.getScalar(this.from));
            var1.push(SleepUtils.getScalar(this.bid));
      }

      return var1;
   }

   public BeaconOutput(String var1, short var2, String var3) {
      this(var1, var2, var3, "");
   }

   public BeaconOutput(String var1, short var2, String var3, String var4) {
      this.from = null;
      this.when = System.currentTimeMillis();
      this.tactic = "";
      this.type = var2;
      this.text = var3;
      this.bid = var1;
      this.tactic = var4;
   }

   public static final BeaconOutput Input(String var0, String var1) {
      return new BeaconOutput(var0, (short)4, var1);
   }

   public static final BeaconOutput Mode(String var0, String var1) {
      return new BeaconOutput(var0, (short)5, var1);
   }

   public static final BeaconOutput Error(String var0, String var1) {
      return new BeaconOutput(var0, (short)0, var1);
   }

   public static final BeaconOutput Task(String var0, String var1) {
      return new BeaconOutput(var0, (short)1, var1);
   }

   public static final BeaconOutput Task(String var0, String var1, String var2) {
      return new BeaconOutput(var0, (short)1, var1, var2);
   }

   public static final BeaconOutput Output(String var0, String var1) {
      return new BeaconOutput(var0, (short)2, var1);
   }

   public static final BeaconOutput OutputB(String var0, String var1) {
      return new BeaconOutput(var0, (short)7, var1);
   }

   public static final BeaconOutput OutputPS(String var0, String var1) {
      return new BeaconOutput(var0, (short)6, var1);
   }

   public static final BeaconOutput OutputLS(String var0, String var1) {
      return new BeaconOutput(var0, (short)9, var1);
   }

   public static final BeaconOutput Checkin(String var0, String var1) {
      return new BeaconOutput(var0, (short)3, var1);
   }

   public static final BeaconOutput OutputJobs(String var0, String var1) {
      return new BeaconOutput(var0, (short)8, var1);
   }

   public static final BeaconOutput Indicator(String var0, String var1) {
      return new BeaconOutput(var0, (short)10, var1);
   }

   public static final BeaconOutput Activity(String var0, String var1) {
      return new BeaconOutput(var0, (short)11, var1);
   }

   public static final BeaconOutput FileIndicator(String var0, String var1, byte[] var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append("file: ");
      var3.append(CommonUtils.toHex(CommonUtils.MD5(var2)));
      var3.append(" ");
      var3.append(var2.length);
      var3.append(" bytes ");
      var3.append(var1);
      return Indicator(var0, var3.toString());
   }

   public static final BeaconOutput ServiceIndicator(String var0, String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append("service: \\\\");
      var3.append(var1);
      var3.append(" ");
      var3.append(var2);
      return Indicator(var0, var3.toString());
   }

   public void touch() {
      this.when = System.currentTimeMillis();
   }

   public void user(String var1) {
      this.from = var1;
   }

   public String toString() {
      if (this.type == 1) {
         return "[TASK] " + this.from + " " + this.text;
      } else if (this.type == 2) {
         return "[OUTPUT] " + this.text;
      } else {
         return this.type == 0 ? "[ERROR] " + this.text : "Output: " + this.type;
      }
   }

   public String getBeaconId() {
      return this.bid;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      if (this.type != 11) {
         var1.writeBytes(CommonUtils.formatLogDate(this.when));
         var1.writeBytes(" ");
         switch (this.type) {
            case 0:
               CommonUtils.writeUTF8(var1, "[error] " + this.text);
               break;
            case 1:
               CommonUtils.writeUTF8(var1, "[task] <" + this.tactic + "> " + this.text);
               break;
            case 2:
            case 6:
            case 7:
            case 8:
            case 9:
               CommonUtils.writeUTF8(var1, "[output]\n" + this.text + "\n");
               break;
            case 3:
               CommonUtils.writeUTF8(var1, "[checkin] " + this.text);
               break;
            case 4:
               CommonUtils.writeUTF8(var1, "[input] <" + this.from + "> " + this.text);
               break;
            case 5:
               CommonUtils.writeUTF8(var1, "[mode] " + this.text);
               break;
            case 10:
               CommonUtils.writeUTF8(var1, "[indicator] " + this.text);
         }

         var1.writeBytes("\n");
      }
   }

   public String getLogFile() {
      return this.prefix(this.bid + ".log");
   }

   public String getLogFolder() {
      return null;
   }

   public boolean hasInformation() {
      return this.type == 10 || this.type == 4 || this.type == 1 || this.type == 3 || this.type == 11 || this.type == 5;
   }

   public Map archive() {
      HashMap var1 = new HashMap();
      if (this.type == 10) {
         var1.put("type", "indicator");
         var1.put("bid", this.bid);
         var1.put("data", this.text);
         var1.put("when", this.when);
      } else if (this.type == 4) {
         var1.put("type", "input");
         var1.put("bid", this.bid);
         var1.put("data", this.text);
         var1.put("when", this.when);
      } else if (this.type == 1) {
         var1.put("type", "task");
         var1.put("bid", this.bid);
         if (this.text.startsWith("Tasked beacon to ")) {
            var1.put("data", this.text.substring("Tasked beacon to ".length()));
         } else if (this.text.startsWith("Tasked session to ")) {
            var1.put("data", this.text.substring("Tasked session to ".length()));
         } else {
            var1.put("data", this.text);
         }

         var1.put("when", this.when);
      } else if (this.type == 3) {
         var1.put("type", "checkin");
         var1.put("bid", this.bid);
         var1.put("data", this.text);
         var1.put("when", this.when);
      } else if (this.type == 11) {
         var1.put("type", "output");
         var1.put("bid", this.bid);
         var1.put("data", this.text);
         var1.put("when", this.when);
      } else if (this.type == 5) {
         var1.put("type", "task");
         var1.put("bid", this.bid);
         var1.put("data", this.text);
         var1.put("when", this.when);
      }

      if (!"".equals(this.tactic)) {
         var1.put("tactic", this.tactic);
      }

      return var1;
   }
}
