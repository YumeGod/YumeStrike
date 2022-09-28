package org.apache.fop.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class PDFInfo extends PDFObject {
   private String producer;
   private String title = null;
   private String author = null;
   private String subject = null;
   private String keywords = null;
   private Date creationDate = null;
   private Date modDate = null;
   private String creator;

   public String getProducer() {
      return this.producer;
   }

   public void setProducer(String producer) {
      this.producer = producer;
   }

   public String getCreator() {
      return this.creator;
   }

   public void setCreator(String creator) {
      this.creator = creator;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String t) {
      this.title = t;
   }

   public String getAuthor() {
      return this.author;
   }

   public void setAuthor(String a) {
      this.author = a;
   }

   public String getSubject() {
      return this.subject;
   }

   public void setSubject(String s) {
      this.subject = s;
   }

   public String getKeywords() {
      return this.keywords;
   }

   public void setKeywords(String k) {
      this.keywords = k;
   }

   public Date getCreationDate() {
      return this.creationDate;
   }

   public void setCreationDate(Date date) {
      this.creationDate = date;
   }

   public Date getModDate() {
      return this.modDate;
   }

   public void setModDate(Date date) {
      this.modDate = date;
   }

   public byte[] toPDF() {
      PDFProfile profile = this.getDocumentSafely().getProfile();
      ByteArrayOutputStream bout = new ByteArrayOutputStream(128);

      try {
         bout.write(encode(this.getObjectID()));
         bout.write(encode("<<\n"));
         if (this.title != null && this.title.length() > 0) {
            bout.write(encode("/Title "));
            bout.write(this.encodeText(this.title));
            bout.write(encode("\n"));
         } else {
            profile.verifyTitleAbsent();
         }

         if (this.author != null) {
            bout.write(encode("/Author "));
            bout.write(this.encodeText(this.author));
            bout.write(encode("\n"));
         }

         if (this.subject != null) {
            bout.write(encode("/Subject "));
            bout.write(this.encodeText(this.subject));
            bout.write(encode("\n"));
         }

         if (this.keywords != null) {
            bout.write(encode("/Keywords "));
            bout.write(this.encodeText(this.keywords));
            bout.write(encode("\n"));
         }

         if (this.creator != null) {
            bout.write(encode("/Creator "));
            bout.write(this.encodeText(this.creator));
            bout.write(encode("\n"));
         }

         bout.write(encode("/Producer "));
         bout.write(this.encodeText(this.producer));
         bout.write(encode("\n"));
         if (this.creationDate == null) {
            this.creationDate = new Date();
         }

         bout.write(encode("/CreationDate "));
         bout.write(this.encodeString(this.formatDateTime(this.creationDate)));
         bout.write(encode("\n"));
         if (profile.isModDateRequired() && this.modDate == null) {
            this.modDate = this.creationDate;
         }

         if (this.modDate != null) {
            bout.write(encode("/ModDate "));
            bout.write(this.encodeString(this.formatDateTime(this.modDate)));
            bout.write(encode("\n"));
         }

         if (profile.isPDFXActive()) {
            bout.write(encode("/GTS_PDFXVersion "));
            bout.write(this.encodeString(profile.getPDFXMode().getName()));
            bout.write(encode("\n"));
         }

         if (profile.isTrappedEntryRequired()) {
            bout.write(encode("/Trapped /False\n"));
         }

         bout.write(encode(">>\nendobj\n"));
      } catch (IOException var4) {
         log.error("Ignored I/O exception", var4);
      }

      return bout.toByteArray();
   }
}
