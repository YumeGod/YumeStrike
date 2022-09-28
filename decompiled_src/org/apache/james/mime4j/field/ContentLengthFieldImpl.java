package org.apache.james.mime4j.field;

import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentLengthField;
import org.apache.james.mime4j.stream.Field;

public class ContentLengthFieldImpl extends AbstractField implements ContentLengthField {
   private boolean parsed = false;
   private long contentLength;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentLengthField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentLengthFieldImpl(rawField, monitor);
      }
   };

   ContentLengthFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      this.contentLength = -1L;
      String body = this.getBody();
      if (body != null) {
         try {
            this.contentLength = Long.parseLong(body);
            if (this.contentLength < 0L) {
               this.contentLength = -1L;
               if (this.monitor.isListening()) {
                  this.monitor.warn("Negative content length: " + body, "ignoring Content-Length header");
               }
            }
         } catch (NumberFormatException var3) {
            if (this.monitor.isListening()) {
               this.monitor.warn("Invalid content length: " + body, "ignoring Content-Length header");
            }
         }
      }

   }

   public long getContentLength() {
      if (!this.parsed) {
         this.parse();
      }

      return this.contentLength;
   }
}
