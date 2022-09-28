package org.apache.james.mime4j.stream;

import org.apache.james.mime4j.util.LangUtils;

public final class NameValuePair {
   private final String name;
   private final String value;

   public NameValuePair(String name, String value) {
      if (name == null) {
         throw new IllegalArgumentException("Name may not be null");
      } else {
         this.name = name;
         this.value = value;
      }
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public String toString() {
      if (this.value == null) {
         return this.name;
      } else {
         StringBuilder buffer = new StringBuilder();
         buffer.append(this.name);
         buffer.append("=");
         buffer.append("\"");
         buffer.append(this.value);
         buffer.append("\"");
         return buffer.toString();
      }
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (!(object instanceof NameValuePair)) {
         return false;
      } else {
         NameValuePair that = (NameValuePair)object;
         return this.name.equals(that.name) && LangUtils.equals(this.value, that.value);
      }
   }

   public int hashCode() {
      int hash = 17;
      hash = LangUtils.hashCode(hash, this.name);
      hash = LangUtils.hashCode(hash, this.value);
      return hash;
   }
}
