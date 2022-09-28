package org.apache.batik.ext.awt.image.renderable;

import java.util.List;
import org.apache.batik.ext.awt.image.CompositeRule;

public interface CompositeRable extends FilterColorInterpolation {
   void setSources(List var1);

   void setCompositeRule(CompositeRule var1);

   CompositeRule getCompositeRule();
}
