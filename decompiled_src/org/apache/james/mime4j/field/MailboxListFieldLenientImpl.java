package org.apache.james.mime4j.field;

import java.util.Collections;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.address.MailboxList;
import org.apache.james.mime4j.dom.field.MailboxListField;
import org.apache.james.mime4j.field.address.LenientAddressBuilder;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.ParserCursor;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

public class MailboxListFieldLenientImpl extends AbstractField implements MailboxListField {
   private boolean parsed = false;
   private MailboxList mailboxList;
   public static final FieldParser PARSER = new FieldParser() {
      public MailboxListField parse(Field rawField, DecodeMonitor monitor) {
         return new MailboxListFieldLenientImpl(rawField, monitor);
      }
   };

   MailboxListFieldLenientImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public MailboxList getMailboxList() {
      if (!this.parsed) {
         this.parse();
      }

      return this.mailboxList;
   }

   private void parse() {
      this.parsed = true;
      RawField f = this.getRawField();
      ByteSequence buf = f.getRaw();
      int pos = f.getDelimiterIdx() + 1;
      if (buf == null) {
         String body = f.getBody();
         if (body == null) {
            this.mailboxList = new MailboxList(Collections.emptyList(), true);
            return;
         }

         buf = ContentUtil.encode(body);
         pos = 0;
      }

      ParserCursor cursor = new ParserCursor(pos, buf.length());
      this.mailboxList = LenientAddressBuilder.DEFAULT.parseAddressList(buf, cursor).flatten();
   }
}
