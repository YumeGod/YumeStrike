package org.apache.xerces.jaxp.validation;

import javax.xml.transform.dom.DOMResult;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ElementNSImpl;
import org.apache.xerces.dom.PSVIAttrNSImpl;
import org.apache.xerces.dom.PSVIDocumentImpl;
import org.apache.xerces.dom.PSVIElementNSImpl;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

final class DOMResultAugmentor implements DOMDocumentHandler {
   private DOMValidatorHelper fDOMValidatorHelper;
   private Document fDocument;
   private CoreDocumentImpl fDocumentImpl;
   private boolean fStorePSVI;
   private boolean fIgnoreChars;
   private final QName fAttributeQName = new QName();

   public DOMResultAugmentor(DOMValidatorHelper var1) {
      this.fDOMValidatorHelper = var1;
   }

   public void setDOMResult(DOMResult var1) {
      this.fIgnoreChars = false;
      if (var1 != null) {
         Node var2 = var1.getNode();
         this.fDocument = var2.getNodeType() == 9 ? (Document)var2 : var2.getOwnerDocument();
         this.fDocumentImpl = this.fDocument instanceof CoreDocumentImpl ? (CoreDocumentImpl)this.fDocument : null;
         this.fStorePSVI = this.fDocument instanceof PSVIDocumentImpl;
      } else {
         this.fDocument = null;
         this.fDocumentImpl = null;
         this.fStorePSVI = false;
      }
   }

   public void doctypeDecl(DocumentType var1) throws XNIException {
   }

   public void characters(Text var1) throws XNIException {
   }

   public void cdata(CDATASection var1) throws XNIException {
   }

   public void comment(Comment var1) throws XNIException {
   }

   public void processingInstruction(ProcessingInstruction var1) throws XNIException {
   }

   public void setIgnoringCharacters(boolean var1) {
      this.fIgnoreChars = var1;
   }

   public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
   }

   public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
   }

   public void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      Element var4 = (Element)this.fDOMValidatorHelper.getCurrentElement();
      NamedNodeMap var5 = var4.getAttributes();
      int var6 = var5.getLength();
      int var8;
      if (this.fDocumentImpl != null) {
         for(var8 = 0; var8 < var6; ++var8) {
            AttrImpl var7 = (AttrImpl)var5.item(var8);
            AttributePSVI var9 = (AttributePSVI)var2.getAugmentations(var8).getItem("ATTRIBUTE_PSVI");
            if (var9 != null && this.processAttributePSVI(var7, var9)) {
               ((ElementImpl)var4).setIdAttributeNode(var7, true);
            }
         }
      }

      int var11 = var2.getLength();
      if (var11 > var6) {
         if (this.fDocumentImpl == null) {
            for(var8 = var6; var8 < var11; ++var8) {
               var2.getName(var8, this.fAttributeQName);
               var4.setAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, var2.getValue(var8));
            }
         } else {
            for(var8 = var6; var8 < var11; ++var8) {
               var2.getName(var8, this.fAttributeQName);
               AttrImpl var12 = (AttrImpl)this.fDocumentImpl.createAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, this.fAttributeQName.localpart);
               var12.setValue(var2.getValue(var8));
               AttributePSVI var10 = (AttributePSVI)var2.getAugmentations(var8).getItem("ATTRIBUTE_PSVI");
               if (var10 != null && this.processAttributePSVI(var12, var10)) {
                  ((ElementImpl)var4).setIdAttributeNode(var12, true);
               }

               var12.setSpecified(false);
               var4.setAttributeNode(var12);
            }
         }
      }

   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      this.startElement(var1, var2, var3);
      this.endElement(var1, var3);
   }

   public void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
   }

   public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
   }

   public void endGeneralEntity(String var1, Augmentations var2) throws XNIException {
   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      if (!this.fIgnoreChars) {
         Element var3 = (Element)this.fDOMValidatorHelper.getCurrentElement();
         var3.appendChild(this.fDocument.createTextNode(var1.toString()));
      }

   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      this.characters(var1, var2);
   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      Node var3 = this.fDOMValidatorHelper.getCurrentElement();
      if (var2 != null && this.fDocumentImpl != null) {
         ElementPSVI var4 = (ElementPSVI)var2.getItem("ELEMENT_PSVI");
         if (var4 != null) {
            if (this.fStorePSVI) {
               ((PSVIElementNSImpl)var3).setPSVI(var4);
            }

            Object var5 = var4.getMemberTypeDefinition();
            if (var5 == null) {
               var5 = var4.getTypeDefinition();
            }

            ((ElementNSImpl)var3).setType((XSTypeDefinition)var5);
         }
      }

   }

   public void startCDATA(Augmentations var1) throws XNIException {
   }

   public void endCDATA(Augmentations var1) throws XNIException {
   }

   public void endDocument(Augmentations var1) throws XNIException {
   }

   public void setDocumentSource(XMLDocumentSource var1) {
   }

   public XMLDocumentSource getDocumentSource() {
      return null;
   }

   private boolean processAttributePSVI(AttrImpl var1, AttributePSVI var2) {
      if (this.fStorePSVI) {
         ((PSVIAttrNSImpl)var1).setPSVI(var2);
      }

      XSSimpleTypeDefinition var3 = var2.getMemberTypeDefinition();
      if (var3 == null) {
         XSTypeDefinition var4 = var2.getTypeDefinition();
         if (var4 != null) {
            var1.setType(var4);
            return ((XSSimpleType)var4).isIDType();
         } else {
            return false;
         }
      } else {
         var1.setType(var3);
         return ((XSSimpleType)var3).isIDType();
      }
   }
}
