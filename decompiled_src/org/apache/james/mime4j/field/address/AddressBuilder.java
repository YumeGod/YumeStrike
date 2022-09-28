package org.apache.james.mime4j.field.address;

import java.io.StringReader;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.address.Group;
import org.apache.james.mime4j.dom.address.Mailbox;

public class AddressBuilder {
   public static final AddressBuilder DEFAULT = new AddressBuilder();

   protected AddressBuilder() {
   }

   public Address parseAddress(String rawAddressString, DecodeMonitor monitor) throws ParseException {
      AddressListParser parser = new AddressListParser(new StringReader(rawAddressString));
      return Builder.getInstance().buildAddress(parser.parseAddress(), monitor);
   }

   public Address parseAddress(String rawAddressString) throws ParseException {
      return this.parseAddress(rawAddressString, DecodeMonitor.STRICT);
   }

   public AddressList parseAddressList(String rawAddressList, DecodeMonitor monitor) throws ParseException {
      AddressListParser parser = new AddressListParser(new StringReader(rawAddressList));
      return Builder.getInstance().buildAddressList(parser.parseAddressList(), monitor);
   }

   public AddressList parseAddressList(String rawAddressList) throws ParseException {
      return this.parseAddressList(rawAddressList, DecodeMonitor.STRICT);
   }

   public Mailbox parseMailbox(String rawMailboxString, DecodeMonitor monitor) throws ParseException {
      AddressListParser parser = new AddressListParser(new StringReader(rawMailboxString));
      return Builder.getInstance().buildMailbox(parser.parseMailbox(), monitor);
   }

   public Mailbox parseMailbox(String rawMailboxString) throws ParseException {
      return this.parseMailbox(rawMailboxString, DecodeMonitor.STRICT);
   }

   public Group parseGroup(String rawGroupString, DecodeMonitor monitor) throws ParseException {
      Address address = this.parseAddress(rawGroupString, monitor);
      if (!(address instanceof Group)) {
         throw new ParseException("Not a group address");
      } else {
         return (Group)address;
      }
   }

   public Group parseGroup(String rawGroupString) throws ParseException {
      return this.parseGroup(rawGroupString, DecodeMonitor.STRICT);
   }
}
