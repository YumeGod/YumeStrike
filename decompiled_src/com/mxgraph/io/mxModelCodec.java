package com.mxgraph.io;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class mxModelCodec extends mxObjectCodec {
   public mxModelCodec() {
      this(new mxGraphModel());
   }

   public mxModelCodec(Object var1) {
      this(var1, (String[])null, (String[])null, (Map)null);
   }

   public mxModelCodec(Object var1, String[] var2, String[] var3, Map var4) {
      super(var1, var2, var3, var4);
   }

   public Node encode(mxCodec var1, Object var2) {
      Element var3 = null;
      if (var2 instanceof mxGraphModel) {
         mxGraphModel var4 = (mxGraphModel)var2;
         var3 = var1.document.createElement(this.getName());
         Element var5 = var1.document.createElement("root");
         var1.encodeCell((mxICell)var4.getRoot(), var5, true);
         var3.appendChild(var5);
      }

      return var3;
   }

   public Node beforeDecode(mxCodec var1, Node var2, Object var3) {
      if (var2 instanceof Element) {
         Element var4 = (Element)var2;
         mxGraphModel var5 = null;
         if (var3 instanceof mxGraphModel) {
            var5 = (mxGraphModel)var3;
         } else {
            var5 = new mxGraphModel();
         }

         Node var6 = var4.getElementsByTagName("root").item(0);
         mxICell var7 = null;
         if (var6 != null) {
            for(Node var8 = var6.getFirstChild(); var8 != null; var8 = var8.getNextSibling()) {
               mxICell var9 = var1.decodeCell(var8, true);
               if (var9 != null && var9.getParent() == null) {
                  var7 = var9;
               }
            }

            var6.getParentNode().removeChild(var6);
         }

         if (var7 != null) {
            var5.setRoot(var7);
         }
      }

      return var2;
   }
}
