package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfBookmark extends RtfElement {
   private String bookmark = null;
   public static final int MAX_BOOKMARK_LENGTH = 40;
   public static final char REPLACE_CHARACTER = '_';

   RtfBookmark(RtfContainer parent, Writer w, String bookmark) throws IOException {
      super(parent, w);
      int now = bookmark.length();
      this.bookmark = bookmark.substring(0, now < 40 ? now : 40);
      this.bookmark = this.bookmark.replace('.', '_');
      this.bookmark = this.bookmark.replace(' ', '_');
   }

   public void writeRtfPrefix() throws IOException {
      this.startBookmark();
   }

   public void writeRtfContent() throws IOException {
   }

   public void writeRtfSuffix() throws IOException {
      this.endBookmark();
   }

   private void startBookmark() throws IOException {
      this.writeRtfBookmark("bkmkstart");
   }

   private void endBookmark() throws IOException {
      this.writeRtfBookmark("bkmkend");
   }

   private void writeRtfBookmark(String tag) throws IOException {
      if (this.bookmark != null) {
         this.writeGroupMark(true);
         this.writeStarControlWord(tag);
         this.writer.write(this.bookmark);
         this.writeGroupMark(false);
      }
   }

   public boolean isEmpty() {
      return this.bookmark == null || this.bookmark.trim().length() == 0;
   }
}
