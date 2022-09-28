package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class PDFObject implements PDFWritable {
   protected static Log log;
   private int objnum;
   private int generation = 0;
   private PDFDocument document;
   private PDFObject parent;
   protected static final SimpleDateFormat DATE_FORMAT;

   public int getObjectNumber() {
      if (this.objnum == 0) {
         throw new IllegalStateException("Object has no number assigned: " + this.toString());
      } else {
         return this.objnum;
      }
   }

   public PDFObject() {
   }

   public PDFObject(PDFObject parent) {
      this.setParent(parent);
   }

   public boolean hasObjectNumber() {
      return this.objnum > 0;
   }

   public void setObjectNumber(int objnum) {
      this.objnum = objnum;
      PDFDocument doc = this.getDocument();
      this.setParent((PDFObject)null);
      this.setDocument(doc);
      if (log.isTraceEnabled()) {
         log.trace("Assigning " + this + " object number " + objnum);
      }

   }

   public int getGeneration() {
      return this.generation;
   }

   public final PDFDocument getDocument() {
      if (this.document != null) {
         return this.document;
      } else {
         return this.getParent() != null ? this.getParent().getDocument() : null;
      }
   }

   public final PDFDocument getDocumentSafely() {
      PDFDocument doc = this.getDocument();
      if (doc == null) {
         throw new IllegalStateException("Parent PDFDocument is unavailable on " + this.getClass().getName());
      } else {
         return doc;
      }
   }

   public void setDocument(PDFDocument doc) {
      this.document = doc;
   }

   public PDFObject getParent() {
      return this.parent;
   }

   public void setParent(PDFObject parent) {
      this.parent = parent;
   }

   public String getObjectID() {
      return this.getObjectNumber() + " " + this.getGeneration() + " obj\n";
   }

   public String referencePDF() {
      if (!this.hasObjectNumber()) {
         throw new IllegalArgumentException("Cannot reference this object. It doesn't have an object number");
      } else {
         String ref = this.getObjectNumber() + " " + this.getGeneration() + " R";
         return ref;
      }
   }

   public PDFReference makeReference() {
      return new PDFReference(this);
   }

   protected int output(OutputStream stream) throws IOException {
      byte[] pdf = this.toPDF();
      stream.write(pdf);
      return pdf.length;
   }

   public void outputInline(OutputStream out, Writer writer) throws IOException {
      if (this.hasObjectNumber()) {
         writer.write(this.referencePDF());
      } else {
         writer.flush();
         this.output(out);
      }

   }

   protected byte[] toPDF() {
      return encode(this.toPDFString());
   }

   protected String toPDFString() {
      throw new UnsupportedOperationException("Not implemented. Use output(OutputStream) instead.");
   }

   public static final byte[] encode(String text) {
      return PDFDocument.encode(text);
   }

   protected byte[] encodeText(String text) {
      if (this.getDocumentSafely().isEncryptionActive()) {
         byte[] buf = PDFText.toUTF16(text);
         return PDFText.escapeByteArray(this.getDocument().getEncryption().encrypt(buf, this));
      } else {
         return encode(PDFText.escapeText(text, false));
      }
   }

   protected byte[] encodeString(String string) {
      return this.encodeText(string);
   }

   protected void encodeBinaryToHexString(byte[] data, OutputStream out) throws IOException {
      out.write(60);
      if (this.getDocumentSafely().isEncryptionActive()) {
         data = this.getDocument().getEncryption().encrypt(data, this);
      }

      String hex = PDFText.toHex(data, false);
      byte[] encoded = hex.getBytes("US-ASCII");
      out.write(encoded);
      out.write(62);
   }

   protected void formatObject(Object obj, OutputStream out, Writer writer) throws IOException {
      if (obj == null) {
         writer.write("null");
      } else if (obj instanceof PDFWritable) {
         ((PDFWritable)obj).outputInline(out, writer);
      } else if (obj instanceof Number) {
         if (!(obj instanceof Double) && !(obj instanceof Float)) {
            writer.write(obj.toString());
         } else {
            writer.write(PDFNumber.doubleOut(((Number)obj).doubleValue()));
         }
      } else if (obj instanceof Boolean) {
         writer.write(obj.toString());
      } else if (obj instanceof byte[]) {
         writer.flush();
         this.encodeBinaryToHexString((byte[])obj, out);
      } else {
         writer.flush();
         out.write(this.encodeText(obj.toString()));
      }

   }

   protected String formatDateTime(Date time, TimeZone tz) {
      Calendar cal = Calendar.getInstance(tz, Locale.ENGLISH);
      cal.setTime(time);
      int offset = cal.get(15);
      offset += cal.get(16);
      Date dt1 = new Date(time.getTime() + (long)offset);
      StringBuffer sb = new StringBuffer();
      sb.append(DATE_FORMAT.format(dt1));
      offset /= 60000;
      if (offset == 0) {
         sb.append('Z');
      } else {
         if (offset > 0) {
            sb.append('+');
         } else {
            sb.append('-');
         }

         int offsetHour = Math.abs(offset / 60);
         int offsetMinutes = Math.abs(offset % 60);
         if (offsetHour < 10) {
            sb.append('0');
         }

         sb.append(Integer.toString(offsetHour));
         sb.append('\'');
         if (offsetMinutes < 10) {
            sb.append('0');
         }

         sb.append(Integer.toString(offsetMinutes));
         sb.append('\'');
      }

      return sb.toString();
   }

   protected String formatDateTime(Date time) {
      return this.formatDateTime(time, TimeZone.getDefault());
   }

   protected boolean contentEquals(PDFObject o) {
      return this.equals(o);
   }

   static {
      log = LogFactory.getLog(PDFObject.class.getName());
      DATE_FORMAT = new SimpleDateFormat("'D:'yyyyMMddHHmmss", Locale.ENGLISH);
      DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
   }
}
