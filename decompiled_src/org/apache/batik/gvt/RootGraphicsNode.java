package org.apache.batik.gvt;

import java.util.LinkedList;
import java.util.List;
import org.apache.batik.gvt.event.GraphicsNodeChangeListener;

public class RootGraphicsNode extends CompositeGraphicsNode {
   List treeGraphicsNodeChangeListeners = null;

   public RootGraphicsNode getRoot() {
      return this;
   }

   public List getTreeGraphicsNodeChangeListeners() {
      if (this.treeGraphicsNodeChangeListeners == null) {
         this.treeGraphicsNodeChangeListeners = new LinkedList();
      }

      return this.treeGraphicsNodeChangeListeners;
   }

   public void addTreeGraphicsNodeChangeListener(GraphicsNodeChangeListener var1) {
      this.getTreeGraphicsNodeChangeListeners().add(var1);
   }

   public void removeTreeGraphicsNodeChangeListener(GraphicsNodeChangeListener var1) {
      this.getTreeGraphicsNodeChangeListeners().remove(var1);
   }
}
