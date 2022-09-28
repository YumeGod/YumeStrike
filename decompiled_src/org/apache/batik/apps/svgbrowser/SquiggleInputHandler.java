package org.apache.batik.apps.svgbrowser;

import java.io.File;
import org.apache.batik.util.ParsedURL;

public interface SquiggleInputHandler {
   String[] getHandledMimeTypes();

   String[] getHandledExtensions();

   String getDescription();

   boolean accept(File var1);

   boolean accept(ParsedURL var1);

   void handle(ParsedURL var1, JSVGViewerFrame var2) throws Exception;
}
