package org.apache.fop.fo.flow.table;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.expr.PropertyException;
import org.xml.sax.Locator;

public interface TableEventProducer extends EventProducer {
   void nonAutoBPDOnTable(Object var1, Locator var2);

   void noTablePaddingWithCollapsingBorderModel(Object var1, Locator var2);

   void noMixRowsAndCells(Object var1, String var2, Locator var3) throws ValidationException;

   void footerOrderCannotRecover(Object var1, String var2, Locator var3) throws ValidationException;

   void startEndRowUnderTableRowWarning(Object var1, Locator var2);

   void tooManyCells(Object var1, Locator var2) throws ValidationException;

   void valueMustBeBiggerGtEqOne(Object var1, String var2, int var3, Locator var4) throws PropertyException;

   void warnImplicitColumns(Object var1, Locator var2);

   void paddingNotApplicable(Object var1, String var2, Locator var3);

   void cellOverlap(Object var1, String var2, int var3, Locator var4) throws PropertyException;

   void forceNextColumnNumber(Object var1, String var2, Number var3, int var4, Locator var5);

   void breakIgnoredDueToRowSpanning(Object var1, String var2, boolean var3, Locator var4);

   public static class Provider {
      public static TableEventProducer get(EventBroadcaster broadcaster) {
         return (TableEventProducer)broadcaster.getEventProducerFor(TableEventProducer.class);
      }
   }
}
