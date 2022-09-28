package org.apache.james.mime4j.field;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.DateTimeField;
import org.apache.james.mime4j.stream.Field;

public class DateTimeFieldLenientImpl extends AbstractField implements DateTimeField {
   private static final String[] DEFAULT_DATE_FORMATS = new String[]{"EEE, dd MMM yyyy hh:mm:ss ZZZZ", "dd MMM yyyy hh:mm:ss ZZZZ"};
   private final List datePatterns = new ArrayList();
   private boolean parsed = false;
   private Date date;
   public static final FieldParser PARSER = new FieldParser() {
      public DateTimeField parse(Field rawField, DecodeMonitor monitor) {
         return new DateTimeFieldLenientImpl(rawField, (Collection)null, monitor);
      }
   };

   DateTimeFieldLenientImpl(Field rawField, Collection dateParsers, DecodeMonitor monitor) {
      super(rawField, monitor);
      if (dateParsers != null) {
         this.datePatterns.addAll(dateParsers);
      } else {
         String[] arr$ = DEFAULT_DATE_FORMATS;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String pattern = arr$[i$];
            this.datePatterns.add(pattern);
         }
      }

   }

   public Date getDate() {
      if (!this.parsed) {
         this.parse();
      }

      return this.date;
   }

   private void parse() {
      this.parsed = true;
      this.date = null;
      String body = this.getBody();
      Iterator i$ = this.datePatterns.iterator();

      while(i$.hasNext()) {
         String datePattern = (String)i$.next();

         try {
            SimpleDateFormat parser = new SimpleDateFormat(datePattern, Locale.US);
            parser.setTimeZone(TimeZone.getTimeZone("GMT"));
            parser.setLenient(true);
            this.date = parser.parse(body);
            break;
         } catch (ParseException var5) {
         }
      }

   }

   public static FieldParser createParser(final Collection dateParsers) {
      return new FieldParser() {
         public DateTimeField parse(Field rawField, DecodeMonitor monitor) {
            return new DateTimeFieldLenientImpl(rawField, dateParsers, monitor);
         }
      };
   }
}
