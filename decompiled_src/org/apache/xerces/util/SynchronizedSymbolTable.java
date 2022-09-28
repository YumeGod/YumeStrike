package org.apache.xerces.util;

public final class SynchronizedSymbolTable extends SymbolTable {
   protected SymbolTable fSymbolTable;

   public SynchronizedSymbolTable(SymbolTable var1) {
      this.fSymbolTable = var1;
   }

   public SynchronizedSymbolTable() {
      this.fSymbolTable = new SymbolTable();
   }

   public SynchronizedSymbolTable(int var1) {
      this.fSymbolTable = new SymbolTable(var1);
   }

   public String addSymbol(String var1) {
      SymbolTable var2 = this.fSymbolTable;
      synchronized(var2) {
         String var3 = this.fSymbolTable.addSymbol(var1);
         return var3;
      }
   }

   public String addSymbol(char[] var1, int var2, int var3) {
      SymbolTable var4 = this.fSymbolTable;
      synchronized(var4) {
         String var5 = this.fSymbolTable.addSymbol(var1, var2, var3);
         return var5;
      }
   }

   public boolean containsSymbol(String var1) {
      SymbolTable var2 = this.fSymbolTable;
      synchronized(var2) {
         boolean var3 = this.fSymbolTable.containsSymbol(var1);
         return var3;
      }
   }

   public boolean containsSymbol(char[] var1, int var2, int var3) {
      SymbolTable var4 = this.fSymbolTable;
      synchronized(var4) {
         boolean var5 = this.fSymbolTable.containsSymbol(var1, var2, var3);
         return var5;
      }
   }
}
