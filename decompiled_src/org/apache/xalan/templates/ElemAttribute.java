package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.XML11Char;
import org.xml.sax.SAXException;

public class ElemAttribute extends ElemElement {
   static final long serialVersionUID = 8817220961566919187L;

   public int getXSLToken() {
      return 48;
   }

   public String getNodeName() {
      return "attribute";
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      SerializationHandler rhandler = transformer.getSerializationHandler();
      super.execute(transformer);
   }

   protected String resolvePrefix(SerializationHandler rhandler, String prefix, String nodeNamespace) throws TransformerException {
      if (null != prefix && (prefix.length() == 0 || prefix.equals("xmlns"))) {
         prefix = rhandler.getPrefix(nodeNamespace);
         if (null == prefix || prefix.length() == 0 || prefix.equals("xmlns")) {
            if (nodeNamespace.length() > 0) {
               NamespaceMappings prefixMapping = rhandler.getNamespaceMappings();
               prefix = prefixMapping.generateNextPrefix();
            } else {
               prefix = "";
            }
         }
      }

      return prefix;
   }

   protected boolean validateNodeName(String nodeName) {
      if (null == nodeName) {
         return false;
      } else {
         return nodeName.equals("xmlns") ? false : XML11Char.isXML11ValidQName(nodeName);
      }
   }

   void constructNode(String nodeName, String prefix, String nodeNamespace, TransformerImpl transformer) throws TransformerException {
      if (null != nodeName && nodeName.length() > 0) {
         SerializationHandler rhandler = transformer.getSerializationHandler();
         String val = transformer.transformToString(this);

         try {
            String localName = QName.getLocalPart(nodeName);
            if (prefix != null && prefix.length() > 0) {
               rhandler.addAttribute(nodeNamespace, localName, nodeName, "CDATA", val, true);
            } else {
               rhandler.addAttribute("", localName, nodeName, "CDATA", val, true);
            }
         } catch (SAXException var8) {
         }
      }

   }

   public ElemTemplateElement appendChild(ElemTemplateElement newChild) {
      int type = newChild.getXSLToken();
      switch (type) {
         default:
            this.error("ER_CANNOT_ADD", new Object[]{newChild.getNodeName(), this.getNodeName()});
         case 9:
         case 17:
         case 28:
         case 30:
         case 35:
         case 36:
         case 37:
         case 42:
         case 50:
         case 72:
         case 73:
         case 74:
         case 75:
         case 78:
            return super.appendChild(newChild);
      }
   }

   public void setName(AVT v) {
      if (v.isSimple() && v.getSimpleString().equals("xmlns")) {
         throw new IllegalArgumentException();
      } else {
         super.setName(v);
      }
   }
}
