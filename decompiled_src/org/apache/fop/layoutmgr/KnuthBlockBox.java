package org.apache.fop.layoutmgr;

import java.util.LinkedList;
import java.util.List;
import org.apache.fop.traits.MinOptMax;

public class KnuthBlockBox extends KnuthBox {
   private MinOptMax ipdRange;
   private int bpd;
   private List footnoteList;
   private List elementLists = null;

   public KnuthBlockBox(int width, MinOptMax range, int bpdim, Position pos, boolean auxiliary) {
      super(width, pos, auxiliary);
      this.ipdRange = range;
      this.bpd = bpdim;
      this.footnoteList = new LinkedList();
   }

   public KnuthBlockBox(int width, List list, Position pos, boolean auxiliary) {
      super(width, pos, auxiliary);
      this.ipdRange = MinOptMax.ZERO;
      this.bpd = 0;
      this.footnoteList = new LinkedList(list);
   }

   public List getFootnoteBodyLMs() {
      return this.footnoteList;
   }

   public boolean hasAnchors() {
      return this.footnoteList.size() > 0;
   }

   public void addElementList(List list) {
      if (this.elementLists == null) {
         this.elementLists = new LinkedList();
      }

      this.elementLists.add(list);
   }

   public List getElementLists() {
      return this.elementLists;
   }

   public MinOptMax getIPDRange() {
      return this.ipdRange;
   }

   public int getBPD() {
      return this.bpd;
   }
}
