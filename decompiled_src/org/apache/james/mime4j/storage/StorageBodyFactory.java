package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.message.BodyFactory;
import org.apache.james.mime4j.util.CharsetUtil;

public class StorageBodyFactory implements BodyFactory {
   private static final Charset FALLBACK_CHARSET;
   private final StorageProvider storageProvider;
   private final DecodeMonitor monitor;

   public StorageBodyFactory() {
      this((StorageProvider)null, (DecodeMonitor)null);
   }

   public StorageBodyFactory(StorageProvider storageProvider, DecodeMonitor monitor) {
      this.storageProvider = storageProvider != null ? storageProvider : DefaultStorageProvider.getInstance();
      this.monitor = monitor != null ? monitor : DecodeMonitor.SILENT;
   }

   public StorageProvider getStorageProvider() {
      return this.storageProvider;
   }

   public BinaryBody binaryBody(InputStream is) throws IOException {
      if (is == null) {
         throw new IllegalArgumentException();
      } else {
         Storage storage = this.storageProvider.store(is);
         return new StorageBinaryBody(new MultiReferenceStorage(storage));
      }
   }

   public BinaryBody binaryBody(Storage storage) throws IOException {
      if (storage == null) {
         throw new IllegalArgumentException();
      } else {
         return new StorageBinaryBody(new MultiReferenceStorage(storage));
      }
   }

   public TextBody textBody(InputStream is) throws IOException {
      if (is == null) {
         throw new IllegalArgumentException();
      } else {
         Storage storage = this.storageProvider.store(is);
         return new StorageTextBody(new MultiReferenceStorage(storage), CharsetUtil.DEFAULT_CHARSET);
      }
   }

   public TextBody textBody(InputStream is, String mimeCharset) throws IOException {
      if (is == null) {
         throw new IllegalArgumentException();
      } else if (mimeCharset == null) {
         throw new IllegalArgumentException();
      } else {
         Storage storage = this.storageProvider.store(is);
         Charset charset = toJavaCharset(mimeCharset, false, this.monitor);
         return new StorageTextBody(new MultiReferenceStorage(storage), charset);
      }
   }

   public TextBody textBody(Storage storage) throws IOException {
      if (storage == null) {
         throw new IllegalArgumentException();
      } else {
         return new StorageTextBody(new MultiReferenceStorage(storage), CharsetUtil.DEFAULT_CHARSET);
      }
   }

   public TextBody textBody(Storage storage, String mimeCharset) throws IOException {
      if (storage == null) {
         throw new IllegalArgumentException();
      } else if (mimeCharset == null) {
         throw new IllegalArgumentException();
      } else {
         Charset charset = toJavaCharset(mimeCharset, false, this.monitor);
         return new StorageTextBody(new MultiReferenceStorage(storage), charset);
      }
   }

   public TextBody textBody(String text) {
      if (text == null) {
         throw new IllegalArgumentException();
      } else {
         return new StringTextBody(text, CharsetUtil.DEFAULT_CHARSET);
      }
   }

   public TextBody textBody(String text, String mimeCharset) {
      if (text == null) {
         throw new IllegalArgumentException();
      } else if (mimeCharset == null) {
         throw new IllegalArgumentException();
      } else {
         Charset charset = toJavaCharset(mimeCharset, true, this.monitor);
         return new StringTextBody(text, charset);
      }
   }

   private static Charset toJavaCharset(String mimeCharset, boolean forEncoding, DecodeMonitor monitor) {
      Charset charset = CharsetUtil.lookup(mimeCharset);
      if (charset == null) {
         if (monitor.isListening()) {
            monitor.warn("MIME charset '" + mimeCharset + "' has no " + "corresponding Java charset", "Using " + FALLBACK_CHARSET + " instead.");
         }

         return FALLBACK_CHARSET;
      } else {
         return charset;
      }
   }

   static {
      FALLBACK_CHARSET = CharsetUtil.DEFAULT_CHARSET;
   }
}
