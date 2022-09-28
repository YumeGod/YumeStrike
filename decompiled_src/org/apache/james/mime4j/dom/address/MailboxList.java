package org.apache.james.mime4j.dom.address;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MailboxList extends AbstractList implements Serializable {
   private static final long serialVersionUID = 1L;
   private final List mailboxes;

   public MailboxList(List mailboxes, boolean dontCopy) {
      if (mailboxes != null) {
         this.mailboxes = (List)(dontCopy ? mailboxes : new ArrayList(mailboxes));
      } else {
         this.mailboxes = Collections.emptyList();
      }

   }

   public int size() {
      return this.mailboxes.size();
   }

   public Mailbox get(int index) {
      return (Mailbox)this.mailboxes.get(index);
   }
}
