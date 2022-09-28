package org.apache.xmlgraphics.util.i18n;

import java.util.Locale;

public class LocaleGroup {
   public static final LocaleGroup DEFAULT = new LocaleGroup();
   protected Locale locale;

   public void setLocale(Locale l) {
      this.locale = l;
   }

   public Locale getLocale() {
      return this.locale;
   }
}
