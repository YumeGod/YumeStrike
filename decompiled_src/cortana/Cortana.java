package cortana;

import common.AObject;
import common.CommonUtils;
import cortana.core.EventManager;
import cortana.core.FormatManager;
import cortana.gui.KeyBridge;
import cortana.gui.MenuBuilder;
import cortana.gui.ScriptableApplication;
import cortana.support.CortanaUtilities;
import cortana.support.Heartbeat;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.io.IOObject;
import sleep.error.RuntimeWarningWatcher;
import sleep.error.ScriptWarning;
import sleep.error.YourCodeSucksException;
import sleep.interfaces.Environment;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class Cortana extends AObject implements Loadable, RuntimeWarningWatcher, Function {
   protected IOObject cortana_io = null;
   protected CortanaPipe pipe = null;
   protected ScriptableApplication application = null;
   protected ConsoleInterface myinterface = null;
   protected EventManager events = new EventManager();
   protected MenuBuilder menus = null;
   protected FormatManager formats = new FormatManager();
   protected Loadable utils = new CortanaUtilities();
   protected Loadable keys = null;
   protected LinkedList bridges = new LinkedList();
   protected boolean active = true;
   protected Map scripts = new HashMap();

   public List getScripts() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.scripts.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (var3 != null) {
            File var4 = new File(var3);
            var1.add(var4.getName());
         }
      }

      return var1;
   }

   public void register(Loadable var1) {
      this.bridges.add(var1);
   }

   public ScriptableApplication getScriptableApplication() {
      return this.application;
   }

   public boolean isActive() {
      return this.active;
   }

   public void go() {
      (new Heartbeat(this)).start();
   }

   public Cortana(ScriptableApplication var1) {
      if (!var1.isHeadless()) {
         this.pipe = new CortanaPipe();
         this.cortana_io = new IOObject();
         this.cortana_io.openWrite(this.pipe.getOutput());
      }

      this.application = var1;
      this.myinterface = new ConsoleInterface(this);
      this.keys = new KeyBridge(this.application);
      this.menus = new MenuBuilder(this.application);
   }

   public String format(String var1, Stack var2) {
      return this.formats.format(var1, var2);
   }

   public static void put(ScriptInstance var0, String var1, Function var2) {
      var0.getScriptEnvironment().getEnvironment().put(var1, new SafeFunction(var2));
   }

   public static void putenv(ScriptInstance var0, String var1, Environment var2) {
      var0.getScriptEnvironment().getEnvironment().put(var1, new SafeEnvironment(var2));
   }

   public MenuBuilder getMenuBuilder() {
      return this.menus;
   }

   public EventManager getEventManager() {
      return this.events;
   }

   public void addTextListener(CortanaPipe.CortanaPipeListener var1) {
      this.pipe.addCortanaPipeListener(var1);
   }

   public void stop() {
      if (this.pipe != null) {
         this.pipe.close();
      }

      this.active = false;
   }

   public ConsoleInterface getConsoleInterface() {
      return this.myinterface;
   }

   public void scriptLoaded(ScriptInstance var1) {
      if (this.cortana_io != null) {
         IOObject.setConsole(var1.getScriptEnvironment(), this.cortana_io);
      }

      var1.getScriptEnvironment().getEnvironment().put("&script_load", this);
      var1.getScriptEnvironment().getEnvironment().put("&script_unload", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&script_load")) {
         try {
            this.loadScript(BridgeUtilities.getString(var3, ""));
         } catch (YourCodeSucksException var5) {
            throw new RuntimeException(var5.formatErrors());
         } catch (Exception var6) {
            throw new RuntimeException(var6);
         }
      } else if (var1.equals("&script_unload")) {
         String var4 = this.findScript(BridgeUtilities.getString(var3, ""));
         if (var4 == null) {
            throw new RuntimeException("Could not find script");
         }

         this.unloadScript(var4);
      }

      return SleepUtils.getEmptyScalar();
   }

   public void processScriptWarning(ScriptWarning var1) {
      String var2 = var1.getNameShort() + ":" + var1.getLineNumber();
      SimpleDateFormat var3 = new SimpleDateFormat("HH:mm:ss");
      Date var4 = new Date();
      String var5 = var3.format(var4, new StringBuffer(), new FieldPosition(0)).toString();
      if (var1.isDebugTrace()) {
         this.p("[" + var5 + "] Trace: " + var1.getMessage() + " at " + var2);
      } else {
         this.p("[" + var5 + "] " + var1.getMessage() + " at " + var2);
      }

   }

   public static void filterList(List var0, String var1) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         String var3 = var2.next() + "";
         if (!var3.startsWith(var1)) {
            var2.remove();
         }
      }

   }

   public String findScript(String var1) {
      Iterator var2 = this.scripts.keySet().iterator();

      String var3;
      File var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = var2.next().toString();
         var4 = new File(var3);
      } while(!var1.equals(var4.getName()));

      return var3;
   }

   public void unloadScript(String var1) {
      Loader var2 = (Loader)this.scripts.get(var1);
      if (var2 != null) {
         this.scripts.remove(var1);
         var2.unload();
      }
   }

   public void loadScript(String var1) throws YourCodeSucksException, IOException {
      this.loadScript(var1, (InputStream)null);
   }

   public void loadScript(String var1, InputStream var2) throws YourCodeSucksException, IOException {
      Loader var3 = new Loader(this);
      if (this.scripts.containsKey(var1)) {
         throw new RuntimeException(var1 + " is already loaded");
      } else {
         var3.getScriptLoader().addGlobalBridge(this.events.getBridge());
         var3.getScriptLoader().addGlobalBridge(this.formats.getBridge());
         var3.getScriptLoader().addGlobalBridge(this.myinterface.getBridge());
         var3.getScriptLoader().addGlobalBridge(this.utils);
         var3.getScriptLoader().addGlobalBridge(this);
         var3.getScriptLoader().addGlobalBridge(this.keys);
         var3.getScriptLoader().addGlobalBridge(this.menus.getBridge());
         Iterator var4 = this.bridges.iterator();

         while(var4.hasNext()) {
            Loadable var5 = (Loadable)var4.next();
            var3.getScriptLoader().addGlobalBridge(var5);
         }

         if (var2 != null) {
            var3.loadScript(var1, var2);
         } else {
            var3.loadScript(var1);
         }

         this.scripts.put(var1, var3);
      }
   }

   public void pgood(String var1) {
      if (this.application.isHeadless()) {
         CommonUtils.print_good(var1);
      } else {
         this.p("\u00039[+]\u000f " + var1);
      }

   }

   public void perror(String var1) {
      if (this.application.isHeadless()) {
         CommonUtils.print_error(var1);
      } else {
         this.p("\u00034[-]\u000f " + var1);
      }

   }

   public void pwarn(String var1) {
      if (this.application.isHeadless()) {
         CommonUtils.print_warn(var1);
      } else {
         this.p("\u00038[!]\u000f " + var1);
      }

   }

   public void pinfo(String var1) {
      if (this.application.isHeadless()) {
         CommonUtils.print_info(var1);
      } else {
         this.p("\u0003C[*]\u000f " + var1);
      }

   }

   public void pdark(String var1) {
      if (this.application.isHeadless()) {
         System.out.println("\u001b[01;30m" + var1 + "\u001b[0m");
      } else {
         this.p("\u0003E" + var1 + '\u000f');
      }

   }

   public void p(String var1) {
      if (this.cortana_io != null) {
         this.cortana_io.printLine(var1);
      } else {
         System.out.println(var1);
      }

   }
}
