package org.apache.xalan.lib;

import org.apache.xpath.NodeSet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExsltMath extends ExsltBase {
   private static String PI = "3.1415926535897932384626433832795028841971693993751";
   private static String E = "2.71828182845904523536028747135266249775724709369996";
   private static String SQRRT2 = "1.41421356237309504880168872420969807856967187537694";
   private static String LN2 = "0.69314718055994530941723212145817656807550013436025";
   private static String LN10 = "2.302585092994046";
   private static String LOG2E = "1.4426950408889633";
   private static String SQRT1_2 = "0.7071067811865476";

   public static double max(NodeList nl) {
      if (nl != null && nl.getLength() != 0) {
         double m = -1.7976931348623157E308;

         for(int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);
            double d = ExsltBase.toNumber(n);
            if (Double.isNaN(d)) {
               return Double.NaN;
            }

            if (d > m) {
               m = d;
            }
         }

         return m;
      } else {
         return Double.NaN;
      }
   }

   public static double min(NodeList nl) {
      if (nl != null && nl.getLength() != 0) {
         double m = Double.MAX_VALUE;

         for(int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);
            double d = ExsltBase.toNumber(n);
            if (Double.isNaN(d)) {
               return Double.NaN;
            }

            if (d < m) {
               m = d;
            }
         }

         return m;
      } else {
         return Double.NaN;
      }
   }

   public static NodeList highest(NodeList nl) {
      double maxValue = max(nl);
      NodeSet highNodes = new NodeSet();
      highNodes.setShouldCacheNodes(true);
      if (Double.isNaN(maxValue)) {
         return highNodes;
      } else {
         for(int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);
            double d = ExsltBase.toNumber(n);
            if (d == maxValue) {
               highNodes.addElement(n);
            }
         }

         return highNodes;
      }
   }

   public static NodeList lowest(NodeList nl) {
      double minValue = min(nl);
      NodeSet lowNodes = new NodeSet();
      lowNodes.setShouldCacheNodes(true);
      if (Double.isNaN(minValue)) {
         return lowNodes;
      } else {
         for(int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);
            double d = ExsltBase.toNumber(n);
            if (d == minValue) {
               lowNodes.addElement(n);
            }
         }

         return lowNodes;
      }
   }

   public static double abs(double num) {
      return Math.abs(num);
   }

   public static double acos(double num) {
      return Math.acos(num);
   }

   public static double asin(double num) {
      return Math.asin(num);
   }

   public static double atan(double num) {
      return Math.atan(num);
   }

   public static double atan2(double num1, double num2) {
      return Math.atan2(num1, num2);
   }

   public static double cos(double num) {
      return Math.cos(num);
   }

   public static double exp(double num) {
      return Math.exp(num);
   }

   public static double log(double num) {
      return Math.log(num);
   }

   public static double power(double num1, double num2) {
      return Math.pow(num1, num2);
   }

   public static double random() {
      return Math.random();
   }

   public static double sin(double num) {
      return Math.sin(num);
   }

   public static double sqrt(double num) {
      return Math.sqrt(num);
   }

   public static double tan(double num) {
      return Math.tan(num);
   }

   public static double constant(String name, double precision) {
      String value = null;
      if (name.equals("PI")) {
         value = PI;
      } else if (name.equals("E")) {
         value = E;
      } else if (name.equals("SQRRT2")) {
         value = SQRRT2;
      } else if (name.equals("LN2")) {
         value = LN2;
      } else if (name.equals("LN10")) {
         value = LN10;
      } else if (name.equals("LOG2E")) {
         value = LOG2E;
      } else if (name.equals("SQRT1_2")) {
         value = SQRT1_2;
      }

      if (value != null) {
         int bits = (new Double(precision)).intValue();
         if (bits <= value.length()) {
            value = value.substring(0, bits);
         }

         return new Double(value);
      } else {
         return Double.NaN;
      }
   }
}
