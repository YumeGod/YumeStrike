package org.apache.xerces.impl.xs.models;

import java.util.Vector;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.xni.QName;

public class XSEmptyCM implements XSCMValidator {
   private static final short STATE_START = 0;
   private static final Vector EMPTY = new Vector(0);

   public int[] startContentModel() {
      return new int[]{0};
   }

   public Object oneTransition(QName var1, int[] var2, SubstitutionGroupHandler var3) {
      if (var2[0] < 0) {
         var2[0] = -2;
         return null;
      } else {
         var2[0] = -1;
         return null;
      }
   }

   public boolean endContentModel(int[] var1) {
      boolean var2 = false;
      int var3 = var1[0];
      return var3 >= 0;
   }

   public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler var1) throws XMLSchemaException {
      return false;
   }

   public Vector whatCanGoHere(int[] var1) {
      return EMPTY;
   }
}
