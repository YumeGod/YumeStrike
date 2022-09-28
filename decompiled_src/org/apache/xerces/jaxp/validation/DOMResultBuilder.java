package org.apache.xerces.jaxp.validation;

import java.util.ArrayList;
import javax.xml.transform.dom.DOMResult;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DocumentTypeImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ElementNSImpl;
import org.apache.xerces.dom.EntityImpl;
import org.apache.xerces.dom.NotationImpl;
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
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

final class DOMResultBuilder implements DOMDocumentHandler {
   private static final int[] kidOK = new int[13];
   private Document fDocument;
   private CoreDocumentImpl fDocumentImpl;
   private boolean fStorePSVI;
   private Node fTarget;
   private Node fNextSibling;
   private Node fCurrentNode;
   private Node fFragmentRoot;
   private final ArrayList fTargetChildren = new ArrayList();
   private boolean fIgnoreChars;
   private final QName fAttributeQName = new QName();

   public DOMResultBuilder() {
   }

   public void setDOMResult(DOMResult var1) {
      this.fCurrentNode = null;
      this.fFragmentRoot = null;
      this.fIgnoreChars = false;
      this.fTargetChildren.clear();
      if (var1 != null) {
         this.fTarget = var1.getNode();
         this.fNextSibling = var1.getNextSibling();
         this.fDocument = this.fTarget.getNodeType() == 9 ? (Document)this.fTarget : this.fTarget.getOwnerDocument();
         this.fDocumentImpl = this.fDocument instanceof CoreDocumentImpl ? (CoreDocumentImpl)this.fDocument : null;
         this.fStorePSVI = this.fDocument instanceof PSVIDocumentImpl;
      } else {
         this.fTarget = null;
         this.fNextSibling = null;
         this.fDocument = null;
         this.fDocumentImpl = null;
         this.fStorePSVI = false;
      }
   }

   public void doctypeDecl(DocumentType var1) throws XNIException {
      if (this.fDocumentImpl != null) {
         DocumentType var2 = this.fDocumentImpl.createDocumentType(var1.getName(), var1.getPublicId(), var1.getSystemId());
         String var3 = var1.getInternalSubset();
         if (var3 != null) {
            ((DocumentTypeImpl)var2).setInternalSubset(var3);
         }

         NamedNodeMap var4 = var1.getEntities();
         NamedNodeMap var5 = var2.getEntities();
         int var6 = var4.getLength();

         for(int var7 = 0; var7 < var6; ++var7) {
            Entity var8 = (Entity)var4.item(var7);
            EntityImpl var9 = (EntityImpl)this.fDocumentImpl.createEntity(var8.getNodeName());
            var9.setPublicId(var8.getPublicId());
            var9.setSystemId(var8.getSystemId());
            var9.setNotationName(var8.getNotationName());
            var5.setNamedItem(var9);
         }

         var4 = var1.getNotations();
         var5 = var2.getNotations();
         var6 = var4.getLength();

         for(int var11 = 0; var11 < var6; ++var11) {
            Notation var12 = (Notation)var4.item(var11);
            NotationImpl var10 = (NotationImpl)this.fDocumentImpl.createNotation(var12.getNodeName());
            var10.setPublicId(var12.getPublicId());
            var10.setSystemId(var12.getSystemId());
            var5.setNamedItem(var10);
         }

         this.append(var2);
      }

   }

   public void characters(Text var1) throws XNIException {
      this.append(this.fDocument.createTextNode(var1.getNodeValue()));
   }

   public void cdata(CDATASection var1) throws XNIException {
      this.append(this.fDocument.createCDATASection(var1.getNodeValue()));
   }

   public void comment(Comment var1) throws XNIException {
      this.append(this.fDocument.createComment(var1.getNodeValue()));
   }

   public void processingInstruction(ProcessingInstruction var1) throws XNIException {
      this.append(this.fDocument.createProcessingInstruction(var1.getTarget(), var1.getData()));
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
      int var5 = var2.getLength();
      Element var4;
      int var6;
      if (this.fDocumentImpl == null) {
         var4 = this.fDocument.createElementNS(var1.uri, var1.rawname);

         for(var6 = 0; var6 < var5; ++var6) {
            var2.getName(var6, this.fAttributeQName);
            var4.setAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, var2.getValue(var6));
         }
      } else {
         var4 = this.fDocumentImpl.createElementNS(var1.uri, var1.rawname, var1.localpart);

         for(var6 = 0; var6 < var5; ++var6) {
            var2.getName(var6, this.fAttributeQName);
            AttrImpl var7 = (AttrImpl)this.fDocumentImpl.createAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, this.fAttributeQName.localpart);
            var7.setValue(var2.getValue(var6));
            AttributePSVI var8 = (AttributePSVI)var2.getAugmentations(var6).getItem("ATTRIBUTE_PSVI");
            if (var8 != null) {
               if (this.fStorePSVI) {
                  ((PSVIAttrNSImpl)var7).setPSVI(var8);
               }

               XSSimpleTypeDefinition var9 = var8.getMemberTypeDefinition();
               if (var9 == null) {
                  XSTypeDefinition var10 = var8.getTypeDefinition();
                  if (var10 != null) {
                     var7.setType(var10);
                     if (((XSSimpleType)var10).isIDType()) {
                        ((ElementImpl)var4).setIdAttributeNode(var7, true);
                     }
                  }
               } else {
                  var7.setType(var9);
                  if (((XSSimpleType)var9).isIDType()) {
                     ((ElementImpl)var4).setIdAttributeNode(var7, true);
                  }
               }
            }

            var7.setSpecified(var2.isSpecified(var6));
            var4.setAttributeNode(var7);
         }
      }

      this.append(var4);
      this.fCurrentNode = var4;
      if (this.fFragmentRoot == null) {
         this.fFragmentRoot = var4;
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
         this.append(this.fDocument.createTextNode(var1.toString()));
      }

   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      this.characters(var1, var2);
   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      if (var2 != null && this.fDocumentImpl != null) {
         ElementPSVI var3 = (ElementPSVI)var2.getItem("ELEMENT_PSVI");
         if (var3 != null) {
            if (this.fStorePSVI) {
               ((PSVIElementNSImpl)this.fCurrentNode).setPSVI(var3);
            }

            Object var4 = var3.getMemberTypeDefinition();
            if (var4 == null) {
               var4 = var3.getTypeDefinition();
            }

            ((ElementNSImpl)this.fCurrentNode).setType((XSTypeDefinition)var4);
         }
      }

      if (this.fCurrentNode == this.fFragmentRoot) {
         this.fCurrentNode = null;
         this.fFragmentRoot = null;
      } else {
         this.fCurrentNode = this.fCurrentNode.getParentNode();
      }
   }

   public void startCDATA(Augmentations var1) throws XNIException {
   }

   public void endCDATA(Augmentations var1) throws XNIException {
   }

   public void endDocument(Augmentations var1) throws XNIException {
      int var2 = this.fTargetChildren.size();
      int var3;
      if (this.fNextSibling == null) {
         for(var3 = 0; var3 < var2; ++var3) {
            this.fTarget.appendChild((Node)this.fTargetChildren.get(var3));
         }
      } else {
         for(var3 = 0; var3 < var2; ++var3) {
            this.fTarget.insertBefore((Node)this.fTargetChildren.get(var3), this.fNextSibling);
         }
      }

   }

   public void setDocumentSource(XMLDocumentSource var1) {
   }

   public XMLDocumentSource getDocumentSource() {
      return null;
   }

   private void append(Node var1) throws XNIException {
      if (this.fCurrentNode != null) {
         this.fCurrentNode.appendChild(var1);
      } else {
         if ((kidOK[this.fTarget.getNodeType()] & 1 << var1.getNodeType()) == 0) {
            String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null);
            throw new XNIException(var2);
         }

         this.fTargetChildren.add(var1);
      }

   }

   static {
      kidOK[9] = 1410;
      kidOK[11] = kidOK[6] = kidOK[5] = kidOK[1] = 442;
      kidOK[2] = 40;
      kidOK[10] = 0;
      kidOK[7] = 0;
      kidOK[8] = 0;
      kidOK[3] = 0;
      kidOK[4] = 0;
      kidOK[12] = 0;
   }
}
