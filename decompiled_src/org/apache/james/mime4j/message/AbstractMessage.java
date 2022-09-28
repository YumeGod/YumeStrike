package org.apache.james.mime4j.message;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.dom.address.MailboxList;
import org.apache.james.mime4j.dom.field.AddressListField;
import org.apache.james.mime4j.dom.field.DateTimeField;
import org.apache.james.mime4j.dom.field.MailboxField;
import org.apache.james.mime4j.dom.field.MailboxListField;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.dom.field.UnstructuredField;
import org.apache.james.mime4j.stream.Field;

public abstract class AbstractMessage extends AbstractEntity implements Message {
   public String getMessageId() {
      Field field = this.obtainField("Message-ID");
      return field == null ? null : field.getBody();
   }

   public void createMessageId(String hostname) {
      Header header = this.obtainHeader();
      header.setField(this.newMessageId(hostname));
   }

   protected abstract ParsedField newMessageId(String var1);

   public String getSubject() {
      UnstructuredField field = (UnstructuredField)this.obtainField("Subject");
      return field == null ? null : field.getValue();
   }

   public void setSubject(String subject) {
      Header header = this.obtainHeader();
      if (subject == null) {
         header.removeFields("Subject");
      } else {
         header.setField(this.newSubject(subject));
      }

   }

   public Date getDate() {
      DateTimeField dateField = (DateTimeField)this.obtainField("Date");
      return dateField == null ? null : dateField.getDate();
   }

   public void setDate(Date date) {
      this.setDate(date, (TimeZone)null);
   }

   public void setDate(Date date, TimeZone zone) {
      Header header = this.obtainHeader();
      if (date == null) {
         header.removeFields("Date");
      } else {
         header.setField(this.newDate(date, zone));
      }

   }

   public Mailbox getSender() {
      return this.getMailbox("Sender");
   }

   public void setSender(Mailbox sender) {
      this.setMailbox("Sender", sender);
   }

   public MailboxList getFrom() {
      return this.getMailboxList("From");
   }

   public void setFrom(Mailbox from) {
      this.setMailboxList("From", from);
   }

   public void setFrom(Mailbox... from) {
      this.setMailboxList("From", from);
   }

   public void setFrom(Collection from) {
      this.setMailboxList("From", from);
   }

   public AddressList getTo() {
      return this.getAddressList("To");
   }

   public void setTo(Address to) {
      this.setAddressList("To", to);
   }

   public void setTo(Address... to) {
      this.setAddressList("To", to);
   }

   public void setTo(Collection to) {
      this.setAddressList("To", to);
   }

   public AddressList getCc() {
      return this.getAddressList("Cc");
   }

   public void setCc(Address cc) {
      this.setAddressList("Cc", cc);
   }

   public void setCc(Address... cc) {
      this.setAddressList("Cc", cc);
   }

   public void setCc(Collection cc) {
      this.setAddressList("Cc", cc);
   }

   public AddressList getBcc() {
      return this.getAddressList("Bcc");
   }

   public void setBcc(Address bcc) {
      this.setAddressList("Bcc", bcc);
   }

   public void setBcc(Address... bcc) {
      this.setAddressList("Bcc", bcc);
   }

   public void setBcc(Collection bcc) {
      this.setAddressList("Bcc", bcc);
   }

   public AddressList getReplyTo() {
      return this.getAddressList("Reply-To");
   }

   public void setReplyTo(Address replyTo) {
      this.setAddressList("Reply-To", replyTo);
   }

   public void setReplyTo(Address... replyTo) {
      this.setAddressList("Reply-To", replyTo);
   }

   public void setReplyTo(Collection replyTo) {
      this.setAddressList("Reply-To", replyTo);
   }

   private Mailbox getMailbox(String fieldName) {
      MailboxField field = (MailboxField)this.obtainField(fieldName);
      return field == null ? null : field.getMailbox();
   }

   private void setMailbox(String fieldName, Mailbox mailbox) {
      Header header = this.obtainHeader();
      if (mailbox == null) {
         header.removeFields(fieldName);
      } else {
         header.setField(this.newMailbox(fieldName, mailbox));
      }

   }

   private MailboxList getMailboxList(String fieldName) {
      MailboxListField field = (MailboxListField)this.obtainField(fieldName);
      return field == null ? null : field.getMailboxList();
   }

   private void setMailboxList(String fieldName, Mailbox mailbox) {
      this.setMailboxList(fieldName, (Collection)(mailbox == null ? null : Collections.singleton(mailbox)));
   }

   private void setMailboxList(String fieldName, Mailbox... mailboxes) {
      this.setMailboxList(fieldName, (Collection)(mailboxes == null ? null : Arrays.asList(mailboxes)));
   }

   private void setMailboxList(String fieldName, Collection mailboxes) {
      Header header = this.obtainHeader();
      if (mailboxes != null && !mailboxes.isEmpty()) {
         header.setField(this.newMailboxList(fieldName, mailboxes));
      } else {
         header.removeFields(fieldName);
      }

   }

   private AddressList getAddressList(String fieldName) {
      AddressListField field = (AddressListField)this.obtainField(fieldName);
      return field == null ? null : field.getAddressList();
   }

   private void setAddressList(String fieldName, Address address) {
      this.setAddressList(fieldName, (Collection)(address == null ? null : Collections.singleton(address)));
   }

   private void setAddressList(String fieldName, Address... addresses) {
      this.setAddressList(fieldName, (Collection)(addresses == null ? null : Arrays.asList(addresses)));
   }

   private void setAddressList(String fieldName, Collection addresses) {
      Header header = this.obtainHeader();
      if (addresses != null && !addresses.isEmpty()) {
         header.setField(this.newAddressList(fieldName, addresses));
      } else {
         header.removeFields(fieldName);
      }

   }

   protected abstract AddressListField newAddressList(String var1, Collection var2);

   protected abstract UnstructuredField newSubject(String var1);

   protected abstract DateTimeField newDate(Date var1, TimeZone var2);

   protected abstract MailboxField newMailbox(String var1, Mailbox var2);

   protected abstract MailboxListField newMailboxList(String var1, Collection var2);
}
