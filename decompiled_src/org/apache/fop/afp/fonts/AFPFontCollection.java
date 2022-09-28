package org.apache.fop.afp.fonts;

import java.util.Iterator;
import java.util.List;
import org.apache.fop.afp.AFPEventProducer;
import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;

public class AFPFontCollection implements FontCollection {
   private final EventBroadcaster eventBroadcaster;
   private final List fontInfoList;

   public AFPFontCollection(EventBroadcaster eventBroadcaster, List fontInfoList) {
      this.eventBroadcaster = eventBroadcaster;
      this.fontInfoList = fontInfoList;
   }

   public int setup(int start, FontInfo fontInfo) {
      int num = 1;
      AFPEventProducer eventProducer = AFPEventProducer.Provider.get(this.eventBroadcaster);
      if (this.fontInfoList != null && this.fontInfoList.size() > 0) {
         Iterator it = this.fontInfoList.iterator();

         while(it.hasNext()) {
            AFPFontInfo afpFontInfo = (AFPFontInfo)it.next();
            AFPFont afpFont = afpFontInfo.getAFPFont();
            List tripletList = afpFontInfo.getFontTriplets();

            for(Iterator it2 = tripletList.iterator(); it2.hasNext(); ++num) {
               FontTriplet triplet = (FontTriplet)it2.next();
               fontInfo.addFontProperties("F" + num, triplet.getName(), triplet.getStyle(), triplet.getWeight());
               fontInfo.addMetrics("F" + num, afpFont);
            }
         }

         this.checkDefaultFontAvailable(fontInfo, eventProducer, "normal", 400);
         this.checkDefaultFontAvailable(fontInfo, eventProducer, "italic", 400);
         this.checkDefaultFontAvailable(fontInfo, eventProducer, "normal", 700);
         this.checkDefaultFontAvailable(fontInfo, eventProducer, "italic", 700);
      } else {
         eventProducer.warnDefaultFontSetup(this);
         FontCollection base12FontCollection = new AFPBase12FontCollection();
         num = base12FontCollection.setup(num, fontInfo);
      }

      return num;
   }

   private void checkDefaultFontAvailable(FontInfo fontInfo, AFPEventProducer eventProducer, String style, int weight) {
      if (!fontInfo.hasFont("any", style, weight)) {
         eventProducer.warnMissingDefaultFont(this, style, weight);
      }

   }
}
