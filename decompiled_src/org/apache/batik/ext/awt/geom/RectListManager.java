package org.apache.batik.ext.awt.geom;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class RectListManager implements Collection {
   Rectangle[] rects;
   int size;
   Rectangle bounds;
   public static Comparator comparator = new RectXComparator();
   // $FF: synthetic field
   static Class class$java$lang$Object;
   // $FF: synthetic field
   static Class class$java$awt$Rectangle;

   public void dump() {
      System.err.println("RLM: " + this + " Sz: " + this.size);
      System.err.println("Bounds: " + this.getBounds());

      for(int var1 = 0; var1 < this.size; ++var1) {
         Rectangle var2 = this.rects[var1];
         System.err.println("  [" + var2.x + ", " + var2.y + ", " + var2.width + ", " + var2.height + ']');
      }

   }

   public RectListManager(Collection var1) {
      this.rects = null;
      this.size = 0;
      this.bounds = null;
      this.rects = new Rectangle[var1.size()];
      Iterator var2 = var1.iterator();

      for(int var3 = 0; var2.hasNext(); this.rects[var3++] = (Rectangle)var2.next()) {
      }

      this.size = this.rects.length;
      Arrays.sort(this.rects, comparator);
   }

   public RectListManager(Rectangle[] var1) {
      this(var1, 0, var1.length);
   }

   public RectListManager(Rectangle[] var1, int var2, int var3) {
      this.rects = null;
      this.size = 0;
      this.bounds = null;
      this.size = var3;
      this.rects = new Rectangle[var3];
      System.arraycopy(var1, var2, this.rects, 0, var3);
      Arrays.sort(this.rects, comparator);
   }

   public RectListManager(RectListManager var1) {
      this(var1.rects);
   }

   public RectListManager(Rectangle var1) {
      this();
      this.add(var1);
   }

   public RectListManager() {
      this.rects = null;
      this.size = 0;
      this.bounds = null;
      this.rects = new Rectangle[10];
      this.size = 0;
   }

   public RectListManager(int var1) {
      this.rects = null;
      this.size = 0;
      this.bounds = null;
      this.rects = new Rectangle[var1];
   }

   public Rectangle getBounds() {
      if (this.bounds != null) {
         return this.bounds;
      } else if (this.size == 0) {
         return null;
      } else {
         this.bounds = new Rectangle(this.rects[0]);

         for(int var1 = 1; var1 < this.size; ++var1) {
            Rectangle var2 = this.rects[var1];
            if (var2.x < this.bounds.x) {
               this.bounds.width = this.bounds.x + this.bounds.width - var2.x;
               this.bounds.x = var2.x;
            }

            if (var2.y < this.bounds.y) {
               this.bounds.height = this.bounds.y + this.bounds.height - var2.y;
               this.bounds.y = var2.y;
            }

            if (var2.x + var2.width > this.bounds.x + this.bounds.width) {
               this.bounds.width = var2.x + var2.width - this.bounds.x;
            }

            if (var2.y + var2.height > this.bounds.y + this.bounds.height) {
               this.bounds.height = var2.y + var2.height - this.bounds.y;
            }
         }

         return this.bounds;
      }
   }

   public Object clone() throws CloneNotSupportedException {
      return this.copy();
   }

   public RectListManager copy() {
      return new RectListManager(this.rects);
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public void clear() {
      Arrays.fill(this.rects, (Object)null);
      this.size = 0;
      this.bounds = null;
   }

   public Iterator iterator() {
      return new RLMIterator();
   }

   public ListIterator listIterator() {
      return new RLMIterator();
   }

   public Object[] toArray() {
      Rectangle[] var1 = new Rectangle[this.size];
      System.arraycopy(this.rects, 0, var1, 0, this.size);
      return var1;
   }

   public Object[] toArray(Object[] var1) {
      Class var2 = var1.getClass().getComponentType();
      if (var2 != (class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object) && var2 != (class$java$awt$Rectangle == null ? (class$java$awt$Rectangle = class$("java.awt.Rectangle")) : class$java$awt$Rectangle)) {
         Arrays.fill((Object[])var1, (Object)null);
         return (Object[])var1;
      } else {
         if (((Object[])var1).length < this.size) {
            var1 = new Rectangle[this.size];
         }

         System.arraycopy(this.rects, 0, var1, 0, this.size);
         Arrays.fill((Object[])var1, this.size, ((Object[])var1).length, (Object)null);
         return (Object[])var1;
      }
   }

   public boolean add(Object var1) {
      this.add((Rectangle)var1);
      return true;
   }

   public void add(Rectangle var1) {
      this.add(var1, 0, this.size - 1);
   }

   protected void add(Rectangle var1, int var2, int var3) {
      this.ensureCapacity(this.size + 1);
      int var4 = var2;

      while(var2 <= var3) {
         for(var4 = (var2 + var3) / 2; this.rects[var4] == null && var4 < var3; ++var4) {
         }

         if (this.rects[var4] == null) {
            var3 = (var2 + var3) / 2;
            var4 = (var2 + var3) / 2;
            if (var2 > var3) {
               var4 = var2;
            }

            while(this.rects[var4] == null && var4 > var2) {
               --var4;
            }

            if (this.rects[var4] == null) {
               this.rects[var4] = var1;
               return;
            }
         }

         if (var1.x == this.rects[var4].x) {
            break;
         }

         if (var1.x < this.rects[var4].x) {
            if (var4 == 0 || this.rects[var4 - 1] != null && var1.x >= this.rects[var4 - 1].x) {
               break;
            }

            var3 = var4 - 1;
         } else {
            if (var4 == this.size - 1) {
               ++var4;
               break;
            }

            if (this.rects[var4 + 1] != null && var1.x <= this.rects[var4 + 1].x) {
               ++var4;
               break;
            }

            var2 = var4 + 1;
         }
      }

      if (var4 < this.size) {
         System.arraycopy(this.rects, var4, this.rects, var4 + 1, this.size - var4);
      }

      this.rects[var4] = var1;
      ++this.size;
      this.bounds = null;
   }

   public boolean addAll(Collection var1) {
      if (var1 instanceof RectListManager) {
         this.add((RectListManager)var1);
      } else {
         this.add(new RectListManager(var1));
      }

      return var1.size() != 0;
   }

   public boolean contains(Object var1) {
      Rectangle var2 = (Rectangle)var1;
      int var3 = 0;
      int var4 = this.size - 1;
      int var5 = 0;

      while(var3 <= var4) {
         var5 = var3 + var4 >>> 1;
         if (var2.x == this.rects[var5].x) {
            break;
         }

         if (var2.x < this.rects[var5].x) {
            if (var5 == 0 || var2.x >= this.rects[var5 - 1].x) {
               break;
            }

            var4 = var5 - 1;
         } else {
            if (var5 == this.size - 1) {
               ++var5;
               break;
            }

            if (var2.x <= this.rects[var5 + 1].x) {
               ++var5;
               break;
            }

            var3 = var5 + 1;
         }
      }

      if (this.rects[var5].x != var2.x) {
         return false;
      } else {
         int var6;
         for(var6 = var5; var6 >= 0; --var6) {
            if (this.rects[var5].equals(var2)) {
               return true;
            }

            if (this.rects[var5].x != var2.x) {
               break;
            }
         }

         for(var6 = var5 + 1; var6 < this.size; ++var6) {
            if (this.rects[var5].equals(var2)) {
               return true;
            }

            if (this.rects[var5].x != var2.x) {
               break;
            }
         }

         return false;
      }
   }

   public boolean containsAll(Collection var1) {
      return var1 instanceof RectListManager ? this.containsAll((RectListManager)var1) : this.containsAll(new RectListManager(var1));
   }

   public boolean containsAll(RectListManager var1) {
      int var3 = 0;
      int var4 = 0;

      for(boolean var5 = false; var4 < var1.size; ++var4) {
         int var6 = var3;

         while(this.rects[var6].x < var1.rects[var4].x) {
            ++var6;
            if (var6 == this.size) {
               return false;
            }
         }

         var3 = var6;
         int var2 = this.rects[var6].x;

         while(!var1.rects[var4].equals(this.rects[var6])) {
            ++var6;
            if (var6 == this.size) {
               return false;
            }

            if (var2 != this.rects[var6].x) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean remove(Object var1) {
      return this.remove((Rectangle)var1);
   }

   public boolean remove(Rectangle var1) {
      int var2 = 0;
      int var3 = this.size - 1;
      int var4 = 0;

      while(var2 <= var3) {
         var4 = var2 + var3 >>> 1;
         if (var1.x == this.rects[var4].x) {
            break;
         }

         if (var1.x < this.rects[var4].x) {
            if (var4 == 0 || var1.x >= this.rects[var4 - 1].x) {
               break;
            }

            var3 = var4 - 1;
         } else {
            if (var4 == this.size - 1) {
               ++var4;
               break;
            }

            if (var1.x <= this.rects[var4 + 1].x) {
               ++var4;
               break;
            }

            var2 = var4 + 1;
         }
      }

      if (this.rects[var4].x != var1.x) {
         return false;
      } else {
         int var5;
         for(var5 = var4; var5 >= 0; --var5) {
            if (this.rects[var4].equals(var1)) {
               System.arraycopy(this.rects, var4 + 1, this.rects, var4, this.size - var4);
               --this.size;
               this.bounds = null;
               return true;
            }

            if (this.rects[var4].x != var1.x) {
               break;
            }
         }

         for(var5 = var4 + 1; var5 < this.size; ++var5) {
            if (this.rects[var4].equals(var1)) {
               System.arraycopy(this.rects, var4 + 1, this.rects, var4, this.size - var4);
               --this.size;
               this.bounds = null;
               return true;
            }

            if (this.rects[var4].x != var1.x) {
               break;
            }
         }

         return false;
      }
   }

   public boolean removeAll(Collection var1) {
      return var1 instanceof RectListManager ? this.removeAll((RectListManager)var1) : this.removeAll(new RectListManager(var1));
   }

   public boolean removeAll(RectListManager var1) {
      int var3 = 0;
      boolean var4 = false;
      int var5 = 0;

      int var7;
      for(boolean var6 = false; var5 < var1.size; ++var5) {
         var7 = var3;

         while(this.rects[var7] == null || this.rects[var7].x < var1.rects[var5].x) {
            ++var7;
            if (var7 == this.size) {
               break;
            }
         }

         if (var7 == this.size) {
            break;
         }

         var3 = var7;
         int var2 = this.rects[var7].x;

         while(true) {
            if (this.rects[var7] == null) {
               ++var7;
               if (var7 == this.size) {
                  break;
               }
            } else {
               if (var1.rects[var5].equals(this.rects[var7])) {
                  this.rects[var7] = null;
                  var4 = true;
               }

               ++var7;
               if (var7 == this.size || var2 != this.rects[var7].x) {
                  break;
               }
            }
         }
      }

      if (var4) {
         var5 = 0;

         for(var7 = 0; var7 < this.size; ++var7) {
            if (this.rects[var7] != null) {
               this.rects[var5++] = this.rects[var7];
            }
         }

         this.size = var5;
         this.bounds = null;
      }

      return var4;
   }

   public boolean retainAll(Collection var1) {
      return var1 instanceof RectListManager ? this.retainAll((RectListManager)var1) : this.retainAll(new RectListManager(var1));
   }

   public boolean retainAll(RectListManager var1) {
      int var3 = 0;
      boolean var4 = false;
      int var5 = 0;

      int var8;
      for(boolean var6 = false; var5 < this.size; ++var5) {
         var8 = var3;

         while(var1.rects[var8].x < this.rects[var5].x) {
            ++var8;
            if (var8 == var1.size) {
               break;
            }
         }

         if (var8 == var1.size) {
            var4 = true;

            for(int var7 = var5; var7 < this.size; ++var7) {
               this.rects[var7] = null;
            }

            this.size = var5;
            break;
         }

         var3 = var8;
         int var2 = var1.rects[var8].x;

         while(!this.rects[var5].equals(var1.rects[var8])) {
            ++var8;
            if (var8 == var1.size || var2 != var1.rects[var8].x) {
               this.rects[var5] = null;
               var4 = true;
               break;
            }
         }
      }

      if (var4) {
         var5 = 0;

         for(var8 = 0; var8 < this.size; ++var8) {
            if (this.rects[var8] != null) {
               this.rects[var5++] = this.rects[var8];
            }
         }

         this.size = var5;
         this.bounds = null;
      }

      return var4;
   }

   public void add(RectListManager var1) {
      if (var1.size != 0) {
         Rectangle[] var2 = this.rects;
         if (this.rects.length < this.size + var1.size) {
            var2 = new Rectangle[this.size + var1.size];
         }

         if (this.size == 0) {
            System.arraycopy(var1.rects, 0, var2, this.size, var1.size);
            this.size = var1.size;
            this.bounds = null;
         } else {
            Rectangle[] var3 = var1.rects;
            int var4 = var1.size;
            int var5 = var4 - 1;
            Rectangle[] var6 = this.rects;
            int var7 = this.size;
            int var8 = var7 - 1;
            int var9 = this.size + var1.size - 1;
            int var10 = var3[var5].x;

            for(int var11 = var6[var8].x; var9 >= 0; --var9) {
               if (var10 <= var11) {
                  var2[var9] = var6[var8];
                  if (var8 == 0) {
                     System.arraycopy(var3, 0, var2, 0, var5 + 1);
                     break;
                  }

                  --var8;
                  var11 = var6[var8].x;
               } else {
                  var2[var9] = var3[var5];
                  if (var5 == 0) {
                     System.arraycopy(var6, 0, var2, 0, var8 + 1);
                     break;
                  }

                  --var5;
                  var10 = var3[var5].x;
               }
            }

            this.rects = var2;
            this.size += var1.size;
            this.bounds = null;
         }
      }
   }

   public void mergeRects(int var1, int var2) {
      if (this.size != 0) {
         new Rectangle();
         Rectangle[] var9 = new Rectangle[4];

         Rectangle var3;
         int var10;
         int var11;
         for(var11 = 0; var11 < this.size; ++var11) {
            var3 = this.rects[var11];
            if (var3 != null) {
               int var6 = var1 + var3.height * var2 + var3.height * var3.width;

               do {
                  int var12 = var3.x + var3.width + var1 / var3.height;

                  for(var10 = var11 + 1; var10 < this.size; ++var10) {
                     Rectangle var4 = this.rects[var10];
                     if (var4 != null && var4 != var3) {
                        if (var4.x >= var12) {
                           var10 = this.size;
                           break;
                        }

                        int var7 = var1 + var4.height * var2 + var4.height * var4.width;
                        Rectangle var5 = var3.union(var4);
                        int var8 = var1 + var5.height * var2 + var5.height * var5.width;
                        if (var8 <= var6 + var7) {
                           var3 = this.rects[var11] = var5;
                           this.rects[var10] = null;
                           var6 = var8;
                           var10 = -1;
                           break;
                        }

                        if (var3.intersects(var4)) {
                           this.splitRect(var4, var3, var9);
                           int var13 = 0;
                           int var14 = 0;

                           for(int var15 = 0; var15 < 4; ++var15) {
                              if (var9[var15] != null) {
                                 Rectangle var16 = var9[var15];
                                 if (var15 < 3) {
                                    var9[var14++] = var16;
                                 }

                                 var13 += var1 + var16.height * var2 + var16.height * var16.width;
                              }
                           }

                           if (var13 < var7) {
                              if (var14 == 0) {
                                 this.rects[var10] = null;
                                 if (var9[3] != null) {
                                    this.add(var9[3], var10, this.size - 1);
                                 }
                              } else {
                                 this.rects[var10] = var9[0];
                                 if (var14 > 1) {
                                    this.insertRects(var9, 1, var10 + 1, var14 - 1);
                                 }

                                 if (var9[3] != null) {
                                    this.add(var9[3], var10, this.size - 1);
                                 }
                              }
                           }
                        }
                     }
                  }
               } while(var10 != this.size);
            }
         }

         var10 = 0;
         var11 = 0;

         float var17;
         for(var17 = 0.0F; var11 < this.size; ++var11) {
            if (this.rects[var11] != null) {
               var3 = this.rects[var11];
               this.rects[var10++] = var3;
               var17 += (float)(var1 + var3.height * var2 + var3.height * var3.width);
            }
         }

         this.size = var10;
         this.bounds = null;
         var3 = this.getBounds();
         if (var3 != null) {
            if ((float)(var1 + var3.height * var2 + var3.height * var3.width) < var17) {
               this.rects[0] = var3;
               this.size = 1;
            }

         }
      }
   }

   public void subtract(RectListManager var1, int var2, int var3) {
      int var7 = 0;
      Rectangle[] var8 = new Rectangle[4];

      int var9;
      int var10;
      for(var9 = 0; var9 < this.size; ++var9) {
         Rectangle var4 = this.rects[var9];
         int var6 = var2 + var4.height * var3 + var4.height * var4.width;

         for(var10 = var7; var10 < var1.size; ++var10) {
            Rectangle var5 = var1.rects[var10];
            if (var5.x + var5.width < var4.x) {
               if (var10 == var7) {
                  ++var7;
               }
            } else {
               if (var5.x > var4.x + var4.width) {
                  break;
               }

               if (var4.intersects(var5)) {
                  this.splitRect(var4, var5, var8);
                  int var11 = 0;

                  int var13;
                  for(var13 = 0; var13 < 4; ++var13) {
                     Rectangle var12 = var8[var13];
                     if (var12 != null) {
                        var11 += var2 + var12.height * var3 + var12.height * var12.width;
                     }
                  }

                  if (var11 < var6) {
                     var13 = 0;

                     for(int var14 = 0; var14 < 3; ++var14) {
                        if (var8[var14] != null) {
                           var8[var13++] = var8[var14];
                        }
                     }

                     if (var13 == 0) {
                        this.rects[var9].width = 0;
                        if (var8[3] != null) {
                           this.add(var8[3], var9, this.size - 1);
                        }
                        break;
                     }

                     var4 = var8[0];
                     this.rects[var9] = var4;
                     var6 = var2 + var4.height * var3 + var4.height * var4.width;
                     if (var13 > 1) {
                        this.insertRects(var8, 1, var9 + 1, var13 - 1);
                     }

                     if (var8[3] != null) {
                        this.add(var8[3], var9 + var13, this.size - 1);
                     }
                  }
               }
            }
         }
      }

      var9 = 0;

      for(var10 = 0; var10 < this.size; ++var10) {
         if (this.rects[var10].width == 0) {
            this.rects[var10] = null;
         } else {
            this.rects[var9++] = this.rects[var10];
         }
      }

      this.size = var9;
      this.bounds = null;
   }

   protected void splitRect(Rectangle var1, Rectangle var2, Rectangle[] var3) {
      int var4 = var1.x;
      int var5 = var4 + var1.width - 1;
      int var6 = var1.y;
      int var7 = var6 + var1.height - 1;
      int var8 = var2.x;
      int var9 = var8 + var2.width - 1;
      int var10 = var2.y;
      int var11 = var10 + var2.height - 1;
      if (var6 < var10 && var7 >= var10) {
         var3[0] = new Rectangle(var4, var6, var1.width, var10 - var6);
         var6 = var10;
      } else {
         var3[0] = null;
      }

      if (var6 <= var11 && var7 > var11) {
         var3[1] = new Rectangle(var4, var11 + 1, var1.width, var7 - var11);
         var7 = var11;
      } else {
         var3[1] = null;
      }

      if (var4 < var8 && var5 >= var8) {
         var3[2] = new Rectangle(var4, var6, var8 - var4, var7 - var6 + 1);
      } else {
         var3[2] = null;
      }

      if (var4 <= var9 && var5 > var9) {
         var3[3] = new Rectangle(var9 + 1, var6, var5 - var9, var7 - var6 + 1);
      } else {
         var3[3] = null;
      }

   }

   protected void insertRects(Rectangle[] var1, int var2, int var3, int var4) {
      if (var4 != 0) {
         this.ensureCapacity(this.size + var4);

         for(int var5 = this.size - 1; var5 >= var3; --var5) {
            this.rects[var5 + var4] = this.rects[var5];
         }

         System.arraycopy(var1, var2, this.rects, var3, var4);
         this.size += var4;
      }
   }

   public void ensureCapacity(int var1) {
      if (var1 > this.rects.length) {
         int var2;
         for(var2 = this.rects.length + (this.rects.length >> 1) + 1; var2 < var1; var2 += (var2 >> 1) + 1) {
         }

         Rectangle[] var3 = new Rectangle[var2];
         System.arraycopy(this.rects, 0, var3, 0, this.size);
         this.rects = var3;
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private class RLMIterator implements ListIterator {
      int idx = 0;
      boolean removeOk = false;
      boolean forward = true;

      RLMIterator() {
      }

      public boolean hasNext() {
         return this.idx < RectListManager.this.size;
      }

      public int nextIndex() {
         return this.idx;
      }

      public Object next() {
         if (this.idx >= RectListManager.this.size) {
            throw new NoSuchElementException("No Next Element");
         } else {
            this.forward = true;
            this.removeOk = true;
            return RectListManager.this.rects[this.idx++];
         }
      }

      public boolean hasPrevious() {
         return this.idx > 0;
      }

      public int previousIndex() {
         return this.idx - 1;
      }

      public Object previous() {
         if (this.idx <= 0) {
            throw new NoSuchElementException("No Previous Element");
         } else {
            this.forward = false;
            this.removeOk = true;
            return RectListManager.this.rects[--this.idx];
         }
      }

      public void remove() {
         if (!this.removeOk) {
            throw new IllegalStateException("remove can only be called directly after next/previous");
         } else {
            if (this.forward) {
               --this.idx;
            }

            if (this.idx != RectListManager.this.size - 1) {
               System.arraycopy(RectListManager.this.rects, this.idx + 1, RectListManager.this.rects, this.idx, RectListManager.this.size - (this.idx + 1));
            }

            --RectListManager.this.size;
            RectListManager.this.rects[RectListManager.this.size] = null;
            this.removeOk = false;
         }
      }

      public void set(Object var1) {
         Rectangle var2 = (Rectangle)var1;
         if (!this.removeOk) {
            throw new IllegalStateException("set can only be called directly after next/previous");
         } else {
            if (this.forward) {
               --this.idx;
            }

            if (this.idx + 1 < RectListManager.this.size && RectListManager.this.rects[this.idx + 1].x < var2.x) {
               throw new UnsupportedOperationException("RectListManager entries must be sorted");
            } else if (this.idx >= 0 && RectListManager.this.rects[this.idx - 1].x > var2.x) {
               throw new UnsupportedOperationException("RectListManager entries must be sorted");
            } else {
               RectListManager.this.rects[this.idx] = var2;
               this.removeOk = false;
            }
         }
      }

      public void add(Object var1) {
         Rectangle var2 = (Rectangle)var1;
         if (this.idx < RectListManager.this.size && RectListManager.this.rects[this.idx].x < var2.x) {
            throw new UnsupportedOperationException("RectListManager entries must be sorted");
         } else if (this.idx != 0 && RectListManager.this.rects[this.idx - 1].x > var2.x) {
            throw new UnsupportedOperationException("RectListManager entries must be sorted");
         } else {
            RectListManager.this.ensureCapacity(RectListManager.this.size + 1);
            if (this.idx != RectListManager.this.size) {
               System.arraycopy(RectListManager.this.rects, this.idx, RectListManager.this.rects, this.idx + 1, RectListManager.this.size - this.idx);
            }

            RectListManager.this.rects[this.idx] = var2;
            ++this.idx;
            this.removeOk = false;
         }
      }
   }

   private static class RectXComparator implements Comparator, Serializable {
      RectXComparator() {
      }

      public final int compare(Object var1, Object var2) {
         return ((Rectangle)var1).x - ((Rectangle)var2).x;
      }
   }
}
