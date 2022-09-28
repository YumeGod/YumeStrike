package org.apache.batik.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public interface ExtendedLocalizable extends Localizable {
   void setLocaleGroup(LocaleGroup var1);

   LocaleGroup getLocaleGroup();

   void setDefaultLocale(Locale var1);

   Locale getDefaultLocale();

   ResourceBundle getResourceBundle();
}
