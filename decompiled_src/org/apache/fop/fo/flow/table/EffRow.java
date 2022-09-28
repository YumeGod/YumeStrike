package org.apache.fop.fo.flow.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.fop.layoutmgr.Keep;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.util.BreakUtil;

public class EffRow {
   public static final int FIRST_IN_PART = 0;
   public static final int LAST_IN_PART = 1;
   private List gridUnits = new ArrayList();
   private int index;
   private int bodyType;
   private MinOptMax height;
   private MinOptMax explicitHeight;

   public EffRow(int index, int bodyType, List gridUnits) {
      this.index = index;
      this.bodyType = bodyType;
      this.gridUnits = gridUnits;
      Iterator guIter = gridUnits.iterator();

      while(guIter.hasNext()) {
         Object gu = guIter.next();
         if (gu instanceof PrimaryGridUnit) {
            ((PrimaryGridUnit)gu).setRowIndex(index);
         }
      }

   }

   public int getIndex() {
      return this.index;
   }

   public int getBodyType() {
      return this.bodyType;
   }

   public TableRow getTableRow() {
      return this.getGridUnit(0).getRow();
   }

   public MinOptMax getHeight() {
      return this.height;
   }

   public void setHeight(MinOptMax mom) {
      this.height = mom;
   }

   public MinOptMax getExplicitHeight() {
      return this.explicitHeight;
   }

   public void setExplicitHeight(MinOptMax mom) {
      this.explicitHeight = mom;
   }

   public List getGridUnits() {
      return this.gridUnits;
   }

   public GridUnit getGridUnit(int column) {
      return (GridUnit)this.gridUnits.get(column);
   }

   public GridUnit safelyGetGridUnit(int column) {
      return column < this.gridUnits.size() ? (GridUnit)this.gridUnits.get(column) : null;
   }

   public boolean getFlag(int which) {
      if (which == 0) {
         return this.getGridUnit(0).getFlag(0);
      } else if (which == 1) {
         return this.getGridUnit(0).getFlag(1);
      } else {
         throw new IllegalArgumentException("Illegal flag queried: " + which);
      }
   }

   public Keep getKeepWithPrevious() {
      Keep keep = Keep.KEEP_AUTO;
      TableRow row = this.getTableRow();
      if (row != null) {
         keep = Keep.getKeep(row.getKeepWithPrevious());
      }

      Iterator iter = this.gridUnits.iterator();

      while(iter.hasNext()) {
         GridUnit gu = (GridUnit)iter.next();
         if (gu.isPrimary()) {
            keep = keep.compare(gu.getPrimary().getKeepWithPrevious());
         }
      }

      return keep;
   }

   public Keep getKeepWithNext() {
      Keep keep = Keep.KEEP_AUTO;
      TableRow row = this.getTableRow();
      if (row != null) {
         keep = Keep.getKeep(row.getKeepWithNext());
      }

      Iterator iter = this.gridUnits.iterator();

      while(iter.hasNext()) {
         GridUnit gu = (GridUnit)iter.next();
         if (!gu.isEmpty() && gu.getColSpanIndex() == 0 && gu.isLastGridUnitRowSpan()) {
            keep = keep.compare(gu.getPrimary().getKeepWithNext());
         }
      }

      return keep;
   }

   public Keep getKeepTogether() {
      TableRow row = this.getTableRow();
      Keep keep = Keep.KEEP_AUTO;
      if (row != null) {
         keep = Keep.getKeep(row.getKeepTogether());
      }

      return keep;
   }

   public int getBreakBefore() {
      int breakBefore = 9;
      Iterator iter = this.gridUnits.iterator();

      while(iter.hasNext()) {
         GridUnit gu = (GridUnit)iter.next();
         if (gu.isPrimary()) {
            breakBefore = BreakUtil.compareBreakClasses(breakBefore, gu.getPrimary().getBreakBefore());
         }
      }

      return breakBefore;
   }

   public int getBreakAfter() {
      int breakAfter = 9;
      Iterator iter = this.gridUnits.iterator();

      while(iter.hasNext()) {
         GridUnit gu = (GridUnit)iter.next();
         if (!gu.isEmpty() && gu.getColSpanIndex() == 0 && gu.isLastGridUnitRowSpan()) {
            breakAfter = BreakUtil.compareBreakClasses(breakAfter, gu.getPrimary().getBreakAfter());
         }
      }

      return breakAfter;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("EffRow {");
      sb.append(this.index);
      if (this.getBodyType() == 0) {
         sb.append(" in body");
      } else if (this.getBodyType() == 1) {
         sb.append(" in header");
      } else {
         sb.append(" in footer");
      }

      sb.append(", ").append(this.height);
      sb.append(", ").append(this.explicitHeight);
      sb.append(", ").append(this.gridUnits.size()).append(" gu");
      sb.append("}");
      return sb.toString();
   }
}
