package org.apache.xerces.util;

public final class ShadowedSymbolTable extends SymbolTable {
   protected SymbolTable fSymbolTable;

   public ShadowedSymbolTable(SymbolTable var1) {
      this.fSymbolTable = var1;
   }

   public String addSymbol(String var1) {
      return this.fSymbolTable.containsSymbol(var1) ? this.fSymbolTable.addSymbol(var1) : super.addSymbol(var1);
   }

   public String addSymbol(char[] var1, int var2, int var3) {
      return this.fSymbolTable.containsSymbol(var1, var2, var3) ? this.fSymbolTable.addSymbol(var1, var2, var3) : super.addSymbol(var1, var2, var3);
   }

   public int hash(String var1) {
      return this.fSymbolTable.hash(var1);
   }

   public int hash(char[] var1, int var2, int var3) {
      return this.fSymbolTable.hash(var1, var2, var3);
   }
}
