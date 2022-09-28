package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.PercentBase;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.TableColLength;

public class PPColWidthFunction extends FunctionBase {
   public int nbArgs() {
      return 1;
   }

   public PercentBase getPercentBase() {
      return new PPColWidthPercentBase();
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      Number d = args[0].getNumber();
      if (d == null) {
         throw new PropertyException("Non numeric operand to proportional-column-width() function.");
      } else {
         PropertyList pList = pInfo.getPropertyList();
         if (!"fo:table-column".equals(pList.getFObj().getName())) {
            throw new PropertyException("proportional-column-width() function may only be used on fo:table-column.");
         } else {
            Table t = (Table)pList.getParentFObj();
            if (t.isAutoLayout()) {
               throw new PropertyException("proportional-column-width() function may only be used when fo:table has table-layout=\"fixed\".");
            } else {
               return new TableColLength(d.doubleValue(), pInfo.getFO());
            }
         }
      }
   }

   private static class PPColWidthPercentBase implements PercentBase {
      private PPColWidthPercentBase() {
      }

      public int getBaseLength(PercentBaseContext context) throws PropertyException {
         return 0;
      }

      public double getBaseValue() {
         return 1.0;
      }

      public int getDimension() {
         return 0;
      }

      // $FF: synthetic method
      PPColWidthPercentBase(Object x0) {
         this();
      }
   }
}
