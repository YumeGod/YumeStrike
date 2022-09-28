package org.apache.batik.gvt.event;

import java.util.EventListener;

public interface SelectionListener extends EventListener {
   void selectionChanged(SelectionEvent var1);

   void selectionDone(SelectionEvent var1);

   void selectionCleared(SelectionEvent var1);

   void selectionStarted(SelectionEvent var1);
}
