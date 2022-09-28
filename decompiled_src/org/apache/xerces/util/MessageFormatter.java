package org.apache.xerces.util;

import java.util.Locale;
import java.util.MissingResourceException;

public interface MessageFormatter {
   String formatMessage(Locale var1, String var2, Object[] var3) throws MissingResourceException;
}
