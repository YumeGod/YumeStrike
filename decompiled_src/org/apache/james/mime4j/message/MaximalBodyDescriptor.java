package org.apache.james.mime4j.message;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.james.mime4j.dom.field.ContentDescriptionField;
import org.apache.james.mime4j.dom.field.ContentDispositionField;
import org.apache.james.mime4j.dom.field.ContentIdField;
import org.apache.james.mime4j.dom.field.ContentLanguageField;
import org.apache.james.mime4j.dom.field.ContentLengthField;
import org.apache.james.mime4j.dom.field.ContentLocationField;
import org.apache.james.mime4j.dom.field.ContentMD5Field;
import org.apache.james.mime4j.dom.field.ContentTransferEncodingField;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.dom.field.MimeVersionField;
import org.apache.james.mime4j.stream.BodyDescriptor;

public class MaximalBodyDescriptor implements BodyDescriptor {
   private static final String CONTENT_TYPE;
   private static final String CONTENT_LENGTH;
   private static final String CONTENT_TRANSFER_ENCODING;
   private static final String CONTENT_DISPOSITION;
   private static final String CONTENT_ID;
   private static final String CONTENT_MD5;
   private static final String CONTENT_DESCRIPTION;
   private static final String CONTENT_LANGUAGE;
   private static final String CONTENT_LOCATION;
   private static final String MIME_VERSION;
   private final String mediaType;
   private final String subType;
   private final String mimeType;
   private final String boundary;
   private final String charset;
   private final Map fields;

   MaximalBodyDescriptor(String mimeType, String mediaType, String subType, String boundary, String charset, Map fields) {
      this.mimeType = mimeType;
      this.mediaType = mediaType;
      this.subType = subType;
      this.boundary = boundary;
      this.charset = charset;
      this.fields = (Map)(fields != null ? new HashMap(fields) : Collections.emptyMap());
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public String getBoundary() {
      return this.boundary;
   }

   public String getCharset() {
      return this.charset;
   }

   public String getMediaType() {
      return this.mediaType;
   }

   public String getSubType() {
      return this.subType;
   }

   public Map getContentTypeParameters() {
      ContentTypeField contentTypeField = (ContentTypeField)this.fields.get(CONTENT_TYPE);
      return contentTypeField != null ? contentTypeField.getParameters() : Collections.emptyMap();
   }

   public String getTransferEncoding() {
      ContentTransferEncodingField contentTransferEncodingField = (ContentTransferEncodingField)this.fields.get(CONTENT_TRANSFER_ENCODING);
      return contentTransferEncodingField != null ? contentTransferEncodingField.getEncoding() : "7bit";
   }

   public long getContentLength() {
      ContentLengthField contentLengthField = (ContentLengthField)this.fields.get(CONTENT_LENGTH);
      return contentLengthField != null ? contentLengthField.getContentLength() : -1L;
   }

   public int getMimeMajorVersion() {
      MimeVersionField mimeVersionField = (MimeVersionField)this.fields.get(MIME_VERSION);
      return mimeVersionField != null ? mimeVersionField.getMajorVersion() : 1;
   }

   public int getMimeMinorVersion() {
      MimeVersionField mimeVersionField = (MimeVersionField)this.fields.get(MIME_VERSION);
      return mimeVersionField != null ? mimeVersionField.getMinorVersion() : 0;
   }

   public String getContentDescription() {
      ContentDescriptionField contentDescriptionField = (ContentDescriptionField)this.fields.get(CONTENT_DESCRIPTION);
      return contentDescriptionField != null ? contentDescriptionField.getDescription() : null;
   }

   public String getContentId() {
      ContentIdField contentIdField = (ContentIdField)this.fields.get(CONTENT_ID);
      return contentIdField != null ? contentIdField.getId() : null;
   }

   public String getContentDispositionType() {
      ContentDispositionField contentDispositionField = (ContentDispositionField)this.fields.get(CONTENT_DISPOSITION);
      return contentDispositionField != null ? contentDispositionField.getDispositionType() : null;
   }

   public Map getContentDispositionParameters() {
      ContentDispositionField contentDispositionField = (ContentDispositionField)this.fields.get(CONTENT_DISPOSITION);
      return contentDispositionField != null ? contentDispositionField.getParameters() : Collections.emptyMap();
   }

   public String getContentDispositionFilename() {
      ContentDispositionField contentDispositionField = (ContentDispositionField)this.fields.get(CONTENT_DISPOSITION);
      return contentDispositionField != null ? contentDispositionField.getFilename() : null;
   }

   public Date getContentDispositionModificationDate() {
      ContentDispositionField contentDispositionField = (ContentDispositionField)this.fields.get(CONTENT_DISPOSITION);
      return contentDispositionField != null ? contentDispositionField.getModificationDate() : null;
   }

   public Date getContentDispositionCreationDate() {
      ContentDispositionField contentDispositionField = (ContentDispositionField)this.fields.get(CONTENT_DISPOSITION);
      return contentDispositionField != null ? contentDispositionField.getCreationDate() : null;
   }

   public Date getContentDispositionReadDate() {
      ContentDispositionField contentDispositionField = (ContentDispositionField)this.fields.get(CONTENT_DISPOSITION);
      return contentDispositionField != null ? contentDispositionField.getReadDate() : null;
   }

   public long getContentDispositionSize() {
      ContentDispositionField contentDispositionField = (ContentDispositionField)this.fields.get(CONTENT_DISPOSITION);
      return contentDispositionField != null ? contentDispositionField.getSize() : -1L;
   }

   public List getContentLanguage() {
      ContentLanguageField contentLanguageField = (ContentLanguageField)this.fields.get(CONTENT_LANGUAGE);
      return contentLanguageField != null ? contentLanguageField.getLanguages() : Collections.emptyList();
   }

   public String getContentLocation() {
      ContentLocationField contentLocationField = (ContentLocationField)this.fields.get(CONTENT_LOCATION);
      return contentLocationField != null ? contentLocationField.getLocation() : null;
   }

   public String getContentMD5Raw() {
      ContentMD5Field contentMD5Field = (ContentMD5Field)this.fields.get(CONTENT_MD5);
      return contentMD5Field != null ? contentMD5Field.getMD5Raw() : null;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("[mimeType=");
      sb.append(this.mimeType);
      sb.append(", mediaType=");
      sb.append(this.mediaType);
      sb.append(", subType=");
      sb.append(this.subType);
      sb.append(", boundary=");
      sb.append(this.boundary);
      sb.append(", charset=");
      sb.append(this.charset);
      sb.append("]");
      return sb.toString();
   }

   static {
      CONTENT_TYPE = "Content-Type".toLowerCase(Locale.US);
      CONTENT_LENGTH = "Content-Length".toLowerCase(Locale.US);
      CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding".toLowerCase(Locale.US);
      CONTENT_DISPOSITION = "Content-Disposition".toLowerCase(Locale.US);
      CONTENT_ID = "Content-ID".toLowerCase(Locale.US);
      CONTENT_MD5 = "Content-MD5".toLowerCase(Locale.US);
      CONTENT_DESCRIPTION = "Content-Description".toLowerCase(Locale.US);
      CONTENT_LANGUAGE = "Content-Language".toLowerCase(Locale.US);
      CONTENT_LOCATION = "Content-Location".toLowerCase(Locale.US);
      MIME_VERSION = "MIME-Version".toLowerCase(Locale.US);
   }
}
