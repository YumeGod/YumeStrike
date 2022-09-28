package org.apache.xerces.dom;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PSVIDocumentImpl extends DocumentImpl {
   static final long serialVersionUID = -8822220250676434522L;

   public PSVIDocumentImpl() {
   }

   public PSVIDocumentImpl(DocumentType var1) {
      super(var1);
   }

   public Node cloneNode(boolean var1) {
      PSVIDocumentImpl var2 = new PSVIDocumentImpl();
      this.callUserDataHandlers(this, var2, (short)1);
      this.cloneNode(var2, var1);
      var2.mutationEvents = super.mutationEvents;
      return var2;
   }

   public DOMImplementation getImplementation() {
      return PSVIDOMImplementationImpl.getDOMImplementation();
   }

   public Element createElementNS(String var1, String var2) throws DOMException {
      return new PSVIElementNSImpl(this, var1, var2);
   }

   public Element createElementNS(String var1, String var2, String var3) throws DOMException {
      return new PSVIElementNSImpl(this, var1, var2, var3);
   }

   public Attr createAttributeNS(String var1, String var2) throws DOMException {
      return new PSVIAttrNSImpl(this, var1, var2);
   }

   public Attr createAttributeNS(String var1, String var2, String var3) throws DOMException {
      return new PSVIAttrNSImpl(this, var1, var2, var3);
   }

   public DOMConfiguration getDomConfig() {
      super.getDomConfig();
      return super.fConfiguration;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      throw new NotSerializableException(this.getClass().getName());
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      throw new NotSerializableException(this.getClass().getName());
   }
}
