package org.apache.james.mime4j.field;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentMD5Field;
import org.apache.james.mime4j.stream.Field;

public class ContentMD5FieldImpl extends AbstractField implements ContentMD5Field {
   private boolean parsed = false;
   private String md5raw;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentMD5Field parse(Field rawField, DecodeMonitor monitor) {
         return new ContentMD5FieldImpl(rawField, monitor);
      }
   };

   ContentMD5FieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      String body = this.getBody();
      if (body != null) {
         this.md5raw = body.trim();
      } else {
         this.md5raw = null;
      }

   }

   public String getMD5Raw() {
      if (!this.parsed) {
         this.parse();
      }

      return this.md5raw;
   }
}
