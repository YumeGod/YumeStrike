package org.apache.james.mime4j.dom.field;

import org.apache.james.mime4j.dom.address.AddressList;

public interface AddressListField extends ParsedField {
   AddressList getAddressList();
}
