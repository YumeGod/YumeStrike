package org.apache.james.mime4j.field;

import java.io.StringReader;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.MimeVersionField;
import org.apache.james.mime4j.field.mimeversion.parser.MimeVersionParser;
import org.apache.james.mime4j.field.mimeversion.parser.ParseException;
import org.apache.james.mime4j.stream.Field;

public class MimeVersionFieldImpl extends AbstractField implements MimeVersionField {
   public static final int DEFAULT_MINOR_VERSION = 0;
   public static final int DEFAULT_MAJOR_VERSION = 1;
   private boolean parsed = false;
   private int major = 1;
   private int minor = 0;
   private ParseException parsedException;
   public static final FieldParser PARSER = new FieldParser() {
      public MimeVersionField parse(Field rawField, DecodeMonitor monitor) {
         return new MimeVersionFieldImpl(rawField, monitor);
      }
   };

   MimeVersionFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      this.major = 1;
      this.minor = 0;
      String body = this.getBody();
      if (body != null) {
         StringReader reader = new StringReader(body);
         MimeVersionParser parser = new MimeVersionParser(reader);

         try {
            parser.parse();
            int v = parser.getMajorVersion();
            if (v != -1) {
               this.major = v;
            }

            v = parser.getMinorVersion();
            if (v != -1) {
               this.minor = v;
            }
         } catch (MimeException var5) {
            this.parsedException = new ParseException(var5);
         }
      }

   }

   public int getMinorVersion() {
      if (!this.parsed) {
         this.parse();
      }

      return this.minor;
   }

   public int getMajorVersion() {
      if (!this.parsed) {
         this.parse();
      }

      return this.major;
   }

   public org.apache.james.mime4j.dom.field.ParseException getParseException() {
      return this.parsedException;
   }
}
