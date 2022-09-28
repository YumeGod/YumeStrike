package org.apache.fop.area;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainReference extends Area {
   private BodyRegion parent;
   private List spanAreas = new ArrayList();
   private boolean isEmpty = true;

   public MainReference(BodyRegion parent) {
      this.parent = parent;
      this.addTrait(Trait.IS_REFERENCE_AREA, Boolean.TRUE);
   }

   public Span createSpan(boolean spanAll) {
      if (this.spanAreas.size() > 0 && this.getCurrentSpan().isEmpty()) {
         this.spanAreas.remove(this.spanAreas.size() - 1);
      }

      RegionViewport rv = this.parent.getRegionViewport();
      int ipdWidth = this.parent.getIPD() - rv.getBorderAndPaddingWidthStart() - rv.getBorderAndPaddingWidthEnd();
      Span newSpan = new Span(spanAll ? 1 : this.getColumnCount(), this.getColumnGap(), ipdWidth);
      this.spanAreas.add(newSpan);
      return this.getCurrentSpan();
   }

   public List getSpans() {
      return this.spanAreas;
   }

   public void setSpans(List spans) {
      this.spanAreas = new ArrayList(spans);
   }

   public Span getCurrentSpan() {
      return (Span)this.spanAreas.get(this.spanAreas.size() - 1);
   }

   public boolean isEmpty() {
      if (this.isEmpty) {
         boolean nonEmptyFound = false;
         Span spanArea;
         if (this.spanAreas != null) {
            for(Iterator spaniter = this.spanAreas.iterator(); spaniter.hasNext(); nonEmptyFound |= !spanArea.isEmpty()) {
               spanArea = (Span)spaniter.next();
            }
         }

         this.isEmpty = !nonEmptyFound;
      }

      return this.isEmpty;
   }

   public int getColumnCount() {
      return this.parent.getColumnCount();
   }

   public int getColumnGap() {
      return this.parent.getColumnGap();
   }
}
