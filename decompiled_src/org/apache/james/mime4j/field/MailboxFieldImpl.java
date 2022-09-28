package org.apache.james.mime4j.field;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.dom.field.MailboxField;
import org.apache.james.mime4j.field.address.AddressBuilder;
import org.apache.james.mime4j.field.address.ParseException;
import org.apache.james.mime4j.stream.Field;

public class MailboxFieldImpl extends AbstractField implements MailboxField {
   private boolean parsed = false;
   private Mailbox mailbox;
   private ParseException parseException;
   public static final FieldParser PARSER = new FieldParser() {
      public MailboxField parse(Field rawField, DecodeMonitor monitor) {
         return new MailboxFieldImpl(rawField, monitor);
      }
   };

   MailboxFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public Mailbox getMailbox() {
      if (!this.parsed) {
         this.parse();
      }

      return this.mailbox;
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
         this.mailbox = AddressBuilder.DEFAULT.parseMailbox(body, this.monitor);
      } catch (ParseException var3) {
         this.parseException = var3;
      }

      this.parsed = true;
   }
}
