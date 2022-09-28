package org.apache.xmlgraphics.ps;

public class PSResource implements Comparable {
   public static final String TYPE_FILE = "file";
   public static final String TYPE_FONT = "font";
   public static final String TYPE_PROCSET = "procset";
   public static final String TYPE_PATTERN = "pattern";
   public static final String TYPE_FORM = "form";
   public static final String TYPE_ENCODING = "encoding";
   private String type;
   private String name;

   public PSResource(String type, String name) {
      this.type = type;
      this.name = name;
   }

   public String getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public String getResourceSpecification() {
      StringBuffer sb = new StringBuffer();
      sb.append(this.getType()).append(" ").append(PSGenerator.convertStringToDSC(this.getName()));
      return sb.toString();
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj instanceof PSResource) {
         PSResource other = (PSResource)obj;
         return other.toString().equals(this.toString());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public int compareTo(Object o) {
      PSResource other = (PSResource)o;
      if (this == other) {
         return 0;
      } else {
         int result = this.getType().compareTo(other.getType());
         if (result == 0) {
            result = this.getName().compareTo(other.getName());
         }

         return result;
      }
   }

   public String toString() {
      return this.getResourceSpecification();
   }
}
