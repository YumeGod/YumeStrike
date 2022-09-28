package org.apache.james.mime4j.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import org.apache.james.mime4j.dom.TextBody;

class StringTextBody extends TextBody {
   private final String text;
   private final Charset charset;

   public StringTextBody(String text, Charset charset) {
      this.text = text;
      this.charset = charset;
   }

   public String getMimeCharset() {
      return this.charset.name();
   }

   public InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream(this.text.getBytes(this.charset.name()));
   }

   public Reader getReader() throws IOException {
      return new StringReader(this.text);
   }

   public void writeTo(OutputStream out) throws IOException {
      if (out == null) {
         throw new IllegalArgumentException();
      } else {
         Reader reader = new StringReader(this.text);
         Writer writer = new OutputStreamWriter(out, this.charset);
         char[] buffer = new char[1024];

         while(true) {
            int nChars = reader.read(buffer);
            if (nChars == -1) {
               reader.close();
               writer.flush();
               return;
            }

            writer.write(buffer, 0, nChars);
         }
      }
   }

   public StringTextBody copy() {
      return new StringTextBody(this.text, this.charset);
   }
}
