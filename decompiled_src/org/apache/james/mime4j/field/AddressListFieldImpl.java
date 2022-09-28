package org.apache.james.mime4j.field;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.field.AddressListField;
import org.apache.james.mime4j.field.address.AddressBuilder;
import org.apache.james.mime4j.field.address.ParseException;
import org.apache.james.mime4j.stream.Field;

public class AddressListFieldImpl extends AbstractField implements AddressListField {
   private boolean parsed = false;
   private AddressList addressList;
   private ParseException parseException;
   public static final FieldParser PARSER = new FieldParser() {
      public AddressListField parse(Field rawField, DecodeMonitor monitor) {
         return new AddressListFieldImpl(rawField, monitor);
      }
   };

   AddressListFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public AddressList getAddressList() {
      if (!this.parsed) {
         this.parse();
      }

      return this.addressList;
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
         this.addressList = AddressBuilder.DEFAULT.parseAddressList(body, this.monitor);
      } catch (ParseException var3) {
         this.parseException = var3;
      }

      this.parsed = true;
   }
}
