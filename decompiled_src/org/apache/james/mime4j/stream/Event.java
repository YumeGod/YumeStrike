package org.apache.james.mime4j.stream;

public final class Event {
   public static final Event MIME_BODY_PREMATURE_END = new Event("Body part ended prematurely. Boundary detected in header or EOF reached.");
   public static final Event HEADERS_PREMATURE_END = new Event("Unexpected end of headers detected. Higher level boundary detected or EOF reached.");
   public static final Event INVALID_HEADER = new Event("Invalid header encountered");
   public static final Event OBSOLETE_HEADER = new Event("Obsolete header encountered");
   private final String code;

   public Event(String code) {
      if (code == null) {
         throw new IllegalArgumentException("Code may not be null");
      } else {
         this.code = code;
      }
   }

   public int hashCode() {
      return this.code.hashCode();
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (this == obj) {
         return true;
      } else if (obj instanceof Event) {
         Event that = (Event)obj;
         return this.code.equals(that.code);
      } else {
         return false;
      }
   }

   public String toString() {
      return this.code;
   }
}
