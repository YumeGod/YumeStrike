package org.apache.xerces.jaxp.validation;

import javax.xml.transform.dom.DOMResult;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

interface DOMDocumentHandler extends XMLDocumentHandler {
   void setDOMResult(DOMResult var1);

   void doctypeDecl(DocumentType var1) throws XNIException;

   void characters(Text var1) throws XNIException;

   void cdata(CDATASection var1) throws XNIException;

   void comment(Comment var1) throws XNIException;

   void processingInstruction(ProcessingInstruction var1) throws XNIException;

   void setIgnoringCharacters(boolean var1);
}
