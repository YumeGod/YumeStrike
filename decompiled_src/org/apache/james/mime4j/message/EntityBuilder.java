package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.util.ByteArrayBuffer;
import org.apache.james.mime4j.util.ByteSequence;

class EntityBuilder implements ContentHandler {
   private final Entity entity;
   private final BodyFactory bodyFactory;
   private final Stack stack;

   EntityBuilder(Entity entity, BodyFactory bodyFactory) {
      this.entity = entity;
      this.bodyFactory = bodyFactory;
      this.stack = new Stack();
   }

   private void expect(Class c) {
      if (!c.isInstance(this.stack.peek())) {
         throw new IllegalStateException("Internal stack error: Expected '" + c.getName() + "' found '" + this.stack.peek().getClass().getName() + "'");
      }
   }

   public void startMessage() throws MimeException {
      if (this.stack.isEmpty()) {
         this.stack.push(this.entity);
      } else {
         this.expect(Entity.class);
         Message m = new MessageImpl();
         ((Entity)this.stack.peek()).setBody(m);
         this.stack.push(m);
      }

   }

   public void endMessage() throws MimeException {
      this.expect(Message.class);
      this.stack.pop();
   }

   public void startHeader() throws MimeException {
      this.stack.push(new HeaderImpl());
   }

   public void field(Field field) throws MimeException {
      this.expect(Header.class);
      ((Header)this.stack.peek()).addField(field);
   }

   public void endHeader() throws MimeException {
      this.expect(Header.class);
      Header h = (Header)this.stack.pop();
      this.expect(Entity.class);
      ((Entity)this.stack.peek()).setHeader(h);
   }

   public void startMultipart(BodyDescriptor bd) throws MimeException {
      this.expect(Entity.class);
      Entity e = (Entity)this.stack.peek();
      String subType = bd.getSubType();
      Multipart multiPart = new MultipartImpl(subType);
      e.setBody(multiPart);
      this.stack.push(multiPart);
   }

   public void body(BodyDescriptor bd, InputStream is) throws MimeException, IOException {
      this.expect(Entity.class);
      Object body;
      if (bd.getMimeType().startsWith("text/")) {
         body = this.bodyFactory.textBody(is, bd.getCharset());
      } else {
         body = this.bodyFactory.binaryBody(is);
      }

      Entity entity = (Entity)this.stack.peek();
      entity.setBody((Body)body);
   }

   public void endMultipart() throws MimeException {
      this.stack.pop();
   }

   public void startBodyPart() throws MimeException {
      this.expect(Multipart.class);
      BodyPart bodyPart = new BodyPart();
      ((Multipart)this.stack.peek()).addBodyPart(bodyPart);
      this.stack.push(bodyPart);
   }

   public void endBodyPart() throws MimeException {
      this.expect(BodyPart.class);
      this.stack.pop();
   }

   public void epilogue(InputStream is) throws MimeException, IOException {
      this.expect(MultipartImpl.class);
      ByteSequence bytes = loadStream(is);
      ((MultipartImpl)this.stack.peek()).setEpilogueRaw(bytes);
   }

   public void preamble(InputStream is) throws MimeException, IOException {
      this.expect(MultipartImpl.class);
      ByteSequence bytes = loadStream(is);
      ((MultipartImpl)this.stack.peek()).setPreambleRaw(bytes);
   }

   public void raw(InputStream is) throws MimeException, IOException {
      throw new UnsupportedOperationException("Not supported");
   }

   private static ByteSequence loadStream(InputStream in) throws IOException {
      ByteArrayBuffer bab = new ByteArrayBuffer(64);

      int b;
      while((b = in.read()) != -1) {
         bab.append(b);
      }

      return bab;
   }
}
