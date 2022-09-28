package org.apache.batik.apps.svgbrowser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.apache.batik.util.PreferenceManager;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLPreferenceManager extends PreferenceManager {
   protected String xmlParserClassName;
   public static final String PREFERENCE_ENCODING = "8859_1";

   public XMLPreferenceManager(String var1) {
      this(var1, (Map)null, XMLResourceDescriptor.getXMLParserClassName());
   }

   public XMLPreferenceManager(String var1, Map var2) {
      this(var1, var2, XMLResourceDescriptor.getXMLParserClassName());
   }

   public XMLPreferenceManager(String var1, String var2) {
      this(var1, (Map)null, var2);
   }

   public XMLPreferenceManager(String var1, Map var2, String var3) {
      super(var1, var2);
      this.internal = new XMLProperties();
      this.xmlParserClassName = var3;
   }

   protected class XMLProperties extends Properties {
      public synchronized void load(InputStream var1) throws IOException {
         BufferedReader var2 = new BufferedReader(new InputStreamReader(var1, "8859_1"));
         SAXDocumentFactory var3 = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), XMLPreferenceManager.this.xmlParserClassName);
         Document var4 = var3.createDocument("http://xml.apache.org/batik/preferences", "preferences", (String)null, (Reader)var2);
         Element var5 = var4.getDocumentElement();

         for(Node var6 = var5.getFirstChild(); var6 != null; var6 = var6.getNextSibling()) {
            if (var6.getNodeType() == 1 && var6.getNodeName().equals("property")) {
               String var7 = ((Element)var6).getAttributeNS((String)null, "name");
               StringBuffer var8 = new StringBuffer();

               for(Node var9 = var6.getFirstChild(); var9 != null && var9.getNodeType() == 3; var9 = var9.getNextSibling()) {
                  var8.append(var9.getNodeValue());
               }

               String var10 = var8.toString();
               this.put(var7, var10);
            }
         }

      }

      public synchronized void store(OutputStream var1, String var2) throws IOException {
         BufferedWriter var3 = new BufferedWriter(new OutputStreamWriter(var1, "8859_1"));
         HashMap var4 = new HashMap();
         this.enumerate(var4);
         var3.write("<preferences xmlns=\"http://xml.apache.org/batik/preferences\">\n");
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            String var7 = (String)var4.get(var6);
            var3.write("<property name=\"" + var6 + "\">");
            var3.write(DOMUtilities.contentToString(var7));
            var3.write("</property>\n");
         }

         var3.write("</preferences>\n");
         var3.flush();
      }

      private synchronized void enumerate(Map var1) {
         Iterator var2;
         Object var3;
         if (this.defaults != null) {
            var2 = var1.keySet().iterator();

            while(var2.hasNext()) {
               var3 = var2.next();
               var1.put(var3, this.defaults.get(var3));
            }
         }

         var2 = this.keySet().iterator();

         while(var2.hasNext()) {
            var3 = var2.next();
            var1.put(var3, this.get(var3));
         }

      }
   }
}
