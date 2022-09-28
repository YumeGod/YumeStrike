package org.apache.james.mime4j.field.address;

public interface AddressListParserTreeConstants {
   int JJTVOID = 0;
   int JJTADDRESS_LIST = 1;
   int JJTADDRESS = 2;
   int JJTMAILBOX = 3;
   int JJTNAME_ADDR = 4;
   int JJTGROUP_BODY = 5;
   int JJTANGLE_ADDR = 6;
   int JJTROUTE = 7;
   int JJTPHRASE = 8;
   int JJTADDR_SPEC = 9;
   int JJTLOCAL_PART = 10;
   int JJTDOMAIN = 11;
   String[] jjtNodeName = new String[]{"void", "address_list", "address", "mailbox", "name_addr", "group_body", "angle_addr", "route", "phrase", "addr_spec", "local_part", "domain"};
}
