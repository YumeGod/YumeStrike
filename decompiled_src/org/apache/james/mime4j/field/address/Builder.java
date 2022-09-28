package org.apache.james.mime4j.field.address;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.codec.DecoderUtil;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.address.DomainList;
import org.apache.james.mime4j.dom.address.Group;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.dom.address.MailboxList;

class Builder {
   private static Builder singleton = new Builder();

   public static Builder getInstance() {
      return singleton;
   }

   public AddressList buildAddressList(ASTaddress_list node, DecodeMonitor monitor) throws ParseException {
      List list = new ArrayList();

      for(int i = 0; i < node.jjtGetNumChildren(); ++i) {
         ASTaddress childNode = (ASTaddress)node.jjtGetChild(i);
         Address address = this.buildAddress(childNode, monitor);
         list.add(address);
      }

      return new AddressList(list, true);
   }

   public Address buildAddress(ASTaddress node, DecodeMonitor monitor) throws ParseException {
      ChildNodeIterator it = new ChildNodeIterator(node);
      Node n = it.next();
      if (n instanceof ASTaddr_spec) {
         return this.buildAddrSpec((ASTaddr_spec)n);
      } else if (n instanceof ASTangle_addr) {
         return this.buildAngleAddr((ASTangle_addr)n);
      } else if (n instanceof ASTphrase) {
         String name = this.buildString((ASTphrase)n, false);
         Node n2 = it.next();
         if (n2 instanceof ASTgroup_body) {
            return new Group(name, this.buildGroupBody((ASTgroup_body)n2, monitor));
         } else if (n2 instanceof ASTangle_addr) {
            try {
               name = DecoderUtil.decodeEncodedWords(name, monitor);
            } catch (IllegalArgumentException var8) {
               throw new ParseException(var8.getMessage());
            }

            Mailbox mb = this.buildAngleAddr((ASTangle_addr)n2);
            return new Mailbox(name, mb.getRoute(), mb.getLocalPart(), mb.getDomain());
         } else {
            throw new ParseException();
         }
      } else {
         throw new ParseException();
      }
   }

   private MailboxList buildGroupBody(ASTgroup_body node, DecodeMonitor monitor) throws ParseException {
      List results = new ArrayList();
      ChildNodeIterator it = new ChildNodeIterator(node);

      while(it.hasNext()) {
         Node n = it.next();
         if (!(n instanceof ASTmailbox)) {
            throw new ParseException();
         }

         results.add(this.buildMailbox((ASTmailbox)n, monitor));
      }

      return new MailboxList(results, true);
   }

   public Mailbox buildMailbox(ASTmailbox node, DecodeMonitor monitor) throws ParseException {
      ChildNodeIterator it = new ChildNodeIterator(node);
      Node n = it.next();
      if (n instanceof ASTaddr_spec) {
         return this.buildAddrSpec((ASTaddr_spec)n);
      } else if (n instanceof ASTangle_addr) {
         return this.buildAngleAddr((ASTangle_addr)n);
      } else if (n instanceof ASTname_addr) {
         return this.buildNameAddr((ASTname_addr)n, monitor);
      } else {
         throw new ParseException();
      }
   }

   private Mailbox buildNameAddr(ASTname_addr node, DecodeMonitor monitor) throws ParseException {
      ChildNodeIterator it = new ChildNodeIterator(node);
      Node n = it.next();
      if (n instanceof ASTphrase) {
         String name = this.buildString((ASTphrase)n, false);
         n = it.next();
         if (n instanceof ASTangle_addr) {
            try {
               name = DecoderUtil.decodeEncodedWords(name, monitor);
            } catch (IllegalArgumentException var7) {
               throw new ParseException(var7.getMessage());
            }

            Mailbox mb = this.buildAngleAddr((ASTangle_addr)n);
            return new Mailbox(name, mb.getRoute(), mb.getLocalPart(), mb.getDomain());
         } else {
            throw new ParseException();
         }
      } else {
         throw new ParseException();
      }
   }

   private Mailbox buildAngleAddr(ASTangle_addr node) throws ParseException {
      ChildNodeIterator it = new ChildNodeIterator(node);
      DomainList route = null;
      Node n = it.next();
      if (n instanceof ASTroute) {
         route = this.buildRoute((ASTroute)n);
         n = it.next();
      } else if (!(n instanceof ASTaddr_spec)) {
         throw new ParseException();
      }

      if (n instanceof ASTaddr_spec) {
         return this.buildAddrSpec(route, (ASTaddr_spec)n);
      } else {
         throw new ParseException();
      }
   }

   private DomainList buildRoute(ASTroute node) throws ParseException {
      List results = new ArrayList(node.jjtGetNumChildren());
      ChildNodeIterator it = new ChildNodeIterator(node);

      while(it.hasNext()) {
         Node n = it.next();
         if (!(n instanceof ASTdomain)) {
            throw new ParseException();
         }

         results.add(this.buildString((ASTdomain)n, true));
      }

      return new DomainList(results, true);
   }

   private Mailbox buildAddrSpec(ASTaddr_spec node) {
      return this.buildAddrSpec((DomainList)null, node);
   }

   private Mailbox buildAddrSpec(DomainList route, ASTaddr_spec node) {
      ChildNodeIterator it = new ChildNodeIterator(node);
      String localPart = this.buildString((ASTlocal_part)it.next(), true);
      String domain = this.buildString((ASTdomain)it.next(), true);
      return new Mailbox(route, localPart, domain);
   }

   private String buildString(SimpleNode node, boolean stripSpaces) {
      Token head = node.firstToken;
      Token tail = node.lastToken;
      StringBuilder out = new StringBuilder();

      while(head != tail) {
         out.append(head.image);
         head = head.next;
         if (!stripSpaces) {
            this.addSpecials(out, head.specialToken);
         }
      }

      out.append(tail.image);
      return out.toString();
   }

   private void addSpecials(StringBuilder out, Token specialToken) {
      if (specialToken != null) {
         this.addSpecials(out, specialToken.specialToken);
         out.append(specialToken.image);
      }

   }

   private static class ChildNodeIterator implements Iterator {
      private SimpleNode simpleNode;
      private int index;
      private int len;

      public ChildNodeIterator(SimpleNode simpleNode) {
         this.simpleNode = simpleNode;
         this.len = simpleNode.jjtGetNumChildren();
         this.index = 0;
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      public boolean hasNext() {
         return this.index < this.len;
      }

      public Node next() {
         return this.simpleNode.jjtGetChild(this.index++);
      }
   }
}
