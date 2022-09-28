package org.apache.batik.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class GenericDocument extends AbstractDocument {
   protected static final String ATTR_ID = "id";
   protected boolean readonly;

   protected GenericDocument() {
   }

   public GenericDocument(DocumentType var1, DOMImplementation var2) {
      super(var1, var2);
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   public boolean isId(Attr var1) {
      return var1.getNamespaceURI() != null ? false : "id".equals(var1.getNodeName());
   }

   public Element createElement(String var1) throws DOMException {
      return new GenericElement(var1.intern(), this);
   }

   public DocumentFragment createDocumentFragment() {
      return new GenericDocumentFragment(this);
   }

   public Text createTextNode(String var1) {
      return new GenericText(var1, this);
   }

   public Comment createComment(String var1) {
      return new GenericComment(var1, this);
   }

   public CDATASection createCDATASection(String var1) throws DOMException {
      return new GenericCDATASection(var1, this);
   }

   public ProcessingInstruction createProcessingInstruction(String var1, String var2) throws DOMException {
      return new GenericProcessingInstruction(var1, var2, this);
   }

   public Attr createAttribute(String var1) throws DOMException {
      return new GenericAttr(var1.intern(), this);
   }

   public EntityReference createEntityReference(String var1) throws DOMException {
      return new GenericEntityReference(var1, this);
   }

   public Element createElementNS(String var1, String var2) throws DOMException {
      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      return (Element)(var1 == null ? new GenericElement(var2.intern(), this) : new GenericElementNS(var1.intern(), var2.intern(), this));
   }

   public Attr createAttributeNS(String var1, String var2) throws DOMException {
      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      return (Attr)(var1 == null ? new GenericAttr(var2.intern(), this) : new GenericAttrNS(var1.intern(), var2.intern(), this));
   }

   protected Node newNode() {
      return new GenericDocument();
   }
}
