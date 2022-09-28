package org.apache.james.mime4j.field;

import java.util.Collections;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.field.AddressListField;
import org.apache.james.mime4j.field.address.LenientAddressBuilder;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.ParserCursor;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

public class AddressListFieldLenientImpl extends AbstractField implements AddressListField {
   private boolean parsed = false;
   private AddressList addressList;
   public static final FieldParser PARSER = new FieldParser() {
      public AddressListField parse(Field rawField, DecodeMonitor monitor) {
         return new AddressListFieldLenientImpl(rawField, monitor);
      }
   };

   AddressListFieldLenientImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public AddressList getAddressList() {
      if (!this.parsed) {
         this.parse();
      }

      return this.addressList;
   }

   private void parse() {
      this.parsed = true;
      RawField f = this.getRawField();
      ByteSequence buf = f.getRaw();
      int pos = f.getDelimiterIdx() + 1;
      if (buf == null) {
         String body = f.getBody();
         if (body == null) {
            this.addressList = new AddressList(Collections.emptyList(), true);
            return;
         }

         buf = ContentUtil.encode(body);
         pos = 0;
      }

      ParserCursor cursor = new ParserCursor(pos, buf.length());
      this.addressList = LenientAddressBuilder.DEFAULT.parseAddressList(buf, cursor);
   }
}
