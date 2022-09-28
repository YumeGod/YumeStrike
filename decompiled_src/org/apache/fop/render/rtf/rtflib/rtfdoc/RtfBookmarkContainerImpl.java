package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfBookmarkContainerImpl extends RtfContainer implements IRtfBookmarkContainer {
   private RtfBookmark mBookmark = null;

   RtfBookmarkContainerImpl(RtfContainer parent, Writer w) throws IOException {
      super(parent, w, (RtfAttributes)null);
   }

   RtfBookmarkContainerImpl(RtfContainer parent, Writer w, RtfAttributes attr) throws IOException {
      super(parent, w, attr);
   }

   public RtfBookmark newBookmark(String bookmark) throws IOException {
      if (this.mBookmark != null) {
         this.mBookmark.close();
      }

      this.mBookmark = new RtfBookmark(this, this.writer, bookmark);
      return this.mBookmark;
   }
}
