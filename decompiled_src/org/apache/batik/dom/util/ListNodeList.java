package org.apache.batik.dom.util;

import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ListNodeList implements NodeList {
   protected List list;

   public ListNodeList(List var1) {
      this.list = var1;
   }

   public Node item(int var1) {
      return var1 >= 0 && var1 <= this.list.size() ? (Node)this.list.get(var1) : null;
   }

   public int getLength() {
      return this.list.size();
   }
}
