package org.apache.fop.fonts;

import java.io.ObjectStreamException;
import java.io.Serializable;

public final class EncodingMode implements Serializable {
   private static final long serialVersionUID = 8311486102457779529L;
   public static final EncodingMode AUTO = new EncodingMode("auto");
   public static final EncodingMode SINGLE_BYTE = new EncodingMode("single-byte");
   public static final EncodingMode CID = new EncodingMode("cid");
   private String name;

   private EncodingMode(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public static EncodingMode valueOf(String name) {
      if (name.equalsIgnoreCase(AUTO.getName())) {
         return AUTO;
      } else if (name.equalsIgnoreCase(SINGLE_BYTE.getName())) {
         return SINGLE_BYTE;
      } else if (name.equalsIgnoreCase(CID.getName())) {
         return CID;
      } else {
         throw new IllegalArgumentException("Invalid encoding mode: " + name);
      }
   }

   private Object readResolve() throws ObjectStreamException {
      return valueOf(this.getName());
   }

   public String toString() {
      return "EncodingMode:" + this.getName();
   }
}
