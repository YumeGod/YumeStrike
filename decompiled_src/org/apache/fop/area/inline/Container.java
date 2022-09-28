package org.apache.fop.area.inline;

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;

public class Container extends Area {
   protected List blocks = new ArrayList();
   protected int width;

   public void addBlock(Block block) {
      this.blocks.add(block);
   }

   public List getBlocks() {
      return this.blocks;
   }

   public int getWidth() {
      return this.width;
   }
}
