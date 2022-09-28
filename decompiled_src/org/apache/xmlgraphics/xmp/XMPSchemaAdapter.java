package org.apache.xmlgraphics.xmp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.xmlgraphics.util.QName;

public class XMPSchemaAdapter {
   protected Metadata meta;
   private XMPSchema schema;

   public XMPSchemaAdapter(Metadata meta, XMPSchema schema) {
      if (meta == null) {
         throw new NullPointerException("Parameter meta must not be null");
      } else if (schema == null) {
         throw new NullPointerException("Parameter schema must not be null");
      } else {
         this.meta = meta;
         this.schema = schema;
      }
   }

   public XMPSchema getSchema() {
      return this.schema;
   }

   protected QName getQName(String propName) {
      return new QName(this.getSchema().getNamespace(), this.getSchema().getPreferredPrefix(), propName);
   }

   private void addStringToArray(String propName, String value, XMPArrayType arrayType) {
      if (value != null && value.length() != 0) {
         this.addObjectToArray(propName, value, arrayType);
      } else {
         throw new IllegalArgumentException("Value must not be empty");
      }
   }

   protected void addObjectToArray(String propName, Object value, XMPArrayType arrayType) {
      if (value == null) {
         throw new IllegalArgumentException("Value must not be null");
      } else {
         QName name = this.getQName(propName);
         XMPProperty prop = this.meta.getProperty(name);
         if (prop == null) {
            prop = new XMPProperty(name, value);
            this.meta.setProperty(prop);
         } else {
            prop.convertSimpleValueToArray(arrayType);
            prop.getArrayValue().add(value);
         }

      }
   }

   protected boolean removeStringFromArray(String propName, String value) {
      if (value == null) {
         return false;
      } else {
         QName name = this.getQName(propName);
         XMPProperty prop = this.meta.getProperty(name);
         if (prop != null) {
            if (prop.isArray()) {
               XMPArray arr = prop.getArrayValue();
               boolean removed = arr.remove(value);
               if (arr.isEmpty()) {
                  this.meta.removeProperty(name);
               }

               return removed;
            }

            Object currentValue = prop.getValue();
            if (value.equals(currentValue)) {
               this.meta.removeProperty(name);
               return true;
            }
         }

         return false;
      }
   }

   protected void addStringToSeq(String propName, String value) {
      this.addStringToArray(propName, value, XMPArrayType.SEQ);
   }

   protected void addStringToBag(String propName, String value) {
      this.addStringToArray(propName, value, XMPArrayType.BAG);
   }

   public static String formatISO8601Date(Date dt) {
      return formatISO8601Date(dt, TimeZone.getDefault());
   }

   private static DateFormat createPseudoISO8601DateFormat() {
      DateFormat df = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss", Locale.ENGLISH);
      df.setTimeZone(TimeZone.getTimeZone("GMT"));
      return df;
   }

   public static String formatISO8601Date(Date dt, TimeZone tz) {
      Calendar cal = Calendar.getInstance(tz, Locale.ENGLISH);
      cal.setTime(dt);
      int offset = cal.get(15);
      offset += cal.get(16);
      Date dt1 = new Date(dt.getTime() + (long)offset);
      StringBuffer sb = new StringBuffer(createPseudoISO8601DateFormat().format(dt1));
      offset /= 60000;
      if (offset == 0) {
         sb.append('Z');
      } else {
         int zoneOffsetHours = offset / 60;
         int zoneOffsetMinutes = Math.abs(offset % 60);
         if (zoneOffsetHours > 0) {
            sb.append('+');
         } else {
            sb.append('-');
         }

         if (zoneOffsetHours < 10) {
            sb.append('0');
         }

         sb.append(Math.abs(zoneOffsetHours));
         sb.append(':');
         if (zoneOffsetMinutes < 10) {
            sb.append('0');
         }

         sb.append(zoneOffsetMinutes);
      }

      return sb.toString();
   }

   public static Date parseISO8601Date(String dt) {
      int offset = 0;
      String parsablePart;
      if (dt.endsWith("Z")) {
         parsablePart = dt.substring(0, dt.length() - 1);
      } else {
         int neg = 1;
         int pos = dt.lastIndexOf(43);
         if (pos < 0) {
            pos = dt.lastIndexOf(45);
            neg = -1;
         }

         if (pos >= 0) {
            String timeZonePart = dt.substring(pos);
            parsablePart = dt.substring(0, pos);
            offset = Integer.parseInt(timeZonePart.substring(1, 3)) * 60;
            offset += Integer.parseInt(timeZonePart.substring(4, 6));
            offset *= neg;
         } else {
            parsablePart = dt;
         }
      }

      Date d;
      try {
         d = createPseudoISO8601DateFormat().parse(parsablePart);
      } catch (ParseException var6) {
         throw new IllegalArgumentException("Invalid ISO 8601 date format: " + dt);
      }

      d.setTime(d.getTime() - (long)(offset * 60 * 1000));
      return d;
   }

   protected void addDateToSeq(String propName, Date value) {
      String dt = formatISO8601Date(value);
      this.addStringToSeq(propName, dt);
   }

   protected void setDateValue(String propName, Date value) {
      String dt = formatISO8601Date(value);
      this.setValue(propName, dt);
   }

   protected Date getDateValue(String propName) {
      String dt = this.getValue(propName);
      return dt == null ? null : parseISO8601Date(dt);
   }

   protected void setLangAlt(String propName, String lang, String value) {
      if (lang == null) {
         lang = "x-default";
      }

      QName name = this.getQName(propName);
      XMPProperty prop = this.meta.getProperty(name);
      if (prop == null) {
         if (value != null && value.length() > 0) {
            prop = new XMPProperty(name, value);
            prop.setXMLLang(lang);
            this.meta.setProperty(prop);
         }
      } else {
         prop.convertSimpleValueToArray(XMPArrayType.ALT);
         XMPArray array = prop.getArrayValue();
         array.removeLangValue(lang);
         if (value != null && value.length() > 0) {
            array.add(value, lang);
         } else if (array.isEmpty()) {
            this.meta.removeProperty(name);
         }
      }

   }

   protected void setValue(String propName, String value) {
      QName name = this.getQName(propName);
      XMPProperty prop = this.meta.getProperty(name);
      if (value != null && value.length() > 0) {
         if (prop != null) {
            prop.setValue(value);
         } else {
            prop = new XMPProperty(name, value);
            this.meta.setProperty(prop);
         }
      } else if (prop != null) {
         this.meta.removeProperty(name);
      }

   }

   protected String getValue(String propName) {
      QName name = this.getQName(propName);
      XMPProperty prop = this.meta.getProperty(name);
      return prop == null ? null : prop.getValue().toString();
   }

   protected String removeLangAlt(String lang, String propName) {
      QName name = this.getQName(propName);
      XMPProperty prop = this.meta.getProperty(name);
      if (prop != null && lang != null) {
         XMPArray array = prop.getArrayValue();
         String removed;
         if (array != null) {
            removed = array.removeLangValue(lang);
            if (array.isEmpty()) {
               this.meta.removeProperty(name);
            }

            return removed;
         } else {
            removed = prop.getValue().toString();
            if (lang.equals(prop.getXMLLang())) {
               this.meta.removeProperty(name);
            }

            return removed;
         }
      } else {
         return null;
      }
   }

   protected String getLangAlt(String lang, String propName) {
      XMPProperty prop = this.meta.getProperty(this.getQName(propName));
      if (prop == null) {
         return null;
      } else {
         XMPArray array = prop.getArrayValue();
         return array != null ? array.getLangValue(lang) : prop.getValue().toString();
      }
   }

   protected PropertyAccess findQualifiedStructure(String propName, QName qualifier, String qualifierValue) {
      XMPProperty prop = this.meta.getProperty(this.getQName(propName));
      if (prop != null) {
         XMPArray array = prop.getArrayValue();
         if (array != null) {
            int i = 0;

            for(int c = array.getSize(); i < c; ++i) {
               Object value = array.getValue(i);
               if (value instanceof PropertyAccess) {
                  PropertyAccess pa = (PropertyAccess)value;
                  XMPProperty q = pa.getProperty(qualifier);
                  if (q != null && q.getValue().equals(qualifierValue)) {
                     return pa;
                  }
               }
            }
         } else if (prop.getStructureValue() != null) {
            PropertyAccess pa = prop.getStructureValue();
            XMPProperty q = pa.getProperty(qualifier);
            if (q != null && q.getValue().equals(qualifierValue)) {
               return pa;
            }
         }
      }

      return null;
   }

   protected Object findQualifiedValue(String propName, QName qualifier, String qualifierValue) {
      PropertyAccess pa = this.findQualifiedStructure(propName, qualifier, qualifierValue);
      if (pa != null) {
         XMPProperty rdfValue = pa.getValueProperty();
         if (rdfValue != null) {
            return rdfValue.getValue();
         }
      }

      return null;
   }

   protected Object[] getObjectArray(String propName) {
      XMPProperty prop = this.meta.getProperty(this.getQName(propName));
      if (prop == null) {
         return null;
      } else {
         XMPArray array = prop.getArrayValue();
         return array != null ? array.toObjectArray() : new Object[]{prop.getValue()};
      }
   }

   protected String[] getStringArray(String propName) {
      Object[] arr = this.getObjectArray(propName);
      if (arr == null) {
         return null;
      } else {
         String[] res = new String[arr.length];
         int i = 0;

         for(int c = res.length; i < c; ++i) {
            Object o = arr[i];
            if (o instanceof PropertyAccess) {
               XMPProperty prop = ((PropertyAccess)o).getValueProperty();
               res[i] = prop.getValue().toString();
            } else {
               res[i] = o.toString();
            }
         }

         return res;
      }
   }

   protected Date[] getDateArray(String propName) {
      Object[] arr = this.getObjectArray(propName);
      if (arr == null) {
         return null;
      } else {
         Date[] res = new Date[arr.length];
         int i = 0;

         for(int c = res.length; i < c; ++i) {
            Object obj = arr[i];
            if (obj instanceof Date) {
               res[i] = (Date)((Date)obj).clone();
            } else {
               res[i] = parseISO8601Date(obj.toString());
            }
         }

         return res;
      }
   }
}
