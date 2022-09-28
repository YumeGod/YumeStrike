package org.apache.batik.util;

public class DoublyLinkedList {
   private Node head = null;
   private int size = 0;

   public synchronized int getSize() {
      return this.size;
   }

   public synchronized void empty() {
      while(this.size > 0) {
         this.pop();
      }

   }

   public Node getHead() {
      return this.head;
   }

   public Node getTail() {
      return this.head.getPrev();
   }

   public void touch(Node var1) {
      if (var1 != null) {
         var1.insertBefore(this.head);
         this.head = var1;
      }
   }

   public void add(int var1, Node var2) {
      if (var2 != null) {
         if (var1 == 0) {
            var2.insertBefore(this.head);
            this.head = var2;
         } else if (var1 == this.size) {
            var2.insertBefore(this.head);
         } else {
            Node var3;
            for(var3 = this.head; var1 != 0; --var1) {
               var3 = var3.getNext();
            }

            var2.insertBefore(var3);
         }

         ++this.size;
      }
   }

   public void add(Node var1) {
      if (var1 != null) {
         var1.insertBefore(this.head);
         this.head = var1;
         ++this.size;
      }
   }

   public void remove(Node var1) {
      if (var1 != null) {
         if (var1 == this.head) {
            if (this.head.getNext() == this.head) {
               this.head = null;
            } else {
               this.head = this.head.getNext();
            }
         }

         var1.unlink();
         --this.size;
      }
   }

   public Node pop() {
      if (this.head == null) {
         return null;
      } else {
         Node var1 = this.head;
         this.remove(var1);
         return var1;
      }
   }

   public Node unpush() {
      if (this.head == null) {
         return null;
      } else {
         Node var1 = this.getTail();
         this.remove(var1);
         return var1;
      }
   }

   public void push(Node var1) {
      var1.insertBefore(this.head);
      if (this.head == null) {
         this.head = var1;
      }

      ++this.size;
   }

   public void unpop(Node var1) {
      var1.insertBefore(this.head);
      this.head = var1;
      ++this.size;
   }

   public static class Node {
      private Node next = null;
      private Node prev = null;

      public final Node getNext() {
         return this.next;
      }

      public final Node getPrev() {
         return this.prev;
      }

      protected final void setNext(Node var1) {
         this.next = var1;
      }

      protected final void setPrev(Node var1) {
         this.prev = var1;
      }

      protected final void unlink() {
         if (this.getNext() != null) {
            this.getNext().setPrev(this.getPrev());
         }

         if (this.getPrev() != null) {
            this.getPrev().setNext(this.getNext());
         }

         this.setNext((Node)null);
         this.setPrev((Node)null);
      }

      protected final void insertBefore(Node var1) {
         if (this != var1) {
            if (this.getPrev() != null) {
               this.unlink();
            }

            if (var1 == null) {
               this.setNext(this);
               this.setPrev(this);
            } else {
               this.setNext(var1);
               this.setPrev(var1.getPrev());
               var1.setPrev(this);
               if (this.getPrev() != null) {
                  this.getPrev().setNext(this);
               }
            }

         }
      }
   }
}
