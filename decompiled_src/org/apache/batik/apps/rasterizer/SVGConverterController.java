package org.apache.batik.apps.rasterizer;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.apache.batik.transcoder.Transcoder;

public interface SVGConverterController {
   boolean proceedWithComputedTask(Transcoder var1, Map var2, List var3, List var4);

   boolean proceedWithSourceTranscoding(SVGConverterSource var1, File var2);

   boolean proceedOnSourceTranscodingFailure(SVGConverterSource var1, File var2, String var3);

   void onSourceTranscodingSuccess(SVGConverterSource var1, File var2);
}
