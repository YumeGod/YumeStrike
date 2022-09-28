package org.apache.james.mime4j.field;

import java.io.StringReader;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentDispositionField;
import org.apache.james.mime4j.field.contentdisposition.parser.ContentDispositionParser;
import org.apache.james.mime4j.field.contentdisposition.parser.ParseException;
import org.apache.james.mime4j.field.contentdisposition.parser.TokenMgrError;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParser;
import org.apache.james.mime4j.stream.Field;

public class ContentDispositionFieldImpl extends AbstractField implements ContentDispositionField {
   private boolean parsed = false;
   private String dispositionType = "";
   private Map parameters = new HashMap();
   private ParseException parseException;
   private boolean creationDateParsed;
   private Date creationDate;
   private boolean modificationDateParsed;
   private Date modificationDate;
   private boolean readDateParsed;
   private Date readDate;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentDispositionField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentDispositionFieldImpl(rawField, monitor);
      }
   };

   ContentDispositionFieldImpl(Field rawField, DecodeMonitor monitor) {
      super(rawField, monitor);
   }

   public ParseException getParseException() {
      if (!this.parsed) {
         this.parse();
      }

      return this.parseException;
   }

   public String getDispositionType() {
      if (!this.parsed) {
         this.parse();
      }

      return this.dispositionType;
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

   public boolean isDispositionType(String dispositionType) {
      if (!this.parsed) {
         this.parse();
      }

      return this.dispositionType.equalsIgnoreCase(dispositionType);
   }

   public boolean isInline() {
      if (!this.parsed) {
         this.parse();
      }

      return this.dispositionType.equals("inline");
   }

   public boolean isAttachment() {
      if (!this.parsed) {
         this.parse();
      }

      return this.dispositionType.equals("attachment");
   }

   public String getFilename() {
      return this.getParameter("filename");
   }

   public Date getCreationDate() {
      if (!this.creationDateParsed) {
         this.creationDate = this.parseDate("creation-date");
         this.creationDateParsed = true;
      }

      return this.creationDate;
   }

   public Date getModificationDate() {
      if (!this.modificationDateParsed) {
         this.modificationDate = this.parseDate("modification-date");
         this.modificationDateParsed = true;
      }

      return this.modificationDate;
   }

   public Date getReadDate() {
      if (!this.readDateParsed) {
         this.readDate = this.parseDate("read-date");
         this.readDateParsed = true;
      }

      return this.readDate;
   }

   public long getSize() {
      String value = this.getParameter("size");
      if (value == null) {
         return -1L;
      } else {
         try {
            long size = Long.parseLong(value);
            return size < 0L ? -1L : size;
         } catch (NumberFormatException var4) {
            return -1L;
         }
      }
   }

   private Date parseDate(String paramName) {
      String value = this.getParameter(paramName);
      if (value == null) {
         this.monitor.warn("Parsing " + paramName + " null", "returning null");
         return null;
      } else {
         try {
            return (new DateTimeParser(new StringReader(value))).parseAll().getDate();
         } catch (org.apache.james.mime4j.field.datetime.parser.ParseException var4) {
            if (this.monitor.isListening()) {
               this.monitor.warn(paramName + " parameter is invalid: " + value, paramName + " parameter is ignored");
            }

            return null;
         } catch (TokenMgrError var5) {
            this.monitor.warn(paramName + " parameter is invalid: " + value, paramName + "parameter is ignored");
            return null;
         }
      }
   }

   private void parse() {
      String body = this.getBody();
      ContentDispositionParser parser = new ContentDispositionParser(new StringReader(body));

      try {
         parser.parseAll();
      } catch (ParseException var10) {
         this.parseException = var10;
      } catch (TokenMgrError var11) {
         this.parseException = new ParseException(var11.getMessage());
      }

      String dispositionType = parser.getDispositionType();
      if (dispositionType != null) {
         this.dispositionType = dispositionType.toLowerCase(Locale.US);
         List paramNames = parser.getParamNames();
         List paramValues = parser.getParamValues();
         if (paramNames != null && paramValues != null) {
            int len = Math.min(paramNames.size(), paramValues.size());

            for(int i = 0; i < len; ++i) {
               String paramName = ((String)paramNames.get(i)).toLowerCase(Locale.US);
               String paramValue = (String)paramValues.get(i);
               this.parameters.put(paramName, paramValue);
            }
         }
      }

      this.parsed = true;
   }
}
