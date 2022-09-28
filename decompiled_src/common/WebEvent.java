package common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import sleep.runtime.SleepUtils;

public class WebEvent implements Serializable, Transcript, Scriptable, Loggable, Informant {
   public String method;
   public String addr;
   public String ua;
   public String from;
   public Map params;
   public String handler;
   public long when = System.currentTimeMillis();
   public String response;
   public long size;
   public String uri;
   public int port;

   public WebEvent(String var1, String var2, String var3, String var4, String var5, String var6, Map var7, String var8, long var9, int var11) {
      this.method = var1;
      this.uri = var2;
      this.addr = var3;
      this.ua = var4;
      this.from = var5;
      this.handler = var6;
      this.params = var7;
      this.response = var8;
      this.size = var9;
      this.port = var11;
      this.params.remove("input");
   }

   public Stack eventArguments() {
      Stack var1 = new Stack();
      var1.push(SleepUtils.getScalar(this.port));
      var1.push(SleepUtils.getScalar(this.when));
      var1.push(SleepUtils.getHashWrapper(this.params));
      var1.push(SleepUtils.getScalar(this.handler));
      var1.push(SleepUtils.getScalar(this.size));
      var1.push(SleepUtils.getScalar(this.response));
      var1.push(SleepUtils.getScalar(this.ua));
      var1.push(SleepUtils.getScalar(this.addr));
      var1.push(SleepUtils.getScalar(this.uri));
      var1.push(SleepUtils.getScalar(this.method));
      return var1;
   }

   public String eventName() {
      return "web_hit";
   }

   public String getBeaconId() {
      return null;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      var2.append(this.addr);
      var2.append(" ");
      if (this.from == null && !"unknown".equals(this.from)) {
         var2.append("- - [");
      } else {
         var2.append(this.from);
         var2.append(" ");
         var2.append(this.from);
         var2.append(" [");
      }

      var2.append(CommonUtils.formatLogDate(this.when));
      var2.append("] \"");
      var2.append(this.method);
      var2.append(" ");
      var2.append(this.uri);
      var2.append("\" ");
      var2.append(this.response.split(" ")[0]);
      var2.append(" ");
      var2.append(this.size);
      var2.append(" \"");
      if (this.handler != null) {
         var2.append(this.handler);
      }

      var2.append("\" \"");
      var2.append(this.ua);
      var2.append("\"\n");
      var1.writeBytes(var2.toString());
   }

   public String getLogFile() {
      StringBuffer var1 = new StringBuffer(32);
      var1.append("weblog_");
      var1.append(this.port);
      var1.append(".log");
      return var1.toString();
   }

   public String getLogFolder() {
      return null;
   }

   public boolean hasInformation() {
      return this.response.startsWith("200") && !"".equals(this.handler);
   }

   public Map archive() {
      HashMap var1 = new HashMap();
      var1.put("when", this.when);
      var1.put("type", "webhit");
      var1.put("data", "visit to " + this.uri + " (" + this.handler + ") by " + this.addr);
      if (this.params.containsKey("id")) {
         var1.put("token", this.params.get("id"));
      }

      return var1;
   }
}
