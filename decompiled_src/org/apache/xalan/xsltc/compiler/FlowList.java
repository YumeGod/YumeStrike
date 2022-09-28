package org.apache.xalan.xsltc.compiler;

import java.util.Iterator;
import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

public final class FlowList {
   private Vector _elements;

   public FlowList() {
      this._elements = null;
   }

   public FlowList(InstructionHandle bh) {
      this._elements = new Vector();
      this._elements.addElement(bh);
   }

   public FlowList(FlowList list) {
      this._elements = list._elements;
   }

   public FlowList add(InstructionHandle bh) {
      if (this._elements == null) {
         this._elements = new Vector();
      }

      this._elements.addElement(bh);
      return this;
   }

   public FlowList append(FlowList right) {
      if (this._elements == null) {
         this._elements = right._elements;
      } else {
         Vector temp = right._elements;
         if (temp != null) {
            int n = temp.size();

            for(int i = 0; i < n; ++i) {
               this._elements.addElement(temp.elementAt(i));
            }
         }
      }

      return this;
   }

   public void backPatch(InstructionHandle target) {
      if (this._elements != null) {
         int n = this._elements.size();

         for(int i = 0; i < n; ++i) {
            BranchHandle bh = (BranchHandle)this._elements.elementAt(i);
            bh.setTarget(target);
         }

         this._elements.clear();
      }

   }

   public FlowList copyAndRedirect(InstructionList oldList, InstructionList newList) {
      FlowList result = new FlowList();
      if (this._elements == null) {
         return result;
      } else {
         int n = this._elements.size();
         Iterator oldIter = oldList.iterator();
         Iterator newIter = newList.iterator();

         while(oldIter.hasNext()) {
            InstructionHandle oldIh = (InstructionHandle)oldIter.next();
            InstructionHandle newIh = (InstructionHandle)newIter.next();

            for(int i = 0; i < n; ++i) {
               if (this._elements.elementAt(i) == oldIh) {
                  result.add(newIh);
               }
            }
         }

         return result;
      }
   }
}
