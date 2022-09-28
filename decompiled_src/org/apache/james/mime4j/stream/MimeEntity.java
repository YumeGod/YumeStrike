package org.apache.james.mime4j.stream;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.Base64InputStream;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.codec.QuotedPrintableInputStream;
import org.apache.james.mime4j.io.BufferedLineReaderInputStream;
import org.apache.james.mime4j.io.LimitedInputStream;
import org.apache.james.mime4j.io.LineNumberSource;
import org.apache.james.mime4j.io.LineReaderInputStream;
import org.apache.james.mime4j.io.LineReaderInputStreamAdaptor;
import org.apache.james.mime4j.io.MaxHeaderLimitException;
import org.apache.james.mime4j.io.MaxLineLimitException;
import org.apache.james.mime4j.io.MimeBoundaryInputStream;
import org.apache.james.mime4j.util.ByteArrayBuffer;
import org.apache.james.mime4j.util.MimeUtil;

class MimeEntity implements EntityStateMachine {
   private final EntityState endState;
   private final MimeConfig config;
   private final DecodeMonitor monitor;
   private final FieldBuilder fieldBuilder;
   private final BodyDescriptorBuilder bodyDescBuilder;
   private final ByteArrayBuffer linebuf;
   private final LineNumberSource lineSource;
   private final BufferedLineReaderInputStream inbuffer;
   private EntityState state;
   private int lineCount;
   private boolean endOfHeader;
   private int headerCount;
   private Field field;
   private BodyDescriptor body;
   private RecursionMode recursionMode;
   private MimeBoundaryInputStream currentMimePartStream;
   private LineReaderInputStreamAdaptor dataStream;
   private byte[] tmpbuf;

   MimeEntity(LineNumberSource lineSource, InputStream instream, MimeConfig config, EntityState startState, EntityState endState, DecodeMonitor monitor, FieldBuilder fieldBuilder, BodyDescriptorBuilder bodyDescBuilder) {
      this.config = config;
      this.state = startState;
      this.endState = endState;
      this.monitor = monitor;
      this.fieldBuilder = fieldBuilder;
      this.bodyDescBuilder = bodyDescBuilder;
      this.linebuf = new ByteArrayBuffer(64);
      this.lineCount = 0;
      this.endOfHeader = false;
      this.headerCount = 0;
      this.lineSource = lineSource;
      this.inbuffer = new BufferedLineReaderInputStream(instream, 4096, config.getMaxLineLen());
      this.dataStream = new LineReaderInputStreamAdaptor(this.inbuffer, config.getMaxLineLen());
   }

   MimeEntity(LineNumberSource lineSource, InputStream instream, MimeConfig config, EntityState startState, EntityState endState, BodyDescriptorBuilder bodyDescBuilder) {
      this(lineSource, instream, config, startState, endState, config.isStrictParsing() ? DecodeMonitor.STRICT : DecodeMonitor.SILENT, new DefaultFieldBuilder(config.getMaxHeaderLen()), bodyDescBuilder);
   }

   MimeEntity(LineNumberSource lineSource, InputStream instream, MimeConfig config, BodyDescriptorBuilder bodyDescBuilder) {
      this(lineSource, instream, config, EntityState.T_START_MESSAGE, EntityState.T_END_MESSAGE, config.isStrictParsing() ? DecodeMonitor.STRICT : DecodeMonitor.SILENT, new DefaultFieldBuilder(config.getMaxHeaderLen()), bodyDescBuilder);
   }

   MimeEntity(LineNumberSource lineSource, InputStream instream, FieldBuilder fieldBuilder, BodyDescriptorBuilder bodyDescBuilder) {
      this(lineSource, instream, new MimeConfig(), EntityState.T_START_MESSAGE, EntityState.T_END_MESSAGE, DecodeMonitor.SILENT, fieldBuilder, bodyDescBuilder);
   }

   MimeEntity(LineNumberSource lineSource, InputStream instream, BodyDescriptorBuilder bodyDescBuilder) {
      this(lineSource, instream, new MimeConfig(), EntityState.T_START_MESSAGE, EntityState.T_END_MESSAGE, DecodeMonitor.SILENT, new DefaultFieldBuilder(-1), bodyDescBuilder);
   }

   public EntityState getState() {
      return this.state;
   }

   public RecursionMode getRecursionMode() {
      return this.recursionMode;
   }

   public void setRecursionMode(RecursionMode recursionMode) {
      this.recursionMode = recursionMode;
   }

   public void stop() {
      this.inbuffer.truncate();
   }

   private int getLineNumber() {
      return this.lineSource == null ? -1 : this.lineSource.getLineNumber();
   }

   private LineReaderInputStream getDataStream() {
      return this.dataStream;
   }

   protected String message(Event event) {
      String message;
      if (event == null) {
         message = "Event is unexpectedly null.";
      } else {
         message = event.toString();
      }

      int lineNumber = this.getLineNumber();
      return lineNumber <= 0 ? message : "Line " + lineNumber + ": " + message;
   }

   protected void monitor(Event event) throws MimeException, IOException {
      if (this.monitor.isListening()) {
         String message = this.message(event);
         if (this.monitor.warn(message, "ignoring")) {
            throw new MimeParseEventException(event);
         }
      }

   }

   private void readRawField() throws IOException, MimeException {
      if (this.endOfHeader) {
         throw new IllegalStateException();
      } else {
         LineReaderInputStream instream = this.getDataStream();

         try {
            while(true) {
               int len = this.linebuf.length();
               if (len > 0) {
                  this.fieldBuilder.append(this.linebuf);
               }

               this.linebuf.clear();
               if (instream.readLine(this.linebuf) == -1) {
                  this.monitor(Event.HEADERS_PREMATURE_END);
                  this.endOfHeader = true;
                  break;
               }

               len = this.linebuf.length();
               if (len > 0 && this.linebuf.byteAt(len - 1) == 10) {
                  --len;
               }

               if (len > 0 && this.linebuf.byteAt(len - 1) == 13) {
                  --len;
               }

               if (len == 0) {
                  this.endOfHeader = true;
                  break;
               }

               ++this.lineCount;
               if (this.lineCount > 1) {
                  int ch = this.linebuf.byteAt(0);
                  if (ch != 32 && ch != 9) {
                     break;
                  }
               }
            }

         } catch (MaxLineLimitException var4) {
            throw new MimeException(var4);
         }
      }
   }

   protected boolean nextField() throws MimeException, IOException {
      int maxHeaderCount = this.config.getMaxHeaderCount();

      while(!this.endOfHeader) {
         if (maxHeaderCount > 0 && this.headerCount >= maxHeaderCount) {
            throw new MaxHeaderLimitException("Maximum header limit exceeded");
         }

         ++this.headerCount;
         this.fieldBuilder.reset();
         this.readRawField();

         try {
            RawField rawfield = this.fieldBuilder.build();
            if (rawfield != null) {
               if (rawfield.getDelimiterIdx() != rawfield.getName().length()) {
                  this.monitor(Event.OBSOLETE_HEADER);
               }

               Field parsedField = this.bodyDescBuilder.addField(rawfield);
               this.field = (Field)(parsedField != null ? parsedField : rawfield);
               return true;
            }
         } catch (MimeException var5) {
            this.monitor(Event.INVALID_HEADER);
            if (this.config.isMalformedHeaderStartsBody()) {
               LineReaderInputStream instream = this.getDataStream();
               ByteArrayBuffer buf = this.fieldBuilder.getRaw();
               if (buf != null && instream.unread(buf)) {
                  return false;
               }

               throw new MimeParseEventException(Event.INVALID_HEADER);
            }
         }
      }

      return false;
   }

   public EntityStateMachine advance() throws IOException, MimeException {
      boolean empty;
      switch (this.state) {
         case T_START_MESSAGE:
            this.state = EntityState.T_START_HEADER;
            break;
         case T_START_BODYPART:
            this.state = EntityState.T_START_HEADER;
            break;
         case T_START_HEADER:
            this.bodyDescBuilder.reset();
         case T_FIELD:
            this.state = this.nextField() ? EntityState.T_FIELD : EntityState.T_END_HEADER;
            break;
         case T_END_HEADER:
            this.body = this.bodyDescBuilder.build();
            String mimeType = this.body.getMimeType();
            if (this.recursionMode == RecursionMode.M_FLAT) {
               this.state = EntityState.T_BODY;
            } else if (MimeUtil.isMultipart(mimeType)) {
               this.state = EntityState.T_START_MULTIPART;
               this.clearMimePartStream();
            } else {
               if (this.recursionMode != RecursionMode.M_NO_RECURSE && MimeUtil.isMessage(mimeType)) {
                  this.state = EntityState.T_BODY;
                  return this.nextMessage();
               }

               this.state = EntityState.T_BODY;
            }
            break;
         case T_START_MULTIPART:
            if (this.dataStream.isUsed()) {
               this.advanceToBoundary();
               this.state = EntityState.T_END_MULTIPART;
               break;
            } else {
               this.createMimePartStream();
               this.state = EntityState.T_PREAMBLE;
               empty = this.currentMimePartStream.isEmptyStream();
               if (!empty) {
                  break;
               }
            }
         case T_PREAMBLE:
            this.advanceToBoundary();
            if (this.currentMimePartStream.eof() && !this.currentMimePartStream.isLastPart()) {
               this.monitor(Event.MIME_BODY_PREMATURE_END);
            } else if (!this.currentMimePartStream.isLastPart()) {
               this.clearMimePartStream();
               this.createMimePartStream();
               return this.nextMimeEntity();
            }

            empty = this.currentMimePartStream.isFullyConsumed();
            this.clearMimePartStream();
            this.state = EntityState.T_EPILOGUE;
            if (!empty) {
               break;
            }
         case T_EPILOGUE:
            this.state = EntityState.T_END_MULTIPART;
            break;
         case T_BODY:
         case T_END_MULTIPART:
            this.state = this.endState;
            break;
         default:
            if (this.state != this.endState) {
               throw new IllegalStateException("Invalid state: " + stateToString(this.state));
            }

            this.state = EntityState.T_END_OF_STREAM;
      }

      return null;
   }

   private void createMimePartStream() throws MimeException, IOException {
      String boundary = this.body.getBoundary();

      try {
         this.currentMimePartStream = new MimeBoundaryInputStream(this.inbuffer, boundary, this.config.isStrictParsing());
      } catch (IllegalArgumentException var3) {
         throw new MimeException(var3.getMessage(), var3);
      }

      this.dataStream = new LineReaderInputStreamAdaptor(this.currentMimePartStream, this.config.getMaxLineLen());
   }

   private void clearMimePartStream() {
      this.currentMimePartStream = null;
      this.dataStream = new LineReaderInputStreamAdaptor(this.inbuffer, this.config.getMaxLineLen());
   }

   private void advanceToBoundary() throws IOException {
      if (!this.dataStream.eof()) {
         if (this.tmpbuf == null) {
            this.tmpbuf = new byte[2048];
         }

         InputStream instream = this.getLimitedContentStream();

         while(true) {
            if (instream.read(this.tmpbuf) != -1) {
               continue;
            }
         }
      }

   }

   private EntityStateMachine nextMessage() {
      InputStream instream = this.currentMimePartStream != null ? this.currentMimePartStream : this.inbuffer;
      InputStream instream = this.decodedStream((InputStream)instream);
      return this.nextMimeEntity(EntityState.T_START_MESSAGE, EntityState.T_END_MESSAGE, instream);
   }

   private InputStream decodedStream(InputStream instream) {
      String transferEncoding = this.body.getTransferEncoding();
      if (MimeUtil.isBase64Encoding(transferEncoding)) {
         instream = new Base64InputStream((InputStream)instream, this.monitor);
      } else if (MimeUtil.isQuotedPrintableEncoded(transferEncoding)) {
         instream = new QuotedPrintableInputStream((InputStream)instream, this.monitor);
      }

      return (InputStream)instream;
   }

   private EntityStateMachine nextMimeEntity() {
      return this.nextMimeEntity(EntityState.T_START_BODYPART, EntityState.T_END_BODYPART, this.currentMimePartStream);
   }

   private EntityStateMachine nextMimeEntity(EntityState startState, EntityState endState, InputStream instream) {
      if (this.recursionMode == RecursionMode.M_RAW) {
         RawEntity message = new RawEntity(instream);
         return message;
      } else {
         MimeEntity mimeentity = new MimeEntity(this.lineSource, instream, this.config, startState, endState, this.monitor, this.fieldBuilder, this.bodyDescBuilder.newChild());
         mimeentity.setRecursionMode(this.recursionMode);
         return mimeentity;
      }
   }

   private InputStream getLimitedContentStream() {
      long maxContentLimit = this.config.getMaxContentLen();
      return (InputStream)(maxContentLimit >= 0L ? new LimitedInputStream(this.dataStream, maxContentLimit) : this.dataStream);
   }

   public BodyDescriptor getBodyDescriptor() {
      switch (this.getState()) {
         case T_START_MULTIPART:
         case T_PREAMBLE:
         case T_EPILOGUE:
         case T_BODY:
         case T_END_OF_STREAM:
            return this.body;
         case T_END_MULTIPART:
         default:
            throw new IllegalStateException("Invalid state :" + stateToString(this.state));
      }
   }

   public Field getField() {
      switch (this.getState()) {
         case T_FIELD:
            return this.field;
         default:
            throw new IllegalStateException("Invalid state :" + stateToString(this.state));
      }
   }

   public InputStream getContentStream() {
      switch (this.state) {
         case T_START_MULTIPART:
         case T_PREAMBLE:
         case T_EPILOGUE:
         case T_BODY:
            return this.getLimitedContentStream();
         default:
            throw new IllegalStateException("Invalid state: " + stateToString(this.state));
      }
   }

   public InputStream getDecodedContentStream() throws IllegalStateException {
      return this.decodedStream(this.getContentStream());
   }

   public String toString() {
      return this.getClass().getName() + " [" + stateToString(this.state) + "][" + this.body.getMimeType() + "][" + this.body.getBoundary() + "]";
   }

   public static final String stateToString(EntityState state) {
      String result;
      switch (state) {
         case T_START_MESSAGE:
            result = "Start message";
            break;
         case T_START_BODYPART:
            result = "Start bodypart";
            break;
         case T_START_HEADER:
            result = "Start header";
            break;
         case T_FIELD:
            result = "Field";
            break;
         case T_END_HEADER:
            result = "End header";
            break;
         case T_START_MULTIPART:
            result = "Start multipart";
            break;
         case T_PREAMBLE:
            result = "Preamble";
            break;
         case T_EPILOGUE:
            result = "Epilogue";
            break;
         case T_BODY:
            result = "Body";
            break;
         case T_END_MULTIPART:
            result = "End multipart";
            break;
         case T_END_OF_STREAM:
            result = "End of stream";
            break;
         case T_END_MESSAGE:
            result = "End message";
            break;
         case T_RAW_ENTITY:
            result = "Raw entity";
            break;
         case T_END_BODYPART:
            result = "End bodypart";
            break;
         default:
            result = "Unknown";
      }

      return result;
   }
}
