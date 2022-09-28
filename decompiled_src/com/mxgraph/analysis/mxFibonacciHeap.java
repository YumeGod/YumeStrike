package com.mxgraph.analysis;

import java.util.Hashtable;
import java.util.Map;

public class mxFibonacciHeap {
   protected Map nodes = new Hashtable();
   protected Node min;
   protected int size;

   public Node getNode(Object var1, boolean var2) {
      Node var3 = (Node)this.nodes.get(var1);
      if (var3 == null && var2) {
         var3 = new Node(var1, Double.MAX_VALUE);
         this.nodes.put(var1, var3);
         this.insert(var3, var3.getKey());
      }

      return var3;
   }

   public boolean isEmpty() {
      return this.min == null;
   }

   public void decreaseKey(Node var1, double var2) {
      if (var2 > var1.key) {
         throw new IllegalArgumentException("decreaseKey() got larger key value");
      } else {
         var1.key = var2;
         Node var4 = var1.parent;
         if (var4 != null && var1.key < var4.key) {
            this.cut(var1, var4);
            this.cascadingCut(var4);
         }

         if (this.min == null || var1.key < this.min.key) {
            this.min = var1;
         }

      }
   }

   public void delete(Node var1) {
      this.decreaseKey(var1, Double.NEGATIVE_INFINITY);
      this.removeMin();
   }

   public void insert(Node var1, double var2) {
      var1.key = var2;
      if (this.min != null) {
         var1.left = this.min;
         var1.right = this.min.right;
         this.min.right = var1;
         var1.right.left = var1;
         if (var2 < this.min.key) {
            this.min = var1;
         }
      } else {
         this.min = var1;
      }

      ++this.size;
   }

   public Node min() {
      return this.min;
   }

   public Node removeMin() {
      Node var1 = this.min;
      if (var1 != null) {
         int var2 = var1.degree;

         for(Node var3 = var1.child; var2 > 0; --var2) {
            Node var4 = var3.right;
            var3.left.right = var3.right;
            var3.right.left = var3.left;
            var3.left = this.min;
            var3.right = this.min.right;
            this.min.right = var3;
            var3.right.left = var3;
            var3.parent = null;
            var3 = var4;
         }

         var1.left.right = var1.right;
         var1.right.left = var1.left;
         if (var1 == var1.right) {
            this.min = null;
         } else {
            this.min = var1.right;
            this.consolidate();
         }

         --this.size;
      }

      return var1;
   }

   public int size() {
      return this.size;
   }

   public static mxFibonacciHeap union(mxFibonacciHeap var0, mxFibonacciHeap var1) {
      mxFibonacciHeap var2 = new mxFibonacciHeap();
      if (var0 != null && var1 != null) {
         var2.min = var0.min;
         if (var2.min != null) {
            if (var1.min != null) {
               var2.min.right.left = var1.min.left;
               var1.min.left.right = var2.min.right;
               var2.min.right = var1.min;
               var1.min.left = var2.min;
               if (var1.min.key < var0.min.key) {
                  var2.min = var1.min;
               }
            }
         } else {
            var2.min = var1.min;
         }

         var2.size = var0.size + var1.size;
      }

      return var2;
   }

   protected void cascadingCut(Node var1) {
      Node var2 = var1.parent;
      if (var2 != null) {
         if (!var1.mark) {
            var1.mark = true;
         } else {
            this.cut(var1, var2);
            this.cascadingCut(var2);
         }
      }

   }

   protected void consolidate() {
      int var1 = this.size + 1;
      Node[] var2 = new Node[var1];

      int var3;
      for(var3 = 0; var3 < var1; ++var3) {
         var2[var3] = null;
      }

      var3 = 0;
      Node var4 = this.min;
      if (var4 != null) {
         ++var3;

         for(var4 = var4.right; var4 != this.min; var4 = var4.right) {
            ++var3;
         }
      }

      int var5;
      while(var3 > 0) {
         var5 = var4.degree;

         Node var6;
         for(var6 = var4.right; var2[var5] != null; ++var5) {
            Node var7 = var2[var5];
            if (var4.key > var7.key) {
               Node var8 = var7;
               var7 = var4;
               var4 = var8;
            }

            this.link(var7, var4);
            var2[var5] = null;
         }

         var2[var5] = var4;
         var4 = var6;
         --var3;
      }

      this.min = null;

      for(var5 = 0; var5 < var1; ++var5) {
         if (var2[var5] != null) {
            if (this.min != null) {
               var2[var5].left.right = var2[var5].right;
               var2[var5].right.left = var2[var5].left;
               var2[var5].left = this.min;
               var2[var5].right = this.min.right;
               this.min.right = var2[var5];
               var2[var5].right.left = var2[var5];
               if (var2[var5].key < this.min.key) {
                  this.min = var2[var5];
               }
            } else {
               this.min = var2[var5];
            }
         }
      }

   }

   protected void cut(Node var1, Node var2) {
      var1.left.right = var1.right;
      var1.right.left = var1.left;
      --var2.degree;
      if (var2.child == var1) {
         var2.child = var1.right;
      }

      if (var2.degree == 0) {
         var2.child = null;
      }

      var1.left = this.min;
      var1.right = this.min.right;
      this.min.right = var1;
      var1.right.left = var1;
      var1.parent = null;
      var1.mark = false;
   }

   protected void link(Node var1, Node var2) {
      var1.left.right = var1.right;
      var1.right.left = var1.left;
      var1.parent = var2;
      if (var2.child == null) {
         var2.child = var1;
         var1.right = var1;
         var1.left = var1;
      } else {
         var1.left = var2.child;
         var1.right = var2.child.right;
         var2.child.right = var1;
         var1.right.left = var1;
      }

      ++var2.degree;
      var1.mark = false;
   }

   public static class Node {
      Object userObject;
      Node child;
      Node left;
      Node parent;
      Node right;
      boolean mark;
      double key;
      int degree;

      public Node(Object var1, double var2) {
         this.userObject = var1;
         this.right = this;
         this.left = this;
         this.key = var2;
      }

      public final double getKey() {
         return this.key;
      }

      public Object getUserObject() {
         return this.userObject;
      }

      public void setUserObject(Object var1) {
         this.userObject = var1;
      }
   }
}
