package org.apache.xerces.util;

public final class SecurityManager {
   private static final int DEFAULT_ENTITY_EXPANSION_LIMIT = 100000;
   private static final int DEFAULT_MAX_OCCUR_NODE_LIMIT = 3000;
   private int entityExpansionLimit = 100000;
   private int maxOccurLimit = 3000;

   public void setEntityExpansionLimit(int var1) {
      this.entityExpansionLimit = var1;
   }

   public int getEntityExpansionLimit() {
      return this.entityExpansionLimit;
   }

   public void setMaxOccurNodeLimit(int var1) {
      this.maxOccurLimit = var1;
   }

   public int getMaxOccurNodeLimit() {
      return this.maxOccurLimit;
   }
}
