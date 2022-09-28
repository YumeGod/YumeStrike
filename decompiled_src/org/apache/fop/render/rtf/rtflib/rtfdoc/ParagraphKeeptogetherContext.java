package org.apache.fop.render.rtf.rtflib.rtfdoc;

public class ParagraphKeeptogetherContext {
   private static int paraKeepTogetherOpen = 0;
   private static boolean paraResetProperties = false;
   private static ParagraphKeeptogetherContext instance = null;

   ParagraphKeeptogetherContext() {
   }

   public static ParagraphKeeptogetherContext getInstance() {
      if (instance == null) {
         instance = new ParagraphKeeptogetherContext();
      }

      return instance;
   }

   public static int getKeepTogetherOpenValue() {
      return paraKeepTogetherOpen;
   }

   public static void keepTogetherOpen() {
      ++paraKeepTogetherOpen;
   }

   public static void keepTogetherClose() {
      if (paraKeepTogetherOpen > 0) {
         --paraKeepTogetherOpen;
         paraResetProperties = paraKeepTogetherOpen == 0;
      }

   }

   public static boolean paragraphResetProperties() {
      return paraResetProperties;
   }

   public static void setParagraphResetPropertiesUsed() {
      paraResetProperties = false;
   }
}
