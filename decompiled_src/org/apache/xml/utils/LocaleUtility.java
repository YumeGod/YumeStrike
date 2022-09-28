package org.apache.xml.utils;

import java.util.Locale;

public class LocaleUtility {
   public static final char IETF_SEPARATOR = '-';
   public static final String EMPTY_STRING = "";

   public static Locale langToLocale(String lang) {
      if (lang != null && !lang.equals("")) {
         String language = "";
         String country = "";
         String variant = "";
         int i1 = lang.indexOf(45);
         if (i1 < 0) {
            language = lang;
         } else {
            language = lang.substring(0, i1);
            ++i1;
            int i2 = lang.indexOf(45, i1);
            if (i2 < 0) {
               country = lang.substring(i1);
            } else {
               country = lang.substring(i1, i2);
               variant = lang.substring(i2 + 1);
            }
         }

         if (language.length() == 2) {
            language = language.toLowerCase();
         } else {
            language = "";
         }

         if (country.length() == 2) {
            country = country.toUpperCase();
         } else {
            country = "";
         }

         if (variant.length() <= 0 || language.length() != 2 && country.length() != 2) {
            variant = "";
         } else {
            variant = variant.toUpperCase();
         }

         return new Locale(language, country, variant);
      } else {
         return Locale.getDefault();
      }
   }
}
