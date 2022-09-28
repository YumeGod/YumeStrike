package org.apache.fop.fo.pagination.bookmarks;

import java.util.ArrayList;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class Bookmark extends FObj {
   private BookmarkTitle bookmarkTitle;
   private ArrayList childBookmarks = new ArrayList();
   private String internalDestination;
   private String externalDestination;
   private boolean bShow = true;

   public Bookmark(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      this.externalDestination = pList.get(94).getString();
      this.internalDestination = pList.get(128).getString();
      this.bShow = pList.get(234).getEnum() == 130;
      if (this.internalDestination.length() > 0) {
         this.externalDestination = null;
      } else if (this.externalDestination.length() == 0) {
         this.getFOValidationEventProducer().missingLinkDestination(this, this.getName(), this.locator);
      } else {
         this.getFOValidationEventProducer().unimplementedFeature(this, this.getName(), "external-destination", this.getLocator());
      }

   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("bookmark-title")) {
            if (this.bookmarkTitle != null) {
               this.tooManyNodesError(loc, "fo:bookmark-title");
            }
         } else if (localName.equals("bookmark")) {
            if (this.bookmarkTitle == null) {
               this.nodesOutOfOrderError(loc, "fo:bookmark-title", "fo:bookmark");
            }
         } else {
            this.invalidChildError(loc, nsURI, localName);
         }
      }

   }

   protected void endOfNode() throws FOPException {
      if (this.bookmarkTitle == null) {
         this.missingChildElementError("(bookmark-title, bookmark*)");
      }

   }

   protected void addChildNode(FONode obj) {
      if (obj instanceof BookmarkTitle) {
         this.bookmarkTitle = (BookmarkTitle)obj;
      } else if (obj instanceof Bookmark) {
         this.childBookmarks.add(obj);
      }

   }

   public String getBookmarkTitle() {
      return this.bookmarkTitle == null ? "" : this.bookmarkTitle.getTitle();
   }

   public String getInternalDestination() {
      return this.internalDestination;
   }

   public String getExternalDestination() {
      return this.externalDestination;
   }

   public boolean showChildItems() {
      return this.bShow;
   }

   public ArrayList getChildBookmarks() {
      return this.childBookmarks;
   }

   public String getLocalName() {
      return "bookmark";
   }

   public int getNameId() {
      return 6;
   }
}
