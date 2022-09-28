package org.apache.james.mime4j.field;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.field.contenttype.parser.ContentTypeParser;
import org.apache.james.mime4j.field.contenttype.parser.ParseException;
import org.apache.james.mime4j.field.contenttype.parser.TokenMgrError;
import org.apache.james.mime4j.stream.Field;

public class ContentTypeFieldImpl extends AbstractField implements ContentTypeField {
   private boolean parsed = false;
   private String mimeType = null;
   private String mediaType = null;
   private String subType = null;
   private Map parameters = new HashMap();
   private ParseException parseException;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentTypeField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentTypeFieldImpl(rawField, monitor);
      }
   };

   ContentTypeFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public ParseException getParseException() {
      if (!this.parsed) {
         this.parse();
      }

      return this.parseException;
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

   public static String getMimeType(ContentTypeField child, ContentTypeField parent) {
      if (child == null || child.getMimeType() == null || child.isMultipart() && child.getBoundary() == null) {
         return parent != null && parent.isMimeType("multipart/digest") ? "message/rfc822" : "text/plain";
      } else {
         return child.getMimeType();
      }
   }

   public static String getCharset(ContentTypeField f) {
      if (f != null) {
         String charset = f.getCharset();
         if (charset != null && charset.length() > 0) {
            return charset;
         }
      }

      return "us-ascii";
   }

   private void parse() {
      String body = this.getBody();
      ContentTypeParser parser = new ContentTypeParser(new StringReader(body));

      try {
         parser.parseAll();
      } catch (ParseException var9) {
         this.parseException = var9;
      } catch (TokenMgrError var10) {
         this.parseException = new ParseException(var10.getMessage());
      }

      this.mediaType = parser.getType();
      this.subType = parser.getSubType();
      if (this.mediaType != null && this.subType != null) {
         this.mimeType = (this.mediaType + "/" + this.subType).toLowerCase();
         List paramNames = parser.getParamNames();
         List paramValues = parser.getParamValues();
         if (paramNames != null && paramValues != null) {
            int len = Math.min(paramNames.size(), paramValues.size());

            for(int i = 0; i < len; ++i) {
               String paramName = ((String)paramNames.get(i)).toLowerCase();
               String paramValue = (String)paramValues.get(i);
               this.parameters.put(paramName, paramValue);
            }
         }
      }

      this.parsed = true;
   }
}
