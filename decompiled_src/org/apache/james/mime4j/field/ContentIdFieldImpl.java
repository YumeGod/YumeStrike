package org.apache.james.mime4j.field;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentIdField;
import org.apache.james.mime4j.stream.Field;

public class ContentIdFieldImpl extends AbstractField implements ContentIdField {
   private boolean parsed = false;
   private String id;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentIdField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentIdFieldImpl(rawField, monitor);
      }
   };

   ContentIdFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      String body = this.getBody();
      if (body != null) {
         this.id = body.trim();
      } else {
         this.id = null;
      }

   }

   public String getId() {
      if (!this.parsed) {
         this.parse();
      }

      return this.id;
   }
}
