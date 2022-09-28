package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class DefaultDocument extends NodeImpl implements Document {
   private String fDocumentURI = null;

   public DocumentType getDoctype() {
      return null;
   }

   public DOMImplementation getImplementation() {
      return null;
   }

   public Element getDocumentElement() {
      return null;
   }

   public NodeList getElementsByTagName(String var1) {
      return null;
   }

   public NodeList getElementsByTagNameNS(String var1, String var2) {
      return null;
   }

   public Element getElementById(String var1) {
      return null;
   }

   public Node importNode(Node var1, boolean var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Element createElement(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public DocumentFragment createDocumentFragment() {
      return null;
   }

   public Text createTextNode(String var1) {
      return null;
   }

   public Comment createComment(String var1) {
      return null;
   }

   public CDATASection createCDATASection(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public ProcessingInstruction createProcessingInstruction(String var1, String var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Attr createAttribute(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public EntityReference createEntityReference(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Element createElementNS(String var1, String var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Attr createAttributeNS(String var1, String var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public String getInputEncoding() {
      return null;
   }

   public String getXmlEncoding() {
      return null;
   }

   public boolean getXmlStandalone() {
      throw new DOMException((short)9, "Method not supported");
   }

   public void setXmlStandalone(boolean var1) {
      throw new DOMException((short)9, "Method not supported");
   }

   public String getXmlVersion() {
      return null;
   }

   public void setXmlVersion(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public boolean getStrictErrorChecking() {
      return false;
   }

   public void setStrictErrorChecking(boolean var1) {
      throw new DOMException((short)9, "Method not supported");
   }

   public String getDocumentURI() {
      return this.fDocumentURI;
   }

   public void setDocumentURI(String var1) {
      this.fDocumentURI = var1;
   }

   public Node adoptNode(Node var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void normalizeDocument() {
      throw new DOMException((short)9, "Method not supported");
   }

   public DOMConfiguration getDomConfig() {
      throw new DOMException((short)9, "Method not supported");
   }

   public Node renameNode(Node var1, String var2, String var3) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }
}
