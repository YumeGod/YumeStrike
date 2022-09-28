package org.apache.james.mime4j.message;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.dom.field.AddressListField;
import org.apache.james.mime4j.dom.field.ContentDispositionField;
import org.apache.james.mime4j.dom.field.ContentTransferEncodingField;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.dom.field.DateTimeField;
import org.apache.james.mime4j.dom.field.MailboxField;
import org.apache.james.mime4j.dom.field.MailboxListField;
import org.apache.james.mime4j.dom.field.UnstructuredField;
import org.apache.james.mime4j.field.ContentTransferEncodingFieldImpl;
import org.apache.james.mime4j.field.ContentTypeFieldImpl;
import org.apache.james.mime4j.field.Fields;
import org.apache.james.mime4j.field.MimeVersionFieldLenientImpl;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.util.MimeUtil;

public class MessageImpl extends AbstractMessage {
   public MessageImpl() {
      Header header = this.obtainHeader();
      RawField rawField = new RawField("MIME-Version", "1.0");
      header.addField(MimeVersionFieldLenientImpl.PARSER.parse(rawField, DecodeMonitor.SILENT));
   }

   protected String newUniqueBoundary() {
      return MimeUtil.createUniqueBoundary();
   }

   protected UnstructuredField newMessageId(String hostname) {
      return Fields.messageId(hostname);
   }

   protected DateTimeField newDate(Date date, TimeZone zone) {
      return Fields.date("Date", date, zone);
   }

   protected MailboxField newMailbox(String fieldName, Mailbox mailbox) {
      return Fields.mailbox(fieldName, mailbox);
   }

   protected MailboxListField newMailboxList(String fieldName, Collection mailboxes) {
      return Fields.mailboxList(fieldName, mailboxes);
   }

   protected AddressListField newAddressList(String fieldName, Collection addresses) {
      return Fields.addressList(fieldName, addresses);
   }

   protected UnstructuredField newSubject(String subject) {
      return Fields.subject(subject);
   }

   protected ContentDispositionField newContentDisposition(String dispositionType, String filename, long size, Date creationDate, Date modificationDate, Date readDate) {
      return Fields.contentDisposition(dispositionType, filename, size, creationDate, modificationDate, readDate);
   }

   protected ContentDispositionField newContentDisposition(String dispositionType, Map parameters) {
      return Fields.contentDisposition(dispositionType, parameters);
   }

   protected ContentTypeField newContentType(String mimeType, Map parameters) {
      return Fields.contentType(mimeType, parameters);
   }

   protected ContentTransferEncodingField newContentTransferEncoding(String contentTransferEncoding) {
      return Fields.contentTransferEncoding(contentTransferEncoding);
   }

   protected String calcTransferEncoding(ContentTransferEncodingField f) {
      return ContentTransferEncodingFieldImpl.getEncoding(f);
   }

   protected String calcMimeType(ContentTypeField child, ContentTypeField parent) {
      return ContentTypeFieldImpl.getMimeType(child, parent);
   }

   protected String calcCharset(ContentTypeField contentType) {
      return ContentTypeFieldImpl.getCharset(contentType);
   }
}
