package org.apache.xalan.lib;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.extensions.XSLProcessorContext;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.templates.ElemLiteralResult;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class PipeDocument {
   public void pipeDocument(XSLProcessorContext context, ElemExtensionCall elem) throws TransformerException, TransformerConfigurationException, SAXException, IOException, FileNotFoundException {
      SAXTransformerFactory saxTFactory = (SAXTransformerFactory)TransformerFactory.newInstance();
      String source = elem.getAttribute("source", context.getContextNode(), context.getTransformer());
      TransformerImpl transImpl = context.getTransformer();
      String baseURLOfSource = transImpl.getBaseURLOfSource();
      String absSourceURL = SystemIDResolver.getAbsoluteURI(source, baseURLOfSource);
      String target = elem.getAttribute("target", context.getContextNode(), context.getTransformer());
      XPathContext xctxt = context.getTransformer().getXPathContext();
      int xt = xctxt.getDTMHandleFromNode(context.getContextNode());
      String sysId = elem.getSystemId();
      NodeList ssNodes = null;
      NodeList paramNodes = null;
      Node ssNode = null;
      Node paramNode = null;
      if (elem.hasChildNodes()) {
         ssNodes = elem.getChildNodes();
         Vector vTHandler = new Vector(ssNodes.getLength());

         for(int i = 0; i < ssNodes.getLength(); ++i) {
            ssNode = ssNodes.item(i);
            if (ssNode.getNodeType() == 1 && ((Element)ssNode).getTagName().equals("stylesheet") && ssNode instanceof ElemLiteralResult) {
               AVT avt = ((ElemLiteralResult)ssNode).getLiteralResultAttribute("href");
               String href = avt.evaluate(xctxt, xt, elem);
               String absURI = SystemIDResolver.getAbsoluteURI(href, sysId);
               Templates tmpl = saxTFactory.newTemplates(new StreamSource(absURI));
               TransformerHandler tHandler = saxTFactory.newTransformerHandler(tmpl);
               Transformer trans = tHandler.getTransformer();
               vTHandler.addElement(tHandler);
               paramNodes = ssNode.getChildNodes();

               for(int j = 0; j < paramNodes.getLength(); ++j) {
                  paramNode = paramNodes.item(j);
                  if (paramNode.getNodeType() == 1 && ((Element)paramNode).getTagName().equals("param") && paramNode instanceof ElemLiteralResult) {
                     avt = ((ElemLiteralResult)paramNode).getLiteralResultAttribute("name");
                     String pName = avt.evaluate(xctxt, xt, elem);
                     avt = ((ElemLiteralResult)paramNode).getLiteralResultAttribute("value");
                     String pValue = avt.evaluate(xctxt, xt, elem);
                     trans.setParameter(pName, pValue);
                  }
               }
            }
         }

         this.usePipe(vTHandler, absSourceURL, target);
      }

   }

   public void usePipe(Vector vTHandler, String source, String target) throws TransformerException, TransformerConfigurationException, FileNotFoundException, IOException, SAXException, SAXNotRecognizedException {
      XMLReader reader = XMLReaderFactory.createXMLReader();
      TransformerHandler tHFirst = (TransformerHandler)vTHandler.firstElement();
      reader.setContentHandler(tHFirst);
      reader.setProperty("http://xml.org/sax/properties/lexical-handler", tHFirst);

      TransformerHandler tHLast;
      for(int i = 1; i < vTHandler.size(); ++i) {
         tHLast = (TransformerHandler)vTHandler.elementAt(i - 1);
         TransformerHandler tHTo = (TransformerHandler)vTHandler.elementAt(i);
         tHLast.setResult(new SAXResult(tHTo));
      }

      tHLast = (TransformerHandler)vTHandler.lastElement();
      Transformer trans = tHLast.getTransformer();
      Properties outputProps = trans.getOutputProperties();
      Serializer serializer = SerializerFactory.getSerializer(outputProps);
      FileOutputStream out = new FileOutputStream(target);

      try {
         serializer.setOutputStream(out);
         tHLast.setResult(new SAXResult(serializer.asContentHandler()));
         reader.parse(source);
      } finally {
         if (out != null) {
            out.close();
         }

      }

   }
}
