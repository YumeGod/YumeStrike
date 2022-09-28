package org.apache.james.mime4j.util;

public final class LangUtils {
   public static final int HASH_SEED = 17;
   public static final int HASH_OFFSET = 37;

   private LangUtils() {
   }

   public static int hashCode(int seed, int hashcode) {
      return seed * 37 + hashcode;
   }

   public static int hashCode(int seed, boolean b) {
      return hashCode(seed, b ? 1 : 0);
   }

   public static int hashCode(int seed, Object obj) {
      return hashCode(seed, obj != null ? obj.hashCode() : 0);
   }

   public static boolean equals(Object obj1, Object obj2) {
      return obj1 == null ? obj2 == null : obj1.equals(obj2);
   }

   public static boolean equalsIgnoreCase(String s1, String s2) {
      return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
   }
}
