package com.xmlmind.fo.graphic;

import java.io.File;

public interface GraphicEnv {
   File createTempFile(String var1) throws Exception;

   void reportWarning(String var1);
}
