package common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import sleep.runtime.Scalar;

public class Keystrokes implements Serializable, Transcript, Loggable, ToScalar {
   protected String when;
   protected String bid;
   protected String data;

   public Scalar toScalar() {
      HashMap var1 = new HashMap();
      var1.put("when", this.when);
      var1.put("bid", this.bid);
      var1.put("data", this.data);
      return ScriptUtils.convertAll(var1);
   }

   public Keystrokes(String var1, String var2) {
      this.bid = var1;
      this.data = var2;
      this.when = System.currentTimeMillis() + "";
   }

   public String id() {
      return this.bid;
   }

   public String toString() {
      return "keystrokes from beacon id: " + this.bid;
   }

   public String time() {
      return this.when;
   }

   public String getKeystrokes() {
      return this.data;
   }

   public String getBeaconId() {
      return this.bid;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      var1.writeBytes(CommonUtils.formatLogDate(Long.parseLong(this.when)) + " Received keystrokes");
      var1.writeBytes("\n\n");
      var1.writeBytes(this.getKeystrokes());
      var1.writeBytes("\n");
   }

   public String getLogFile() {
      return "keystrokes_" + this.bid + ".txt";
   }

   public String getLogFolder() {
      return "keystrokes";
   }
}
