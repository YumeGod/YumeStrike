package org.apache.batik.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.Version;

public class ParsedURL {
   ParsedURLData data;
   String userAgent;
   private static Map handlersMap = null;
   private static ParsedURLProtocolHandler defaultHandler = new ParsedURLDefaultProtocolHandler();
   private static String globalUserAgent = "Batik/" + Version.getVersion();
   // $FF: synthetic field
   static Class class$org$apache$batik$util$ParsedURLProtocolHandler;

   public static String getGlobalUserAgent() {
      return globalUserAgent;
   }

   public static void setGlobalUserAgent(String var0) {
      globalUserAgent = var0;
   }

   private static synchronized Map getHandlersMap() {
      if (handlersMap != null) {
         return handlersMap;
      } else {
         handlersMap = new HashMap();
         registerHandler(new ParsedURLDataProtocolHandler());
         registerHandler(new ParsedURLJarProtocolHandler());
         Iterator var0 = Service.providers(class$org$apache$batik$util$ParsedURLProtocolHandler == null ? (class$org$apache$batik$util$ParsedURLProtocolHandler = class$("org.apache.batik.util.ParsedURLProtocolHandler")) : class$org$apache$batik$util$ParsedURLProtocolHandler);

         while(var0.hasNext()) {
            ParsedURLProtocolHandler var1 = (ParsedURLProtocolHandler)var0.next();
            registerHandler(var1);
         }

         return handlersMap;
      }
   }

   public static synchronized ParsedURLProtocolHandler getHandler(String var0) {
      if (var0 == null) {
         return defaultHandler;
      } else {
         Map var1 = getHandlersMap();
         ParsedURLProtocolHandler var2 = (ParsedURLProtocolHandler)var1.get(var0);
         if (var2 == null) {
            var2 = defaultHandler;
         }

         return var2;
      }
   }

   public static synchronized void registerHandler(ParsedURLProtocolHandler var0) {
      if (var0.getProtocolHandled() == null) {
         defaultHandler = var0;
      } else {
         Map var1 = getHandlersMap();
         var1.put(var0.getProtocolHandled(), var0);
      }
   }

   public static InputStream checkGZIP(InputStream var0) throws IOException {
      return ParsedURLData.checkGZIP(var0);
   }

   public ParsedURL(String var1) {
      this.userAgent = getGlobalUserAgent();
      this.data = parseURL(var1);
   }

   public ParsedURL(URL var1) {
      this.userAgent = getGlobalUserAgent();
      this.data = new ParsedURLData(var1);
   }

   public ParsedURL(String var1, String var2) {
      this.userAgent = getGlobalUserAgent();
      if (var1 != null) {
         this.data = parseURL(var1, var2);
      } else {
         this.data = parseURL(var2);
      }

   }

   public ParsedURL(URL var1, String var2) {
      this.userAgent = getGlobalUserAgent();
      if (var1 != null) {
         this.data = parseURL(new ParsedURL(var1), var2);
      } else {
         this.data = parseURL(var2);
      }

   }

   public ParsedURL(ParsedURL var1, String var2) {
      if (var1 != null) {
         this.userAgent = var1.getUserAgent();
         this.data = parseURL(var1, var2);
      } else {
         this.data = parseURL(var2);
      }

   }

   public String toString() {
      return this.data.toString();
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof ParsedURL)) {
         return false;
      } else {
         ParsedURL var2 = (ParsedURL)var1;
         return this.data.equals(var2.data);
      }
   }

   public int hashCode() {
      return this.data.hashCode();
   }

   public boolean complete() {
      return this.data.complete();
   }

   public String getUserAgent() {
      return this.userAgent;
   }

   public void setUserAgent(String var1) {
      this.userAgent = var1;
   }

   public String getProtocol() {
      return this.data.protocol == null ? null : this.data.protocol;
   }

   public String getHost() {
      return this.data.host == null ? null : this.data.host;
   }

   public int getPort() {
      return this.data.port;
   }

   public String getPath() {
      return this.data.path == null ? null : this.data.path;
   }

   public String getRef() {
      return this.data.ref == null ? null : this.data.ref;
   }

   public String getPortStr() {
      return this.data.getPortStr();
   }

   public String getContentType() {
      return this.data.getContentType(this.userAgent);
   }

   public String getContentTypeMediaType() {
      return this.data.getContentTypeMediaType(this.userAgent);
   }

   public String getContentTypeCharset() {
      return this.data.getContentTypeCharset(this.userAgent);
   }

   public boolean hasContentTypeParameter(String var1) {
      return this.data.hasContentTypeParameter(this.userAgent, var1);
   }

   public String getContentEncoding() {
      return this.data.getContentEncoding(this.userAgent);
   }

   public InputStream openStream() throws IOException {
      return this.data.openStream(this.userAgent, (Iterator)null);
   }

   public InputStream openStream(String var1) throws IOException {
      ArrayList var2 = new ArrayList(1);
      var2.add(var1);
      return this.data.openStream(this.userAgent, var2.iterator());
   }

   public InputStream openStream(String[] var1) throws IOException {
      ArrayList var2 = new ArrayList(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.add(var1[var3]);
      }

      return this.data.openStream(this.userAgent, var2.iterator());
   }

   public InputStream openStream(Iterator var1) throws IOException {
      return this.data.openStream(this.userAgent, var1);
   }

   public InputStream openStreamRaw() throws IOException {
      return this.data.openStreamRaw(this.userAgent, (Iterator)null);
   }

   public InputStream openStreamRaw(String var1) throws IOException {
      ArrayList var2 = new ArrayList(1);
      var2.add(var1);
      return this.data.openStreamRaw(this.userAgent, var2.iterator());
   }

   public InputStream openStreamRaw(String[] var1) throws IOException {
      ArrayList var2 = new ArrayList(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.add(var1[var3]);
      }

      return this.data.openStreamRaw(this.userAgent, var2.iterator());
   }

   public InputStream openStreamRaw(Iterator var1) throws IOException {
      return this.data.openStreamRaw(this.userAgent, var1);
   }

   public boolean sameFile(ParsedURL var1) {
      return this.data.sameFile(var1.data);
   }

   protected static String getProtocol(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = 0;
         int var2 = var0.length();
         if (var2 == 0) {
            return null;
         } else {
            char var3;
            for(var3 = var0.charAt(var1); var3 == '-' || var3 == '+' || var3 == '.' || var3 >= 'a' && var3 <= 'z' || var3 >= 'A' && var3 <= 'Z'; var3 = var0.charAt(var1)) {
               ++var1;
               if (var1 == var2) {
                  var3 = 0;
                  break;
               }
            }

            return var3 == ':' ? var0.substring(0, var1).toLowerCase() : null;
         }
      }
   }

   public static ParsedURLData parseURL(String var0) {
      ParsedURLProtocolHandler var1 = getHandler(getProtocol(var0));
      return var1.parseURL(var0);
   }

   public static ParsedURLData parseURL(String var0, String var1) {
      if (var0 == null) {
         return parseURL(var1);
      } else {
         ParsedURL var2 = new ParsedURL(var0);
         return parseURL(var2, var1);
      }
   }

   public static ParsedURLData parseURL(ParsedURL var0, String var1) {
      if (var0 == null) {
         return parseURL(var1);
      } else {
         String var2 = getProtocol(var1);
         if (var2 == null) {
            var2 = var0.getProtocol();
         }

         ParsedURLProtocolHandler var3 = getHandler(var2);
         return var3.parseURL(var0, var1);
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
