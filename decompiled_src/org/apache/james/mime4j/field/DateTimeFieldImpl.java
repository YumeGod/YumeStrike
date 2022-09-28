package org.apache.james.mime4j.field;

import java.io.StringReader;
import java.util.Date;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.DateTimeField;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParser;
import org.apache.james.mime4j.field.datetime.parser.ParseException;
import org.apache.james.mime4j.field.datetime.parser.TokenMgrError;
import org.apache.james.mime4j.stream.Field;

public class DateTimeFieldImpl extends AbstractField implements DateTimeField {
   private boolean parsed = false;
   private Date date;
   private ParseException parseException;
   public static final FieldParser PARSER = new FieldParser() {
      public DateTimeField parse(Field rawField, DecodeMonitor monitor) {
         return new DateTimeFieldImpl(rawField, monitor);
      }
   };

   DateTimeFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public Date getDate() {
      if (!this.parsed) {
         this.parse();
      }

      return this.date;
   }

   public ParseException getParseException() {
      if (!this.parsed) {
         this.parse();
      }

      return this.parseException;
   }

   private void parse() {
      String body = this.getBody();

      try {
         this.date = (new DateTimeParser(new StringReader(body))).parseAll().getDate();
      } catch (ParseException var3) {
         this.parseException = var3;
      } catch (TokenMgrError var4) {
         this.parseException = new ParseException(var4.getMessage());
      }

      this.parsed = true;
   }
}
