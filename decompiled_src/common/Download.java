package common;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import sleep.runtime.Scalar;

public class Download implements Serializable, Transcript, ToScalar, Loggable {
   protected long date;
   protected String bid;
   protected String name;
   protected String rpath;
   protected String lpath;
   protected long size;
   protected String host;
   protected long rcvd;
   protected int fid;

   public Download(int var1, String var2, String var3, String var4, String var5, String var6, long var7) {
      this.fid = var1;
      this.bid = var2;
      this.name = var4;
      this.rpath = var5;
      this.lpath = var6;
      this.size = var7;
      this.date = System.currentTimeMillis();
      this.host = var3;
      this.rcvd = (new File(var6)).length();
   }

   public String getBeaconId() {
      return null;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      var2.append(CommonUtils.formatLogDate(this.date));
      var2.append("\t");
      var2.append(this.host);
      var2.append("\t");
      var2.append(this.bid);
      var2.append("\t");
      var2.append(this.size);
      var2.append("\t");
      var2.append(this.lpath);
      var2.append("\t");
      var2.append(this.name);
      var2.append("\t");
      var2.append(this.rpath);
      var2.append("\n");
      CommonUtils.writeUTF8(var1, var2.toString());
   }

   public String getLogFile() {
      return "downloads.log";
   }

   public String getLogFolder() {
      return null;
   }

   public String id() {
      return this.bid;
   }

   public String toString() {
      return "file download";
   }

   public Scalar toScalar() {
      return ScriptUtils.convertAll(this.toMap());
   }

   public Map toMap() {
      HashMap var1 = new HashMap();
      var1.put("host", this.host);
      var1.put("name", this.name);
      var1.put("date", this.date + "");
      var1.put("path", this.rpath);
      var1.put("lpath", this.lpath);
      var1.put("size", this.size + "");
      var1.put("rcvd", this.rcvd + "");
      var1.put("fid", this.fid + "");
      return var1;
   }
}
