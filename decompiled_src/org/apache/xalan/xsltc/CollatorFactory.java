package org.apache.xalan.xsltc;

import java.text.Collator;
import java.util.Locale;

public interface CollatorFactory {
   Collator getCollator(String var1, String var2);

   Collator getCollator(Locale var1);
}
