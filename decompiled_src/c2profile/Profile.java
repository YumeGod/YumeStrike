package c2profile;

import beacon.BeaconPayload;
import cloudstrike.Response;
import common.CodeSigner;
import common.CommonUtils;
import common.MudgeSanity;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Profile implements Serializable {
   protected Map data = new HashMap();
   protected Preview preview = null;
   protected Map datal = new HashMap();
   protected Map variants = new HashMap();

   public String[] getVariants() {
      LinkedList var1 = new LinkedList(this.variants.keySet());
      Collections.sort(var1);
      return CommonUtils.toArray((Collection)var1);
   }

   public Profile getVariantProfile(String var1) {
      if (var1 != null && !"".equals(var1)) {
         if (!this.variants.containsKey(var1)) {
            CommonUtils.print_warn("Profile variant '" + var1 + "' does not exist. Degrading to normal profile state.");
            return this;
         } else {
            ProfileVariant var2 = (ProfileVariant)this.variants.get("default");
            ProfileVariant var3 = (ProfileVariant)this.variants.get(var1);
            Profile var4 = new Profile();
            var4.data.putAll(var2.data);
            var4.data.putAll(var3.data);
            var4.datal.putAll(var2.datal);
            var4.datal.putAll(var3.datal);
            return var4;
         }
      } else {
         return this;
      }
   }

   public void activateVariant(String var1) {
      if (!this.variants.containsKey(var1)) {
         this.variants.put(var1, new ProfileVariant());
      }

      ProfileVariant var2 = (ProfileVariant)this.variants.get(var1);
      this.data = var2.data;
      this.datal = var2.datal;
   }

   public Profile() {
      this.activateVariant("default");
   }

   public void setList(String var1, List var2) {
      this.datal.put(var1, var2);
   }

   public void addList(String var1) {
      this.datal.put(var1, new LinkedList());
   }

   public void addParameter(String var1, Object var2) {
      this.data.put(var1, var2);
   }

   public void logToString(String var1, String var2) {
      String var3 = var1 + ".log.string";
      if (!this.data.containsKey(var3)) {
         this.data.put(var3, new LinkedList());
      }

      LinkedList var4 = (LinkedList)this.data.get(var3);
      var4.add(var2.trim());
   }

   public String getToStringLog(String var1) {
      String var2 = var1 + ".log.string";
      if (!this.data.containsKey(var2)) {
         return null;
      } else {
         LinkedList var3 = new LinkedList((LinkedList)this.data.get(var2));
         return CommonUtils.join((Collection)var3, (String)"\n");
      }
   }

   public void addToString(String var1, byte[] var2) {
      String var3 = var1 + ".string";
      if (!this.data.containsKey(var3)) {
         this.data.put(var3, new SmartBuffer());
      }

      SmartBuffer var4 = (SmartBuffer)this.data.get(var3);
      var4.append(var2);
   }

   public SmartBuffer getToString(String var1) {
      String var2 = var1 + ".string";
      if (!this.data.containsKey(var2)) {
         return new SmartBuffer();
      } else {
         SmartBuffer var3 = (SmartBuffer)this.data.get(var2);
         return var3;
      }
   }

   public static boolean usesCookieBeacon(Profile var0) {
      return var0.usesCookie(".http-get.client") || var0.usesCookie(".http-post.client") || var0.usesCookie(".http-get.client.metadata") || var0.usesCookie(".http-post.client.id") || var0.usesCookie(".http-post.client.output");
   }

   public static boolean usesHostBeacon(Profile var0) {
      return var0.usesHost(".http-get.client.metadata") || var0.usesHost(".http-post.client.id") || var0.usesHost(".http-post.client.output");
   }

   public boolean usesCookie(String var1) {
      Program var2 = this.getProgram(var1);
      return var2 != null && var2.usesCookie();
   }

   public boolean usesHost(String var1) {
      Program var2 = this.getProgram(var1);
      return var2 != null && var2.usesHost();
   }

   public boolean isSealed(String var1) {
      Program var2 = this.getProgram(var1);
      return var2 != null && var2.isSealed();
   }

   public Preview getPreview() {
      synchronized(this) {
         if (this.preview == null) {
            this.preview = new Preview(this);
         }

         return this.preview;
      }
   }

   public void addCommand(String var1, String var2, String var3) {
      if (this.datal.containsKey(var1)) {
         LinkedList var5 = (LinkedList)this.datal.get(var1);
         if (var3 == null) {
            var5.add(var2);
         } else {
            var5.add(var2 + " " + var3);
         }

      } else {
         if (!this.data.containsKey(var1)) {
            this.data.put(var1, new Program());
         }

         Program var4 = this.getProgram(var1);
         var4.addStep(var2, var3);
      }
   }

   public void apply(String var1, Response var2, byte[] var3) {
      Program var4 = this.getProgram(var1);
      if (var4 != null) {
         var4.transform(this, var2, var3);
      }

   }

   public String recover(String var1, Map var2, Map var3, String var4, String var5) {
      Program var6 = this.getProgram(var1);
      return var6.recover(var2, var3, var4, var5);
   }

   public Program getProgram(String var1) {
      return (Program)this.data.get(var1);
   }

   public byte[] apply_binary(String var1) throws IOException {
      Program var2 = this.getProgram(var1);
      return var2.transform_binary(this);
   }

   public byte[] recover_binary(String var1) throws IOException {
      Program var2 = this.getProgram(var1);
      return var2.recover_binary();
   }

   public int size(String var1, int var2) throws IOException {
      byte[] var3 = new byte[var2];
      Response var4 = new Response("200 OK", (String)null, (InputStream)null);
      this.apply(var1, var4, var3);
      return var4.data != null ? var4.data.available() : 0;
   }

   public boolean hasString(String var1) {
      return this.data.containsKey(var1);
   }

   public List getList(String var1) {
      return (LinkedList)this.datal.get(var1);
   }

   public String getString(String var1) {
      return this.data.get(var1) + "";
   }

   public boolean option(String var1) {
      return this.getString(var1).equals("true");
   }

   public byte[] getByteArray(String var1) {
      return (byte[])((byte[])this.data.get(var1));
   }

   public File getFile(String var1) {
      return new File(this.getString(var1));
   }

   public CodeSigner getCodeSigner() {
      return new CodeSigner(this);
   }

   public boolean isFile(String var1) {
      return "".equals(this.getString(var1)) ? false : this.getFile(var1).exists();
   }

   public boolean posts(String var1) {
      Program var2 = this.getProgram(var1);
      return var2 == null ? false : var2.postsData();
   }

   public boolean shouldChunkPosts() {
      return !this.posts(".http-post.client.output");
   }

   public boolean exerciseCFGCaution() {
      return !"".equals(this.getString(".stage.module_x86")) || !"".equals(this.getString(".stage.module_x64"));
   }

   public int getInt(String var1) {
      return Integer.parseInt(this.getString(var1));
   }

   protected String certDescription() {
      return "CN=" + this.getString(".https-certificate.CN") + ", OU=" + this.getString(".https-certificate.OU") + ", O=" + this.getString(".https-certificate.O") + ", L=" + this.getString(".https-certificate.L") + ", ST=" + this.getString(".https-certificate.ST") + ", C=" + this.getString(".https-certificate.C");
   }

   public boolean regenerateKeystore() {
      return !"CN=, OU=, O=, L=, ST=, C=".equals(this.certDescription()) || this.getInt(".https-certificate.validity") != 3650;
   }

   public String getSSLPassword() {
      return this.getString(".https-certificate.password");
   }

   public boolean hasValidSSL() {
      return this.isFile(".https-certificate.keystore");
   }

   public InputStream getSSLKeystore() {
      try {
         if (this.isFile(".https-certificate.keystore")) {
            return new FileInputStream(this.getFile(".https-certificate.keystore"));
         } else if (!this.regenerateKeystore()) {
            return null;
         } else {
            File var1 = new File("./ssl" + System.currentTimeMillis() + ".store");
            var1.deleteOnExit();
            LinkedList var2 = new LinkedList();
            var2.add("keytool");
            var2.add("-keystore");
            var2.add(var1.getAbsolutePath());
            var2.add("-storepass");
            var2.add("123456");
            var2.add("-keypass");
            var2.add("123456");
            var2.add("-genkey");
            var2.add("-keyalg");
            var2.add("RSA");
            var2.add("-alias");
            var2.add("cobaltstrike");
            var2.add("-dname");
            var2.add(this.certDescription());
            var2.add("-validity");
            var2.add(this.getString(".https-certificate.validity"));
            ProcessBuilder var3 = new ProcessBuilder(var2);
            var3.inheritIO();
            Process var4 = var3.start();
            var4.waitFor();
            return new FileInputStream(var1);
         }
      } catch (Exception var5) {
         CommonUtils.print_error("SSL certificate generation failed:\n\t" + var5.getMessage());
         return null;
      }
   }

   public byte[] getPrependedData(String var1) {
      Program var2 = this.getProgram(var1);
      return var2 == null ? new byte[0] : var2.getPrependedData();
   }

   public byte[] getAppendedData(String var1) {
      Program var2 = this.getProgram(var1);
      return var2 == null ? new byte[0] : var2.getAppendedData();
   }

   public long getHTTPContentOffset(String var1) {
      Response var2 = new Response("200 OK", (String)null, (InputStream)null);
      byte[] var3 = CommonUtils.randomData(16);
      this.apply(var1, var2, var3);
      return var2.offset;
   }

   public Map getHeadersAsMap(String var1) {
      Response var2 = new Response("200 OK", (String)null, (InputStream)null);
      this.apply(var1, var2, new byte[0]);
      return var2.header;
   }

   protected boolean hasHeader(Map var1, String var2) {
      Iterator var3 = var1.keySet().iterator();

      String var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (String)var3.next();
      } while(!var2.toLowerCase().equals(var4.toLowerCase()));

      return true;
   }

   public String getHeaders(String var1, String var2) {
      Response var3 = new Response("200 OK", (String)null, (InputStream)null);
      byte[] var4 = CommonUtils.randomData(16);
      this.apply(var1, var3, var4);
      StringBuffer var5 = new StringBuffer();
      if (!this.hasHeader(var3.header, "User-Agent")) {
         var3.header.put("User-Agent", BeaconPayload.randua(this));
      }

      if (!"".equals(var2) && !this.hasHeader(var3.header, "Host")) {
         var3.header.put("Host", var2);
      }

      Iterator var6 = var3.header.entrySet().iterator();

      while(true) {
         while(var6.hasNext()) {
            Map.Entry var7 = (Map.Entry)var6.next();
            String var8 = var7.getKey() + "";
            String var9 = var7.getValue() + "";
            if (!"".equals(var2) && var8.toLowerCase().equals("host")) {
               var5.append(var8 + ": " + var2 + "\r\n");
            } else {
               var5.append(var8 + ": " + var9 + "\r\n");
            }
         }

         return var5.toString();
      }
   }

   public String getQueryString(String var1) {
      Response var2 = new Response("200 OK", (String)null, (InputStream)null);
      byte[] var3 = CommonUtils.randomData(16);
      this.apply(var1, var2, var3);
      StringBuffer var4 = new StringBuffer();
      Iterator var5 = var2.params.entrySet().iterator();

      while(var5.hasNext()) {
         Map.Entry var6 = (Map.Entry)var5.next();
         String var7 = var6.getKey() + "";
         String var8 = var6.getValue() + "";

         try {
            var6.setValue(URLEncoder.encode(var6.getValue() + "", "UTF-8"));
         } catch (Exception var10) {
            MudgeSanity.logException("url encoding: " + var6, var10, false);
         }

         var4.append(var7 + "=" + var8);
         if (var5.hasNext()) {
            var4.append("&");
         }
      }

      return var4.length() == 0 ? "" : "?" + var4;
   }
}
