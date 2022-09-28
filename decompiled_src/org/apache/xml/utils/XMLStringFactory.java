package org.apache.xml.utils;

public abstract class XMLStringFactory {
   public abstract XMLString newstr(String var1);

   public abstract XMLString newstr(FastStringBuffer var1, int var2, int var3);

   public abstract XMLString newstr(char[] var1, int var2, int var3);

   public abstract XMLString emptystr();
}
