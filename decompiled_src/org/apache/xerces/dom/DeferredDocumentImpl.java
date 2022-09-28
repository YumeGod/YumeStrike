package org.apache.xerces.dom;

import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DeferredDocumentImpl extends DocumentImpl implements DeferredNode {
   static final long serialVersionUID = 5186323580749626857L;
   private static final boolean DEBUG_PRINT_REF_COUNTS = false;
   private static final boolean DEBUG_PRINT_TABLES = false;
   private static final boolean DEBUG_IDS = false;
   protected static final int CHUNK_SHIFT = 11;
   protected static final int CHUNK_SIZE = 2048;
   protected static final int CHUNK_MASK = 2047;
   protected static final int INITIAL_CHUNK_COUNT = 32;
   protected transient int fNodeCount;
   protected transient int[][] fNodeType;
   protected transient Object[][] fNodeName;
   protected transient Object[][] fNodeValue;
   protected transient int[][] fNodeParent;
   protected transient int[][] fNodeLastChild;
   protected transient int[][] fNodePrevSib;
   protected transient Object[][] fNodeURI;
   protected transient int[][] fNodeExtra;
   protected transient int fIdCount;
   protected transient String[] fIdName;
   protected transient int[] fIdElement;
   protected boolean fNamespacesEnabled;
   private final transient StringBuffer fBufferStr;
   private final transient Vector fStrChunks;
   private static final int[] INIT_ARRAY = new int[2049];

   public DeferredDocumentImpl() {
      this(false);
   }

   public DeferredDocumentImpl(boolean var1) {
      this(var1, false);
   }

   public DeferredDocumentImpl(boolean var1, boolean var2) {
      super(var2);
      this.fNodeCount = 0;
      this.fNamespacesEnabled = false;
      this.fBufferStr = new StringBuffer();
      this.fStrChunks = new Vector();
      this.needsSyncData(true);
      this.needsSyncChildren(true);
      this.fNamespacesEnabled = var1;
   }

   public DOMImplementation getImplementation() {
      return DeferredDOMImplementationImpl.getDOMImplementation();
   }

   boolean getNamespacesEnabled() {
      return this.fNamespacesEnabled;
   }

   void setNamespacesEnabled(boolean var1) {
      this.fNamespacesEnabled = var1;
   }

   public int createDeferredDocument() {
      int var1 = this.createNode((short)9);
      return var1;
   }

   public int createDeferredDocumentType(String var1, String var2, String var3) {
      int var4 = this.createNode((short)10);
      int var5 = var4 >> 11;
      int var6 = var4 & 2047;
      this.setChunkValue(this.fNodeName, var1, var5, var6);
      this.setChunkValue(this.fNodeValue, var2, var5, var6);
      this.setChunkValue(this.fNodeURI, var3, var5, var6);
      return var4;
   }

   public void setInternalSubset(int var1, String var2) {
      int var3 = var1 >> 11;
      int var4 = var1 & 2047;
      int var5 = this.createNode((short)10);
      int var6 = var5 >> 11;
      int var7 = var5 & 2047;
      this.setChunkIndex(this.fNodeExtra, var5, var3, var4);
      this.setChunkValue(this.fNodeValue, var2, var6, var7);
   }

   public int createDeferredNotation(String var1, String var2, String var3, String var4) {
      int var5 = this.createNode((short)12);
      int var6 = var5 >> 11;
      int var7 = var5 & 2047;
      int var8 = this.createNode((short)12);
      int var9 = var8 >> 11;
      int var10 = var8 & 2047;
      this.setChunkValue(this.fNodeName, var1, var6, var7);
      this.setChunkValue(this.fNodeValue, var2, var6, var7);
      this.setChunkValue(this.fNodeURI, var3, var6, var7);
      this.setChunkIndex(this.fNodeExtra, var8, var6, var7);
      this.setChunkValue(this.fNodeName, var4, var9, var10);
      return var5;
   }

   public int createDeferredEntity(String var1, String var2, String var3, String var4, String var5) {
      int var6 = this.createNode((short)6);
      int var7 = var6 >> 11;
      int var8 = var6 & 2047;
      int var9 = this.createNode((short)6);
      int var10 = var9 >> 11;
      int var11 = var9 & 2047;
      this.setChunkValue(this.fNodeName, var1, var7, var8);
      this.setChunkValue(this.fNodeValue, var2, var7, var8);
      this.setChunkValue(this.fNodeURI, var3, var7, var8);
      this.setChunkIndex(this.fNodeExtra, var9, var7, var8);
      this.setChunkValue(this.fNodeName, var4, var10, var11);
      this.setChunkValue(this.fNodeValue, (Object)null, var10, var11);
      this.setChunkValue(this.fNodeURI, (Object)null, var10, var11);
      int var12 = this.createNode((short)6);
      int var13 = var12 >> 11;
      int var14 = var12 & 2047;
      this.setChunkIndex(this.fNodeExtra, var12, var10, var11);
      this.setChunkValue(this.fNodeName, var5, var13, var14);
      return var6;
   }

   public String getDeferredEntityBaseURI(int var1) {
      if (var1 != -1) {
         int var2 = this.getNodeExtra(var1, false);
         var2 = this.getNodeExtra(var2, false);
         return this.getNodeName(var2, false);
      } else {
         return null;
      }
   }

   public void setEntityInfo(int var1, String var2, String var3) {
      int var4 = this.getNodeExtra(var1, false);
      if (var4 != -1) {
         int var5 = var4 >> 11;
         int var6 = var4 & 2047;
         this.setChunkValue(this.fNodeValue, var2, var5, var6);
         this.setChunkValue(this.fNodeURI, var3, var5, var6);
      }

   }

   public void setInputEncoding(int var1, String var2) {
      int var3 = this.getNodeExtra(var1, false);
      int var4 = this.getNodeExtra(var3, false);
      int var5 = var4 >> 11;
      int var6 = var4 & 2047;
      this.setChunkValue(this.fNodeValue, var2, var5, var6);
   }

   public int createDeferredEntityReference(String var1, String var2) {
      int var3 = this.createNode((short)5);
      int var4 = var3 >> 11;
      int var5 = var3 & 2047;
      this.setChunkValue(this.fNodeName, var1, var4, var5);
      this.setChunkValue(this.fNodeValue, var2, var4, var5);
      return var3;
   }

   public int createDeferredElement(String var1, String var2, Object var3) {
      int var4 = this.createNode((short)1);
      int var5 = var4 >> 11;
      int var6 = var4 & 2047;
      this.setChunkValue(this.fNodeName, var2, var5, var6);
      this.setChunkValue(this.fNodeURI, var1, var5, var6);
      this.setChunkValue(this.fNodeValue, var3, var5, var6);
      return var4;
   }

   /** @deprecated */
   public int createDeferredElement(String var1) {
      return this.createDeferredElement((String)null, var1);
   }

   /** @deprecated */
   public int createDeferredElement(String var1, String var2) {
      int var3 = this.createNode((short)1);
      int var4 = var3 >> 11;
      int var5 = var3 & 2047;
      this.setChunkValue(this.fNodeName, var2, var4, var5);
      this.setChunkValue(this.fNodeURI, var1, var4, var5);
      return var3;
   }

   public int setDeferredAttribute(int var1, String var2, String var3, String var4, boolean var5, boolean var6, Object var7) {
      int var8 = this.createDeferredAttribute(var2, var3, var4, var5);
      int var9 = var8 >> 11;
      int var10 = var8 & 2047;
      this.setChunkIndex(this.fNodeParent, var1, var9, var10);
      int var11 = var1 >> 11;
      int var12 = var1 & 2047;
      int var13 = this.getChunkIndex(this.fNodeExtra, var11, var12);
      int var14;
      int var15;
      if (var13 != 0) {
         var14 = var13 >> 11;
         var15 = var13 & 2047;
         this.setChunkIndex(this.fNodePrevSib, var13, var9, var10);
      }

      this.setChunkIndex(this.fNodeExtra, var8, var11, var12);
      var14 = this.getChunkIndex(this.fNodeExtra, var9, var10);
      if (var6) {
         var14 |= 512;
         this.setChunkIndex(this.fNodeExtra, var14, var9, var10);
         String var18 = this.getChunkValue(this.fNodeValue, var9, var10);
         this.putIdentifier(var18, var1);
      }

      if (var7 != null) {
         var15 = this.createNode((short)20);
         int var16 = var15 >> 11;
         int var17 = var15 & 2047;
         this.setChunkIndex(this.fNodeLastChild, var15, var9, var10);
         this.setChunkValue(this.fNodeValue, var7, var16, var17);
      }

      return var8;
   }

   /** @deprecated */
   public int setDeferredAttribute(int var1, String var2, String var3, String var4, boolean var5) {
      int var6 = this.createDeferredAttribute(var2, var3, var4, var5);
      int var7 = var6 >> 11;
      int var8 = var6 & 2047;
      this.setChunkIndex(this.fNodeParent, var1, var7, var8);
      int var9 = var1 >> 11;
      int var10 = var1 & 2047;
      int var11 = this.getChunkIndex(this.fNodeExtra, var9, var10);
      if (var11 != 0) {
         this.setChunkIndex(this.fNodePrevSib, var11, var7, var8);
      }

      this.setChunkIndex(this.fNodeExtra, var6, var9, var10);
      return var6;
   }

   public int createDeferredAttribute(String var1, String var2, boolean var3) {
      return this.createDeferredAttribute(var1, (String)null, var2, var3);
   }

   public int createDeferredAttribute(String var1, String var2, String var3, boolean var4) {
      int var5 = this.createNode((short)2);
      int var6 = var5 >> 11;
      int var7 = var5 & 2047;
      this.setChunkValue(this.fNodeName, var1, var6, var7);
      this.setChunkValue(this.fNodeURI, var2, var6, var7);
      this.setChunkValue(this.fNodeValue, var3, var6, var7);
      int var8 = var4 ? 32 : 0;
      this.setChunkIndex(this.fNodeExtra, var8, var6, var7);
      return var5;
   }

   public int createDeferredElementDefinition(String var1) {
      int var2 = this.createNode((short)21);
      int var3 = var2 >> 11;
      int var4 = var2 & 2047;
      this.setChunkValue(this.fNodeName, var1, var3, var4);
      return var2;
   }

   public int createDeferredTextNode(String var1, boolean var2) {
      int var3 = this.createNode((short)3);
      int var4 = var3 >> 11;
      int var5 = var3 & 2047;
      this.setChunkValue(this.fNodeValue, var1, var4, var5);
      this.setChunkIndex(this.fNodeExtra, var2 ? 1 : 0, var4, var5);
      return var3;
   }

   public int createDeferredCDATASection(String var1) {
      int var2 = this.createNode((short)4);
      int var3 = var2 >> 11;
      int var4 = var2 & 2047;
      this.setChunkValue(this.fNodeValue, var1, var3, var4);
      return var2;
   }

   public int createDeferredProcessingInstruction(String var1, String var2) {
      int var3 = this.createNode((short)7);
      int var4 = var3 >> 11;
      int var5 = var3 & 2047;
      this.setChunkValue(this.fNodeName, var1, var4, var5);
      this.setChunkValue(this.fNodeValue, var2, var4, var5);
      return var3;
   }

   public int createDeferredComment(String var1) {
      int var2 = this.createNode((short)8);
      int var3 = var2 >> 11;
      int var4 = var2 & 2047;
      this.setChunkValue(this.fNodeValue, var1, var3, var4);
      return var2;
   }

   public int cloneNode(int var1, boolean var2) {
      int var3 = var1 >> 11;
      int var4 = var1 & 2047;
      int var5 = this.fNodeType[var3][var4];
      int var6 = this.createNode((short)var5);
      int var7 = var6 >> 11;
      int var8 = var6 & 2047;
      this.setChunkValue(this.fNodeName, this.fNodeName[var3][var4], var7, var8);
      this.setChunkValue(this.fNodeValue, this.fNodeValue[var3][var4], var7, var8);
      this.setChunkValue(this.fNodeURI, this.fNodeURI[var3][var4], var7, var8);
      int var9 = this.fNodeExtra[var3][var4];
      if (var9 != -1) {
         if (var5 != 2 && var5 != 3) {
            var9 = this.cloneNode(var9, false);
         }

         this.setChunkIndex(this.fNodeExtra, var9, var7, var8);
      }

      if (var2) {
         int var10 = -1;

         for(int var11 = this.getLastChild(var1, false); var11 != -1; var11 = this.getRealPrevSibling(var11, false)) {
            int var12 = this.cloneNode(var11, var2);
            this.insertBefore(var6, var12, var10);
            var10 = var12;
         }
      }

      return var6;
   }

   public void appendChild(int var1, int var2) {
      int var3 = var1 >> 11;
      int var4 = var1 & 2047;
      int var5 = var2 >> 11;
      int var6 = var2 & 2047;
      this.setChunkIndex(this.fNodeParent, var1, var5, var6);
      int var7 = this.getChunkIndex(this.fNodeLastChild, var3, var4);
      this.setChunkIndex(this.fNodePrevSib, var7, var5, var6);
      this.setChunkIndex(this.fNodeLastChild, var2, var3, var4);
   }

   public int setAttributeNode(int var1, int var2) {
      int var3 = var1 >> 11;
      int var4 = var1 & 2047;
      int var5 = var2 >> 11;
      int var6 = var2 & 2047;
      String var7 = this.getChunkValue(this.fNodeName, var5, var6);
      int var8 = this.getChunkIndex(this.fNodeExtra, var3, var4);
      int var9 = -1;
      int var10 = -1;

      int var11;
      for(var11 = -1; var8 != -1; var8 = this.getChunkIndex(this.fNodePrevSib, var10, var11)) {
         var10 = var8 >> 11;
         var11 = var8 & 2047;
         String var12 = this.getChunkValue(this.fNodeName, var10, var11);
         if (var12.equals(var7)) {
            break;
         }

         var9 = var8;
      }

      int var16;
      if (var8 != -1) {
         var16 = this.getChunkIndex(this.fNodePrevSib, var10, var11);
         int var13;
         int var14;
         if (var9 == -1) {
            this.setChunkIndex(this.fNodeExtra, var16, var3, var4);
         } else {
            var13 = var9 >> 11;
            var14 = var9 & 2047;
            this.setChunkIndex(this.fNodePrevSib, var16, var13, var14);
         }

         this.clearChunkIndex(this.fNodeType, var10, var11);
         this.clearChunkValue(this.fNodeName, var10, var11);
         this.clearChunkValue(this.fNodeValue, var10, var11);
         this.clearChunkIndex(this.fNodeParent, var10, var11);
         this.clearChunkIndex(this.fNodePrevSib, var10, var11);
         var13 = this.clearChunkIndex(this.fNodeLastChild, var10, var11);
         var14 = var13 >> 11;
         int var15 = var13 & 2047;
         this.clearChunkIndex(this.fNodeType, var14, var15);
         this.clearChunkValue(this.fNodeValue, var14, var15);
         this.clearChunkIndex(this.fNodeParent, var14, var15);
         this.clearChunkIndex(this.fNodeLastChild, var14, var15);
      }

      var16 = this.getChunkIndex(this.fNodeExtra, var3, var4);
      this.setChunkIndex(this.fNodeExtra, var2, var3, var4);
      this.setChunkIndex(this.fNodePrevSib, var16, var5, var6);
      return var8;
   }

   public void setIdAttributeNode(int var1, int var2) {
      int var3 = var2 >> 11;
      int var4 = var2 & 2047;
      int var5 = this.getChunkIndex(this.fNodeExtra, var3, var4);
      var5 |= 512;
      this.setChunkIndex(this.fNodeExtra, var5, var3, var4);
      String var6 = this.getChunkValue(this.fNodeValue, var3, var4);
      this.putIdentifier(var6, var1);
   }

   public void setIdAttribute(int var1) {
      int var2 = var1 >> 11;
      int var3 = var1 & 2047;
      int var4 = this.getChunkIndex(this.fNodeExtra, var2, var3);
      var4 |= 512;
      this.setChunkIndex(this.fNodeExtra, var4, var2, var3);
   }

   public int insertBefore(int var1, int var2, int var3) {
      if (var3 == -1) {
         this.appendChild(var1, var2);
         return var2;
      } else {
         int var4 = var2 >> 11;
         int var5 = var2 & 2047;
         int var6 = var3 >> 11;
         int var7 = var3 & 2047;
         int var8 = this.getChunkIndex(this.fNodePrevSib, var6, var7);
         this.setChunkIndex(this.fNodePrevSib, var2, var6, var7);
         this.setChunkIndex(this.fNodePrevSib, var8, var4, var5);
         return var2;
      }
   }

   public void setAsLastChild(int var1, int var2) {
      int var3 = var1 >> 11;
      int var4 = var1 & 2047;
      this.setChunkIndex(this.fNodeLastChild, var2, var3, var4);
   }

   public int getParentNode(int var1) {
      return this.getParentNode(var1, false);
   }

   public int getParentNode(int var1, boolean var2) {
      if (var1 == -1) {
         return -1;
      } else {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;
         return var2 ? this.clearChunkIndex(this.fNodeParent, var3, var4) : this.getChunkIndex(this.fNodeParent, var3, var4);
      }
   }

   public int getLastChild(int var1) {
      return this.getLastChild(var1, true);
   }

   public int getLastChild(int var1, boolean var2) {
      if (var1 == -1) {
         return -1;
      } else {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;
         return var2 ? this.clearChunkIndex(this.fNodeLastChild, var3, var4) : this.getChunkIndex(this.fNodeLastChild, var3, var4);
      }
   }

   public int getPrevSibling(int var1) {
      return this.getPrevSibling(var1, true);
   }

   public int getPrevSibling(int var1, boolean var2) {
      if (var1 == -1) {
         return -1;
      } else {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;
         int var5 = this.getChunkIndex(this.fNodeType, var3, var4);
         if (var5 == 3) {
            do {
               var1 = this.getChunkIndex(this.fNodePrevSib, var3, var4);
               if (var1 == -1) {
                  break;
               }

               var3 = var1 >> 11;
               var4 = var1 & 2047;
               var5 = this.getChunkIndex(this.fNodeType, var3, var4);
            } while(var5 == 3);
         } else {
            var1 = this.getChunkIndex(this.fNodePrevSib, var3, var4);
         }

         return var1;
      }
   }

   public int getRealPrevSibling(int var1) {
      return this.getRealPrevSibling(var1, true);
   }

   public int getRealPrevSibling(int var1, boolean var2) {
      if (var1 == -1) {
         return -1;
      } else {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;
         return var2 ? this.clearChunkIndex(this.fNodePrevSib, var3, var4) : this.getChunkIndex(this.fNodePrevSib, var3, var4);
      }
   }

   public int lookupElementDefinition(String var1) {
      if (this.fNodeCount > 1) {
         int var2 = -1;
         int var3 = 0;
         int var4 = 0;

         for(int var5 = this.getChunkIndex(this.fNodeLastChild, var3, var4); var5 != -1; var5 = this.getChunkIndex(this.fNodePrevSib, var3, var4)) {
            var3 = var5 >> 11;
            var4 = var5 & 2047;
            if (this.getChunkIndex(this.fNodeType, var3, var4) == 10) {
               var2 = var5;
               break;
            }
         }

         if (var2 == -1) {
            return -1;
         }

         var3 = var2 >> 11;
         var4 = var2 & 2047;

         for(int var6 = this.getChunkIndex(this.fNodeLastChild, var3, var4); var6 != -1; var6 = this.getChunkIndex(this.fNodePrevSib, var3, var4)) {
            var3 = var6 >> 11;
            var4 = var6 & 2047;
            if (this.getChunkIndex(this.fNodeType, var3, var4) == 21 && this.getChunkValue(this.fNodeName, var3, var4) == var1) {
               return var6;
            }
         }
      }

      return -1;
   }

   public DeferredNode getNodeObject(int var1) {
      if (var1 == -1) {
         return null;
      } else {
         int var2 = var1 >> 11;
         int var3 = var1 & 2047;
         int var4 = this.getChunkIndex(this.fNodeType, var2, var3);
         if (var4 != 3 && var4 != 4) {
            this.clearChunkIndex(this.fNodeType, var2, var3);
         }

         Object var5;
         var5 = null;
         label65:
         switch (var4) {
            case 1:
               if (this.fNamespacesEnabled) {
                  var5 = new DeferredElementNSImpl(this, var1);
               } else {
                  var5 = new DeferredElementImpl(this, var1);
               }

               if (super.docElement == null) {
                  super.docElement = (ElementImpl)var5;
               }

               if (this.fIdElement != null) {
                  int var6 = binarySearch(this.fIdElement, 0, this.fIdCount - 1, var1);

                  while(true) {
                     while(true) {
                        if (var6 == -1) {
                           break label65;
                        }

                        String var7 = this.fIdName[var6];
                        if (var7 != null) {
                           this.putIdentifier0(var7, (Element)var5);
                           this.fIdName[var6] = null;
                        }

                        if (var6 + 1 < this.fIdCount && this.fIdElement[var6 + 1] == var1) {
                           ++var6;
                        } else {
                           var6 = -1;
                        }
                     }
                  }
               }
               break;
            case 2:
               if (this.fNamespacesEnabled) {
                  var5 = new DeferredAttrNSImpl(this, var1);
               } else {
                  var5 = new DeferredAttrImpl(this, var1);
               }
               break;
            case 3:
               var5 = new DeferredTextImpl(this, var1);
               break;
            case 4:
               var5 = new DeferredCDATASectionImpl(this, var1);
               break;
            case 5:
               var5 = new DeferredEntityReferenceImpl(this, var1);
               break;
            case 6:
               var5 = new DeferredEntityImpl(this, var1);
               break;
            case 7:
               var5 = new DeferredProcessingInstructionImpl(this, var1);
               break;
            case 8:
               var5 = new DeferredCommentImpl(this, var1);
               break;
            case 9:
               var5 = this;
               break;
            case 10:
               var5 = new DeferredDocumentTypeImpl(this, var1);
               super.docType = (DocumentTypeImpl)var5;
               break;
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            default:
               throw new IllegalArgumentException("type: " + var4);
            case 12:
               var5 = new DeferredNotationImpl(this, var1);
               break;
            case 21:
               var5 = new DeferredElementDefinitionImpl(this, var1);
         }

         if (var5 != null) {
            return (DeferredNode)var5;
         } else {
            throw new IllegalArgumentException();
         }
      }
   }

   public String getNodeName(int var1) {
      return this.getNodeName(var1, true);
   }

   public String getNodeName(int var1, boolean var2) {
      if (var1 == -1) {
         return null;
      } else {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;
         return var2 ? this.clearChunkValue(this.fNodeName, var3, var4) : this.getChunkValue(this.fNodeName, var3, var4);
      }
   }

   public String getNodeValueString(int var1) {
      return this.getNodeValueString(var1, true);
   }

   public String getNodeValueString(int var1, boolean var2) {
      if (var1 == -1) {
         return null;
      } else {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;
         String var5 = var2 ? this.clearChunkValue(this.fNodeValue, var3, var4) : this.getChunkValue(this.fNodeValue, var3, var4);
         if (var5 == null) {
            return null;
         } else {
            int var6 = this.getChunkIndex(this.fNodeType, var3, var4);
            int var7;
            int var8;
            if (var6 == 3) {
               var7 = this.getRealPrevSibling(var1);
               if (var7 != -1 && this.getNodeType(var7, false) == 3) {
                  this.fStrChunks.addElement(var5);

                  do {
                     var3 = var7 >> 11;
                     var4 = var7 & 2047;
                     var5 = this.getChunkValue(this.fNodeValue, var3, var4);
                     this.fStrChunks.addElement(var5);
                     var7 = this.getChunkIndex(this.fNodePrevSib, var3, var4);
                  } while(var7 != -1 && this.getNodeType(var7, false) == 3);

                  var8 = this.fStrChunks.size();

                  for(int var9 = var8 - 1; var9 >= 0; --var9) {
                     this.fBufferStr.append((String)this.fStrChunks.elementAt(var9));
                  }

                  var5 = this.fBufferStr.toString();
                  this.fStrChunks.removeAllElements();
                  this.fBufferStr.setLength(0);
                  return var5;
               }
            } else if (var6 == 4) {
               var7 = this.getLastChild(var1, false);
               if (var7 != -1) {
                  this.fBufferStr.append(var5);

                  while(var7 != -1) {
                     var3 = var7 >> 11;
                     var4 = var7 & 2047;
                     var5 = this.getChunkValue(this.fNodeValue, var3, var4);
                     this.fStrChunks.addElement(var5);
                     var7 = this.getChunkIndex(this.fNodePrevSib, var3, var4);
                  }

                  for(var8 = this.fStrChunks.size() - 1; var8 >= 0; --var8) {
                     this.fBufferStr.append((String)this.fStrChunks.elementAt(var8));
                  }

                  var5 = this.fBufferStr.toString();
                  this.fStrChunks.setSize(0);
                  this.fBufferStr.setLength(0);
                  return var5;
               }
            }

            return var5;
         }
      }
   }

   public String getNodeValue(int var1) {
      return this.getNodeValue(var1, true);
   }

   public Object getTypeInfo(int var1) {
      if (var1 == -1) {
         return null;
      } else {
         int var2 = var1 >> 11;
         int var3 = var1 & 2047;
         Object var4 = this.fNodeValue[var2] != null ? this.fNodeValue[var2][var3] : null;
         if (var4 != null) {
            this.fNodeValue[var2][var3] = null;
            RefCount var5 = (RefCount)this.fNodeValue[var2][2048];
            --var5.fCount;
            if (var5.fCount == 0) {
               this.fNodeValue[var2] = null;
            }
         }

         return var4;
      }
   }

   public String getNodeValue(int var1, boolean var2) {
      if (var1 == -1) {
         return null;
      } else {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;
         return var2 ? this.clearChunkValue(this.fNodeValue, var3, var4) : this.getChunkValue(this.fNodeValue, var3, var4);
      }
   }

   public int getNodeExtra(int var1) {
      return this.getNodeExtra(var1, true);
   }

   public int getNodeExtra(int var1, boolean var2) {
      if (var1 == -1) {
         return -1;
      } else {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;
         return var2 ? this.clearChunkIndex(this.fNodeExtra, var3, var4) : this.getChunkIndex(this.fNodeExtra, var3, var4);
      }
   }

   public short getNodeType(int var1) {
      return this.getNodeType(var1, true);
   }

   public short getNodeType(int var1, boolean var2) {
      if (var1 == -1) {
         return -1;
      } else {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;
         return var2 ? (short)this.clearChunkIndex(this.fNodeType, var3, var4) : (short)this.getChunkIndex(this.fNodeType, var3, var4);
      }
   }

   public String getAttribute(int var1, String var2) {
      if (var1 != -1 && var2 != null) {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;

         int var6;
         int var7;
         for(int var5 = this.getChunkIndex(this.fNodeExtra, var3, var4); var5 != -1; var5 = this.getChunkIndex(this.fNodePrevSib, var6, var7)) {
            var6 = var5 >> 11;
            var7 = var5 & 2047;
            if (this.getChunkValue(this.fNodeName, var6, var7) == var2) {
               return this.getChunkValue(this.fNodeValue, var6, var7);
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public String getNodeURI(int var1) {
      return this.getNodeURI(var1, true);
   }

   public String getNodeURI(int var1, boolean var2) {
      if (var1 == -1) {
         return null;
      } else {
         int var3 = var1 >> 11;
         int var4 = var1 & 2047;
         return var2 ? this.clearChunkValue(this.fNodeURI, var3, var4) : this.getChunkValue(this.fNodeURI, var3, var4);
      }
   }

   public void putIdentifier(String var1, int var2) {
      if (this.fIdName == null) {
         this.fIdName = new String[64];
         this.fIdElement = new int[64];
      }

      if (this.fIdCount == this.fIdName.length) {
         String[] var3 = new String[this.fIdCount * 2];
         System.arraycopy(this.fIdName, 0, var3, 0, this.fIdCount);
         this.fIdName = var3;
         int[] var4 = new int[var3.length];
         System.arraycopy(this.fIdElement, 0, var4, 0, this.fIdCount);
         this.fIdElement = var4;
      }

      this.fIdName[this.fIdCount] = var1;
      this.fIdElement[this.fIdCount] = var2;
      ++this.fIdCount;
   }

   public void print() {
   }

   public int getNodeIndex() {
      return 0;
   }

   protected void synchronizeData() {
      this.needsSyncData(false);
      if (this.fIdElement != null) {
         IntVector var1 = new IntVector();

         for(int var2 = 0; var2 < this.fIdCount; ++var2) {
            int var3 = this.fIdElement[var2];
            String var4 = this.fIdName[var2];
            if (var4 != null) {
               var1.removeAllElements();
               int var5 = var3;

               int var7;
               do {
                  var1.addElement(var5);
                  int var6 = var5 >> 11;
                  var7 = var5 & 2047;
                  var5 = this.getChunkIndex(this.fNodeParent, var6, var7);
               } while(var5 != -1);

               Object var10 = this;

               for(var7 = var1.size() - 2; var7 >= 0; --var7) {
                  var5 = var1.elementAt(var7);

                  for(Node var8 = ((Node)var10).getLastChild(); var8 != null; var8 = var8.getPreviousSibling()) {
                     if (var8 instanceof DeferredNode) {
                        int var9 = ((DeferredNode)var8).getNodeIndex();
                        if (var9 == var5) {
                           var10 = var8;
                           break;
                        }
                     }
                  }
               }

               Element var11 = (Element)var10;
               this.putIdentifier0(var4, var11);
               this.fIdName[var2] = null;

               while(var2 + 1 < this.fIdCount && this.fIdElement[var2 + 1] == var3) {
                  ++var2;
                  var4 = this.fIdName[var2];
                  if (var4 != null) {
                     this.putIdentifier0(var4, var11);
                  }
               }
            }
         }
      }

   }

   protected void synchronizeChildren() {
      if (this.needsSyncData()) {
         this.synchronizeData();
         if (!this.needsSyncChildren()) {
            return;
         }
      }

      boolean var1 = super.mutationEvents;
      super.mutationEvents = false;
      this.needsSyncChildren(false);
      this.getNodeType(0);
      ChildNode var2 = null;
      ChildNode var3 = null;

      for(int var4 = this.getLastChild(0); var4 != -1; var4 = this.getPrevSibling(var4)) {
         ChildNode var5 = (ChildNode)this.getNodeObject(var4);
         if (var3 == null) {
            var3 = var5;
         } else {
            var2.previousSibling = var5;
         }

         var5.ownerNode = this;
         var5.isOwned(true);
         var5.nextSibling = var2;
         var2 = var5;
         short var6 = var5.getNodeType();
         if (var6 == 1) {
            super.docElement = (ElementImpl)var5;
         } else if (var6 == 10) {
            super.docType = (DocumentTypeImpl)var5;
         }
      }

      if (var2 != null) {
         super.firstChild = var2;
         var2.isFirstChild(true);
         this.lastChild(var3);
      }

      super.mutationEvents = var1;
   }

   protected final void synchronizeChildren(AttrImpl var1, int var2) {
      boolean var3 = this.getMutationEvents();
      this.setMutationEvents(false);
      var1.needsSyncChildren(false);
      int var4 = this.getLastChild(var2);
      int var5 = this.getPrevSibling(var4);
      if (var5 == -1) {
         var1.value = this.getNodeValueString(var2);
         var1.hasStringValue(true);
      } else {
         ChildNode var6 = null;
         ChildNode var7 = null;

         for(int var8 = var4; var8 != -1; var8 = this.getPrevSibling(var8)) {
            ChildNode var9 = (ChildNode)this.getNodeObject(var8);
            if (var7 == null) {
               var7 = var9;
            } else {
               var6.previousSibling = var9;
            }

            var9.ownerNode = var1;
            var9.isOwned(true);
            var9.nextSibling = var6;
            var6 = var9;
         }

         if (var7 != null) {
            var1.value = var6;
            var6.isFirstChild(true);
            var1.lastChild(var7);
         }

         var1.hasStringValue(false);
      }

      this.setMutationEvents(var3);
   }

   protected final void synchronizeChildren(ParentNode var1, int var2) {
      boolean var3 = this.getMutationEvents();
      this.setMutationEvents(false);
      var1.needsSyncChildren(false);
      ChildNode var4 = null;
      ChildNode var5 = null;

      for(int var6 = this.getLastChild(var2); var6 != -1; var6 = this.getPrevSibling(var6)) {
         ChildNode var7 = (ChildNode)this.getNodeObject(var6);
         if (var5 == null) {
            var5 = var7;
         } else {
            var4.previousSibling = var7;
         }

         var7.ownerNode = var1;
         var7.isOwned(true);
         var7.nextSibling = var4;
         var4 = var7;
      }

      if (var5 != null) {
         var1.firstChild = var4;
         var4.isFirstChild(true);
         var1.lastChild(var5);
      }

      this.setMutationEvents(var3);
   }

   protected void ensureCapacity(int var1) {
      if (this.fNodeType == null) {
         this.fNodeType = new int[32][];
         this.fNodeName = new Object[32][];
         this.fNodeValue = new Object[32][];
         this.fNodeParent = new int[32][];
         this.fNodeLastChild = new int[32][];
         this.fNodePrevSib = new int[32][];
         this.fNodeURI = new Object[32][];
         this.fNodeExtra = new int[32][];
      } else if (this.fNodeType.length <= var1) {
         int var2 = var1 * 2;
         int[][] var3 = new int[var2][];
         System.arraycopy(this.fNodeType, 0, var3, 0, var1);
         this.fNodeType = var3;
         Object[][] var4 = new Object[var2][];
         System.arraycopy(this.fNodeName, 0, var4, 0, var1);
         this.fNodeName = var4;
         var4 = new Object[var2][];
         System.arraycopy(this.fNodeValue, 0, var4, 0, var1);
         this.fNodeValue = var4;
         var3 = new int[var2][];
         System.arraycopy(this.fNodeParent, 0, var3, 0, var1);
         this.fNodeParent = var3;
         var3 = new int[var2][];
         System.arraycopy(this.fNodeLastChild, 0, var3, 0, var1);
         this.fNodeLastChild = var3;
         var3 = new int[var2][];
         System.arraycopy(this.fNodePrevSib, 0, var3, 0, var1);
         this.fNodePrevSib = var3;
         var4 = new Object[var2][];
         System.arraycopy(this.fNodeURI, 0, var4, 0, var1);
         this.fNodeURI = var4;
         var3 = new int[var2][];
         System.arraycopy(this.fNodeExtra, 0, var3, 0, var1);
         this.fNodeExtra = var3;
      } else if (this.fNodeType[var1] != null) {
         return;
      }

      this.createChunk(this.fNodeType, var1);
      this.createChunk(this.fNodeName, var1);
      this.createChunk(this.fNodeValue, var1);
      this.createChunk(this.fNodeParent, var1);
      this.createChunk(this.fNodeLastChild, var1);
      this.createChunk(this.fNodePrevSib, var1);
      this.createChunk(this.fNodeURI, var1);
      this.createChunk(this.fNodeExtra, var1);
   }

   protected int createNode(short var1) {
      int var2 = this.fNodeCount >> 11;
      int var3 = this.fNodeCount & 2047;
      this.ensureCapacity(var2);
      this.setChunkIndex(this.fNodeType, var1, var2, var3);
      return this.fNodeCount++;
   }

   protected static int binarySearch(int[] var0, int var1, int var2, int var3) {
      while(var1 <= var2) {
         int var4 = (var1 + var2) / 2;
         int var5 = var0[var4];
         if (var5 == var3) {
            while(var4 > 0 && var0[var4 - 1] == var3) {
               --var4;
            }

            return var4;
         }

         if (var5 > var3) {
            var2 = var4 - 1;
         } else {
            var1 = var4 + 1;
         }
      }

      return -1;
   }

   private final void createChunk(int[][] var1, int var2) {
      var1[var2] = new int[2049];
      System.arraycopy(INIT_ARRAY, 0, var1[var2], 0, 2048);
   }

   private final void createChunk(Object[][] var1, int var2) {
      var1[var2] = new Object[2049];
      var1[var2][2048] = new RefCount();
   }

   private final int setChunkIndex(int[][] var1, int var2, int var3, int var4) {
      if (var2 == -1) {
         return this.clearChunkIndex(var1, var3, var4);
      } else {
         int var5 = var1[var3][var4];
         if (var5 == -1) {
            int var10002 = var1[var3][2048]++;
         }

         var1[var3][var4] = var2;
         return var5;
      }
   }

   private final String setChunkValue(Object[][] var1, Object var2, int var3, int var4) {
      if (var2 == null) {
         return this.clearChunkValue(var1, var3, var4);
      } else {
         String var5 = (String)var1[var3][var4];
         if (var5 == null) {
            RefCount var6 = (RefCount)var1[var3][2048];
            ++var6.fCount;
         }

         var1[var3][var4] = var2;
         return var5;
      }
   }

   private final int getChunkIndex(int[][] var1, int var2, int var3) {
      return var1[var2] != null ? var1[var2][var3] : -1;
   }

   private final String getChunkValue(Object[][] var1, int var2, int var3) {
      return var1[var2] != null ? (String)var1[var2][var3] : null;
   }

   private final String getNodeValue(int var1, int var2) {
      Object var3 = this.fNodeValue[var1][var2];
      if (var3 == null) {
         return null;
      } else {
         return var3 instanceof String ? (String)var3 : var3.toString();
      }
   }

   private final int clearChunkIndex(int[][] var1, int var2, int var3) {
      int var4 = var1[var2] != null ? var1[var2][var3] : -1;
      if (var4 != -1) {
         int var10002 = var1[var2][2048]--;
         var1[var2][var3] = -1;
         if (var1[var2][2048] == 0) {
            var1[var2] = null;
         }
      }

      return var4;
   }

   private final String clearChunkValue(Object[][] var1, int var2, int var3) {
      String var4 = var1[var2] != null ? (String)var1[var2][var3] : null;
      if (var4 != null) {
         var1[var2][var3] = null;
         RefCount var5 = (RefCount)var1[var2][2048];
         --var5.fCount;
         if (var5.fCount == 0) {
            var1[var2] = null;
         }
      }

      return var4;
   }

   private final void putIdentifier0(String var1, Element var2) {
      if (super.identifiers == null) {
         super.identifiers = new Hashtable();
      }

      super.identifiers.put(var1, var2);
   }

   private static void print(int[] var0, int var1, int var2, int var3, int var4) {
   }

   static {
      for(int var0 = 0; var0 < 2048; ++var0) {
         INIT_ARRAY[var0] = -1;
      }

   }

   static class IntVector {
      private int[] data;
      private int size;

      public int size() {
         return this.size;
      }

      public int elementAt(int var1) {
         return this.data[var1];
      }

      public void addElement(int var1) {
         this.ensureCapacity(this.size + 1);
         this.data[this.size++] = var1;
      }

      public void removeAllElements() {
         this.size = 0;
      }

      private void ensureCapacity(int var1) {
         if (this.data == null) {
            this.data = new int[var1 + 15];
         } else if (var1 > this.data.length) {
            int[] var2 = new int[var1 + 15];
            System.arraycopy(this.data, 0, var2, 0, this.data.length);
            this.data = var2;
         }

      }
   }

   class RefCount {
      int fCount;
   }
}
