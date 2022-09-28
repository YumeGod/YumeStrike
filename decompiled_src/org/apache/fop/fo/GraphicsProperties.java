package org.apache.fop.fo;

import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.properties.LengthRangeProperty;

public interface GraphicsProperties {
   LengthRangeProperty getInlineProgressionDimension();

   LengthRangeProperty getBlockProgressionDimension();

   Length getHeight();

   Length getWidth();

   Length getContentHeight();

   Length getContentWidth();

   int getScaling();

   int getOverflow();

   int getDisplayAlign();

   int getTextAlign();
}
