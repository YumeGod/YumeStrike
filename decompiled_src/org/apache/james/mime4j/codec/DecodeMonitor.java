package org.apache.james.mime4j.codec;

public class DecodeMonitor {
   public static final DecodeMonitor STRICT = new DecodeMonitor() {
      public boolean warn(String error, String dropDesc) {
         return true;
      }

      public boolean isListening() {
         return true;
      }
   };
   public static final DecodeMonitor SILENT = new DecodeMonitor();

   public boolean warn(String error, String dropDesc) {
      return false;
   }

   public boolean isListening() {
      return false;
   }
}
