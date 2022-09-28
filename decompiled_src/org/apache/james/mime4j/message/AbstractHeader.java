package org.apache.james.mime4j.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.stream.Field;

public abstract class AbstractHeader implements Header {
   private List fields = new LinkedList();
   private Map fieldMap = new HashMap();

   public AbstractHeader() {
   }

   public AbstractHeader(Header other) {
      Iterator i$ = other.getFields().iterator();

      while(i$.hasNext()) {
         Field otherField = (Field)i$.next();
         this.addField(otherField);
      }

   }

   public void addField(Field field) {
      List values = (List)this.fieldMap.get(field.getName().toLowerCase());
      if (values == null) {
         values = new LinkedList();
         this.fieldMap.put(field.getName().toLowerCase(), values);
      }

      ((List)values).add(field);
      this.fields.add(field);
   }

   public List getFields() {
      return Collections.unmodifiableList(this.fields);
   }

   public Field getField(String name) {
      List l = (List)this.fieldMap.get(name.toLowerCase());
      return l != null && !l.isEmpty() ? (Field)l.get(0) : null;
   }

   public List getFields(String name) {
      String lowerCaseName = name.toLowerCase();
      List l = (List)this.fieldMap.get(lowerCaseName);
      List results;
      if (l != null && !l.isEmpty()) {
         results = Collections.unmodifiableList(l);
      } else {
         results = Collections.emptyList();
      }

      return results;
   }

   public Iterator iterator() {
      return Collections.unmodifiableList(this.fields).iterator();
   }

   public int removeFields(String name) {
      String lowerCaseName = name.toLowerCase();
      List removed = (List)this.fieldMap.remove(lowerCaseName);
      if (removed != null && !removed.isEmpty()) {
         Iterator iterator = this.fields.iterator();

         while(iterator.hasNext()) {
            Field field = (Field)iterator.next();
            if (field.getName().equalsIgnoreCase(name)) {
               iterator.remove();
            }
         }

         return removed.size();
      } else {
         return 0;
      }
   }

   public void setField(Field field) {
      String lowerCaseName = field.getName().toLowerCase();
      List l = (List)this.fieldMap.get(lowerCaseName);
      if (l != null && !l.isEmpty()) {
         l.clear();
         l.add(field);
         int firstOccurrence = -1;
         int index = 0;

         for(Iterator iterator = this.fields.iterator(); iterator.hasNext(); ++index) {
            Field f = (Field)iterator.next();
            if (f.getName().equalsIgnoreCase(field.getName())) {
               iterator.remove();
               if (firstOccurrence == -1) {
                  firstOccurrence = index;
               }
            }
         }

         this.fields.add(firstOccurrence, field);
      } else {
         this.addField(field);
      }
   }

   public String toString() {
      StringBuilder str = new StringBuilder(128);
      Iterator i$ = this.fields.iterator();

      while(i$.hasNext()) {
         Field field = (Field)i$.next();
         str.append(field.toString());
         str.append("\r\n");
      }

      return str.toString();
   }
}
