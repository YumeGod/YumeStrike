package org.xml.sax.ext;

import org.xml.sax.Attributes;

public interface Attributes2 extends Attributes {
   boolean isDeclared(int var1);

   boolean isDeclared(String var1);

   boolean isDeclared(String var1, String var2);

   boolean isSpecified(int var1);

   boolean isSpecified(String var1, String var2);

   boolean isSpecified(String var1);
}
