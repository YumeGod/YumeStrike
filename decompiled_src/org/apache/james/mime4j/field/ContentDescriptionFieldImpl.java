package org.apache.james.mime4j.field;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentDescriptionField;
import org.apache.james.mime4j.stream.Field;

public class ContentDescriptionFieldImpl extends AbstractField implements ContentDescriptionField {
   private boolean parsed = false;
   private String description;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentDescriptionField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentDescriptionFieldImpl(rawField, monitor);
      }
   };

   ContentDescriptionFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      String body = this.getBody();
      if (body != null) {
         this.description = body.trim();
      } else {
         this.description = null;
      }

   }

   public String getDescription() {
      if (!this.parsed) {
         this.parse();
      }

      return this.description;
   }
}
