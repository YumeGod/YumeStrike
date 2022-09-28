package org.apache.james.mime4j.field;

import java.util.Locale;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentTransferEncodingField;
import org.apache.james.mime4j.stream.Field;

public class ContentTransferEncodingFieldImpl extends AbstractField implements ContentTransferEncodingField {
   private boolean parsed = false;
   private String encoding;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentTransferEncodingField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentTransferEncodingFieldImpl(rawField, monitor);
      }
   };

   ContentTransferEncodingFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      String body = this.getBody();
      if (body != null) {
         this.encoding = body.trim().toLowerCase(Locale.US);
      } else {
         this.encoding = null;
      }

   }

   public String getEncoding() {
      if (!this.parsed) {
         this.parse();
      }

      return this.encoding;
   }

   public static String getEncoding(ContentTransferEncodingField f) {
      return f != null && f.getEncoding().length() != 0 ? f.getEncoding() : "7bit";
   }
}
