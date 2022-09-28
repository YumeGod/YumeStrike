package org.apache.fop.util.text;

import java.util.Map;

public class IfFieldPart implements AdvancedMessageFormat.Part {
   protected String fieldName;
   protected String ifValue;
   protected String elseValue;

   public IfFieldPart(String fieldName, String values) {
      this.fieldName = fieldName;
      this.parseValues(values);
   }

   protected void parseValues(String values) {
      String[] parts = AdvancedMessageFormat.COMMA_SEPARATOR_REGEX.split(values, 2);
      if (parts.length == 2) {
         this.ifValue = AdvancedMessageFormat.unescapeComma(parts[0]);
         this.elseValue = AdvancedMessageFormat.unescapeComma(parts[1]);
      } else {
         this.ifValue = AdvancedMessageFormat.unescapeComma(values);
      }

   }

   public void write(StringBuffer sb, Map params) {
      boolean isTrue = this.isTrue(params);
      if (isTrue) {
         sb.append(this.ifValue);
      } else if (this.elseValue != null) {
         sb.append(this.elseValue);
      }

   }

   protected boolean isTrue(Map params) {
      Object obj = params.get(this.fieldName);
      if (obj instanceof Boolean) {
         return (Boolean)obj;
      } else {
         return obj != null;
      }
   }

   public boolean isGenerated(Map params) {
      return this.isTrue(params) || this.elseValue != null;
   }

   public String toString() {
      return "{" + this.fieldName + ", if...}";
   }

   public static class Factory implements AdvancedMessageFormat.PartFactory {
      public AdvancedMessageFormat.Part newPart(String fieldName, String values) {
         return new IfFieldPart(fieldName, values);
      }

      public String getFormat() {
         return "if";
      }
   }
}
