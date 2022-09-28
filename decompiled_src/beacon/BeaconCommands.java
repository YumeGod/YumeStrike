package beacon;

import common.CommandParser;
import common.CommonUtils;
import common.MudgeSanity;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BeaconCommands {
   public Map descriptions = new HashMap();
   public Map details = new HashMap();

   public BeaconCommands() {
      this.loadCommands();
      this.loadDetails();
   }

   public void register(String var1, String var2, String var3) {
      this.descriptions.put(var1, var2);
      this.details.put(var1, var3);
   }

   public String getCommandFile() {
      return "resources/bhelp.txt";
   }

   public String getDetailFile() {
      return "resources/bdetails.txt";
   }

   protected void loadCommands() {
      try {
         InputStream var1 = CommonUtils.resource(this.getCommandFile());
         byte[] var2 = CommonUtils.readAll(var1);
         var1.close();
         String[] var3 = CommonUtils.bString(var2).split("\n");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String[] var5 = var3[var4].split("\t+");
            if (var5.length == 2) {
               this.descriptions.put(var5[0], var5[1]);
            } else {
               CommonUtils.print_error("bhelp, line: " + var4 + " '" + var3[var4] + "'");
            }
         }
      } catch (IOException var6) {
         MudgeSanity.logException("Load Commands", var6, false);
      }

   }

   protected void loadDetails() {
      try {
         InputStream var1 = CommonUtils.resource(this.getDetailFile());
         byte[] var2 = CommonUtils.readAll(var1);
         var1.close();
         String[] var3 = CommonUtils.bString(var2).split("\n");
         String var4 = null;
         StringBuffer var5 = new StringBuffer();

         for(int var6 = 0; var6 < var3.length; ++var6) {
            CommandParser var7 = new CommandParser(var3[var6]);
            if (var7.is("beacon>")) {
               if (var7.verify("AZ")) {
                  if (var4 != null) {
                     this.details.put(var4, var5.toString().trim());
                  }

                  var4 = var7.popString();
                  var5 = new StringBuffer();
               }
            } else {
               var5.append(var3[var6] + "\n");
            }
         }

         this.details.put(var4, var5.toString().trim());
      } catch (IOException var8) {
         MudgeSanity.logException("Load Details", var8, false);
      }

   }

   public List commands() {
      synchronized(this) {
         return new LinkedList(this.descriptions.keySet());
      }
   }

   public String getDetails(String var1) {
      synchronized(this) {
         return this.details.get(var1) + "";
      }
   }

   public String getDescription(String var1) {
      synchronized(this) {
         return this.descriptions.get(var1) + "";
      }
   }

   public boolean isHelpAvailable(String var1) {
      synchronized(this) {
         return this.details.containsKey(var1);
      }
   }
}
