package org.apache.james.mime4j.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.io.LineNumberInputStream;
import org.apache.james.mime4j.io.LineNumberSource;
import org.apache.james.mime4j.util.CharsetUtil;

public class MimeTokenStream {
   private final MimeConfig config;
   private final DecodeMonitor monitor;
   private final FieldBuilder fieldBuilder;
   private final BodyDescriptorBuilder bodyDescBuilder;
   private final LinkedList entities;
   private EntityState state;
   private EntityStateMachine currentStateMachine;
   private RecursionMode recursionMode;
   private MimeEntity rootentity;

   public MimeTokenStream() {
      this((MimeConfig)null);
   }

   public MimeTokenStream(MimeConfig config) {
      this(config, (DecodeMonitor)null, (FieldBuilder)null, (BodyDescriptorBuilder)null);
   }

   public MimeTokenStream(MimeConfig config, BodyDescriptorBuilder bodyDescBuilder) {
      this(config, (DecodeMonitor)null, (FieldBuilder)null, bodyDescBuilder);
   }

   public MimeTokenStream(MimeConfig config, DecodeMonitor monitor, BodyDescriptorBuilder bodyDescBuilder) {
      this(config, monitor, (FieldBuilder)null, bodyDescBuilder);
   }

   public MimeTokenStream(MimeConfig config, DecodeMonitor monitor, FieldBuilder fieldBuilder, BodyDescriptorBuilder bodyDescBuilder) {
      this.entities = new LinkedList();
      this.state = EntityState.T_END_OF_STREAM;
      this.recursionMode = RecursionMode.M_RECURSE;
      this.config = config != null ? config : new MimeConfig();
      this.fieldBuilder = (FieldBuilder)(fieldBuilder != null ? fieldBuilder : new DefaultFieldBuilder(this.config.getMaxHeaderLen()));
      this.monitor = monitor != null ? monitor : (this.config.isStrictParsing() ? DecodeMonitor.STRICT : DecodeMonitor.SILENT);
      this.bodyDescBuilder = (BodyDescriptorBuilder)(bodyDescBuilder != null ? bodyDescBuilder : new FallbackBodyDescriptorBuilder());
   }

   public void parse(InputStream stream) {
      this.doParse(stream, EntityState.T_START_MESSAGE);
   }

   public Field parseHeadless(InputStream stream, String contentType) {
      if (contentType == null) {
         throw new IllegalArgumentException("Content type may not be null");
      } else {
         Object newContentType;
         try {
            RawField rawContentType = new RawField("Content-Type", contentType);
            newContentType = this.bodyDescBuilder.addField(rawContentType);
            if (newContentType == null) {
               newContentType = rawContentType;
            }
         } catch (MimeException var7) {
            throw new IllegalArgumentException(var7.getMessage());
         }

         this.doParse(stream, EntityState.T_END_HEADER);

         try {
            this.next();
            return (Field)newContentType;
         } catch (IOException var5) {
            throw new IllegalStateException(var5);
         } catch (MimeException var6) {
            throw new IllegalStateException(var6);
         }
      }
   }

   private void doParse(InputStream stream, EntityState start) {
      LineNumberSource lineSource = null;
      if (this.config.isCountLineNumbers()) {
         LineNumberInputStream lineInput = new LineNumberInputStream((InputStream)stream);
         lineSource = lineInput;
         stream = lineInput;
      }

      this.rootentity = new MimeEntity(lineSource, (InputStream)stream, this.config, start, EntityState.T_END_MESSAGE, this.monitor, this.fieldBuilder, this.bodyDescBuilder);
      this.rootentity.setRecursionMode(this.recursionMode);
      this.currentStateMachine = this.rootentity;
      this.entities.clear();
      this.entities.add(this.currentStateMachine);
      this.state = this.currentStateMachine.getState();
   }

   public boolean isRaw() {
      return this.recursionMode == RecursionMode.M_RAW;
   }

   public RecursionMode getRecursionMode() {
      return this.recursionMode;
   }

   public void setRecursionMode(RecursionMode mode) {
      this.recursionMode = mode;
      if (this.currentStateMachine != null) {
         this.currentStateMachine.setRecursionMode(mode);
      }

   }

   public void stop() {
      this.rootentity.stop();
   }

   public EntityState getState() {
      return this.state;
   }

   public InputStream getInputStream() {
      return this.currentStateMachine.getContentStream();
   }

   public InputStream getDecodedInputStream() {
      return this.currentStateMachine.getDecodedContentStream();
   }

   public Reader getReader() {
      BodyDescriptor bodyDescriptor = this.getBodyDescriptor();
      String mimeCharset = bodyDescriptor.getCharset();
      Charset charset;
      if (mimeCharset != null && !"".equals(mimeCharset)) {
         charset = Charset.forName(mimeCharset);
      } else {
         charset = CharsetUtil.US_ASCII;
      }

      InputStream instream = this.getDecodedInputStream();
      return new InputStreamReader(instream, charset);
   }

   public BodyDescriptor getBodyDescriptor() {
      return this.currentStateMachine.getBodyDescriptor();
   }

   public Field getField() {
      return this.currentStateMachine.getField();
   }

   public EntityState next() throws IOException, MimeException {
      if (this.state != EntityState.T_END_OF_STREAM && this.currentStateMachine != null) {
         while(this.currentStateMachine != null) {
            EntityStateMachine next = this.currentStateMachine.advance();
            if (next != null) {
               this.entities.add(next);
               this.currentStateMachine = next;
            }

            this.state = this.currentStateMachine.getState();
            if (this.state != EntityState.T_END_OF_STREAM) {
               return this.state;
            }

            this.entities.removeLast();
            if (this.entities.isEmpty()) {
               this.currentStateMachine = null;
            } else {
               this.currentStateMachine = (EntityStateMachine)this.entities.getLast();
               this.currentStateMachine.setRecursionMode(this.recursionMode);
            }
         }

         this.state = EntityState.T_END_OF_STREAM;
         return this.state;
      } else {
         throw new IllegalStateException("No more tokens are available.");
      }
   }

   public static final String stateToString(EntityState state) {
      return MimeEntity.stateToString(state);
   }

   public MimeConfig getConfig() {
      return this.config;
   }
}
