package org.apache.xmlgraphics.util.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizableSupport implements Localizable {
   protected LocaleGroup localeGroup;
   protected String bundleName;
   protected ClassLoader classLoader;
   protected Locale locale;
   protected Locale usedLocale;
   protected ResourceBundle resourceBundle;

   public LocalizableSupport(String s) {
      this(s, (ClassLoader)null);
   }

   public LocalizableSupport(String s, ClassLoader cl) {
      this.localeGroup = LocaleGroup.DEFAULT;
      this.bundleName = s;
      this.classLoader = cl;
   }

   public void setLocale(Locale l) {
      if (this.locale != l) {
         this.locale = l;
         this.resourceBundle = null;
      }

   }

   public Locale getLocale() {
      return this.locale;
   }

   public void setLocaleGroup(LocaleGroup lg) {
      this.localeGroup = lg;
   }

   public LocaleGroup getLocaleGroup() {
      return this.localeGroup;
   }

   public void setDefaultLocale(Locale l) {
      this.localeGroup.setLocale(l);
   }

   public Locale getDefaultLocale() {
      return this.localeGroup.getLocale();
   }

   public String formatMessage(String key, Object[] args) {
      this.getResourceBundle();
      return MessageFormat.format(this.resourceBundle.getString(key), args);
   }

   public ResourceBundle getResourceBundle() {
      Locale l;
      if (this.resourceBundle == null) {
         if (this.locale == null) {
            if ((l = this.localeGroup.getLocale()) == null) {
               this.usedLocale = Locale.getDefault();
            } else {
               this.usedLocale = l;
            }
         } else {
            this.usedLocale = this.locale;
         }

         if (this.classLoader == null) {
            this.resourceBundle = ResourceBundle.getBundle(this.bundleName, this.usedLocale);
         } else {
            this.resourceBundle = ResourceBundle.getBundle(this.bundleName, this.usedLocale, this.classLoader);
         }
      } else if (this.locale == null) {
         if ((l = this.localeGroup.getLocale()) == null) {
            if (this.usedLocale != (l = Locale.getDefault())) {
               this.usedLocale = l;
               if (this.classLoader == null) {
                  this.resourceBundle = ResourceBundle.getBundle(this.bundleName, this.usedLocale);
               } else {
                  this.resourceBundle = ResourceBundle.getBundle(this.bundleName, this.usedLocale, this.classLoader);
               }
            }
         } else if (this.usedLocale != l) {
            this.usedLocale = l;
            if (this.classLoader == null) {
               this.resourceBundle = ResourceBundle.getBundle(this.bundleName, this.usedLocale);
            } else {
               this.resourceBundle = ResourceBundle.getBundle(this.bundleName, this.usedLocale, this.classLoader);
            }
         }
      }

      return this.resourceBundle;
   }
}
