package org.apache.james.mime4j.stream;

import java.util.BitSet;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.io.MaxHeaderLengthLimitException;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public class DefaultFieldBuilder implements FieldBuilder {
   private static final BitSet FIELD_CHARS = new BitSet();
   private final ByteArrayBuffer buf = new ByteArrayBuffer(1024);
   private final int maxlen;

   public DefaultFieldBuilder(int maxlen) {
      this.maxlen = maxlen;
   }

   public void reset() {
      this.buf.clear();
   }

   public void append(ByteArrayBuffer line) throws MaxHeaderLengthLimitException {
      if (line != null) {
         int len = line.length();
         if (this.maxlen > 0 && this.buf.length() + len >= this.maxlen) {
            throw new MaxHeaderLengthLimitException("Maximum header length limit exceeded");
         } else {
            this.buf.append(line.buffer(), 0, line.length());
         }
      }
   }

   public RawField build() throws MimeException {
      int len = this.buf.length();
      if (len > 0) {
         if (this.buf.byteAt(len - 1) == 10) {
            --len;
         }

         if (this.buf.byteAt(len - 1) == 13) {
            --len;
         }
      }

      ByteArrayBuffer copy = new ByteArrayBuffer(this.buf.buffer(), len, false);
      RawField field = RawFieldParser.DEFAULT.parseField(copy);
      String name = field.getName();

      for(int i = 0; i < name.length(); ++i) {
         char ch = name.charAt(i);
         if (!FIELD_CHARS.get(ch)) {
            throw new MimeException("MIME field name contains illegal characters: " + field.getName());
         }
      }

      return field;
   }

   public ByteArrayBuffer getRaw() {
      return this.buf;
   }

   static {
      int i;
      for(i = 33; i <= 57; ++i) {
         FIELD_CHARS.set(i);
      }

      for(i = 59; i <= 126; ++i) {
         FIELD_CHARS.set(i);
      }

   }
}
