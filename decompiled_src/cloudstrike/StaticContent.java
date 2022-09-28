package cloudstrike;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class StaticContent implements WebService {
   protected String content;
   protected String type;
   protected String desc;

   public StaticContent(String content, String type, String desc) {
      this.content = content;
      this.type = type;
      this.desc = desc;
   }

   public void setup(WebServer w, String uri) {
      w.register(uri, this);
   }

   public Response serve(String uri, String method, Properties header, Properties param) {
      return new Response("200 OK", this.type, this.content.replace("%TOKEN%", param.get("id") + ""));
   }

   public String toString() {
      return this.desc;
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
