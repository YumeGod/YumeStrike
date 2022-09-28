package org.apache.xerces.util;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;

public class XMLAttributesImpl implements XMLAttributes {
   protected static final int TABLE_SIZE = 101;
   protected static final int SIZE_LIMIT = 20;
   protected boolean fNamespaces;
   protected int fLargeCount;
   protected int fLength;
   protected Attribute[] fAttributes;
   protected Attribute[] fAttributeTableView;
   protected int[] fAttributeTableViewChainState;
   protected int fTableViewBuckets;
   protected boolean fIsTableViewConsistent;

   public XMLAttributesImpl() {
      this(101);
   }

   public XMLAttributesImpl(int var1) {
      this.fNamespaces = true;
      this.fLargeCount = 1;
      this.fAttributes = new Attribute[4];
      this.fTableViewBuckets = var1;

      for(int var2 = 0; var2 < this.fAttributes.length; ++var2) {
         this.fAttributes[var2] = new Attribute();
      }

   }

   public void setNamespaces(boolean var1) {
      this.fNamespaces = var1;
   }

   public int addAttribute(QName var1, String var2, String var3) {
      int var4;
      if (this.fLength < 20) {
         var4 = var1.uri != null && !var1.uri.equals("") ? this.getIndexFast(var1.uri, var1.localpart) : this.getIndexFast(var1.rawname);
         if (var4 == -1) {
            var4 = this.fLength;
            if (this.fLength++ == this.fAttributes.length) {
               Attribute[] var5 = new Attribute[this.fAttributes.length + 4];
               System.arraycopy(this.fAttributes, 0, var5, 0, this.fAttributes.length);

               for(int var6 = this.fAttributes.length; var6 < var5.length; ++var6) {
                  var5[var6] = new Attribute();
               }

               this.fAttributes = var5;
            }
         }
      } else if (var1.uri == null || var1.uri.length() == 0 || (var4 = this.getIndexFast(var1.uri, var1.localpart)) == -1) {
         if (!this.fIsTableViewConsistent || this.fLength == 20) {
            this.prepareAndPopulateTableView();
            this.fIsTableViewConsistent = true;
         }

         int var9 = this.getTableViewBucket(var1.rawname);
         if (this.fAttributeTableViewChainState[var9] != this.fLargeCount) {
            var4 = this.fLength;
            if (this.fLength++ == this.fAttributes.length) {
               Attribute[] var11 = new Attribute[this.fAttributes.length << 1];
               System.arraycopy(this.fAttributes, 0, var11, 0, this.fAttributes.length);

               for(int var7 = this.fAttributes.length; var7 < var11.length; ++var7) {
                  var11[var7] = new Attribute();
               }

               this.fAttributes = var11;
            }

            this.fAttributeTableViewChainState[var9] = this.fLargeCount;
            this.fAttributes[var4].next = null;
            this.fAttributeTableView[var9] = this.fAttributes[var4];
         } else {
            Attribute var12;
            for(var12 = this.fAttributeTableView[var9]; var12 != null && var12.name.rawname != var1.rawname; var12 = var12.next) {
            }

            if (var12 == null) {
               var4 = this.fLength;
               if (this.fLength++ == this.fAttributes.length) {
                  Attribute[] var13 = new Attribute[this.fAttributes.length << 1];
                  System.arraycopy(this.fAttributes, 0, var13, 0, this.fAttributes.length);

                  for(int var8 = this.fAttributes.length; var8 < var13.length; ++var8) {
                     var13[var8] = new Attribute();
                  }

                  this.fAttributes = var13;
               }

               this.fAttributes[var4].next = this.fAttributeTableView[var9];
               this.fAttributeTableView[var9] = this.fAttributes[var4];
            } else {
               var4 = this.getIndexFast(var1.rawname);
            }
         }
      }

      Attribute var10 = this.fAttributes[var4];
      var10.name.setValues(var1);
      var10.type = var2;
      var10.value = var3;
      var10.nonNormalizedValue = var3;
      var10.specified = false;
      var10.augs.removeAllItems();
      return var4;
   }

   public void removeAllAttributes() {
      this.fLength = 0;
   }

   public void removeAttributeAt(int var1) {
      this.fIsTableViewConsistent = false;
      if (var1 < this.fLength - 1) {
         Attribute var2 = this.fAttributes[var1];
         System.arraycopy(this.fAttributes, var1 + 1, this.fAttributes, var1, this.fLength - var1 - 1);
         this.fAttributes[this.fLength - 1] = var2;
      }

      --this.fLength;
   }

   public void setName(int var1, QName var2) {
      this.fAttributes[var1].name.setValues(var2);
   }

   public void getName(int var1, QName var2) {
      var2.setValues(this.fAttributes[var1].name);
   }

   public void setType(int var1, String var2) {
      this.fAttributes[var1].type = var2;
   }

   public void setValue(int var1, String var2) {
      Attribute var3 = this.fAttributes[var1];
      var3.value = var2;
      var3.nonNormalizedValue = var2;
   }

   public void setNonNormalizedValue(int var1, String var2) {
      if (var2 == null) {
         var2 = this.fAttributes[var1].value;
      }

      this.fAttributes[var1].nonNormalizedValue = var2;
   }

   public String getNonNormalizedValue(int var1) {
      String var2 = this.fAttributes[var1].nonNormalizedValue;
      return var2;
   }

   public void setSpecified(int var1, boolean var2) {
      this.fAttributes[var1].specified = var2;
   }

   public boolean isSpecified(int var1) {
      return this.fAttributes[var1].specified;
   }

   public int getLength() {
      return this.fLength;
   }

   public String getType(int var1) {
      return var1 >= 0 && var1 < this.fLength ? this.getReportableType(this.fAttributes[var1].type) : null;
   }

   public String getType(String var1) {
      int var2 = this.getIndex(var1);
      return var2 != -1 ? this.getReportableType(this.fAttributes[var2].type) : null;
   }

   public String getValue(int var1) {
      return var1 >= 0 && var1 < this.fLength ? this.fAttributes[var1].value : null;
   }

   public String getValue(String var1) {
      int var2 = this.getIndex(var1);
      return var2 != -1 ? this.fAttributes[var2].value : null;
   }

   public String getName(int var1) {
      return var1 >= 0 && var1 < this.fLength ? this.fAttributes[var1].name.rawname : null;
   }

   public int getIndex(String var1) {
      for(int var2 = 0; var2 < this.fLength; ++var2) {
         Attribute var3 = this.fAttributes[var2];
         if (var3.name.rawname != null && var3.name.rawname.equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public int getIndex(String var1, String var2) {
      for(int var3 = 0; var3 < this.fLength; ++var3) {
         Attribute var4 = this.fAttributes[var3];
         if (var4.name.localpart != null && var4.name.localpart.equals(var2) && (var1 == var4.name.uri || var1 != null && var4.name.uri != null && var4.name.uri.equals(var1))) {
            return var3;
         }
      }

      return -1;
   }

   public String getLocalName(int var1) {
      if (!this.fNamespaces) {
         return "";
      } else {
         return var1 >= 0 && var1 < this.fLength ? this.fAttributes[var1].name.localpart : null;
      }
   }

   public String getQName(int var1) {
      if (var1 >= 0 && var1 < this.fLength) {
         String var2 = this.fAttributes[var1].name.rawname;
         return var2 != null ? var2 : "";
      } else {
         return null;
      }
   }

   public String getType(String var1, String var2) {
      if (!this.fNamespaces) {
         return null;
      } else {
         int var3 = this.getIndex(var1, var2);
         return var3 != -1 ? this.getReportableType(this.fAttributes[var3].type) : null;
      }
   }

   public String getPrefix(int var1) {
      if (var1 >= 0 && var1 < this.fLength) {
         String var2 = this.fAttributes[var1].name.prefix;
         return var2 != null ? var2 : "";
      } else {
         return null;
      }
   }

   public String getURI(int var1) {
      if (var1 >= 0 && var1 < this.fLength) {
         String var2 = this.fAttributes[var1].name.uri;
         return var2;
      } else {
         return null;
      }
   }

   public String getValue(String var1, String var2) {
      int var3 = this.getIndex(var1, var2);
      return var3 != -1 ? this.getValue(var3) : null;
   }

   public Augmentations getAugmentations(String var1, String var2) {
      int var3 = this.getIndex(var1, var2);
      return var3 != -1 ? this.fAttributes[var3].augs : null;
   }

   public Augmentations getAugmentations(String var1) {
      int var2 = this.getIndex(var1);
      return var2 != -1 ? this.fAttributes[var2].augs : null;
   }

   public Augmentations getAugmentations(int var1) {
      return var1 >= 0 && var1 < this.fLength ? this.fAttributes[var1].augs : null;
   }

   public void setAugmentations(int var1, Augmentations var2) {
      this.fAttributes[var1].augs = var2;
   }

   public void setURI(int var1, String var2) {
      this.fAttributes[var1].name.uri = var2;
   }

   public void setSchemaId(int var1, boolean var2) {
      this.fAttributes[var1].schemaId = var2;
   }

   public boolean getSchemaId(int var1) {
      return var1 >= 0 && var1 < this.fLength ? this.fAttributes[var1].schemaId : false;
   }

   public boolean getSchemaId(String var1) {
      int var2 = this.getIndex(var1);
      return var2 != -1 ? this.fAttributes[var2].schemaId : false;
   }

   public boolean getSchemaId(String var1, String var2) {
      if (!this.fNamespaces) {
         return false;
      } else {
         int var3 = this.getIndex(var1, var2);
         return var3 != -1 ? this.fAttributes[var3].schemaId : false;
      }
   }

   public int getIndexFast(String var1) {
      for(int var2 = 0; var2 < this.fLength; ++var2) {
         Attribute var3 = this.fAttributes[var2];
         if (var3.name.rawname == var1) {
            return var2;
         }
      }

      return -1;
   }

   public void addAttributeNS(QName var1, String var2, String var3) {
      int var4 = this.fLength;
      if (this.fLength++ == this.fAttributes.length) {
         Attribute[] var5;
         if (this.fLength < 20) {
            var5 = new Attribute[this.fAttributes.length + 4];
         } else {
            var5 = new Attribute[this.fAttributes.length << 1];
         }

         System.arraycopy(this.fAttributes, 0, var5, 0, this.fAttributes.length);

         for(int var6 = this.fAttributes.length; var6 < var5.length; ++var6) {
            var5[var6] = new Attribute();
         }

         this.fAttributes = var5;
      }

      Attribute var7 = this.fAttributes[var4];
      var7.name.setValues(var1);
      var7.type = var2;
      var7.value = var3;
      var7.nonNormalizedValue = var3;
      var7.specified = false;
      var7.augs.removeAllItems();
   }

   public QName checkDuplicatesNS() {
      int var3;
      Attribute var4;
      if (this.fLength <= 20) {
         for(int var1 = 0; var1 < this.fLength - 1; ++var1) {
            Attribute var2 = this.fAttributes[var1];

            for(var3 = var1 + 1; var3 < this.fLength; ++var3) {
               var4 = this.fAttributes[var3];
               if (var2.name.localpart == var4.name.localpart && var2.name.uri == var4.name.uri) {
                  return var4.name;
               }
            }
         }
      } else {
         this.fIsTableViewConsistent = false;
         this.prepareTableView();

         for(var3 = this.fLength - 1; var3 >= 0; --var3) {
            Attribute var5 = this.fAttributes[var3];
            int var6 = this.getTableViewBucket(var5.name.localpart, var5.name.uri);
            if (this.fAttributeTableViewChainState[var6] != this.fLargeCount) {
               this.fAttributeTableViewChainState[var6] = this.fLargeCount;
               var5.next = null;
               this.fAttributeTableView[var6] = var5;
            } else {
               for(var4 = this.fAttributeTableView[var6]; var4 != null; var4 = var4.next) {
                  if (var4.name.localpart == var5.name.localpart && var4.name.uri == var5.name.uri) {
                     return var5.name;
                  }
               }

               var5.next = this.fAttributeTableView[var6];
               this.fAttributeTableView[var6] = var5;
            }
         }
      }

      return null;
   }

   public int getIndexFast(String var1, String var2) {
      for(int var3 = 0; var3 < this.fLength; ++var3) {
         Attribute var4 = this.fAttributes[var3];
         if (var4.name.localpart == var2 && var4.name.uri == var1) {
            return var3;
         }
      }

      return -1;
   }

   private String getReportableType(String var1) {
      return var1.charAt(0) == '(' ? "NMTOKEN" : var1;
   }

   protected int getTableViewBucket(String var1) {
      return (var1.hashCode() & Integer.MAX_VALUE) % this.fTableViewBuckets;
   }

   protected int getTableViewBucket(String var1, String var2) {
      return var2 == null ? (var1.hashCode() & Integer.MAX_VALUE) % this.fTableViewBuckets : (var1.hashCode() + var2.hashCode() & Integer.MAX_VALUE) % this.fTableViewBuckets;
   }

   protected void cleanTableView() {
      if (++this.fLargeCount < 0) {
         if (this.fAttributeTableViewChainState != null) {
            for(int var1 = this.fTableViewBuckets - 1; var1 >= 0; --var1) {
               this.fAttributeTableViewChainState[var1] = 0;
            }
         }

         this.fLargeCount = 1;
      }

   }

   protected void prepareTableView() {
      if (this.fAttributeTableView == null) {
         this.fAttributeTableView = new Attribute[this.fTableViewBuckets];
         this.fAttributeTableViewChainState = new int[this.fTableViewBuckets];
      } else {
         this.cleanTableView();
      }

   }

   protected void prepareAndPopulateTableView() {
      this.prepareTableView();

      for(int var3 = 0; var3 < this.fLength; ++var3) {
         Attribute var1 = this.fAttributes[var3];
         int var2 = this.getTableViewBucket(var1.name.rawname);
         if (this.fAttributeTableViewChainState[var2] != this.fLargeCount) {
            this.fAttributeTableViewChainState[var2] = this.fLargeCount;
            var1.next = null;
            this.fAttributeTableView[var2] = var1;
         } else {
            var1.next = this.fAttributeTableView[var2];
            this.fAttributeTableView[var2] = var1;
         }
      }

   }

   static class Attribute {
      public QName name = new QName();
      public String type;
      public String value;
      public String nonNormalizedValue;
      public boolean specified;
      public boolean schemaId;
      public Augmentations augs = new AugmentationsImpl();
      public Attribute next;
   }
}
