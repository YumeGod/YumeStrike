package org.apache.fop.fo.expr;

import java.util.List;
import org.apache.fop.fo.FOPropertyMapping;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.flow.table.ColumnNumberManager;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableCell;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.flow.table.TableFObj;
import org.apache.fop.fo.properties.Property;

public class FromTableColumnFunction extends FunctionBase {
   public int nbArgs() {
      return 1;
   }

   public boolean padArgsWithPropertyName() {
      return true;
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      FObj fo = pInfo.getPropertyList().getFObj();
      int propId = false;
      int propId;
      if (args.length == 0) {
         propId = pInfo.getPropertyMaker().getPropId();
      } else {
         String propName = args[0].getString();
         propId = FOPropertyMapping.getPropertyId(propName);
      }

      if (propId == -1) {
         throw new PropertyException("Incorrect parameter to from-table-column() function");
      } else {
         int columnNumber = true;
         int span = false;
         int columnNumber;
         int span;
         if (fo.getNameId() == 75) {
            columnNumber = pInfo.getPropertyList().get(76).getNumeric().getValue();
            span = pInfo.getPropertyList().get(165).getNumeric().getValue();
         } else {
            do {
               fo = (FObj)fo.getParent();
            } while(fo.getNameId() != 75 && fo.getNameId() != 53);

            if (fo.getNameId() != 75) {
               throw new PropertyException("from-table-column() may only be used on fo:table-cell or its descendants.");
            }

            columnNumber = ((TableCell)fo).getColumnNumber();
            span = ((TableCell)fo).getNumberColumnsSpanned();
         }

         Table t = ((TableFObj)fo).getTable();
         List cols = t.getColumns();
         ColumnNumberManager columnIndexManager = t.getColumnNumberManager();
         if (cols == null) {
            return pInfo.getPropertyList().get(propId, false, true);
         } else if (columnIndexManager.isColumnNumberUsed(columnNumber)) {
            return ((TableColumn)cols.get(columnNumber - 1)).getProperty(propId);
         } else {
            do {
               --span;
               if (span <= 0) {
                  break;
               }

               ++columnNumber;
            } while(!columnIndexManager.isColumnNumberUsed(columnNumber));

            return columnIndexManager.isColumnNumberUsed(columnNumber) ? ((TableColumn)cols.get(columnNumber - 1)).getProperty(propId) : pInfo.getPropertyList().get(propId, false, true);
         }
      }
   }
}
