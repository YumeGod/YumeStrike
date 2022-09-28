package org.apache.xerces.xinclude;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.io.ASCIIReader;
import org.apache.xerces.impl.io.UTF8Reader;
import org.apache.xerces.util.EncodingMap;
import org.apache.xerces.util.HTTPInputSource;
import org.apache.xerces.util.MessageFormatter;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XIncludeTextReader {
   private Reader fReader;
   private XIncludeHandler fHandler;
   private XMLInputSource fSource;
   private XMLErrorReporter fErrorReporter;
   private XMLString fTempString = new XMLString();

   public XIncludeTextReader(XMLInputSource var1, XIncludeHandler var2, int var3) throws IOException {
      this.fHandler = var2;
      this.fSource = var1;
      this.fTempString = new XMLString(new char[var3 + 1], 0, 0);
   }

   public void setErrorReporter(XMLErrorReporter var1) {
      this.fErrorReporter = var1;
   }

   protected Reader getReader(XMLInputSource var1) throws IOException {
      if (var1.getCharacterStream() != null) {
         return var1.getCharacterStream();
      } else {
         Object var2 = null;
         String var3 = var1.getEncoding();
         if (var3 == null) {
            var3 = "UTF-8";
         }

         String var4;
         if (var1.getByteStream() != null) {
            var2 = var1.getByteStream();
            if (!(var2 instanceof BufferedInputStream)) {
               var2 = new BufferedInputStream((InputStream)var2, this.fTempString.ch.length);
            }
         } else {
            var4 = XMLEntityManager.expandSystemId(var1.getSystemId(), var1.getBaseSystemId(), false);
            URL var5 = new URL(var4);
            URLConnection var6 = var5.openConnection();
            Iterator var9;
            if (var6 instanceof HttpURLConnection && var1 instanceof HTTPInputSource) {
               HttpURLConnection var7 = (HttpURLConnection)var6;
               HTTPInputSource var8 = (HTTPInputSource)var1;
               var9 = var8.getHTTPRequestProperties();

               while(var9.hasNext()) {
                  Map.Entry var10 = (Map.Entry)var9.next();
                  var7.setRequestProperty((String)var10.getKey(), (String)var10.getValue());
               }

               boolean var17 = var8.getFollowHTTPRedirects();
               if (!var17) {
                  XMLEntityManager.setInstanceFollowRedirects(var7, var17);
               }
            }

            var2 = new BufferedInputStream(var6.getInputStream());
            String var14 = var6.getContentType();
            int var15 = var14 != null ? var14.indexOf(59) : -1;
            var9 = null;
            String var18 = null;
            String var16;
            if (var15 != -1) {
               var16 = var14.substring(0, var15).trim();
               var18 = var14.substring(var15 + 1).trim();
               if (!var18.startsWith("charset=")) {
                  var18 = null;
               } else {
                  var18 = var18.substring(8).trim();
                  if (var18.charAt(0) == '"' && var18.charAt(var18.length() - 1) == '"' || var18.charAt(0) == '\'' && var18.charAt(var18.length() - 1) == '\'') {
                     var18 = var18.substring(1, var18.length() - 1);
                  }
               }
            } else {
               var16 = var14.trim();
            }

            String var11 = null;
            if (var16.equals("text/xml")) {
               if (var18 != null) {
                  var11 = var18;
               } else {
                  var11 = "US-ASCII";
               }
            } else if (var16.equals("application/xml")) {
               if (var18 != null) {
                  var11 = var18;
               } else {
                  var11 = this.getEncodingName((InputStream)var2);
               }
            } else if (var16.endsWith("+xml")) {
               var11 = this.getEncodingName((InputStream)var2);
            }

            if (var11 != null) {
               var3 = var11;
            }
         }

         var3 = var3.toUpperCase(Locale.ENGLISH);
         var3 = this.consumeBOM((InputStream)var2, var3);
         if (var3.equals("UTF-8")) {
            return new UTF8Reader((InputStream)var2, this.fTempString.ch.length, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
         } else {
            var4 = EncodingMap.getIANA2JavaMapping(var3);
            if (var4 == null) {
               MessageFormatter var12 = this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210");
               Locale var13 = this.fErrorReporter.getLocale();
               throw new IOException(var12.formatMessage(var13, "EncodingDeclInvalid", new Object[]{var3}));
            } else {
               return (Reader)(var4.equals("ASCII") ? new ASCIIReader((InputStream)var2, this.fTempString.ch.length, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale()) : new InputStreamReader((InputStream)var2, var4));
            }
         }
      }
   }

   protected String getEncodingName(InputStream var1) throws IOException {
      byte[] var2 = new byte[4];
      String var3 = null;
      var1.mark(4);
      int var4 = var1.read(var2, 0, 4);
      var1.reset();
      if (var4 == 4) {
         var3 = this.getEncodingName(var2);
      }

      return var3;
   }

   protected String consumeBOM(InputStream var1, String var2) throws IOException {
      byte[] var3 = new byte[3];
      boolean var4 = false;
      var1.mark(3);
      int var5;
      int var6;
      int var8;
      if (var2.equals("UTF-8")) {
         var8 = var1.read(var3, 0, 3);
         if (var8 == 3) {
            var5 = var3[0] & 255;
            var6 = var3[1] & 255;
            int var7 = var3[2] & 255;
            if (var5 != 239 || var6 != 187 || var7 != 191) {
               var1.reset();
            }
         } else {
            var1.reset();
         }
      } else if (var2.startsWith("UTF-16")) {
         var8 = var1.read(var3, 0, 2);
         if (var8 == 2) {
            var5 = var3[0] & 255;
            var6 = var3[1] & 255;
            if (var5 == 254 && var6 == 255) {
               return "UTF-16BE";
            }

            if (var5 == 255 && var6 == 254) {
               return "UTF-16LE";
            }
         }

         var1.reset();
      }

      return var2;
   }

   protected String getEncodingName(byte[] var1) {
      int var2 = var1[0] & 255;
      int var3 = var1[1] & 255;
      if (var2 == 254 && var3 == 255) {
         return "UTF-16BE";
      } else if (var2 == 255 && var3 == 254) {
         return "UTF-16LE";
      } else {
         int var4 = var1[2] & 255;
         if (var2 == 239 && var3 == 187 && var4 == 191) {
            return "UTF-8";
         } else {
            int var5 = var1[3] & 255;
            if (var2 == 0 && var3 == 0 && var4 == 0 && var5 == 60) {
               return "ISO-10646-UCS-4";
            } else if (var2 == 60 && var3 == 0 && var4 == 0 && var5 == 0) {
               return "ISO-10646-UCS-4";
            } else if (var2 == 0 && var3 == 0 && var4 == 60 && var5 == 0) {
               return "ISO-10646-UCS-4";
            } else if (var2 == 0 && var3 == 60 && var4 == 0 && var5 == 0) {
               return "ISO-10646-UCS-4";
            } else if (var2 == 0 && var3 == 60 && var4 == 0 && var5 == 63) {
               return "UTF-16BE";
            } else if (var2 == 60 && var3 == 0 && var4 == 63 && var5 == 0) {
               return "UTF-16LE";
            } else {
               return var2 == 76 && var3 == 111 && var4 == 167 && var5 == 148 ? "CP037" : null;
            }
         }
      }
   }

   public void parse() throws IOException {
      this.fReader = this.getReader(this.fSource);
      this.fSource = null;

      for(int var1 = this.fReader.read(this.fTempString.ch, 0, this.fTempString.ch.length - 1); var1 != -1; var1 = this.fReader.read(this.fTempString.ch, 0, this.fTempString.ch.length - 1)) {
         for(int var2 = 0; var2 < var1; ++var2) {
            char var3 = this.fTempString.ch[var2];
            if (!this.isValid(var3)) {
               if (XMLChar.isHighSurrogate(var3)) {
                  ++var2;
                  int var4;
                  if (var2 < var1) {
                     var4 = this.fTempString.ch[var2];
                  } else {
                     var4 = this.fReader.read();
                     if (var4 != -1) {
                        this.fTempString.ch[var1++] = (char)var4;
                     }
                  }

                  if (XMLChar.isLowSurrogate(var4)) {
                     int var5 = XMLChar.supplemental(var3, (char)var4);
                     if (!this.isValid(var5)) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[]{Integer.toString(var5, 16)}, (short)2);
                     }
                  } else {
                     this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[]{Integer.toString(var4, 16)}, (short)2);
                  }
               } else {
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[]{Integer.toString(var3, 16)}, (short)2);
               }
            }
         }

         if (this.fHandler != null && var1 > 0) {
            this.fTempString.offset = 0;
            this.fTempString.length = var1;
            this.fHandler.characters(this.fTempString, this.fHandler.modifyAugmentations((Augmentations)null, true));
         }
      }

   }

   public void setInputSource(XMLInputSource var1) {
      this.fSource = var1;
   }

   public void close() throws IOException {
      if (this.fReader != null) {
         this.fReader.close();
         this.fReader = null;
      }

   }

   protected boolean isValid(int var1) {
      return XMLChar.isValid(var1);
   }

   protected void setBufferSize(int var1) {
      int var10000 = this.fTempString.ch.length;
      ++var1;
      if (var10000 != var1) {
         this.fTempString.ch = new char[var1];
      }

   }
}
