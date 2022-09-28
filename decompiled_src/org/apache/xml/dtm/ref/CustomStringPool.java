package org.apache.xml.dtm.ref;

import java.util.Hashtable;

public class CustomStringPool extends DTMStringPool {
   final Hashtable m_stringToInt = new Hashtable();
   public static final int NULL = -1;

   public void removeAllElements() {
      super.m_intToString.removeAllElements();
      if (this.m_stringToInt != null) {
         this.m_stringToInt.clear();
      }

   }

   public String indexToString(int i) throws ArrayIndexOutOfBoundsException {
      return (String)super.m_intToString.elementAt(i);
   }

   public int stringToIndex(String s) {
      if (s == null) {
         return -1;
      } else {
         Integer iobj = (Integer)this.m_stringToInt.get(s);
         if (iobj == null) {
            super.m_intToString.addElement(s);
            iobj = new Integer(super.m_intToString.size());
            this.m_stringToInt.put(s, iobj);
         }

         return iobj;
      }
   }
}
