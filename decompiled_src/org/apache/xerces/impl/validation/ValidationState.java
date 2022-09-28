package org.apache.xerces.impl.validation;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.NamespaceContext;

public class ValidationState implements ValidationContext {
   private boolean fExtraChecking = true;
   private boolean fFacetChecking = true;
   private boolean fNormalize = true;
   private boolean fNamespaces = true;
   private EntityState fEntityState = null;
   private NamespaceContext fNamespaceContext = null;
   private SymbolTable fSymbolTable = null;
   private final Hashtable fIdTable = new Hashtable();
   private final Hashtable fIdRefTable = new Hashtable();
   private static final Object fNullValue = new Object();

   public void setExtraChecking(boolean var1) {
      this.fExtraChecking = var1;
   }

   public void setFacetChecking(boolean var1) {
      this.fFacetChecking = var1;
   }

   public void setNormalizationRequired(boolean var1) {
      this.fNormalize = var1;
   }

   public void setUsingNamespaces(boolean var1) {
      this.fNamespaces = var1;
   }

   public void setEntityState(EntityState var1) {
      this.fEntityState = var1;
   }

   public void setNamespaceSupport(NamespaceContext var1) {
      this.fNamespaceContext = var1;
   }

   public void setSymbolTable(SymbolTable var1) {
      this.fSymbolTable = var1;
   }

   public String checkIDRefID() {
      Enumeration var1 = this.fIdRefTable.keys();

      while(var1.hasMoreElements()) {
         String var2 = (String)var1.nextElement();
         if (!this.fIdTable.containsKey(var2)) {
            return var2;
         }
      }

      return null;
   }

   public void reset() {
      this.fExtraChecking = true;
      this.fFacetChecking = true;
      this.fNamespaces = true;
      this.fIdTable.clear();
      this.fIdRefTable.clear();
      this.fEntityState = null;
      this.fNamespaceContext = null;
      this.fSymbolTable = null;
   }

   public void resetIDTables() {
      this.fIdTable.clear();
      this.fIdRefTable.clear();
   }

   public boolean needExtraChecking() {
      return this.fExtraChecking;
   }

   public boolean needFacetChecking() {
      return this.fFacetChecking;
   }

   public boolean needToNormalize() {
      return this.fNormalize;
   }

   public boolean useNamespaces() {
      return this.fNamespaces;
   }

   public boolean isEntityDeclared(String var1) {
      return this.fEntityState != null ? this.fEntityState.isEntityDeclared(this.getSymbol(var1)) : false;
   }

   public boolean isEntityUnparsed(String var1) {
      return this.fEntityState != null ? this.fEntityState.isEntityUnparsed(this.getSymbol(var1)) : false;
   }

   public boolean isIdDeclared(String var1) {
      return this.fIdTable.containsKey(var1);
   }

   public void addId(String var1) {
      this.fIdTable.put(var1, fNullValue);
   }

   public void addIdRef(String var1) {
      this.fIdRefTable.put(var1, fNullValue);
   }

   public String getSymbol(String var1) {
      return this.fSymbolTable != null ? this.fSymbolTable.addSymbol(var1) : var1.intern();
   }

   public String getURI(String var1) {
      return this.fNamespaceContext != null ? this.fNamespaceContext.getURI(var1) : null;
   }
}
