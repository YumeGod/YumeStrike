package org.apache.avalon.framework.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ConfigurationUtil {
   private ConfigurationUtil() {
   }

   public static Configuration toConfiguration(Element element) {
      DefaultConfiguration configuration = new DefaultConfiguration(element.getNodeName(), "dom-created");
      NamedNodeMap attributes = element.getAttributes();
      int length = attributes.getLength();

      for(int i = 0; i < length; ++i) {
         Node node = attributes.item(i);
         String name = node.getNodeName();
         String value = node.getNodeValue();
         configuration.setAttribute(name, value);
      }

      boolean flag = false;
      String content = "";
      NodeList nodes = element.getChildNodes();
      int count = nodes.getLength();

      for(int i = 0; i < count; ++i) {
         Node node = nodes.item(i);
         if (node instanceof Element) {
            Configuration child = toConfiguration((Element)node);
            configuration.addChild(child);
         } else if (node instanceof CharacterData) {
            CharacterData data = (CharacterData)node;
            content = content + data.getData();
            flag = true;
         }
      }

      if (flag) {
         configuration.setValue(content);
      }

      return configuration;
   }

   public static Element toElement(Configuration configuration) {
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document document = builder.newDocument();
         return createElement(document, configuration);
      } catch (ParserConfigurationException var4) {
         throw new IllegalStateException(var4.toString());
      }
   }

   public static String toString(Configuration configuration) {
      DefaultConfigurationSerializer ser = new DefaultConfigurationSerializer();

      try {
         return ser.serialize(configuration);
      } catch (Exception var3) {
         return var3.getMessage();
      }
   }

   public static boolean equals(Configuration c1, Configuration c2) {
      return c1.getName().equals(c2.getName()) && areValuesEqual(c1, c2) && areAttributesEqual(c1, c2) && areChildrenEqual(c1, c2);
   }

   private static boolean areChildrenEqual(Configuration c1, Configuration c2) {
      Configuration[] kids1 = c1.getChildren();
      ArrayList kids2 = new ArrayList(Arrays.asList(c2.getChildren()));
      if (kids1.length != kids2.size()) {
         return false;
      } else {
         for(int i = 0; i < kids1.length; ++i) {
            if (!findMatchingChild(kids1[i], kids2)) {
               return false;
            }
         }

         return kids2.isEmpty();
      }
   }

   private static boolean findMatchingChild(Configuration c, ArrayList matchAgainst) {
      Iterator i = matchAgainst.iterator();

      do {
         if (!i.hasNext()) {
            return false;
         }
      } while(!equals(c, (Configuration)i.next()));

      i.remove();
      return true;
   }

   private static boolean areAttributesEqual(Configuration c1, Configuration c2) {
      String[] names1 = c1.getAttributeNames();
      String[] names2 = c2.getAttributeNames();
      if (names1.length != names2.length) {
         return false;
      } else {
         for(int i = 0; i < names1.length; ++i) {
            String name = names1[i];
            String value1 = c1.getAttribute(name, (String)null);
            String value2 = c2.getAttribute(name, (String)null);
            if (!value1.equals(value2)) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean areValuesEqual(Configuration c1, Configuration c2) {
      String value1 = c1.getValue((String)null);
      String value2 = c2.getValue((String)null);
      return value1 == null && value2 == null || value1 != null && value1.equals(value2);
   }

   private static Element createElement(Document document, Configuration configuration) {
      Element element = document.createElement(configuration.getName());
      String content = configuration.getValue((String)null);
      if (null != content) {
         Text child = document.createTextNode(content);
         element.appendChild(child);
      }

      String[] names = configuration.getAttributeNames();

      for(int i = 0; i < names.length; ++i) {
         String name = names[i];
         String value = configuration.getAttribute(name, (String)null);
         element.setAttribute(name, value);
      }

      Configuration[] children = configuration.getChildren();

      for(int i = 0; i < children.length; ++i) {
         Element child = createElement(document, children[i]);
         element.appendChild(child);
      }

      return element;
   }
}
