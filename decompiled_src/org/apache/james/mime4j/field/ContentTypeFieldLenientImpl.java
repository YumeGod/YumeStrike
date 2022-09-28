package org.apache.james.mime4j.field;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.NameValuePair;
import org.apache.james.mime4j.stream.RawBody;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.stream.RawFieldParser;

public class ContentTypeFieldLenientImpl extends AbstractField implements ContentTypeField {
   private boolean parsed = false;
   private String mimeType = null;
   private String mediaType = null;
   private String subType = null;
   private Map parameters = new HashMap();
   public static final FieldParser PARSER = new FieldParser() {
      public ContentTypeField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentTypeFieldLenientImpl(rawField, monitor);
      }
   };

   ContentTypeFieldLenientImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public String getMimeType() {
      if (!this.parsed) {
         this.parse();
      }

      return this.mimeType;
   }

   public String getMediaType() {
      if (!this.parsed) {
         this.parse();
      }

      return this.mediaType;
   }

   public String getSubType() {
      if (!this.parsed) {
         this.parse();
      }

      return this.subType;
   }

   public String getParameter(String name) {
      if (!this.parsed) {
         this.parse();
      }

      return (String)this.parameters.get(name.toLowerCase());
   }

   public Map getParameters() {
      if (!this.parsed) {
         this.parse();
      }

      return Collections.unmodifiableMap(this.parameters);
   }

   public boolean isMimeType(String mimeType) {
      if (!this.parsed) {
         this.parse();
      }

      return this.mimeType != null && this.mimeType.equalsIgnoreCase(mimeType);
   }

   public boolean isMultipart() {
      if (!this.parsed) {
         this.parse();
      }

      return this.mimeType != null && this.mimeType.startsWith("multipart/");
   }

   public String getBoundary() {
      return this.getParameter("boundary");
   }

   public String getCharset() {
      return this.getParameter("charset");
   }

   private void parse() {
      this.parsed = true;
      RawField f = this.getRawField();
      RawBody body = RawFieldParser.DEFAULT.parseRawBody(f);
      String main = body.getValue();
      String type = null;
      String subtype = null;
      if (main != null) {
         main = main.toLowerCase().trim();
         int index = main.indexOf(47);
         boolean valid = false;
         if (index != -1) {
            type = main.substring(0, index).trim();
            subtype = main.substring(index + 1).trim();
            if (type.length() > 0 && subtype.length() > 0) {
               main = type + "/" + subtype;
               valid = true;
            }
         }

         if (!valid) {
            if (this.monitor.isListening()) {
               this.monitor.warn("Invalid Content-Type: " + body, "Content-Type value ignored");
            }

            main = null;
            type = null;
            subtype = null;
         }
      }

      this.mimeType = main;
      this.mediaType = type;
      this.subType = subtype;
      this.parameters.clear();
      Iterator i$ = body.getParams().iterator();

      while(i$.hasNext()) {
         NameValuePair nmp = (NameValuePair)i$.next();
         String name = nmp.getName().toLowerCase(Locale.US);
         this.parameters.put(name, nmp.getValue());
      }

   }
}
