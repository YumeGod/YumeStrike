package org.apache.fop.util.text;

import java.text.ChoiceFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChoiceFieldPart implements AdvancedMessageFormat.Part {
   private static final Pattern VARIABLE_REGEX = Pattern.compile("\\{([^\\}]+)\\}");
   private String fieldName;
   private ChoiceFormat choiceFormat;

   public ChoiceFieldPart(String fieldName, String choicesPattern) {
      this.fieldName = fieldName;
      this.choiceFormat = new ChoiceFormat(choicesPattern);
   }

   public boolean isGenerated(Map params) {
      Object obj = params.get(this.fieldName);
      return obj != null;
   }

   public void write(StringBuffer sb, Map params) {
      Object obj = params.get(this.fieldName);
      Number num = (Number)obj;
      String result = this.choiceFormat.format(num.doubleValue());
      Matcher m = VARIABLE_REGEX.matcher(result);
      if (m.find()) {
         AdvancedMessageFormat f = new AdvancedMessageFormat(result);
         f.format(params, sb);
      } else {
         sb.append(result);
      }

   }

   public String toString() {
      return "{" + this.fieldName + ",choice, ....}";
   }

   public static class Factory implements AdvancedMessageFormat.PartFactory {
      public AdvancedMessageFormat.Part newPart(String fieldName, String values) {
         return new ChoiceFieldPart(fieldName, values);
      }

      public String getFormat() {
         return "choice";
      }
   }
}
