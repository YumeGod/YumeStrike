package org.apache.fop.render.rtf.rtflib.tools;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;
import org.apache.fop.render.rtf.rtflib.rtfdoc.ITableColumnsInfo;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfAttributes;

public class TableContext implements ITableColumnsInfo {
   private final Log log = new SimpleLog("FOP/RTF");
   private final BuilderContext context;
   private final List colWidths = new ArrayList();
   private int colIndex;
   private final List colRowSpanningNumber = new ArrayList();
   private final List colRowSpanningAttrs = new ArrayList();
   private final List colFirstSpanningCol = new ArrayList();
   private boolean bNextRowBelongsToHeader = false;

   public void setNextRowBelongsToHeader(boolean value) {
      this.bNextRowBelongsToHeader = value;
   }

   public boolean getNextRowBelongsToHeader() {
      return this.bNextRowBelongsToHeader;
   }

   public TableContext(BuilderContext ctx) {
      this.context = ctx;
   }

   public void setNextColumnWidth(Float width) {
      this.colWidths.add(width);
   }

   public RtfAttributes getColumnRowSpanningAttrs() {
      return (RtfAttributes)this.colRowSpanningAttrs.get(this.colIndex);
   }

   public Integer getColumnRowSpanningNumber() {
      return (Integer)this.colRowSpanningNumber.get(this.colIndex);
   }

   public boolean getFirstSpanningCol() {
      Boolean b = (Boolean)this.colFirstSpanningCol.get(this.colIndex);
      return b;
   }

   public void setCurrentColumnRowSpanning(Integer iRowSpanning, RtfAttributes attrs) {
      if (this.colIndex < this.colRowSpanningNumber.size()) {
         this.colRowSpanningNumber.set(this.colIndex, iRowSpanning);
         this.colRowSpanningAttrs.set(this.colIndex, attrs);
      } else {
         this.colRowSpanningNumber.add(iRowSpanning);
         this.colRowSpanningAttrs.add(this.colIndex, attrs);
      }

   }

   public void setNextColumnRowSpanning(Integer iRowSpanning, RtfAttributes attrs) {
      this.colRowSpanningNumber.add(iRowSpanning);
      this.colRowSpanningAttrs.add(this.colIndex, attrs);
   }

   public void setCurrentFirstSpanningCol(boolean bFirstSpanningCol) {
      if (this.colIndex >= this.colRowSpanningNumber.size()) {
         this.colFirstSpanningCol.add(new Boolean(bFirstSpanningCol));
      } else {
         while(true) {
            if (this.colIndex < this.colFirstSpanningCol.size()) {
               this.colFirstSpanningCol.set(this.colIndex, new Boolean(bFirstSpanningCol));
               break;
            }

            this.setNextFirstSpanningCol(false);
         }
      }

   }

   public void setNextFirstSpanningCol(boolean bFirstSpanningCol) {
      this.colFirstSpanningCol.add(new Boolean(bFirstSpanningCol));
   }

   public void decreaseRowSpannings() {
      for(int z = 0; z < this.colRowSpanningNumber.size(); ++z) {
         Integer i = (Integer)this.colRowSpanningNumber.get(z);
         if (i > 0) {
            i = new Integer(i - 1);
         }

         this.colRowSpanningNumber.set(z, i);
         if (i == 0) {
            this.colRowSpanningAttrs.set(z, (Object)null);
            this.colFirstSpanningCol.set(z, new Boolean(false));
         }
      }

   }

   public void selectFirstColumn() {
      this.colIndex = 0;
   }

   public void selectNextColumn() {
      ++this.colIndex;
   }

   public float getColumnWidth() {
      if (this.colIndex < 0) {
         throw new IllegalStateException("colIndex must not be negative!");
      } else {
         if (this.colIndex >= this.getNumberOfColumns()) {
            this.log.warn("Column width for column " + (this.colIndex + 1) + " is not defined, using " + 200.0F);

            while(this.colIndex >= this.getNumberOfColumns()) {
               this.setNextColumnWidth(new Float(200.0F));
            }
         }

         return (Float)this.colWidths.get(this.colIndex);
      }
   }

   public void setColumnIndex(int index) {
      this.colIndex = index;
   }

   public int getColumnIndex() {
      return this.colIndex;
   }

   public int getNumberOfColumns() {
      return this.colWidths.size();
   }
}
