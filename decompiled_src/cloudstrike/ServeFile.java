package cloudstrike;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ServeFile implements WebService {
   protected String mimetype;
   protected File resource;

   public ServeFile(File resource, String mimetype) {
      this.resource = resource;
      this.mimetype = mimetype;
   }

   public void setup(WebServer w, String uri) {
      w.register(uri, this);
   }

   public Response serve(String uri, String method, Properties header, Properties param) {
      try {
         return new Response("200 OK", this.mimetype, new FileInputStream(this.resource), this.resource.length());
      } catch (IOException var6) {
         WebServer.logException("Could not serve: '" + this.resource + "'", var6, false);
         return new Response("404 Not Found", "text/plain", "");
      }
   }

   public String toString() {
      return "Serves " + this.resource;
   }

   public String getType() {
      return "page";
   }

   public List cleanupJobs() {
      return new LinkedList();
   }

   public boolean suppressEvent(String uri) {
      return false;
   }

   public boolean isFuzzy() {
      return false;
   }
}
