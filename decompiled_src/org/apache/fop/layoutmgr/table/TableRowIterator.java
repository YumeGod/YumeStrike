package org.apache.fop.layoutmgr.table;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.flow.table.EffRow;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableBody;

public class TableRowIterator {
   public static final int BODY = 0;
   public static final int HEADER = 1;
   public static final int FOOTER = 2;
   protected Table table;
   private int tablePart;
   private Iterator rowGroupsIter;
   private int rowIndex = 0;

   public TableRowIterator(Table table, int tablePart) {
      this.table = table;
      this.tablePart = tablePart;
      switch (tablePart) {
         case 0:
            List rowGroupsList = new LinkedList();
            FONode.FONodeIterator iter = table.getChildNodes();

            while(iter.hasNext()) {
               FONode node = iter.nextNode();
               if (node instanceof TableBody) {
                  rowGroupsList.addAll(((TableBody)node).getRowGroups());
               }
            }

            this.rowGroupsIter = rowGroupsList.iterator();
            break;
         case 1:
            this.rowGroupsIter = table.getTableHeader().getRowGroups().iterator();
            break;
         case 2:
            this.rowGroupsIter = table.getTableFooter().getRowGroups().iterator();
            break;
         default:
            throw new IllegalArgumentException("Unrecognised TablePart: " + tablePart);
      }

   }

   EffRow[] getNextRowGroup() {
      if (!this.rowGroupsIter.hasNext()) {
         return null;
      } else {
         List rowGroup = (List)this.rowGroupsIter.next();
         EffRow[] effRowGroup = new EffRow[rowGroup.size()];
         int i = 0;

         List gridUnits;
         for(Iterator rowIter = rowGroup.iterator(); rowIter.hasNext(); effRowGroup[i++] = new EffRow(this.rowIndex++, this.tablePart, gridUnits)) {
            gridUnits = (List)rowIter.next();
         }

         return effRowGroup;
      }
   }
}
