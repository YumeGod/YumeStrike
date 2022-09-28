package org.apache.xalan.templates;

import java.util.Enumeration;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.StringVector;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

public class ElemLiteralResult extends ElemUse {
   static final long serialVersionUID = -8703409074421657260L;
   private static final String EMPTYSTRING = "";
   private boolean isLiteralResultAsStylesheet = false;
   private Vector m_avts = null;
   private Vector m_xslAttr = null;
   private String m_namespace;
   private String m_localName;
   private String m_rawName;
   private StringVector m_ExtensionElementURIs;
   private String m_version;
   private StringVector m_excludeResultPrefixes;

   public void setIsLiteralResultAsStylesheet(boolean b) {
      this.isLiteralResultAsStylesheet = b;
   }

   public boolean getIsLiteralResultAsStylesheet() {
      return this.isLiteralResultAsStylesheet;
   }

   public void compose(StylesheetRoot sroot) throws TransformerException {
      super.compose(sroot);
      StylesheetRoot.ComposeState cstate = sroot.getComposeState();
      Vector vnames = cstate.getVariableNames();
      if (null != this.m_avts) {
         int nAttrs = this.m_avts.size();

         for(int i = nAttrs - 1; i >= 0; --i) {
            AVT avt = (AVT)this.m_avts.elementAt(i);
            avt.fixupVariables(vnames, cstate.getGlobalsSize());
         }
      }

   }

   public void addLiteralResultAttribute(AVT avt) {
      if (null == this.m_avts) {
         this.m_avts = new Vector();
      }

      this.m_avts.addElement(avt);
   }

   public void addLiteralResultAttribute(String att) {
      if (null == this.m_xslAttr) {
         this.m_xslAttr = new Vector();
      }

      this.m_xslAttr.addElement(att);
   }

   public void setXmlSpace(AVT avt) {
      this.addLiteralResultAttribute(avt);
      String val = avt.getSimpleString();
      if (val.equals("default")) {
         super.setXmlSpace(2);
      } else if (val.equals("preserve")) {
         super.setXmlSpace(1);
      }

   }

   public AVT getLiteralResultAttributeNS(String namespaceURI, String localName) {
      if (null != this.m_avts) {
         int nAttrs = this.m_avts.size();

         for(int i = nAttrs - 1; i >= 0; --i) {
            AVT avt = (AVT)this.m_avts.elementAt(i);
            if (avt.getName().equals(localName) && avt.getURI().equals(namespaceURI)) {
               return avt;
            }
         }
      }

      return null;
   }

   public String getAttributeNS(String namespaceURI, String localName) {
      AVT avt = this.getLiteralResultAttributeNS(namespaceURI, localName);
      return null != avt ? avt.getSimpleString() : "";
   }

   public AVT getLiteralResultAttribute(String name) {
      if (null != this.m_avts) {
         int nAttrs = this.m_avts.size();
         String namespace = null;

         for(int i = nAttrs - 1; i >= 0; --i) {
            AVT avt = (AVT)this.m_avts.elementAt(i);
            namespace = avt.getURI();
            if (namespace != null && !namespace.equals("") && (namespace + ":" + avt.getName()).equals(name) || (namespace == null || namespace.equals("")) && avt.getRawName().equals(name)) {
               return avt;
            }
         }
      }

      return null;
   }

   public String getAttribute(String rawName) {
      AVT avt = this.getLiteralResultAttribute(rawName);
      return null != avt ? avt.getSimpleString() : "";
   }

   public boolean containsExcludeResultPrefix(String prefix, String uri) {
      if (uri == null || null == this.m_excludeResultPrefixes && null == this.m_ExtensionElementURIs) {
         return super.containsExcludeResultPrefix(prefix, uri);
      } else {
         if (prefix.length() == 0) {
            prefix = "#default";
         }

         if (this.m_excludeResultPrefixes != null) {
            for(int i = 0; i < this.m_excludeResultPrefixes.size(); ++i) {
               if (uri.equals(this.getNamespaceForPrefix(this.m_excludeResultPrefixes.elementAt(i)))) {
                  return true;
               }
            }
         }

         return this.m_ExtensionElementURIs != null && this.m_ExtensionElementURIs.contains(uri) ? true : super.containsExcludeResultPrefix(prefix, uri);
      }
   }

   public void resolvePrefixTables() throws TransformerException {
      super.resolvePrefixTables();
      StylesheetRoot stylesheet = this.getStylesheetRoot();
      if (null != this.m_namespace && this.m_namespace.length() > 0) {
         NamespaceAlias nsa = stylesheet.getNamespaceAliasComposed(this.m_namespace);
         if (null != nsa) {
            this.m_namespace = nsa.getResultNamespace();
            String resultPrefix = nsa.getStylesheetPrefix();
            if (null != resultPrefix && resultPrefix.length() > 0) {
               this.m_rawName = resultPrefix + ":" + this.m_localName;
            } else {
               this.m_rawName = this.m_localName;
            }
         }
      }

      if (null != this.m_avts) {
         int n = this.m_avts.size();

         for(int i = 0; i < n; ++i) {
            AVT avt = (AVT)this.m_avts.elementAt(i);
            String ns = avt.getURI();
            if (null != ns && ns.length() > 0) {
               NamespaceAlias nsa = stylesheet.getNamespaceAliasComposed(this.m_namespace);
               if (null != nsa) {
                  String namespace = nsa.getResultNamespace();
                  String resultPrefix = nsa.getStylesheetPrefix();
                  String rawName = avt.getName();
                  if (null != resultPrefix && resultPrefix.length() > 0) {
                     rawName = resultPrefix + ":" + rawName;
                  }

                  avt.setURI(namespace);
                  avt.setRawName(rawName);
               }
            }
         }
      }

   }

   boolean needToCheckExclude() {
      if (null == this.m_excludeResultPrefixes && null == super.m_prefixTable && this.m_ExtensionElementURIs == null) {
         return false;
      } else {
         if (null == super.m_prefixTable) {
            super.m_prefixTable = new Vector();
         }

         return true;
      }
   }

   public void setNamespace(String ns) {
      if (null == ns) {
         ns = "";
      }

      this.m_namespace = ns;
   }

   public String getNamespace() {
      return this.m_namespace;
   }

   public void setLocalName(String localName) {
      this.m_localName = localName;
   }

   public String getLocalName() {
      return this.m_localName;
   }

   public void setRawName(String rawName) {
      this.m_rawName = rawName;
   }

   public String getRawName() {
      return this.m_rawName;
   }

   public String getPrefix() {
      int len = this.m_rawName.length() - this.m_localName.length() - 1;
      return len > 0 ? this.m_rawName.substring(0, len) : "";
   }

   public void setExtensionElementPrefixes(StringVector v) {
      this.m_ExtensionElementURIs = v;
   }

   public NamedNodeMap getAttributes() {
      return new LiteralElementAttributes();
   }

   public String getExtensionElementPrefix(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_ExtensionElementURIs) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return this.m_ExtensionElementURIs.elementAt(i);
      }
   }

   public int getExtensionElementPrefixCount() {
      return null != this.m_ExtensionElementURIs ? this.m_ExtensionElementURIs.size() : 0;
   }

   public boolean containsExtensionElementURI(String uri) {
      return null == this.m_ExtensionElementURIs ? false : this.m_ExtensionElementURIs.contains(uri);
   }

   public int getXSLToken() {
      return 77;
   }

   public String getNodeName() {
      return this.m_rawName;
   }

   public void setVersion(String v) {
      this.m_version = v;
   }

   public String getVersion() {
      return this.m_version;
   }

   public void setExcludeResultPrefixes(StringVector v) {
      this.m_excludeResultPrefixes = v;
   }

   private boolean excludeResultNSDecl(String prefix, String uri) throws TransformerException {
      return null != this.m_excludeResultPrefixes ? this.containsExcludeResultPrefix(prefix, uri) : false;
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      SerializationHandler rhandler = transformer.getSerializationHandler();

      try {
         if (transformer.getDebug()) {
            rhandler.flushPending();
            transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
         }

         rhandler.startPrefixMapping(this.getPrefix(), this.getNamespace());
         this.executeNSDecls(transformer);
         rhandler.startElement(this.getNamespace(), this.getLocalName(), this.getRawName());
      } catch (SAXException var14) {
         throw new TransformerException(var14);
      }

      TransformerException tException = null;

      try {
         super.execute(transformer);
         if (null != this.m_avts) {
            int nAttrs = this.m_avts.size();

            for(int i = nAttrs - 1; i >= 0; --i) {
               AVT avt = (AVT)this.m_avts.elementAt(i);
               XPathContext xctxt = transformer.getXPathContext();
               int sourceNode = xctxt.getCurrentNode();
               String stringedValue = avt.evaluate(xctxt, sourceNode, this);
               if (null != stringedValue) {
                  rhandler.addAttribute(avt.getURI(), avt.getName(), avt.getRawName(), "CDATA", stringedValue, false);
               }
            }
         }

         transformer.executeChildTemplates(this, true);
      } catch (TransformerException var12) {
         tException = var12;
      } catch (SAXException var13) {
         tException = new TransformerException(var13);
      }

      try {
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }

         rhandler.endElement(this.getNamespace(), this.getLocalName(), this.getRawName());
      } catch (SAXException var11) {
         if (tException != null) {
            throw tException;
         }

         throw new TransformerException(var11);
      }

      if (tException != null) {
         throw tException;
      } else {
         this.unexecuteNSDecls(transformer);

         try {
            rhandler.endPrefixMapping(this.getPrefix());
         } catch (SAXException var10) {
            throw new TransformerException(var10);
         }
      }
   }

   public Enumeration enumerateLiteralResultAttributes() {
      return null == this.m_avts ? null : this.m_avts.elements();
   }

   protected boolean accept(XSLTVisitor visitor) {
      return visitor.visitLiteralResultElement(this);
   }

   protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs) {
      if (callAttrs && null != this.m_avts) {
         int nAttrs = this.m_avts.size();

         for(int i = nAttrs - 1; i >= 0; --i) {
            AVT avt = (AVT)this.m_avts.elementAt(i);
            avt.callVisitors(visitor);
         }
      }

      super.callChildVisitors(visitor, callAttrs);
   }

   public void throwDOMException(short code, String msg) {
      String themsg = XSLMessages.createMessage(msg, (Object[])null);
      throw new DOMException(code, themsg);
   }

   public class Attribute implements Attr {
      private AVT m_attribute;
      private Element m_owner = null;

      public Attribute(AVT avt, Element elem) {
         this.m_attribute = avt;
         this.m_owner = elem;
      }

      public Node appendChild(Node newChild) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
         return null;
      }

      public Node cloneNode(boolean deep) {
         return ElemLiteralResult.this.new Attribute(this.m_attribute, this.m_owner);
      }

      public NamedNodeMap getAttributes() {
         return null;
      }

      public NodeList getChildNodes() {
         return new NodeList() {
            public int getLength() {
               return 0;
            }

            public Node item(int index) {
               return null;
            }
         };
      }

      public Node getFirstChild() {
         return null;
      }

      public Node getLastChild() {
         return null;
      }

      public String getLocalName() {
         return this.m_attribute.getName();
      }

      public String getNamespaceURI() {
         String uri = this.m_attribute.getURI();
         return uri.equals("") ? null : uri;
      }

      public Node getNextSibling() {
         return null;
      }

      public String getNodeName() {
         String uri = this.m_attribute.getURI();
         String localName = this.getLocalName();
         return uri.equals("") ? localName : uri + ":" + localName;
      }

      public short getNodeType() {
         return 2;
      }

      public String getNodeValue() throws DOMException {
         return this.m_attribute.getSimpleString();
      }

      public Document getOwnerDocument() {
         return this.m_owner.getOwnerDocument();
      }

      public Node getParentNode() {
         return this.m_owner;
      }

      public String getPrefix() {
         String uri = this.m_attribute.getURI();
         String rawName = this.m_attribute.getRawName();
         return uri.equals("") ? null : rawName.substring(0, rawName.indexOf(":"));
      }

      public Node getPreviousSibling() {
         return null;
      }

      public boolean hasAttributes() {
         return false;
      }

      public boolean hasChildNodes() {
         return false;
      }

      public Node insertBefore(Node newChild, Node refChild) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
         return null;
      }

      public boolean isSupported(String feature, String version) {
         return false;
      }

      public void normalize() {
      }

      public Node removeChild(Node oldChild) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
         return null;
      }

      public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
         return null;
      }

      public void setNodeValue(String nodeValue) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
      }

      public void setPrefix(String prefix) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
      }

      public String getName() {
         return this.m_attribute.getName();
      }

      public String getValue() {
         return this.m_attribute.getSimpleString();
      }

      public Element getOwnerElement() {
         return this.m_owner;
      }

      public boolean getSpecified() {
         return true;
      }

      public void setValue(String value) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
      }

      public TypeInfo getSchemaTypeInfo() {
         return null;
      }

      public boolean isId() {
         return false;
      }

      public Object setUserData(String key, Object data, UserDataHandler handler) {
         return this.getOwnerDocument().setUserData(key, data, handler);
      }

      public Object getUserData(String key) {
         return this.getOwnerDocument().getUserData(key);
      }

      public Object getFeature(String feature, String version) {
         return this.isSupported(feature, version) ? this : null;
      }

      public boolean isEqualNode(Node arg) {
         return arg == this;
      }

      public String lookupNamespaceURI(String specifiedPrefix) {
         return null;
      }

      public boolean isDefaultNamespace(String namespaceURI) {
         return false;
      }

      public String lookupPrefix(String namespaceURI) {
         return null;
      }

      public boolean isSameNode(Node other) {
         return this == other;
      }

      public void setTextContent(String textContent) throws DOMException {
         this.setNodeValue(textContent);
      }

      public String getTextContent() throws DOMException {
         return this.getNodeValue();
      }

      public short compareDocumentPosition(Node other) throws DOMException {
         return 0;
      }

      public String getBaseURI() {
         return null;
      }
   }

   public class LiteralElementAttributes implements NamedNodeMap {
      private int m_count = -1;

      public int getLength() {
         if (this.m_count == -1) {
            if (null != ElemLiteralResult.this.m_avts) {
               this.m_count = ElemLiteralResult.this.m_avts.size();
            } else {
               this.m_count = 0;
            }
         }

         return this.m_count;
      }

      public Node getNamedItem(String name) {
         if (this.getLength() == 0) {
            return null;
         } else {
            String uri = null;
            String localName = name;
            int index = name.indexOf(":");
            if (-1 != index) {
               uri = name.substring(0, index);
               localName = name.substring(index + 1);
            }

            Node retNode = null;
            Enumeration eum = ElemLiteralResult.this.m_avts.elements();

            while(eum.hasMoreElements()) {
               AVT avt = (AVT)eum.nextElement();
               if (localName.equals(avt.getName())) {
                  String nsURI = avt.getURI();
                  if (uri == null && nsURI == null || uri != null && uri.equals(nsURI)) {
                     retNode = ElemLiteralResult.this.new Attribute(avt, ElemLiteralResult.this);
                     break;
                  }
               }
            }

            return retNode;
         }
      }

      public Node getNamedItemNS(String namespaceURI, String localName) {
         if (this.getLength() == 0) {
            return null;
         } else {
            Node retNode = null;
            Enumeration eum = ElemLiteralResult.this.m_avts.elements();

            while(eum.hasMoreElements()) {
               AVT avt = (AVT)eum.nextElement();
               if (localName.equals(avt.getName())) {
                  String nsURI = avt.getURI();
                  if (namespaceURI == null && nsURI == null || namespaceURI != null && namespaceURI.equals(nsURI)) {
                     retNode = ElemLiteralResult.this.new Attribute(avt, ElemLiteralResult.this);
                     break;
                  }
               }
            }

            return retNode;
         }
      }

      public Node item(int i) {
         return this.getLength() != 0 && i < ElemLiteralResult.this.m_avts.size() ? ElemLiteralResult.this.new Attribute((AVT)ElemLiteralResult.this.m_avts.elementAt(i), ElemLiteralResult.this) : null;
      }

      public Node removeNamedItem(String name) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
         return null;
      }

      public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
         return null;
      }

      public Node setNamedItem(Node arg) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
         return null;
      }

      public Node setNamedItemNS(Node arg) throws DOMException {
         ElemLiteralResult.this.throwDOMException((short)7, "NO_MODIFICATION_ALLOWED_ERR");
         return null;
      }
   }
}
