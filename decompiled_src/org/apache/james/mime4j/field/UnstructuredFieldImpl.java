package org.apache.james.mime4j.field;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.codec.DecoderUtil;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.UnstructuredField;
import org.apache.james.mime4j.stream.Field;

public class UnstructuredFieldImpl extends AbstractField implements UnstructuredField {
   private boolean parsed = false;
   private String value;
   public static final FieldParser PARSER = new FieldParser() {
      public UnstructuredField parse(Field rawField, DecodeMonitor monitor) {
         return new UnstructuredFieldImpl(rawField, monitor);
      }
   };

   UnstructuredFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public String getValue() {
      if (!this.parsed) {
         this.parse();
      }

      return this.value;
   }

   private void parse() {
      String body = this.getBody();
      this.value = DecoderUtil.decodeEncodedWords(body, this.monitor);
      this.parsed = true;
   }
}
