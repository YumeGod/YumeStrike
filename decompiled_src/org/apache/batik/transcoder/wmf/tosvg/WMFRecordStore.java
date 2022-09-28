package org.apache.batik.transcoder.wmf.tosvg;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WMFRecordStore extends AbstractWMFReader {
   private URL url;
   protected int numRecords;
   protected float vpX;
   protected float vpY;
   protected List records;
   private boolean _bext = true;

   public WMFRecordStore() {
      this.reset();
   }

   public void reset() {
      this.numRecords = 0;
      this.vpX = 0.0F;
      this.vpY = 0.0F;
      this.vpW = 1000;
      this.vpH = 1000;
      this.scaleX = 1.0F;
      this.scaleY = 1.0F;
      this.scaleXY = 1.0F;
      this.inch = 84;
      this.records = new ArrayList(20);
   }

   protected boolean readRecords(DataInputStream var1) throws IOException {
      short var2 = 1;
      boolean var3 = false;

      for(this.numRecords = 0; var2 > 0; ++this.numRecords) {
         int var23 = this.readInt(var1);
         var23 -= 3;
         var2 = this.readShort(var1);
         if (var2 <= 0) {
            break;
         }

         Object var5 = new MetaRecord();
         int var6;
         int var7;
         int var8;
         int var9;
         int var10;
         int var11;
         int var12;
         int var13;
         int var14;
         int var15;
         int var16;
         byte[] var17;
         int var18;
         int var19;
         MetaRecord.ByteRecord var24;
         short var26;
         short var27;
         short var28;
         short var31;
         byte[] var32;
         short var34;
         switch (var2) {
            case 258:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var28 = this.readShort(var1);
               ((MetaRecord)var5).addElement(var28);
               if (var23 > 1) {
                  for(var7 = 1; var7 < var23; ++var7) {
                     this.readShort(var1);
                  }
               }

               this.records.add(var5);
               break;
            case 259:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var28 = this.readShort(var1);
               if (var28 == 8) {
                  this.isotropic = false;
               }

               ((MetaRecord)var5).addElement(var28);
               this.records.add(var5);
               break;
            case 260:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               if (var23 == 1) {
                  var6 = this.readShort(var1);
               } else {
                  var6 = this.readInt(var1);
               }

               ((MetaRecord)var5).addElement(var6);
               this.records.add(var5);
               break;
            case 262:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var28 = this.readShort(var1);
               if (var23 > 1) {
                  for(var7 = 1; var7 < var23; ++var7) {
                     this.readShort(var1);
                  }
               }

               ((MetaRecord)var5).addElement(var28);
               this.records.add(var5);
               break;
            case 302:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var28 = this.readShort(var1);
               if (var23 > 1) {
                  for(var7 = 1; var7 < var23; ++var7) {
                     this.readShort(var1);
                  }
               }

               ((MetaRecord)var5).addElement(var28);
               this.records.add(var5);
               break;
            case 322:
               var6 = var1.readInt() & 255;
               var7 = 2 * var23 - 4;
               var32 = new byte[var7];

               for(var9 = 0; var9 < var7; ++var9) {
                  var32[var9] = var1.readByte();
               }

               var24 = new MetaRecord.ByteRecord(var32);
               var24.numPoints = var23;
               var24.functionId = var2;
               var24.addElement(var6);
               this.records.add(var24);
               break;
            case 513:
            case 521:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var6 = this.readInt(var1);
               var7 = var6 & 255;
               var8 = (var6 & '\uff00') >> 8;
               var9 = (var6 & 16711680) >> 16;
               var10 = (var6 & 50331648) >> 24;
               ((MetaRecord)var5).addElement(var7);
               ((MetaRecord)var5).addElement(var8);
               ((MetaRecord)var5).addElement(var9);
               this.records.add(var5);
               break;
            case 523:
            case 524:
            case 525:
            case 526:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var6 = this.readShort(var1);
               var7 = this.readShort(var1);
               if (var7 < 0) {
                  var7 = -var7;
                  this.xSign = -1;
               }

               if (var6 < 0) {
                  var6 = -var6;
                  this.ySign = -1;
               }

               ((MetaRecord)var5).addElement((int)((float)var7 * this.scaleXY));
               ((MetaRecord)var5).addElement(var6);
               this.records.add(var5);
               if (this._bext && var2 == 524) {
                  this.vpW = var7;
                  this.vpH = var6;
                  if (!this.isotropic) {
                     this.scaleXY = (float)this.vpW / (float)this.vpH;
                  }

                  this.vpW = (int)((float)this.vpW * this.scaleXY);
                  this._bext = false;
               }

               if (!this.isAldus) {
                  this.width = this.vpW;
                  this.height = this.vpH;
               }
               break;
            case 527:
            case 529:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var6 = this.readShort(var1) * this.ySign;
               var7 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               ((MetaRecord)var5).addElement(var7);
               ((MetaRecord)var5).addElement(var6);
               this.records.add(var5);
               break;
            case 531:
            case 532:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var6 = this.readShort(var1) * this.ySign;
               var7 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               ((MetaRecord)var5).addElement(var7);
               ((MetaRecord)var5).addElement(var6);
               this.records.add(var5);
               break;
            case 762:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               ((MetaRecord)var5).addElement(this.readShort(var1));
               var6 = this.readInt(var1);
               var7 = this.readInt(var1);
               if (var23 == 6) {
                  this.readShort(var1);
               }

               var8 = var7 & 255;
               var9 = (var7 & '\uff00') >> 8;
               var10 = (var7 & 16711680) >> 16;
               var11 = (var7 & 50331648) >> 24;
               ((MetaRecord)var5).addElement(var8);
               ((MetaRecord)var5).addElement(var9);
               ((MetaRecord)var5).addElement(var10);
               ((MetaRecord)var5).addElement(var6);
               this.records.add(var5);
               break;
            case 763:
               var28 = this.readShort(var1);
               this.readShort(var1);
               var27 = this.readShort(var1);
               var31 = this.readShort(var1);
               var34 = this.readShort(var1);
               byte var36 = var1.readByte();
               byte var37 = var1.readByte();
               byte var38 = var1.readByte();
               var14 = var1.readByte() & 255;
               byte var43 = var1.readByte();
               byte var41 = var1.readByte();
               byte var44 = var1.readByte();
               byte var45 = var1.readByte();
               var19 = 2 * (var23 - 9);
               byte[] var20 = new byte[var19];

               for(int var22 = 0; var22 < var19; ++var22) {
                  var20[var22] = var1.readByte();
               }

               String var46 = new String(var20);
               MetaRecord.StringRecord var25 = new MetaRecord.StringRecord(var46);
               var25.numPoints = var23;
               var25.functionId = var2;
               var25.addElement(var28);
               var25.addElement(var36);
               var25.addElement(var34);
               var25.addElement(var14);
               var25.addElement(var37);
               var25.addElement(var38);
               var25.addElement(var31);
               var25.addElement(var27);
               this.records.add(var25);
               break;
            case 764:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               ((MetaRecord)var5).addElement(this.readShort(var1));
               var6 = this.readInt(var1);
               var7 = var6 & 255;
               var8 = (var6 & '\uff00') >> 8;
               var9 = (var6 & 16711680) >> 16;
               var10 = (var6 & 50331648) >> 24;
               ((MetaRecord)var5).addElement(var7);
               ((MetaRecord)var5).addElement(var8);
               ((MetaRecord)var5).addElement(var9);
               ((MetaRecord)var5).addElement(this.readShort(var1));
               this.records.add(var5);
               break;
            case 804:
            case 805:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var28 = this.readShort(var1);
               ((MetaRecord)var5).addElement(var28);

               for(var7 = 0; var7 < var28; ++var7) {
                  ((MetaRecord)var5).addElement((int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY));
                  ((MetaRecord)var5).addElement(this.readShort(var1) * this.ySign);
               }

               this.records.add(var5);
               break;
            case 1040:
            case 1042:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var28 = this.readShort(var1);
               var26 = this.readShort(var1);
               var27 = this.readShort(var1);
               var31 = this.readShort(var1);
               ((MetaRecord)var5).addElement(var27);
               ((MetaRecord)var5).addElement(var28);
               ((MetaRecord)var5).addElement(var31);
               ((MetaRecord)var5).addElement(var26);
               this.records.add(var5);
               this.scaleX = this.scaleX * (float)var27 / (float)var31;
               this.scaleY = this.scaleY * (float)var28 / (float)var26;
               break;
            case 1046:
            case 1048:
            case 1051:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var6 = this.readShort(var1) * this.ySign;
               var7 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var8 = this.readShort(var1) * this.ySign;
               var9 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               ((MetaRecord)var5).addElement(var9);
               ((MetaRecord)var5).addElement(var8);
               ((MetaRecord)var5).addElement(var7);
               ((MetaRecord)var5).addElement(var6);
               this.records.add(var5);
               break;
            case 1313:
               var28 = this.readShort(var1);
               byte var30 = 1;
               var32 = new byte[var28];

               for(var9 = 0; var9 < var28; ++var9) {
                  var32[var9] = var1.readByte();
               }

               if (var28 % 2 != 0) {
                  var1.readByte();
               }

               var7 = var30 + (var28 + 1) / 2;
               var9 = this.readShort(var1) * this.ySign;
               var10 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var7 += 2;
               if (var7 < var23) {
                  for(var11 = var7; var11 < var23; ++var11) {
                     this.readShort(var1);
                  }
               }

               var24 = new MetaRecord.ByteRecord(var32);
               var24.numPoints = var23;
               var24.functionId = var2;
               var24.addElement(var10);
               var24.addElement(var9);
               this.records.add(var24);
               break;
            case 1336:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var28 = this.readShort(var1);
               int[] var29 = new int[var28];
               var8 = 0;

               for(var9 = 0; var9 < var28; ++var9) {
                  var29[var9] = this.readShort(var1);
                  var8 += var29[var9];
               }

               ((MetaRecord)var5).addElement(var28);

               for(var9 = 0; var9 < var28; ++var9) {
                  ((MetaRecord)var5).addElement(var29[var9]);
               }

               var9 = var28 + 1;

               for(var10 = 0; var10 < var28; ++var10) {
                  var11 = var29[var10];

                  for(var12 = 0; var12 < var11; ++var12) {
                     ((MetaRecord)var5).addElement((int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY));
                     ((MetaRecord)var5).addElement(this.readShort(var1) * this.ySign);
                  }
               }

               this.records.add(var5);
               break;
            case 1564:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var6 = this.readShort(var1) * this.ySign;
               var7 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var8 = this.readShort(var1) * this.ySign;
               var9 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var10 = this.readShort(var1) * this.ySign;
               var11 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               ((MetaRecord)var5).addElement(var11);
               ((MetaRecord)var5).addElement(var10);
               ((MetaRecord)var5).addElement(var9);
               ((MetaRecord)var5).addElement(var8);
               ((MetaRecord)var5).addElement(var7);
               ((MetaRecord)var5).addElement(var6);
               this.records.add(var5);
               break;
            case 1565:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var6 = this.readInt(var1);
               var7 = this.readShort(var1) * this.ySign;
               var8 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var9 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var10 = this.readShort(var1) * this.ySign;
               ((MetaRecord)var5).addElement(var6);
               ((MetaRecord)var5).addElement(var7);
               ((MetaRecord)var5).addElement(var8);
               ((MetaRecord)var5).addElement(var10);
               ((MetaRecord)var5).addElement(var9);
               this.records.add(var5);
               break;
            case 1583:
               for(var6 = 0; var6 < var23; ++var6) {
                  this.readShort(var1);
               }

               --this.numRecords;
               break;
            case 1791:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var6 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var7 = this.readShort(var1) * this.ySign;
               var8 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var9 = this.readShort(var1) * this.ySign;
               ((MetaRecord)var5).addElement(var6);
               ((MetaRecord)var5).addElement(var7);
               ((MetaRecord)var5).addElement(var8);
               ((MetaRecord)var5).addElement(var9);
               this.records.add(var5);
               break;
            case 2071:
            case 2074:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;
               var6 = this.readShort(var1) * this.ySign;
               var7 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var8 = this.readShort(var1) * this.ySign;
               var9 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var10 = this.readShort(var1) * this.ySign;
               var11 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var12 = this.readShort(var1) * this.ySign;
               var13 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               ((MetaRecord)var5).addElement(var13);
               ((MetaRecord)var5).addElement(var12);
               ((MetaRecord)var5).addElement(var11);
               ((MetaRecord)var5).addElement(var10);
               ((MetaRecord)var5).addElement(var9);
               ((MetaRecord)var5).addElement(var8);
               ((MetaRecord)var5).addElement(var7);
               ((MetaRecord)var5).addElement(var6);
               this.records.add(var5);
               break;
            case 2368:
               var6 = var1.readInt() & 255;
               var26 = this.readShort(var1);
               var27 = this.readShort(var1);
               this.readShort(var1);
               var34 = this.readShort(var1);
               var11 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               short var35 = this.readShort(var1);
               var13 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var14 = 2 * var23 - 18;
               if (var14 <= 0) {
                  ((MetaRecord)var5).numPoints = var23;
                  ((MetaRecord)var5).functionId = var2;

                  for(var15 = 0; var15 < var14; ++var15) {
                     var1.readByte();
                  }
               } else {
                  byte[] var40 = new byte[var14];

                  for(var16 = 0; var16 < var14; ++var16) {
                     var40[var16] = var1.readByte();
                  }

                  var5 = new MetaRecord.ByteRecord(var40);
                  ((MetaRecord)var5).numPoints = var23;
                  ((MetaRecord)var5).functionId = var2;
               }

               ((MetaRecord)var5).addElement(var6);
               ((MetaRecord)var5).addElement(var34);
               ((MetaRecord)var5).addElement(var11);
               ((MetaRecord)var5).addElement(var26);
               ((MetaRecord)var5).addElement(var27);
               ((MetaRecord)var5).addElement(var35);
               ((MetaRecord)var5).addElement(var13);
               this.records.add(var5);
               break;
            case 2610:
               var6 = this.readShort(var1) * this.ySign;
               var7 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var27 = this.readShort(var1);
               var31 = this.readShort(var1);
               var10 = 4;
               boolean var33 = false;
               var12 = 0;
               var13 = 0;
               var14 = 0;
               var15 = 0;
               if ((var31 & 4) != 0) {
                  var12 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
                  var13 = this.readShort(var1) * this.ySign;
                  var14 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
                  var15 = this.readShort(var1) * this.ySign;
                  var10 += 4;
                  var33 = true;
               }

               var17 = new byte[var27];

               for(var18 = 0; var18 < var27; ++var18) {
                  var17[var18] = var1.readByte();
               }

               var10 += (var27 + 1) / 2;
               if (var27 % 2 != 0) {
                  var1.readByte();
               }

               if (var10 < var23) {
                  for(var19 = var10; var19 < var23; ++var19) {
                     this.readShort(var1);
                  }
               }

               var24 = new MetaRecord.ByteRecord(var17);
               var24.numPoints = var23;
               var24.functionId = var2;
               var24.addElement(var7);
               var24.addElement(var6);
               var24.addElement(var31);
               if (var33) {
                  var24.addElement(var12);
                  var24.addElement(var13);
                  var24.addElement(var14);
                  var24.addElement(var15);
               }

               this.records.add(var24);
               break;
            case 2881:
               var6 = var1.readInt() & 255;
               var7 = this.readShort(var1) * this.ySign;
               var8 = this.readShort(var1) * this.xSign;
               var9 = this.readShort(var1) * this.ySign;
               var10 = this.readShort(var1) * this.xSign;
               var11 = this.readShort(var1) * this.ySign;
               var12 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var13 = this.readShort(var1) * this.ySign;
               var14 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var15 = 2 * var23 - 20;
               byte[] var39 = new byte[var15];

               for(int var42 = 0; var42 < var15; ++var42) {
                  var39[var42] = var1.readByte();
               }

               var24 = new MetaRecord.ByteRecord(var39);
               var24.numPoints = var23;
               var24.functionId = var2;
               var24.addElement(var6);
               var24.addElement(var7);
               var24.addElement(var8);
               var24.addElement(var9);
               var24.addElement(var10);
               var24.addElement(var11);
               var24.addElement(var12);
               var24.addElement(var13);
               var24.addElement(var14);
               this.records.add(var24);
               break;
            case 3907:
               var6 = var1.readInt() & 255;
               this.readShort(var1);
               var8 = this.readShort(var1) * this.ySign;
               var9 = this.readShort(var1) * this.xSign;
               var10 = this.readShort(var1) * this.ySign;
               var11 = this.readShort(var1) * this.xSign;
               var12 = this.readShort(var1) * this.ySign;
               var13 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var14 = this.readShort(var1) * this.ySign;
               var15 = (int)((float)(this.readShort(var1) * this.xSign) * this.scaleXY);
               var16 = 2 * var23 - 22;
               var17 = new byte[var16];

               for(var18 = 0; var18 < var16; ++var18) {
                  var17[var18] = var1.readByte();
               }

               var24 = new MetaRecord.ByteRecord(var17);
               var24.numPoints = var23;
               var24.functionId = var2;
               var24.addElement(var6);
               var24.addElement(var8);
               var24.addElement(var9);
               var24.addElement(var10);
               var24.addElement(var11);
               var24.addElement(var12);
               var24.addElement(var13);
               var24.addElement(var14);
               var24.addElement(var15);
               this.records.add(var24);
               break;
            default:
               ((MetaRecord)var5).numPoints = var23;
               ((MetaRecord)var5).functionId = var2;

               for(var6 = 0; var6 < var23; ++var6) {
                  ((MetaRecord)var5).addElement(this.readShort(var1));
               }

               this.records.add(var5);
         }
      }

      if (!this.isAldus) {
         this.right = (int)this.vpX;
         this.left = (int)(this.vpX + (float)this.vpW);
         this.top = (int)this.vpY;
         this.bottom = (int)(this.vpY + (float)this.vpH);
      }

      this.setReading(false);
      return true;
   }

   public URL getUrl() {
      return this.url;
   }

   public void setUrl(URL var1) {
      this.url = var1;
   }

   public MetaRecord getRecord(int var1) {
      return (MetaRecord)this.records.get(var1);
   }

   public int getNumRecords() {
      return this.numRecords;
   }

   public float getVpX() {
      return this.vpX;
   }

   public float getVpY() {
      return this.vpY;
   }

   public void setVpX(float var1) {
      this.vpX = var1;
   }

   public void setVpY(float var1) {
      this.vpY = var1;
   }
}
