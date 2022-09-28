package org.apache.xalan.trace;

import java.util.EventListener;
import javax.xml.transform.TransformerException;

public interface TraceListener extends EventListener {
   void trace(TracerEvent var1);

   void selected(SelectionEvent var1) throws TransformerException;

   void generated(GenerateEvent var1);
}
