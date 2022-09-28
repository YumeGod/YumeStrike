package org.apache.james.mime4j.dom;

import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.dom.address.MailboxList;

public interface Message extends Entity, Body {
   String getMessageId();

   void createMessageId(String var1);

   String getSubject();

   void setSubject(String var1);

   Date getDate();

   void setDate(Date var1);

   void setDate(Date var1, TimeZone var2);

   Mailbox getSender();

   void setSender(Mailbox var1);

   MailboxList getFrom();

   void setFrom(Mailbox var1);

   void setFrom(Mailbox... var1);

   void setFrom(Collection var1);

   AddressList getTo();

   void setTo(Address var1);

   void setTo(Address... var1);

   void setTo(Collection var1);

   AddressList getCc();

   void setCc(Address var1);

   void setCc(Address... var1);

   void setCc(Collection var1);

   AddressList getBcc();

   void setBcc(Address var1);

   void setBcc(Address... var1);

   void setBcc(Collection var1);

   AddressList getReplyTo();

   void setReplyTo(Address var1);

   void setReplyTo(Address... var1);

   void setReplyTo(Collection var1);
}
