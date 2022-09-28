package beacon;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.BeaconEntry;
import common.StringStack;
import console.Console;
import console.GenericTabCompletion;
import cortana.Cortana;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SecureShellTabCompletion extends GenericTabCompletion {
   protected AggressorClient client;
   protected String bid;

   public SecureShellTabCompletion(String var1, AggressorClient var2, Console var3) {
      super(var3);
      this.client = var2;
      this.bid = var1;
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

   public String transformText(String var1) {
      return var1.replace(" ~", " " + System.getProperty("user.home"));
   }

   public Collection getOptionsFromList(String var1, List var2) {
      LinkedList var3 = new LinkedList();
      StringStack var4 = new StringStack(var1, " ");
      var4.pop();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         var3.add(var4.toString() + " " + var5.next());
      }

      Collections.sort(var3);
      filterList(var3, var1);
      return var3;
   }

   public Collection getOptions(String var1) {
      Object var2 = DataUtils.getSSHCommands(this.client.getData()).commands();
      ((List)var2).addAll(this.client.getSSHAliases().commands());
      Collections.sort((List)var2);
      Cortana.filterList((List)var2, var1);
      if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("upload ")) {
         String var10 = var1.substring(var1.indexOf(" ") + 1);
         File var11 = new File(var10);
         if (!var11.exists() || !var11.isDirectory()) {
            var11 = var11.getParentFile();
         }

         var2 = new LinkedList();
         if (var11 == null) {
            ((List)var2).add(var1);
            return (Collection)var2;
         }

         File[] var12 = var11.listFiles();

         for(int var6 = 0; var12 != null && var6 < var12.length; ++var6) {
            ((List)var2).add(var1.substring(0, var1.indexOf(" ")) + " " + var12[var6].getAbsolutePath());
         }

         Collections.sort((LinkedList)var2);
         filterList((LinkedList)var2, var1);
      } else {
         List var7;
         if (var2 != null && ((List)var2).size() == 0 && (var1.startsWith("help ") || var1.startsWith("? "))) {
            var7 = DataUtils.getSSHCommands(this.client.getData()).commands();
            return this.getOptionsFromList(var1, var7);
         }

         if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("note ")) {
            BeaconEntry var8 = DataUtils.getBeacon(this.client.getData(), this.bid);
            if (var8 != null) {
               LinkedList var9 = new LinkedList();
               var9.add(var8.getNote());
               return this.getOptionsFromList(var1, var9);
            }

            return this.getOptionsFromList(var1, new LinkedList());
         }

         if (var2 != null && ((List)var2).size() == 0 && var1.matches("connect .*? .*")) {
            var7 = DataUtils.getTCPPorts(this.client.getData());
            return this.getOptionsFromList(var1, var7);
         }

         if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("connect ")) {
            var7 = DataUtils.getTargetNames(this.client.getData());
            return this.getOptionsFromList(var1, var7);
         }

         if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("unlink ")) {
            LinkedList var3 = new LinkedList();
            Iterator var4 = DataUtils.getBeaconChildren(this.client.getData(), this.bid).iterator();

            while(var4.hasNext()) {
               BeaconEntry var5 = (BeaconEntry)var4.next();
               if (!var5.getPivotHint().isReverse()) {
                  var3.add(var5.getInternal() + " " + var5.getPid());
               }
            }

            return this.getOptionsFromList(var1, var3);
         }
      }

      return (Collection)var2;
   }
}
