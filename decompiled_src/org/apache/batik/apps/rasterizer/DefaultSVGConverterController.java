package org.apache.batik.apps.rasterizer;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.apache.batik.transcoder.Transcoder;

public class DefaultSVGConverterController implements SVGConverterController {
   public boolean proceedWithComputedTask(Transcoder var1, Map var2, List var3, List var4) {
      return true;
   }

   public boolean proceedWithSourceTranscoding(SVGConverterSource var1, File var2) {
      System.out.println("About to transcoder source of type: " + var1.getClass().getName());
      return true;
   }

   public boolean proceedOnSourceTranscodingFailure(SVGConverterSource var1, File var2, String var3) {
      return true;
   }

   public void onSourceTranscodingSuccess(SVGConverterSource var1, File var2) {
   }
}
