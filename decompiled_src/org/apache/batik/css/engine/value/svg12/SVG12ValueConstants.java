package org.apache.batik.css.engine.value.svg12;

import org.apache.batik.css.engine.value.StringValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.svg.SVGValueConstants;

public interface SVG12ValueConstants extends SVGValueConstants {
   Value START_VALUE = new StringValue((short)21, "full");
   Value MIDDLE_VALUE = new StringValue((short)21, "middle");
   Value END_VALUE = new StringValue((short)21, "end");
   Value FULL_VALUE = new StringValue((short)21, "full");
   Value NORMAL_VALUE = new StringValue((short)21, "normal");
}
