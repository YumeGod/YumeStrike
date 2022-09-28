package com.mxgraph.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class mxCell implements mxICell, Cloneable, Serializable {
   private static final long serialVersionUID = 910211337632342672L;
   protected String id;
   protected Object value;
   protected mxGeometry geometry;
   protected String style;
   protected boolean vertex;
   protected boolean edge;
   protected boolean connectable;
   protected boolean visible;
   protected boolean collapsed;
   protected mxICell parent;
   protected mxICell source;
   protected mxICell target;
   protected List children;
   protected List edges;

   public mxCell() {
      this((Object)null);
   }

   public mxCell(Object var1) {
      this(var1, (mxGeometry)null, (String)null);
   }

   public mxCell(Object var1, mxGeometry var2, String var3) {
      this.vertex = false;
      this.edge = false;
      this.connectable = true;
      this.visible = true;
      this.collapsed = false;
      this.setValue(var1);
      this.setGeometry(var2);
      this.setStyle(var3);
   }

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object var1) {
      this.value = var1;
   }

   public mxGeometry getGeometry() {
      return this.geometry;
   }

   public void setGeometry(mxGeometry var1) {
      this.geometry = var1;
   }

   public String getStyle() {
      return this.style;
   }

   public void setStyle(String var1) {
      this.style = var1;
   }

   public boolean isVertex() {
      return this.vertex;
   }

   public void setVertex(boolean var1) {
      this.vertex = var1;
   }

   public boolean isEdge() {
      return this.edge;
   }

   public void setEdge(boolean var1) {
      this.edge = var1;
   }

   public boolean isConnectable() {
      return this.connectable;
   }

   public void setConnectable(boolean var1) {
      this.connectable = var1;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public void setVisible(boolean var1) {
      this.visible = var1;
   }

   public boolean isCollapsed() {
      return this.collapsed;
   }

   public void setCollapsed(boolean var1) {
      this.collapsed = var1;
   }

   public mxICell getParent() {
      return this.parent;
   }

   public void setParent(mxICell var1) {
      this.parent = var1;
   }

   public mxICell getSource() {
      return this.source;
   }

   public void setSource(mxICell var1) {
      this.source = var1;
   }

   public mxICell getTarget() {
      return this.target;
   }

   public void setTarget(mxICell var1) {
      this.target = var1;
   }

   public mxICell getTerminal(boolean var1) {
      return var1 ? this.getSource() : this.getTarget();
   }

   public mxICell setTerminal(mxICell var1, boolean var2) {
      if (var2) {
         this.setSource(var1);
      } else {
         this.setTarget(var1);
      }

      return var1;
   }

   public int getChildCount() {
      return this.children != null ? this.children.size() : 0;
   }

   public int getIndex(mxICell var1) {
      return this.children != null ? this.children.indexOf(var1) : -1;
   }

   public mxICell getChildAt(int var1) {
      return this.children != null ? (mxICell)this.children.get(var1) : null;
   }

   public mxICell insert(mxICell var1) {
      int var2 = this.getChildCount();
      if (var1.getParent() == this && this.getIndex(var1) >= 0) {
         --var2;
      }

      return this.insert(var1, var2);
   }

   public mxICell insert(mxICell var1, int var2) {
      if (var1 != null) {
         var1.removeFromParent();
         var1.setParent(this);
         if (this.children == null) {
            this.children = new ArrayList();
            this.children.add(var1);
         } else {
            this.children.add(var2, var1);
         }
      }

      return var1;
   }

   public mxICell remove(int var1) {
      mxICell var2 = null;
      if (this.children != null && var1 >= 0) {
         var2 = this.getChildAt(var1);
         this.remove(var2);
      }

      return var2;
   }

   public mxICell remove(mxICell var1) {
      if (var1 != null && this.children != null) {
         this.children.remove(var1);
         var1.setParent((mxICell)null);
      }

      return var1;
   }

   public void removeFromParent() {
      if (this.parent != null) {
         this.parent.remove(this);
      }

   }

   public int getEdgeCount() {
      return this.edges != null ? this.edges.size() : 0;
   }

   public int getEdgeIndex(mxICell var1) {
      return this.edges != null ? this.edges.indexOf(var1) : -1;
   }

   public mxICell getEdgeAt(int var1) {
      return this.edges != null ? (mxICell)this.edges.get(var1) : null;
   }

   public mxICell insertEdge(mxICell var1, boolean var2) {
      if (var1 != null) {
         var1.removeFromTerminal(var2);
         var1.setTerminal(this, var2);
         if (this.edges == null || var1.getTerminal(!var2) != this || !this.edges.contains(var1)) {
            if (this.edges == null) {
               this.edges = new ArrayList();
            }

            this.edges.add(var1);
         }
      }

      return var1;
   }

   public mxICell removeEdge(mxICell var1, boolean var2) {
      if (var1 != null) {
         if (var1.getTerminal(!var2) != this && this.edges != null) {
            this.edges.remove(var1);
         }

         var1.setTerminal((mxICell)null, var2);
      }

      return var1;
   }

   public void removeFromTerminal(boolean var1) {
      mxICell var2 = this.getTerminal(var1);
      if (var2 != null) {
         var2.removeEdge(this, var1);
      }

   }

   public String getAttribute(String var1) {
      return this.getAttribute(var1, (String)null);
   }

   public String getAttribute(String var1, String var2) {
      Object var3 = this.getValue();
      String var4 = null;
      if (var3 instanceof Element) {
         Element var5 = (Element)var3;
         var4 = var5.getAttribute(var1);
      }

      if (var4 == null) {
         var4 = var2;
      }

      return var4;
   }

   public void setAttribute(String var1, String var2) {
      Object var3 = this.getValue();
      if (var3 instanceof Element) {
         Element var4 = (Element)var3;
         var4.setAttribute(var1, var2);
      }

   }

   public Object clone() throws CloneNotSupportedException {
      mxCell var1 = (mxCell)super.clone();
      var1.setValue(this.cloneValue());
      var1.setStyle(this.getStyle());
      var1.setCollapsed(this.isCollapsed());
      var1.setConnectable(this.isConnectable());
      var1.setEdge(this.isEdge());
      var1.setVertex(this.isVertex());
      var1.setVisible(this.isVisible());
      var1.setParent((mxICell)null);
      var1.setSource((mxICell)null);
      var1.setTarget((mxICell)null);
      var1.children = null;
      var1.edges = null;
      mxGeometry var2 = this.getGeometry();
      if (var2 != null) {
         var1.setGeometry((mxGeometry)var2.clone());
      }

      return var1;
   }

   protected Object cloneValue() {
      Object var1 = this.getValue();
      if (var1 instanceof Node) {
         var1 = ((Node)var1).cloneNode(true);
      }

      return var1;
   }
}
