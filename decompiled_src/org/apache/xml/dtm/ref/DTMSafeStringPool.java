package org.apache.xml.dtm.ref;

public class DTMSafeStringPool extends DTMStringPool {
   public synchronized void removeAllElements() {
      super.removeAllElements();
   }

   public synchronized String indexToString(int i) throws ArrayIndexOutOfBoundsException {
      return super.indexToString(i);
   }

   public synchronized int stringToIndex(String s) {
      return super.stringToIndex(s);
   }

   public static void main(String[] args) {
      String[] word = new String[]{"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Twenty-One", "Twenty-Two", "Twenty-Three", "Twenty-Four", "Twenty-Five", "Twenty-Six", "Twenty-Seven", "Twenty-Eight", "Twenty-Nine", "Thirty", "Thirty-One", "Thirty-Two", "Thirty-Three", "Thirty-Four", "Thirty-Five", "Thirty-Six", "Thirty-Seven", "Thirty-Eight", "Thirty-Nine"};
      DTMStringPool pool = new DTMSafeStringPool();
      System.out.println("If no complaints are printed below, we passed initial test.");

      for(int pass = 0; pass <= 1; ++pass) {
         int i;
         int j;
         for(i = 0; i < word.length; ++i) {
            j = pool.stringToIndex(word[i]);
            if (j != i) {
               System.out.println("\tMismatch populating pool: assigned " + j + " for create " + i);
            }
         }

         for(i = 0; i < word.length; ++i) {
            j = pool.stringToIndex(word[i]);
            if (j != i) {
               System.out.println("\tMismatch in stringToIndex: returned " + j + " for lookup " + i);
            }
         }

         for(i = 0; i < word.length; ++i) {
            String w = pool.indexToString(i);
            if (!word[i].equals(w)) {
               System.out.println("\tMismatch in indexToString: returned" + w + " for lookup " + i);
            }
         }

         pool.removeAllElements();
         System.out.println("\nPass " + pass + " complete\n");
      }

   }
}
