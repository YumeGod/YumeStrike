package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;

public class MultiReferenceStorage implements Storage {
   private final Storage storage;
   private int referenceCounter;

   public MultiReferenceStorage(Storage storage) {
      if (storage == null) {
         throw new IllegalArgumentException();
      } else {
         this.storage = storage;
         this.referenceCounter = 1;
      }
   }

   public void addReference() {
      this.incrementCounter();
   }

   public void delete() {
      if (this.decrementCounter()) {
         this.storage.delete();
      }

   }

   public InputStream getInputStream() throws IOException {
      return this.storage.getInputStream();
   }

   private synchronized void incrementCounter() {
      if (this.referenceCounter == 0) {
         throw new IllegalStateException("storage has been deleted");
      } else {
         ++this.referenceCounter;
      }
   }

   private synchronized boolean decrementCounter() {
      if (this.referenceCounter == 0) {
         throw new IllegalStateException("storage has been deleted");
      } else {
         return --this.referenceCounter == 0;
      }
   }
}
