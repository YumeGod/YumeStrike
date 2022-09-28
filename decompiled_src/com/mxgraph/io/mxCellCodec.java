package com.mxgraph.io;

import com.mxgraph.model.mxCell;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class mxCellCodec extends mxObjectCodec {
   public mxCellCodec() {
      this(new mxCell(), (String[])null, new String[]{"parent", "source", "target"}, (Map)null);
   }

   public mxCellCodec(Object var1) {
      this(var1, (String[])null, (String[])null, (Map)null);
   }

   public mxCellCodec(Object var1, String[] var2, String[] var3, Map var4) {
      super(var1, var2, var3, var4);
   }

   public boolean isExcluded(Object var1, String var2, Object var3, boolean var4) {
      return this.exclude.contains(var2) || var4 && var2.equals("value") && var3 instanceof Node && ((Node)var3).getNodeType() == 1;
   }

   public Node afterEncode(mxCodec var1, Object var2, Node var3) {
      if (var2 instanceof mxCell) {
         mxCell var4 = (mxCell)var2;
         if (var4.getValue() instanceof Node) {
            Element var5 = (Element)var3;
            var3 = var1.getDocument().importNode((Node)var4.getValue(), true);
            var3.appendChild(var5);
            String var6 = var5.getAttribute("id");
            ((Element)var3).setAttribute("id", var6);
            var5.removeAttribute("id");
         }
      }

      return var3;
   }

   public Node beforeDecode(mxCodec var1, Node var2, Object var3) {
      Element var4 = (Element)var2;
      if (var3 instanceof mxCell) {
         mxCell var5 = (mxCell)var3;
         String var6 = this.getName();
         String var15;
         if (var2.getNodeName().equals(var6)) {
            var5.setId(((Element)var2).getAttribute("id"));
         } else {
            Node var7 = var4.getElementsByTagName(var6).item(0);
            if (var7 != null && var7.getParentNode() == var2) {
               var4 = (Element)var7;

               Node var8;
               Node var9;
               for(var8 = var7.getPreviousSibling(); var8 != null && var8.getNodeType() == 3; var8 = var9) {
                  var9 = var8.getPreviousSibling();
                  if (var8.getTextContent().trim().length() == 0) {
                     var8.getParentNode().removeChild(var8);
                  }
               }

               for(var8 = var7.getNextSibling(); var8 != null && var8.getNodeType() == 3; var8 = var9) {
                  var9 = var8.getPreviousSibling();
                  if (var8.getTextContent().trim().length() == 0) {
                     var8.getParentNode().removeChild(var8);
                  }
               }

               var7.getParentNode().removeChild(var7);
            } else {
               var4 = null;
            }

            Element var14 = (Element)var2.cloneNode(true);
            var5.setValue(var14);
            var15 = var14.getAttribute("id");
            if (var15 != null) {
               var5.setId(var15);
               var14.removeAttribute("id");
            }
         }

         if (var4 != null && this.idrefs != null) {
            Iterator var13 = this.idrefs.iterator();

            while(var13.hasNext()) {
               String var16 = (String)var13.next();
               var15 = var4.getAttribute(var16);
               if (var15 != null && var15.length() > 0) {
                  var4.removeAttribute(var16);
                  Object var10 = var1.objects.get(var15);
                  if (var10 == null) {
                     var10 = var1.lookup(var15);
                  }

                  if (var10 == null) {
                     Node var11 = var1.getElementById(var15);
                     if (var11 != null) {
                        Object var12 = mxCodecRegistry.getCodec(var11.getNodeName());
                        if (var12 == null) {
                           var12 = this;
                        }

                        var10 = ((mxObjectCodec)var12).decode(var1, var11);
                     }
                  }

                  this.setFieldValue(var3, var16, var10);
               }
            }
         }
      }

      return var4;
   }
}
