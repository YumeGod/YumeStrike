package org.apache.batik.dom.traversal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;

public class TraversalSupport {
   protected List iterators;

   public static TreeWalker createTreeWalker(AbstractDocument var0, Node var1, int var2, NodeFilter var3, boolean var4) {
      if (var1 == null) {
         throw var0.createDOMException((short)9, "null.root", (Object[])null);
      } else {
         return new DOMTreeWalker(var1, var2, var3, var4);
      }
   }

   public NodeIterator createNodeIterator(AbstractDocument var1, Node var2, int var3, NodeFilter var4, boolean var5) throws DOMException {
      if (var2 == null) {
         throw var1.createDOMException((short)9, "null.root", (Object[])null);
      } else {
         DOMNodeIterator var6 = new DOMNodeIterator(var1, var2, var3, var4, var5);
         if (this.iterators == null) {
            this.iterators = new LinkedList();
         }

         this.iterators.add(var6);
         return var6;
      }
   }

   public void nodeToBeRemoved(Node var1) {
      if (this.iterators != null) {
         Iterator var2 = this.iterators.iterator();

         while(var2.hasNext()) {
            ((DOMNodeIterator)var2.next()).nodeToBeRemoved(var1);
         }
      }

   }

   public void detachNodeIterator(NodeIterator var1) {
      this.iterators.remove(var1);
   }
}
