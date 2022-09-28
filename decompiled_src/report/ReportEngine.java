package report;

import aggressor.AggressorClient;
import aggressor.Prefs;
import aggressor.bridges.AggregateBridge;
import aggressor.bridges.AttackBridge;
import common.CommonUtils;
import common.MudgeSanity;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import sleep.error.YourCodeSucksException;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptLoader;

public class ReportEngine {
   protected AggressorClient client;
   protected ReportBridge bridge;
   protected LinkedList reportassets = new LinkedList();

   public ReportEngine(AggressorClient var1) {
      this.client = var1;
      this.bridge = new ReportBridge();
   }

   public List reportTitles() {
      this.rehash();
      List var1 = this.bridge.reportTitles();
      Collections.sort(var1);
      return var1;
   }

   public String describe(String var1) {
      return this.bridge.describe(var1);
   }

   public Document buildReport(String var1, String var2, Stack var3) {
      return this.bridge.buildReport(var1, var2, var3);
   }

   public void load(String var1, InputStream var2) {
      new Hashtable();
      ScriptLoader var4 = new ScriptLoader();

      try {
         var4.addGlobalBridge(this.bridge);
         var4.addGlobalBridge(this.client.getScriptEngine());
         var4.addGlobalBridge(new AggregateBridge(this.client));
         var4.addGlobalBridge(new AttackBridge());
         ScriptInstance var5 = var4.loadScript(var1, var2);
         var5.addWarningWatcher(this.client.getScriptEngine());
         var5.runScript();
      } catch (YourCodeSucksException var6) {
         CommonUtils.print_error("Could not load: " + var1 + " (syntax errors; go to View -> Script Console)");
         this.client.getScriptEngine().perror("Could not load " + var1 + ":\n" + var6.formatErrors());
      } catch (Exception var7) {
         this.client.getScriptEngine().perror("Could not load " + var1 + ": " + var7.getMessage());
         MudgeSanity.logException("Could not load:" + var1, var7, false);
      }

   }

   public void registerInternal(String var1) {
      this.reportassets.add(var1);
      this.rehash();
   }

   public void rehash() {
      this.bridge = new ReportBridge();
      Iterator var1 = this.reportassets.iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();

         try {
            this.load(var2, CommonUtils.resource(var2));
         } catch (Exception var6) {
            MudgeSanity.logException("asset: " + var2, var6, false);
         }
      }

      Iterator var7 = Prefs.getPreferences().getList("reporting.custom_reports").iterator();

      while(var7.hasNext()) {
         String var3 = (String)var7.next();

         try {
            this.load(var3, new FileInputStream(var3));
         } catch (Exception var5) {
            MudgeSanity.logException("file: " + var3, var5, false);
         }
      }

   }
}
