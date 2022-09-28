package org.apache.xmlgraphics.xmp;

import java.util.Iterator;
import org.apache.xmlgraphics.util.QName;

public interface PropertyAccess {
   void setProperty(XMPProperty var1);

   XMPProperty getProperty(String var1, String var2);

   XMPProperty getProperty(QName var1);

   XMPProperty removeProperty(QName var1);

   XMPProperty getValueProperty();

   int getPropertyCount();

   Iterator iterator();
}
