package org.apache.batik.dom.util;

import java.io.IOException;
import org.xml.sax.SAXException;

public class SAXIOException extends IOException {
   protected SAXException saxe;

   public SAXIOException(SAXException var1) {
      super(var1.getMessage());
      this.saxe = var1;
   }

   public SAXException getSAXException() {
      return this.saxe;
   }

   public Throwable getCause() {
      return this.saxe;
   }
}
