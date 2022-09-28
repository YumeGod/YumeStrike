package com.mxgraph.io;

import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxStylesheet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class mxStylesheetCodec extends mxObjectCodec {
   public mxStylesheetCodec() {
      this(new mxStylesheet());
   }

   public mxStylesheetCodec(Object var1) {
      this(var1, (String[])null, (String[])null, (Map)null);
   }

   public mxStylesheetCodec(Object var1, String[] var2, String[] var3, Map var4) {
      super(var1, var2, var3, var4);
   }

   public Node encode(mxCodec var1, Object var2) {
      Element var3 = var1.document.createElement(this.getName());
      if (var2 instanceof mxStylesheet) {
         mxStylesheet var4 = (mxStylesheet)var2;
         Iterator var5 = var4.getStyles().entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            Element var7 = var1.document.createElement("add");
            String var8 = (String)var6.getKey();
            var7.setAttribute("as", var8);
            Map var9 = (Map)var6.getValue();
            Iterator var10 = var9.entrySet().iterator();

            while(var10.hasNext()) {
               Map.Entry var11 = (Map.Entry)var10.next();
               Element var12 = var1.document.createElement("add");
               var12.setAttribute("as", String.valueOf(var11.getKey()));
               var12.setAttribute("value", String.valueOf(var11.getValue()));
               var7.appendChild(var12);
            }

            if (var7.getChildNodes().getLength() > 0) {
               var3.appendChild(var7);
            }
         }
      }

      return var3;
   }

   public Object decode(mxCodec var1, Node var2, Object var3) {
      Object var4 = null;
      if (var2 instanceof Element) {
         String var5 = ((Element)var2).getAttribute("id");
         var4 = var1.objects.get(var5);
         if (var4 == null) {
            var4 = var3;
            if (var3 == null) {
               var4 = this.cloneTemplate(var2);
            }

            if (var5 != null && var5.length() > 0) {
               var1.putObject(var5, var4);
            }
         }

         for(var2 = var2.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            if (!this.processInclude(var1, var2, var4) && var2.getNodeName().equals("add") && var2 instanceof Element) {
               String var6 = ((Element)var2).getAttribute("as");
               if (var6 != null && var6.length() > 0) {
                  String var7 = ((Element)var2).getAttribute("extend");
                  Map var8 = var7 != null ? (Map)((mxStylesheet)var4).getStyles().get(var7) : null;
                  Hashtable var14;
                  if (var8 == null) {
                     var14 = new Hashtable();
                  } else {
                     var14 = new Hashtable(var8);
                  }

                  for(Node var9 = var2.getFirstChild(); var9 != null; var9 = var9.getNextSibling()) {
                     if (var9 instanceof Element) {
                        Element var10 = (Element)var9;
                        String var11 = var10.getAttribute("as");
                        if (!var9.getNodeName().equals("add")) {
                           if (var9.getNodeName().equals("remove")) {
                              var14.remove(var11);
                           }
                        } else {
                           String var12 = var9.getTextContent();
                           Object var13 = null;
                           if (var12 != null && var12.length() > 0) {
                              var13 = mxUtils.eval(var12);
                           } else {
                              var13 = var10.getAttribute("value");
                           }

                           if (var13 != null) {
                              var14.put(var11, var13);
                           }
                        }
                     }
                  }

                  ((mxStylesheet)var4).putCellStyle(var6, var14);
               }
            }
         }
      }

      return var4;
   }
}
