package org.apache.fop.hyphenation;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Stack;

public class TernaryTree implements Cloneable, Serializable {
   protected char[] lo;
   protected char[] hi;
   protected char[] eq;
   protected char[] sc;
   protected CharVector kv;
   protected char root;
   protected char freenode;
   protected int length;
   protected static final int BLOCK_SIZE = 2048;

   TernaryTree() {
      this.init();
   }

   protected void init() {
      this.root = 0;
      this.freenode = 1;
      this.length = 0;
      this.lo = new char[2048];
      this.hi = new char[2048];
      this.eq = new char[2048];
      this.sc = new char[2048];
      this.kv = new CharVector();
   }

   public void insert(String key, char val) {
      int len = key.length() + 1;
      if (this.freenode + len > this.eq.length) {
         this.redimNodeArrays(this.eq.length + 2048);
      }

      char[] strkey = new char[len--];
      key.getChars(0, len, strkey, 0);
      strkey[len] = 0;
      this.root = this.insert(this.root, strkey, 0, val);
   }

   public void insert(char[] key, int start, char val) {
      int len = strlen(key) + 1;
      if (this.freenode + len > this.eq.length) {
         this.redimNodeArrays(this.eq.length + 2048);
      }

      this.root = this.insert(this.root, key, start, val);
   }

   private char insert(char p, char[] key, int start, char val) {
      int len = strlen(key, start);
      char var10002;
      if (p == 0) {
         var10002 = this.freenode;
         this.freenode = (char)(var10002 + 1);
         p = var10002;
         this.eq[p] = val;
         ++this.length;
         this.hi[p] = 0;
         if (len > 0) {
            this.sc[p] = '\uffff';
            this.lo[p] = (char)this.kv.alloc(len + 1);
            strcpy(this.kv.getArray(), this.lo[p], key, start);
         } else {
            this.sc[p] = 0;
            this.lo[p] = 0;
         }

         return p;
      } else {
         char s;
         if (this.sc[p] == '\uffff') {
            var10002 = this.freenode;
            this.freenode = (char)(var10002 + 1);
            s = var10002;
            this.lo[s] = this.lo[p];
            this.eq[s] = this.eq[p];
            this.lo[p] = 0;
            if (len <= 0) {
               this.sc[s] = '\uffff';
               this.hi[p] = s;
               this.sc[p] = 0;
               this.eq[p] = val;
               ++this.length;
               return p;
            }

            this.sc[p] = this.kv.get(this.lo[s]);
            this.eq[p] = s;
            ++this.lo[s];
            if (this.kv.get(this.lo[s]) == 0) {
               this.lo[s] = 0;
               this.sc[s] = 0;
               this.hi[s] = 0;
            } else {
               this.sc[s] = '\uffff';
            }
         }

         s = key[start];
         if (s < this.sc[p]) {
            this.lo[p] = this.insert(this.lo[p], key, start, val);
         } else if (s == this.sc[p]) {
            if (s != 0) {
               this.eq[p] = this.insert(this.eq[p], key, start + 1, val);
            } else {
               this.eq[p] = val;
            }
         } else {
            this.hi[p] = this.insert(this.hi[p], key, start, val);
         }

         return p;
      }
   }

   public static int strcmp(char[] a, int startA, char[] b, int startB) {
      while(a[startA] == b[startB]) {
         if (a[startA] == 0) {
            return 0;
         }

         ++startA;
         ++startB;
      }

      return a[startA] - b[startB];
   }

   public static int strcmp(String str, char[] a, int start) {
      int len = str.length();

      int i;
      for(i = 0; i < len; ++i) {
         int d = str.charAt(i) - a[start + i];
         if (d != 0) {
            return d;
         }

         if (a[start + i] == 0) {
            return d;
         }
      }

      if (a[start + i] != 0) {
         return -a[start + i];
      } else {
         return 0;
      }
   }

   public static void strcpy(char[] dst, int di, char[] src, int si) {
      while(src[si] != 0) {
         dst[di++] = src[si++];
      }

      dst[di] = 0;
   }

   public static int strlen(char[] a, int start) {
      int len = 0;

      for(int i = start; i < a.length && a[i] != 0; ++i) {
         ++len;
      }

      return len;
   }

   public static int strlen(char[] a) {
      return strlen(a, 0);
   }

   public int find(String key) {
      int len = key.length();
      char[] strkey = new char[len + 1];
      key.getChars(0, len, strkey, 0);
      strkey[len] = 0;
      return this.find(strkey, 0);
   }

   public int find(char[] key, int start) {
      char p = this.root;
      int i = start;

      while(p != 0) {
         if (this.sc[p] == '\uffff') {
            if (strcmp(key, i, this.kv.getArray(), this.lo[p]) == 0) {
               return this.eq[p];
            }

            return -1;
         }

         char c = key[i];
         int d = c - this.sc[p];
         if (d == 0) {
            if (c == 0) {
               return this.eq[p];
            }

            ++i;
            p = this.eq[p];
         } else if (d < 0) {
            p = this.lo[p];
         } else {
            p = this.hi[p];
         }
      }

      return -1;
   }

   public boolean knows(String key) {
      return this.find(key) >= 0;
   }

   private void redimNodeArrays(int newsize) {
      int len = newsize < this.lo.length ? newsize : this.lo.length;
      char[] na = new char[newsize];
      System.arraycopy(this.lo, 0, na, 0, len);
      this.lo = na;
      na = new char[newsize];
      System.arraycopy(this.hi, 0, na, 0, len);
      this.hi = na;
      na = new char[newsize];
      System.arraycopy(this.eq, 0, na, 0, len);
      this.eq = na;
      na = new char[newsize];
      System.arraycopy(this.sc, 0, na, 0, len);
      this.sc = na;
   }

   public int size() {
      return this.length;
   }

   public Object clone() {
      TernaryTree t = new TernaryTree();
      t.lo = (char[])this.lo.clone();
      t.hi = (char[])this.hi.clone();
      t.eq = (char[])this.eq.clone();
      t.sc = (char[])this.sc.clone();
      t.kv = (CharVector)this.kv.clone();
      t.root = this.root;
      t.freenode = this.freenode;
      t.length = this.length;
      return t;
   }

   protected void insertBalanced(String[] k, char[] v, int offset, int n) {
      if (n >= 1) {
         int m = n >> 1;
         this.insert(k[m + offset], v[m + offset]);
         this.insertBalanced(k, v, offset, m);
         this.insertBalanced(k, v, offset + m + 1, n - m - 1);
      }
   }

   public void balance() {
      int i = 0;
      int n = this.length;
      String[] k = new String[n];
      char[] v = new char[n];

      for(Iterator iter = new Iterator(); iter.hasMoreElements(); k[i++] = (String)iter.nextElement()) {
         v[i] = iter.getValue();
      }

      this.init();
      this.insertBalanced(k, v, 0, n);
   }

   public void trimToSize() {
      this.balance();
      this.redimNodeArrays(this.freenode);
      CharVector kx = new CharVector();
      kx.alloc(1);
      TernaryTree map = new TernaryTree();
      this.compact(kx, map, this.root);
      this.kv = kx;
      this.kv.trimToSize();
   }

   private void compact(CharVector kx, TernaryTree map, char p) {
      if (p != 0) {
         if (this.sc[p] == '\uffff') {
            int k = map.find(this.kv.getArray(), this.lo[p]);
            if (k < 0) {
               k = kx.alloc(strlen(this.kv.getArray(), this.lo[p]) + 1);
               strcpy(kx.getArray(), k, this.kv.getArray(), this.lo[p]);
               map.insert(kx.getArray(), k, (char)k);
            }

            this.lo[p] = (char)k;
         } else {
            this.compact(kx, map, this.lo[p]);
            if (this.sc[p] != 0) {
               this.compact(kx, map, this.eq[p]);
            }

            this.compact(kx, map, this.hi[p]);
         }

      }
   }

   public Enumeration keys() {
      return new Iterator();
   }

   public void printStats() {
      System.out.println("Number of keys = " + Integer.toString(this.length));
      System.out.println("Node count = " + Integer.toString(this.freenode));
      System.out.println("Key Array length = " + Integer.toString(this.kv.length()));
   }

   public static void main(String[] args) throws Exception {
      TernaryTree tt = new TernaryTree();
      tt.insert("Carlos", 'C');
      tt.insert("Car", 'r');
      tt.insert("palos", 'l');
      tt.insert("pa", 'p');
      tt.trimToSize();
      System.out.println((char)tt.find("Car"));
      System.out.println((char)tt.find("Carlos"));
      System.out.println((char)tt.find("alto"));
      tt.printStats();
   }

   public class Iterator implements Enumeration {
      int cur = -1;
      String curkey;
      Stack ns = new Stack();
      StringBuffer ks = new StringBuffer();

      public Iterator() {
         this.rewind();
      }

      public void rewind() {
         this.ns.removeAllElements();
         this.ks.setLength(0);
         this.cur = TernaryTree.this.root;
         this.run();
      }

      public Object nextElement() {
         String res = new String(this.curkey);
         this.cur = this.up();
         this.run();
         return res;
      }

      public char getValue() {
         return this.cur >= 0 ? TernaryTree.this.eq[this.cur] : '\u0000';
      }

      public boolean hasMoreElements() {
         return this.cur != -1;
      }

      private int up() {
         new Item();
         int res = 0;
         if (this.ns.empty()) {
            return -1;
         } else if (this.cur != 0 && TernaryTree.this.sc[this.cur] == 0) {
            return TernaryTree.this.lo[this.cur];
         } else {
            boolean climb = true;

            while(climb) {
               Item i = (Item)this.ns.pop();
               ++i.child;
               switch (i.child) {
                  case '\u0001':
                     if (TernaryTree.this.sc[i.parent] != 0) {
                        res = TernaryTree.this.eq[i.parent];
                        this.ns.push(i.clone());
                        this.ks.append(TernaryTree.this.sc[i.parent]);
                     } else {
                        ++i.child;
                        this.ns.push(i.clone());
                        res = TernaryTree.this.hi[i.parent];
                     }

                     climb = false;
                     break;
                  case '\u0002':
                     res = TernaryTree.this.hi[i.parent];
                     this.ns.push(i.clone());
                     if (this.ks.length() > 0) {
                        this.ks.setLength(this.ks.length() - 1);
                     }

                     climb = false;
                     break;
                  default:
                     if (this.ns.empty()) {
                        return -1;
                     }

                     climb = true;
               }
            }

            return res;
         }
      }

      private int run() {
         if (this.cur == -1) {
            return -1;
         } else {
            boolean leaf = false;

            while(true) {
               while(true) {
                  if (this.cur != 0) {
                     if (TernaryTree.this.sc[this.cur] == '\uffff') {
                        leaf = true;
                     } else {
                        this.ns.push(new Item((char)this.cur, '\u0000'));
                        if (TernaryTree.this.sc[this.cur] != 0) {
                           this.cur = TernaryTree.this.lo[this.cur];
                           continue;
                        }

                        leaf = true;
                     }
                  }

                  if (leaf) {
                     StringBuffer buf = new StringBuffer(this.ks.toString());
                     if (TernaryTree.this.sc[this.cur] == '\uffff') {
                        int p = TernaryTree.this.lo[this.cur];

                        while(TernaryTree.this.kv.get(p) != 0) {
                           buf.append(TernaryTree.this.kv.get(p++));
                        }
                     }

                     this.curkey = buf.toString();
                     return 0;
                  }

                  this.cur = this.up();
                  if (this.cur == -1) {
                     return -1;
                  }
               }
            }
         }
      }

      private class Item implements Cloneable {
         char parent;
         char child;

         public Item() {
            this.parent = 0;
            this.child = 0;
         }

         public Item(char p, char c) {
            this.parent = p;
            this.child = c;
         }

         public Object clone() {
            return Iterator.this.new Item(this.parent, this.child);
         }
      }
   }
}
