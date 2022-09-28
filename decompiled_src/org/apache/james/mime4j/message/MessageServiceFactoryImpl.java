package org.apache.james.mime4j.message;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.MessageServiceFactory;
import org.apache.james.mime4j.dom.MessageWriter;
import org.apache.james.mime4j.stream.BodyDescriptorBuilder;
import org.apache.james.mime4j.stream.MimeConfig;

public class MessageServiceFactoryImpl extends MessageServiceFactory {
   private BodyFactory bodyFactory = null;
   private MimeConfig mimeEntityConfig = null;
   private BodyDescriptorBuilder bodyDescriptorBuilder = null;
   private DecodeMonitor decodeMonitor = null;
   private Boolean flatMode = null;
   private Boolean contentDecoding = null;

   public MessageBuilder newMessageBuilder() {
      DefaultMessageBuilder m = new DefaultMessageBuilder();
      if (this.bodyFactory != null) {
         m.setBodyFactory(this.bodyFactory);
      }

      if (this.mimeEntityConfig != null) {
         m.setMimeEntityConfig(this.mimeEntityConfig);
      }

      if (this.bodyDescriptorBuilder != null) {
         m.setBodyDescriptorBuilder(this.bodyDescriptorBuilder);
      }

      if (this.flatMode != null) {
         m.setFlatMode(this.flatMode);
      }

      if (this.contentDecoding != null) {
         m.setContentDecoding(this.contentDecoding);
      }

      if (this.decodeMonitor != null) {
         m.setDecodeMonitor(this.decodeMonitor);
      }

      return m;
   }

   public MessageWriter newMessageWriter() {
      return new DefaultMessageWriter();
   }

   public void setAttribute(String name, Object value) throws IllegalArgumentException {
      if ("BodyFactory".equals(name)) {
         if (value instanceof BodyFactory) {
            this.bodyFactory = (BodyFactory)value;
         } else {
            throw new IllegalArgumentException("Unsupported attribute value type for " + name + ", expected a BodyFactory");
         }
      } else if ("MimeEntityConfig".equals(name)) {
         if (value instanceof MimeConfig) {
            this.mimeEntityConfig = (MimeConfig)value;
         } else {
            throw new IllegalArgumentException("Unsupported attribute value type for " + name + ", expected a MimeConfig");
         }
      } else if ("MutableBodyDescriptorFactory".equals(name)) {
         if (value instanceof BodyDescriptorBuilder) {
            this.bodyDescriptorBuilder = (BodyDescriptorBuilder)value;
         } else {
            throw new IllegalArgumentException("Unsupported attribute value type for " + name + ", expected a MutableBodyDescriptorFactory");
         }
      } else if ("DecodeMonitor".equals(name)) {
         if (value instanceof DecodeMonitor) {
            this.decodeMonitor = (DecodeMonitor)value;
         } else {
            throw new IllegalArgumentException("Unsupported attribute value type for " + name + ", expected a DecodeMonitor");
         }
      } else if ("FlatMode".equals(name)) {
         if (value instanceof Boolean) {
            this.flatMode = (Boolean)value;
         } else {
            throw new IllegalArgumentException("Unsupported attribute value type for " + name + ", expected a Boolean");
         }
      } else if ("ContentDecoding".equals(name)) {
         if (value instanceof Boolean) {
            this.contentDecoding = (Boolean)value;
         } else {
            throw new IllegalArgumentException("Unsupported attribute value type for " + name + ", expected a Boolean");
         }
      } else {
         throw new IllegalArgumentException("Unsupported attribute: " + name);
      }
   }
}
