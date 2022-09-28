package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import org.apache.james.mime4j.dom.TextBody;

class StorageTextBody extends TextBody {
   private MultiReferenceStorage storage;
   private Charset charset;

   public StorageTextBody(MultiReferenceStorage storage, Charset charset) {
      this.storage = storage;
      this.charset = charset;
   }

   public String getMimeCharset() {
      return this.charset.name();
   }

   public Reader getReader() throws IOException {
      return new InputStreamReader(this.storage.getInputStream(), this.charset);
   }

   public InputStream getInputStream() throws IOException {
      return this.storage.getInputStream();
   }

   public StorageTextBody copy() {
      this.storage.addReference();
      return new StorageTextBody(this.storage, this.charset);
   }

   public void dispose() {
      if (this.storage != null) {
         this.storage.delete();
         this.storage = null;
      }

   }
}
