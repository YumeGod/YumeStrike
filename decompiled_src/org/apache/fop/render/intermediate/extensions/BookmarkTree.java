package org.apache.fop.render.intermediate.extensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookmarkTree {
   private List bookmarks = new ArrayList();

   public void addBookmark(Bookmark bookmark) {
      this.bookmarks.add(bookmark);
   }

   public List getBookmarks() {
      return Collections.unmodifiableList(this.bookmarks);
   }
}
