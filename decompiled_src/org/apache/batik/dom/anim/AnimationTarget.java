package org.apache.batik.dom.anim;

import org.apache.batik.anim.values.AnimatableValue;
import org.w3c.dom.Element;

public interface AnimationTarget {
   short PERCENTAGE_FONT_SIZE = 0;
   short PERCENTAGE_VIEWPORT_WIDTH = 1;
   short PERCENTAGE_VIEWPORT_HEIGHT = 2;
   short PERCENTAGE_VIEWPORT_SIZE = 3;

   Element getElement();

   void updatePropertyValue(String var1, AnimatableValue var2);

   void updateAttributeValue(String var1, String var2, AnimatableValue var3);

   void updateOtherValue(String var1, AnimatableValue var2);

   AnimatableValue getUnderlyingValue(String var1, String var2);

   short getPercentageInterpretation(String var1, String var2, boolean var3);

   boolean useLinearRGBColorInterpolation();

   float svgToUserSpace(float var1, short var2, short var3);

   void addTargetListener(String var1, String var2, boolean var3, AnimationTargetListener var4);

   void removeTargetListener(String var1, String var2, boolean var3, AnimationTargetListener var4);
}
