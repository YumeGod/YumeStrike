package org.apache.fop.render.rtf.rtflib.exceptions;

import java.io.IOException;

public class RtfException extends IOException {
   public RtfException(String reason) {
      super(reason);
   }
}
