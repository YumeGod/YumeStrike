package org.apache.xerces.dom;

import org.apache.xerces.xni.parser.XMLParseException;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMLocator;

public class DOMErrorImpl implements DOMError {
   public short fSeverity = 1;
   public String fMessage = null;
   public DOMLocatorImpl fLocator = new DOMLocatorImpl();
   public Exception fException = null;
   public String fType;
   public Object fRelatedData;

   public DOMErrorImpl() {
   }

   public DOMErrorImpl(short var1, XMLParseException var2) {
      this.fSeverity = var1;
      this.fException = var2;
      this.fLocator = this.createDOMLocator(var2);
   }

   public short getSeverity() {
      return this.fSeverity;
   }

   public String getMessage() {
      return this.fMessage;
   }

   public DOMLocator getLocation() {
      return this.fLocator;
   }

   private DOMLocatorImpl createDOMLocator(XMLParseException var1) {
      return new DOMLocatorImpl(var1.getLineNumber(), var1.getColumnNumber(), var1.getCharacterOffset(), var1.getExpandedSystemId());
   }

   public Object getRelatedException() {
      return this.fException;
   }

   public void reset() {
      this.fSeverity = 1;
      this.fException = null;
   }

   public String getType() {
      return this.fType;
   }

   public Object getRelatedData() {
      return this.fRelatedData;
   }
}
