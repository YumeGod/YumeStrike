package cortana;

import cortana.core.CommandManager;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import sleep.error.YourCodeSucksException;
import sleep.interfaces.Loadable;

public class ConsoleInterface {
   protected Cortana engine = null;
   protected CommandManager commands = null;

   public ConsoleInterface(Cortana var1) {
      this.engine = var1;
      this.commands = new CommandManager();
   }

   public Loadable getBridge() {
      return this.commands.getBridge();
   }

   public List commandList(String var1) {
      String[] var2 = var1.trim().split("\\s+");
      Cortana var10000;
      if (!"reload".equals(var2[0]) && !"pron".equals(var2[0]) && !"profile".equals(var2[0]) && !"proff".equals(var2[0]) && !"tron".equals(var2[0]) && !"unload".equals(var2[0]) && !"troff".equals(var2[0])) {
         if ("load".equals(var2[0]) && var1.length() > 5) {
            String var9 = var1.substring(5);
            File var10 = new File(var9);
            if (!var10.exists() || !var10.isDirectory()) {
               var10 = var10.getParentFile();
            }

            LinkedList var5 = new LinkedList();
            if (var10 == null) {
               var5.add(var1);
               return var5;
            } else {
               File[] var6 = var10.listFiles();

               for(int var7 = 0; var6 != null && var7 < var6.length; ++var7) {
                  if (var6[var7].isDirectory() || var6[var7].getName().endsWith(".cna")) {
                     var5.add(var2[0] + " " + var6[var7].getAbsolutePath());
                  }
               }

               var10000 = this.engine;
               Cortana.filterList(var5, var1);
               Collections.sort(var5);
               return var5;
            }
         } else {
            List var8 = this.commands.commandList(var1);
            var8.add("help");
            var8.add("ls");
            var8.add("reload");
            var8.add("unload");
            var8.add("load");
            var8.add("pron");
            var8.add("proff");
            var8.add("profile");
            var8.add("tron");
            var8.add("troff");
            Collections.sort(var8);
            var10000 = this.engine;
            Cortana.filterList(var8, var1);
            return var8;
         }
      } else {
         LinkedList var3 = new LinkedList();
         Iterator var4 = this.engine.scripts.keySet().iterator();

         while(var4.hasNext()) {
            var3.add(var2[0] + " " + (new File(var4.next() + "")).getName());
         }

         var10000 = this.engine;
         Cortana.filterList(var3, var1);
         Collections.sort(var3);
         return var3;
      }
   }

   public void processCommand(final String var1) {
      final String[] var2 = var1.trim().split("\\s+");
      HashSet var3 = new HashSet();
      var3.add("tron");
      var3.add("troff");
      var3.add("profile");
      var3.add("pron");
      var3.add("proff");
      HashSet var4 = new HashSet();
      var4.addAll(var3);
      var4.add("unload");
      var4.add("load");
      var4.add("reload");
      Iterator var5;
      if ("ls".equals(var1)) {
         this.engine.p("");
         this.engine.p("Scripts");
         this.engine.pdark("-------");
         var5 = this.engine.scripts.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (var6 != null) {
               File var7 = new File(var6);
               this.engine.p(var7.getName());
            }
         }

         this.engine.p("");
      } else if (var4.contains(var2[0]) && var2.length != 2) {
         this.engine.perror("Missing arguments");
      } else {
         String var12;
         if (var3.contains(var2[0]) && var2.length == 2) {
            var12 = this.engine.findScript(var2[1]);
            if (var12 == null) {
               this.engine.perror("Could not find '" + var2[1] + "'");
            } else {
               Loader var13 = (Loader)this.engine.scripts.get(var12);
               if ("tron".equals(var2[0])) {
                  this.engine.pgood("Tracing '" + var2[1] + "'");
                  var13.setDebugLevel(8);
               } else if ("troff".equals(var2[0])) {
                  this.engine.pgood("Stopped trace of '" + var2[1] + "'");
                  var13.unsetDebugLevel(8);
               } else if ("pron".equals(var2[0])) {
                  this.engine.pgood("Profiling '" + var2[1] + "'");
                  var13.setDebugLevel(24);
               } else if ("profile".equals(var2[0]) || "proff".equals(var2[0])) {
                  if ("proff".equals(var2[0])) {
                     this.engine.pgood("Stopped profile of '" + var2[1] + "'");
                     var13.unsetDebugLevel(24);
                  }

                  this.engine.p("");
                  this.engine.p("Profile " + var2[1]);
                  this.engine.pdark("-------");
                  var13.printProfile(this.engine.cortana_io.getOutputStream());
                  this.engine.p("");
               }
            }
         } else if ("unload".equals(var2[0]) && var2.length == 2) {
            var12 = this.engine.findScript(var2[1]);
            if (var12 == null) {
               this.engine.perror("Could not find '" + var2[1] + "'");
            } else {
               this.engine.pgood("Unload " + var12);
               this.engine.unloadScript(var12);
            }
         } else if ("load".equals(var2[0]) && var2.length == 2) {
            this.engine.pgood("Load " + var2[1]);

            try {
               this.engine.loadScript(var2[1]);
            } catch (YourCodeSucksException var10) {
               this.engine.p(var10.formatErrors());
            } catch (Exception var11) {
               this.engine.perror("Could not load: " + var11.getMessage());
            }
         } else if ("reload".equals(var2[0]) && var2.length == 2) {
            var12 = this.engine.findScript(var2[1]);
            if (var12 == null) {
               this.engine.perror("Could not find '" + var2[1] + "'");
            } else {
               this.engine.pgood("Reload " + var12);

               try {
                  this.engine.unloadScript(var12);
                  this.engine.loadScript(var12);
               } catch (IOException var8) {
                  this.engine.perror("Could not load: '" + var2[1] + "' " + var8.getMessage());
               } catch (YourCodeSucksException var9) {
                  this.engine.p(var9.formatErrors());
               }
            }
         } else if ("help".equals(var1)) {
            this.engine.p("");
            this.engine.p("Commands");
            this.engine.pdark("--------");
            var5 = this.commandList("").iterator();

            while(var5.hasNext()) {
               this.engine.p(var5.next() + "");
            }

            this.engine.p("");
         } else if (this.engine.getScriptableApplication().isHeadless()) {
            if (!this.commands.fireCommand(var2[0], var1)) {
               this.engine.perror("Command not found");
            }
         } else {
            (new Thread(new Runnable() {
               public void run() {
                  if (!ConsoleInterface.this.commands.fireCommand(var2[0], var1)) {
                     ConsoleInterface.this.engine.perror("Command not found");
                  }

               }
            }, "cortana command: " + var2[0])).start();
         }
      }

   }
}
