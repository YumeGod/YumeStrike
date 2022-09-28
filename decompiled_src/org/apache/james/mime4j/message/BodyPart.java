package org.apache.james.mime4j.message;

import java.util.Date;
import java.util.Map;
import org.apache.james.mime4j.dom.field.ContentDispositionField;
import org.apache.james.mime4j.dom.field.ContentTransferEncodingField;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.field.ContentTransferEncodingFieldImpl;
import org.apache.james.mime4j.field.ContentTypeFieldImpl;
import org.apache.james.mime4j.field.Fields;
import org.apache.james.mime4j.util.MimeUtil;

public class BodyPart extends AbstractEntity {
   protected String newUniqueBoundary() {
      return MimeUtil.createUniqueBoundary();
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
