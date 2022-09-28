package org.apache.xerces.util;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.parser.XMLInputSource;

public final class HTTPInputSource extends XMLInputSource {
   protected boolean fFollowRedirects = true;
   protected Map fHTTPRequestProperties = new HashMap();

   public HTTPInputSource(String var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   public HTTPInputSource(XMLResourceIdentifier var1) {
      super(var1);
   }

   public HTTPInputSource(String var1, String var2, String var3, InputStream var4, String var5) {
      super(var1, var2, var3, var4, var5);
   }

   public HTTPInputSource(String var1, String var2, String var3, Reader var4, String var5) {
      super(var1, var2, var3, var4, var5);
   }

   public boolean getFollowHTTPRedirects() {
      return this.fFollowRedirects;
   }

   public void setFollowHTTPRedirects(boolean var1) {
      this.fFollowRedirects = var1;
   }

   public String getHTTPRequestProperty(String var1) {
      return (String)this.fHTTPRequestProperties.get(var1);
   }

   public Iterator getHTTPRequestProperties() {
      return this.fHTTPRequestProperties.entrySet().iterator();
   }

   public void setHTTPRequestProperty(String var1, String var2) {
      if (var2 != null) {
         this.fHTTPRequestProperties.put(var1, var2);
      } else {
         this.fHTTPRequestProperties.remove(var1);
      }

   }
}
