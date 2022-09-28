package org.apache.fop.layoutmgr;

import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;
import org.apache.fop.fo.pagination.PageProductionException;
import org.xml.sax.Locator;

public interface BlockLevelEventProducer extends EventProducer {
   void rowTooTall(Object var1, int var2, int var3, int var4, Locator var5);

   void tableFixedAutoWidthNotSupported(Object var1, Locator var2);

   void objectTooWide(Object var1, String var2, int var3, int var4, Locator var5);

   void overconstrainedAdjustEndIndent(Object var1, String var2, int var3, Locator var4);

   void viewportOverflow(Object var1, String var2, int var3, boolean var4, boolean var5, Locator var6) throws LayoutException;

   void regionOverflow(Object var1, String var2, String var3, int var4, boolean var5, boolean var6, Locator var7) throws LayoutException;

   void flowNotMappingToRegionBody(Object var1, String var2, String var3, Locator var4) throws UnsupportedOperationException;

   void pageSequenceMasterExhausted(Object var1, String var2, boolean var3, Locator var4) throws PageProductionException;

   void missingSubsequencesInPageSequenceMaster(Object var1, String var2, Locator var3) throws PageProductionException;

   void noMatchingPageMaster(Object var1, String var2, String var3, Locator var4) throws PageProductionException;

   void nonRestartableContentFlowingToNarrowerPage(Object var1);

   public static final class Provider {
      private Provider() {
      }

      public static BlockLevelEventProducer get(EventBroadcaster broadcaster) {
         return (BlockLevelEventProducer)broadcaster.getEventProducerFor(BlockLevelEventProducer.class);
      }
   }
}
