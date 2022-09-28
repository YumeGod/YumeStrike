package org.apache.fop.util;

import java.util.List;

public final class ListUtil {
   private ListUtil() {
   }

   public static Object getLast(List list) {
      return list.get(list.size() - 1);
   }

   public static Object removeLast(List list) {
      return list.remove(list.size() - 1);
   }
}
