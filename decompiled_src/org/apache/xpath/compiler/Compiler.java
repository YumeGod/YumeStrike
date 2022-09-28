package org.apache.xpath.compiler;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xpath.Expression;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.UnionPathIterator;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.functions.FuncExtFunctionAvailable;
import org.apache.xpath.functions.Function;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XString;
import org.apache.xpath.operations.And;
import org.apache.xpath.operations.Bool;
import org.apache.xpath.operations.Div;
import org.apache.xpath.operations.Equals;
import org.apache.xpath.operations.Gt;
import org.apache.xpath.operations.Gte;
import org.apache.xpath.operations.Lt;
import org.apache.xpath.operations.Lte;
import org.apache.xpath.operations.Minus;
import org.apache.xpath.operations.Mod;
import org.apache.xpath.operations.Mult;
import org.apache.xpath.operations.Neg;
import org.apache.xpath.operations.NotEquals;
import org.apache.xpath.operations.Number;
import org.apache.xpath.operations.Operation;
import org.apache.xpath.operations.Or;
import org.apache.xpath.operations.Plus;
import org.apache.xpath.operations.String;
import org.apache.xpath.operations.UnaryOperation;
import org.apache.xpath.operations.Variable;
import org.apache.xpath.patterns.FunctionPattern;
import org.apache.xpath.patterns.StepPattern;
import org.apache.xpath.patterns.UnionPattern;
import org.apache.xpath.res.XPATHMessages;

public class Compiler extends OpMap {
   private int locPathDepth = -1;
   private static final boolean DEBUG = false;
   private static long s_nextMethodId = 0L;
   private PrefixResolver m_currentPrefixResolver = null;
   ErrorListener m_errorHandler;
   SourceLocator m_locator;
   private FunctionTable m_functionTable;

   public Compiler(ErrorListener errorHandler, SourceLocator locator, FunctionTable fTable) {
      this.m_errorHandler = errorHandler;
      this.m_locator = locator;
      this.m_functionTable = fTable;
   }

   public Compiler() {
      this.m_errorHandler = null;
      this.m_locator = null;
   }

   public Expression compile(int opPos) throws TransformerException {
      int op = this.getOp(opPos);
      Expression expr = null;
      switch (op) {
         case 1:
            expr = this.compile(opPos + 2);
            break;
         case 2:
            expr = this.or(opPos);
            break;
         case 3:
            expr = this.and(opPos);
            break;
         case 4:
            expr = this.notequals(opPos);
            break;
         case 5:
            expr = this.equals(opPos);
            break;
         case 6:
            expr = this.lte(opPos);
            break;
         case 7:
            expr = this.lt(opPos);
            break;
         case 8:
            expr = this.gte(opPos);
            break;
         case 9:
            expr = this.gt(opPos);
            break;
         case 10:
            expr = this.plus(opPos);
            break;
         case 11:
            expr = this.minus(opPos);
            break;
         case 12:
            expr = this.mult(opPos);
            break;
         case 13:
            expr = this.div(opPos);
            break;
         case 14:
            expr = this.mod(opPos);
            break;
         case 15:
            this.error("ER_UNKNOWN_OPCODE", new Object[]{"quo"});
            break;
         case 16:
            expr = this.neg(opPos);
            break;
         case 17:
            expr = this.string(opPos);
            break;
         case 18:
            expr = this.bool(opPos);
            break;
         case 19:
            expr = this.number(opPos);
            break;
         case 20:
            expr = this.union(opPos);
            break;
         case 21:
            expr = this.literal(opPos);
            break;
         case 22:
            expr = this.variable(opPos);
            break;
         case 23:
            expr = this.group(opPos);
            break;
         case 24:
            expr = this.compileExtension(opPos);
            break;
         case 25:
            expr = this.compileFunction(opPos);
            break;
         case 26:
            expr = this.arg(opPos);
            break;
         case 27:
            expr = this.numberlit(opPos);
            break;
         case 28:
            expr = this.locationPath(opPos);
            break;
         case 29:
            expr = null;
            break;
         case 30:
            expr = this.matchPattern(opPos + 2);
            break;
         case 31:
            expr = this.locationPathPattern(opPos);
            break;
         default:
            this.error("ER_UNKNOWN_OPCODE", new Object[]{Integer.toString(this.getOp(opPos))});
      }

      return expr;
   }

   private Expression compileOperation(Operation operation, int opPos) throws TransformerException {
      int leftPos = OpMap.getFirstChildPos(opPos);
      int rightPos = this.getNextOpPos(leftPos);
      operation.setLeftRight(this.compile(leftPos), this.compile(rightPos));
      return operation;
   }

   private Expression compileUnary(UnaryOperation unary, int opPos) throws TransformerException {
      int rightPos = OpMap.getFirstChildPos(opPos);
      unary.setRight(this.compile(rightPos));
      return unary;
   }

   protected Expression or(int opPos) throws TransformerException {
      return this.compileOperation(new Or(), opPos);
   }

   protected Expression and(int opPos) throws TransformerException {
      return this.compileOperation(new And(), opPos);
   }

   protected Expression notequals(int opPos) throws TransformerException {
      return this.compileOperation(new NotEquals(), opPos);
   }

   protected Expression equals(int opPos) throws TransformerException {
      return this.compileOperation(new Equals(), opPos);
   }

   protected Expression lte(int opPos) throws TransformerException {
      return this.compileOperation(new Lte(), opPos);
   }

   protected Expression lt(int opPos) throws TransformerException {
      return this.compileOperation(new Lt(), opPos);
   }

   protected Expression gte(int opPos) throws TransformerException {
      return this.compileOperation(new Gte(), opPos);
   }

   protected Expression gt(int opPos) throws TransformerException {
      return this.compileOperation(new Gt(), opPos);
   }

   protected Expression plus(int opPos) throws TransformerException {
      return this.compileOperation(new Plus(), opPos);
   }

   protected Expression minus(int opPos) throws TransformerException {
      return this.compileOperation(new Minus(), opPos);
   }

   protected Expression mult(int opPos) throws TransformerException {
      return this.compileOperation(new Mult(), opPos);
   }

   protected Expression div(int opPos) throws TransformerException {
      return this.compileOperation(new Div(), opPos);
   }

   protected Expression mod(int opPos) throws TransformerException {
      return this.compileOperation(new Mod(), opPos);
   }

   protected Expression neg(int opPos) throws TransformerException {
      return this.compileUnary(new Neg(), opPos);
   }

   protected Expression string(int opPos) throws TransformerException {
      return this.compileUnary(new String(), opPos);
   }

   protected Expression bool(int opPos) throws TransformerException {
      return this.compileUnary(new Bool(), opPos);
   }

   protected Expression number(int opPos) throws TransformerException {
      return this.compileUnary(new Number(), opPos);
   }

   protected Expression literal(int opPos) {
      opPos = OpMap.getFirstChildPos(opPos);
      return (XString)this.getTokenQueue().elementAt(this.getOp(opPos));
   }

   protected Expression numberlit(int opPos) {
      opPos = OpMap.getFirstChildPos(opPos);
      return (XNumber)this.getTokenQueue().elementAt(this.getOp(opPos));
   }

   protected Expression variable(int opPos) throws TransformerException {
      Variable var = new Variable();
      opPos = OpMap.getFirstChildPos(opPos);
      int nsPos = this.getOp(opPos);
      java.lang.String namespace = -2 == nsPos ? null : (java.lang.String)this.getTokenQueue().elementAt(nsPos);
      java.lang.String localname = (java.lang.String)this.getTokenQueue().elementAt(this.getOp(opPos + 1));
      QName qname = new QName(namespace, localname);
      var.setQName(qname);
      return var;
   }

   protected Expression group(int opPos) throws TransformerException {
      return this.compile(opPos + 2);
   }

   protected Expression arg(int opPos) throws TransformerException {
      return this.compile(opPos + 2);
   }

   protected Expression union(int opPos) throws TransformerException {
      ++this.locPathDepth;

      LocPathIterator var2;
      try {
         var2 = UnionPathIterator.createUnionIterator(this, opPos);
      } finally {
         --this.locPathDepth;
      }

      return var2;
   }

   public int getLocationPathDepth() {
      return this.locPathDepth;
   }

   FunctionTable getFunctionTable() {
      return this.m_functionTable;
   }

   public Expression locationPath(int opPos) throws TransformerException {
      ++this.locPathDepth;

      Expression var3;
      try {
         DTMIterator iter = WalkerFactory.newDTMIterator(this, opPos, this.locPathDepth == 0);
         var3 = (Expression)iter;
      } finally {
         --this.locPathDepth;
      }

      return var3;
   }

   public Expression predicate(int opPos) throws TransformerException {
      return this.compile(opPos + 2);
   }

   protected Expression matchPattern(int opPos) throws TransformerException {
      ++this.locPathDepth;

      Expression var4;
      try {
         int nextOpPos = opPos;

         int i;
         for(i = 0; this.getOp(nextOpPos) == 31; ++i) {
            nextOpPos = this.getNextOpPos(nextOpPos);
         }

         if (i != 1) {
            UnionPattern up = new UnionPattern();
            StepPattern[] patterns = new StepPattern[i];

            for(i = 0; this.getOp(opPos) == 31; ++i) {
               nextOpPos = this.getNextOpPos(opPos);
               patterns[i] = (StepPattern)this.compile(opPos);
               opPos = nextOpPos;
            }

            up.setPatterns(patterns);
            UnionPattern var7 = up;
            return var7;
         }

         var4 = this.compile(opPos);
      } finally {
         --this.locPathDepth;
      }

      return var4;
   }

   public Expression locationPathPattern(int opPos) throws TransformerException {
      opPos = OpMap.getFirstChildPos(opPos);
      return this.stepPattern(opPos, 0, (StepPattern)null);
   }

   public int getWhatToShow(int opPos) {
      int axesType = this.getOp(opPos);
      int testType = this.getOp(opPos + 3);
      switch (testType) {
         case 34:
            switch (axesType) {
               case 39:
               case 51:
                  return 2;
               case 40:
               case 41:
               case 42:
               case 43:
               case 44:
               case 45:
               case 46:
               case 47:
               case 48:
               case 50:
               default:
                  return 1;
               case 49:
                  return 4096;
               case 52:
               case 53:
                  return 1;
            }
         case 35:
            return 1280;
         case 1030:
            return 128;
         case 1031:
            return 12;
         case 1032:
            return 64;
         case 1033:
            switch (axesType) {
               case 38:
               case 42:
               case 48:
                  return -1;
               case 39:
               case 51:
                  return 2;
               case 40:
               case 41:
               case 43:
               case 44:
               case 45:
               case 46:
               case 47:
               case 50:
               default:
                  if (this.getOp(0) == 30) {
                     return -1283;
                  }

                  return -3;
               case 49:
                  return 4096;
            }
         case 1034:
            return 65536;
         default:
            return -1;
      }
   }

   protected StepPattern stepPattern(int opPos, int stepCount, StepPattern ancestorPattern) throws TransformerException {
      int startOpPos = opPos;
      int stepType = this.getOp(opPos);
      if (-1 == stepType) {
         return null;
      } else {
         boolean addMagicSelf = true;
         int endStep = this.getNextOpPos(opPos);
         Object pattern;
         int argLen;
         switch (stepType) {
            case 25:
               addMagicSelf = false;
               argLen = this.getOp(opPos + 1);
               pattern = new FunctionPattern(this.compileFunction(opPos), 10, 3);
               break;
            case 50:
               addMagicSelf = false;
               argLen = this.getArgLengthOfStep(opPos);
               opPos = OpMap.getFirstChildPosOfStep(opPos);
               pattern = new StepPattern(1280, 10, 3);
               break;
            case 51:
               argLen = this.getArgLengthOfStep(opPos);
               opPos = OpMap.getFirstChildPosOfStep(opPos);
               pattern = new StepPattern(2, this.getStepNS(startOpPos), this.getStepLocalName(startOpPos), 10, 2);
               break;
            case 52:
               argLen = this.getArgLengthOfStep(opPos);
               opPos = OpMap.getFirstChildPosOfStep(opPos);
               int what = this.getWhatToShow(startOpPos);
               if (1280 == what) {
                  addMagicSelf = false;
               }

               pattern = new StepPattern(this.getWhatToShow(startOpPos), this.getStepNS(startOpPos), this.getStepLocalName(startOpPos), 0, 3);
               break;
            case 53:
               argLen = this.getArgLengthOfStep(opPos);
               opPos = OpMap.getFirstChildPosOfStep(opPos);
               pattern = new StepPattern(this.getWhatToShow(startOpPos), this.getStepNS(startOpPos), this.getStepLocalName(startOpPos), 10, 3);
               break;
            default:
               this.error("ER_UNKNOWN_MATCH_OPERATION", (Object[])null);
               return null;
         }

         ((StepPattern)pattern).setPredicates(this.getCompiledPredicates(opPos + argLen));
         if (null != ancestorPattern) {
            ((StepPattern)pattern).setRelativePathPattern(ancestorPattern);
         }

         StepPattern relativePathPattern = this.stepPattern(endStep, stepCount + 1, (StepPattern)pattern);
         return (StepPattern)(null != relativePathPattern ? relativePathPattern : pattern);
      }
   }

   public Expression[] getCompiledPredicates(int opPos) throws TransformerException {
      int count = this.countPredicates(opPos);
      if (count > 0) {
         Expression[] predicates = new Expression[count];
         this.compilePredicates(opPos, predicates);
         return predicates;
      } else {
         return null;
      }
   }

   public int countPredicates(int opPos) throws TransformerException {
      int count;
      for(count = 0; 29 == this.getOp(opPos); opPos = this.getNextOpPos(opPos)) {
         ++count;
      }

      return count;
   }

   private void compilePredicates(int opPos, Expression[] predicates) throws TransformerException {
      for(int i = 0; 29 == this.getOp(opPos); ++i) {
         predicates[i] = this.predicate(opPos);
         opPos = this.getNextOpPos(opPos);
      }

   }

   Expression compileFunction(int opPos) throws TransformerException {
      int endFunc = opPos + this.getOp(opPos + 1) - 1;
      opPos = OpMap.getFirstChildPos(opPos);
      int funcID = this.getOp(opPos);
      ++opPos;
      if (-1 == funcID) {
         this.error("ER_FUNCTION_TOKEN_NOT_FOUND", (Object[])null);
         return null;
      } else {
         Function func = this.m_functionTable.getFunction(funcID);
         if (func instanceof FuncExtFunctionAvailable) {
            ((FuncExtFunctionAvailable)func).setFunctionTable(this.m_functionTable);
         }

         func.postCompileStep(this);

         try {
            int i = 0;

            for(int p = opPos; p < endFunc; ++i) {
               func.setArg(this.compile(p), i);
               p = this.getNextOpPos(p);
            }

            func.checkNumberArgs(i);
         } catch (WrongNumberArgsException var7) {
            java.lang.String name = this.m_functionTable.getFunctionName(funcID);
            this.m_errorHandler.fatalError(new TransformerException(XPATHMessages.createXPATHMessage("ER_ONLY_ALLOWS", new Object[]{name, var7.getMessage()}), this.m_locator));
         }

         return func;
      }
   }

   private synchronized long getNextMethodId() {
      if (s_nextMethodId == Long.MAX_VALUE) {
         s_nextMethodId = 0L;
      }

      return (long)(s_nextMethodId++);
   }

   private Expression compileExtension(int opPos) throws TransformerException {
      int endExtFunc = opPos + this.getOp(opPos + 1) - 1;
      opPos = OpMap.getFirstChildPos(opPos);
      java.lang.String ns = (java.lang.String)this.getTokenQueue().elementAt(this.getOp(opPos));
      ++opPos;
      java.lang.String funcName = (java.lang.String)this.getTokenQueue().elementAt(this.getOp(opPos));
      ++opPos;
      Function extension = new FuncExtFunction(ns, funcName, java.lang.String.valueOf(this.getNextMethodId()));

      try {
         for(int i = 0; opPos < endExtFunc; ++i) {
            int nextOpPos = this.getNextOpPos(opPos);
            extension.setArg(this.compile(opPos), i);
            opPos = nextOpPos;
         }
      } catch (WrongNumberArgsException var8) {
      }

      return extension;
   }

   public void warn(java.lang.String msg, Object[] args) throws TransformerException {
      java.lang.String fmsg = XPATHMessages.createXPATHWarning(msg, args);
      if (null != this.m_errorHandler) {
         this.m_errorHandler.warning(new TransformerException(fmsg, this.m_locator));
      } else {
         System.out.println(fmsg + "; file " + this.m_locator.getSystemId() + "; line " + this.m_locator.getLineNumber() + "; column " + this.m_locator.getColumnNumber());
      }

   }

   public void assertion(boolean b, java.lang.String msg) {
      if (!b) {
         java.lang.String fMsg = XPATHMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{msg});
         throw new RuntimeException(fMsg);
      }
   }

   public void error(java.lang.String msg, Object[] args) throws TransformerException {
      java.lang.String fmsg = XPATHMessages.createXPATHMessage(msg, args);
      if (null != this.m_errorHandler) {
         this.m_errorHandler.fatalError(new TransformerException(fmsg, this.m_locator));
      } else {
         throw new TransformerException(fmsg, (SAXSourceLocator)this.m_locator);
      }
   }

   public PrefixResolver getNamespaceContext() {
      return this.m_currentPrefixResolver;
   }

   public void setNamespaceContext(PrefixResolver pr) {
      this.m_currentPrefixResolver = pr;
   }
}
