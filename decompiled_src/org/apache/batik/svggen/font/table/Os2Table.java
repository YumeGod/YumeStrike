package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Os2Table implements Table {
   private int version;
   private short xAvgCharWidth;
   private int usWeightClass;
   private int usWidthClass;
   private short fsType;
   private short ySubscriptXSize;
   private short ySubscriptYSize;
   private short ySubscriptXOffset;
   private short ySubscriptYOffset;
   private short ySuperscriptXSize;
   private short ySuperscriptYSize;
   private short ySuperscriptXOffset;
   private short ySuperscriptYOffset;
   private short yStrikeoutSize;
   private short yStrikeoutPosition;
   private short sFamilyClass;
   private Panose panose;
   private int ulUnicodeRange1;
   private int ulUnicodeRange2;
   private int ulUnicodeRange3;
   private int ulUnicodeRange4;
   private int achVendorID;
   private short fsSelection;
   private int usFirstCharIndex;
   private int usLastCharIndex;
   private short sTypoAscender;
   private short sTypoDescender;
   private short sTypoLineGap;
   private int usWinAscent;
   private int usWinDescent;
   private int ulCodePageRange1;
   private int ulCodePageRange2;

   protected Os2Table(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.version = var2.readUnsignedShort();
      this.xAvgCharWidth = var2.readShort();
      this.usWeightClass = var2.readUnsignedShort();
      this.usWidthClass = var2.readUnsignedShort();
      this.fsType = var2.readShort();
      this.ySubscriptXSize = var2.readShort();
      this.ySubscriptYSize = var2.readShort();
      this.ySubscriptXOffset = var2.readShort();
      this.ySubscriptYOffset = var2.readShort();
      this.ySuperscriptXSize = var2.readShort();
      this.ySuperscriptYSize = var2.readShort();
      this.ySuperscriptXOffset = var2.readShort();
      this.ySuperscriptYOffset = var2.readShort();
      this.yStrikeoutSize = var2.readShort();
      this.yStrikeoutPosition = var2.readShort();
      this.sFamilyClass = var2.readShort();
      byte[] var3 = new byte[10];
      var2.read(var3);
      this.panose = new Panose(var3);
      this.ulUnicodeRange1 = var2.readInt();
      this.ulUnicodeRange2 = var2.readInt();
      this.ulUnicodeRange3 = var2.readInt();
      this.ulUnicodeRange4 = var2.readInt();
      this.achVendorID = var2.readInt();
      this.fsSelection = var2.readShort();
      this.usFirstCharIndex = var2.readUnsignedShort();
      this.usLastCharIndex = var2.readUnsignedShort();
      this.sTypoAscender = var2.readShort();
      this.sTypoDescender = var2.readShort();
      this.sTypoLineGap = var2.readShort();
      this.usWinAscent = var2.readUnsignedShort();
      this.usWinDescent = var2.readUnsignedShort();
      this.ulCodePageRange1 = var2.readInt();
      this.ulCodePageRange2 = var2.readInt();
   }

   public int getVersion() {
      return this.version;
   }

   public short getAvgCharWidth() {
      return this.xAvgCharWidth;
   }

   public int getWeightClass() {
      return this.usWeightClass;
   }

   public int getWidthClass() {
      return this.usWidthClass;
   }

   public short getLicenseType() {
      return this.fsType;
   }

   public short getSubscriptXSize() {
      return this.ySubscriptXSize;
   }

   public short getSubscriptYSize() {
      return this.ySubscriptYSize;
   }

   public short getSubscriptXOffset() {
      return this.ySubscriptXOffset;
   }

   public short getSubscriptYOffset() {
      return this.ySubscriptYOffset;
   }

   public short getSuperscriptXSize() {
      return this.ySuperscriptXSize;
   }

   public short getSuperscriptYSize() {
      return this.ySuperscriptYSize;
   }

   public short getSuperscriptXOffset() {
      return this.ySuperscriptXOffset;
   }

   public short getSuperscriptYOffset() {
      return this.ySuperscriptYOffset;
   }

   public short getStrikeoutSize() {
      return this.yStrikeoutSize;
   }

   public short getStrikeoutPosition() {
      return this.yStrikeoutPosition;
   }

   public short getFamilyClass() {
      return this.sFamilyClass;
   }

   public Panose getPanose() {
      return this.panose;
   }

   public int getUnicodeRange1() {
      return this.ulUnicodeRange1;
   }

   public int getUnicodeRange2() {
      return this.ulUnicodeRange2;
   }

   public int getUnicodeRange3() {
      return this.ulUnicodeRange3;
   }

   public int getUnicodeRange4() {
      return this.ulUnicodeRange4;
   }

   public int getVendorID() {
      return this.achVendorID;
   }

   public short getSelection() {
      return this.fsSelection;
   }

   public int getFirstCharIndex() {
      return this.usFirstCharIndex;
   }

   public int getLastCharIndex() {
      return this.usLastCharIndex;
   }

   public short getTypoAscender() {
      return this.sTypoAscender;
   }

   public short getTypoDescender() {
      return this.sTypoDescender;
   }

   public short getTypoLineGap() {
      return this.sTypoLineGap;
   }

   public int getWinAscent() {
      return this.usWinAscent;
   }

   public int getWinDescent() {
      return this.usWinDescent;
   }

   public int getCodePageRange1() {
      return this.ulCodePageRange1;
   }

   public int getCodePageRange2() {
      return this.ulCodePageRange2;
   }

   public int getType() {
      return 1330851634;
   }
}
