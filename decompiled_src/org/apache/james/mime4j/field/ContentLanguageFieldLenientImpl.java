package org.apache.james.mime4j.field;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentLanguageField;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.ParserCursor;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.stream.RawFieldParser;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

public class ContentLanguageFieldLenientImpl extends AbstractField implements ContentLanguageField {
   private static final int COMMA = 44;
   private static final BitSet DELIM = RawFieldParser.INIT_BITSET(44);
   private boolean parsed = false;
   private List languages;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentLanguageField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentLanguageFieldLenientImpl(rawField, monitor);
      }
   };

   ContentLanguageFieldLenientImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      this.languages = new ArrayList();
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

      while(true) {
         String token = parser.parseToken(buf, cursor, DELIM);
         if (token.length() > 0) {
            this.languages.add(token);
         }

         if (cursor.atEnd()) {
            return;
         }

         pos = cursor.getPos();
         if (buf.byteAt(pos) == 44) {
            cursor.updatePos(pos + 1);
         }
      }
   }

   public List getLanguages() {
      if (!this.parsed) {
         this.parse();
      }

      return new ArrayList(this.languages);
   }
}
