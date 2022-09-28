package org.apache.fop.render.intermediate;

import org.apache.fop.util.DelegatingContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class DelegatingFragmentContentHandler extends DelegatingContentHandler {
   public DelegatingFragmentContentHandler(ContentHandler delegate) {
      this.setDelegateContentHandler(delegate);
      if (delegate instanceof LexicalHandler) {
         this.setDelegateLexicalHandler((LexicalHandler)delegate);
      }

      if (delegate instanceof DTDHandler) {
         this.setDelegateDTDHandler((DTDHandler)delegate);
      }

      if (delegate instanceof EntityResolver) {
         this.setDelegateEntityResolver((EntityResolver)delegate);
      }

      if (delegate instanceof ErrorHandler) {
         this.setDelegateErrorHandler((ErrorHandler)delegate);
      }

   }

   public void startDocument() throws SAXException {
   }

   public void endDocument() throws SAXException {
   }
}
