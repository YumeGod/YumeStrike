package org.apache.batik.gvt.text;

import java.text.AttributedCharacterIterator;
import java.util.Map;
import java.util.Set;

public class AttributedCharacterSpanIterator implements AttributedCharacterIterator {
   private AttributedCharacterIterator aci;
   private int begin;
   private int end;

   public AttributedCharacterSpanIterator(AttributedCharacterIterator var1, int var2, int var3) {
      this.aci = var1;
      this.end = Math.min(var1.getEndIndex(), var3);
      this.begin = Math.max(var1.getBeginIndex(), var2);
      this.aci.setIndex(this.begin);
   }

   public Set getAllAttributeKeys() {
      return this.aci.getAllAttributeKeys();
   }

   public Object getAttribute(AttributedCharacterIterator.Attribute var1) {
      return this.aci.getAttribute(var1);
   }

   public Map getAttributes() {
      return this.aci.getAttributes();
   }

   public int getRunLimit() {
      return Math.min(this.aci.getRunLimit(), this.end);
   }

   public int getRunLimit(AttributedCharacterIterator.Attribute var1) {
      return Math.min(this.aci.getRunLimit(var1), this.end);
   }

   public int getRunLimit(Set var1) {
      return Math.min(this.aci.getRunLimit(var1), this.end);
   }

   public int getRunStart() {
      return Math.max(this.aci.getRunStart(), this.begin);
   }

   public int getRunStart(AttributedCharacterIterator.Attribute var1) {
      return Math.max(this.aci.getRunStart(var1), this.begin);
   }

   public int getRunStart(Set var1) {
      return Math.max(this.aci.getRunStart(var1), this.begin);
   }

   public Object clone() {
      return new AttributedCharacterSpanIterator((AttributedCharacterIterator)this.aci.clone(), this.begin, this.end);
   }

   public char current() {
      return this.aci.current();
   }

   public char first() {
      return this.aci.setIndex(this.begin);
   }

   public int getBeginIndex() {
      return this.begin;
   }

   public int getEndIndex() {
      return this.end;
   }

   public int getIndex() {
      return this.aci.getIndex();
   }

   public char last() {
      return this.setIndex(this.end - 1);
   }

   public char next() {
      return this.getIndex() < this.end - 1 ? this.aci.next() : this.setIndex(this.end);
   }

   public char previous() {
      return this.getIndex() > this.begin ? this.aci.previous() : '\uffff';
   }

   public char setIndex(int var1) {
      int var2 = Math.max(var1, this.begin);
      var2 = Math.min(var2, this.end);
      char var3 = this.aci.setIndex(var2);
      if (var2 == this.end) {
         var3 = '\uffff';
      }

      return var3;
   }
}
