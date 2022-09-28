package org.apache.fop.pdf;

import java.util.Comparator;

public class DestinationComparator implements Comparator {
   public int compare(Object obj1, Object obj2) {
      if (obj1 instanceof PDFDestination && obj2 instanceof PDFDestination) {
         PDFDestination dest1 = (PDFDestination)obj1;
         PDFDestination dest2 = (PDFDestination)obj2;
         return dest1.getIDRef().compareTo(dest2.getIDRef());
      } else {
         return 0;
      }
   }
}
