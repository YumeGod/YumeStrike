package org.apache.james.mime4j.parser;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.BodyDescriptorBuilder;
import org.apache.james.mime4j.stream.EntityState;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.MimeConfig;
import org.apache.james.mime4j.stream.MimeTokenStream;
import org.apache.james.mime4j.stream.RecursionMode;

public class MimeStreamParser {
   private ContentHandler handler;
   private boolean contentDecoding;
   private final MimeTokenStream mimeTokenStream;

   public MimeStreamParser(MimeTokenStream tokenStream) {
      this.handler = null;
      this.mimeTokenStream = tokenStream;
      this.contentDecoding = false;
   }

   public MimeStreamParser(MimeConfig config, DecodeMonitor monitor, BodyDescriptorBuilder bodyDescBuilder) {
      this(new MimeTokenStream(config != null ? config.clone() : new MimeConfig(), monitor, bodyDescBuilder));
   }

   public MimeStreamParser(MimeConfig config) {
      this(config, (DecodeMonitor)null, (BodyDescriptorBuilder)null);
   }

   public MimeStreamParser() {
      this(new MimeTokenStream(new MimeConfig(), (DecodeMonitor)null, (BodyDescriptorBuilder)null));
   }

   public boolean isContentDecoding() {
      return this.contentDecoding;
   }

   public void setContentDecoding(boolean b) {
      this.contentDecoding = b;
   }

   public void parse(InputStream instream) throws MimeException, IOException {
      MimeConfig config = this.mimeTokenStream.getConfig();
      if (config.getHeadlessParsing() != null) {
         Field contentType = this.mimeTokenStream.parseHeadless(instream, config.getHeadlessParsing());
         this.handler.startMessage();
         this.handler.startHeader();
         this.handler.field(contentType);
         this.handler.endHeader();
      } else {
         this.mimeTokenStream.parse(instream);
      }

      while(true) {
         EntityState state = this.mimeTokenStream.getState();
         switch (state) {
            case T_BODY:
               BodyDescriptor desc = this.mimeTokenStream.getBodyDescriptor();
               InputStream bodyContent;
               if (this.contentDecoding) {
                  bodyContent = this.mimeTokenStream.getDecodedInputStream();
               } else {
                  bodyContent = this.mimeTokenStream.getInputStream();
               }

               this.handler.body(desc, bodyContent);
               break;
            case T_END_BODYPART:
               this.handler.endBodyPart();
               break;
            case T_END_HEADER:
               this.handler.endHeader();
               break;
            case T_END_MESSAGE:
               this.handler.endMessage();
               break;
            case T_END_MULTIPART:
               this.handler.endMultipart();
               break;
            case T_END_OF_STREAM:
               return;
            case T_EPILOGUE:
               this.handler.epilogue(this.mimeTokenStream.getInputStream());
               break;
            case T_FIELD:
               this.handler.field(this.mimeTokenStream.getField());
               break;
            case T_PREAMBLE:
               this.handler.preamble(this.mimeTokenStream.getInputStream());
               break;
            case T_RAW_ENTITY:
               this.handler.raw(this.mimeTokenStream.getInputStream());
               break;
            case T_START_BODYPART:
               this.handler.startBodyPart();
               break;
            case T_START_HEADER:
               this.handler.startHeader();
               break;
            case T_START_MESSAGE:
               this.handler.startMessage();
               break;
            case T_START_MULTIPART:
               this.handler.startMultipart(this.mimeTokenStream.getBodyDescriptor());
               break;
            default:
               throw new IllegalStateException("Invalid state: " + state);
         }

         state = this.mimeTokenStream.next();
      }
   }

   public boolean isRaw() {
      return this.mimeTokenStream.isRaw();
   }

   public void setRaw() {
      this.mimeTokenStream.setRecursionMode(RecursionMode.M_RAW);
   }

   public void setFlat() {
      this.mimeTokenStream.setRecursionMode(RecursionMode.M_FLAT);
   }

   public void setRecurse() {
      this.mimeTokenStream.setRecursionMode(RecursionMode.M_RECURSE);
   }

   public void stop() {
      this.mimeTokenStream.stop();
   }

   public void setContentHandler(ContentHandler h) {
      this.handler = h;
   }
}
