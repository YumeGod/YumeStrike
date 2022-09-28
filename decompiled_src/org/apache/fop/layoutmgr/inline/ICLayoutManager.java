package org.apache.fop.layoutmgr.inline;

import java.util.List;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.fo.flow.InlineContainer;

public class ICLayoutManager extends LeafNodeLayoutManager {
   private List childrenLM;

   public ICLayoutManager(InlineContainer node, List childLM) {
      super(node);
      this.childrenLM = childLM;
   }

   public InlineArea get(int index) {
      return null;
   }
}
