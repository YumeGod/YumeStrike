package org.apache.xml.dtm.ref;

import java.util.BitSet;
import org.apache.xml.res.XMLMessages;

public class CoroutineManager {
   BitSet m_activeIDs = new BitSet();
   static final int m_unreasonableId = 1024;
   Object m_yield = null;
   static final int NOBODY = -1;
   static final int ANYBODY = -1;
   int m_nextCoroutine = -1;

   public synchronized int co_joinCoroutineSet(int coroutineID) {
      if (coroutineID >= 0) {
         if (coroutineID >= 1024 || this.m_activeIDs.get(coroutineID)) {
            return -1;
         }
      } else {
         for(coroutineID = 0; coroutineID < 1024 && this.m_activeIDs.get(coroutineID); ++coroutineID) {
         }

         if (coroutineID >= 1024) {
            return -1;
         }
      }

      this.m_activeIDs.set(coroutineID);
      return coroutineID;
   }

   public synchronized Object co_entry_pause(int thisCoroutine) throws NoSuchMethodException {
      if (!this.m_activeIDs.get(thisCoroutine)) {
         throw new NoSuchMethodException();
      } else {
         while(this.m_nextCoroutine != thisCoroutine) {
            try {
               this.wait();
            } catch (InterruptedException var3) {
            }
         }

         return this.m_yield;
      }
   }

   public synchronized Object co_resume(Object arg_object, int thisCoroutine, int toCoroutine) throws NoSuchMethodException {
      if (!this.m_activeIDs.get(toCoroutine)) {
         throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[]{Integer.toString(toCoroutine)}));
      } else {
         this.m_yield = arg_object;
         this.m_nextCoroutine = toCoroutine;
         this.notify();

         while(this.m_nextCoroutine != thisCoroutine || this.m_nextCoroutine == -1 || this.m_nextCoroutine == -1) {
            try {
               this.wait();
            } catch (InterruptedException var5) {
            }
         }

         if (this.m_nextCoroutine == -1) {
            this.co_exit(thisCoroutine);
            throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_CO_EXIT", (Object[])null));
         } else {
            return this.m_yield;
         }
      }
   }

   public synchronized void co_exit(int thisCoroutine) {
      this.m_activeIDs.clear(thisCoroutine);
      this.m_nextCoroutine = -1;
      this.notify();
   }

   public synchronized void co_exit_to(Object arg_object, int thisCoroutine, int toCoroutine) throws NoSuchMethodException {
      if (!this.m_activeIDs.get(toCoroutine)) {
         throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[]{Integer.toString(toCoroutine)}));
      } else {
         this.m_yield = arg_object;
         this.m_nextCoroutine = toCoroutine;
         this.m_activeIDs.clear(thisCoroutine);
         this.notify();
      }
   }
}
