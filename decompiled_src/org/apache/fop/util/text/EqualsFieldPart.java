package org.apache.fop.util.text;

import java.util.Map;

public class EqualsFieldPart extends IfFieldPart {
   private String equalsValue;

   public EqualsFieldPart(String fieldName, String values) {
      super(fieldName, values);
   }

   protected void parseValues(String values) {
      String[] parts = AdvancedMessageFormat.COMMA_SEPARATOR_REGEX.split(values, 3);
      this.equalsValue = parts[0];
      if (parts.length == 1) {
         throw new IllegalArgumentException("'equals' format must have at least 2 parameters");
      } else {
         if (parts.length == 3) {
            this.ifValue = AdvancedMessageFormat.unescapeComma(parts[1]);
            this.elseValue = AdvancedMessageFormat.unescapeComma(parts[2]);
         } else {
            this.ifValue = AdvancedMessageFormat.unescapeComma(parts[1]);
         }

      }
   }

   protected boolean isTrue(Map params) {
      Object obj = params.get(this.fieldName);
      return obj != null ? String.valueOf(obj).equals(this.equalsValue) : false;
   }

   public String toString() {
      return "{" + this.fieldName + ", equals " + this.equalsValue + "}";
   }

   public static class Factory implements AdvancedMessageFormat.PartFactory {
      public AdvancedMessageFormat.Part newPart(String fieldName, String values) {
         return new EqualsFieldPart(fieldName, values);
      }

      public String getFormat() {
         return "equals";
      }
   }
}
