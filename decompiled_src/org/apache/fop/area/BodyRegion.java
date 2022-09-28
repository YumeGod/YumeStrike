package org.apache.fop.area;

import java.util.List;
import org.apache.fop.fo.pagination.RegionBody;

public class BodyRegion extends RegionReference {
   private BeforeFloat beforeFloat;
   private MainReference mainReference;
   private Footnote footnote;
   private int columnGap;
   private int columnCount;

   public BodyRegion(RegionBody rb, RegionViewport parent) {
      this(rb.getNameId(), rb.getRegionName(), parent, rb.getColumnCount(), rb.getColumnGap());
   }

   public BodyRegion(int regionClass, String regionName, RegionViewport parent, int columnCount, int columnGap) {
      super(regionClass, regionName, parent);
      this.columnCount = columnCount;
      this.columnGap = columnGap;
      this.mainReference = new MainReference(this);
   }

   public int getColumnCount() {
      return this.columnCount;
   }

   public int getColumnGap() {
      return this.columnGap;
   }

   public MainReference getMainReference() {
      return this.mainReference;
   }

   public boolean isEmpty() {
      return (this.mainReference == null || this.mainReference.isEmpty()) && (this.footnote == null || this.footnote.isEmpty()) && (this.beforeFloat == null || this.beforeFloat.isEmpty());
   }

   public BeforeFloat getBeforeFloat() {
      if (this.beforeFloat == null) {
         this.beforeFloat = new BeforeFloat();
      }

      return this.beforeFloat;
   }

   public Footnote getFootnote() {
      if (this.footnote == null) {
         this.footnote = new Footnote();
      }

      return this.footnote;
   }

   public int getRemainingBPD() {
      int usedBPD = 0;
      List spans = this.getMainReference().getSpans();
      int previousSpanCount = spans.size() - 1;

      for(int i = 0; i < previousSpanCount; ++i) {
         usedBPD += ((Span)spans.get(i)).getHeight();
      }

      return this.getBPD() - usedBPD;
   }

   public Object clone() {
      BodyRegion br = new BodyRegion(this.getRegionClass(), this.getRegionName(), this.regionViewport, this.getColumnCount(), this.getColumnGap());
      br.setCTM(this.getCTM());
      br.setIPD(this.getIPD());
      br.beforeFloat = this.beforeFloat;
      br.mainReference = this.mainReference;
      br.footnote = this.footnote;
      return br;
   }
}
