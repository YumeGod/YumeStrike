package org.apache.james.mime4j.dom.field;

import org.apache.james.mime4j.dom.address.MailboxList;

public interface MailboxListField extends ParsedField {
   MailboxList getMailboxList();
}
