package com.mxgraph.io;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxCellPath;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxUtils;
import java.util.Hashtable;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class mxCodec {
   protected Document document;
   protected Map objects;
   protected boolean encodeDefaults;

   public mxCodec() {
      this(mxUtils.createDocument());
   }

   public mxCodec(Document var1) {
      this.objects = new Hashtable();
      this.encodeDefaults = false;
      if (var1 == null) {
         var1 = mxUtils.createDocument();
      }

      this.document = var1;
   }

   public boolean isEncodeDefaults() {
      return this.encodeDefaults;
   }

   public void setEncodeDefaults(boolean var1) {
      this.encodeDefaults = var1;
   }

   public Map getObjects() {
      return this.objects;
   }

   public Document getDocument() {
      return this.document;
   }

   public Object putObject(String var1, Object var2) {
      return this.objects.put(var1, var2);
   }

   public Object getObject(String var1) {
      Object var2 = null;
      if (var1 != null) {
         var2 = this.objects.get(var1);
         if (var2 == null) {
            var2 = this.lookup(var1);
            if (var2 == null) {
               Node var3 = this.getElementById(var1);
               if (var3 != null) {
                  var2 = this.decode(var3);
               }
            }
         }
      }

      return var2;
   }

   public Object lookup(String var1) {
      return null;
   }

   public Node getElementById(String var1) {
      return this.getElementById(var1, (String)null);
   }

   public Node getElementById(String var1, String var2) {
      if (var2 == null) {
         var2 = "id";
      }

      String var3 = "//*[@" + var2 + "='" + var1 + "']";
      return mxUtils.selectSingleNode(this.document, var3);
   }

   public String getId(Object var1) {
      String var2 = null;
      if (var1 != null) {
         var2 = this.reference(var1);
         if (var2 == null && var1 instanceof mxICell) {
            var2 = ((mxICell)var1).getId();
            if (var2 == null) {
               var2 = mxCellPath.create((mxICell)var1);
               if (var2.length() == 0) {
                  var2 = "root";
               }
            }
         }
      }

      return var2;
   }

   public String reference(Object var1) {
      return null;
   }

   public Node encode(Object var1) {
      Node var2 = null;
      if (var1 != null) {
         String var3 = mxCodecRegistry.getName(var1);
         mxObjectCodec var4 = mxCodecRegistry.getCodec(var3);
         if (var4 != null) {
            var2 = var4.encode(this, var1);
         } else if (var1 instanceof Node) {
            var2 = ((Node)var1).cloneNode(true);
         } else {
            System.err.println("No codec for " + var3);
         }
      }

      return var2;
   }

   public Object decode(Node var1) {
      return this.decode(var1, (Object)null);
   }

   public Object decode(Node var1, Object var2) {
      Object var3 = null;
      if (var1 != null && var1.getNodeType() == 1) {
         mxObjectCodec var4 = mxCodecRegistry.getCodec(var1.getNodeName());

         try {
            if (var4 != null) {
               var3 = var4.decode(this, var1, var2);
            } else {
               var3 = var1.cloneNode(true);
               ((Element)var3).removeAttribute("as");
            }
         } catch (Exception var6) {
            System.err.println("Cannot decode " + var1.getNodeName() + ": " + var6.getMessage());
            var6.printStackTrace();
         }
      }

      return var3;
   }

   public void encodeCell(mxICell var1, Node var2, boolean var3) {
      var2.appendChild(this.encode(var1));
      if (var3) {
         int var4 = var1.getChildCount();

         for(int var5 = 0; var5 < var4; ++var5) {
            this.encodeCell(var1.getChildAt(var5), var2, var3);
         }
      }

   }

   public mxICell decodeCell(Node var1, boolean var2) {
      mxICell var3 = null;
      if (var1 != null && var1.getNodeType() == 1) {
         mxObjectCodec var4 = mxCodecRegistry.getCodec(var1.getNodeName());
         if (!(var4 instanceof mxCellCodec)) {
            for(Node var5 = var1.getFirstChild(); var5 != null && !(var4 instanceof mxCellCodec); var5 = var5.getNextSibling()) {
               var4 = mxCodecRegistry.getCodec(var5.getNodeName());
            }

            String var6 = mxCell.class.getSimpleName();
            var4 = mxCodecRegistry.getCodec(var6);
         }

         if (!(var4 instanceof mxCellCodec)) {
            String var8 = mxCell.class.getSimpleName();
            var4 = mxCodecRegistry.getCodec(var8);
         }

         var3 = (mxICell)var4.decode(this, var1);
         if (var2) {
            mxICell var9 = var3.getParent();
            if (var9 != null) {
               var9.insert(var3);
            }

            mxICell var10 = var3.getTerminal(true);
            if (var10 != null) {
               var10.insertEdge(var3, true);
            }

            mxICell var7 = var3.getTerminal(false);
            if (var7 != null) {
               var7.insertEdge(var3, false);
            }
         }
      }

      return var3;
   }

   public static void setAttribute(Node var0, String var1, Object var2) {
      if (var0.getNodeType() == 1 && var1 != null && var2 != null) {
         ((Element)var0).setAttribute(var1, String.valueOf(var2));
      }

   }
}
