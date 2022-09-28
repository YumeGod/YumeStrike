package org.apache.james.mime4j.field;

import java.util.BitSet;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.dom.field.MailboxField;
import org.apache.james.mime4j.field.address.LenientAddressBuilder;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.ParserCursor;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

public class MailboxFieldLenientImpl extends AbstractField implements MailboxField {
   private boolean parsed = false;
   private Mailbox mailbox;
   public static final FieldParser PARSER = new FieldParser() {
      public MailboxField parse(Field rawField, DecodeMonitor monitor) {
         return new MailboxFieldLenientImpl(rawField, monitor);
      }
   };

   MailboxFieldLenientImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public Mailbox getMailbox() {
      if (!this.parsed) {
         this.parse();
      }

      return this.mailbox;
   }

   private void parse() {
      this.parsed = true;
      RawField f = this.getRawField();
      ByteSequence buf = f.getRaw();
      int pos = f.getDelimiterIdx() + 1;
      if (buf == null) {
         String body = f.getBody();
         if (body == null) {
            return;
         }

         buf = ContentUtil.encode(body);
         pos = 0;
      }

      ParserCursor cursor = new ParserCursor(pos, buf.length());
      this.mailbox = LenientAddressBuilder.DEFAULT.parseMailbox(buf, cursor, (BitSet)null);
   }
}
