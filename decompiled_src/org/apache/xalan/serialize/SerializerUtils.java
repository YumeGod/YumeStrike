package org.apache.xalan.serialize;

import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.xml.sax.SAXException;

public class SerializerUtils {
   public static void addAttribute(SerializationHandler handler, int attr) throws TransformerException {
      TransformerImpl transformer = (TransformerImpl)handler.getTransformer();
      DTM dtm = transformer.getXPathContext().getDTM(attr);
      if (!isDefinedNSDecl(handler, attr, dtm)) {
         String ns = dtm.getNamespaceURI(attr);
         if (ns == null) {
            ns = "";
         }

         try {
            handler.addAttribute(ns, dtm.getLocalName(attr), dtm.getNodeName(attr), "CDATA", dtm.getNodeValue(attr), false);
         } catch (SAXException var6) {
         }

      }
   }

   public static void addAttributes(SerializationHandler handler, int src) throws TransformerException {
      TransformerImpl transformer = (TransformerImpl)handler.getTransformer();
      DTM dtm = transformer.getXPathContext().getDTM(src);

      for(int node = dtm.getFirstAttribute(src); -1 != node; node = dtm.getNextAttribute(node)) {
         addAttribute(handler, node);
      }

   }

   public static void outputResultTreeFragment(SerializationHandler handler, XObject obj, XPathContext support) throws SAXException {
      int doc = obj.rtf();
      DTM dtm = support.getDTM(doc);
      if (null != dtm) {
         for(int n = dtm.getFirstChild(doc); -1 != n; n = dtm.getNextSibling(n)) {
            handler.flushPending();
            if (dtm.getNodeType(n) == 1 && dtm.getNamespaceURI(n) == null) {
               handler.startPrefixMapping("", "");
            }

            dtm.dispatchToEvents(n, handler);
         }
      }

   }

   public static void processNSDecls(SerializationHandler handler, int src, int type, DTM dtm) throws TransformerException {
      try {
         String prefix;
         String srcURI;
         if (type == 1) {
            for(int namespace = dtm.getFirstNamespaceNode(src, true); -1 != namespace; namespace = dtm.getNextNamespaceNode(src, namespace, true)) {
               prefix = dtm.getNodeNameX(namespace);
               srcURI = handler.getNamespaceURIFromPrefix(prefix);
               String srcURI = dtm.getNodeValue(namespace);
               if (!srcURI.equalsIgnoreCase(srcURI)) {
                  handler.startPrefixMapping(prefix, srcURI, false);
               }
            }
         } else if (type == 13) {
            String prefix = dtm.getNodeNameX(src);
            prefix = handler.getNamespaceURIFromPrefix(prefix);
            srcURI = dtm.getNodeValue(src);
            if (!srcURI.equalsIgnoreCase(prefix)) {
               handler.startPrefixMapping(prefix, srcURI, false);
            }
         }

      } catch (SAXException var8) {
         throw new TransformerException(var8);
      }
   }

   public static boolean isDefinedNSDecl(SerializationHandler serializer, int attr, DTM dtm) {
      if (13 == dtm.getNodeType(attr)) {
         String prefix = dtm.getNodeNameX(attr);
         String uri = serializer.getNamespaceURIFromPrefix(prefix);
         if (null != uri && uri.equals(dtm.getStringValue(attr))) {
            return true;
         }
      }

      return false;
   }

   public static void ensureNamespaceDeclDeclared(SerializationHandler handler, DTM dtm, int namespace) throws SAXException {
      String uri = dtm.getNodeValue(namespace);
      String prefix = dtm.getNodeNameX(namespace);
      if (uri != null && uri.length() > 0 && null != prefix) {
         NamespaceMappings ns = handler.getNamespaceMappings();
         if (ns != null) {
            String foundURI = ns.lookupNamespace(prefix);
            if (null == foundURI || !foundURI.equals(uri)) {
               handler.startPrefixMapping(prefix, uri, false);
            }
         }
      }

   }
}
