package org.apache.fop.fonts.substitute;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class AttributeValue extends ArrayList {
   private static final long serialVersionUID = 748610847500940557L;

   public static AttributeValue valueOf(String valuesString) {
      AttributeValue attribute = new AttributeValue();
      StringTokenizer stringTokenizer = new StringTokenizer(valuesString, ",");
      String token;
      if (stringTokenizer.countTokens() > 1) {
         while(stringTokenizer.hasMoreTokens()) {
            token = stringTokenizer.nextToken().trim();
            AttributeValue tokenAttribute = valueOf(token);
            attribute.addAll(tokenAttribute);
         }
      } else {
         token = stringTokenizer.nextToken().trim();
         Object value = null;

         try {
            value = Integer.valueOf(token);
         } catch (NumberFormatException var6) {
            value = FontWeightRange.valueOf(token);
            if (value == null) {
               value = token;
            }
         }

         if (value != null) {
            attribute.add(value);
         }
      }

      return attribute;
   }
}
