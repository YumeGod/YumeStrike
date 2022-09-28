package c2profile;

import beacon.BeaconPayload;
import cloudstrike.Response;
import common.CommonUtils;
import common.License;
import common.MudgeSanity;
import common.SleevedResource;
import common.WebTransforms;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import pe.MalleablePE;
import pe.PEParser;

public class Preview implements Serializable {
   protected Profile c2profile;
   protected Map characteristics = null;
   protected List notes = new LinkedList();

   public Preview(Profile var1) {
      this.c2profile = var1;
   }

   public String getSampleName() {
      return this.c2profile.getString(".sample_name");
   }

   public void summarize(Map var1) {
      var1.put("c2sample.client", this.getClientSample());
      var1.put("c2sample.server", this.getServerSample());
      var1.put("c2sample.name", this.getSampleName());
      var1.put("c2sample.strings", this.getStrings());
      var1.put("c2sample.pe", this.getPE());
   }

   public void note(String var1) {
      this.notes.add(var1);
   }

   public Map getPE() {
      if (this.characteristics != null) {
         return this.characteristics;
      } else {
         byte[] var1 = SleevedResource.readResource("resources/beacon.dll");
         MalleablePE var2 = new MalleablePE(this.c2profile);
         byte[] var3 = var2.pre_process(var1, "x86");
         PEParser var4 = PEParser.load(var3);
         this.characteristics = new LinkedHashMap();
         this.characteristics.put("Checksum", var4.get("CheckSum"));
         this.characteristics.put("Compilation Timestamp", var4.getDate("TimeDateStamp"));
         this.characteristics.put("Entry Point", var4.get("AddressOfEntryPoint"));
         this.characteristics.put("Name", var4.getString("Export.Name").replaceAll("\\P{Print}", "."));
         this.characteristics.put("Size", var4.get("SizeOfImage"));
         this.characteristics.put("Target Machine", "x86");
         if (License.isTrial()) {
            this.note("EICAR strings were observed within this payload and its traffic. This is a clever technique to detect and evade anti-virus products.");
         }

         if (!this.c2profile.option(".stage.obfuscate") && !this.c2profile.option(".stage.sleep_mask")) {
            if (this.c2profile.option(".stage.stomppe")) {
               this.note("The payload DLL clears its in-memory MZ, PE, and e_lfanew header values. This is a common obfuscation for memory injected DLLs.");
            }
         } else {
            this.characteristics.remove("Name");
            if (!this.c2profile.option(".stage.cleanup")) {
               this.note("The final payload DLL is obfuscated in memory.");
               this.note("The package that loads the payload DLL is less obfuscated.");
            } else {
               this.note("The payload DLL obfuscates itself in memory.");
            }
         }

         if (this.c2profile.option(".stage.userwx")) {
            if ("".equals(this.c2profile.getString(".stage.module_x86"))) {
               this.note("This payload resides in memory pages with RWX permissions. These memory pages are not backed by a file on disk.");
            } else {
               this.note("This payload resides in memory pages with RWX permissions.");
            }
         }

         if (!"".equals(this.c2profile.getString(".stage.module_x86"))) {
            this.note("This payload loads " + this.c2profile.getString(".stage.module_x86") + " and overwrites its location in memory. This hides the payload within memory backed by this legitimate file.");
         }

         if (this.notes.size() > 0) {
            this.characteristics.put("Notes", CommonUtils.join((Collection)this.notes, (String)" "));
         }

         return this.characteristics;
      }
   }

   public String getClientSample() {
      return this.getClientSample(".http-get");
   }

   public String getServerSample() {
      return this.getServerSample(".http-get");
   }

   public String getStrings() {
      return this.c2profile.getToStringLog(".stage");
   }

   public String getClientSample(String var1) {
      Response var2 = new Response("200 OK", (String)null, (InputStream)null);
      byte[] var3 = CommonUtils.randomData(16);
      String var4 = "";
      String var5 = "";
      if (var1.equals(".http-stager")) {
         var4 = this.c2profile.getString(var1 + ".uri_x86");
         if ("".equals(var4)) {
            var4 = CommonUtils.MSFURI();
         }

         var5 = "GET";
      } else {
         var4 = CommonUtils.pick(this.c2profile.getString(var1 + ".uri").split(" "));
         var5 = this.c2profile.getString(var1 + ".verb");
      }

      if (var1.equals(".http-post")) {
         var3 = CommonUtils.toBytes(CommonUtils.rand(99999) + "");
      }

      this.c2profile.apply(var1 + ".client", var2, var3);
      StringBuffer var6 = new StringBuffer();
      Iterator var7 = var2.params.entrySet().iterator();

      String var10;
      while(var7.hasNext()) {
         Map.Entry var8 = (Map.Entry)var7.next();
         String var9 = var8.getKey() + "";
         var10 = var8.getValue() + "";

         try {
            var8.setValue(URLEncoder.encode(var8.getValue() + "", "UTF-8"));
         } catch (Exception var13) {
            MudgeSanity.logException("url encoding: " + var8, var13, false);
         }

         var6.append(var9 + "=" + var10);
         if (var7.hasNext()) {
            var6.append("&");
         }
      }

      if (var6.length() > 0) {
         var4 = var4 + var2.uri + "?" + var6;
      } else {
         var4 = var4 + var2.uri;
      }

      StringBuffer var14 = new StringBuffer();
      var14.append(var5 + " " + var4 + " HTTP/1.1\n");
      if (!var2.header.containsKey("User-Agent")) {
         var2.header.put("User-Agent", BeaconPayload.randua(this.c2profile));
      }

      var7 = var2.header.entrySet().iterator();

      while(var7.hasNext()) {
         Map.Entry var15 = (Map.Entry)var7.next();
         var10 = var15.getKey() + "";
         String var11 = var15.getValue() + "";
         var15.setValue(var11.replaceAll("\\P{Graph}", ""));
         var14.append(var10 + ": " + var11 + "\n");
      }

      if (var2.data != null) {
         try {
            byte[] var16 = new byte[0];
            var16 = new byte[var2.data.available()];
            var2.data.read(var16, 0, var16.length);
            var14.append("\n" + CommonUtils.bString(var16).replaceAll("\\P{Print}", "."));
            var14.append("\n");
         } catch (Exception var12) {
            MudgeSanity.logException("sample generate", var12, false);
         }
      }

      var14.append("\n");
      return var14.toString();
   }

   public String getServerSample(String var1) {
      try {
         Response var2 = new Response("200 OK", (String)null, (InputStream)null);
         byte[] var3 = CommonUtils.randomData(64);
         if (".http-post".equals(var1)) {
            var3 = new byte[0];
         }

         this.c2profile.apply(var1 + ".server", var2, var3);
         (new WebTransforms(this.c2profile)).filterResponse(var2);
         StringBuffer var4 = new StringBuffer();
         var4.append("HTTP/1.1 " + var2.status + "\n");
         Iterator var5 = var2.header.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            String var7 = var6.getKey() + "";
            String var8 = var6.getValue() + "";
            var6.setValue(var8.replaceAll("\\P{Graph}", ""));
            var4.append(var7 + ": " + var8 + "\n");
         }

         byte[] var10 = new byte[0];
         if (var2.data != null) {
            var10 = new byte[var2.data.available()];
            var2.data.read(var10, 0, var10.length);
         }

         var4.append("\n" + CommonUtils.bString(var10).replaceAll("\\P{Print}", "."));
         return var4.toString();
      } catch (IOException var9) {
         MudgeSanity.logException("getServerSample: " + var1, var9, false);
         return "";
      }
   }
}
