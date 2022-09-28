package org.apache.fop.layoutmgr.inline;

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.fo.flow.BidiOverride;

public class BidiLayoutManager extends LeafNodeLayoutManager {
   private List children;

   public BidiLayoutManager(BidiOverride node, InlineLayoutManager cLM) {
      super(node);
      this.setParent(cLM);
      this.children = new ArrayList();
   }

   public int size() {
      return this.children.size();
   }

   public InlineArea get(int index) {
      return (InlineArea)this.children.get(index);
   }
}
