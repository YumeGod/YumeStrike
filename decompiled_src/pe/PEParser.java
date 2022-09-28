package pe;

import common.AssertUtils;
import common.CommonUtils;
import common.MudgeSanity;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

public class PEParser {
   protected DataInputStream content;
   protected byte[] bdata;
   protected ByteBuffer buffer;
   protected Map values;
   protected byte[] original;
   protected Stack frames;
   protected Map locations;
   protected LinkedList strings;
   protected boolean procassembly;

   public boolean isProcessAssembly() {
      return this.procassembly;
   }

   protected void parseDirectory(int var1) throws IOException {
      long var2 = this.readLong();
      long var4 = this.readLong();
      this.put("DataDirectory." + var1 + ".VirtualAddress", var2);
      this.put("DataDirectory." + var1 + ".Size", var4);
   }

   protected void parseFunctionNameHint(int var1, int var2, int var3, List var4) throws IOException {
      this.jump((long)(var3 - var2 + var1));
      int var5 = this.readShort();
      String var6 = this.readString();
      var4.add(var6 + "@" + var5);
      this.complete();
   }

   protected List parseFunctionNameList(int var1, int var2, int var3) throws IOException {
      LinkedList var4 = new LinkedList();
      this.jump((long)(var1 - var2 + var3));

      while(true) {
         if (this.is64()) {
            long var7 = this.readQWord();
            if ((var7 & Long.MIN_VALUE) == Long.MIN_VALUE) {
               var7 &= Long.MAX_VALUE;
               var4.add("<ordinal>@" + var7);
            } else {
               if (var7 <= 0L) {
                  break;
               }

               this.parseFunctionNameHint(var3, var2, (int)var7, var4);
            }
         } else {
            int var5 = this.readInt();
            if ((var5 & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
               var5 &= Integer.MAX_VALUE;
               var4.add("<ordinal>@" + var5);
            } else {
               if (var5 <= 0) {
                  break;
               }

               this.parseFunctionNameHint(var3, var2, var5, var4);
            }
         }
      }

      this.complete();
      return var4;
   }

   protected boolean parseImport(int var1) throws IOException {
      int var2 = this.dirEntry(1);
      int var3 = this.get("DataDirectory.1.VirtualAddress");
      long var4 = (long)this.readInt();
      this.consume(8);
      long var6 = (long)this.readInt();
      long var8 = (long)this.readInt();
      if (var4 == 0L && var6 == 0L && var8 == 0L) {
         return false;
      } else {
         this.put("Import." + var1 + ".RVAFunctionNameList", var4);
         this.put("Import." + var1 + ".RVAFunctionNameList.X", this.parseFunctionNameList((int)var4, var3, var2));
         this.put("Import." + var1 + ".RVAModuleName", var6);
         this.put("Import." + var1 + ".RVAModuleName.X", this.getStringFromPointer((int)var6, var3, var2));
         this.put("Import." + var1 + ".RVAFunctionAddressList", var8);
         return true;
      }
   }

   public int getPointerForLocation(int var1, int var2) {
      int var3 = this.dirEntry(var1);
      int var4 = this.get("DataDirectory." + var1 + ".VirtualAddress");
      return var2 - var3 + var4;
   }

   protected String getStringFromPointer(int var1, int var2, int var3) throws IOException {
      this.jump((long)(var1 - var2 + var3));
      String var4 = this.readString();
      this.complete();
      return var4;
   }

   public List getExportedFunctions() {
      return (List)this.values.get("Export.FunctionNames");
   }

   public byte[] carveExportedFunction(String var1) {
      int var2 = this.getFunctionOffset(var1);
      int var3 = this.getNextFunctionOffset(var1);
      if (var2 == -1) {
         CommonUtils.print_error("Could not find '" + var1 + "' in DLL");
         return new byte[0];
      } else {
         return Arrays.copyOfRange(this.original, var2, var3);
      }
   }

   public int getFunctionOffset(String var1) {
      List var2 = this.getExportedFunctions();
      List var3 = (List)this.values.get("Export.FunctionAddressesFixed");
      Iterator var4 = var2.iterator();
      Iterator var5 = var3.iterator();

      while(var4.hasNext() && var5.hasNext()) {
         String var6 = (String)var4.next();
         Long var7 = (Long)var5.next();
         if (var1.equals(var6)) {
            return (int)var7;
         }
      }

      return -1;
   }

   public int getNextFunctionOffset(String var1) {
      long var2 = (long)this.getFunctionOffset(var1);
      List var4 = this.getExportedFunctions();
      List var5 = (List)this.values.get("Export.FunctionAddressesFixed");
      Iterator var6 = var4.iterator();
      Iterator var7 = var5.iterator();

      while(var6.hasNext() && var7.hasNext()) {
         Long var8 = (Long)var7.next();
         if (var8 > var2) {
            return (int)var8;
         }
      }

      int var10 = this.get(".text.PointerToRawData");
      int var9 = this.get(".text.SizeOfRawData");
      return var10 + var9;
   }

   protected void parseExport() throws IOException {
      int var1 = this.dirEntry(0);
      int var2 = this.get("DataDirectory.0.VirtualAddress");
      this.consume(12);
      this.report("Export.Name");
      this.put("Export.Name", this.getStringFromPointer(this.readInt(), var2, var1));
      this.put("Export.Base", (long)this.readInt());
      this.put("Export.NumberOfFunctions", (long)this.readInt());
      this.put("Export.NumberOfNames", (long)this.readInt());
      this.put("Export.AddressOfFunctions", (long)this.readInt());
      this.put("Export.AddressOfNames", (long)this.readInt());
      this.put("Export.AddressOfNameOridinals", (long)this.readInt());
      this.jump((long)(this.get("Export.AddressOfNames") - var2 + var1));
      this.jump((long)(this.readInt() - var2 + var1));
      LinkedList var3 = new LinkedList();

      int var4;
      for(var4 = 0; var4 < this.get("Export.NumberOfNames"); ++var4) {
         this.report("Export.AddressOfName." + var4);
         var3.add(this.readString());
      }

      this.put("Export.FunctionNames", (List)var3);
      this.complete();
      this.complete();
      this.jump((long)(this.get("Export.AddressOfFunctions") - var2 + var1));
      var3 = new LinkedList();

      for(var4 = 0; var4 < this.get("Export.NumberOfNames"); ++var4) {
         var3.add(this.readLong());
      }

      this.put("Export.FunctionAddresses", (List)var3);
      this.complete();
      this.jump((long)(this.get("Export.AddressOfFunctions") - var2 + var1));
      var3 = new LinkedList();

      for(var4 = 0; var4 < this.get("Export.NumberOfNames"); ++var4) {
         var3.add(new Long(this.fixAddress(this.readLong())));
      }

      this.put("Export.FunctionAddressesFixed", (List)var3);
      this.complete();
   }

   public long fixAddress(long var1) {
      Iterator var3 = this.SectionsTable().iterator();

      String var4;
      do {
         if (!var3.hasNext()) {
            return -1L;
         }

         var4 = var3.next() + "";
      } while(!this.inSection(var4, var1));

      return var1 - (long)this.sectionAddress(var4) + (long)this.sectionStart(var4);
   }

   public static PEParser load(InputStream var0) {
      return new PEParser(var0);
   }

   public static PEParser load(byte[] var0) {
      return new PEParser(var0);
   }

   protected PEParser(InputStream var1) {
      this(CommonUtils.readAll(var1));
   }

   protected void jump(long var1) throws IOException {
      this.frames.push(this.content);
      this.content = new DataInputStream(new ByteArrayInputStream(this.original));
      if (var1 > 0L) {
         this.consume((int)var1);
      }

   }

   protected void complete() throws IOException {
      this.content.close();
      this.content = (DataInputStream)this.frames.pop();
   }

   public long checksum() {
      PEImageChecksum var1 = new PEImageChecksum((long)this.getLocation("CheckSum"));
      var1.update(this.original, 0, this.original.length);
      return var1.getValue();
   }

   protected PEParser(byte[] var1) {
      this.content = null;
      this.bdata = new byte[8];
      this.buffer = null;
      this.values = new HashMap();
      this.frames = new Stack();
      this.locations = new HashMap();
      this.strings = new LinkedList();
      this.procassembly = false;
      this.original = var1;
      this.buffer = ByteBuffer.wrap(this.bdata);
      this.buffer.order(ByteOrder.LITTLE_ENDIAN);
      this.content = new DataInputStream(new ByteArrayInputStream(var1));

      try {
         this.parse();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   protected void consume(int var1) throws IOException {
      this.content.skipBytes(var1);
   }

   protected int readInt() throws IOException {
      this.buffer.clear();
      this.content.read(this.bdata, 0, 4);
      return (int)this.buffer.getLong(0);
   }

   protected long readLong() throws IOException {
      this.buffer.clear();
      this.content.read(this.bdata, 0, 4);
      return this.buffer.getLong(0);
   }

   protected long readQWord() throws IOException {
      this.buffer.clear();
      this.content.read(this.bdata, 0, 8);
      return this.buffer.getLong(0);
   }

   protected char readChar() throws IOException {
      return (char)this.content.readByte();
   }

   protected char readChar(DataInputStream var1) throws IOException {
      return (char)var1.readByte();
   }

   protected int readShort() throws IOException {
      this.content.read(this.bdata, 0, 2);
      return this.buffer.getShort(0) & '\uffff';
   }

   protected String readString() throws IOException {
      this.string();
      StringBuffer var1 = new StringBuffer();

      while(true) {
         char var2 = this.readChar();
         if (var2 <= 0) {
            if (var1.toString().startsWith("_ReflectiveLoader") || var1.toString().startsWith("ReflectiveLoader")) {
               this.strings.removeLast();
            }

            if (var1.toString().contains("CorExeMain")) {
               this.procassembly = true;
            }

            return var1.toString();
         }

         var1.append(var2);
      }
   }

   protected String readString(int var1) throws IOException {
      this.string();
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1; ++var3) {
         char var4 = this.readChar();
         if (var4 > 0) {
            var2.append(var4);
         }
      }

      if (var2.toString().startsWith("_ReflectiveLoader") || var2.toString().startsWith("ReflectiveLoader")) {
         this.strings.removeLast();
      }

      if (var2.toString().contains("CorExeMain")) {
         this.procassembly = true;
      }

      return var2.toString();
   }

   protected void put(String var1, long var2) {
      this.values.put(var1, new Long(var2));
   }

   protected void put(String var1, List var2) {
      this.values.put(var1, var2);
   }

   protected void put(String var1, String var2) {
      this.values.put(var1, var2);
   }

   protected void put(String var1, Date var2) {
      this.values.put(var1, var2);
   }

   protected void error(String var1) {
      throw new RuntimeException(var1);
   }

   protected void header(String var1, int var2) throws Exception {
      this.report("header." + var1);
      int var3 = this.readShort();
      if (var3 != var2) {
         this.error("Header " + var1 + " Magic Failed: " + var3 + " expected (" + var2 + ")");
      }

   }

   public int get(String var1) {
      Long var2 = (Long)this.values.get(var1);
      return var2 == null ? 0 : (int)var2;
   }

   public Date getDate(String var1) {
      return (Date)this.values.get(var1);
   }

   public String getString(String var1) {
      return (String)this.values.get(var1);
   }

   public String getDLLName() {
      return this.getString("Export.Name");
   }

   protected void put(String var1, String var2, long var3) {
      this.values.put(var1 + "." + var2, new Long(var3));
   }

   protected void put(String var1, String var2, String var3) {
      this.values.put(var1 + "." + var2, new Long(var3));
   }

   protected void readCharacteristics(String var1) throws IOException {
      long var2 = this.readLong();
      LinkedList var4 = new LinkedList();
      if ((var2 & 32L) == 32L) {
         var4.add("Code");
      }

      if ((var2 & 64L) == 64L) {
         var4.add("Initialized Data");
      }

      if ((var2 & 128L) == 128L) {
         var4.add("Uninitialized Data");
      }

      if ((var2 & 67108864L) == 67108864L) {
         var4.add("Section cannot be cached");
      }

      if ((var2 & 134217728L) == 134217728L) {
         var4.add("Section is not pageable");
      }

      if ((var2 & 268435456L) == 268435456L) {
         var4.add("Section is shared");
      }

      if ((var2 & 536870912L) == 536870912L) {
         var4.add("Executable");
      }

      if ((var2 & 1073741824L) == 1073741824L) {
         var4.add("Readable");
      }

      if ((var2 & 2147483648L) == 2147483648L) {
         var4.add("Writable");
      }

      var4.add("0x" + Long.toString(var2, 16));
      this.values.put(var1 + ".Characteristics", var4);
   }

   protected Date readDate() throws IOException {
      return new Date(this.readLong() * 1000L);
   }

   public boolean hasSection(String var1) {
      HashSet var2 = new HashSet(this.SectionsTable());
      return var2.contains(var1);
   }

   protected void parseSection(int var1) throws Exception {
      this.report("Sections.AddressOfName." + var1);
      String var2 = this.readString(8);
      this.append("SectionsTable", var2);
      this.put(var2, "VirtualSize", (long)this.readInt());
      this.put(var2, "VirtualAddress", (long)this.readInt());
      this.put(var2, "SizeOfRawData", (long)this.readInt());
      this.put(var2, "PointerToRawData", (long)this.readInt());
      this.put(var2, "PointerToRelocations", (long)this.readInt());
      this.put(var2, "PointerToLinenumbers", (long)this.readInt());
      this.report(var2 + ".NumberOfRelocations");
      this.put(var2, "NumberOfRelocations", (long)this.readShort());
      this.put(var2, "NumberOfLinenumbers", (long)this.readShort());
      this.readCharacteristics(var2);
   }

   protected void append(String var1, String var2) {
      if (this.values.get(var1) == null) {
         this.values.put(var1, new LinkedList());
      }

      LinkedList var3 = (LinkedList)this.values.get(var1);
      var3.add(var2);
   }

   public int sectionStart(String var1) {
      return this.get(var1 + ".PointerToRawData");
   }

   public int sectionSize(String var1) {
      return this.get(var1 + ".SizeOfRawData");
   }

   public int sectionAddress(String var1) {
      return this.get(var1 + ".VirtualAddress");
   }

   public int sectionEnd(String var1) {
      return this.get(var1 + ".VirtualAddress") + this.get(var1 + ".VirtualSize");
   }

   protected boolean inSection(String var1, long var2) {
      long var4 = (long)this.sectionAddress(var1);
      long var6 = (long)this.get(var1 + ".VirtualSize");
      return var2 >= var4 && var2 < var4 + var6;
   }

   public List SectionsTable() {
      return (List)this.values.get("SectionsTable");
   }

   protected int dirEntry(int var1) {
      int var2 = this.get("DataDirectory." + var1 + ".VirtualAddress");
      Iterator var3 = this.SectionsTable().iterator();

      String var4;
      int var5;
      int var6;
      do {
         if (!var3.hasNext()) {
            throw new RuntimeException("Directory entry: " + var1 + "@" + var2 + " not found");
         }

         var4 = (String)var3.next();
         var5 = this.sectionAddress(var4);
         var6 = this.sectionSize(var4);
      } while(var2 < var5 || var2 >= var5 + var6);

      return this.sectionStart(var4) + (var2 - var5);
   }

   public boolean is64() {
      return this.get("Machine") == 34404;
   }

   protected void parse64() throws Exception {
      this.header("Optional", 523);
      this.consume(14);
      this.report("AddressOfEntryPoint");
      this.put("AddressOfEntryPoint", (long)this.readInt());
      this.consume(4);
      this.put("ImageBase", this.readQWord());
      this.report("SectionAlignment");
      this.put("SectionAlignment", (long)this.readInt());
      this.put("FileAlignment", (long)this.readInt());
      this.consume(8);
      this.put("MajorSubSystemVersion", (long)this.readShort());
      this.consume(6);
      this.report("SizeOfImage");
      this.put("SizeOfImage", (long)this.readInt());
      this.put("SizeOfHeaders", (long)this.readInt());
      this.report("CheckSum");
      this.put("CheckSum", (long)this.readInt());
      this.put("Subsystem", (long)this.readShort());
      this.put("DllCharacteristics", (long)this.readShort());
      this.consume(32);
      this.report("LoaderFlags");
      this.put("LoaderFlags", (long)this.readInt());
      this.put("NumberOfRvaAndSizes", (long)this.readInt());
   }

   protected void parse32() throws Exception {
      this.header("Optional", 267);
      this.consume(14);
      this.report("AddressOfEntryPoint");
      this.put("AddressOfEntryPoint", (long)this.readInt());
      this.consume(8);
      this.put("ImageBase", (long)this.readInt());
      this.report("SectionAlignment");
      this.put("SectionAlignment", (long)this.readInt());
      this.put("FileAlignment", (long)this.readInt());
      this.consume(8);
      this.put("MajorSubSystemVersion", (long)this.readShort());
      this.consume(6);
      this.report("SizeOfImage");
      this.put("SizeOfImage", (long)this.readInt());
      this.put("SizeOfHeaders", (long)this.readInt());
      this.report("CheckSum");
      this.put("CheckSum", (long)this.readInt());
      this.put("Subsystem", (long)this.readShort());
      this.put("DllCharacteristics", (long)this.readShort());
      this.consume(16);
      this.report("LoaderFlags");
      this.put("LoaderFlags", (long)this.readInt());
      this.put("NumberOfRvaAndSizes", (long)this.readInt());
   }

   public int here() throws IOException {
      return this.original.length - this.content.available();
   }

   public void string() {
      try {
         int var1 = this.here();
         this.strings.add(new Integer(var1));
      } catch (Exception var2) {
         MudgeSanity.logException("string", var2, false);
      }

   }

   public void report(String var1) {
      try {
         int var2 = this.here();
         this.locations.put(var1, new Integer(var2));
      } catch (Exception var3) {
         MudgeSanity.logException("report: " + var1, var3, false);
      }

   }

   public Iterator stringIterator() {
      return this.strings.iterator();
   }

   public int getLocation(String var1) {
      if (!this.locations.containsKey(var1)) {
         throw new IllegalArgumentException("No location for '" + var1 + "'");
      } else {
         int var2 = (Integer)this.locations.get(var1);
         AssertUtils.Test(var2 >= 60, var1 + " (offset: " + var2 + ") Reflective Loader bootstrap region");
         return var2;
      }
   }

   public int getRichHeaderSize() {
      return this.get("e_lfanew") - 128;
   }

   public byte[] getRichHeader() {
      return this.getRichHeaderSize() <= 0 ? new byte[0] : Arrays.copyOfRange(this.original, 128, this.get("e_lfanew"));
   }

   protected void parse() throws Exception {
      this.header("e_magic", 23117);
      this.consume(58);
      this.report("e_lfanew");
      this.put("e_lfanew", (long)this.readInt());
      this.jump((long)this.get("e_lfanew"));
      this.header("PE", 17744);
      this.consume(2);
      this.put("Machine", (long)this.readShort());
      this.put("Sections", (long)this.readShort());
      this.report("TimeDateStamp");
      this.put("TimeDateStamp", this.readDate());
      this.put("PointerToSymbolTable", (long)this.readInt());
      this.report("NumberOfSymbols");
      this.put("NumberOfSymbols", (long)this.readInt());
      this.put("SizeOfOptionalHeader", (long)this.readShort());
      this.report("Characteristics");
      this.put("Characteristics", (long)this.readShort());
      if (this.is64()) {
         this.parse64();
      } else {
         this.parse32();
      }

      int var1;
      for(var1 = 0; var1 < this.get("NumberOfRvaAndSizes"); ++var1) {
         this.parseDirectory(var1);
      }

      for(var1 = 0; var1 < this.get("Sections"); ++var1) {
         this.parseSection(var1);
      }

      this.report("HeaderSlack");
      this.complete();
      if (this.get("DataDirectory.1.VirtualAddress") != 0) {
         this.jump((long)this.dirEntry(1));

         for(var1 = 0; this.parseImport(var1); ++var1) {
         }

         this.complete();
      }

      if (this.get("DataDirectory.0.VirtualAddress") != 0) {
         this.jump((long)this.dirEntry(0));
         this.parseExport();
         this.complete();
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Key                                Value\n");
      var1.append("---                                -----\n");
      Iterator var2 = (new TreeMap(this.values)).entrySet().iterator();

      while(true) {
         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();

            String var4;
            for(var4 = (String)var3.getKey(); var4.length() < 35; var4 = var4 + " ") {
            }

            var1.append(var4);
            if (var3.getValue() instanceof Long) {
               Long var9 = (Long)var3.getValue();
               long var6 = var9;

               String var8;
               for(var8 = "0x" + Long.toString(var6, 16); var8.length() < 12; var8 = var8 + " ") {
               }

               var8 = var8 + var6;
               var1.append(var8);
               var1.append("\n");
            } else if (var3.getValue() instanceof String) {
               var1.append(var3.getValue() + "\n");
            } else if (var3.getValue() instanceof List) {
               var1.append(var3.getValue() + "\n");
            } else if (var3.getValue() instanceof Date) {
               long var5 = ((Date)var3.getValue()).getTime() / 1000L;

               String var7;
               for(var7 = "0x" + Long.toString(var5, 16); var7.length() < 12; var7 = var7 + " ") {
               }

               for(var7 = var7 + var5; var7.length() < 32; var7 = var7 + " ") {
               }

               var7 = var7 + CommonUtils.formatDateAny("dd MMM yyyy HH:mm:ss", var5 * 1000L);
               var1.append(var7);
               var1.append("\n");
            }
         }

         return var1.toString();
      }
   }

   public String getStringAt(int var1) {
      StringBuffer var2;
      for(var2 = new StringBuffer(); this.original[var1] != 0; ++var1) {
         var2.append((char)this.original[var1]);
      }

      return var2.toString();
   }

   public void stringWalk() {
      Iterator var1 = this.stringIterator();

      while(var1.hasNext()) {
         int var2 = (Integer)var1.next();
         CommonUtils.print_stat("[" + var2 + "] " + this.getStringAt(var2));
      }

   }

   public static void dump(String[] var0) throws Exception {
      File var1 = new File(var0[1]);
      PEParser var2 = load((InputStream)(new FileInputStream(var1)));
      System.out.println(var2.toString());
      System.out.println("Checksum: " + var2.checksum());
      System.out.println("\n\nLocations:\n----------");
      Iterator var3 = var2.locations.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();

         String var5;
         for(var5 = (String)var4.getKey(); var5.length() < 31; var5 = var5 + " ") {
         }

         System.out.println(var5 + " " + var4.getValue());
      }

   }

   public static void stage(String[] var0) throws Exception {
      PEClone var1 = new PEClone();
      var1.start(var0[0]);
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         CommonUtils.print_info("Cobalt Strike PE Parser. Options:\n\t./peclone [file]\n\t\tDump PE headers as a Malleable PE stage block\n\t./peclone dump [file]\n\t\tRun Cobalt Strike's PE parser against the file");
      } else if (var0[0].equals("dump") && var0.length == 2) {
         dump(var0);
      } else {
         stage(var0);
      }

   }
}
