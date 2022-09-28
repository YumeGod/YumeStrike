package org.apache.batik.extension.svg;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;
import org.apache.batik.gvt.renderer.StrokingTextPainter;

public class FlowExtTextPainter extends StrokingTextPainter {
   protected static TextPainter singleton = new FlowExtTextPainter();

   public static TextPainter getInstance() {
      return singleton;
   }

   public List getTextRuns(TextNode var1, AttributedCharacterIterator var2) {
      List var3 = var1.getTextRuns();
      if (var3 != null) {
         return var3;
      } else {
         AttributedCharacterIterator[] var4 = this.getTextChunkACIs(var2);
         var3 = this.computeTextRuns(var1, var2, var4);
         var2.first();
         List var5 = (List)var2.getAttribute(FLOW_REGIONS);
         if (var5 != null) {
            Iterator var6 = var3.iterator();
            ArrayList var7 = new ArrayList();
            StrokingTextPainter.TextRun var8 = (StrokingTextPainter.TextRun)var6.next();
            ArrayList var9 = new ArrayList();
            var7.add(var9);
            var9.add(var8.getLayout());

            for(; var6.hasNext(); var9.add(var8.getLayout())) {
               var8 = (StrokingTextPainter.TextRun)var6.next();
               if (var8.isFirstRunInChunk()) {
                  var9 = new ArrayList();
                  var7.add(var9);
               }
            }

            FlowExtGlyphLayout.textWrapTextChunk(var4, var7, var5);
         }

         var1.setTextRuns(var3);
         return var3;
      }
   }
}
