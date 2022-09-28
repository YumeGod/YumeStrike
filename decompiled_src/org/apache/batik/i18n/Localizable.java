package org.apache.batik.i18n;

import java.util.Locale;
import java.util.MissingResourceException;

public interface Localizable {
   void setLocale(Locale var1);

   Locale getLocale();

   String formatMessage(String var1, Object[] var2) throws MissingResourceException;
}
