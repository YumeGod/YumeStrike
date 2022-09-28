package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xpath.XPath;
import org.apache.xerces.util.IntStack;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSTypeDefinition;

public class XPathMatcher {
   protected static final boolean DEBUG_ALL = false;
   protected static final boolean DEBUG_METHODS = false;
   protected static final boolean DEBUG_METHODS2 = false;
   protected static final boolean DEBUG_METHODS3 = false;
   protected static final boolean DEBUG_MATCH = false;
   protected static final boolean DEBUG_STACK = false;
   protected static final boolean DEBUG_ANY = false;
   protected static final int MATCHED = 1;
   protected static final int MATCHED_ATTRIBUTE = 3;
   protected static final int MATCHED_DESCENDANT = 5;
   protected static final int MATCHED_DESCENDANT_PREVIOUS = 13;
   private XPath.LocationPath[] fLocationPaths;
   private int[] fMatched;
   protected Object fMatchedString;
   private IntStack[] fStepIndexes;
   private int[] fCurrentStep;
   private int[] fNoMatchDepth;
   final QName fQName = new QName();

   public XPathMatcher(XPath var1) {
      this.fLocationPaths = var1.getLocationPaths();
      this.fStepIndexes = new IntStack[this.fLocationPaths.length];

      for(int var2 = 0; var2 < this.fStepIndexes.length; ++var2) {
         this.fStepIndexes[var2] = new IntStack();
      }

      this.fCurrentStep = new int[this.fLocationPaths.length];
      this.fNoMatchDepth = new int[this.fLocationPaths.length];
      this.fMatched = new int[this.fLocationPaths.length];
   }

   public boolean isMatched() {
      for(int var1 = 0; var1 < this.fLocationPaths.length; ++var1) {
         if ((this.fMatched[var1] & 1) == 1 && (this.fMatched[var1] & 13) != 13 && (this.fNoMatchDepth[var1] == 0 || (this.fMatched[var1] & 5) == 5)) {
            return true;
         }
      }

      return false;
   }

   protected void handleContent(XSTypeDefinition var1, boolean var2, Object var3, short var4, ShortList var5) {
   }

   protected void matched(Object var1, short var2, ShortList var3, boolean var4) {
   }

   public void startDocumentFragment() {
      this.fMatchedString = null;

      for(int var1 = 0; var1 < this.fLocationPaths.length; ++var1) {
         this.fStepIndexes[var1].clear();
         this.fCurrentStep[var1] = 0;
         this.fNoMatchDepth[var1] = 0;
         this.fMatched[var1] = 0;
      }

   }

   public void startElement(QName var1, XMLAttributes var2) {
      for(int var3 = 0; var3 < this.fLocationPaths.length; ++var3) {
         int var4 = this.fCurrentStep[var3];
         this.fStepIndexes[var3].push(var4);
         int var10002;
         if ((this.fMatched[var3] & 5) != 1 && this.fNoMatchDepth[var3] <= 0) {
            if ((this.fMatched[var3] & 5) == 5) {
               this.fMatched[var3] = 13;
            }

            XPath.Step[] var5;
            for(var5 = this.fLocationPaths[var3].steps; this.fCurrentStep[var3] < var5.length && var5[this.fCurrentStep[var3]].axis.type == 3; var10002 = this.fCurrentStep[var3]++) {
            }

            if (this.fCurrentStep[var3] == var5.length) {
               this.fMatched[var3] = 1;
            } else {
               int var6;
               for(var6 = this.fCurrentStep[var3]; this.fCurrentStep[var3] < var5.length && var5[this.fCurrentStep[var3]].axis.type == 4; var10002 = this.fCurrentStep[var3]++) {
               }

               boolean var7 = this.fCurrentStep[var3] > var6;
               if (this.fCurrentStep[var3] == var5.length) {
                  var10002 = this.fNoMatchDepth[var3]++;
               } else {
                  XPath.NodeTest var9;
                  if ((this.fCurrentStep[var3] == var4 || this.fCurrentStep[var3] > var6) && var5[this.fCurrentStep[var3]].axis.type == 1) {
                     XPath.Step var8 = var5[this.fCurrentStep[var3]];
                     var9 = var8.nodeTest;
                     if (var9.type == 1 && !var9.name.equals(var1)) {
                        if (this.fCurrentStep[var3] > var6) {
                           this.fCurrentStep[var3] = var6;
                        } else {
                           var10002 = this.fNoMatchDepth[var3]++;
                        }
                        continue;
                     }

                     var10002 = this.fCurrentStep[var3]++;
                  }

                  if (this.fCurrentStep[var3] == var5.length) {
                     if (var7) {
                        this.fCurrentStep[var3] = var6;
                        this.fMatched[var3] = 5;
                     } else {
                        this.fMatched[var3] = 1;
                     }
                  } else if (this.fCurrentStep[var3] < var5.length && var5[this.fCurrentStep[var3]].axis.type == 2) {
                     int var13 = var2.getLength();
                     if (var13 > 0) {
                        var9 = var5[this.fCurrentStep[var3]].nodeTest;

                        for(int var10 = 0; var10 < var13; ++var10) {
                           var2.getName(var10, this.fQName);
                           if (var9.type != 1 || var9.name.equals(this.fQName)) {
                              var10002 = this.fCurrentStep[var3]++;
                              if (this.fCurrentStep[var3] == var5.length) {
                                 this.fMatched[var3] = 3;

                                 int var11;
                                 for(var11 = 0; var11 < var3 && (this.fMatched[var11] & 1) != 1; ++var11) {
                                 }

                                 if (var11 == var3) {
                                    AttributePSVI var12 = (AttributePSVI)var2.getAugmentations(var10).getItem("ATTRIBUTE_PSVI");
                                    this.fMatchedString = var12.getActualNormalizedValue();
                                    this.matched(this.fMatchedString, var12.getActualNormalizedValueType(), var12.getItemValueTypes(), false);
                                 }
                              }
                              break;
                           }
                        }
                     }

                     if ((this.fMatched[var3] & 1) != 1) {
                        if (this.fCurrentStep[var3] > var6) {
                           this.fCurrentStep[var3] = var6;
                        } else {
                           var10002 = this.fNoMatchDepth[var3]++;
                        }
                     }
                  }
               }
            }
         } else {
            var10002 = this.fNoMatchDepth[var3]++;
         }
      }

   }

   public void endElement(QName var1, XSTypeDefinition var2, boolean var3, Object var4, short var5, ShortList var6) {
      for(int var7 = 0; var7 < this.fLocationPaths.length; ++var7) {
         this.fCurrentStep[var7] = this.fStepIndexes[var7].pop();
         if (this.fNoMatchDepth[var7] > 0) {
            int var10002 = this.fNoMatchDepth[var7]--;
         } else {
            int var8;
            for(var8 = 0; var8 < var7 && (this.fMatched[var8] & 1) != 1; ++var8) {
            }

            if (var8 >= var7 && this.fMatched[var8] != 0 && (this.fMatched[var8] & 3) != 3) {
               this.handleContent(var2, var3, var4, var5, var6);
               this.fMatched[var7] = 0;
            }
         }
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      String var2 = super.toString();
      int var3 = var2.lastIndexOf(46);
      if (var3 != -1) {
         var2 = var2.substring(var3 + 1);
      }

      var1.append(var2);

      for(int var4 = 0; var4 < this.fLocationPaths.length; ++var4) {
         var1.append('[');
         XPath.Step[] var5 = this.fLocationPaths[var4].steps;

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (var6 == this.fCurrentStep[var4]) {
               var1.append('^');
            }

            var1.append(var5[var6].toString());
            if (var6 < var5.length - 1) {
               var1.append('/');
            }
         }

         if (this.fCurrentStep[var4] == var5.length) {
            var1.append('^');
         }

         var1.append(']');
         var1.append(',');
      }

      return var1.toString();
   }

   private String normalize(String var1) {
      StringBuffer var2 = new StringBuffer();
      int var3 = var1.length();

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var1.charAt(var4);
         switch (var5) {
            case '\n':
               var2.append("\\n");
               break;
            default:
               var2.append(var5);
         }
      }

      return var2.toString();
   }
}
