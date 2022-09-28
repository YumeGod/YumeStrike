package org.apache.james.mime4j.field.address;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.address.DomainList;
import org.apache.james.mime4j.dom.address.Group;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.stream.ParserCursor;
import org.apache.james.mime4j.stream.RawFieldParser;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.CharsetUtil;
import org.apache.james.mime4j.util.ContentUtil;

public class LenientAddressBuilder {
   private static final int AT = 64;
   private static final int OPENING_BRACKET = 60;
   private static final int CLOSING_BRACKET = 62;
   private static final int COMMA = 44;
   private static final int COLON = 58;
   private static final int SEMICOLON = 59;
   private static final BitSet AT_AND_CLOSING_BRACKET = RawFieldParser.INIT_BITSET(64, 62);
   private static final BitSet CLOSING_BRACKET_ONLY = RawFieldParser.INIT_BITSET(62);
   private static final BitSet COMMA_AND_COLON = RawFieldParser.INIT_BITSET(44, 58);
   private static final BitSet COMMA_ONLY = RawFieldParser.INIT_BITSET(44);
   private static final BitSet COLON_ONLY = RawFieldParser.INIT_BITSET(58);
   private static final BitSet SEMICOLON_ONLY = RawFieldParser.INIT_BITSET(59);
   public static final LenientAddressBuilder DEFAULT = new LenientAddressBuilder();
   private final RawFieldParser parser = new RawFieldParser();

   protected LenientAddressBuilder() {
   }

   String parseDomain(ByteSequence buf, ParserCursor cursor, BitSet delimiters) {
      StringBuilder dst = new StringBuilder();

      while(!cursor.atEnd()) {
         char current = (char)(buf.byteAt(cursor.getPos()) & 255);
         if (delimiters != null && delimiters.get(current)) {
            break;
         }

         if (CharsetUtil.isWhitespace(current)) {
            this.parser.skipWhiteSpace(buf, cursor);
         } else if (current == '(') {
            this.parser.skipComment(buf, cursor);
         } else {
            this.parser.copyContent(buf, cursor, delimiters, dst);
         }
      }

      return dst.toString();
   }

   DomainList parseRoute(ByteSequence buf, ParserCursor cursor) {
      List domains = null;

      while(!cursor.atEnd()) {
         this.parser.skipAllWhiteSpace(buf, cursor);
         int pos = cursor.getPos();
         int current = (char)(buf.byteAt(pos) & 255);
         if (current != '@') {
            break;
         }

         cursor.updatePos(pos + 1);
         String s = this.parseDomain(buf, cursor, COMMA_AND_COLON);
         if (s != null && s.length() > 0) {
            if (domains == null) {
               domains = new ArrayList();
            }

            domains.add(s);
         }

         pos = cursor.getPos();
         current = (char)(buf.byteAt(pos) & 255);
         if (current != ',') {
            if (current == ':') {
               cursor.updatePos(pos + 1);
            }
            break;
         }

         cursor.updatePos(pos + 1);
      }

      return domains != null ? new DomainList(domains, true) : null;
   }

   Mailbox parseMailboxAddress(String openingText, ByteSequence buf, ParserCursor cursor) {
      if (cursor.atEnd()) {
         return new Mailbox((String)null, (DomainList)null, openingText, (String)null);
      } else {
         int pos = cursor.getPos();
         char current = (char)(buf.byteAt(pos) & 255);
         if (current != '<') {
            return new Mailbox((String)null, (DomainList)null, openingText, (String)null);
         } else {
            cursor.updatePos(pos + 1);
            DomainList domainList = this.parseRoute(buf, cursor);
            String localPart = this.parser.parseValue(buf, cursor, AT_AND_CLOSING_BRACKET);
            if (cursor.atEnd()) {
               return new Mailbox(openingText, domainList, localPart, (String)null);
            } else {
               pos = cursor.getPos();
               current = (char)(buf.byteAt(pos) & 255);
               if (current != '@') {
                  return new Mailbox(openingText, domainList, localPart, (String)null);
               } else {
                  cursor.updatePos(pos + 1);
                  String domain = this.parseDomain(buf, cursor, CLOSING_BRACKET_ONLY);
                  if (cursor.atEnd()) {
                     return new Mailbox(openingText, domainList, localPart, domain);
                  } else {
                     pos = cursor.getPos();
                     current = (char)(buf.byteAt(pos) & 255);
                     if (current != '>') {
                        return new Mailbox(openingText, domainList, localPart, domain);
                     } else {
                        cursor.updatePos(pos + 1);

                        while(!cursor.atEnd()) {
                           pos = cursor.getPos();
                           current = (char)(buf.byteAt(pos) & 255);
                           if (CharsetUtil.isWhitespace(current)) {
                              this.parser.skipWhiteSpace(buf, cursor);
                           } else {
                              if (current != '(') {
                                 break;
                              }

                              this.parser.skipComment(buf, cursor);
                           }
                        }

                        return new Mailbox(openingText, domainList, localPart, domain);
                     }
                  }
               }
            }
         }
      }
   }

   private Mailbox createMailbox(String localPart) {
      return localPart != null && localPart.length() > 0 ? new Mailbox((String)null, (DomainList)null, localPart, (String)null) : null;
   }

   public Mailbox parseMailbox(ByteSequence buf, ParserCursor cursor, BitSet delimiters) {
      BitSet bitset = RawFieldParser.INIT_BITSET(64, 60);
      if (delimiters != null) {
         bitset.or(delimiters);
      }

      String openingText = this.parser.parseValue(buf, cursor, bitset);
      if (cursor.atEnd()) {
         return this.createMailbox(openingText);
      } else {
         int pos = cursor.getPos();
         char current = (char)(buf.byteAt(pos) & 255);
         if (current == '<') {
            return this.parseMailboxAddress(openingText, buf, cursor);
         } else if (current == '@') {
            cursor.updatePos(pos + 1);
            String domain = this.parseDomain(buf, cursor, delimiters);
            return new Mailbox((String)null, (DomainList)null, openingText, domain);
         } else {
            return this.createMailbox(openingText);
         }
      }
   }

   public Mailbox parseMailbox(String text) {
      ByteSequence raw = ContentUtil.encode(text);
      ParserCursor cursor = new ParserCursor(0, text.length());
      return this.parseMailbox(raw, cursor, (BitSet)null);
   }

   List parseMailboxes(ByteSequence buf, ParserCursor cursor, BitSet delimiters) {
      BitSet bitset = RawFieldParser.INIT_BITSET(44);
      if (delimiters != null) {
         bitset.or(delimiters);
      }

      List mboxes = new ArrayList();

      while(!cursor.atEnd()) {
         int pos = cursor.getPos();
         int current = (char)(buf.byteAt(pos) & 255);
         if (delimiters != null && delimiters.get(current)) {
            break;
         }

         if (current == ',') {
            cursor.updatePos(pos + 1);
         } else {
            Mailbox mbox = this.parseMailbox(buf, cursor, bitset);
            if (mbox != null) {
               mboxes.add(mbox);
            }
         }
      }

      return mboxes;
   }

   public Group parseGroup(ByteSequence buf, ParserCursor cursor) {
      String name = this.parser.parseToken(buf, cursor, COLON_ONLY);
      if (cursor.atEnd()) {
         return new Group(name, Collections.emptyList());
      } else {
         int pos = cursor.getPos();
         int current = (char)(buf.byteAt(pos) & 255);
         if (current == ':') {
            cursor.updatePos(pos + 1);
         }

         List mboxes = this.parseMailboxes(buf, cursor, SEMICOLON_ONLY);
         return new Group(name, mboxes);
      }
   }

   public Group parseGroup(String text) {
      ByteSequence raw = ContentUtil.encode(text);
      ParserCursor cursor = new ParserCursor(0, text.length());
      return this.parseGroup(raw, cursor);
   }

   public Address parseAddress(ByteSequence buf, ParserCursor cursor, BitSet delimiters) {
      BitSet bitset = RawFieldParser.INIT_BITSET(58, 64, 60);
      if (delimiters != null) {
         bitset.or(delimiters);
      }

      String openingText = this.parser.parseValue(buf, cursor, bitset);
      if (cursor.atEnd()) {
         return this.createMailbox(openingText);
      } else {
         int pos = cursor.getPos();
         char current = (char)(buf.byteAt(pos) & 255);
         if (current == '<') {
            return this.parseMailboxAddress(openingText, buf, cursor);
         } else if (current == '@') {
            cursor.updatePos(pos + 1);
            String domain = this.parseDomain(buf, cursor, delimiters);
            return new Mailbox((String)null, (DomainList)null, openingText, domain);
         } else if (current == ':') {
            cursor.updatePos(pos + 1);
            List mboxes = this.parseMailboxes(buf, cursor, SEMICOLON_ONLY);
            if (!cursor.atEnd()) {
               pos = cursor.getPos();
               current = (char)(buf.byteAt(pos) & 255);
               if (current == ';') {
                  cursor.updatePos(pos + 1);
               }
            }

            return new Group(openingText, mboxes);
         } else {
            return this.createMailbox(openingText);
         }
      }
   }

   public Address parseAddress(String text) {
      ByteSequence raw = ContentUtil.encode(text);
      ParserCursor cursor = new ParserCursor(0, text.length());
      return this.parseAddress(raw, cursor, (BitSet)null);
   }

   public AddressList parseAddressList(ByteSequence buf, ParserCursor cursor) {
      List addresses = new ArrayList();

      while(!cursor.atEnd()) {
         int pos = cursor.getPos();
         int current = (char)(buf.byteAt(pos) & 255);
         if (current == ',') {
            cursor.updatePos(pos + 1);
         } else {
            Address address = this.parseAddress(buf, cursor, COMMA_ONLY);
            if (address != null) {
               addresses.add(address);
            }
         }
      }

      return new AddressList(addresses, false);
   }

   public AddressList parseAddressList(String text) {
      ByteSequence raw = ContentUtil.encode(text);
      ParserCursor cursor = new ParserCursor(0, text.length());
      return this.parseAddressList(raw, cursor);
   }
}
