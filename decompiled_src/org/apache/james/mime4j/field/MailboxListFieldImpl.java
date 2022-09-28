package org.apache.james.mime4j.field;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.address.MailboxList;
import org.apache.james.mime4j.dom.field.MailboxListField;
import org.apache.james.mime4j.field.address.AddressBuilder;
import org.apache.james.mime4j.field.address.ParseException;
import org.apache.james.mime4j.stream.Field;

public class MailboxListFieldImpl extends AbstractField implements MailboxListField {
   private boolean parsed = false;
   private MailboxList mailboxList;
   private ParseException parseException;
   public static final FieldParser PARSER = new FieldParser() {
      public MailboxListField parse(Field rawField, DecodeMonitor monitor) {
         return new MailboxListFieldImpl(rawField, monitor);
      }
   };

   MailboxListFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public MailboxList getMailboxList() {
      if (!this.parsed) {
         this.parse();
      }

      return this.mailboxList;
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
         this.mailboxList = AddressBuilder.DEFAULT.parseAddressList(body, this.monitor).flatten();
      } catch (ParseException var3) {
         this.parseException = var3;
      }

      this.parsed = true;
   }
}
