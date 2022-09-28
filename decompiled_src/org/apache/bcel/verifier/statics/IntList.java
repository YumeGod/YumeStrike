package org.apache.bcel.verifier.statics;

import java.util.ArrayList;

public class IntList {
   private ArrayList theList = new ArrayList();

   IntList() {
   }

   void add(int i) {
      this.theList.add(new Integer(i));
   }

   boolean contains(int i) {
      Integer[] ints = new Integer[this.theList.size()];
      this.theList.toArray(ints);

      for(int j = 0; j < ints.length; ++j) {
         if (i == ints[j]) {
            return true;
         }
      }

      return false;
   }
}
