package org.apache.xalan.trace;

import javax.xml.transform.TransformerException;

public interface TraceListenerEx extends TraceListener {
   void selectEnd(EndSelectionEvent var1) throws TransformerException;
}
