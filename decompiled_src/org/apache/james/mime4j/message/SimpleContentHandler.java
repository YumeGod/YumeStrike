package org.apache.james.mime4j.message;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.field.LenientFieldParser;
import org.apache.james.mime4j.parser.AbstractContentHandler;
import org.apache.james.mime4j.stream.Field;

public abstract class SimpleContentHandler extends AbstractContentHandler {
   private final FieldParser fieldParser;
   private final DecodeMonitor monitor;
   private Header currHeader;

   public SimpleContentHandler(FieldParser fieldParser, DecodeMonitor monitor) {
      this.fieldParser = (FieldParser)(fieldParser != null ? fieldParser : LenientFieldParser.getParser());
      this.monitor = monitor != null ? monitor : DecodeMonitor.SILENT;
   }

   public SimpleContentHandler() {
      this((FieldParser)null, (DecodeMonitor)null);
   }

   public abstract void headers(Header var1);

   public final void startHeader() {
      this.currHeader = new HeaderImpl();
   }

   public final void field(Field field) throws MimeException {
      ParsedField parsedField;
      if (field instanceof ParsedField) {
         parsedField = (ParsedField)field;
      } else {
         parsedField = this.fieldParser.parse(field, this.monitor);
      }

      this.currHeader.addField(parsedField);
   }

   public final void endHeader() {
      Header tmp = this.currHeader;
      this.currHeader = null;
      this.headers(tmp);
   }
}
