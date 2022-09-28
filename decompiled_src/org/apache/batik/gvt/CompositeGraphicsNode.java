package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.batik.util.HaltingThread;

public class CompositeGraphicsNode extends AbstractGraphicsNode implements List {
   public static final Rectangle2D VIEWPORT = new Rectangle();
   public static final Rectangle2D NULL_RECT = new Rectangle();
   protected GraphicsNode[] children;
   protected volatile int count;
   protected volatile int modCount;
   protected Rectangle2D backgroundEnableRgn = null;
   private volatile Rectangle2D geometryBounds;
   private volatile Rectangle2D primitiveBounds;
   private volatile Rectangle2D sensitiveBounds;
   private Shape outline;

   public List getChildren() {
      return this;
   }

   public void setBackgroundEnable(Rectangle2D var1) {
      this.backgroundEnableRgn = var1;
   }

   public Rectangle2D getBackgroundEnable() {
      return this.backgroundEnableRgn;
   }

   public void setVisible(boolean var1) {
      this.isVisible = var1;
   }

   public void primitivePaint(Graphics2D var1) {
      if (this.count != 0) {
         Thread var2 = Thread.currentThread();

         for(int var3 = 0; var3 < this.count; ++var3) {
            if (HaltingThread.hasBeenHalted(var2)) {
               return;
            }

            GraphicsNode var4 = this.children[var3];
            if (var4 != null) {
               var4.paint(var1);
            }
         }

      }
   }

   protected void invalidateGeometryCache() {
      super.invalidateGeometryCache();
      this.geometryBounds = null;
      this.primitiveBounds = null;
      this.sensitiveBounds = null;
      this.outline = null;
   }

   public Rectangle2D getPrimitiveBounds() {
      if (this.primitiveBounds != null) {
         return this.primitiveBounds == NULL_RECT ? null : this.primitiveBounds;
      } else {
         Thread var1 = Thread.currentThread();
         int var2 = 0;
         Rectangle2D var3 = null;

         while(var3 == null && var2 < this.count) {
            var3 = this.children[var2++].getTransformedBounds(GraphicsNode.IDENTITY);
            if ((var2 & 15) == 0 && HaltingThread.hasBeenHalted(var1)) {
               break;
            }
         }

         if (HaltingThread.hasBeenHalted(var1)) {
            this.invalidateGeometryCache();
            return null;
         } else if (var3 == null) {
            this.primitiveBounds = NULL_RECT;
            return null;
         } else {
            this.primitiveBounds = var3;

            while(var2 < this.count) {
               Rectangle2D var4 = this.children[var2++].getTransformedBounds(GraphicsNode.IDENTITY);
               if (var4 != null) {
                  if (this.primitiveBounds == null) {
                     return null;
                  }

                  this.primitiveBounds.add(var4);
               }

               if ((var2 & 15) == 0 && HaltingThread.hasBeenHalted(var1)) {
                  break;
               }
            }

            if (HaltingThread.hasBeenHalted(var1)) {
               this.invalidateGeometryCache();
            }

            return this.primitiveBounds;
         }
      }
   }

   public static Rectangle2D getTransformedBBox(Rectangle2D var0, AffineTransform var1) {
      if (var1 != null && var0 != null) {
         double var2 = var0.getX();
         double var4 = var0.getWidth();
         double var6 = var0.getY();
         double var8 = var0.getHeight();
         double var10 = var1.getScaleX();
         double var12 = var1.getScaleY();
         if (var10 < 0.0) {
            var2 = -(var2 + var4);
            var10 = -var10;
         }

         if (var12 < 0.0) {
            var6 = -(var6 + var8);
            var12 = -var12;
         }

         return new Rectangle2D.Float((float)(var2 * var10 + var1.getTranslateX()), (float)(var6 * var12 + var1.getTranslateY()), (float)(var4 * var10), (float)(var8 * var12));
      } else {
         return var0;
      }
   }

   public Rectangle2D getTransformedPrimitiveBounds(AffineTransform var1) {
      AffineTransform var2 = var1;
      if (this.transform != null) {
         var2 = new AffineTransform(var1);
         var2.concatenate(this.transform);
      }

      if (var2 != null && (var2.getShearX() != 0.0 || var2.getShearY() != 0.0)) {
         int var3 = 0;

         Rectangle2D var4;
         for(var4 = null; var4 == null && var3 < this.count; var4 = this.children[var3++].getTransformedBounds(var2)) {
         }

         while(var3 < this.count) {
            Rectangle2D var5 = this.children[var3++].getTransformedBounds(var2);
            if (var5 != null) {
               var4.add(var5);
            }
         }

         return var4;
      } else {
         return getTransformedBBox(this.getPrimitiveBounds(), var2);
      }
   }

   public Rectangle2D getGeometryBounds() {
      if (this.geometryBounds == null) {
         int var1;
         for(var1 = 0; this.geometryBounds == null && var1 < this.count; this.geometryBounds = this.children[var1++].getTransformedGeometryBounds(GraphicsNode.IDENTITY)) {
         }

         while(var1 < this.count) {
            Rectangle2D var2 = this.children[var1++].getTransformedGeometryBounds(GraphicsNode.IDENTITY);
            if (var2 != null) {
               if (this.geometryBounds == null) {
                  return this.getGeometryBounds();
               }

               this.geometryBounds.add(var2);
            }
         }
      }

      return this.geometryBounds;
   }

   public Rectangle2D getTransformedGeometryBounds(AffineTransform var1) {
      AffineTransform var2 = var1;
      if (this.transform != null) {
         var2 = new AffineTransform(var1);
         var2.concatenate(this.transform);
      }

      if (var2 == null || var2.getShearX() == 0.0 && var2.getShearY() == 0.0) {
         return getTransformedBBox(this.getGeometryBounds(), var2);
      } else {
         Rectangle2D var3 = null;

         int var4;
         for(var4 = 0; var3 == null && var4 < this.count; var3 = this.children[var4++].getTransformedGeometryBounds(var2)) {
         }

         Rectangle2D var5 = null;

         while(var4 < this.count) {
            var5 = this.children[var4++].getTransformedGeometryBounds(var2);
            if (var5 != null) {
               var3.add(var5);
            }
         }

         return var3;
      }
   }

   public Rectangle2D getSensitiveBounds() {
      if (this.sensitiveBounds != null) {
         return this.sensitiveBounds;
      } else {
         int var1;
         for(var1 = 0; this.sensitiveBounds == null && var1 < this.count; this.sensitiveBounds = this.children[var1++].getTransformedSensitiveBounds(GraphicsNode.IDENTITY)) {
         }

         while(var1 < this.count) {
            Rectangle2D var2 = this.children[var1++].getTransformedSensitiveBounds(GraphicsNode.IDENTITY);
            if (var2 != null) {
               if (this.sensitiveBounds == null) {
                  return this.getSensitiveBounds();
               }

               this.sensitiveBounds.add(var2);
            }
         }

         return this.sensitiveBounds;
      }
   }

   public Rectangle2D getTransformedSensitiveBounds(AffineTransform var1) {
      AffineTransform var2 = var1;
      if (this.transform != null) {
         var2 = new AffineTransform(var1);
         var2.concatenate(this.transform);
      }

      if (var2 != null && (var2.getShearX() != 0.0 || var2.getShearY() != 0.0)) {
         Rectangle2D var3 = null;

         int var4;
         for(var4 = 0; var3 == null && var4 < this.count; var3 = this.children[var4++].getTransformedSensitiveBounds(var2)) {
         }

         while(var4 < this.count) {
            Rectangle2D var5 = this.children[var4++].getTransformedSensitiveBounds(var2);
            if (var5 != null) {
               var3.add(var5);
            }
         }

         return var3;
      } else {
         return getTransformedBBox(this.getSensitiveBounds(), var2);
      }
   }

   public boolean contains(Point2D var1) {
      Rectangle2D var2 = this.getSensitiveBounds();
      if (this.count > 0 && var2 != null && var2.contains(var1)) {
         Point2D var3 = null;
         Point2D var4 = null;

         for(int var5 = 0; var5 < this.count; ++var5) {
            AffineTransform var6 = this.children[var5].getInverseTransform();
            if (var6 != null) {
               var3 = var6.transform(var1, var3);
               var4 = var3;
            } else {
               var4 = var1;
            }

            if (this.children[var5].contains(var4)) {
               return true;
            }
         }
      }

      return false;
   }

   public GraphicsNode nodeHitAt(Point2D var1) {
      Rectangle2D var2 = this.getSensitiveBounds();
      if (this.count > 0 && var2 != null && var2.contains(var1)) {
         Point2D var3 = null;
         Point2D var4 = null;

         for(int var5 = this.count - 1; var5 >= 0; --var5) {
            AffineTransform var6 = this.children[var5].getInverseTransform();
            if (var6 != null) {
               var3 = var6.transform(var1, var3);
               var4 = var3;
            } else {
               var4 = var1;
            }

            GraphicsNode var7 = this.children[var5].nodeHitAt(var4);
            if (var7 != null) {
               return var7;
            }
         }
      }

      return null;
   }

   public Shape getOutline() {
      if (this.outline != null) {
         return this.outline;
      } else {
         this.outline = new GeneralPath();

         for(int var1 = 0; var1 < this.count; ++var1) {
            Shape var2 = this.children[var1].getOutline();
            if (var2 != null) {
               AffineTransform var3 = this.children[var1].getTransform();
               if (var3 != null) {
                  ((GeneralPath)this.outline).append(var3.createTransformedShape(var2), false);
               } else {
                  ((GeneralPath)this.outline).append(var2, false);
               }
            }
         }

         return this.outline;
      }
   }

   protected void setRoot(RootGraphicsNode var1) {
      super.setRoot(var1);

      for(int var2 = 0; var2 < this.count; ++var2) {
         GraphicsNode var3 = this.children[var2];
         ((AbstractGraphicsNode)var3).setRoot(var1);
      }

   }

   public int size() {
      return this.count;
   }

   public boolean isEmpty() {
      return this.count == 0;
   }

   public boolean contains(Object var1) {
      return this.indexOf(var1) >= 0;
   }

   public Iterator iterator() {
      return new Itr();
   }

   public Object[] toArray() {
      GraphicsNode[] var1 = new GraphicsNode[this.count];
      System.arraycopy(this.children, 0, var1, 0, this.count);
      return var1;
   }

   public Object[] toArray(Object[] var1) {
      if (((Object[])var1).length < this.count) {
         var1 = new GraphicsNode[this.count];
      }

      System.arraycopy(this.children, 0, var1, 0, this.count);
      if (((Object[])var1).length > this.count) {
         ((Object[])var1)[this.count] = null;
      }

      return (Object[])var1;
   }

   public Object get(int var1) {
      this.checkRange(var1);
      return this.children[var1];
   }

   public Object set(int var1, Object var2) {
      if (!(var2 instanceof GraphicsNode)) {
         throw new IllegalArgumentException(var2 + " is not a GraphicsNode");
      } else {
         this.checkRange(var1);
         GraphicsNode var3 = (GraphicsNode)var2;
         this.fireGraphicsNodeChangeStarted(var3);
         if (var3.getParent() != null) {
            var3.getParent().getChildren().remove(var3);
         }

         GraphicsNode var4 = this.children[var1];
         this.children[var1] = var3;
         ((AbstractGraphicsNode)var3).setParent(this);
         ((AbstractGraphicsNode)var4).setParent((CompositeGraphicsNode)null);
         ((AbstractGraphicsNode)var3).setRoot(this.getRoot());
         ((AbstractGraphicsNode)var4).setRoot((RootGraphicsNode)null);
         this.invalidateGeometryCache();
         this.fireGraphicsNodeChangeCompleted();
         return var4;
      }
   }

   public boolean add(Object var1) {
      if (!(var1 instanceof GraphicsNode)) {
         throw new IllegalArgumentException(var1 + " is not a GraphicsNode");
      } else {
         GraphicsNode var2 = (GraphicsNode)var1;
         this.fireGraphicsNodeChangeStarted(var2);
         if (var2.getParent() != null) {
            var2.getParent().getChildren().remove(var2);
         }

         this.ensureCapacity(this.count + 1);
         this.children[this.count++] = var2;
         ((AbstractGraphicsNode)var2).setParent(this);
         ((AbstractGraphicsNode)var2).setRoot(this.getRoot());
         this.invalidateGeometryCache();
         this.fireGraphicsNodeChangeCompleted();
         return true;
      }
   }

   public void add(int var1, Object var2) {
      if (!(var2 instanceof GraphicsNode)) {
         throw new IllegalArgumentException(var2 + " is not a GraphicsNode");
      } else if (var1 <= this.count && var1 >= 0) {
         GraphicsNode var3 = (GraphicsNode)var2;
         this.fireGraphicsNodeChangeStarted(var3);
         if (var3.getParent() != null) {
            var3.getParent().getChildren().remove(var3);
         }

         this.ensureCapacity(this.count + 1);
         System.arraycopy(this.children, var1, this.children, var1 + 1, this.count - var1);
         this.children[var1] = var3;
         ++this.count;
         ((AbstractGraphicsNode)var3).setParent(this);
         ((AbstractGraphicsNode)var3).setRoot(this.getRoot());
         this.invalidateGeometryCache();
         this.fireGraphicsNodeChangeCompleted();
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1 + ", Size: " + this.count);
      }
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(int var1, Collection var2) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(Object var1) {
      if (!(var1 instanceof GraphicsNode)) {
         throw new IllegalArgumentException(var1 + " is not a GraphicsNode");
      } else {
         GraphicsNode var2 = (GraphicsNode)var1;
         if (var2.getParent() != this) {
            return false;
         } else {
            int var3;
            for(var3 = 0; var2 != this.children[var3]; ++var3) {
            }

            this.remove(var3);
            return true;
         }
      }
   }

   public Object remove(int var1) {
      this.checkRange(var1);
      GraphicsNode var2 = this.children[var1];
      this.fireGraphicsNodeChangeStarted(var2);
      ++this.modCount;
      int var3 = this.count - var1 - 1;
      if (var3 > 0) {
         System.arraycopy(this.children, var1 + 1, this.children, var1, var3);
      }

      this.children[--this.count] = null;
      if (this.count == 0) {
         this.children = null;
      }

      ((AbstractGraphicsNode)var2).setParent((CompositeGraphicsNode)null);
      ((AbstractGraphicsNode)var2).setRoot((RootGraphicsNode)null);
      this.invalidateGeometryCache();
      this.fireGraphicsNodeChangeCompleted();
      return var2;
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection var1) {
      Iterator var2 = var1.iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }
      } while(this.contains(var2.next()));

      return false;
   }

   public int indexOf(Object var1) {
      if (var1 != null && var1 instanceof GraphicsNode) {
         if (((GraphicsNode)var1).getParent() == this) {
            int var2 = this.count;
            GraphicsNode[] var3 = this.children;

            for(int var4 = 0; var4 < var2; ++var4) {
               if (var1 == var3[var4]) {
                  return var4;
               }
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public int lastIndexOf(Object var1) {
      if (var1 != null && var1 instanceof GraphicsNode) {
         if (((GraphicsNode)var1).getParent() == this) {
            for(int var2 = this.count - 1; var2 >= 0; --var2) {
               if (var1 == this.children[var2]) {
                  return var2;
               }
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public ListIterator listIterator() {
      return this.listIterator(0);
   }

   public ListIterator listIterator(int var1) {
      if (var1 >= 0 && var1 <= this.count) {
         return new ListItr(var1);
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1);
      }
   }

   public List subList(int var1, int var2) {
      throw new UnsupportedOperationException();
   }

   private void checkRange(int var1) {
      if (var1 >= this.count || var1 < 0) {
         throw new IndexOutOfBoundsException("Index: " + var1 + ", Size: " + this.count);
      }
   }

   public void ensureCapacity(int var1) {
      if (this.children == null) {
         this.children = new GraphicsNode[4];
      }

      ++this.modCount;
      int var2 = this.children.length;
      if (var1 > var2) {
         GraphicsNode[] var3 = this.children;
         int var4 = var2 + var2 / 2 + 1;
         if (var4 < var1) {
            var4 = var1;
         }

         this.children = new GraphicsNode[var4];
         System.arraycopy(var3, 0, this.children, 0, this.count);
      }

   }

   private class ListItr extends Itr implements ListIterator {
      ListItr(int var2) {
         super(null);
         this.cursor = var2;
      }

      public boolean hasPrevious() {
         return this.cursor != 0;
      }

      public Object previous() {
         try {
            Object var1 = CompositeGraphicsNode.this.get(--this.cursor);
            this.checkForComodification();
            this.lastRet = this.cursor;
            return var1;
         } catch (IndexOutOfBoundsException var2) {
            this.checkForComodification();
            throw new NoSuchElementException();
         }
      }

      public int nextIndex() {
         return this.cursor;
      }

      public int previousIndex() {
         return this.cursor - 1;
      }

      public void set(Object var1) {
         if (this.lastRet == -1) {
            throw new IllegalStateException();
         } else {
            this.checkForComodification();

            try {
               CompositeGraphicsNode.this.set(this.lastRet, var1);
               this.expectedModCount = CompositeGraphicsNode.this.modCount;
            } catch (IndexOutOfBoundsException var3) {
               throw new ConcurrentModificationException();
            }
         }
      }

      public void add(Object var1) {
         this.checkForComodification();

         try {
            CompositeGraphicsNode.this.add(this.cursor++, var1);
            this.lastRet = -1;
            this.expectedModCount = CompositeGraphicsNode.this.modCount;
         } catch (IndexOutOfBoundsException var3) {
            throw new ConcurrentModificationException();
         }
      }
   }

   private class Itr implements Iterator {
      int cursor;
      int lastRet;
      int expectedModCount;

      private Itr() {
         this.cursor = 0;
         this.lastRet = -1;
         this.expectedModCount = CompositeGraphicsNode.this.modCount;
      }

      public boolean hasNext() {
         return this.cursor != CompositeGraphicsNode.this.count;
      }

      public Object next() {
         try {
            Object var1 = CompositeGraphicsNode.this.get(this.cursor);
            this.checkForComodification();
            this.lastRet = this.cursor++;
            return var1;
         } catch (IndexOutOfBoundsException var2) {
            this.checkForComodification();
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         if (this.lastRet == -1) {
            throw new IllegalStateException();
         } else {
            this.checkForComodification();

            try {
               CompositeGraphicsNode.this.remove(this.lastRet);
               if (this.lastRet < this.cursor) {
                  --this.cursor;
               }

               this.lastRet = -1;
               this.expectedModCount = CompositeGraphicsNode.this.modCount;
            } catch (IndexOutOfBoundsException var2) {
               throw new ConcurrentModificationException();
            }
         }
      }

      final void checkForComodification() {
         if (CompositeGraphicsNode.this.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
         }
      }

      // $FF: synthetic method
      Itr(Object var2) {
         this();
      }
   }
}
