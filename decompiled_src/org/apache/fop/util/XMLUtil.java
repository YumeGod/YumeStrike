package org.apache.fop.util;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMLUtil implements XMLConstants {
   public static boolean getAttributeAsBoolean(Attributes attributes, String name, boolean defaultValue) {
      String s = attributes.getValue(name);
      return s == null ? defaultValue : Boolean.valueOf(s);
   }

   public static int getAttributeAsInt(Attributes attributes, String name, int defaultValue) {
      String s = attributes.getValue(name);
      return s == null ? defaultValue : Integer.parseInt(s);
   }

   public static int getAttributeAsInt(Attributes attributes, String name) throws SAXException {
      String s = attributes.getValue(name);
      if (s == null) {
         throw new SAXException("Attribute '" + name + "' is missing");
      } else {
         return Integer.parseInt(s);
      }
   }

   public static Integer getAttributeAsInteger(Attributes attributes, String name) {
      String s = attributes.getValue(name);
      return s == null ? null : new Integer(s);
   }

   public static Rectangle2D getAttributeAsRectangle2D(Attributes attributes, String name) {
      String s = attributes.getValue(name).trim();
      double[] values = ConversionUtils.toDoubleArray(s, "\\s");
      if (values.length != 4) {
         throw new IllegalArgumentException("Rectangle must consist of 4 double values!");
      } else {
         return new Rectangle2D.Double(values[0], values[1], values[2], values[3]);
      }
   }

   public static Rectangle getAttributeAsRectangle(Attributes attributes, String name) {
      String s = attributes.getValue(name);
      if (s == null) {
         return null;
      } else {
         int[] values = ConversionUtils.toIntArray(s.trim(), "\\s");
         if (values.length != 4) {
            throw new IllegalArgumentException("Rectangle must consist of 4 int values!");
         } else {
            return new Rectangle(values[0], values[1], values[2], values[3]);
         }
      }
   }

   public static int[] getAttributeAsIntArray(Attributes attributes, String name) {
      String s = attributes.getValue(name);
      return s == null ? null : ConversionUtils.toIntArray(s.trim(), "\\s");
   }

   public static void addAttribute(AttributesImpl atts, org.apache.xmlgraphics.util.QName attribute, String value) {
      atts.addAttribute(attribute.getNamespaceURI(), attribute.getLocalName(), attribute.getQName(), "CDATA", value);
   }

   public static void addAttribute(AttributesImpl atts, String localName, String value) {
      atts.addAttribute("", localName, localName, "CDATA", value);
   }

   public static String toRFC3066(Locale language) {
      if (language != null && language.getLanguage().length() != 0) {
         StringBuffer sb = new StringBuffer();
         sb.append(language.getLanguage());
         if (language.getCountry().length() > 0) {
            sb.append('-');
            sb.append(language.getCountry());
         }

         return sb.toString();
      } else {
         return null;
      }
   }

   public static Locale convertRFC3066ToLocale(String lang) {
      if (lang != null && lang.length() != 0) {
         String[] parts = lang.split("-");
         return parts.length == 1 ? new Locale(parts[0]) : new Locale(parts[0], parts[1]);
      } else {
         return null;
      }
   }
}
