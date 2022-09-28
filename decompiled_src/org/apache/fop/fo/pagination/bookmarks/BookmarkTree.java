package org.apache.fop.fo.pagination.bookmarks;

import java.util.ArrayList;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.pagination.Root;
import org.xml.sax.Locator;

public class BookmarkTree extends FObj {
   private ArrayList bookmarks = new ArrayList();

   public BookmarkTree(FONode parent) {
      super(parent);
   }

   protected void addChildNode(FONode obj) {
      if (obj instanceof Bookmark) {
         this.bookmarks.add(obj);
      }

   }

   protected void endOfNode() throws FOPException {
      if (this.bookmarks == null) {
         this.missingChildElementError("(fo:bookmark+)");
      }

      ((Root)this.parent).setBookmarkTree(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !localName.equals("bookmark")) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public ArrayList getBookmarks() {
      return this.bookmarks;
   }

   public String getLocalName() {
      return "bookmark-tree";
   }

   public int getNameId() {
      return 5;
   }
}
