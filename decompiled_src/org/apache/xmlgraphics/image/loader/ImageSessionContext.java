package org.apache.xmlgraphics.image.loader;

import java.io.FileNotFoundException;
import javax.xml.transform.Source;

public interface ImageSessionContext {
   ImageContext getParentContext();

   float getTargetResolution();

   Source newSource(String var1);

   Source getSource(String var1);

   Source needSource(String var1) throws FileNotFoundException;

   void returnSource(String var1, Source var2);
}
