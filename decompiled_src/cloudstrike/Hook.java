package cloudstrike;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Hook implements WebService {
   protected String content_type;
   protected String type;
   protected String desc;
   protected MyHook hook = null;
   protected boolean suppress;

   public Hook(String content_type, String type, String desc, boolean suppress) {
      this.content_type = content_type;
      this.type = type;
      this.desc = desc;
      this.suppress = suppress;
   }

   public void setup(WebServer w, String uri, MyHook hook) {
      this.hook = hook;
      w.register(uri, this);
   }

   public void setup(WebServer w, String uri) {
      throw new RuntimeException("Need one more argument for setup");
   }

   public void bind(WebServer w, String uri) {
      String[] uriz = uri.split(",");

      for(int x = 0; x < uriz.length; ++x) {
         w.registerSecondary(uriz[x], this);
      }

   }

   public Response serve(String uri, String method, Properties header, Properties param) {
      try {
         return new Response("200 OK", this.content_type, new ByteArrayInputStream(this.hook.serve(uri, method, header, param)));
      } catch (Exception var6) {
         WebServer.logException("Error while serving URI: '" + uri + "'", var6, false);
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

   public boolean suppressEvent(String uri) {
      return this.suppress;
   }

   public boolean isFuzzy() {
      return false;
   }

   public interface MyHook {
      byte[] serve(String var1, String var2, Properties var3, Properties var4);
   }
}
