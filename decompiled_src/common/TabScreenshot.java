package common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TabScreenshot implements Serializable, Loggable {
   protected long when;
   protected byte[] data;
   protected String who = null;
   protected String title = null;
   private static final SimpleDateFormat screenFileFormat = new SimpleDateFormat("hhmmss");

   public TabScreenshot(String var1, byte[] var2) {
      this.title = var1;
      this.data = var2;
   }

   public void touch(String var1) {
      this.when = System.currentTimeMillis();
      this.who = var1;
   }

   public String toString() {
      return "screenshot: " + this.title;
   }

   public String getBeaconId() {
      return null;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      var1.write(this.data);
   }

   public String getLogFile() {
      Date var1 = new Date(this.when);
      String var2 = screenFileFormat.format(var1);
      return var2 + "_" + this.title.replaceAll("[^a-zA-Z0-9\\.]", "") + ".png";
   }

   public String getLogFolder() {
      return "screenshots/" + this.who.replaceAll("[^a-zA-Z0-9]", "");
   }

   static {
      screenFileFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
   }
}
