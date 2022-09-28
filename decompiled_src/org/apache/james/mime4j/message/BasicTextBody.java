package org.apache.james.mime4j.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.james.mime4j.dom.SingleBody;
import org.apache.james.mime4j.dom.TextBody;

class BasicTextBody extends TextBody {
   private final byte[] content;
   private final String charset;

   BasicTextBody(byte[] content, String charset) {
      this.content = content;
      this.charset = charset;
   }

   public String getMimeCharset() {
      return this.charset;
   }

   public Reader getReader() throws IOException {
      return new InputStreamReader(this.getInputStream(), this.charset);
   }

   public InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream(this.content);
   }

   public SingleBody copy() {
      return new BasicTextBody(this.content, this.charset);
   }
}
