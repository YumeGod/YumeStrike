package org.apache.fop.render.rtf;

import java.io.OutputStream;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.FOEventHandler;
import org.apache.fop.render.AbstractFOEventHandlerMaker;

public class RTFFOEventHandlerMaker extends AbstractFOEventHandlerMaker {
   private static final String[] MIMES = new String[]{"application/rtf", "text/richtext", "text/rtf"};

   public FOEventHandler makeFOEventHandler(FOUserAgent ua, OutputStream out) {
      return new RTFHandler(ua, out);
   }

   public boolean needsOutputStream() {
      return true;
   }

   public String[] getSupportedMimeTypes() {
      return MIMES;
   }
}
