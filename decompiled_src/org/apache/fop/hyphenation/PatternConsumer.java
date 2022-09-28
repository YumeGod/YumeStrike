package org.apache.fop.hyphenation;

import java.util.ArrayList;

public interface PatternConsumer {
   void addClass(String var1);

   void addException(String var1, ArrayList var2);

   void addPattern(String var1, String var2);
}
