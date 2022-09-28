package com.mxgraph.swing.view;

import java.util.EventObject;

public interface mxICellEditor {
   Object getEditingCell();

   void startEditing(Object var1, EventObject var2);

   void stopEditing(boolean var1);
}
