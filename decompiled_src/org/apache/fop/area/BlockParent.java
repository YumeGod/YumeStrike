package org.apache.fop.area;

import java.util.ArrayList;
import java.util.List;

public class BlockParent extends Area {
   protected int xOffset = 0;
   protected int yOffset = 0;
   protected List children = null;

   public void addChildArea(Area childArea) {
      if (this.children == null) {
         this.children = new ArrayList();
      }

      this.children.add(childArea);
   }

   public void addBlock(Block block) {
      this.addChildArea(block);
   }

   public List getChildAreas() {
      return this.children;
   }

   public boolean isEmpty() {
      return this.children == null || this.children.size() == 0;
   }

   public void setXOffset(int off) {
      this.xOffset = off;
   }

   public void setYOffset(int off) {
      this.yOffset = off;
   }

   public int getXOffset() {
      return this.xOffset;
   }

   public int getYOffset() {
      return this.yOffset;
   }
}
