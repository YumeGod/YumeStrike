package org.apache.fop.layoutmgr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.traits.MinOptMax;

public abstract class UnresolvedListElementWithLength extends UnresolvedListElement {
   protected static Log log;
   private MinOptMax length;
   private boolean conditional;
   private RelSide side;
   private boolean isFirst;
   private boolean isLast;

   public UnresolvedListElementWithLength(Position position, MinOptMax length, RelSide side, boolean conditional, boolean isFirst, boolean isLast) {
      super(position);
      this.length = length;
      this.side = side;
      this.conditional = conditional;
      this.isFirst = isFirst;
      this.isLast = isLast;
   }

   public boolean isConditional() {
      return this.conditional;
   }

   public MinOptMax getLength() {
      return this.length;
   }

   public RelSide getSide() {
      return this.side;
   }

   public boolean isFirst() {
      return this.isFirst;
   }

   public boolean isLast() {
      return this.isLast;
   }

   public abstract void notifyLayoutManager(MinOptMax var1);

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append(this.getSide().getName()).append(", ");
      sb.append(this.length.toString());
      if (this.isConditional()) {
         sb.append("[discard]");
      } else {
         sb.append("[RETAIN]");
      }

      if (this.isFirst()) {
         sb.append("[first]");
      }

      if (this.isLast()) {
         sb.append("[last]");
      }

      return sb.toString();
   }

   static {
      log = LogFactory.getLog(UnresolvedListElementWithLength.class);
   }
}
