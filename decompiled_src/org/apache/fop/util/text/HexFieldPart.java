package org.apache.fop.util.text;

import java.util.Map;

public class HexFieldPart implements AdvancedMessageFormat.Part {
   private String fieldName;

   public HexFieldPart(String fieldName) {
      this.fieldName = fieldName;
   }

   public boolean isGenerated(Map params) {
      Object obj = params.get(this.fieldName);
      return obj != null;
   }

   public void write(StringBuffer sb, Map params) {
      if (!params.containsKey(this.fieldName)) {
         throw new IllegalArgumentException("Message pattern contains unsupported field name: " + this.fieldName);
      } else {
         Object obj = params.get(this.fieldName);
         if (obj instanceof Character) {
            sb.append(Integer.toHexString((Character)obj));
         } else {
            if (!(obj instanceof Number)) {
               throw new IllegalArgumentException("Incompatible value for hex field part: " + obj.getClass().getName());
            }

            sb.append(Integer.toHexString(((Number)obj).intValue()));
         }

      }
   }

   public String toString() {
      return "{" + this.fieldName + ",hex}";
   }

   public static class Factory implements AdvancedMessageFormat.PartFactory {
      public AdvancedMessageFormat.Part newPart(String fieldName, String values) {
         return new HexFieldPart(fieldName);
      }

      public String getFormat() {
         return "hex";
      }
   }
}
