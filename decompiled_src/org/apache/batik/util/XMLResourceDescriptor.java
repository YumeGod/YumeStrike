package org.apache.batik.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;

public class XMLResourceDescriptor {
   public static final String XML_PARSER_CLASS_NAME_KEY = "org.xml.sax.driver";
   public static final String CSS_PARSER_CLASS_NAME_KEY = "org.w3c.css.sac.driver";
   public static final String RESOURCES = "resources/XMLResourceDescriptor.properties";
   protected static Properties parserProps = null;
   protected static String xmlParserClassName;
   protected static String cssParserClassName;
   // $FF: synthetic field
   static Class class$org$apache$batik$util$XMLResourceDescriptor;

   protected static synchronized Properties getParserProps() {
      if (parserProps != null) {
         return parserProps;
      } else {
         parserProps = new Properties();

         try {
            Class var0 = class$org$apache$batik$util$XMLResourceDescriptor == null ? (class$org$apache$batik$util$XMLResourceDescriptor = class$("org.apache.batik.util.XMLResourceDescriptor")) : class$org$apache$batik$util$XMLResourceDescriptor;
            InputStream var1 = var0.getResourceAsStream("resources/XMLResourceDescriptor.properties");
            parserProps.load(var1);
         } catch (IOException var2) {
            throw new MissingResourceException(var2.getMessage(), "resources/XMLResourceDescriptor.properties", (String)null);
         }

         return parserProps;
      }
   }

   public static String getXMLParserClassName() {
      if (xmlParserClassName == null) {
         xmlParserClassName = getParserProps().getProperty("org.xml.sax.driver");
      }

      return xmlParserClassName;
   }

   public static void setXMLParserClassName(String var0) {
      xmlParserClassName = var0;
   }

   public static String getCSSParserClassName() {
      if (cssParserClassName == null) {
         cssParserClassName = getParserProps().getProperty("org.w3c.css.sac.driver");
      }

      return cssParserClassName;
   }

   public static void setCSSParserClassName(String var0) {
      cssParserClassName = var0;
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
