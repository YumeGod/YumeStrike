package org.w3c.dom;

public interface DOMImplementation {
   boolean hasFeature(String var1, String var2);

   DocumentType createDocumentType(String var1, String var2, String var3) throws DOMException;

   Document createDocument(String var1, String var2, DocumentType var3) throws DOMException;

   Object getFeature(String var1, String var2);
}
