package org.apache.batik.util;

import java.net.MalformedURLException;
import java.net.URL;

public class ParsedURLJarProtocolHandler extends ParsedURLDefaultProtocolHandler {
   public static final String JAR = "jar";

   public ParsedURLJarProtocolHandler() {
      super("jar");
   }

   public ParsedURLData parseURL(ParsedURL var1, String var2) {
      String var3 = var2.substring(0, "jar".length() + 1).toLowerCase();
      if (var3.equals("jar:")) {
         return this.parseURL(var2);
      } else {
         try {
            URL var4 = new URL(var1.toString());
            URL var5 = new URL(var4, var2);
            return this.constructParsedURLData(var5);
         } catch (MalformedURLException var6) {
            return super.parseURL(var1, var2);
         }
      }
   }
}
