package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import org.apache.james.mime4j.codec.CodecUtil;
import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageWriter;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.SingleBody;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.util.ByteArrayBuffer;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;
import org.apache.james.mime4j.util.MimeUtil;

public class DefaultMessageWriter implements MessageWriter {
   private static final byte[] CRLF = new byte[]{13, 10};
   private static final byte[] DASHES = new byte[]{45, 45};

   public void writeBody(Body body, OutputStream out) throws IOException {
      if (body instanceof Message) {
         this.writeEntity((Message)body, out);
      } else if (body instanceof Multipart) {
         this.writeMultipart((Multipart)body, out);
      } else {
         if (!(body instanceof SingleBody)) {
            throw new IllegalArgumentException("Unsupported body class");
         }

         ((SingleBody)body).writeTo(out);
      }

   }

   public void writeEntity(Entity entity, OutputStream out) throws IOException {
      Header header = entity.getHeader();
      if (header == null) {
         throw new IllegalArgumentException("Missing header");
      } else {
         this.writeHeader(header, out);
         Body body = entity.getBody();
         if (body == null) {
            throw new IllegalArgumentException("Missing body");
         } else {
            boolean binaryBody = body instanceof BinaryBody;
            OutputStream encOut = this.encodeStream(out, entity.getContentTransferEncoding(), binaryBody);
            this.writeBody(body, encOut);
            if (encOut != out) {
               encOut.close();
            }

         }
      }
   }

   public void writeMessage(Message message, OutputStream out) throws IOException {
      this.writeEntity(message, out);
   }

   public void writeMultipart(Multipart multipart, OutputStream out) throws IOException {
      ContentTypeField contentType = this.getContentType(multipart);
      ByteSequence boundary = this.getBoundary(contentType);
      ByteSequence preamble;
      ByteSequence epilogue;
      if (multipart instanceof MultipartImpl) {
         preamble = ((MultipartImpl)multipart).getPreambleRaw();
         epilogue = ((MultipartImpl)multipart).getEpilogueRaw();
      } else {
         preamble = multipart.getPreamble() != null ? ContentUtil.encode(multipart.getPreamble()) : null;
         epilogue = multipart.getEpilogue() != null ? ContentUtil.encode(multipart.getEpilogue()) : null;
      }

      if (preamble != null) {
         this.writeBytes(preamble, out);
         out.write(CRLF);
      }

      Iterator i$ = multipart.getBodyParts().iterator();

      while(i$.hasNext()) {
         Entity bodyPart = (Entity)i$.next();
         out.write(DASHES);
         this.writeBytes(boundary, out);
         out.write(CRLF);
         this.writeEntity(bodyPart, out);
         out.write(CRLF);
      }

      out.write(DASHES);
      this.writeBytes(boundary, out);
      out.write(DASHES);
      out.write(CRLF);
      if (epilogue != null) {
         this.writeBytes(epilogue, out);
      }

   }

   public void writeField(Field field, OutputStream out) throws IOException {
      ByteSequence raw = field.getRaw();
      if (raw == null) {
         StringBuilder buf = new StringBuilder();
         buf.append(field.getName());
         buf.append(": ");
         String body = field.getBody();
         if (body != null) {
            buf.append(body);
         }

         raw = ContentUtil.encode(MimeUtil.fold(buf.toString(), 0));
      }

      this.writeBytes(raw, out);
      out.write(CRLF);
   }

   public void writeHeader(Header header, OutputStream out) throws IOException {
      Iterator i$ = header.iterator();

      while(i$.hasNext()) {
         Field field = (Field)i$.next();
         this.writeField(field, out);
      }

      out.write(CRLF);
   }

   protected OutputStream encodeStream(OutputStream out, String encoding, boolean binaryBody) throws IOException {
      if (MimeUtil.isBase64Encoding(encoding)) {
         return CodecUtil.wrapBase64(out);
      } else {
         return MimeUtil.isQuotedPrintableEncoded(encoding) ? CodecUtil.wrapQuotedPrintable(out, binaryBody) : out;
      }
   }

   private ContentTypeField getContentType(Multipart multipart) {
      Entity parent = multipart.getParent();
      if (parent == null) {
         throw new IllegalArgumentException("Missing parent entity in multipart");
      } else {
         Header header = parent.getHeader();
         if (header == null) {
            throw new IllegalArgumentException("Missing header in parent entity");
         } else {
            ContentTypeField contentType = (ContentTypeField)header.getField("Content-Type");
            if (contentType == null) {
               throw new IllegalArgumentException("Content-Type field not specified");
            } else {
               return contentType;
            }
         }
      }
   }

   private ByteSequence getBoundary(ContentTypeField contentType) {
      String boundary = contentType.getBoundary();
      if (boundary == null) {
         throw new IllegalArgumentException("Multipart boundary not specified. Mime-Type: " + contentType.getMimeType() + ", Raw: " + contentType.toString());
      } else {
         return ContentUtil.encode(boundary);
      }
   }

   private void writeBytes(ByteSequence byteSequence, OutputStream out) throws IOException {
      if (byteSequence instanceof ByteArrayBuffer) {
         ByteArrayBuffer bab = (ByteArrayBuffer)byteSequence;
         out.write(bab.buffer(), 0, bab.length());
      } else {
         out.write(byteSequence.toByteArray());
      }

   }
}
