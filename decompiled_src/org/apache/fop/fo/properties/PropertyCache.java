package org.apache.fop.fo.properties;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import org.apache.fop.fo.flow.Marker;

public final class PropertyCache {
   private static final int SEGMENT_COUNT = 32;
   private static final int INITIAL_BUCKET_COUNT = 32;
   private static final int SEGMENT_MASK = 31;
   private final boolean useCache = Boolean.valueOf(System.getProperty("org.apache.fop.fo.properties.use-cache", "true"));
   private CacheSegment[] segments = new CacheSegment[32];
   private CacheEntry[] table = new CacheEntry[32];
   private Class runtimeType;
   private final boolean[] votesForRehash = new boolean[32];
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   private static int hash(Object x) {
      return hash(x.hashCode());
   }

   private static int hash(int hashCode) {
      int h = hashCode + ~(hashCode << 9);
      h ^= h >>> 14;
      h += h << 4;
      h ^= h >>> 10;
      return h;
   }

   private static boolean eq(Object p, Object q) {
      return p == q || p != null && p.equals(q);
   }

   private void cleanSegment(int segmentIndex) {
      CacheSegment segment = this.segments[segmentIndex];
      int oldCount = segment.count;

      for(int bucketIndex = segmentIndex; bucketIndex < this.table.length; bucketIndex += 32) {
         CacheEntry prev = null;
         CacheEntry entry = this.table[bucketIndex];
         if (entry != null) {
            do {
               if (entry.get() == null) {
                  if (prev == null) {
                     this.table[bucketIndex] = entry.nextEntry;
                  } else {
                     prev.nextEntry = entry.nextEntry;
                  }

                  segment.count--;
                  if (!$assertionsDisabled && segment.count < 0) {
                     throw new AssertionError();
                  }
               } else {
                  prev = entry;
               }

               entry = entry.nextEntry;
            } while(entry != null);
         }
      }

      synchronized(this.votesForRehash) {
         if (oldCount > segment.count) {
            this.votesForRehash[segmentIndex] = false;
         } else {
            if (!this.votesForRehash[segmentIndex]) {
               this.votesForRehash[segmentIndex] = true;
               int voteCount = 0;
               int i = 32;

               while(true) {
                  --i;
                  if (i < 0) {
                     if (voteCount > 7) {
                        this.rehash(31);
                        i = 32;

                        while(true) {
                           --i;
                           if (i < 0) {
                              return;
                           }

                           this.votesForRehash[i] = false;
                        }
                     }
                     break;
                  }

                  if (this.votesForRehash[i]) {
                     ++voteCount;
                  }
               }
            }

         }
      }
   }

   private void put(Object o) {
      int hash = hash(o);
      int segmentIndex = hash & 31;
      CacheSegment segment = this.segments[segmentIndex];
      synchronized(segment) {
         int index = hash & this.table.length - 1;
         CacheEntry entry = this.table[index];
         if (entry == null) {
            entry = new CacheEntry(o, (CacheEntry)null);
            this.table[index] = entry;
            segment.count++;
         } else {
            Object p = entry.get();
            if (eq(p, o)) {
               return;
            }

            CacheEntry newEntry = new CacheEntry(o, entry);
            this.table[index] = newEntry;
            segment.count++;
         }

         if (segment.count > 2 * this.table.length) {
            this.cleanSegment(segmentIndex);
         }

      }
   }

   private Object get(Object o) {
      int hash = hash(o);
      int index = hash & this.table.length - 1;
      CacheEntry entry = this.table[index];

      Object q;
      for(CacheEntry e = entry; e != null; e = e.nextEntry) {
         if (e.hash == hash && (q = e.get()) != null && eq(q, o)) {
            return q;
         }
      }

      CacheSegment segment = this.segments[hash & 31];
      synchronized(segment) {
         entry = this.table[index];

         for(CacheEntry e = entry; e != null; e = e.nextEntry) {
            if (e.hash == hash && (q = e.get()) != null && eq(q, o)) {
               return q;
            }
         }

         return null;
      }
   }

   private void rehash(int index) {
      CacheSegment seg = this.segments[index];
      synchronized(seg) {
         if (index > 0) {
            this.rehash(index - 1);
         } else {
            int newLength = this.table.length << 1;
            if (newLength > 0) {
               int i = this.segments.length;

               while(true) {
                  --i;
                  if (i < 0) {
                     CacheEntry[] newTable = new CacheEntry[newLength];
                     --newLength;
                     int i = this.table.length;

                     while(true) {
                        --i;
                        if (i < 0) {
                           this.table = newTable;
                           return;
                        }

                        for(CacheEntry c = this.table[i]; c != null; c = c.nextEntry) {
                           Object o;
                           if ((o = c.get()) != null) {
                              int hash = c.hash;
                              int idx = hash & newLength;
                              newTable[idx] = new CacheEntry(o, newTable[idx]);
                              this.segments[hash & 31].count++;
                           }
                        }
                     }
                  }

                  this.segments[i].count = 0;
               }
            }
         }

      }
   }

   public PropertyCache(Class c) {
      if (this.useCache) {
         int i = 32;

         while(true) {
            --i;
            if (i < 0) {
               break;
            }

            this.segments[i] = new CacheSegment();
         }
      }

      this.runtimeType = c;
   }

   private Object fetch(Object obj) {
      if (!this.useCache) {
         return obj;
      } else if (obj == null) {
         return null;
      } else {
         Object cacheEntry = this.get(obj);
         if (cacheEntry != null) {
            return cacheEntry;
         } else {
            this.put(obj);
            return obj;
         }
      }
   }

   public Property fetch(Property prop) {
      return (Property)this.fetch((Object)prop);
   }

   public CommonHyphenation fetch(CommonHyphenation chy) {
      return (CommonHyphenation)this.fetch((Object)chy);
   }

   public CommonFont fetch(CommonFont cf) {
      return (CommonFont)this.fetch((Object)cf);
   }

   public CommonBorderPaddingBackground fetch(CommonBorderPaddingBackground cbpb) {
      return (CommonBorderPaddingBackground)this.fetch((Object)cbpb);
   }

   public CommonBorderPaddingBackground.BorderInfo fetch(CommonBorderPaddingBackground.BorderInfo bi) {
      return (CommonBorderPaddingBackground.BorderInfo)this.fetch((Object)bi);
   }

   public Marker.MarkerAttribute fetch(Marker.MarkerAttribute ma) {
      return (Marker.MarkerAttribute)this.fetch((Object)ma);
   }

   public String toString() {
      return super.toString() + "[runtimeType=" + this.runtimeType + "]";
   }

   static {
      $assertionsDisabled = !PropertyCache.class.desiredAssertionStatus();
   }

   private static class CacheSegment {
      private int count;

      private CacheSegment() {
         this.count = 0;
      }

      // $FF: synthetic method
      CacheSegment(Object x0) {
         this();
      }
   }

   private static class CacheEntry extends WeakReference {
      private volatile CacheEntry nextEntry;
      private final int hash;

      public CacheEntry(Object p, CacheEntry nextEntry, ReferenceQueue refQueue) {
         super(p, refQueue);
         this.nextEntry = nextEntry;
         this.hash = PropertyCache.hash(p);
      }

      public CacheEntry(Object p, CacheEntry nextEntry) {
         super(p);
         this.nextEntry = nextEntry;
         this.hash = PropertyCache.hash(p);
      }
   }
}
