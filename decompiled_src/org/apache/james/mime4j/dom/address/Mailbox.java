package org.apache.james.mime4j.dom.address;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.james.mime4j.util.LangUtils;

public class Mailbox extends Address {
   private static final long serialVersionUID = 1L;
   private static final DomainList EMPTY_ROUTE_LIST = new DomainList(Collections.emptyList(), true);
   private final String name;
   private final DomainList route;
   private final String localPart;
   private final String domain;

   public Mailbox(String name, DomainList route, String localPart, String domain) {
      if (localPart == null) {
         throw new IllegalArgumentException();
      } else {
         this.name = name != null && name.length() != 0 ? name : null;
         this.route = route == null ? EMPTY_ROUTE_LIST : route;
         this.localPart = localPart;
         this.domain = domain != null && domain.length() != 0 ? domain : null;
      }
   }

   Mailbox(String name, Mailbox baseMailbox) {
      this(name, baseMailbox.getRoute(), baseMailbox.getLocalPart(), baseMailbox.getDomain());
   }

   public Mailbox(String localPart, String domain) {
      this((String)null, (DomainList)null, localPart, domain);
   }

   public Mailbox(DomainList route, String localPart, String domain) {
      this((String)null, route, localPart, domain);
   }

   public Mailbox(String name, String localPart, String domain) {
      this(name, (DomainList)null, localPart, domain);
   }

   public String getName() {
      return this.name;
   }

   public DomainList getRoute() {
      return this.route;
   }

   public String getLocalPart() {
      return this.localPart;
   }

   public String getDomain() {
      return this.domain;
   }

   public String getAddress() {
      return this.domain == null ? this.localPart : this.localPart + '@' + this.domain;
   }

   protected final void doAddMailboxesTo(List results) {
      results.add(this);
   }

   public int hashCode() {
      int hash = 17;
      hash = LangUtils.hashCode(hash, this.localPart);
      hash = LangUtils.hashCode(hash, this.domain != null ? this.domain.toLowerCase(Locale.US) : null);
      return hash;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Mailbox)) {
         return false;
      } else {
         Mailbox that = (Mailbox)obj;
         return LangUtils.equals(this.localPart, that.localPart) && LangUtils.equalsIgnoreCase(this.domain, that.domain);
      }
   }

   public String toString() {
      return this.getAddress();
   }
}
