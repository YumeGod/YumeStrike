package org.apache.fop.layoutmgr;

import java.util.List;
import java.util.Stack;
import org.apache.fop.area.Area;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FObj;

public interface LayoutManager extends PercentBaseContext {
   void setParent(LayoutManager var1);

   LayoutManager getParent();

   void initialize();

   PageSequenceLayoutManager getPSLM();

   boolean isFinished();

   void setFinished(boolean var1);

   Area getParentArea(Area var1);

   void addChildArea(Area var1);

   void addAreas(PositionIterator var1, LayoutContext var2);

   boolean createNextChildLMs(int var1);

   List getChildLMs();

   void addChildLM(LayoutManager var1);

   void addChildLMs(List var1);

   List getNextKnuthElements(LayoutContext var1, int var2);

   List getChangedKnuthElements(List var1, int var2);

   int getContentAreaIPD();

   int getContentAreaBPD();

   boolean getGeneratesReferenceArea();

   boolean getGeneratesBlockArea();

   boolean getGeneratesLineArea();

   FObj getFObj();

   Position notifyPos(Position var1);

   void reset();

   boolean isRestartable();

   List getNextKnuthElements(LayoutContext var1, int var2, Stack var3, Position var4, LayoutManager var5);
}
