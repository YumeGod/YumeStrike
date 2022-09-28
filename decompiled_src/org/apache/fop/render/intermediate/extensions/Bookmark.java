package org.apache.fop.render.intermediate.extensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bookmark {
   private String title;
   private boolean show;
   private List childBookmarks;
   private AbstractAction action;

   public Bookmark(String title, boolean show, AbstractAction action) {
      this.title = title;
      this.show = show;
      this.action = action;
   }

   public String getTitle() {
      return this.title;
   }

   public boolean isShown() {
      return this.show;
   }

   public AbstractAction getAction() {
      return this.action;
   }

   public void setAction(AbstractAction action) {
      this.action = action;
   }

   public void addChildBookmark(Bookmark bookmark) {
      if (this.childBookmarks == null) {
         this.childBookmarks = new ArrayList();
      }

      this.childBookmarks.add(bookmark);
   }

   public List getChildBookmarks() {
      return this.childBookmarks == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(this.childBookmarks);
   }
}
