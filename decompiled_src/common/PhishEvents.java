package common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PhishEvents {
   protected String sid;

   public PhishEvents(String var1) {
      this.sid = var1;
   }

   protected PhishEvent build(String var1, LinkedList var2, String var3, Map var4) {
      PhishEvent var5 = new PhishEvent(this.sid, var1, var2, var3, var4);
      return var5;
   }

   public PhishEvent SendmailStart(int var1, String var2, String var3, String var4, String var5, String var6, String var7) {
      StringBuffer var8 = new StringBuffer();
      var8.append("[Campaign Start]\n");
      var8.append("Number of targets: " + var1 + "\n");
      var8.append("Template:          " + var6 + "\n");
      var8.append("Subject:           " + var5 + "\n");
      var8.append("URL:               " + var7 + "\n");
      var8.append("Attachment:        " + var1 + "\n");
      var8.append("Mail Server:       " + var4 + "\n");
      var8.append("Bounce To:         " + var3 + "\n");
      LinkedList var9 = new LinkedList();
      var9.add(this.sid);
      var9.add(new Long((long)var1));
      var9.add(var2);
      var9.add(var3);
      var9.add(var4);
      var9.add(var5);
      var9.add(var6);
      var9.add(var7);
      HashMap var10 = new HashMap();
      var10.put("when", System.currentTimeMillis());
      var10.put("type", "sendmail_start");
      var10.put("subject", var5);
      var10.put("url", var7);
      var10.put("attachment", var2);
      var10.put("template", var6);
      var10.put("subject", var5);
      var10.put("cid", this.sid);
      return this.build("sendmail_start", var9, var8.toString(), var10);
   }

   public PhishEvent SendmailPre(String var1) {
      LinkedList var2 = new LinkedList();
      var2.add(this.sid);
      var2.add(var1);
      return this.build("sendmail_pre", var2, "[Send] " + var1, (Map)null);
   }

   public PhishEvent SendmailPost(String var1, String var2, String var3, String var4) {
      LinkedList var5 = new LinkedList();
      var5.add(this.sid);
      var5.add(var1);
      var5.add(var2);
      var5.add(var3);
      HashMap var6 = new HashMap();
      var6.put("when", System.currentTimeMillis());
      var6.put("type", "sendmail_post");
      var6.put("status", var2);
      var6.put("data", var3.trim());
      var6.put("token", var4);
      var6.put("cid", this.sid);
      return this.build("sendmail_post", var5, "[Status] " + var4 + " " + var1 + " " + var2 + " " + var3.trim(), var6);
   }

   public PhishEvent SendmailDone() {
      LinkedList var1 = new LinkedList();
      var1.add(this.sid);
      return this.build("sendmail_done", var1, "[Campaign Complete]", (Map)null);
   }
}
