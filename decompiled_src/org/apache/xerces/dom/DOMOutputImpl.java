package org.apache.xerces.dom;

import java.io.OutputStream;
import java.io.Writer;
import org.w3c.dom.ls.LSOutput;

public class DOMOutputImpl implements LSOutput {
   protected Writer fCharStream = null;
   protected OutputStream fByteStream = null;
   protected String fSystemId = null;
   protected String fEncoding = null;

   public Writer getCharacterStream() {
      return this.fCharStream;
   }

   public void setCharacterStream(Writer var1) {
      this.fCharStream = var1;
   }

   public OutputStream getByteStream() {
      return this.fByteStream;
   }

   public void setByteStream(OutputStream var1) {
      this.fByteStream = var1;
   }

   public String getSystemId() {
      return this.fSystemId;
   }

   public void setSystemId(String var1) {
      this.fSystemId = var1;
   }

   public String getEncoding() {
      return this.fEncoding;
   }

   public void setEncoding(String var1) {
      this.fEncoding = var1;
   }
}
