package org.apache.james.mime4j.stream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.util.MimeUtil;

class FallbackBodyDescriptorBuilder implements BodyDescriptorBuilder {
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
   private String mediaType;
   private String subType;
   private String mimeType;
   private String boundary;
   private String charset;
   private String transferEncoding;
   private long contentLength;

   public FallbackBodyDescriptorBuilder() {
      this((String)null, (DecodeMonitor)null);
   }

   public FallbackBodyDescriptorBuilder(String parentMimeType, DecodeMonitor monitor) {
      this.parentMimeType = parentMimeType;
      this.monitor = monitor != null ? monitor : DecodeMonitor.SILENT;
      this.reset();
   }

   public void reset() {
      this.mimeType = null;
      this.subType = null;
      this.mediaType = null;
      this.boundary = null;
      this.charset = null;
      this.transferEncoding = null;
      this.contentLength = -1L;
   }

   public BodyDescriptorBuilder newChild() {
      return new FallbackBodyDescriptorBuilder(this.mimeType, this.monitor);
   }

   public BodyDescriptor build() {
      String actualMimeType = this.mimeType;
      String actualMediaType = this.mediaType;
      String actualSubType = this.subType;
      String actualCharset = this.charset;
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

      return new BasicBodyDescriptor(actualMimeType, actualMediaType, actualSubType, this.boundary, actualCharset, this.transferEncoding != null ? this.transferEncoding : "7bit", this.contentLength);
   }

   public Field addField(RawField field) throws MimeException {
      String name = field.getName().toLowerCase(Locale.US);
      String value;
      if (name.equals("content-transfer-encoding") && this.transferEncoding == null) {
         value = field.getBody();
         if (value != null) {
            value = value.trim().toLowerCase(Locale.US);
            if (value.length() > 0) {
               this.transferEncoding = value;
            }
         }
      } else if (name.equals("content-length") && this.contentLength == -1L) {
         value = field.getBody();
         if (value != null) {
            value = value.trim();

            try {
               this.contentLength = Long.parseLong(value.trim());
            } catch (NumberFormatException var5) {
               if (this.monitor.warn("Invalid content length: " + value, "ignoring Content-Length header")) {
                  throw new MimeException("Invalid Content-Length header: " + value);
               }
            }
         }
      } else if (name.equals("content-type") && this.mimeType == null) {
         this.parseContentType(field);
      }

      return null;
   }

   private void parseContentType(Field field) throws MimeException {
      RawField rawfield;
      if (field instanceof RawField) {
         rawfield = (RawField)field;
      } else {
         rawfield = new RawField(field.getName(), field.getBody());
      }

      RawBody body = RawFieldParser.DEFAULT.parseRawBody(rawfield);
      String main = body.getValue();
      Map params = new HashMap();
      Iterator i$ = body.getParams().iterator();

      String b;
      while(i$.hasNext()) {
         NameValuePair nmp = (NameValuePair)i$.next();
         b = nmp.getName().toLowerCase(Locale.US);
         params.put(b, nmp.getValue());
      }

      String type = null;
      String subtype = null;
      if (main != null) {
         main = main.toLowerCase().trim();
         int index = main.indexOf(47);
         boolean valid = false;
         if (index != -1) {
            type = main.substring(0, index).trim();
            subtype = main.substring(index + 1).trim();
            if (type.length() > 0 && subtype.length() > 0) {
               main = type + "/" + subtype;
               valid = true;
            }
         }

         if (!valid) {
            main = null;
            type = null;
            subtype = null;
         }
      }

      b = (String)params.get("boundary");
      if (main != null && (main.startsWith("multipart/") && b != null || !main.startsWith("multipart/"))) {
         this.mimeType = main;
         this.mediaType = type;
         this.subType = subtype;
      }

      if (MimeUtil.isMultipart(this.mimeType)) {
         this.boundary = b;
      }

      String c = (String)params.get("charset");
      this.charset = null;
      if (c != null) {
         c = c.trim();
         if (c.length() > 0) {
            this.charset = c;
         }
      }

   }
}
