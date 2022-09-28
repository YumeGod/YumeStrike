package org.apache.batik.bridge.svg12;

import java.util.ArrayList;
import org.apache.batik.dom.svg12.XBLOMContentElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DefaultContentSelector extends AbstractContentSelector {
   protected SelectedNodes selectedContent;

   public DefaultContentSelector(ContentManager var1, XBLOMContentElement var2, Element var3) {
      super(var1, var2, var3);
   }

   public NodeList getSelectedContent() {
      if (this.selectedContent == null) {
         this.selectedContent = new SelectedNodes();
      }

      return this.selectedContent;
   }

   boolean update() {
      if (this.selectedContent == null) {
         this.selectedContent = new SelectedNodes();
         return true;
      } else {
         return this.selectedContent.update();
      }
   }

   protected class SelectedNodes implements NodeList {
      protected ArrayList nodes = new ArrayList(10);

      public SelectedNodes() {
         this.update();
      }

      protected boolean update() {
         ArrayList var1 = (ArrayList)this.nodes.clone();
         this.nodes.clear();

         for(Node var2 = DefaultContentSelector.this.boundElement.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            if (!DefaultContentSelector.this.isSelected(var2)) {
               this.nodes.add(var2);
            }
         }

         int var4 = this.nodes.size();
         if (var1.size() != var4) {
            return true;
         } else {
            for(int var3 = 0; var3 < var4; ++var3) {
               if (var1.get(var3) != this.nodes.get(var3)) {
                  return true;
               }
            }

            return false;
         }
      }

      public Node item(int var1) {
         return var1 >= 0 && var1 < this.nodes.size() ? (Node)this.nodes.get(var1) : null;
      }

      public int getLength() {
         return this.nodes.size();
      }
   }
}
