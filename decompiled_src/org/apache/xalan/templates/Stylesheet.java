package org.apache.xalan.templates;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;

public class Stylesheet extends ElemTemplateElement implements Serializable {
   static final long serialVersionUID = 2085337282743043776L;
   public static final String STYLESHEET_EXT = ".lxc";
   private String m_XmlnsXsl;
   private StringVector m_ExtensionElementURIs;
   private StringVector m_ExcludeResultPrefixs;
   private String m_Id;
   private String m_Version;
   private boolean m_isCompatibleMode = false;
   private Vector m_imports;
   private Vector m_includes;
   Stack m_DecimalFormatDeclarations;
   private Vector m_whitespaceStrippingElements;
   private Vector m_whitespacePreservingElements;
   private Vector m_output;
   private Vector m_keyDeclarations;
   private Vector m_attributeSets;
   private Vector m_topLevelVariables;
   private Vector m_templates;
   private Vector m_prefix_aliases;
   private Hashtable m_NonXslTopLevel;
   private String m_href = null;
   private String m_publicId;
   private String m_systemId;
   private StylesheetRoot m_stylesheetRoot;
   private Stylesheet m_stylesheetParent;

   public Stylesheet(Stylesheet parent) {
      if (null != parent) {
         this.m_stylesheetParent = parent;
         this.m_stylesheetRoot = parent.getStylesheetRoot();
      }

   }

   public Stylesheet getStylesheet() {
      return this;
   }

   public boolean isAggregatedType() {
      return false;
   }

   public boolean isRoot() {
      return false;
   }

   private void readObject(ObjectInputStream stream) throws IOException, TransformerException {
      try {
         stream.defaultReadObject();
      } catch (ClassNotFoundException var3) {
         throw new TransformerException(var3);
      }
   }

   private void writeObject(ObjectOutputStream stream) throws IOException {
      stream.defaultWriteObject();
   }

   public void setXmlnsXsl(String v) {
      this.m_XmlnsXsl = v;
   }

   public String getXmlnsXsl() {
      return this.m_XmlnsXsl;
   }

   public void setExtensionElementPrefixes(StringVector v) {
      this.m_ExtensionElementURIs = v;
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

   public void setExcludeResultPrefixes(StringVector v) {
      this.m_ExcludeResultPrefixs = v;
   }

   public String getExcludeResultPrefix(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_ExcludeResultPrefixs) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return this.m_ExcludeResultPrefixs.elementAt(i);
      }
   }

   public int getExcludeResultPrefixCount() {
      return null != this.m_ExcludeResultPrefixs ? this.m_ExcludeResultPrefixs.size() : 0;
   }

   public boolean containsExcludeResultPrefix(String prefix, String uri) {
      if (null != this.m_ExcludeResultPrefixs && uri != null) {
         for(int i = 0; i < this.m_ExcludeResultPrefixs.size(); ++i) {
            if (uri.equals(this.getNamespaceForPrefix(this.m_ExcludeResultPrefixs.elementAt(i)))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void setId(String v) {
      this.m_Id = v;
   }

   public String getId() {
      return this.m_Id;
   }

   public void setVersion(String v) {
      this.m_Version = v;
      this.m_isCompatibleMode = Double.valueOf(v) > 1.0;
   }

   public boolean getCompatibleMode() {
      return this.m_isCompatibleMode;
   }

   public String getVersion() {
      return this.m_Version;
   }

   public void setImport(StylesheetComposed v) {
      if (null == this.m_imports) {
         this.m_imports = new Vector();
      }

      this.m_imports.addElement(v);
   }

   public StylesheetComposed getImport(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_imports) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (StylesheetComposed)this.m_imports.elementAt(i);
      }
   }

   public int getImportCount() {
      return null != this.m_imports ? this.m_imports.size() : 0;
   }

   public void setInclude(Stylesheet v) {
      if (null == this.m_includes) {
         this.m_includes = new Vector();
      }

      this.m_includes.addElement(v);
   }

   public Stylesheet getInclude(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_includes) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (Stylesheet)this.m_includes.elementAt(i);
      }
   }

   public int getIncludeCount() {
      return null != this.m_includes ? this.m_includes.size() : 0;
   }

   public void setDecimalFormat(DecimalFormatProperties edf) {
      if (null == this.m_DecimalFormatDeclarations) {
         this.m_DecimalFormatDeclarations = new Stack();
      }

      this.m_DecimalFormatDeclarations.push(edf);
   }

   public DecimalFormatProperties getDecimalFormat(QName name) {
      if (null == this.m_DecimalFormatDeclarations) {
         return null;
      } else {
         int n = this.getDecimalFormatCount();

         for(int i = n - 1; i >= 0; ++i) {
            DecimalFormatProperties dfp = this.getDecimalFormat(i);
            if (dfp.getName().equals(name)) {
               return dfp;
            }
         }

         return null;
      }
   }

   public DecimalFormatProperties getDecimalFormat(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_DecimalFormatDeclarations) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (DecimalFormatProperties)this.m_DecimalFormatDeclarations.elementAt(i);
      }
   }

   public int getDecimalFormatCount() {
      return null != this.m_DecimalFormatDeclarations ? this.m_DecimalFormatDeclarations.size() : 0;
   }

   public void setStripSpaces(WhiteSpaceInfo wsi) {
      if (null == this.m_whitespaceStrippingElements) {
         this.m_whitespaceStrippingElements = new Vector();
      }

      this.m_whitespaceStrippingElements.addElement(wsi);
   }

   public WhiteSpaceInfo getStripSpace(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_whitespaceStrippingElements) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (WhiteSpaceInfo)this.m_whitespaceStrippingElements.elementAt(i);
      }
   }

   public int getStripSpaceCount() {
      return null != this.m_whitespaceStrippingElements ? this.m_whitespaceStrippingElements.size() : 0;
   }

   public void setPreserveSpaces(WhiteSpaceInfo wsi) {
      if (null == this.m_whitespacePreservingElements) {
         this.m_whitespacePreservingElements = new Vector();
      }

      this.m_whitespacePreservingElements.addElement(wsi);
   }

   public WhiteSpaceInfo getPreserveSpace(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_whitespacePreservingElements) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (WhiteSpaceInfo)this.m_whitespacePreservingElements.elementAt(i);
      }
   }

   public int getPreserveSpaceCount() {
      return null != this.m_whitespacePreservingElements ? this.m_whitespacePreservingElements.size() : 0;
   }

   public void setOutput(OutputProperties v) {
      if (null == this.m_output) {
         this.m_output = new Vector();
      }

      this.m_output.addElement(v);
   }

   public OutputProperties getOutput(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_output) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (OutputProperties)this.m_output.elementAt(i);
      }
   }

   public int getOutputCount() {
      return null != this.m_output ? this.m_output.size() : 0;
   }

   public void setKey(KeyDeclaration v) {
      if (null == this.m_keyDeclarations) {
         this.m_keyDeclarations = new Vector();
      }

      this.m_keyDeclarations.addElement(v);
   }

   public KeyDeclaration getKey(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_keyDeclarations) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (KeyDeclaration)this.m_keyDeclarations.elementAt(i);
      }
   }

   public int getKeyCount() {
      return null != this.m_keyDeclarations ? this.m_keyDeclarations.size() : 0;
   }

   public void setAttributeSet(ElemAttributeSet attrSet) {
      if (null == this.m_attributeSets) {
         this.m_attributeSets = new Vector();
      }

      this.m_attributeSets.addElement(attrSet);
   }

   public ElemAttributeSet getAttributeSet(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_attributeSets) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (ElemAttributeSet)this.m_attributeSets.elementAt(i);
      }
   }

   public int getAttributeSetCount() {
      return null != this.m_attributeSets ? this.m_attributeSets.size() : 0;
   }

   public void setVariable(ElemVariable v) {
      if (null == this.m_topLevelVariables) {
         this.m_topLevelVariables = new Vector();
      }

      this.m_topLevelVariables.addElement(v);
   }

   public ElemVariable getVariableOrParam(QName qname) {
      if (null != this.m_topLevelVariables) {
         int n = this.getVariableOrParamCount();

         for(int i = 0; i < n; ++i) {
            ElemVariable var = this.getVariableOrParam(i);
            if (var.getName().equals(qname)) {
               return var;
            }
         }
      }

      return null;
   }

   public ElemVariable getVariable(QName qname) {
      if (null != this.m_topLevelVariables) {
         int n = this.getVariableOrParamCount();

         for(int i = 0; i < n; ++i) {
            ElemVariable var = this.getVariableOrParam(i);
            if (var.getXSLToken() == 73 && var.getName().equals(qname)) {
               return var;
            }
         }
      }

      return null;
   }

   public ElemVariable getVariableOrParam(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_topLevelVariables) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (ElemVariable)this.m_topLevelVariables.elementAt(i);
      }
   }

   public int getVariableOrParamCount() {
      return null != this.m_topLevelVariables ? this.m_topLevelVariables.size() : 0;
   }

   public void setParam(ElemParam v) {
      this.setVariable(v);
   }

   public ElemParam getParam(QName qname) {
      if (null != this.m_topLevelVariables) {
         int n = this.getVariableOrParamCount();

         for(int i = 0; i < n; ++i) {
            ElemVariable var = this.getVariableOrParam(i);
            if (var.getXSLToken() == 41 && var.getName().equals(qname)) {
               return (ElemParam)var;
            }
         }
      }

      return null;
   }

   public void setTemplate(ElemTemplate v) {
      if (null == this.m_templates) {
         this.m_templates = new Vector();
      }

      this.m_templates.addElement(v);
      v.setStylesheet(this);
   }

   public ElemTemplate getTemplate(int i) throws TransformerException {
      if (null == this.m_templates) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (ElemTemplate)this.m_templates.elementAt(i);
      }
   }

   public int getTemplateCount() {
      return null != this.m_templates ? this.m_templates.size() : 0;
   }

   public void setNamespaceAlias(NamespaceAlias na) {
      if (this.m_prefix_aliases == null) {
         this.m_prefix_aliases = new Vector();
      }

      this.m_prefix_aliases.addElement(na);
   }

   public NamespaceAlias getNamespaceAlias(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_prefix_aliases) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (NamespaceAlias)this.m_prefix_aliases.elementAt(i);
      }
   }

   public int getNamespaceAliasCount() {
      return null != this.m_prefix_aliases ? this.m_prefix_aliases.size() : 0;
   }

   public void setNonXslTopLevel(QName name, Object obj) {
      if (null == this.m_NonXslTopLevel) {
         this.m_NonXslTopLevel = new Hashtable();
      }

      this.m_NonXslTopLevel.put(name, obj);
   }

   public Object getNonXslTopLevel(QName name) {
      return null != this.m_NonXslTopLevel ? this.m_NonXslTopLevel.get(name) : null;
   }

   public String getHref() {
      return this.m_href;
   }

   public void setHref(String baseIdent) {
      this.m_href = baseIdent;
   }

   public void setLocaterInfo(SourceLocator locator) {
      if (null != locator) {
         this.m_publicId = locator.getPublicId();
         this.m_systemId = locator.getSystemId();
         if (null != this.m_systemId) {
            try {
               this.m_href = SystemIDResolver.getAbsoluteURI(this.m_systemId, (String)null);
            } catch (TransformerException var3) {
            }
         }

         super.setLocaterInfo(locator);
      }

   }

   public StylesheetRoot getStylesheetRoot() {
      return this.m_stylesheetRoot;
   }

   public void setStylesheetRoot(StylesheetRoot v) {
      this.m_stylesheetRoot = v;
   }

   public Stylesheet getStylesheetParent() {
      return this.m_stylesheetParent;
   }

   public void setStylesheetParent(Stylesheet v) {
      this.m_stylesheetParent = v;
   }

   public StylesheetComposed getStylesheetComposed() {
      Stylesheet sheet;
      for(sheet = this; !sheet.isAggregatedType(); sheet = sheet.getStylesheetParent()) {
      }

      return (StylesheetComposed)sheet;
   }

   public short getNodeType() {
      return 9;
   }

   public int getXSLToken() {
      return 25;
   }

   public String getNodeName() {
      return "stylesheet";
   }

   public void replaceTemplate(ElemTemplate v, int i) throws TransformerException {
      if (null == this.m_templates) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         this.replaceChild((ElemTemplateElement)v, (ElemTemplateElement)((ElemTemplateElement)this.m_templates.elementAt(i)));
         this.m_templates.setElementAt(v, i);
         v.setStylesheet(this);
      }
   }

   protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs) {
      int s = this.getImportCount();

      for(int j = 0; j < s; ++j) {
         this.getImport(j).callVisitors(visitor);
      }

      s = this.getIncludeCount();

      for(int j = 0; j < s; ++j) {
         this.getInclude(j).callVisitors(visitor);
      }

      s = this.getOutputCount();

      for(int j = 0; j < s; ++j) {
         visitor.visitTopLevelInstruction(this.getOutput(j));
      }

      s = this.getAttributeSetCount();

      for(int j = 0; j < s; ++j) {
         ElemAttributeSet attrSet = this.getAttributeSet(j);
         if (visitor.visitTopLevelInstruction(attrSet)) {
            attrSet.callChildVisitors(visitor);
         }
      }

      s = this.getDecimalFormatCount();

      for(int j = 0; j < s; ++j) {
         visitor.visitTopLevelInstruction(this.getDecimalFormat(j));
      }

      s = this.getKeyCount();

      for(int j = 0; j < s; ++j) {
         visitor.visitTopLevelInstruction(this.getKey(j));
      }

      s = this.getNamespaceAliasCount();

      for(int j = 0; j < s; ++j) {
         visitor.visitTopLevelInstruction(this.getNamespaceAlias(j));
      }

      s = this.getTemplateCount();

      for(int j = 0; j < s; ++j) {
         try {
            ElemTemplate template = this.getTemplate(j);
            if (visitor.visitTopLevelInstruction(template)) {
               template.callChildVisitors(visitor);
            }
         } catch (TransformerException var17) {
            throw new WrappedRuntimeException(var17);
         }
      }

      s = this.getVariableOrParamCount();

      for(int j = 0; j < s; ++j) {
         ElemVariable var = this.getVariableOrParam(j);
         if (visitor.visitTopLevelVariableOrParamDecl(var)) {
            var.callChildVisitors(visitor);
         }
      }

      s = this.getStripSpaceCount();

      for(int j = 0; j < s; ++j) {
         visitor.visitTopLevelInstruction(this.getStripSpace(j));
      }

      s = this.getPreserveSpaceCount();

      for(int j = 0; j < s; ++j) {
         visitor.visitTopLevelInstruction(this.getPreserveSpace(j));
      }

      if (null != this.m_NonXslTopLevel) {
         Enumeration elements = this.m_NonXslTopLevel.elements();

         while(elements.hasMoreElements()) {
            ElemTemplateElement elem = (ElemTemplateElement)elements.nextElement();
            if (visitor.visitTopLevelInstruction(elem)) {
               elem.callChildVisitors(visitor);
            }
         }
      }

   }

   protected boolean accept(XSLTVisitor visitor) {
      return visitor.visitStylesheet(this);
   }
}
