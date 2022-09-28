package c2profile;

import cloudstrike.Response;
import cloudstrike.WebServer;
import cloudstrike.WebService;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class MalleableHook implements WebService {
   protected MyHook hook = null;
   protected Profile profile;
   protected String desc = "";
   protected String type = "";
   protected String key = "";

   public MalleableHook(Profile var1, String var2, String var3) {
      this.profile = var1;
      this.type = var2;
      this.desc = var3;
   }

   public void setup(WebServer var1, String var2, MyHook var3) {
      this.hook = var3;
      this.key = var2;
      var1.register("beacon" + var2, this);
      String[] var4 = this.profile.getString(var2 + ".uri").split(" ");

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var1.registerSecondary(var4[var5], this);
         var1.setSpecialPostURI(var4[var5]);
      }

   }

   public void setup(WebServer var1, String var2) {
      throw new RuntimeException("Missing arguments");
   }

   public Response serve(String var1, String var2, Properties var3, Properties var4) {
      try {
         Response var5 = new Response("200 OK", (String)null, (InputStream)null);
         byte[] var6 = this.hook.serve(var1, var2, var3, var4);
         this.profile.apply(this.key + ".server", var5, var6);
         return var5;
      } catch (Exception var7) {
         var7.printStackTrace();
         return new Response("500 Internal Server Error", "text/plain", "Oops... something went wrong");
      }
   }

   public String toString() {
      return this.desc;
   }

   public String getType() {
      return this.type;
   }

   public List cleanupJobs() {
      return new LinkedList();
   }

   public boolean suppressEvent(String var1) {
      return true;
   }

   public boolean isFuzzy() {
      return true;
   }

   public interface MyHook {
      byte[] serve(String var1, String var2, Properties var3, Properties var4);
   }
}
