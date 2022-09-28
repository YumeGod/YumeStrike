package org.apache.james.mime4j.field;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.field.ParseException;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.util.ByteSequence;

public abstract class AbstractField implements ParsedField {
   protected final Field rawField;
   protected final DecodeMonitor monitor;

   protected AbstractField(Field rawField, DecodeMonitor monitor) {
      this.rawField = rawField;
      this.monitor = monitor != null ? monitor : DecodeMonitor.SILENT;
   }

   public String getName() {
      return this.rawField.getName();
   }

   public String getBody() {
      return this.rawField.getBody();
   }

   public ByteSequence getRaw() {
      return this.rawField.getRaw();
   }

   public boolean isValidField() {
      return this.getParseException() == null;
   }

   public ParseException getParseException() {
      return null;
   }

   protected RawField getRawField() {
      return this.rawField instanceof RawField ? (RawField)this.rawField : new RawField(this.rawField.getName(), this.rawField.getBody());
   }

   public String toString() {
      return this.rawField.toString();
   }
}
