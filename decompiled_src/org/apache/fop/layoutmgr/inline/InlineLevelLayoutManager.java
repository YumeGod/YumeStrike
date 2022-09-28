package org.apache.fop.layoutmgr.inline;

import java.util.List;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.Position;

public interface InlineLevelLayoutManager extends LayoutManager {
   List addALetterSpaceTo(List var1);

   void removeWordSpace(List var1);

   String getWordChars(Position var1);

   void hyphenate(Position var1, HyphContext var2);

   boolean applyChanges(List var1);
}
