package org.apache.xerces.impl.xs;

import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.grammars.XMLSchemaDescription;

public class XSDDescription extends XMLResourceIdentifierImpl implements XMLSchemaDescription {
   public static final short CONTEXT_INITIALIZE = -1;
   public static final short CONTEXT_INCLUDE = 0;
   public static final short CONTEXT_REDEFINE = 1;
   public static final short CONTEXT_IMPORT = 2;
   public static final short CONTEXT_PREPARSE = 3;
   public static final short CONTEXT_INSTANCE = 4;
   public static final short CONTEXT_ELEMENT = 5;
   public static final short CONTEXT_ATTRIBUTE = 6;
   public static final short CONTEXT_XSITYPE = 7;
   protected short fContextType;
   protected String[] fLocationHints;
   protected QName fTriggeringComponent;
   protected QName fEnclosedElementName;
   protected XMLAttributes fAttributes;

   public String getGrammarType() {
      return "http://www.w3.org/2001/XMLSchema";
   }

   public short getContextType() {
      return this.fContextType;
   }

   public String getTargetNamespace() {
      return super.fNamespace;
   }

   public String[] getLocationHints() {
      return this.fLocationHints;
   }

   public QName getTriggeringComponent() {
      return this.fTriggeringComponent;
   }

   public QName getEnclosingElementName() {
      return this.fEnclosedElementName;
   }

   public XMLAttributes getAttributes() {
      return this.fAttributes;
   }

   public boolean fromInstance() {
      return this.fContextType == 6 || this.fContextType == 5 || this.fContextType == 4 || this.fContextType == 7;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof XMLSchemaDescription)) {
         return false;
      } else {
         XMLSchemaDescription var2 = (XMLSchemaDescription)var1;
         if (super.fNamespace != null) {
            return super.fNamespace.equals(var2.getTargetNamespace());
         } else {
            return var2.getTargetNamespace() == null;
         }
      }
   }

   public int hashCode() {
      return super.fNamespace == null ? 0 : super.fNamespace.hashCode();
   }

   public void setContextType(short var1) {
      this.fContextType = var1;
   }

   public void setTargetNamespace(String var1) {
      super.fNamespace = var1;
   }

   public void setLocationHints(String[] var1) {
      int var2 = var1.length;
      this.fLocationHints = new String[var2];
      System.arraycopy(var1, 0, this.fLocationHints, 0, var2);
   }

   public void setTriggeringComponent(QName var1) {
      this.fTriggeringComponent = var1;
   }

   public void setEnclosingElementName(QName var1) {
      this.fEnclosedElementName = var1;
   }

   public void setAttributes(XMLAttributes var1) {
      this.fAttributes = var1;
   }

   public void reset() {
      super.clear();
      this.fContextType = -1;
      this.fLocationHints = null;
      this.fTriggeringComponent = null;
      this.fEnclosedElementName = null;
      this.fAttributes = null;
   }

   public XSDDescription makeClone() {
      XSDDescription var1 = new XSDDescription();
      var1.fAttributes = this.fAttributes;
      var1.fBaseSystemId = super.fBaseSystemId;
      var1.fContextType = this.fContextType;
      var1.fEnclosedElementName = this.fEnclosedElementName;
      var1.fExpandedSystemId = super.fExpandedSystemId;
      var1.fLiteralSystemId = super.fLiteralSystemId;
      var1.fLocationHints = this.fLocationHints;
      var1.fPublicId = super.fPublicId;
      var1.fNamespace = super.fNamespace;
      var1.fTriggeringComponent = this.fTriggeringComponent;
      return var1;
   }
}
