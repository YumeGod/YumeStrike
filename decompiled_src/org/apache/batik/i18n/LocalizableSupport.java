package org.apache.batik.i18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocalizableSupport implements Localizable {
   protected LocaleGroup localeGroup;
   protected String bundleName;
   protected ClassLoader classLoader;
   protected Locale locale;
   protected Locale usedLocale;
   List resourceBundles;
   Class lastResourceClass;
   Class cls;
   // $FF: synthetic field
   static Class class$java$lang$Object;

   public LocalizableSupport(String var1, Class var2) {
      this(var1, var2, (ClassLoader)null);
   }

   public LocalizableSupport(String var1, Class var2, ClassLoader var3) {
      this.localeGroup = LocaleGroup.DEFAULT;
      this.resourceBundles = new ArrayList();
      this.bundleName = var1;
      this.cls = var2;
      this.classLoader = var3;
   }

   public LocalizableSupport(String var1) {
      this(var1, (ClassLoader)null);
   }

   public LocalizableSupport(String var1, ClassLoader var2) {
      this.localeGroup = LocaleGroup.DEFAULT;
      this.resourceBundles = new ArrayList();
      this.bundleName = var1;
      this.classLoader = var2;
   }

   public void setLocale(Locale var1) {
      if (this.locale != var1) {
         this.locale = var1;
         this.resourceBundles.clear();
         this.lastResourceClass = null;
      }

   }

   public Locale getLocale() {
      return this.locale;
   }

   public void setLocaleGroup(LocaleGroup var1) {
      this.localeGroup = var1;
   }

   public LocaleGroup getLocaleGroup() {
      return this.localeGroup;
   }

   public void setDefaultLocale(Locale var1) {
      this.localeGroup.setLocale(var1);
   }

   public Locale getDefaultLocale() {
      return this.localeGroup.getLocale();
   }

   public String formatMessage(String var1, Object[] var2) {
      return MessageFormat.format(this.getString(var1), var2);
   }

   protected Locale getCurrentLocale() {
      if (this.locale != null) {
         return this.locale;
      } else {
         Locale var1 = this.localeGroup.getLocale();
         return var1 != null ? var1 : Locale.getDefault();
      }
   }

   protected boolean setUsedLocale() {
      Locale var1 = this.getCurrentLocale();
      if (this.usedLocale == var1) {
         return false;
      } else {
         this.usedLocale = var1;
         this.resourceBundles.clear();
         this.lastResourceClass = null;
         return true;
      }
   }

   public ResourceBundle getResourceBundle() {
      return this.getResourceBundle(0);
   }

   protected boolean hasNextResourceBundle(int var1) {
      if (var1 == 0) {
         return true;
      } else if (var1 < this.resourceBundles.size()) {
         return true;
      } else if (this.lastResourceClass == null) {
         return false;
      } else {
         return this.lastResourceClass != (class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
      }
   }

   protected ResourceBundle lookupResourceBundle(String var1, Class var2) {
      ClassLoader var3 = this.classLoader;
      ResourceBundle var4 = null;
      if (var3 != null) {
         try {
            var4 = ResourceBundle.getBundle(var1, this.usedLocale, var3);
         } catch (MissingResourceException var8) {
         }

         if (var4 != null) {
            return var4;
         }
      }

      if (var2 != null) {
         try {
            var3 = var2.getClassLoader();
         } catch (SecurityException var7) {
         }
      }

      if (var3 == null) {
         var3 = this.getClass().getClassLoader();
      }

      try {
         var4 = ResourceBundle.getBundle(var1, this.usedLocale, var3);
      } catch (MissingResourceException var6) {
      }

      return var4;
   }

   protected ResourceBundle getResourceBundle(int var1) {
      this.setUsedLocale();
      ResourceBundle var2 = null;
      if (this.cls == null) {
         if (this.resourceBundles.size() == 0) {
            var2 = this.lookupResourceBundle(this.bundleName, (Class)null);
            this.resourceBundles.add(var2);
         }

         return (ResourceBundle)this.resourceBundles.get(0);
      } else {
         while(var1 >= this.resourceBundles.size()) {
            if (this.lastResourceClass == (class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object)) {
               return null;
            }

            if (this.lastResourceClass == null) {
               this.lastResourceClass = this.cls;
            } else {
               this.lastResourceClass = this.lastResourceClass.getSuperclass();
            }

            Class var3 = this.lastResourceClass;
            String var4 = var3.getPackage().getName() + "." + this.bundleName;
            this.resourceBundles.add(this.lookupResourceBundle(var4, var3));
         }

         return (ResourceBundle)this.resourceBundles.get(var1);
      }
   }

   public String getString(String var1) throws MissingResourceException {
      this.setUsedLocale();

      for(int var2 = 0; this.hasNextResourceBundle(var2); ++var2) {
         ResourceBundle var3 = this.getResourceBundle(var2);
         if (var3 != null) {
            try {
               String var4 = var3.getString(var1);
               if (var4 != null) {
                  return var4;
               }
            } catch (MissingResourceException var5) {
            }
         }
      }

      String var6 = this.cls != null ? this.cls.toString() : this.bundleName;
      throw new MissingResourceException("Unable to find resource: " + var1, var6, var1);
   }

   public int getInteger(String var1) throws MissingResourceException {
      String var2 = this.getString(var1);

      try {
         return Integer.parseInt(var2);
      } catch (NumberFormatException var4) {
         throw new MissingResourceException("Malformed integer", this.bundleName, var1);
      }
   }

   public int getCharacter(String var1) throws MissingResourceException {
      String var2 = this.getString(var1);
      if (var2 != null && var2.length() != 0) {
         return var2.charAt(0);
      } else {
         throw new MissingResourceException("Malformed character", this.bundleName, var1);
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
