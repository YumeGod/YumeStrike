package common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import sleep.runtime.Scalar;

public class Screenshot implements Serializable, Transcript, Loggable, ToScalar {
   protected String when;
   protected String bid;
   protected byte[] data;
   private static final SimpleDateFormat screenFileFormat = new SimpleDateFormat("hhmmss");

   public Screenshot(String var1, byte[] var2) {
      this.bid = var1;
      this.data = var2;
      this.when = System.currentTimeMillis() + "";
   }

   public String id() {
      return this.bid;
   }

   public String toString() {
      return "screenshot from beacon id: " + this.bid;
   }

   public String time() {
      return this.when;
   }

   public Icon getImage() {
      return new ImageIcon(this.data);
   }

   public String getBeaconId() {
      return this.bid;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      var1.write(this.data);
   }

   public String getLogFile() {
      Date var1 = new Date(Long.parseLong(this.when));
      String var2 = screenFileFormat.format(var1);
      return "screen_" + var2 + "_" + this.bid + ".jpg";
   }

   public String getLogFolder() {
      return "screenshots";
   }

   public Scalar toScalar() {
      HashMap var1 = new HashMap();
      var1.put("bid", this.bid);
      var1.put("when", this.when);
      var1.put("data", this.data);
      return ScriptUtils.convertAll(var1);
   }

   static {
      screenFileFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
   }
}
