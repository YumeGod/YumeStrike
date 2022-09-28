package org.apache.fop.traits;

import java.io.Serializable;

public abstract class TraitEnum implements Serializable {
   private String name;
   private int enumValue;

   protected TraitEnum(String name, int enumValue) {
      this.name = name;
      this.enumValue = enumValue;
   }

   public String getName() {
      return this.name;
   }

   public int getEnumValue() {
      return this.enumValue;
   }
}
