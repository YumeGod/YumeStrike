package org.apache.bcel.verifier;

import java.util.ArrayList;
import java.util.TreeSet;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class VerifierFactoryListModel implements VerifierFactoryObserver, ListModel {
   private ArrayList listeners = new ArrayList();
   private TreeSet cache = new TreeSet();

   public VerifierFactoryListModel() {
      VerifierFactory.attach(this);
      this.update((String)null);
   }

   public synchronized void update(String s) {
      int size = this.listeners.size();
      Verifier[] verifiers = VerifierFactory.getVerifiers();
      int num_of_verifiers = verifiers.length;
      this.cache.clear();

      for(int i = 0; i < num_of_verifiers; ++i) {
         this.cache.add(verifiers[i].getClassName());
      }

      for(int i = 0; i < size; ++i) {
         ListDataEvent e = new ListDataEvent(this, 0, 0, num_of_verifiers - 1);
         ((ListDataListener)this.listeners.get(i)).contentsChanged(e);
      }

   }

   public synchronized void addListDataListener(ListDataListener l) {
      this.listeners.add(l);
   }

   public synchronized void removeListDataListener(ListDataListener l) {
      this.listeners.remove(l);
   }

   public synchronized int getSize() {
      return this.cache.size();
   }

   public synchronized Object getElementAt(int index) {
      return this.cache.toArray()[index];
   }
}
