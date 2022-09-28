package org.apache.fop.render.txt;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.fop.area.CTM;

public class TXTState {
   private LinkedList stackCTM = new LinkedList();
   private CTM resultCTM = new CTM();

   private void updateResultCTM(CTM ctm) {
      this.resultCTM = this.resultCTM.multiply(ctm);
   }

   private void calcResultCTM() {
      this.resultCTM = new CTM();
      Iterator i = this.stackCTM.iterator();

      while(i.hasNext()) {
         this.updateResultCTM((CTM)i.next());
      }

   }

   public void push(CTM ctm) {
      this.stackCTM.addLast(ctm);
      this.updateResultCTM(ctm);
   }

   public void pop() {
      this.stackCTM.removeLast();
      this.calcResultCTM();
   }

   public CTM refineCTM(CTM ctm) {
      double[] da = ctm.toArray();
      da[4] = (double)Helper.roundPosition((int)da[4], 6000);
      da[5] = (double)Helper.roundPosition((int)da[5], 7860);
      return new CTM(da[0], da[1], da[2], da[3], da[4], da[5]);
   }

   public Point transformPoint(Point p, CTM ctm) {
      Rectangle2D r = new Rectangle2D.Double((double)p.x, (double)p.y, 0.0, 0.0);
      CTM nctm = this.refineCTM(ctm);
      Rectangle2D r = nctm.transform(r);
      return new Point((int)r.getX(), (int)r.getY());
   }

   public Point transformPoint(int x, int y) {
      return this.transformPoint(new Point(x, y), this.resultCTM);
   }

   public CTM getResultCTM() {
      return this.resultCTM;
   }
}
