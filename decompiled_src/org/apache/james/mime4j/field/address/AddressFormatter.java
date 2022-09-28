package org.apache.james.mime4j.field.address;

import java.util.Iterator;
import org.apache.james.mime4j.codec.EncoderUtil;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.Group;
import org.apache.james.mime4j.dom.address.Mailbox;

public class AddressFormatter {
   public static final AddressFormatter DEFAULT = new AddressFormatter();

   protected AddressFormatter() {
   }

   public void format(StringBuilder sb, Address address, boolean includeRoute) {
      if (address != null) {
         if (address instanceof Mailbox) {
            this.format(sb, (Mailbox)address, includeRoute);
         } else {
            if (!(address instanceof Group)) {
               throw new IllegalArgumentException("Unsuppported Address class: " + address.getClass());
            }

            this.format(sb, (Group)address, includeRoute);
         }

      }
   }

   public void encode(StringBuilder sb, Address address) {
      if (address != null) {
         if (address instanceof Mailbox) {
            this.encode(sb, (Mailbox)address);
         } else {
            if (!(address instanceof Group)) {
               throw new IllegalArgumentException("Unsuppported Address class: " + address.getClass());
            }

            this.encode(sb, (Group)address);
         }

      }
   }

   public void format(StringBuilder sb, Mailbox mailbox, boolean includeRoute) {
      if (sb == null) {
         throw new IllegalArgumentException("StringBuilder may not be null");
      } else if (mailbox == null) {
         throw new IllegalArgumentException("Mailbox may not be null");
      } else {
         includeRoute &= mailbox.getRoute() != null;
         boolean includeAngleBrackets = mailbox.getName() != null || includeRoute;
         if (mailbox.getName() != null) {
            sb.append(mailbox.getName());
            sb.append(' ');
         }

         if (includeAngleBrackets) {
            sb.append('<');
         }

         if (includeRoute) {
            sb.append(mailbox.getRoute().toRouteString());
            sb.append(':');
         }

         sb.append(mailbox.getLocalPart());
         if (mailbox.getDomain() != null) {
            sb.append('@');
            sb.append(mailbox.getDomain());
         }

         if (includeAngleBrackets) {
            sb.append('>');
         }

      }
   }

   public String format(Mailbox mailbox, boolean includeRoute) {
      StringBuilder sb = new StringBuilder();
      this.format(sb, mailbox, includeRoute);
      return sb.toString();
   }

   public void encode(StringBuilder sb, Mailbox mailbox) {
      if (sb == null) {
         throw new IllegalArgumentException("StringBuilder may not be null");
      } else if (mailbox == null) {
         throw new IllegalArgumentException("Mailbox may not be null");
      } else {
         if (mailbox.getName() != null) {
            sb.append(EncoderUtil.encodeAddressDisplayName(mailbox.getName()));
            sb.append(" <");
         }

         sb.append(EncoderUtil.encodeAddressLocalPart(mailbox.getLocalPart()));
         if (mailbox.getDomain() != null) {
            sb.append('@');
            sb.append(mailbox.getDomain());
         }

         if (mailbox.getName() != null) {
            sb.append('>');
         }

      }
   }

   public String encode(Mailbox mailbox) {
      StringBuilder sb = new StringBuilder();
      this.encode(sb, mailbox);
      return sb.toString();
   }

   public void format(StringBuilder sb, Group group, boolean includeRoute) {
      if (sb == null) {
         throw new IllegalArgumentException("StringBuilder may not be null");
      } else if (group == null) {
         throw new IllegalArgumentException("Group may not be null");
      } else {
         sb.append(group.getName());
         sb.append(':');
         boolean first = true;
         Iterator i$ = group.getMailboxes().iterator();

         while(i$.hasNext()) {
            Mailbox mailbox = (Mailbox)i$.next();
            if (first) {
               first = false;
            } else {
               sb.append(',');
            }

            sb.append(' ');
            this.format(sb, mailbox, includeRoute);
         }

         sb.append(";");
      }
   }

   public String format(Group group, boolean includeRoute) {
      StringBuilder sb = new StringBuilder();
      this.format(sb, group, includeRoute);
      return sb.toString();
   }

   public void encode(StringBuilder sb, Group group) {
      if (sb == null) {
         throw new IllegalArgumentException("StringBuilder may not be null");
      } else if (group == null) {
         throw new IllegalArgumentException("Group may not be null");
      } else {
         sb.append(EncoderUtil.encodeAddressDisplayName(group.getName()));
         sb.append(':');
         boolean first = true;
         Iterator i$ = group.getMailboxes().iterator();

         while(i$.hasNext()) {
            Mailbox mailbox = (Mailbox)i$.next();
            if (first) {
               first = false;
            } else {
               sb.append(',');
            }

            sb.append(' ');
            this.encode(sb, mailbox);
         }

         sb.append(';');
      }
   }

   public String encode(Group group) {
      StringBuilder sb = new StringBuilder();
      this.encode(sb, group);
      return sb.toString();
   }
}
