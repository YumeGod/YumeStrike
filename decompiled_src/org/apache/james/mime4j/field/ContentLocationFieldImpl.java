package org.apache.james.mime4j.field;

import java.io.StringReader;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentLocationField;
import org.apache.james.mime4j.field.structured.parser.ParseException;
import org.apache.james.mime4j.field.structured.parser.StructuredFieldParser;
import org.apache.james.mime4j.stream.Field;

public class ContentLocationFieldImpl extends AbstractField implements ContentLocationField {
   private boolean parsed = false;
   private String location;
   private ParseException parseException;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentLocationField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentLocationFieldImpl(rawField, monitor);
      }
   };

   ContentLocationFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      String body = this.getBody();
      this.location = null;
      if (body != null) {
         StringReader stringReader = new StringReader(body);
         StructuredFieldParser parser = new StructuredFieldParser(stringReader);

         try {
            this.location = parser.parse().replaceAll("\\s", "");
         } catch (ParseException var5) {
            this.parseException = var5;
         }
      }

   }

   public String getLocation() {
      if (!this.parsed) {
         this.parse();
      }

      return this.location;
   }

   public org.apache.james.mime4j.dom.field.ParseException getParseException() {
      return this.parseException;
   }
}
