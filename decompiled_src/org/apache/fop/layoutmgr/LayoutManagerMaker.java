package org.apache.fop.layoutmgr;

import java.util.List;
import org.apache.fop.area.AreaTreeHandler;
import org.apache.fop.area.Block;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.extensions.ExternalDocument;
import org.apache.fop.fo.pagination.Flow;
import org.apache.fop.fo.pagination.PageSequence;
import org.apache.fop.fo.pagination.SideRegion;
import org.apache.fop.fo.pagination.StaticContent;
import org.apache.fop.fo.pagination.Title;
import org.apache.fop.layoutmgr.inline.ContentLayoutManager;

public interface LayoutManagerMaker {
   void makeLayoutManagers(FONode var1, List var2);

   LayoutManager makeLayoutManager(FONode var1);

   PageSequenceLayoutManager makePageSequenceLayoutManager(AreaTreeHandler var1, PageSequence var2);

   ExternalDocumentLayoutManager makeExternalDocumentLayoutManager(AreaTreeHandler var1, ExternalDocument var2);

   FlowLayoutManager makeFlowLayoutManager(PageSequenceLayoutManager var1, Flow var2);

   ContentLayoutManager makeContentLayoutManager(PageSequenceLayoutManager var1, Title var2);

   StaticContentLayoutManager makeStaticContentLayoutManager(PageSequenceLayoutManager var1, StaticContent var2, SideRegion var3);

   StaticContentLayoutManager makeStaticContentLayoutManager(PageSequenceLayoutManager var1, StaticContent var2, Block var3);
}
