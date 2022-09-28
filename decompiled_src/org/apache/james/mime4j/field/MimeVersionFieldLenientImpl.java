package org.apache.james.mime4j.field;

import java.util.BitSet;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.MimeVersionField;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.ParserCursor;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.stream.RawFieldParser;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

public class MimeVersionFieldLenientImpl extends AbstractField implements MimeVersionField {
   private static final int FULL_STOP = 46;
   private static final BitSet DELIM = RawFieldParser.INIT_BITSET(46);
   public static final int DEFAULT_MINOR_VERSION = 0;
   public static final int DEFAULT_MAJOR_VERSION = 1;
   private boolean parsed = false;
   private int major = 1;
   private int minor = 0;
   public static final FieldParser PARSER = new FieldParser() {
      public MimeVersionField parse(Field rawField, DecodeMonitor monitor) {
         return new MimeVersionFieldLenientImpl(rawField, monitor);
      }
   };

   MimeVersionFieldLenientImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      this.major = 1;
      this.minor = 0;
      RawField f = this.getRawField();
      ByteSequence buf = f.getRaw();
      int pos = f.getDelimiterIdx() + 1;
      if (buf == null) {
         String body = f.getBody();
         if (body == null) {
            return;
         }

         buf = ContentUtil.encode(body);
         pos = 0;
      }

      RawFieldParser parser = RawFieldParser.DEFAULT;
      ParserCursor cursor = new ParserCursor(pos, buf.length());
      String token1 = parser.parseValue(buf, cursor, DELIM);

      try {
         this.major = Integer.parseInt(token1);
         if (this.major < 0) {
            this.major = 0;
         }
      } catch (NumberFormatException var10) {
      }

      if (!cursor.atEnd() && buf.byteAt(cursor.getPos()) == 46) {
         cursor.updatePos(cursor.getPos() + 1);
      }

      String token2 = parser.parseValue(buf, cursor, (BitSet)null);

      try {
         this.minor = Integer.parseInt(token2);
         if (this.minor < 0) {
            this.minor = 0;
         }
      } catch (NumberFormatException var9) {
      }

   }

   public int getMinorVersion() {
      if (!this.parsed) {
         this.parse();
      }

      return this.minor;
   }

   public int getMajorVersion() {
      if (!this.parsed) {
         this.parse();
      }

      return this.major;
   }
}
