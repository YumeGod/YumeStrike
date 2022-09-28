package org.apache.batik.util.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class ResourceManager {
   protected ResourceBundle bundle;

   public ResourceManager(ResourceBundle var1) {
      this.bundle = var1;
   }

   public String getString(String var1) throws MissingResourceException {
      return this.bundle.getString(var1);
   }

   public List getStringList(String var1) throws MissingResourceException {
      return this.getStringList(var1, " \t\n\r\f", false);
   }

   public List getStringList(String var1, String var2) throws MissingResourceException {
      return this.getStringList(var1, var2, false);
   }

   public List getStringList(String var1, String var2, boolean var3) throws MissingResourceException {
      ArrayList var4 = new ArrayList();
      StringTokenizer var5 = new StringTokenizer(this.getString(var1), var2, var3);

      while(var5.hasMoreTokens()) {
         var4.add(var5.nextToken());
      }

      return var4;
   }

   public boolean getBoolean(String var1) throws MissingResourceException, ResourceFormatException {
      String var2 = this.getString(var1);
      if (var2.equals("true")) {
         return true;
      } else if (var2.equals("false")) {
         return false;
      } else {
         throw new ResourceFormatException("Malformed boolean", this.bundle.getClass().getName(), var1);
      }
   }

   public int getInteger(String var1) throws MissingResourceException, ResourceFormatException {
      String var2 = this.getString(var1);

      try {
         return Integer.parseInt(var2);
      } catch (NumberFormatException var4) {
         throw new ResourceFormatException("Malformed integer", this.bundle.getClass().getName(), var1);
      }
   }

   public int getCharacter(String var1) throws MissingResourceException, ResourceFormatException {
      String var2 = this.getString(var1);
      if (var2 != null && var2.length() != 0) {
         return var2.charAt(0);
      } else {
         throw new ResourceFormatException("Malformed character", this.bundle.getClass().getName(), var1);
      }
   }
}
