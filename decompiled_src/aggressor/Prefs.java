package aggressor;

import common.CommonUtils;
import common.MudgeSanity;
import common.RangeList;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Prefs {
   protected static final Prefs prefs = new Prefs();
   protected Properties data = null;

   protected File myFile() {
      return new File(System.getProperty("user.home"), ".aggressor.prop");
   }

   protected Prefs() {
   }

   public void load() {
      if (this.data == null) {
         File var1 = this.myFile();

         try {
            this.data = new Properties();
            Object var2 = null;
            if (var1.exists()) {
               var2 = new FileInputStream(var1);
            } else {
               var2 = CommonUtils.resource("resources/aggressor.prop");
            }

            this.data.load((InputStream)var2);
            ((InputStream)var2).close();
         } catch (IOException var3) {
            MudgeSanity.logException("Load Preferences: " + var1, var3, false);
         }

      }
   }

   public void scrub() {
      try {
         LinkedList var1 = new LinkedList(this.getList("trusted.servers"));
         if (var1.size() > 100) {
            while(true) {
               if (var1.size() <= 50) {
                  this.setList("trusted.servers", var1);
                  break;
               }

               var1.removeFirst();
            }
         }

         LinkedHashSet var2 = new LinkedHashSet(this.getList("connection.profiles"));
         Iterator var3 = this.data.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            if (var4.getKey().toString().startsWith("connection.profiles.")) {
               String var5 = var4.getKey().toString().substring("connection.profiles.".length());
               var5 = var5.substring(0, var5.lastIndexOf("."));
               if (!var2.contains(var5)) {
                  var3.remove();
               }
            }
         }
      } catch (Exception var6) {
         MudgeSanity.logException("scrub preferences", var6, false);
      }

   }

   public void save() {
      this.scrub();
      File var1 = this.myFile();

      try {
         FileOutputStream var2 = new FileOutputStream(var1);
         this.data.store(var2, "Cobalt Strike (Aggressor) Configuration");
         var2.close();
      } catch (IOException var3) {
         MudgeSanity.logException("Save Preferences: " + var1, var3, false);
      }

   }

   public String getString(String var1, String var2) {
      return this.data.getProperty(var1, var2);
   }

   public boolean isSet(String var1, boolean var2) {
      return "true".equals(this.getString(var1, var2 + ""));
   }

   public long getLongNumber(String var1, long var2) {
      return CommonUtils.toLongNumber(this.getString(var1, var2 + ""), var2);
   }

   public int getRandomPort(String var1, String var2) {
      String var3 = this.getString(var1, var2);
      if ("".equals(var3)) {
         var3 = var2;
      }

      RangeList var4 = new RangeList(var3);
      return var4.random();
   }

   public Color getColor(String var1, String var2) {
      return Color.decode(this.getString(var1, var2));
   }

   public Font getFont(String var1, String var2) {
      return Font.decode(this.getString(var1, var2));
   }

   public List getList(String var1) {
      String var2 = this.getString(var1, "");
      return (List)("".equals(var2) ? new LinkedList() : CommonUtils.toList((Object[])var2.split("!!")));
   }

   public void appendList(String var1, String var2) {
      List var3 = this.getList(var1);
      var3.add(var2);
      this.setList(var1, new LinkedList(new LinkedHashSet(var3)));
   }

   public void setList(String var1, List var2) {
      LinkedList var5 = new LinkedList(var2);
      Iterator var3 = var5.iterator();

      while(true) {
         String var4;
         do {
            if (!var3.hasNext()) {
               this.set(var1, CommonUtils.join((Collection)var5, (String)"!!"));
               return;
            }

            var4 = (String)var3.next();
         } while(var4 != null && !"".equals(var4));

         var3.remove();
      }
   }

   public void set(String var1, String var2) {
      this.data.setProperty(var1, var2);
   }

   public void update(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         String var4 = (String)var3.getKey();
         String var5 = (String)var3.getValue();
         this.data.setProperty(var4, var5);
      }

      this.save();
   }

   public Map copy() {
      return new HashMap(this.data);
   }

   public static Prefs getPreferences() {
      prefs.load();
      return prefs;
   }
}
