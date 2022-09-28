package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.XML11Char;
import org.apache.xpath.XPathContext;
import org.xml.sax.SAXException;

public class ElemElement extends ElemUse {
   static final long serialVersionUID = -324619535592435183L;
   protected AVT m_name_avt = null;
   protected AVT m_namespace_avt = null;

   public void setName(AVT v) {
      this.m_name_avt = v;
   }

   public AVT getName() {
      return this.m_name_avt;
   }

   public void setNamespace(AVT v) {
      this.m_namespace_avt = v;
   }

   public AVT getNamespace() {
      return this.m_namespace_avt;
   }

   public void compose(StylesheetRoot sroot) throws TransformerException {
      super.compose(sroot);
      StylesheetRoot.ComposeState cstate = sroot.getComposeState();
      Vector vnames = cstate.getVariableNames();
      if (null != this.m_name_avt) {
         this.m_name_avt.fixupVariables(vnames, cstate.getGlobalsSize());
      }

      if (null != this.m_namespace_avt) {
         this.m_namespace_avt.fixupVariables(vnames, cstate.getGlobalsSize());
      }

   }

   public int getXSLToken() {
      return 46;
   }

   public String getNodeName() {
      return "element";
   }

   protected String resolvePrefix(SerializationHandler rhandler, String prefix, String nodeNamespace) throws TransformerException {
      return prefix;
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
      }

      SerializationHandler rhandler = transformer.getSerializationHandler();
      XPathContext xctxt = transformer.getXPathContext();
      int sourceNode = xctxt.getCurrentNode();
      String nodeName = this.m_name_avt == null ? null : this.m_name_avt.evaluate(xctxt, sourceNode, this);
      String prefix = null;
      String nodeNamespace = "";
      if (nodeName != null && !this.m_name_avt.isSimple() && !XML11Char.isXML11ValidQName(nodeName)) {
         transformer.getMsgMgr().warn(this, "WG_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"name", nodeName});
         nodeName = null;
      } else if (nodeName != null) {
         prefix = QName.getPrefixPart(nodeName);
         if (null != this.m_namespace_avt) {
            nodeNamespace = this.m_namespace_avt.evaluate(xctxt, sourceNode, this);
            if (null != nodeNamespace && (prefix == null || prefix.length() <= 0 || nodeNamespace.length() != 0)) {
               prefix = this.resolvePrefix(rhandler, prefix, nodeNamespace);
               if (null == prefix) {
                  prefix = "";
               }

               if (prefix.length() > 0) {
                  nodeName = prefix + ":" + QName.getLocalPart(nodeName);
               } else {
                  nodeName = QName.getLocalPart(nodeName);
               }
            } else {
               transformer.getMsgMgr().error(this, "ER_NULL_URI_NAMESPACE");
            }
         } else {
            try {
               nodeNamespace = this.getNamespaceForPrefix(prefix);
               if (null == nodeNamespace && prefix.length() == 0) {
                  nodeNamespace = "";
               } else if (null == nodeNamespace) {
                  transformer.getMsgMgr().warn(this, "WG_COULD_NOT_RESOLVE_PREFIX", new Object[]{prefix});
                  nodeName = null;
               }
            } catch (Exception var9) {
               transformer.getMsgMgr().warn(this, "WG_COULD_NOT_RESOLVE_PREFIX", new Object[]{prefix});
               nodeName = null;
            }
         }
      }

      this.constructNode(nodeName, prefix, nodeNamespace, transformer);
      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
      }

   }

   void constructNode(String nodeName, String prefix, String nodeNamespace, TransformerImpl transformer) throws TransformerException {
      try {
         SerializationHandler rhandler = transformer.getResultTreeHandler();
         boolean shouldAddAttrs;
         if (null == nodeName) {
            shouldAddAttrs = false;
         } else {
            if (null != prefix) {
               rhandler.startPrefixMapping(prefix, nodeNamespace, true);
            }

            rhandler.startElement(nodeNamespace, QName.getLocalPart(nodeName), nodeName);
            super.execute(transformer);
            shouldAddAttrs = true;
         }

         transformer.executeChildTemplates(this, shouldAddAttrs);
         if (null != nodeName) {
            rhandler.endElement(nodeNamespace, QName.getLocalPart(nodeName), nodeName);
            if (null != prefix) {
               rhandler.endPrefixMapping(prefix);
            }
         }

      } catch (SAXException var7) {
         throw new TransformerException(var7);
      }
   }

   protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs) {
      if (callAttrs) {
         if (null != this.m_name_avt) {
            this.m_name_avt.callVisitors(visitor);
         }

         if (null != this.m_namespace_avt) {
            this.m_namespace_avt.callVisitors(visitor);
         }
      }

      super.callChildVisitors(visitor, callAttrs);
   }
}
