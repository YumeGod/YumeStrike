package org.apache.james.mime4j.field;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ContentDispositionField;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.NameValuePair;
import org.apache.james.mime4j.stream.RawBody;
import org.apache.james.mime4j.stream.RawField;
import org.apache.james.mime4j.stream.RawFieldParser;

public class ContentDispositionFieldLenientImpl extends AbstractField implements ContentDispositionField {
   private static final String DEFAULT_DATE_FORMAT = "EEE, dd MMM yyyy hh:mm:ss ZZZZ";
   private final List datePatterns = new ArrayList();
   private boolean parsed = false;
   private String dispositionType = "";
   private Map parameters = new HashMap();
   private boolean creationDateParsed;
   private Date creationDate;
   private boolean modificationDateParsed;
   private Date modificationDate;
   private boolean readDateParsed;
   private Date readDate;
   public static final FieldParser PARSER = new FieldParser() {
      public ContentDispositionField parse(Field rawField, DecodeMonitor monitor) {
         return new ContentDispositionFieldLenientImpl(rawField, (Collection)null, monitor);
      }
   };

   ContentDispositionFieldLenientImpl(Field rawField, Collection dateParsers, DecodeMonitor monitor) {
      super(rawField, monitor);
      if (dateParsers != null) {
         this.datePatterns.addAll(dateParsers);
      } else {
         this.datePatterns.add("EEE, dd MMM yyyy hh:mm:ss ZZZZ");
      }

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

   private void parse() {
      this.parsed = true;
      RawField f = this.getRawField();
      RawBody body = RawFieldParser.DEFAULT.parseRawBody(f);
      String main = body.getValue();
      if (main != null) {
         this.dispositionType = main.toLowerCase(Locale.US);
      } else {
         this.dispositionType = null;
      }

      this.parameters.clear();
      Iterator i$ = body.getParams().iterator();

      while(i$.hasNext()) {
         NameValuePair nmp = (NameValuePair)i$.next();
         String name = nmp.getName().toLowerCase(Locale.US);
         this.parameters.put(name, nmp.getValue());
      }

   }

   private Date parseDate(String paramName) {
      String value = this.getParameter(paramName);
      if (value == null) {
         return null;
      } else {
         Iterator i$ = this.datePatterns.iterator();

         while(i$.hasNext()) {
            String datePattern = (String)i$.next();

            try {
               SimpleDateFormat parser = new SimpleDateFormat(datePattern, Locale.US);
               parser.setTimeZone(TimeZone.getTimeZone("GMT"));
               parser.setLenient(true);
               return parser.parse(value);
            } catch (ParseException var6) {
            }
         }

         if (this.monitor.isListening()) {
            this.monitor.warn(paramName + " parameter is invalid: " + value, paramName + " parameter is ignored");
         }

         return null;
      }
   }
}
