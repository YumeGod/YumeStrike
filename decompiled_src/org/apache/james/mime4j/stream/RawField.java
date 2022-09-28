package org.apache.james.mime4j.stream;

import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.CharsetUtil;
import org.apache.james.mime4j.util.ContentUtil;
import org.apache.james.mime4j.util.MimeUtil;

public final class RawField implements Field {
   private final ByteSequence raw;
   private final int delimiterIdx;
   private final String name;
   private final String body;

   RawField(ByteSequence raw, int delimiterIdx, String name, String body) {
      if (name == null) {
         throw new IllegalArgumentException("Field may not be null");
      } else {
         this.raw = raw;
         this.delimiterIdx = delimiterIdx;
         this.name = name.trim();
         this.body = body;
      }
   }

   public RawField(String name, String body) {
      this((ByteSequence)null, -1, name, body);
   }

   public ByteSequence getRaw() {
      return this.raw;
   }

   public String getName() {
      return this.name;
   }

   public String getBody() {
      if (this.body != null) {
         return this.body;
      } else if (this.raw != null) {
         int len = this.raw.length();
         int off = this.delimiterIdx + 1;
         if (len > off + 1 && CharsetUtil.isWhitespace((char)(this.raw.byteAt(off) & 255))) {
            ++off;
         }

         return MimeUtil.unfold(ContentUtil.decode(this.raw, off, len - off));
      } else {
         return null;
      }
   }

   public int getDelimiterIdx() {
      return this.delimiterIdx;
   }

   public String toString() {
      if (this.raw != null) {
         return ContentUtil.decode(this.raw);
      } else {
         StringBuilder buf = new StringBuilder();
         buf.append(this.name);
         buf.append(": ");
         if (this.body != null) {
            buf.append(this.body);
         }

         return buf.toString();
      }
   }
}
