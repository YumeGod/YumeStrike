package org.apache.james.mime4j.message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.field.DefaultFieldParser;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.BodyDescriptorBuilder;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.util.MimeUtil;

public class DefaultBodyDescriptorBuilder implements BodyDescriptorBuilder {
   private static final String CONTENT_TYPE;
   private static final String US_ASCII = "us-ascii";
   private static final String SUB_TYPE_EMAIL = "rfc822";
   private static final String MEDIA_TYPE_TEXT = "text";
   private static final String MEDIA_TYPE_MESSAGE = "message";
   private static final String EMAIL_MESSAGE_MIME_TYPE = "message/rfc822";
   private static final String DEFAULT_SUB_TYPE = "plain";
   private static final String DEFAULT_MEDIA_TYPE = "text";
   private static final String DEFAULT_MIME_TYPE = "text/plain";
   private final String parentMimeType;
   private final DecodeMonitor monitor;
   private final FieldParser fieldParser;
   private final Map fields;

   public DefaultBodyDescriptorBuilder() {
      this((String)null);
   }

   public DefaultBodyDescriptorBuilder(String parentMimeType) {
      this(parentMimeType, (FieldParser)null, (DecodeMonitor)null);
   }

   public DefaultBodyDescriptorBuilder(String parentMimeType, FieldParser fieldParser, DecodeMonitor monitor) {
      this.parentMimeType = parentMimeType;
      this.fieldParser = (FieldParser)(fieldParser != null ? fieldParser : DefaultFieldParser.getParser());
      this.monitor = monitor != null ? monitor : DecodeMonitor.SILENT;
      this.fields = new HashMap();
   }

   public void reset() {
      this.fields.clear();
   }

   public Field addField(RawField rawfield) throws MimeException {
      ParsedField field = this.fieldParser.parse(rawfield, this.monitor);
      String name = field.getName().toLowerCase(Locale.US);
      if (!this.fields.containsKey(name)) {
         this.fields.put(name, field);
      }

      return field;
   }

   public BodyDescriptor build() {
      String actualMimeType = null;
      String actualMediaType = null;
      String actualSubType = null;
      String actualCharset = null;
      String actualBoundary = null;
      ContentTypeField contentTypeField = (ContentTypeField)this.fields.get(CONTENT_TYPE);
      if (contentTypeField != null) {
         actualMimeType = contentTypeField.getMimeType();
         actualMediaType = contentTypeField.getMediaType();
         actualSubType = contentTypeField.getSubType();
         actualCharset = contentTypeField.getCharset();
         actualBoundary = contentTypeField.getBoundary();
      }

      if (actualMimeType == null) {
         if (MimeUtil.isSameMimeType("multipart/digest", this.parentMimeType)) {
            actualMimeType = "message/rfc822";
            actualMediaType = "message";
            actualSubType = "rfc822";
         } else {
            actualMimeType = "text/plain";
            actualMediaType = "text";
            actualSubType = "plain";
         }
      }

      if (actualCharset == null && "text".equals(actualMediaType)) {
         actualCharset = "us-ascii";
      }

      if (!MimeUtil.isMultipart(actualMimeType)) {
         actualBoundary = null;
      }

      return new MaximalBodyDescriptor(actualMimeType, actualMediaType, actualSubType, actualBoundary, actualCharset, this.fields);
   }

   public BodyDescriptorBuilder newChild() {
      ContentTypeField contentTypeField = (ContentTypeField)this.fields.get(CONTENT_TYPE);
      String actualMimeType;
      if (contentTypeField != null) {
         actualMimeType = contentTypeField.getMimeType();
      } else if (MimeUtil.isSameMimeType("multipart/digest", this.parentMimeType)) {
         actualMimeType = "message/rfc822";
      } else {
         actualMimeType = "text/plain";
      }

      return new DefaultBodyDescriptorBuilder(actualMimeType, this.fieldParser, this.monitor);
   }

   static {
      CONTENT_TYPE = "Content-Type".toLowerCase(Locale.US);
   }
}
