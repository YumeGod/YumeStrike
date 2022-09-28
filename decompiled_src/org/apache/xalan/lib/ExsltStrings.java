package org.apache.xalan.lib;

import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.NodeSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ExsltStrings extends ExsltBase {
   public static String align(String targetStr, String paddingStr, String type) {
      if (targetStr.length() >= paddingStr.length()) {
         return targetStr.substring(0, paddingStr.length());
      } else if (type.equals("right")) {
         return paddingStr.substring(0, paddingStr.length() - targetStr.length()) + targetStr;
      } else if (type.equals("center")) {
         int startIndex = (paddingStr.length() - targetStr.length()) / 2;
         return paddingStr.substring(0, startIndex) + targetStr + paddingStr.substring(startIndex + targetStr.length());
      } else {
         return targetStr + paddingStr.substring(targetStr.length());
      }
   }

   public static String align(String targetStr, String paddingStr) {
      return align(targetStr, paddingStr, "left");
   }

   public static String concat(NodeList nl) {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < nl.getLength(); ++i) {
         Node node = nl.item(i);
         String value = ExsltBase.toString(node);
         if (value != null && value.length() > 0) {
            sb.append(value);
         }
      }

      return sb.toString();
   }

   public static String padding(double length, String pattern) {
      if (pattern != null && pattern.length() != 0) {
         StringBuffer sb = new StringBuffer();
         int len = (int)length;
         int numAdded = 0;

         for(int index = 0; numAdded < len; ++numAdded) {
            if (index == pattern.length()) {
               index = 0;
            }

            sb.append(pattern.charAt(index));
            ++index;
         }

         return sb.toString();
      } else {
         return "";
      }
   }

   public static String padding(double length) {
      return padding(length, " ");
   }

   public static NodeList split(String str, String pattern) {
      NodeSet resultSet = new NodeSet();
      resultSet.setShouldCacheNodes(true);
      boolean done = false;
      int fromIndex = 0;
      int matchIndex = false;
      String token = null;

      while(!done && fromIndex < str.length()) {
         int matchIndex = str.indexOf(pattern, fromIndex);
         if (matchIndex >= 0) {
            token = str.substring(fromIndex, matchIndex);
            fromIndex = matchIndex + pattern.length();
         } else {
            done = true;
            token = str.substring(fromIndex);
         }

         Document doc = ExsltStrings.DocumentHolder.m_doc;
         synchronized(doc) {
            Element element = doc.createElement("token");
            Text text = doc.createTextNode(token);
            element.appendChild(text);
            resultSet.addNode(element);
         }
      }

      return resultSet;
   }

   public static NodeList split(String str) {
      return split(str, " ");
   }

   public static NodeList tokenize(String toTokenize, String delims) {
      NodeSet resultSet = new NodeSet();
      Element element;
      if (delims != null && delims.length() > 0) {
         StringTokenizer lTokenizer = new StringTokenizer(toTokenize, delims);
         Document doc = ExsltStrings.DocumentHolder.m_doc;
         synchronized(doc) {
            while(lTokenizer.hasMoreTokens()) {
               element = doc.createElement("token");
               element.appendChild(doc.createTextNode(lTokenizer.nextToken()));
               resultSet.addNode(element);
            }
         }
      } else {
         Document doc = ExsltStrings.DocumentHolder.m_doc;
         synchronized(doc) {
            for(int i = 0; i < toTokenize.length(); ++i) {
               element = doc.createElement("token");
               element.appendChild(doc.createTextNode(toTokenize.substring(i, i + 1)));
               resultSet.addNode(element);
            }
         }
      }

      return resultSet;
   }

   public static NodeList tokenize(String toTokenize) {
      return tokenize(toTokenize, " \t\n\r");
   }

   private static class DocumentHolder {
      private static final Document m_doc;

      static {
         try {
            m_doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
         } catch (ParserConfigurationException var1) {
            throw new WrappedRuntimeException(var1);
         }
      }
   }
}
