package org.apache.xerces.impl.xs.models;

import java.util.Vector;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.xni.QName;

public class XSAllCM implements XSCMValidator {
   private static final short STATE_START = 0;
   private static final short STATE_VALID = 1;
   private static final short STATE_CHILD = 1;
   private XSElementDecl[] fAllElements;
   private boolean[] fIsOptionalElement;
   private boolean fHasOptionalContent = false;
   private int fNumElements = 0;

   public XSAllCM(boolean var1, int var2) {
      this.fHasOptionalContent = var1;
      this.fAllElements = new XSElementDecl[var2];
      this.fIsOptionalElement = new boolean[var2];
   }

   public void addElement(XSElementDecl var1, boolean var2) {
      this.fAllElements[this.fNumElements] = var1;
      this.fIsOptionalElement[this.fNumElements] = var2;
      ++this.fNumElements;
   }

   public int[] startContentModel() {
      int[] var1 = new int[this.fNumElements + 1];

      for(int var2 = 0; var2 <= this.fNumElements; ++var2) {
         var1[var2] = 0;
      }

      return var1;
   }

   Object findMatchingDecl(QName var1, SubstitutionGroupHandler var2) {
      XSElementDecl var3 = null;

      for(int var4 = 0; var4 < this.fNumElements; ++var4) {
         var3 = var2.getMatchingElemDecl(var1, this.fAllElements[var4]);
         if (var3 != null) {
            break;
         }
      }

      return var3;
   }

   public Object oneTransition(QName var1, int[] var2, SubstitutionGroupHandler var3) {
      if (var2[0] < 0) {
         var2[0] = -2;
         return this.findMatchingDecl(var1, var3);
      } else {
         var2[0] = 1;
         XSElementDecl var4 = null;

         for(int var5 = 0; var5 < this.fNumElements; ++var5) {
            if (var2[var5 + 1] == 0) {
               var4 = var3.getMatchingElemDecl(var1, this.fAllElements[var5]);
               if (var4 != null) {
                  var2[var5 + 1] = 1;
                  return var4;
               }
            }
         }

         var2[0] = -1;
         return this.findMatchingDecl(var1, var3);
      }
   }

   public boolean endContentModel(int[] var1) {
      int var2 = var1[0];
      if (var2 != -1 && var2 != -2) {
         if (this.fHasOptionalContent && var2 == 0) {
            return true;
         } else {
            for(int var3 = 0; var3 < this.fNumElements; ++var3) {
               if (!this.fIsOptionalElement[var3] && var1[var3 + 1] == 0) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler var1) throws XMLSchemaException {
      for(int var2 = 0; var2 < this.fNumElements; ++var2) {
         for(int var3 = var2 + 1; var3 < this.fNumElements; ++var3) {
            if (XSConstraints.overlapUPA(this.fAllElements[var2], this.fAllElements[var3], var1)) {
               throw new XMLSchemaException("cos-nonambig", new Object[]{this.fAllElements[var2].toString(), this.fAllElements[var3].toString()});
            }
         }
      }

      return false;
   }

   public Vector whatCanGoHere(int[] var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < this.fNumElements; ++var3) {
         if (var1[var3 + 1] == 0) {
            var2.addElement(this.fAllElements[var3]);
         }
      }

      return var2;
   }
}
