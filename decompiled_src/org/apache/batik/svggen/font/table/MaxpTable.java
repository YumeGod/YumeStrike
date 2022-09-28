package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class MaxpTable implements Table {
   private int versionNumber;
   private int numGlyphs;
   private int maxPoints;
   private int maxContours;
   private int maxCompositePoints;
   private int maxCompositeContours;
   private int maxZones;
   private int maxTwilightPoints;
   private int maxStorage;
   private int maxFunctionDefs;
   private int maxInstructionDefs;
   private int maxStackElements;
   private int maxSizeOfInstructions;
   private int maxComponentElements;
   private int maxComponentDepth;

   protected MaxpTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      this.versionNumber = var2.readInt();
      this.numGlyphs = var2.readUnsignedShort();
      this.maxPoints = var2.readUnsignedShort();
      this.maxContours = var2.readUnsignedShort();
      this.maxCompositePoints = var2.readUnsignedShort();
      this.maxCompositeContours = var2.readUnsignedShort();
      this.maxZones = var2.readUnsignedShort();
      this.maxTwilightPoints = var2.readUnsignedShort();
      this.maxStorage = var2.readUnsignedShort();
      this.maxFunctionDefs = var2.readUnsignedShort();
      this.maxInstructionDefs = var2.readUnsignedShort();
      this.maxStackElements = var2.readUnsignedShort();
      this.maxSizeOfInstructions = var2.readUnsignedShort();
      this.maxComponentElements = var2.readUnsignedShort();
      this.maxComponentDepth = var2.readUnsignedShort();
   }

   public int getMaxComponentDepth() {
      return this.maxComponentDepth;
   }

   public int getMaxComponentElements() {
      return this.maxComponentElements;
   }

   public int getMaxCompositeContours() {
      return this.maxCompositeContours;
   }

   public int getMaxCompositePoints() {
      return this.maxCompositePoints;
   }

   public int getMaxContours() {
      return this.maxContours;
   }

   public int getMaxFunctionDefs() {
      return this.maxFunctionDefs;
   }

   public int getMaxInstructionDefs() {
      return this.maxInstructionDefs;
   }

   public int getMaxPoints() {
      return this.maxPoints;
   }

   public int getMaxSizeOfInstructions() {
      return this.maxSizeOfInstructions;
   }

   public int getMaxStackElements() {
      return this.maxStackElements;
   }

   public int getMaxStorage() {
      return this.maxStorage;
   }

   public int getMaxTwilightPoints() {
      return this.maxTwilightPoints;
   }

   public int getMaxZones() {
      return this.maxZones;
   }

   public int getNumGlyphs() {
      return this.numGlyphs;
   }

   public int getType() {
      return 1835104368;
   }
}
