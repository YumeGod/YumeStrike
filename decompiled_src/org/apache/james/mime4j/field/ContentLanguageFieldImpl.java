package org.apache.james.mime4j.field;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentLanguageField;
import org.apache.james.mime4j.field.language.parser.ContentLanguageParser;
import org.apache.james.mime4j.field.language.parser.ParseException;
import org.apache.james.mime4j.stream.Field;

public class ContentLanguageFieldImpl extends AbstractField implements ContentLanguageField {
   private boolean parsed = false;
   private List languages;
   private ParseException parseException;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentLanguageField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentLanguageFieldImpl(rawField, monitor);
      }
   };

   ContentLanguageFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   private void parse() {
      this.parsed = true;
      this.languages = Collections.emptyList();
      String body = this.getBody();
      if (body != null) {
         ContentLanguageParser parser = new ContentLanguageParser(new StringReader(body));

         try {
            this.languages = parser.parse();
         } catch (ParseException var4) {
            this.parseException = var4;
         }
      }

   }

   public org.apache.james.mime4j.dom.field.ParseException getParseException() {
      return this.parseException;
   }

   public List getLanguages() {
      if (!this.parsed) {
         this.parse();
      }

      return new ArrayList(this.languages);
   }
}
