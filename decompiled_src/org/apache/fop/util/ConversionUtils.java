package org.apache.fop.util;

public final class ConversionUtils {
   public static int[] toIntArray(String baseString, String separatorPattern) {
      if (baseString != null && !"".equals(baseString)) {
         if (separatorPattern != null && !"".equals(separatorPattern)) {
            String[] values = baseString.split(separatorPattern);
            int numValues = values.length;
            if (numValues == 0) {
               return null;
            } else {
               int[] returnArray = new int[numValues];

               for(int i = 0; i < numValues; ++i) {
                  returnArray[i] = Integer.parseInt(values[i]);
               }

               return returnArray;
            }
         } else {
            return new int[]{Integer.parseInt(baseString)};
         }
      } else {
         return null;
      }
   }

   public static double[] toDoubleArray(String baseString, String separatorPattern) {
      if (baseString != null && !"".equals(baseString)) {
         if (separatorPattern != null && !"".equals(separatorPattern)) {
            String[] values = baseString.split(separatorPattern);
            int numValues = values.length;
            if (numValues == 0) {
               return null;
            } else {
               double[] returnArray = new double[numValues];

               for(int i = 0; i < numValues; ++i) {
                  returnArray[i] = Double.parseDouble(values[i]);
               }

               return returnArray;
            }
         } else {
            return new double[]{Double.parseDouble(baseString)};
         }
      } else {
         return null;
      }
   }
}
