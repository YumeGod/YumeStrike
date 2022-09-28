package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.SingleBody;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.field.DefaultFieldParser;
import org.apache.james.mime4j.field.LenientFieldParser;
import org.apache.james.mime4j.parser.AbstractContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.BodyDescriptorBuilder;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.MimeConfig;

public class DefaultMessageBuilder implements MessageBuilder {
   private FieldParser fieldParser = null;
   private BodyFactory bodyFactory = null;
   private MimeConfig config = null;
   private BodyDescriptorBuilder bodyDescBuilder = null;
   private boolean contentDecoding = true;
   private boolean flatMode = false;
   private DecodeMonitor monitor = null;

   public void setFieldParser(FieldParser fieldParser) {
      this.fieldParser = fieldParser;
   }

   public void setBodyFactory(BodyFactory bodyFactory) {
      this.bodyFactory = bodyFactory;
   }

   public void setMimeEntityConfig(MimeConfig config) {
      this.config = config;
   }

   public void setBodyDescriptorBuilder(BodyDescriptorBuilder bodyDescBuilder) {
      this.bodyDescBuilder = bodyDescBuilder;
   }

   public void setDecodeMonitor(DecodeMonitor monitor) {
      this.monitor = monitor;
   }

   public void setContentDecoding(boolean contentDecoding) {
      this.contentDecoding = contentDecoding;
   }

   public void setFlatMode(boolean flatMode) {
      this.flatMode = flatMode;
   }

   public Header copy(Header other) {
      HeaderImpl copy = new HeaderImpl();
      Iterator i$ = other.getFields().iterator();

      while(i$.hasNext()) {
         Field otherField = (Field)i$.next();
         copy.addField(otherField);
      }

      return copy;
   }

   public BodyPart copy(Entity other) {
      BodyPart copy = new BodyPart();
      if (other.getHeader() != null) {
         copy.setHeader(this.copy(other.getHeader()));
      }

      if (other.getBody() != null) {
         copy.setBody(this.copy(other.getBody()));
      }

      return copy;
   }

   public Multipart copy(Multipart other) {
      MultipartImpl copy = new MultipartImpl(other.getSubType());
      Iterator i$ = other.getBodyParts().iterator();

      while(i$.hasNext()) {
         Entity otherBodyPart = (Entity)i$.next();
         copy.addBodyPart(this.copy(otherBodyPart));
      }

      copy.setPreamble(other.getPreamble());
      copy.setEpilogue(other.getEpilogue());
      return copy;
   }

   public Body copy(Body body) {
      if (body == null) {
         throw new IllegalArgumentException("Body is null");
      } else if (body instanceof Message) {
         return this.copy((Message)body);
      } else if (body instanceof Multipart) {
         return this.copy((Multipart)body);
      } else if (body instanceof SingleBody) {
         return ((SingleBody)body).copy();
      } else {
         throw new IllegalArgumentException("Unsupported body class");
      }
   }

   public Message copy(Message other) {
      MessageImpl copy = new MessageImpl();
      if (other.getHeader() != null) {
         copy.setHeader(this.copy(other.getHeader()));
      }

      if (other.getBody() != null) {
         copy.setBody(this.copy(other.getBody()));
      }

      return copy;
   }

   public Header newHeader() {
      return new HeaderImpl();
   }

   public Header newHeader(Header source) {
      return this.copy(source);
   }

   public Multipart newMultipart(String subType) {
      return new MultipartImpl(subType);
   }

   public Multipart newMultipart(Multipart source) {
      return this.copy(source);
   }

   public Header parseHeader(InputStream is) throws IOException, MimeIOException {
      MimeConfig cfg = this.config != null ? this.config : new MimeConfig();
      boolean strict = cfg.isStrictParsing();
      final DecodeMonitor mon = this.monitor != null ? this.monitor : (strict ? DecodeMonitor.STRICT : DecodeMonitor.SILENT);
      final FieldParser fp = this.fieldParser != null ? this.fieldParser : (strict ? DefaultFieldParser.getParser() : LenientFieldParser.getParser());
      final HeaderImpl header = new HeaderImpl();
      final MimeStreamParser parser = new MimeStreamParser();
      parser.setContentHandler(new AbstractContentHandler() {
         public void endHeader() {
            parser.stop();
         }

         public void field(Field field) throws MimeException {
            ParsedField parsedField;
            if (field instanceof ParsedField) {
               parsedField = (ParsedField)field;
            } else {
               parsedField = ((FieldParser)fp).parse(field, mon);
            }

            header.addField(parsedField);
         }
      });

      try {
         parser.parse(is);
         return header;
      } catch (MimeException var9) {
         throw new MimeIOException(var9);
      }
   }

   public Message newMessage() {
      return new MessageImpl();
   }

   public Message newMessage(Message source) {
      return this.copy(source);
   }

   public Message parseMessage(InputStream is) throws IOException, MimeIOException {
      try {
         MessageImpl message = new MessageImpl();
         MimeConfig cfg = this.config != null ? this.config : new MimeConfig();
         boolean strict = cfg.isStrictParsing();
         DecodeMonitor mon = this.monitor != null ? this.monitor : (strict ? DecodeMonitor.STRICT : DecodeMonitor.SILENT);
         BodyDescriptorBuilder bdb = this.bodyDescBuilder != null ? this.bodyDescBuilder : new DefaultBodyDescriptorBuilder((String)null, (FieldParser)(this.fieldParser != null ? this.fieldParser : (strict ? DefaultFieldParser.getParser() : LenientFieldParser.getParser())), mon);
         BodyFactory bf = this.bodyFactory != null ? this.bodyFactory : new BasicBodyFactory();
         MimeStreamParser parser = new MimeStreamParser(cfg, mon, (BodyDescriptorBuilder)bdb);
         parser.setContentHandler(new EntityBuilder(message, (BodyFactory)bf));
         parser.setContentDecoding(this.contentDecoding);
         if (this.flatMode) {
            parser.setFlat();
         } else {
            parser.setRecurse();
         }

         parser.parse(is);
         return message;
      } catch (MimeException var9) {
         throw new MimeIOException(var9);
      }
   }
}
