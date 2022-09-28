package org.apache.fop.render.pcl;

import java.io.ObjectStreamException;
import java.io.Serializable;

public final class PCLRenderingMode implements Serializable {
   private static final long serialVersionUID = 6359884255324755026L;
   public static final PCLRenderingMode QUALITY = new PCLRenderingMode("quality");
   public static final PCLRenderingMode SPEED = new PCLRenderingMode("speed");
   public static final PCLRenderingMode BITMAP = new PCLRenderingMode("bitmap");
   private String name;

   private PCLRenderingMode(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public static PCLRenderingMode valueOf(String name) {
      if (QUALITY.getName().equalsIgnoreCase(name)) {
         return QUALITY;
      } else if (SPEED.getName().equalsIgnoreCase(name)) {
         return SPEED;
      } else if (BITMAP.getName().equalsIgnoreCase(name)) {
         return BITMAP;
      } else {
         throw new IllegalArgumentException("Illegal value for enumeration: " + name);
      }
   }

   private Object readResolve() throws ObjectStreamException {
      return valueOf(this.getName());
   }

   public String toString() {
      return "PCLRenderingMode:" + this.name;
   }
}
