package org.apache.xalan.templates;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.UnImplNode;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.WhitespaceStrippingElementMatcher;
import org.apache.xpath.XPathContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;

public class ElemTemplateElement extends UnImplNode implements PrefixResolver, Serializable, ExpressionNode, WhitespaceStrippingElementMatcher, XSLTVisitable {
   static final long serialVersionUID = 4440018597841834447L;
   private int m_lineNumber;
   private int m_endLineNumber;
   private int m_columnNumber;
   private int m_endColumnNumber;
   private boolean m_defaultSpace = true;
   private boolean m_hasTextLitOnly = false;
   protected boolean m_hasVariableDecl = false;
   private Vector m_declaredPrefixes;
   Vector m_prefixTable;
   protected int m_docOrderNumber = -1;
   protected ElemTemplateElement m_parentNode;
   ElemTemplateElement m_nextSibling;
   ElemTemplateElement m_firstChild;
   private transient Node m_DOMBackPointer;

   public boolean isCompiledTemplate() {
      return false;
   }

   public int getXSLToken() {
      return -1;
   }

   public String getNodeName() {
      return "Unknown XSLT Element";
   }

   public String getLocalName() {
      return this.getNodeName();
   }

   public void runtimeInit(TransformerImpl transformer) throws TransformerException {
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
   }

   public StylesheetComposed getStylesheetComposed() {
      return this.m_parentNode.getStylesheetComposed();
   }

   public Stylesheet getStylesheet() {
      return null == this.m_parentNode ? null : this.m_parentNode.getStylesheet();
   }

   public StylesheetRoot getStylesheetRoot() {
      return this.m_parentNode.getStylesheetRoot();
   }

   public void recompose(StylesheetRoot root) throws TransformerException {
   }

   public void compose(StylesheetRoot sroot) throws TransformerException {
      this.resolvePrefixTables();
      ElemTemplateElement t = this.getFirstChildElem();
      this.m_hasTextLitOnly = t != null && t.getXSLToken() == 78 && t.getNextSiblingElem() == null;
      StylesheetRoot.ComposeState cstate = sroot.getComposeState();
      cstate.pushStackMark();
   }

   public void endCompose(StylesheetRoot sroot) throws TransformerException {
      StylesheetRoot.ComposeState cstate = sroot.getComposeState();
      cstate.popStackMark();
   }

   public void error(String msg, Object[] args) {
      String themsg = XSLMessages.createMessage(msg, args);
      throw new RuntimeException(XSLMessages.createMessage("ER_ELEMTEMPLATEELEM_ERR", new Object[]{themsg}));
   }

   public void error(String msg) {
      this.error(msg, (Object[])null);
   }

   public Node appendChild(Node newChild) throws DOMException {
      if (null == newChild) {
         this.error("ER_NULL_CHILD", (Object[])null);
      }

      ElemTemplateElement elem = (ElemTemplateElement)newChild;
      if (null == this.m_firstChild) {
         this.m_firstChild = elem;
      } else {
         ElemTemplateElement last = (ElemTemplateElement)this.getLastChild();
         last.m_nextSibling = elem;
      }

      elem.m_parentNode = this;
      return newChild;
   }

   public ElemTemplateElement appendChild(ElemTemplateElement elem) {
      if (null == elem) {
         this.error("ER_NULL_CHILD", (Object[])null);
      }

      if (null == this.m_firstChild) {
         this.m_firstChild = elem;
      } else {
         ElemTemplateElement last = this.getLastChildElem();
         last.m_nextSibling = elem;
      }

      elem.setParentElem(this);
      return elem;
   }

   public boolean hasChildNodes() {
      return null != this.m_firstChild;
   }

   public short getNodeType() {
      return 1;
   }

   public NodeList getChildNodes() {
      return this;
   }

   public ElemTemplateElement removeChild(ElemTemplateElement childETE) {
      if (childETE != null && childETE.m_parentNode == this) {
         if (childETE == this.m_firstChild) {
            this.m_firstChild = childETE.m_nextSibling;
         } else {
            ElemTemplateElement prev = childETE.getPreviousSiblingElem();
            prev.m_nextSibling = childETE.m_nextSibling;
         }

         childETE.m_parentNode = null;
         childETE.m_nextSibling = null;
         return childETE;
      } else {
         return null;
      }
   }

   public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
      if (oldChild != null && oldChild.getParentNode() == this) {
         ElemTemplateElement newChildElem = (ElemTemplateElement)newChild;
         ElemTemplateElement oldChildElem = (ElemTemplateElement)oldChild;
         ElemTemplateElement prev = (ElemTemplateElement)oldChildElem.getPreviousSibling();
         if (null != prev) {
            prev.m_nextSibling = newChildElem;
         }

         if (this.m_firstChild == oldChildElem) {
            this.m_firstChild = newChildElem;
         }

         newChildElem.m_parentNode = this;
         oldChildElem.m_parentNode = null;
         newChildElem.m_nextSibling = oldChildElem.m_nextSibling;
         oldChildElem.m_nextSibling = null;
         return newChildElem;
      } else {
         return null;
      }
   }

   public Node insertBefore(Node newChild, Node refChild) throws DOMException {
      if (null == refChild) {
         this.appendChild(newChild);
         return newChild;
      } else if (newChild == refChild) {
         return newChild;
      } else {
         Node node = this.m_firstChild;
         Node prev = null;
         boolean foundit = false;

         while(null != node) {
            if (newChild == node) {
               if (null != prev) {
                  ((ElemTemplateElement)prev).m_nextSibling = (ElemTemplateElement)((Node)node).getNextSibling();
               } else {
                  this.m_firstChild = (ElemTemplateElement)((Node)node).getNextSibling();
               }

               node = ((Node)node).getNextSibling();
            } else if (refChild == node) {
               if (null != prev) {
                  ((ElemTemplateElement)prev).m_nextSibling = (ElemTemplateElement)newChild;
               } else {
                  this.m_firstChild = (ElemTemplateElement)newChild;
               }

               ((ElemTemplateElement)newChild).m_nextSibling = (ElemTemplateElement)refChild;
               ((ElemTemplateElement)newChild).setParentElem(this);
               prev = newChild;
               node = ((Node)node).getNextSibling();
               foundit = true;
            } else {
               prev = node;
               node = ((Node)node).getNextSibling();
            }
         }

         if (!foundit) {
            throw new DOMException((short)8, "refChild was not found in insertBefore method!");
         } else {
            return newChild;
         }
      }
   }

   public ElemTemplateElement replaceChild(ElemTemplateElement newChildElem, ElemTemplateElement oldChildElem) {
      if (oldChildElem != null && oldChildElem.getParentElem() == this) {
         ElemTemplateElement prev = oldChildElem.getPreviousSiblingElem();
         if (null != prev) {
            prev.m_nextSibling = newChildElem;
         }

         if (this.m_firstChild == oldChildElem) {
            this.m_firstChild = newChildElem;
         }

         newChildElem.m_parentNode = this;
         oldChildElem.m_parentNode = null;
         newChildElem.m_nextSibling = oldChildElem.m_nextSibling;
         oldChildElem.m_nextSibling = null;
         return newChildElem;
      } else {
         return null;
      }
   }

   public int getLength() {
      int count = 0;

      for(ElemTemplateElement node = this.m_firstChild; node != null; node = node.m_nextSibling) {
         ++count;
      }

      return count;
   }

   public Node item(int index) {
      ElemTemplateElement node = this.m_firstChild;

      for(int i = 0; i < index && node != null; ++i) {
         node = node.m_nextSibling;
      }

      return node;
   }

   public Document getOwnerDocument() {
      return this.getStylesheet();
   }

   public ElemTemplate getOwnerXSLTemplate() {
      ElemTemplateElement el = this;
      int type = this.getXSLToken();

      while(null != el && type != 19) {
         el = el.getParentElem();
         if (null != el) {
            type = el.getXSLToken();
         }
      }

      return (ElemTemplate)el;
   }

   public String getTagName() {
      return this.getNodeName();
   }

   public boolean hasTextLitOnly() {
      return this.m_hasTextLitOnly;
   }

   public String getBaseIdentifier() {
      return this.getSystemId();
   }

   public int getEndLineNumber() {
      return this.m_endLineNumber;
   }

   public int getLineNumber() {
      return this.m_lineNumber;
   }

   public int getEndColumnNumber() {
      return this.m_endColumnNumber;
   }

   public int getColumnNumber() {
      return this.m_columnNumber;
   }

   public String getPublicId() {
      return null != this.m_parentNode ? this.m_parentNode.getPublicId() : null;
   }

   public String getSystemId() {
      Stylesheet sheet = this.getStylesheet();
      return sheet == null ? null : sheet.getHref();
   }

   public void setLocaterInfo(SourceLocator locator) {
      this.m_lineNumber = locator.getLineNumber();
      this.m_columnNumber = locator.getColumnNumber();
   }

   public void setEndLocaterInfo(SourceLocator locator) {
      this.m_endLineNumber = locator.getLineNumber();
      this.m_endColumnNumber = locator.getColumnNumber();
   }

   public boolean hasVariableDecl() {
      return this.m_hasVariableDecl;
   }

   public void setXmlSpace(int v) {
      this.m_defaultSpace = 2 == v;
   }

   public boolean getXmlSpace() {
      return this.m_defaultSpace;
   }

   public Vector getDeclaredPrefixes() {
      return this.m_declaredPrefixes;
   }

   public void setPrefixes(NamespaceSupport nsSupport) throws TransformerException {
      this.setPrefixes(nsSupport, false);
   }

   public void setPrefixes(NamespaceSupport nsSupport, boolean excludeXSLDecl) throws TransformerException {
      Enumeration decls = nsSupport.getDeclaredPrefixes();

      while(true) {
         String prefix;
         String uri;
         do {
            if (!decls.hasMoreElements()) {
               return;
            }

            prefix = (String)decls.nextElement();
            if (null == this.m_declaredPrefixes) {
               this.m_declaredPrefixes = new Vector();
            }

            uri = nsSupport.getURI(prefix);
         } while(excludeXSLDecl && uri.equals("http://www.w3.org/1999/XSL/Transform"));

         XMLNSDecl decl = new XMLNSDecl(prefix, uri, false);
         this.m_declaredPrefixes.addElement(decl);
      }
   }

   public String getNamespaceForPrefix(String prefix, Node context) {
      this.error("ER_CANT_RESOLVE_NSPREFIX", (Object[])null);
      return null;
   }

   public String getNamespaceForPrefix(String prefix) {
      Vector nsDecls = this.m_declaredPrefixes;
      if (null != nsDecls) {
         int n = nsDecls.size();
         if (prefix.equals("#default")) {
            prefix = "";
         }

         for(int i = 0; i < n; ++i) {
            XMLNSDecl decl = (XMLNSDecl)nsDecls.elementAt(i);
            if (prefix.equals(decl.getPrefix())) {
               return decl.getURI();
            }
         }
      }

      if (null != this.m_parentNode) {
         return this.m_parentNode.getNamespaceForPrefix(prefix);
      } else {
         return "xml".equals(prefix) ? "http://www.w3.org/XML/1998/namespace" : null;
      }
   }

   public Vector getPrefixes() {
      return this.m_prefixTable;
   }

   public boolean containsExcludeResultPrefix(String prefix, String uri) {
      ElemTemplateElement parent = this.getParentElem();
      return null != parent ? parent.containsExcludeResultPrefix(prefix, uri) : false;
   }

   private boolean excludeResultNSDecl(String prefix, String uri) throws TransformerException {
      if (uri != null) {
         if (uri.equals("http://www.w3.org/1999/XSL/Transform") || this.getStylesheet().containsExtensionElementURI(uri)) {
            return true;
         }

         if (this.containsExcludeResultPrefix(prefix, uri)) {
            return true;
         }
      }

      return false;
   }

   public void resolvePrefixTables() throws TransformerException {
      this.m_prefixTable = null;
      int n;
      if (null != this.m_declaredPrefixes) {
         StylesheetRoot stylesheet = this.getStylesheetRoot();
         int n = this.m_declaredPrefixes.size();

         for(n = 0; n < n; ++n) {
            XMLNSDecl decl = (XMLNSDecl)this.m_declaredPrefixes.elementAt(n);
            String prefix = decl.getPrefix();
            String uri = decl.getURI();
            if (null == uri) {
               uri = "";
            }

            boolean shouldExclude = this.excludeResultNSDecl(prefix, uri);
            if (null == this.m_prefixTable) {
               this.m_prefixTable = new Vector();
            }

            NamespaceAlias nsAlias = stylesheet.getNamespaceAliasComposed(uri);
            if (null != nsAlias) {
               decl = new XMLNSDecl(nsAlias.getStylesheetPrefix(), nsAlias.getResultNamespace(), shouldExclude);
            } else {
               decl = new XMLNSDecl(prefix, uri, shouldExclude);
            }

            this.m_prefixTable.addElement(decl);
         }
      }

      ElemTemplateElement parent = this.getParentNodeElem();
      if (null != parent) {
         Vector prefixes = parent.m_prefixTable;
         if (null == this.m_prefixTable && !this.needToCheckExclude()) {
            this.m_prefixTable = parent.m_prefixTable;
         } else {
            n = prefixes.size();

            for(int i = 0; i < n; ++i) {
               XMLNSDecl decl = (XMLNSDecl)prefixes.elementAt(i);
               boolean shouldExclude = this.excludeResultNSDecl(decl.getPrefix(), decl.getURI());
               if (shouldExclude != decl.getIsExcluded()) {
                  decl = new XMLNSDecl(decl.getPrefix(), decl.getURI(), shouldExclude);
               }

               this.addOrReplaceDecls(decl);
            }
         }
      } else if (null == this.m_prefixTable) {
         this.m_prefixTable = new Vector();
      }

   }

   void addOrReplaceDecls(XMLNSDecl newDecl) {
      int n = this.m_prefixTable.size();

      for(int i = n - 1; i >= 0; --i) {
         XMLNSDecl decl = (XMLNSDecl)this.m_prefixTable.elementAt(i);
         if (decl.getPrefix().equals(newDecl.getPrefix())) {
            return;
         }
      }

      this.m_prefixTable.addElement(newDecl);
   }

   boolean needToCheckExclude() {
      return false;
   }

   void executeNSDecls(TransformerImpl transformer) throws TransformerException {
      this.executeNSDecls(transformer, (String)null);
   }

   void executeNSDecls(TransformerImpl transformer, String ignorePrefix) throws TransformerException {
      try {
         if (null != this.m_prefixTable) {
            SerializationHandler rhandler = transformer.getResultTreeHandler();
            int n = this.m_prefixTable.size();

            for(int i = n - 1; i >= 0; --i) {
               XMLNSDecl decl = (XMLNSDecl)this.m_prefixTable.elementAt(i);
               if (!decl.getIsExcluded() && (null == ignorePrefix || !decl.getPrefix().equals(ignorePrefix))) {
                  rhandler.startPrefixMapping(decl.getPrefix(), decl.getURI(), true);
               }
            }
         }

      } catch (SAXException var7) {
         throw new TransformerException(var7);
      }
   }

   void unexecuteNSDecls(TransformerImpl transformer) throws TransformerException {
      this.unexecuteNSDecls(transformer, (String)null);
   }

   void unexecuteNSDecls(TransformerImpl transformer, String ignorePrefix) throws TransformerException {
      try {
         if (null != this.m_prefixTable) {
            SerializationHandler rhandler = transformer.getResultTreeHandler();
            int n = this.m_prefixTable.size();

            for(int i = 0; i < n; ++i) {
               XMLNSDecl decl = (XMLNSDecl)this.m_prefixTable.elementAt(i);
               if (!decl.getIsExcluded() && (null == ignorePrefix || !decl.getPrefix().equals(ignorePrefix))) {
                  rhandler.endPrefixMapping(decl.getPrefix());
               }
            }
         }

      } catch (SAXException var7) {
         throw new TransformerException(var7);
      }
   }

   public void setUid(int i) {
      this.m_docOrderNumber = i;
   }

   public int getUid() {
      return this.m_docOrderNumber;
   }

   public Node getParentNode() {
      return this.m_parentNode;
   }

   public ElemTemplateElement getParentElem() {
      return this.m_parentNode;
   }

   public void setParentElem(ElemTemplateElement p) {
      this.m_parentNode = p;
   }

   public Node getNextSibling() {
      return this.m_nextSibling;
   }

   public Node getPreviousSibling() {
      Node walker = this.getParentNode();
      Node prev = null;
      if (walker != null) {
         for(walker = walker.getFirstChild(); walker != null; walker = walker.getNextSibling()) {
            if (walker == this) {
               return prev;
            }

            prev = walker;
         }
      }

      return null;
   }

   public ElemTemplateElement getPreviousSiblingElem() {
      ElemTemplateElement walker = this.getParentNodeElem();
      ElemTemplateElement prev = null;
      if (walker != null) {
         for(walker = walker.getFirstChildElem(); walker != null; walker = walker.getNextSiblingElem()) {
            if (walker == this) {
               return prev;
            }

            prev = walker;
         }
      }

      return null;
   }

   public ElemTemplateElement getNextSiblingElem() {
      return this.m_nextSibling;
   }

   public ElemTemplateElement getParentNodeElem() {
      return this.m_parentNode;
   }

   public Node getFirstChild() {
      return this.m_firstChild;
   }

   public ElemTemplateElement getFirstChildElem() {
      return this.m_firstChild;
   }

   public Node getLastChild() {
      ElemTemplateElement lastChild = null;

      for(ElemTemplateElement node = this.m_firstChild; node != null; node = node.m_nextSibling) {
         lastChild = node;
      }

      return lastChild;
   }

   public ElemTemplateElement getLastChildElem() {
      ElemTemplateElement lastChild = null;

      for(ElemTemplateElement node = this.m_firstChild; node != null; node = node.m_nextSibling) {
         lastChild = node;
      }

      return lastChild;
   }

   public Node getDOMBackPointer() {
      return this.m_DOMBackPointer;
   }

   public void setDOMBackPointer(Node n) {
      this.m_DOMBackPointer = n;
   }

   public int compareTo(Object o) throws ClassCastException {
      ElemTemplateElement ro = (ElemTemplateElement)o;
      int roPrecedence = ro.getStylesheetComposed().getImportCountComposed();
      int myPrecedence = this.getStylesheetComposed().getImportCountComposed();
      if (myPrecedence < roPrecedence) {
         return -1;
      } else {
         return myPrecedence > roPrecedence ? 1 : this.getUid() - ro.getUid();
      }
   }

   public boolean shouldStripWhiteSpace(XPathContext support, Element targetElement) throws TransformerException {
      StylesheetRoot sroot = this.getStylesheetRoot();
      return null != sroot ? sroot.shouldStripWhiteSpace(support, targetElement) : false;
   }

   public boolean canStripWhiteSpace() {
      StylesheetRoot sroot = this.getStylesheetRoot();
      return null != sroot ? sroot.canStripWhiteSpace() : false;
   }

   public boolean canAcceptVariables() {
      return true;
   }

   public void exprSetParent(ExpressionNode n) {
      this.setParentElem((ElemTemplateElement)n);
   }

   public ExpressionNode exprGetParent() {
      return this.getParentElem();
   }

   public void exprAddChild(ExpressionNode n, int i) {
      this.appendChild((ElemTemplateElement)n);
   }

   public ExpressionNode exprGetChild(int i) {
      return (ExpressionNode)this.item(i);
   }

   public int exprGetNumChildren() {
      return this.getLength();
   }

   protected boolean accept(XSLTVisitor visitor) {
      return visitor.visitInstruction(this);
   }

   public void callVisitors(XSLTVisitor visitor) {
      if (this.accept(visitor)) {
         this.callChildVisitors(visitor);
      }

   }

   protected void callChildVisitors(XSLTVisitor visitor, boolean callAttributes) {
      for(ElemTemplateElement node = this.m_firstChild; node != null; node = node.m_nextSibling) {
         node.callVisitors(visitor);
      }

   }

   protected void callChildVisitors(XSLTVisitor visitor) {
      this.callChildVisitors(visitor, true);
   }

   public boolean handlesNullPrefixes() {
      return false;
   }
}
