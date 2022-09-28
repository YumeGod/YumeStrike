package org.apache.batik.i18n;

import java.util.Locale;

public class LocaleGroup {
   public static final LocaleGroup DEFAULT = new LocaleGroup();
   protected Locale locale;

   public void setLocale(Locale var1) {
      this.locale = var1;
   }

   public Locale getLocale() {
      return this.locale;
   }
}
