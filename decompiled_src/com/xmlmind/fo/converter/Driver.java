package com.xmlmind.fo.converter;

import com.xmlmind.fo.util.SystemUtil;
import com.xmlmind.fo.util.URLUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class Driver implements ErrorHandler {
   private static final String defaultOutputFormat = "rtf";
   private Properties properties = new Properties();
   private ErrorHandler errorHandler = this;
   private UriResolver uriResolver;
   private XMLReader reader;
   private InputSource input;
   private OutputDestination output;

   public static String[] listEncodings(String var0) throws IllegalArgumentException {
      return Converter.listEncodings(var0);
   }

   public void setInput(InputSource var1) {
      this.input = new InputSource();
      this.input.setByteStream(var1.getByteStream());
      this.input.setCharacterStream(var1.getCharacterStream());
      this.input.setEncoding(var1.getEncoding());
      this.input.setPublicId(var1.getPublicId());
      String var2 = var1.getSystemId();
      if (var2 != null) {
         this.input.setSystemId(URLUtil.locationOrFilename(var2));
      }

   }

   public void setInput(String var1) {
      this.input = new InputSource(URLUtil.locationOrFilename(var1));
   }

   public void setInput(URL var1) {
      this.input = new InputSource(var1.toString());
   }

   public void setInput(InputStream var1) {
      this.input = new InputSource(var1);
   }

   public void setInput(Reader var1) {
      this.input = new InputSource(var1);
   }

   public void setOutput(OutputDestination var1) {
      this.output = var1.copy();
   }

   public void setOutput(String var1) {
      this.output = new OutputDestination(var1);
   }

   public void setOutput(OutputStream var1) {
      this.output = new OutputDestination(var1);
   }

   public void setOutput(Writer var1) {
      this.output = new OutputDestination(var1);
   }

   public void setProperty(String var1, String var2) {
      this.properties.setProperty(var1, var2);
   }

   public void setProperties(Properties var1) {
      Enumeration var2 = var1.propertyNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         this.setProperty(var3, var1.getProperty(var3));
      }

   }

   public void loadPropertyFile(String var1) throws IOException {
      this.loadPropertyFile(new File(var1));
   }

   public void loadPropertyFile(File var1) throws IOException {
      BufferedInputStream var2 = new BufferedInputStream(new FileInputStream(var1));
      this.properties.load(var2);
   }

   public void loadUserConfiguration() throws IOException {
      File var1 = SystemUtil.userPreferencesDir();
      if (var1 != null) {
         File var2 = new File(var1, "xfc.properties");
         if (var2.isFile() && var2.canRead()) {
            this.loadPropertyFile(var2);
         }
      }

   }

   public void setErrorHandler(ErrorHandler var1) {
      if (var1 != null) {
         this.errorHandler = var1;
      } else {
         this.errorHandler = this;
      }

   }

   public void setUriResolver(UriResolver var1) {
      this.uriResolver = var1;
   }

   public UriResolver getUriResolver() {
      return this.uriResolver;
   }

   public void setXMLReader(XMLReader var1) {
      this.reader = var1;
   }

   public XMLReader getXMLReader() {
      return this.reader;
   }

   public void convert() throws Exception {
      if (this.input != null && this.output != null) {
         Converter var3 = new Converter();
         var3.setProperties(this.properties);
         var3.setErrorHandler(this.errorHandler);
         var3.setUriResolver(this.uriResolver);
         var3.setXMLReader(this.reader);
         String var1 = this.properties.getProperty("outputFormat");
         if (var1 == null) {
            var1 = this.getOutputFormat(this.output);
            if (var1 == null) {
               var1 = "rtf";
            }

            var3.setProperty("outputFormat", var1);
         }

         if (this.input.getEncoding() == null) {
            String var2 = this.properties.getProperty("inputEncoding");
            if (var2 != null) {
               this.input.setEncoding(var2);
            }
         }

         var3.convert(this.input, this.output);
      } else {
         this.errorHandler.error(new Exception("no input/output specified"));
      }
   }

   public void convert(InputSource var1, OutputDestination var2) throws Exception {
      this.setInput(var1);
      this.setOutput(var2);
      this.convert();
   }

   private String getOutputFormat(OutputDestination var1) {
      String var2 = null;
      String var3 = var1.getFileName();
      if (var3 != null) {
         int var4 = var3.lastIndexOf(46);
         if (var4 > 0) {
            String var5 = var3.substring(var4 + 1).toLowerCase();
            if (var5.equals("rtf")) {
               var2 = "rtf";
            } else if (var5.equals("xml")) {
               var2 = "wml";
            } else if (var5.equals("odt")) {
               var2 = "odt";
            }
         }
      }

      return var2;
   }

   public void error(Exception var1) throws Exception {
      throw var1;
   }

   public void warning(Exception var1) throws Exception {
      String var2 = var1.getMessage();
      System.err.println("warning: " + var2);
   }

   public static void main(String[] var0) {
      Properties var1 = new Properties();
      String var2 = null;
      String var3 = null;

      String var7;
      for(int var4 = 0; var4 < var0.length; ++var4) {
         if (var0[var4].charAt(0) == '-' && var0[var4].length() > 1) {
            int var5 = var0[var4].indexOf(61);
            if (var5 < 2) {
               usage("illegal argument \"" + var0[var4] + "\"");
            }

            String var6 = var0[var4].substring(1, var5);
            var7 = var0[var4].substring(var5 + 1);
            var1.setProperty(var6, var7);
         } else if (var2 == null) {
            var2 = var0[var4];
         } else if (var3 == null) {
            var3 = var0[var4];
         } else {
            usage("illegal argument \"" + var0[var4] + "\"");
         }
      }

      boolean var11 = var2 == null || var2.equals("-");
      boolean var12 = var3 == null || var3.equals("-");

      try {
         Driver var13 = new Driver();
         var7 = var1.getProperty("validation");
         if (var7 != null && var7.equals("true")) {
            SAXParserFactory var8 = SAXParserFactory.newInstance();
            var8.setValidating(true);
            SAXParser var9 = var8.newSAXParser();
            var13.setXMLReader(var9.getXMLReader());
         }

         var13.loadUserConfiguration();
         if (!var1.isEmpty()) {
            var13.setProperties(var1);
         }

         if (var11) {
            var13.setInput(System.in);
         } else {
            var13.setInput(var2);
         }

         if (var12) {
            var13.setOutput((OutputStream)System.out);
         } else {
            var13.setOutput(var3);
         }

         var13.convert();
      } catch (Exception var10) {
         var7 = var10.getMessage();
         if (var7 == null) {
            var7 = var10.getClass().getName();
         }

         System.err.println("Cannot convert \"" + (var11 ? "(standard input)" : var2) + "\" to \"" + (var12 ? "(standard output)" : var3) + "\": " + var7);
         System.exit(2);
      }

      System.exit(0);
   }

   private static void usage(String var0) {
      if (var0 != null) {
         System.err.println("Error: " + var0);
      }

      System.err.println("Usage: fo2XXX [ -property_name=property_value ]* [ in_fo_file|- [ out_file|- ] ]");
      System.exit(1);
   }
}
