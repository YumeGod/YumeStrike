package org.apache.james.mime4j.field;

import java.util.BitSet;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentLocationField;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.ParserCursor;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.stream.RawFieldParser;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.CharsetUtil;
import org.apache.james.mime4j.util.ContentUtil;

public class ContentLocationFieldLenientImpl extends AbstractField implements ContentLocationField {
   private boolean parsed = false;
   private String location;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentLocationField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentLocationFieldLenientImpl(rawField, monitor);
      }
   };

   ContentLocationFieldLenientImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      this.location = null;
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
      String token = parser.parseValue(buf, cursor, (BitSet)null);
      StringBuilder sb = new StringBuilder(token.length());

      for(int i = 0; i < token.length(); ++i) {
         char ch = token.charAt(i);
         if (!CharsetUtil.isWhitespace(ch)) {
            sb.append(ch);
         }
      }

      this.location = sb.toString();
   }

   public String getLocation() {
      if (!this.parsed) {
         this.parse();
      }

      return this.location;
   }
}
