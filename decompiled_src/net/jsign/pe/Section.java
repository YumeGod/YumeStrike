package net.jsign.pe;

import java.util.List;

public class Section {
   private PEFile peFile;
   private int baseOffset;

   Section(PEFile peFile, int baseOffset) {
      this.peFile = peFile;
      this.baseOffset = baseOffset;
   }

   public String getName() {
      byte[] buffer = new byte[8];
      this.peFile.read(buffer, (long)this.baseOffset, 0);
      String name = new String(buffer);
      if (name.indexOf(0) != -1) {
         name = name.substring(0, name.indexOf(0));
      }

      return name;
   }

   public long getVirtualSize() {
      return this.peFile.readDWord((long)this.baseOffset, 8);
   }

   public long getVirtualAddress() {
      return this.peFile.readDWord((long)this.baseOffset, 12);
   }

   public long getSizeOfRawData() {
      return this.peFile.readDWord((long)this.baseOffset, 16);
   }

   public long getPointerToRawData() {
      return this.peFile.readDWord((long)this.baseOffset, 20);
   }

   public long getPointerToRelocations() {
      return this.peFile.readDWord((long)this.baseOffset, 24);
   }

   public long getPointerToLineNumbers() {
      return this.peFile.readDWord((long)this.baseOffset, 28);
   }

   public int getNumberOfRelocations() {
      return this.peFile.readWord((long)this.baseOffset, 32);
   }

   public int getNumberOfLineNumbers() {
      return this.peFile.readWord((long)this.baseOffset, 34);
   }

   public List getCharacteristics() {
      return SectionFlag.getFlags((int)this.peFile.readDWord((long)this.baseOffset, 36));
   }
}
