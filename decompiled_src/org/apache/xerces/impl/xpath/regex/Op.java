package org.apache.xerces.impl.xpath.regex;

import java.util.Vector;

class Op {
   static final int DOT = 0;
   static final int CHAR = 1;
   static final int RANGE = 3;
   static final int NRANGE = 4;
   static final int ANCHOR = 5;
   static final int STRING = 6;
   static final int CLOSURE = 7;
   static final int NONGREEDYCLOSURE = 8;
   static final int QUESTION = 9;
   static final int NONGREEDYQUESTION = 10;
   static final int UNION = 11;
   static final int CAPTURE = 15;
   static final int BACKREFERENCE = 16;
   static final int LOOKAHEAD = 20;
   static final int NEGATIVELOOKAHEAD = 21;
   static final int LOOKBEHIND = 22;
   static final int NEGATIVELOOKBEHIND = 23;
   static final int INDEPENDENT = 24;
   static final int MODIFIER = 25;
   static final int CONDITION = 26;
   static int nofinstances = 0;
   static final boolean COUNT = false;
   int type;
   Op next = null;

   static Op createDot() {
      return new Op(0);
   }

   static CharOp createChar(int var0) {
      return new CharOp(1, var0);
   }

   static CharOp createAnchor(int var0) {
      return new CharOp(5, var0);
   }

   static CharOp createCapture(int var0, Op var1) {
      CharOp var2 = new CharOp(15, var0);
      var2.next = var1;
      return var2;
   }

   static UnionOp createUnion(int var0) {
      return new UnionOp(11, var0);
   }

   static ChildOp createClosure(int var0) {
      return new ModifierOp(7, var0, -1);
   }

   static ChildOp createNonGreedyClosure() {
      return new ChildOp(8);
   }

   static ChildOp createQuestion(boolean var0) {
      return new ChildOp(var0 ? 10 : 9);
   }

   static RangeOp createRange(Token var0) {
      return new RangeOp(3, var0);
   }

   static ChildOp createLook(int var0, Op var1, Op var2) {
      ChildOp var3 = new ChildOp(var0);
      var3.setChild(var2);
      var3.next = var1;
      return var3;
   }

   static CharOp createBackReference(int var0) {
      return new CharOp(16, var0);
   }

   static StringOp createString(String var0) {
      return new StringOp(6, var0);
   }

   static ChildOp createIndependent(Op var0, Op var1) {
      ChildOp var2 = new ChildOp(24);
      var2.setChild(var1);
      var2.next = var0;
      return var2;
   }

   static ModifierOp createModifier(Op var0, Op var1, int var2, int var3) {
      ModifierOp var4 = new ModifierOp(25, var2, var3);
      var4.setChild(var1);
      var4.next = var0;
      return var4;
   }

   static ConditionOp createCondition(Op var0, int var1, Op var2, Op var3, Op var4) {
      ConditionOp var5 = new ConditionOp(26, var1, var2, var3, var4);
      var5.next = var0;
      return var5;
   }

   protected Op(int var1) {
      this.type = var1;
   }

   int size() {
      return 0;
   }

   Op elementAt(int var1) {
      throw new RuntimeException("Internal Error: type=" + this.type);
   }

   Op getChild() {
      throw new RuntimeException("Internal Error: type=" + this.type);
   }

   int getData() {
      throw new RuntimeException("Internal Error: type=" + this.type);
   }

   int getData2() {
      throw new RuntimeException("Internal Error: type=" + this.type);
   }

   RangeToken getToken() {
      throw new RuntimeException("Internal Error: type=" + this.type);
   }

   String getString() {
      throw new RuntimeException("Internal Error: type=" + this.type);
   }

   static class ConditionOp extends Op {
      int refNumber;
      Op condition;
      Op yes;
      Op no;

      ConditionOp(int var1, int var2, Op var3, Op var4, Op var5) {
         super(var1);
         this.refNumber = var2;
         this.condition = var3;
         this.yes = var4;
         this.no = var5;
      }
   }

   static class StringOp extends Op {
      String string;

      StringOp(int var1, String var2) {
         super(var1);
         this.string = var2;
      }

      String getString() {
         return this.string;
      }
   }

   static class RangeOp extends Op {
      Token tok;

      RangeOp(int var1, Token var2) {
         super(var1);
         this.tok = var2;
      }

      RangeToken getToken() {
         return (RangeToken)this.tok;
      }
   }

   static class ModifierOp extends ChildOp {
      int v1;
      int v2;

      ModifierOp(int var1, int var2, int var3) {
         super(var1);
         this.v1 = var2;
         this.v2 = var3;
      }

      int getData() {
         return this.v1;
      }

      int getData2() {
         return this.v2;
      }
   }

   static class ChildOp extends Op {
      Op child;

      ChildOp(int var1) {
         super(var1);
      }

      void setChild(Op var1) {
         this.child = var1;
      }

      Op getChild() {
         return this.child;
      }
   }

   static class UnionOp extends Op {
      Vector branches;

      UnionOp(int var1, int var2) {
         super(var1);
         this.branches = new Vector(var2);
      }

      void addElement(Op var1) {
         this.branches.addElement(var1);
      }

      int size() {
         return this.branches.size();
      }

      Op elementAt(int var1) {
         return (Op)this.branches.elementAt(var1);
      }
   }

   static class CharOp extends Op {
      int charData;

      CharOp(int var1, int var2) {
         super(var1);
         this.charData = var2;
      }

      int getData() {
         return this.charData;
      }
   }
}
