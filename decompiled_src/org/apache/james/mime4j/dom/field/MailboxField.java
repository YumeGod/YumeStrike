package org.apache.james.mime4j.dom.field;

import org.apache.james.mime4j.dom.address.Mailbox;

public interface MailboxField extends ParsedField {
   Mailbox getMailbox();
}
