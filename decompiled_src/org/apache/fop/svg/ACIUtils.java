package org.apache.fop.svg;

import java.text.AttributedCharacterIterator;
import java.util.Iterator;
import java.util.Map;

public final class ACIUtils {
   private ACIUtils() {
   }

   public static void dumpAttrs(AttributedCharacterIterator aci) {
      aci.first();
      Iterator i = aci.getAttributes().entrySet().iterator();

      while(i.hasNext()) {
         Map.Entry entry = (Map.Entry)i.next();
         if (entry.getValue() != null) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
         }
      }

      int start = aci.getBeginIndex();
      System.out.print("AttrRuns: ");

      while(aci.current() != '\uffff') {
         int end = aci.getRunLimit();
         System.out.print("" + (end - start) + ", ");
         aci.setIndex(end);
         if (start == end) {
            break;
         }

         start = end;
      }

      System.out.println("");
   }
}
