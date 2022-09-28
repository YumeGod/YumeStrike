package org.apache.xalan.client;

import java.applet.Applet;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.res.XSLMessages;

public class XSLTProcessorApplet extends Applet {
   transient TransformerFactory m_tfactory = null;
   private String m_styleURL;
   private String m_documentURL;
   private final String PARAM_styleURL = "styleURL";
   private final String PARAM_documentURL = "documentURL";
   private String m_styleURLOfCached = null;
   private String m_documentURLOfCached = null;
   private URL m_codeBase = null;
   private String m_treeURL = null;
   private URL m_documentBase = null;
   private transient Thread m_callThread = null;
   private transient TrustedAgent m_trustedAgent = null;
   private transient Thread m_trustedWorker = null;
   private transient String m_htmlText = null;
   private transient String m_sourceText = null;
   private transient String m_nameOfIDAttrOfElemToModify = null;
   private transient String m_elemIdToModify = null;
   private transient String m_attrNameToSet = null;
   private transient String m_attrValueToSet = null;
   transient Hashtable m_parameters;
   private static final long serialVersionUID = 4618876841979251422L;

   public String getAppletInfo() {
      return "Name: XSLTProcessorApplet\r\nAuthor: Scott Boag";
   }

   public String[][] getParameterInfo() {
      String[][] info = new String[][]{{"styleURL", "String", "URL to an XSL stylesheet"}, {"documentURL", "String", "URL to an XML document"}};
      return info;
   }

   public void init() {
      String param = this.getParameter("styleURL");
      this.m_parameters = new Hashtable();
      if (param != null) {
         this.setStyleURL(param);
      }

      param = this.getParameter("documentURL");
      if (param != null) {
         this.setDocumentURL(param);
      }

      this.m_codeBase = this.getCodeBase();
      this.m_documentBase = this.getDocumentBase();
      this.resize(320, 240);
   }

   public void start() {
      this.m_trustedAgent = new TrustedAgent();
      Thread currentThread = Thread.currentThread();
      this.m_trustedWorker = new Thread(currentThread.getThreadGroup(), this.m_trustedAgent);
      this.m_trustedWorker.start();

      try {
         this.m_tfactory = TransformerFactory.newInstance();
         this.showStatus("Causing Transformer and Parser to Load and JIT...");
         StringReader xmlbuf = new StringReader("<?xml version='1.0'?><foo/>");
         StringReader xslbuf = new StringReader("<?xml version='1.0'?><xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'><xsl:template match='foo'><out/></xsl:template></xsl:stylesheet>");
         PrintWriter pw = new PrintWriter(new StringWriter());
         TransformerFactory var5 = this.m_tfactory;
         synchronized(var5) {
            Templates templates = this.m_tfactory.newTemplates(new StreamSource(xslbuf));
            Transformer transformer = templates.newTransformer();
            transformer.transform(new StreamSource(xmlbuf), new StreamResult(pw));
         }

         System.out.println("Primed the pump!");
         this.showStatus("Ready to go!");
      } catch (Exception var10) {
         this.showStatus("Could not prime the pump!");
         System.out.println("Could not prime the pump!");
         var10.printStackTrace();
      }

   }

   public void paint(Graphics g) {
   }

   public void stop() {
      if (null != this.m_trustedWorker) {
         this.m_trustedWorker.stop();
         this.m_trustedWorker = null;
      }

      this.m_styleURLOfCached = null;
      this.m_documentURLOfCached = null;
   }

   public void destroy() {
      if (null != this.m_trustedWorker) {
         this.m_trustedWorker.stop();
         this.m_trustedWorker = null;
      }

      this.m_styleURLOfCached = null;
      this.m_documentURLOfCached = null;
   }

   public void setStyleURL(String urlString) {
      this.m_styleURL = urlString;
   }

   public void setDocumentURL(String urlString) {
      this.m_documentURL = urlString;
   }

   public void freeCache() {
      this.m_styleURLOfCached = null;
      this.m_documentURLOfCached = null;
   }

   public void setStyleSheetAttribute(String nameOfIDAttrOfElemToModify, String elemId, String attrName, String value) {
      this.m_nameOfIDAttrOfElemToModify = nameOfIDAttrOfElemToModify;
      this.m_elemIdToModify = elemId;
      this.m_attrNameToSet = attrName;
      this.m_attrValueToSet = value;
   }

   public void setStylesheetParam(String key, String expr) {
      this.m_parameters.put(key, expr);
   }

   public String escapeString(String s) {
      StringBuffer sb = new StringBuffer();
      int length = s.length();

      for(int i = 0; i < length; ++i) {
         char ch = s.charAt(i);
         if ('<' == ch) {
            sb.append("&lt;");
         } else if ('>' == ch) {
            sb.append("&gt;");
         } else if ('&' == ch) {
            sb.append("&amp;");
         } else if ('\ud800' <= ch && ch < '\udc00') {
            if (i + 1 >= length) {
               throw new RuntimeException(XSLMessages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(ch)}));
            }

            ++i;
            int next = s.charAt(i);
            if (56320 > next || next >= 57344) {
               throw new RuntimeException(XSLMessages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(ch) + " " + Integer.toHexString(next)}));
            }

            next = (ch - '\ud800' << 10) + next - '\udc00' + 65536;
            sb.append("&#x");
            sb.append(Integer.toHexString(next));
            sb.append(";");
         } else {
            sb.append(ch);
         }
      }

      return sb.toString();
   }

   public String getHtmlText() {
      this.m_trustedAgent.m_getData = true;
      this.m_callThread = Thread.currentThread();

      try {
         Thread var1 = this.m_callThread;
         synchronized(var1) {
            this.m_callThread.wait();
         }
      } catch (InterruptedException var4) {
         System.out.println(var4.getMessage());
      }

      return this.m_htmlText;
   }

   public String getTreeAsText(String treeURL) throws IOException {
      this.m_treeURL = treeURL;
      this.m_trustedAgent.m_getData = true;
      this.m_trustedAgent.m_getSource = true;
      this.m_callThread = Thread.currentThread();

      try {
         Thread var2 = this.m_callThread;
         synchronized(var2) {
            this.m_callThread.wait();
         }
      } catch (InterruptedException var5) {
         System.out.println(var5.getMessage());
      }

      return this.m_sourceText;
   }

   private String getSource() throws TransformerException {
      StringWriter osw = new StringWriter();
      PrintWriter pw = new PrintWriter(osw, false);
      String text = "";

      try {
         URL docURL = new URL(this.m_documentBase, this.m_treeURL);
         TransformerFactory var5 = this.m_tfactory;
         synchronized(var5) {
            Transformer transformer = this.m_tfactory.newTransformer();
            StreamSource source = new StreamSource(docURL.toString());
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            text = osw.toString();
         }
      } catch (MalformedURLException var11) {
         var11.printStackTrace();
         throw new RuntimeException(var11.getMessage());
      } catch (Exception var12) {
         var12.printStackTrace();
      }

      return text;
   }

   public String getSourceTreeAsText() throws Exception {
      return this.getTreeAsText(this.m_documentURL);
   }

   public String getStyleTreeAsText() throws Exception {
      return this.getTreeAsText(this.m_styleURL);
   }

   public String getResultTreeAsText() throws Exception {
      return this.escapeString(this.getHtmlText());
   }

   public String transformToHtml(String doc, String style) {
      if (null != doc) {
         this.m_documentURL = doc;
      }

      if (null != style) {
         this.m_styleURL = style;
      }

      return this.getHtmlText();
   }

   public String transformToHtml(String doc) {
      if (null != doc) {
         this.m_documentURL = doc;
      }

      this.m_styleURL = null;
      return this.getHtmlText();
   }

   private String processTransformation() throws TransformerException {
      String htmlData = null;
      this.showStatus("Waiting for Transformer and Parser to finish loading and JITing...");
      TransformerFactory var2 = this.m_tfactory;
      synchronized(var2) {
         URL documentURL = null;
         URL styleURL = null;
         StringWriter osw = new StringWriter();
         PrintWriter pw = new PrintWriter(osw, false);
         StreamResult result = new StreamResult(pw);
         this.showStatus("Begin Transformation...");

         try {
            documentURL = new URL(this.m_codeBase, this.m_documentURL);
            StreamSource xmlSource = new StreamSource(documentURL.toString());
            styleURL = new URL(this.m_codeBase, this.m_styleURL);
            StreamSource xslSource = new StreamSource(styleURL.toString());
            Transformer transformer = this.m_tfactory.newTransformer(xslSource);
            Enumeration m_keys = this.m_parameters.keys();

            while(true) {
               if (!m_keys.hasMoreElements()) {
                  transformer.transform(xmlSource, result);
                  break;
               }

               Object key = m_keys.nextElement();
               Object expression = this.m_parameters.get(key);
               transformer.setParameter((String)key, expression);
            }
         } catch (TransformerConfigurationException var15) {
            var15.printStackTrace();
            throw new RuntimeException(var15.getMessage());
         } catch (MalformedURLException var16) {
            var16.printStackTrace();
            throw new RuntimeException(var16.getMessage());
         }

         this.showStatus("Transformation Done!");
         htmlData = osw.toString();
         return htmlData;
      }
   }

   private void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
      inStream.defaultReadObject();
      this.m_tfactory = TransformerFactory.newInstance();
   }

   class TrustedAgent implements Runnable {
      public boolean m_getData = false;
      public boolean m_getSource = false;

      public void run() {
         while(true) {
            XSLTProcessorApplet.this.m_trustedWorker;
            Thread.yield();
            if (this.m_getData) {
               try {
                  this.m_getData = false;
                  XSLTProcessorApplet.this.m_htmlText = null;
                  XSLTProcessorApplet.this.m_sourceText = null;
                  if (this.m_getSource) {
                     this.m_getSource = false;
                     XSLTProcessorApplet.this.m_sourceText = XSLTProcessorApplet.this.getSource();
                  } else {
                     XSLTProcessorApplet.this.m_htmlText = XSLTProcessorApplet.this.processTransformation();
                  }
               } catch (Exception var13) {
                  var13.printStackTrace();
               } finally {
                  Thread var4 = XSLTProcessorApplet.this.m_callThread;
                  synchronized(var4) {
                     XSLTProcessorApplet.this.m_callThread.notify();
                  }
               }
            } else {
               try {
                  XSLTProcessorApplet.this.m_trustedWorker;
                  Thread.sleep(50L);
               } catch (InterruptedException var15) {
                  var15.printStackTrace();
               }
            }
         }
      }
   }
}
