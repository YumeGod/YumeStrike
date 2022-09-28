package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSWildcard;

public class XSWildcardDecl implements XSWildcard {
   public static final String ABSENT = null;
   public short fType = 1;
   public short fProcessContents = 1;
   public String[] fNamespaceList;
   public XSAnnotationImpl fAnnotation = null;
   private String fDescription = null;

   public boolean allowNamespace(String var1) {
      if (this.fType == 1) {
         return true;
      } else {
         int var3;
         if (this.fType == 2) {
            boolean var2 = false;
            var3 = this.fNamespaceList.length;

            for(int var4 = 0; var4 < var3 && !var2; ++var4) {
               if (var1 == this.fNamespaceList[var4]) {
                  var2 = true;
               }
            }

            if (!var2) {
               return true;
            }
         }

         if (this.fType == 3) {
            int var5 = this.fNamespaceList.length;

            for(var3 = 0; var3 < var5; ++var3) {
               if (var1 == this.fNamespaceList[var3]) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean isSubsetOf(XSWildcardDecl var1) {
      if (var1 == null) {
         return false;
      } else if (var1.fType == 1) {
         return true;
      } else if (this.fType == 2 && var1.fType == 2 && this.fNamespaceList[0] == var1.fNamespaceList[0]) {
         return true;
      } else {
         if (this.fType == 3) {
            if (var1.fType == 3 && this.subset2sets(this.fNamespaceList, var1.fNamespaceList)) {
               return true;
            }

            if (var1.fType == 2 && !this.elementInSet(var1.fNamespaceList[0], this.fNamespaceList) && !this.elementInSet(ABSENT, this.fNamespaceList)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean weakerProcessContents(XSWildcardDecl var1) {
      return this.fProcessContents == 3 && var1.fProcessContents == 1 || this.fProcessContents == 2 && var1.fProcessContents != 2;
   }

   public XSWildcardDecl performUnionWith(XSWildcardDecl var1, short var2) {
      if (var1 == null) {
         return null;
      } else {
         XSWildcardDecl var3 = new XSWildcardDecl();
         var3.fProcessContents = var2;
         if (this.areSame(var1)) {
            var3.fType = this.fType;
            var3.fNamespaceList = this.fNamespaceList;
         } else if (this.fType != 1 && var1.fType != 1) {
            if (this.fType == 3 && var1.fType == 3) {
               var3.fType = 3;
               var3.fNamespaceList = this.union2sets(this.fNamespaceList, var1.fNamespaceList);
            } else if (this.fType == 2 && var1.fType == 2) {
               var3.fType = 2;
               var3.fNamespaceList = new String[2];
               var3.fNamespaceList[0] = ABSENT;
               var3.fNamespaceList[1] = ABSENT;
            } else if (this.fType == 2 && var1.fType == 3 || this.fType == 3 && var1.fType == 2) {
               String[] var4 = null;
               String[] var5 = null;
               if (this.fType == 2) {
                  var4 = this.fNamespaceList;
                  var5 = var1.fNamespaceList;
               } else {
                  var4 = var1.fNamespaceList;
                  var5 = this.fNamespaceList;
               }

               boolean var6 = this.elementInSet(ABSENT, var5);
               if (var4[0] != ABSENT) {
                  boolean var7 = this.elementInSet(var4[0], var5);
                  if (var7 && var6) {
                     var3.fType = 1;
                  } else if (var7 && !var6) {
                     var3.fType = 2;
                     var3.fNamespaceList = new String[2];
                     var3.fNamespaceList[0] = ABSENT;
                     var3.fNamespaceList[1] = ABSENT;
                  } else {
                     if (!var7 && var6) {
                        return null;
                     }

                     var3.fType = 2;
                     var3.fNamespaceList = var4;
                  }
               } else if (var6) {
                  var3.fType = 1;
               } else {
                  var3.fType = 2;
                  var3.fNamespaceList = var4;
               }
            }
         } else {
            var3.fType = 1;
         }

         return var3;
      }
   }

   public XSWildcardDecl performIntersectionWith(XSWildcardDecl var1, short var2) {
      if (var1 == null) {
         return null;
      } else {
         XSWildcardDecl var3 = new XSWildcardDecl();
         var3.fProcessContents = var2;
         if (this.areSame(var1)) {
            var3.fType = this.fType;
            var3.fNamespaceList = this.fNamespaceList;
         } else {
            XSWildcardDecl var4;
            if (this.fType != 1 && var1.fType != 1) {
               if (this.fType == 2 && var1.fType == 3 || this.fType == 3 && var1.fType == 2) {
                  var4 = null;
                  String[] var5 = null;
                  String[] var10;
                  if (this.fType == 2) {
                     var5 = this.fNamespaceList;
                     var10 = var1.fNamespaceList;
                  } else {
                     var5 = var1.fNamespaceList;
                     var10 = this.fNamespaceList;
                  }

                  int var6 = var10.length;
                  String[] var7 = new String[var6];
                  int var8 = 0;

                  for(int var9 = 0; var9 < var6; ++var9) {
                     if (var10[var9] != var5[0] && var10[var9] != ABSENT) {
                        var7[var8++] = var10[var9];
                     }
                  }

                  var3.fType = 3;
                  var3.fNamespaceList = new String[var8];
                  System.arraycopy(var7, 0, var3.fNamespaceList, 0, var8);
               } else if (this.fType == 3 && var1.fType == 3) {
                  var3.fType = 3;
                  var3.fNamespaceList = this.intersect2sets(this.fNamespaceList, var1.fNamespaceList);
               } else if (this.fType == 2 && var1.fType == 2) {
                  if (this.fNamespaceList[0] != ABSENT && var1.fNamespaceList[0] != ABSENT) {
                     return null;
                  }

                  var4 = this;
                  if (this.fNamespaceList[0] == ABSENT) {
                     var4 = var1;
                  }

                  var3.fType = var4.fType;
                  var3.fNamespaceList = var4.fNamespaceList;
               }
            } else {
               var4 = this;
               if (this.fType == 1) {
                  var4 = var1;
               }

               var3.fType = var4.fType;
               var3.fNamespaceList = var4.fNamespaceList;
            }
         }

         return var3;
      }
   }

   private boolean areSame(XSWildcardDecl var1) {
      if (this.fType == var1.fType) {
         if (this.fType == 1) {
            return true;
         }

         if (this.fType == 2) {
            return this.fNamespaceList[0] == var1.fNamespaceList[0];
         }

         if (this.fNamespaceList.length == var1.fNamespaceList.length) {
            for(int var2 = 0; var2 < this.fNamespaceList.length; ++var2) {
               if (!this.elementInSet(this.fNamespaceList[var2], var1.fNamespaceList)) {
                  return false;
               }
            }

            return true;
         }
      }

      return false;
   }

   String[] intersect2sets(String[] var1, String[] var2) {
      String[] var3 = new String[Math.min(var1.length, var2.length)];
      int var4 = 0;

      for(int var5 = 0; var5 < var1.length; ++var5) {
         if (this.elementInSet(var1[var5], var2)) {
            var3[var4++] = var1[var5];
         }
      }

      String[] var6 = new String[var4];
      System.arraycopy(var3, 0, var6, 0, var4);
      return var6;
   }

   String[] union2sets(String[] var1, String[] var2) {
      String[] var3 = new String[var1.length];
      int var4 = 0;

      for(int var5 = 0; var5 < var1.length; ++var5) {
         if (!this.elementInSet(var1[var5], var2)) {
            var3[var4++] = var1[var5];
         }
      }

      String[] var6 = new String[var4 + var2.length];
      System.arraycopy(var3, 0, var6, 0, var4);
      System.arraycopy(var2, 0, var6, var4, var2.length);
      return var6;
   }

   boolean subset2sets(String[] var1, String[] var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (!this.elementInSet(var1[var3], var2)) {
            return false;
         }
      }

      return true;
   }

   boolean elementInSet(String var1, String[] var2) {
      boolean var3 = false;

      for(int var4 = 0; var4 < var2.length && !var3; ++var4) {
         if (var1 == var2[var4]) {
            var3 = true;
         }
      }

      return var3;
   }

   public String toString() {
      if (this.fDescription == null) {
         StringBuffer var1 = new StringBuffer();
         var1.append("WC[");
         switch (this.fType) {
            case 1:
               var1.append("##any");
               break;
            case 2:
               var1.append("##other");
               var1.append(":\"");
               if (this.fNamespaceList[0] != null) {
                  var1.append(this.fNamespaceList[0]);
               }

               var1.append("\"");
               break;
            case 3:
               if (this.fNamespaceList.length != 0) {
                  var1.append("\"");
                  if (this.fNamespaceList[0] != null) {
                     var1.append(this.fNamespaceList[0]);
                  }

                  var1.append("\"");

                  for(int var2 = 1; var2 < this.fNamespaceList.length; ++var2) {
                     var1.append(",\"");
                     if (this.fNamespaceList[var2] != null) {
                        var1.append(this.fNamespaceList[var2]);
                     }

                     var1.append("\"");
                  }
               }
         }

         var1.append("]");
         this.fDescription = var1.toString();
      }

      return this.fDescription;
   }

   public short getType() {
      return 9;
   }

   public String getName() {
      return null;
   }

   public String getNamespace() {
      return null;
   }

   public short getConstraintType() {
      return this.fType;
   }

   public StringList getNsConstraintList() {
      return new StringListImpl(this.fNamespaceList, this.fNamespaceList == null ? 0 : this.fNamespaceList.length);
   }

   public short getProcessContents() {
      return this.fProcessContents;
   }

   public String getProcessContentsAsString() {
      switch (this.fProcessContents) {
         case 1:
            return "strict";
         case 2:
            return "skip";
         case 3:
            return "lax";
         default:
            return "invalid value";
      }
   }

   public XSAnnotation getAnnotation() {
      return this.fAnnotation;
   }

   public XSNamespaceItem getNamespaceItem() {
      return null;
   }
}
