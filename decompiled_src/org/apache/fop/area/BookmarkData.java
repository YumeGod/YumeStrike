package org.apache.fop.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.fop.fo.pagination.bookmarks.Bookmark;
import org.apache.fop.fo.pagination.bookmarks.BookmarkTree;

public class BookmarkData extends AbstractOffDocumentItem implements Resolvable {
   private List subData = new ArrayList();
   private String bookmarkTitle = null;
   private boolean bShow = true;
   private String idRef;
   private PageViewport pageRef = null;
   private Map unresolvedIDRefs = new HashMap();

   public BookmarkData(BookmarkTree bookmarkTree) {
      this.idRef = null;
      this.whenToProcess = 2;
      this.bShow = true;

      for(int count = 0; count < bookmarkTree.getBookmarks().size(); ++count) {
         Bookmark bkmk = (Bookmark)bookmarkTree.getBookmarks().get(count);
         this.addSubData(this.createBookmarkData(bkmk));
      }

   }

   public BookmarkData(Bookmark bookmark) {
      this.bookmarkTitle = bookmark.getBookmarkTitle();
      this.bShow = bookmark.showChildItems();
      this.idRef = bookmark.getInternalDestination();
   }

   private void putUnresolved(String id, BookmarkData bd) {
      List refs = (List)this.unresolvedIDRefs.get(id);
      if (refs == null) {
         refs = new ArrayList();
         this.unresolvedIDRefs.put(id, refs);
      }

      ((List)refs).add(bd);
   }

   public BookmarkData() {
      this.idRef = null;
      this.whenToProcess = 2;
      this.bShow = true;
   }

   public BookmarkData(String title, boolean showChildren, PageViewport pv, String idRef) {
      this.bookmarkTitle = title;
      this.bShow = showChildren;
      this.pageRef = pv;
      this.idRef = idRef;
   }

   public String getIDRef() {
      return this.idRef;
   }

   public void addSubData(BookmarkData sub) {
      this.subData.add(sub);
      if (sub.pageRef == null || sub.pageRef.equals("")) {
         this.putUnresolved(sub.getIDRef(), sub);
         String[] ids = sub.getIDRefs();

         for(int count = 0; count < ids.length; ++count) {
            this.putUnresolved(ids[count], sub);
         }
      }

   }

   public String getBookmarkTitle() {
      return this.bookmarkTitle;
   }

   public boolean showChildItems() {
      return this.bShow;
   }

   public int getCount() {
      return this.subData.size();
   }

   public BookmarkData getSubData(int count) {
      return (BookmarkData)this.subData.get(count);
   }

   public PageViewport getPageViewport() {
      return this.pageRef;
   }

   public boolean isResolved() {
      return this.unresolvedIDRefs == null || this.unresolvedIDRefs.size() == 0;
   }

   public String[] getIDRefs() {
      return (String[])this.unresolvedIDRefs.keySet().toArray(new String[0]);
   }

   public void resolveIDRef(String id, List pages) {
      if (id.equals(this.idRef)) {
         this.pageRef = (PageViewport)pages.get(0);
      }

      Collection refs = (Collection)this.unresolvedIDRefs.get(id);
      if (refs != null) {
         Iterator iter = refs.iterator();

         while(iter.hasNext()) {
            BookmarkData bd = (BookmarkData)iter.next();
            bd.resolveIDRef(id, pages);
         }
      }

      this.unresolvedIDRefs.remove(id);
   }

   public String getName() {
      return "Bookmarks";
   }

   private BookmarkData createBookmarkData(Bookmark bookmark) {
      BookmarkData data = new BookmarkData(bookmark);

      for(int count = 0; count < bookmark.getChildBookmarks().size(); ++count) {
         Bookmark bkmk = (Bookmark)bookmark.getChildBookmarks().get(count);
         data.addSubData(this.createBookmarkData(bkmk));
      }

      return data;
   }
}
