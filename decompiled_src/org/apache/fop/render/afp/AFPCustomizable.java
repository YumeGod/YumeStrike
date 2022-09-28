package org.apache.fop.render.afp;

import org.apache.fop.afp.AFPResourceLevelDefaults;

public interface AFPCustomizable {
   void setBitsPerPixel(int var1);

   void setColorImages(boolean var1);

   void setNativeImagesSupported(boolean var1);

   void setCMYKImagesSupported(boolean var1);

   void setShadingMode(AFPShadingMode var1);

   void setDitheringQuality(float var1);

   void setResolution(int var1);

   int getResolution();

   void setDefaultResourceGroupFilePath(String var1);

   void setResourceLevelDefaults(AFPResourceLevelDefaults var1);
}
