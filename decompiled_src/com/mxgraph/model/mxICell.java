package com.mxgraph.model;

public interface mxICell {
   String getId();

   void setId(String var1);

   Object getValue();

   void setValue(Object var1);

   mxGeometry getGeometry();

   void setGeometry(mxGeometry var1);

   String getStyle();

   void setStyle(String var1);

   boolean isVertex();

   boolean isEdge();

   boolean isConnectable();

   boolean isVisible();

   void setVisible(boolean var1);

   boolean isCollapsed();

   void setCollapsed(boolean var1);

   mxICell getParent();

   void setParent(mxICell var1);

   mxICell getTerminal(boolean var1);

   mxICell setTerminal(mxICell var1, boolean var2);

   int getChildCount();

   int getIndex(mxICell var1);

   mxICell getChildAt(int var1);

   mxICell insert(mxICell var1);

   mxICell insert(mxICell var1, int var2);

   mxICell remove(int var1);

   mxICell remove(mxICell var1);

   void removeFromParent();

   int getEdgeCount();

   int getEdgeIndex(mxICell var1);

   mxICell getEdgeAt(int var1);

   mxICell insertEdge(mxICell var1, boolean var2);

   mxICell removeEdge(mxICell var1, boolean var2);

   void removeFromTerminal(boolean var1);

   Object clone() throws CloneNotSupportedException;
}
