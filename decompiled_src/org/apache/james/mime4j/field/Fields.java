package org.apache.james.mime4j.field;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.codec.EncoderUtil;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.dom.field.AddressListField;
import org.apache.james.mime4j.dom.field.ContentDispositionField;
import org.apache.james.mime4j.dom.field.ContentTransferEncodingField;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.dom.field.DateTimeField;
import org.apache.james.mime4j.dom.field.MailboxField;
import org.apache.james.mime4j.dom.field.MailboxListField;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.dom.field.UnstructuredField;
import org.apache.james.mime4j.field.address.AddressFormatter;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.util.MimeUtil;

public class Fields {
   private static final Pattern FIELD_NAME_PATTERN = Pattern.compile("[\\x21-\\x39\\x3b-\\x7e]+");

   private Fields() {
   }

   public static ContentTypeField contentType(String contentType) {
      return (ContentTypeField)parse(ContentTypeFieldImpl.PARSER, "Content-Type", contentType);
   }

   public static ContentTypeField contentType(String mimeType, Map parameters) {
      if (!isValidMimeType(mimeType)) {
         throw new IllegalArgumentException();
      } else if (parameters != null && !parameters.isEmpty()) {
         StringBuilder sb = new StringBuilder(mimeType);
         Iterator i$ = parameters.entrySet().iterator();

         while(i$.hasNext()) {
            Map.Entry entry = (Map.Entry)i$.next();
            sb.append("; ");
            sb.append(EncoderUtil.encodeHeaderParameter((String)entry.getKey(), (String)entry.getValue()));
         }

         String contentType = sb.toString();
         return contentType(contentType);
      } else {
         return (ContentTypeField)parse(ContentTypeFieldImpl.PARSER, "Content-Type", mimeType);
      }
   }

   public static ContentTransferEncodingField contentTransferEncoding(String contentTransferEncoding) {
      return (ContentTransferEncodingField)parse(ContentTransferEncodingFieldImpl.PARSER, "Content-Transfer-Encoding", contentTransferEncoding);
   }

   public static ContentDispositionField contentDisposition(String contentDisposition) {
      return (ContentDispositionField)parse(ContentDispositionFieldImpl.PARSER, "Content-Disposition", contentDisposition);
   }

   public static ContentDispositionField contentDisposition(String dispositionType, Map parameters) {
      if (!isValidDispositionType(dispositionType)) {
         throw new IllegalArgumentException();
      } else if (parameters != null && !parameters.isEmpty()) {
         StringBuilder sb = new StringBuilder(dispositionType);
         Iterator i$ = parameters.entrySet().iterator();

         while(i$.hasNext()) {
            Map.Entry entry = (Map.Entry)i$.next();
            sb.append("; ");
            sb.append(EncoderUtil.encodeHeaderParameter((String)entry.getKey(), (String)entry.getValue()));
         }

         String contentDisposition = sb.toString();
         return contentDisposition(contentDisposition);
      } else {
         return (ContentDispositionField)parse(ContentDispositionFieldImpl.PARSER, "Content-Disposition", dispositionType);
      }
   }

   public static ContentDispositionField contentDisposition(String dispositionType, String filename) {
      return contentDisposition(dispositionType, filename, -1L, (Date)null, (Date)null, (Date)null);
   }

   public static ContentDispositionField contentDisposition(String dispositionType, String filename, long size) {
      return contentDisposition(dispositionType, filename, size, (Date)null, (Date)null, (Date)null);
   }

   public static ContentDispositionField contentDisposition(String dispositionType, String filename, long size, Date creationDate, Date modificationDate, Date readDate) {
      Map parameters = new HashMap();
      if (filename != null) {
         parameters.put("filename", filename);
      }

      if (size >= 0L) {
         parameters.put("size", Long.toString(size));
      }

      if (creationDate != null) {
         parameters.put("creation-date", MimeUtil.formatDate(creationDate, (TimeZone)null));
      }

      if (modificationDate != null) {
         parameters.put("modification-date", MimeUtil.formatDate(modificationDate, (TimeZone)null));
      }

      if (readDate != null) {
         parameters.put("read-date", MimeUtil.formatDate(readDate, (TimeZone)null));
      }

      return contentDisposition(dispositionType, (Map)parameters);
   }

   public static DateTimeField date(Date date) {
      return date0("Date", date, (TimeZone)null);
   }

   public static DateTimeField date(String fieldName, Date date) {
      checkValidFieldName(fieldName);
      return date0(fieldName, date, (TimeZone)null);
   }

   public static DateTimeField date(String fieldName, Date date, TimeZone zone) {
      checkValidFieldName(fieldName);
      return date0(fieldName, date, zone);
   }

   public static UnstructuredField messageId(String hostname) {
      String fieldValue = MimeUtil.createUniqueMessageId(hostname);
      return (UnstructuredField)parse(UnstructuredFieldImpl.PARSER, "Message-ID", fieldValue);
   }

   public static UnstructuredField subject(String subject) {
      int usedCharacters = "Subject".length() + 2;
      String fieldValue = EncoderUtil.encodeIfNecessary(subject, EncoderUtil.Usage.TEXT_TOKEN, usedCharacters);
      return (UnstructuredField)parse(UnstructuredFieldImpl.PARSER, "Subject", fieldValue);
   }

   public static MailboxField sender(Mailbox mailbox) {
      return mailbox0("Sender", mailbox);
   }

   public static MailboxListField from(Mailbox mailbox) {
      return mailboxList0("From", Collections.singleton(mailbox));
   }

   public static MailboxListField from(Mailbox... mailboxes) {
      return mailboxList0("From", Arrays.asList(mailboxes));
   }

   public static MailboxListField from(Iterable mailboxes) {
      return mailboxList0("From", mailboxes);
   }

   public static AddressListField to(Address address) {
      return addressList0("To", Collections.singleton(address));
   }

   public static AddressListField to(Address... addresses) {
      return addressList0("To", Arrays.asList(addresses));
   }

   public static AddressListField to(Iterable addresses) {
      return addressList0("To", addresses);
   }

   public static AddressListField cc(Address address) {
      return addressList0("Cc", Collections.singleton(address));
   }

   public static AddressListField cc(Address... addresses) {
      return addressList0("Cc", Arrays.asList(addresses));
   }

   public static AddressListField cc(Iterable addresses) {
      return addressList0("Cc", addresses);
   }

   public static AddressListField bcc(Address address) {
      return addressList0("Bcc", Collections.singleton(address));
   }

   public static AddressListField bcc(Address... addresses) {
      return addressList0("Bcc", Arrays.asList(addresses));
   }

   public static AddressListField bcc(Iterable addresses) {
      return addressList0("Bcc", addresses);
   }

   public static AddressListField replyTo(Address address) {
      return addressList0("Reply-To", Collections.singleton(address));
   }

   public static AddressListField replyTo(Address... addresses) {
      return addressList0("Reply-To", Arrays.asList(addresses));
   }

   public static AddressListField replyTo(Iterable addresses) {
      return addressList0("Reply-To", addresses);
   }

   public static MailboxField mailbox(String fieldName, Mailbox mailbox) {
      checkValidFieldName(fieldName);
      return mailbox0(fieldName, mailbox);
   }

   public static MailboxListField mailboxList(String fieldName, Iterable mailboxes) {
      checkValidFieldName(fieldName);
      return mailboxList0(fieldName, mailboxes);
   }

   public static AddressListField addressList(String fieldName, Iterable addresses) {
      checkValidFieldName(fieldName);
      return addressList0(fieldName, addresses);
   }

   private static DateTimeField date0(String fieldName, Date date, TimeZone zone) {
      String formattedDate = MimeUtil.formatDate(date, zone);
      return (DateTimeField)parse(DateTimeFieldImpl.PARSER, fieldName, formattedDate);
   }

   private static MailboxField mailbox0(String fieldName, Mailbox mailbox) {
      String fieldValue = encodeAddresses(Collections.singleton(mailbox));
      return (MailboxField)parse(MailboxFieldImpl.PARSER, fieldName, fieldValue);
   }

   private static MailboxListField mailboxList0(String fieldName, Iterable mailboxes) {
      String fieldValue = encodeAddresses(mailboxes);
      return (MailboxListField)parse(MailboxListFieldImpl.PARSER, fieldName, fieldValue);
   }

   private static AddressListField addressList0(String fieldName, Iterable addresses) {
      String fieldValue = encodeAddresses(addresses);
      return (AddressListField)parse(AddressListFieldImpl.PARSER, fieldName, fieldValue);
   }

   private static void checkValidFieldName(String fieldName) {
      if (!FIELD_NAME_PATTERN.matcher(fieldName).matches()) {
         throw new IllegalArgumentException("Invalid field name");
      }
   }

   private static boolean isValidMimeType(String mimeType) {
      if (mimeType == null) {
         return false;
      } else {
         int idx = mimeType.indexOf(47);
         if (idx == -1) {
            return false;
         } else {
            String type = mimeType.substring(0, idx);
            String subType = mimeType.substring(idx + 1);
            return EncoderUtil.isToken(type) && EncoderUtil.isToken(subType);
         }
      }
   }

   private static boolean isValidDispositionType(String dispositionType) {
      return dispositionType == null ? false : EncoderUtil.isToken(dispositionType);
   }

   private static ParsedField parse(FieldParser parser, String fieldName, String fieldBody) {
      RawField rawField = new RawField(fieldName, fieldBody);
      return parser.parse(rawField, DecodeMonitor.SILENT);
   }

   private static String encodeAddresses(Iterable addresses) {
      StringBuilder sb = new StringBuilder();

      Address address;
      for(Iterator i$ = addresses.iterator(); i$.hasNext(); AddressFormatter.DEFAULT.encode(sb, address)) {
         address = (Address)i$.next();
         if (sb.length() > 0) {
            sb.append(", ");
         }
      }

      return sb.toString();
   }
}
