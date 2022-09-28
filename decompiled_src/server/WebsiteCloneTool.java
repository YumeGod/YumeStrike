package server;

import common.CommonUtils;
import common.MudgeSanity;
import common.Request;
import common.StringStack;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import ssl.ArmitageTrustListener;
import ssl.SecureSocket;

public class WebsiteCloneTool implements Runnable, HostnameVerifier, ArmitageTrustListener {
   protected Request request;
   protected ManageUser client;

   public WebsiteCloneTool(Request var1, ManageUser var2) {
      this.request = var1;
      this.client = var2;
      (new Thread(this, "Clone: " + var1.arg(0))).start();
   }

   public boolean trust(String var1) {
      return true;
   }

   public boolean verify(String var1, SSLSession var2) {
      return true;
   }

   private String cloneAttempt(String var1) throws Exception {
      URL var2 = new URL(var1);
      HttpURLConnection var3 = (HttpURLConnection)var2.openConnection();
      if (var3 instanceof HttpsURLConnection) {
         HttpsURLConnection var4 = (HttpsURLConnection)var3;
         var4.setHostnameVerifier(this);
         var4.setSSLSocketFactory(SecureSocket.getMyFactory(this));
      }

      var3.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
      var3.setInstanceFollowRedirects(true);
      byte[] var8 = CommonUtils.readAll(var3.getInputStream());
      if (var3.getResponseCode() != 302 && var3.getResponseCode() != 301) {
         String var5 = CommonUtils.bString(var8);
         String var6;
         if (!var2.getFile().endsWith("/")) {
            StringStack var7 = new StringStack(var2.getFile(), "/");
            var7.pop();
            var6 = CommonUtils.strrep(var1, var2.getFile(), var7.toString() + "/");
         } else {
            var6 = var1;
         }

         if (var5.toLowerCase().indexOf("shortcut icon") < 0 && var5.toLowerCase().indexOf("rel=\"icon") < 0) {
            var5 = var5.replaceFirst("(?i:<head.*?>)", "$0\n<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"/favicon.ico\">");
         }

         if (var5.toLowerCase().indexOf("<base href=") < 0) {
            var5 = var5.replaceFirst("(?i:<head.*?>)", "$0\n<base href=\"" + var6 + "\">");
         }

         return var5;
      } else {
         return this.cloneAttempt(var3.getHeaderField("location"));
      }
   }

   public void run() {
      String var1 = this.request.arg(0) + "";

      try {
         String var2 = this.cloneAttempt(var1);
         this.client.write(this.request.reply(var2));
      } catch (Exception var3) {
         MudgeSanity.logException("clone: " + var1, var3, false);
         this.client.write(this.request.reply("error: " + var3.getMessage()));
      }

   }
}
