package org.apache.batik.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

public class ParsedURLData {
   protected static final String HTTP_USER_AGENT_HEADER = "User-Agent";
   protected static final String HTTP_ACCEPT_HEADER = "Accept";
   protected static final String HTTP_ACCEPT_LANGUAGE_HEADER = "Accept-Language";
   protected static final String HTTP_ACCEPT_ENCODING_HEADER = "Accept-Encoding";
   protected static List acceptedEncodings = new LinkedList();
   public static final byte[] GZIP_MAGIC;
   public String protocol = null;
   public String host = null;
   public int port = -1;
   public String path = null;
   public String ref = null;
   public String contentType = null;
   public String contentEncoding = null;
   public InputStream stream = null;
   public boolean hasBeenOpened = false;
   protected String contentTypeMediaType;
   protected String contentTypeCharset;

   public static InputStream checkGZIP(InputStream var0) throws IOException {
      if (!((InputStream)var0).markSupported()) {
         var0 = new BufferedInputStream((InputStream)var0);
      }

      byte[] var1 = new byte[2];

      try {
         ((InputStream)var0).mark(2);
         ((InputStream)var0).read(var1);
         ((InputStream)var0).reset();
      } catch (Exception var5) {
         ((InputStream)var0).reset();
         return (InputStream)var0;
      }

      if (var1[0] == GZIP_MAGIC[0] && var1[1] == GZIP_MAGIC[1]) {
         return new GZIPInputStream((InputStream)var0);
      } else {
         if ((var1[0] & 15) == 8 && var1[0] >>> 4 <= 7) {
            int var2 = (var1[0] & 255) * 256 + (var1[1] & 255);
            if (var2 % 31 == 0) {
               try {
                  ((InputStream)var0).mark(100);
                  Object var3 = new InflaterInputStream((InputStream)var0);
                  if (!((InputStream)var3).markSupported()) {
                     var3 = new BufferedInputStream((InputStream)var3);
                  }

                  ((InputStream)var3).mark(2);
                  ((InputStream)var3).read(var1);
                  ((InputStream)var0).reset();
                  InflaterInputStream var6 = new InflaterInputStream((InputStream)var0);
                  return var6;
               } catch (ZipException var4) {
                  ((InputStream)var0).reset();
                  return (InputStream)var0;
               }
            }
         }

         return (InputStream)var0;
      }
   }

   public ParsedURLData() {
   }

   public ParsedURLData(URL var1) {
      this.protocol = var1.getProtocol();
      if (this.protocol != null && this.protocol.length() == 0) {
         this.protocol = null;
      }

      this.host = var1.getHost();
      if (this.host != null && this.host.length() == 0) {
         this.host = null;
      }

      this.port = var1.getPort();
      this.path = var1.getFile();
      if (this.path != null && this.path.length() == 0) {
         this.path = null;
      }

      this.ref = var1.getRef();
      if (this.ref != null && this.ref.length() == 0) {
         this.ref = null;
      }

   }

   protected URL buildURL() throws MalformedURLException {
      if (this.protocol != null && this.host != null) {
         String var1 = "";
         if (this.path != null) {
            var1 = this.path;
         }

         return this.port == -1 ? new URL(this.protocol, this.host, var1) : new URL(this.protocol, this.host, this.port, var1);
      } else {
         return new URL(this.toString());
      }
   }

   public int hashCode() {
      int var1 = this.port;
      if (this.protocol != null) {
         var1 ^= this.protocol.hashCode();
      }

      if (this.host != null) {
         var1 ^= this.host.hashCode();
      }

      int var2;
      if (this.path != null) {
         var2 = this.path.length();
         if (var2 > 20) {
            var1 ^= this.path.substring(var2 - 20).hashCode();
         } else {
            var1 ^= this.path.hashCode();
         }
      }

      if (this.ref != null) {
         var2 = this.ref.length();
         if (var2 > 20) {
            var1 ^= this.ref.substring(var2 - 20).hashCode();
         } else {
            var1 ^= this.ref.hashCode();
         }
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof ParsedURLData)) {
         return false;
      } else {
         ParsedURLData var2 = (ParsedURLData)var1;
         if (var2.port != this.port) {
            return false;
         } else {
            if (var2.protocol == null) {
               if (this.protocol != null) {
                  return false;
               }
            } else {
               if (this.protocol == null) {
                  return false;
               }

               if (!var2.protocol.equals(this.protocol)) {
                  return false;
               }
            }

            if (var2.host == null) {
               if (this.host != null) {
                  return false;
               }
            } else {
               if (this.host == null) {
                  return false;
               }

               if (!var2.host.equals(this.host)) {
                  return false;
               }
            }

            if (var2.ref == null) {
               if (this.ref != null) {
                  return false;
               }
            } else {
               if (this.ref == null) {
                  return false;
               }

               if (!var2.ref.equals(this.ref)) {
                  return false;
               }
            }

            if (var2.path == null) {
               if (this.path != null) {
                  return false;
               }
            } else {
               if (this.path == null) {
                  return false;
               }

               if (!var2.path.equals(this.path)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public String getContentType(String var1) {
      if (this.contentType != null) {
         return this.contentType;
      } else {
         if (!this.hasBeenOpened) {
            try {
               this.openStreamInternal(var1, (Iterator)null, (Iterator)null);
            } catch (IOException var3) {
            }
         }

         return this.contentType;
      }
   }

   public String getContentTypeMediaType(String var1) {
      if (this.contentTypeMediaType != null) {
         return this.contentTypeMediaType;
      } else {
         this.extractContentTypeParts(var1);
         return this.contentTypeMediaType;
      }
   }

   public String getContentTypeCharset(String var1) {
      if (this.contentTypeMediaType != null) {
         return this.contentTypeCharset;
      } else {
         this.extractContentTypeParts(var1);
         return this.contentTypeCharset;
      }
   }

   public boolean hasContentTypeParameter(String var1, String var2) {
      this.getContentType(var1);
      if (this.contentType == null) {
         return false;
      } else {
         int var3 = 0;
         int var4 = this.contentType.length();
         int var5 = var2.length();

         label68:
         while(var3 < var4) {
            switch (this.contentType.charAt(var3)) {
               case ' ':
               case ';':
                  break label68;
               default:
                  ++var3;
            }
         }

         if (var3 == var4) {
            this.contentTypeMediaType = this.contentType;
         } else {
            this.contentTypeMediaType = this.contentType.substring(0, var3);
         }

         while(true) {
            label58:
            while(var3 >= var4 || this.contentType.charAt(var3) == ';') {
               if (var3 == var4) {
                  return false;
               }

               ++var3;

               while(var3 < var4 && this.contentType.charAt(var3) == ' ') {
                  ++var3;
               }

               if (var3 >= var4 - var5 - 1) {
                  return false;
               }

               for(int var6 = 0; var6 < var5; ++var6) {
                  if (this.contentType.charAt(var3++) != var2.charAt(var6)) {
                     continue label58;
                  }
               }

               if (this.contentType.charAt(var3) == '=') {
                  return true;
               }
            }

            ++var3;
         }
      }
   }

   protected void extractContentTypeParts(String var1) {
      this.getContentType(var1);
      if (this.contentType != null) {
         int var2 = 0;
         int var3 = this.contentType.length();

         label92:
         while(var2 < var3) {
            switch (this.contentType.charAt(var2)) {
               case ' ':
               case ';':
                  break label92;
               default:
                  ++var2;
            }
         }

         if (var2 == var3) {
            this.contentTypeMediaType = this.contentType;
         } else {
            this.contentTypeMediaType = this.contentType.substring(0, var2);
         }

         while(true) {
            while(var2 >= var3 || this.contentType.charAt(var2) == ';') {
               if (var2 == var3) {
                  return;
               }

               ++var2;

               while(var2 < var3 && this.contentType.charAt(var2) == ' ') {
                  ++var2;
               }

               if (var2 >= var3 - 8) {
                  return;
               }

               if (this.contentType.charAt(var2++) == 'c' && this.contentType.charAt(var2++) == 'h' && this.contentType.charAt(var2++) == 'a' && this.contentType.charAt(var2++) == 'r' && this.contentType.charAt(var2++) == 's' && this.contentType.charAt(var2++) == 'e' && this.contentType.charAt(var2++) == 't' && this.contentType.charAt(var2++) == '=') {
                  int var4 = var2;

                  label58:
                  while(var2 < var3) {
                     switch (this.contentType.charAt(var2)) {
                        case ' ':
                        case ';':
                           break label58;
                        default:
                           ++var2;
                     }
                  }

                  this.contentTypeCharset = this.contentType.substring(var4, var2);
                  return;
               }
            }

            ++var2;
         }
      }
   }

   public String getContentEncoding(String var1) {
      if (this.contentEncoding != null) {
         return this.contentEncoding;
      } else {
         if (!this.hasBeenOpened) {
            try {
               this.openStreamInternal(var1, (Iterator)null, (Iterator)null);
            } catch (IOException var3) {
            }
         }

         return this.contentEncoding;
      }
   }

   public boolean complete() {
      try {
         this.buildURL();
         return true;
      } catch (MalformedURLException var2) {
         return false;
      }
   }

   public InputStream openStream(String var1, Iterator var2) throws IOException {
      InputStream var3 = this.openStreamInternal(var1, var2, acceptedEncodings.iterator());
      if (var3 == null) {
         return null;
      } else {
         this.stream = null;
         return checkGZIP(var3);
      }
   }

   public InputStream openStreamRaw(String var1, Iterator var2) throws IOException {
      InputStream var3 = this.openStreamInternal(var1, var2, (Iterator)null);
      this.stream = null;
      return var3;
   }

   protected InputStream openStreamInternal(String var1, Iterator var2, Iterator var3) throws IOException {
      if (this.stream != null) {
         return this.stream;
      } else {
         this.hasBeenOpened = true;
         URL var4 = null;

         try {
            var4 = this.buildURL();
         } catch (MalformedURLException var7) {
            throw new IOException("Unable to make sense of URL for connection");
         }

         if (var4 == null) {
            return null;
         } else {
            URLConnection var5 = var4.openConnection();
            if (var5 instanceof HttpURLConnection) {
               if (var1 != null) {
                  var5.setRequestProperty("User-Agent", var1);
               }

               String var6;
               if (var2 != null) {
                  var6 = "";

                  while(var2.hasNext()) {
                     var6 = var6 + var2.next();
                     if (var2.hasNext()) {
                        var6 = var6 + ",";
                     }
                  }

                  var5.setRequestProperty("Accept", var6);
               }

               if (var3 != null) {
                  var6 = "";

                  while(var3.hasNext()) {
                     var6 = var6 + var3.next();
                     if (var3.hasNext()) {
                        var6 = var6 + ",";
                     }
                  }

                  var5.setRequestProperty("Accept-Encoding", var6);
               }

               this.contentType = var5.getContentType();
               this.contentEncoding = var5.getContentEncoding();
            }

            return this.stream = var5.getInputStream();
         }
      }
   }

   public String getPortStr() {
      String var1 = "";
      if (this.protocol != null) {
         var1 = var1 + this.protocol + ":";
      }

      if (this.host != null || this.port != -1) {
         var1 = var1 + "//";
         if (this.host != null) {
            var1 = var1 + this.host;
         }

         if (this.port != -1) {
            var1 = var1 + ":" + this.port;
         }
      }

      return var1;
   }

   protected boolean sameFile(ParsedURLData var1) {
      if (this == var1) {
         return true;
      } else {
         return this.port == var1.port && (this.path == var1.path || this.path != null && this.path.equals(var1.path)) && (this.host == var1.host || this.host != null && this.host.equals(var1.host)) && (this.protocol == var1.protocol || this.protocol != null && this.protocol.equals(var1.protocol));
      }
   }

   public String toString() {
      String var1 = this.getPortStr();
      if (this.path != null) {
         var1 = var1 + this.path;
      }

      if (this.ref != null) {
         var1 = var1 + "#" + this.ref;
      }

      return var1;
   }

   static {
      acceptedEncodings.add("gzip");
      GZIP_MAGIC = new byte[]{31, -117};
   }
}
