package org.apache.james.mime4j.message;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.dom.field.ContentDispositionField;
import org.apache.james.mime4j.dom.field.ContentTransferEncodingField;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.dom.field.ParsedField;

public abstract class AbstractEntity implements Entity {
   private Header header = null;
   private Body body = null;
   private Entity parent = null;

   protected AbstractEntity() {
   }

   public Entity getParent() {
      return this.parent;
   }

   public void setParent(Entity parent) {
      this.parent = parent;
   }

   public Header getHeader() {
      return this.header;
   }

   public void setHeader(Header header) {
      this.header = header;
   }

   public Body getBody() {
      return this.body;
   }

   public void setBody(Body body) {
      if (this.body != null) {
         throw new IllegalStateException("body already set");
      } else {
         this.body = body;
         body.setParent(this);
      }
   }

   public Body removeBody() {
      if (this.body == null) {
         return null;
      } else {
         Body body = this.body;
         this.body = null;
         body.setParent((Entity)null);
         return body;
      }
   }

   public void setMessage(Message message) {
      this.setBody(message, "message/rfc822", (Map)null);
   }

   public void setMultipart(Multipart multipart) {
      String mimeType = "multipart/" + multipart.getSubType();
      Map parameters = Collections.singletonMap("boundary", this.newUniqueBoundary());
      this.setBody(multipart, mimeType, parameters);
   }

   public void setMultipart(Multipart multipart, Map parameters) {
      String mimeType = "multipart/" + multipart.getSubType();
      if (!((Map)parameters).containsKey("boundary")) {
         parameters = new HashMap((Map)parameters);
         ((Map)parameters).put("boundary", this.newUniqueBoundary());
      }

      this.setBody(multipart, mimeType, (Map)parameters);
   }

   public void setText(TextBody textBody) {
      this.setText(textBody, "plain");
   }

   public void setText(TextBody textBody, String subtype) {
      String mimeType = "text/" + subtype;
      Map parameters = null;
      String mimeCharset = textBody.getMimeCharset();
      if (mimeCharset != null && !mimeCharset.equalsIgnoreCase("us-ascii")) {
         parameters = Collections.singletonMap("charset", mimeCharset);
      }

      this.setBody(textBody, mimeType, parameters);
   }

   public void setBody(Body body, String mimeType) {
      this.setBody(body, mimeType, (Map)null);
   }

   public void setBody(Body body, String mimeType, Map parameters) {
      this.setBody(body);
      Header header = this.obtainHeader();
      header.setField(this.newContentType(mimeType, parameters));
   }

   public String getMimeType() {
      ContentTypeField child = this.getContentTypeField();
      ContentTypeField parent = this.getParent() != null ? (ContentTypeField)this.getParent().getHeader().getField("Content-Type") : null;
      return this.calcMimeType(child, parent);
   }

   private ContentTypeField getContentTypeField() {
      return (ContentTypeField)this.getHeader().getField("Content-Type");
   }

   public String getCharset() {
      return this.calcCharset((ContentTypeField)this.getHeader().getField("Content-Type"));
   }

   public String getContentTransferEncoding() {
      ContentTransferEncodingField f = (ContentTransferEncodingField)this.getHeader().getField("Content-Transfer-Encoding");
      return this.calcTransferEncoding(f);
   }

   public void setContentTransferEncoding(String contentTransferEncoding) {
      Header header = this.obtainHeader();
      header.setField(this.newContentTransferEncoding(contentTransferEncoding));
   }

   public String getDispositionType() {
      ContentDispositionField field = (ContentDispositionField)this.obtainField("Content-Disposition");
      return field == null ? null : field.getDispositionType();
   }

   public void setContentDisposition(String dispositionType) {
      Header header = this.obtainHeader();
      header.setField(this.newContentDisposition(dispositionType, (String)null, -1L, (Date)null, (Date)null, (Date)null));
   }

   public void setContentDisposition(String dispositionType, String filename) {
      Header header = this.obtainHeader();
      header.setField(this.newContentDisposition(dispositionType, filename, -1L, (Date)null, (Date)null, (Date)null));
   }

   public void setContentDisposition(String dispositionType, String filename, long size) {
      Header header = this.obtainHeader();
      header.setField(this.newContentDisposition(dispositionType, filename, size, (Date)null, (Date)null, (Date)null));
   }

   public void setContentDisposition(String dispositionType, String filename, long size, Date creationDate, Date modificationDate, Date readDate) {
      Header header = this.obtainHeader();
      header.setField(this.newContentDisposition(dispositionType, filename, size, creationDate, modificationDate, readDate));
   }

   public String getFilename() {
      ContentDispositionField field = (ContentDispositionField)this.obtainField("Content-Disposition");
      return field == null ? null : field.getFilename();
   }

   public void setFilename(String filename) {
      Header header = this.obtainHeader();
      ContentDispositionField field = (ContentDispositionField)header.getField("Content-Disposition");
      if (field == null) {
         if (filename != null) {
            header.setField(this.newContentDisposition("attachment", filename, -1L, (Date)null, (Date)null, (Date)null));
         }
      } else {
         String dispositionType = field.getDispositionType();
         Map parameters = new HashMap(field.getParameters());
         if (filename == null) {
            parameters.remove("filename");
         } else {
            parameters.put("filename", filename);
         }

         header.setField(this.newContentDisposition(dispositionType, parameters));
      }

   }

   public boolean isMimeType(String type) {
      return this.getMimeType().equalsIgnoreCase(type);
   }

   public boolean isMultipart() {
      ContentTypeField f = this.getContentTypeField();
      return f != null && f.getBoundary() != null && this.getMimeType().startsWith("multipart/");
   }

   public void dispose() {
      if (this.body != null) {
         this.body.dispose();
      }

   }

   Header obtainHeader() {
      if (this.header == null) {
         this.header = new HeaderImpl();
      }

      return this.header;
   }

   ParsedField obtainField(String fieldName) {
      Header header = this.getHeader();
      if (header == null) {
         return null;
      } else {
         ParsedField field = (ParsedField)header.getField(fieldName);
         return field;
      }
   }

   protected abstract String newUniqueBoundary();

   protected abstract ContentDispositionField newContentDisposition(String var1, String var2, long var3, Date var5, Date var6, Date var7);

   protected abstract ContentDispositionField newContentDisposition(String var1, Map var2);

   protected abstract ContentTypeField newContentType(String var1, Map var2);

   protected abstract ContentTransferEncodingField newContentTransferEncoding(String var1);

   protected abstract String calcMimeType(ContentTypeField var1, ContentTypeField var2);

   protected abstract String calcTransferEncoding(ContentTransferEncodingField var1);

   protected abstract String calcCharset(ContentTypeField var1);
}
