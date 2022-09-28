package org.apache.james.mime4j.dom.address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Group extends Address {
   private static final long serialVersionUID = 1L;
   private final String name;
   private final MailboxList mailboxList;

   public Group(String name, MailboxList mailboxes) {
      if (name == null) {
         throw new IllegalArgumentException();
      } else if (mailboxes == null) {
         throw new IllegalArgumentException();
      } else {
         this.name = name;
         this.mailboxList = mailboxes;
      }
   }

   public Group(String name, Mailbox... mailboxes) {
      this(name, new MailboxList(Arrays.asList(mailboxes), true));
   }

   public Group(String name, Collection mailboxes) {
      this(name, new MailboxList(new ArrayList(mailboxes), true));
   }

   public String getName() {
      return this.name;
   }

   public MailboxList getMailboxes() {
      return this.mailboxList;
   }

   protected void doAddMailboxesTo(List results) {
      Iterator i$ = this.mailboxList.iterator();

      while(i$.hasNext()) {
         Mailbox mailbox = (Mailbox)i$.next();
         results.add(mailbox);
      }

   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(this.name);
      sb.append(':');
      boolean first = true;
      Iterator i$ = this.mailboxList.iterator();

      while(i$.hasNext()) {
         Mailbox mailbox = (Mailbox)i$.next();
         if (first) {
            first = false;
         } else {
            sb.append(',');
         }

         sb.append(' ');
         sb.append(mailbox);
      }

      sb.append(";");
      return sb.toString();
   }
}
