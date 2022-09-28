package org.apache.fop.render.afp;

import java.io.ObjectStreamException;
import java.io.Serializable;

public final class AFPShadingMode implements Serializable {
   private static final long serialVersionUID = 8579867898716480779L;
   public static final AFPShadingMode COLOR = new AFPShadingMode("COLOR");
   public static final AFPShadingMode DITHERED = new AFPShadingMode("DITHERED");
   private String name;

   private AFPShadingMode(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public static AFPShadingMode valueOf(String name) {
      if (COLOR.getName().equalsIgnoreCase(name)) {
         return COLOR;
      } else if (DITHERED.getName().equalsIgnoreCase(name)) {
         return DITHERED;
      } else {
         throw new IllegalArgumentException("Illegal value for enumeration: " + name);
      }
   }

   private Object readResolve() throws ObjectStreamException {
      return valueOf(this.getName());
   }

   public String toString() {
      return this.getClass().getName() + ":" + this.name;
   }
}
