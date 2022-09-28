package phish;

import common.AObject;
import common.CommonUtils;
import encoders.Base64;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import ssl.ArmitageTrustListener;
import ssl.SecureSocket;

public class SmtpClient extends AObject implements ArmitageTrustListener {
   protected Socket socket = null;
   protected InputStream in = null;
   protected OutputStream out = null;
   protected SmtpNotify notify = null;

   public SmtpClient(SmtpNotify var1) {
      this.notify = var1;
   }

   public boolean trust(String var1) {
      return true;
   }

   public void update(String var1) {
      if (this.notify != null) {
         this.notify.update(var1);
      }

   }

   public String readLoop(InputStream var1) throws IOException {
      String var2 = CommonUtils.bString(SecureSocket.readbytes(var1));
      this.checkSmtpError(var2);
      return var2;
   }

   public void checkSmtpError(String var1) {
      if (!var1.startsWith("2") && !var1.startsWith("3")) {
         throw new RuntimeException(var1);
      }
   }

   public void writeb(OutputStream var1, String var2) throws IOException {
      for(int var3 = 0; var3 < var2.length(); ++var3) {
         var1.write((byte)var2.charAt(var3));
      }

      var1.flush();
   }

   public String send_email(String var1, String var2, String var3, String var4) throws Exception {
      MailServer var5 = PhishingUtils.parseServerString(var1);
      String var6 = "";
      String var7 = var2.split("@")[1];
      if (var5.delay > 0) {
         for(int var8 = CommonUtils.rand(var5.delay) + 1; var8 > 0; --var8) {
            this.update("[Delay " + var8 + "s]");
            CommonUtils.sleep(1000L);
         }
      }

      this.update("[Connecting to " + var5.lhost + ":" + var5.lport + "]");
      SecureSocket var9;
      if (var5.ssl) {
         var9 = new SecureSocket(var5.lhost, var5.lport, this);
         this.socket = var9.getSocket();
      } else {
         this.socket = new Socket(var5.lhost, var5.lport);
      }

      this.in = this.socket.getInputStream();
      this.out = new BufferedOutputStream(this.socket.getOutputStream(), 65536);
      this.socket.setSoTimeout(0);
      this.update("[Connected to " + var5.lhost + ":" + var5.lport + "]");
      var6 = this.readLoop(this.in);
      this.writeb(this.out, "EHLO " + var7 + "\r\n");
      this.update("[EHLO " + var7 + "]");
      var6 = this.readLoop(this.in);
      if (var5.starttls) {
         this.writeb(this.out, "STARTTLS\r\n");
         this.update("[STARTTLS]");
         var6 = this.readLoop(this.in);
         var9 = new SecureSocket(this.socket);
         this.socket = var9.getSocket();
         this.in = this.socket.getInputStream();
         this.out = new BufferedOutputStream(this.socket.getOutputStream(), 65536);
         this.socket.setSoTimeout(0);
         this.writeb(this.out, "EHLO " + var7 + "\r\n");
         this.update("EHLO " + var7);
         var6 = this.readLoop(this.in);
      }

      if (var5.username != null && var5.password != null) {
         if (!var5.starttls && CommonUtils.isin("STARTTLS", var6) && !CommonUtils.isin("AUTH", var6)) {
            this.writeb(this.out, "STARTTLS\r\n");
            this.update("[STARTTLS]");
            var6 = this.readLoop(this.in);
            var9 = new SecureSocket(this.socket);
            this.socket = var9.getSocket();
            this.in = this.socket.getInputStream();
            this.out = new BufferedOutputStream(this.socket.getOutputStream(), 65536);
            this.socket.setSoTimeout(0);
            this.writeb(this.out, "EHLO " + var7 + "\r\n");
            this.update("EHLO " + var7);
            var6 = this.readLoop(this.in);
         }

         this.writeb(this.out, "AUTH LOGIN\r\n");
         this.update("[AUTH LOGIN]");
         var6 = this.readLoop(this.in);
         this.writeb(this.out, Base64.encode(var5.username) + "\r\n");
         var6 = this.readLoop(this.in);
         this.writeb(this.out, Base64.encode(var5.password) + "\r\n");
         var6 = this.readLoop(this.in);
         this.update("[I am authenticated...]");
      }

      this.writeb(this.out, "MAIL FROM: <" + var2 + ">\r\n");
      this.update("[MAIL FROM: <" + var2 + ">]");
      var6 = this.readLoop(this.in);
      this.writeb(this.out, "RCPT TO: <" + var3 + ">\r\n");
      this.update("[RCPT TO: <" + var3 + ">]");
      var6 = this.readLoop(this.in);
      this.writeb(this.out, "DATA\r\n");
      this.update("[DATA]");
      var6 = this.readLoop(this.in);
      this.writeb(this.out, var4);
      this.writeb(this.out, "\r\n.\r\n");
      this.update("[Message Transmitted]");
      var6 = this.readLoop(this.in);
      return var6;
   }

   public void cleanup() {
      try {
         if (this.in != null) {
            this.in.close();
         }

         if (this.out != null) {
            this.out.close();
         }

         if (this.socket != null) {
            this.socket.close();
         }

         this.in = null;
         this.out = null;
         this.socket = null;
      } catch (IOException var2) {
      }

   }
}
