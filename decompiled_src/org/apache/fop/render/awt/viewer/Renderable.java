package org.apache.fop.render.awt.viewer;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;

public interface Renderable {
   void renderTo(FOUserAgent var1, String var2) throws FOPException;
}
