package org.apache.fop.render.intermediate;

import org.apache.fop.render.intermediate.extensions.AbstractAction;
import org.apache.fop.render.intermediate.extensions.BookmarkTree;
import org.apache.fop.render.intermediate.extensions.Link;
import org.apache.fop.render.intermediate.extensions.NamedDestination;

public interface IFDocumentNavigationHandler {
   void renderNamedDestination(NamedDestination var1) throws IFException;

   void renderBookmarkTree(BookmarkTree var1) throws IFException;

   void renderLink(Link var1) throws IFException;

   void addResolvedAction(AbstractAction var1) throws IFException;
}
