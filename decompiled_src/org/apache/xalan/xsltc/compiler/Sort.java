package org.apache.xalan.xsltc.compiler;

import java.util.ArrayList;
import java.util.Vector;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.TABLESWITCH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.CompareGenerator;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSortRecordFactGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSortRecordGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Sort extends Instruction implements Closure {
   private Expression _select;
   private AttributeValue _order;
   private AttributeValue _caseOrder;
   private AttributeValue _dataType;
   private String _lang;
   private String _data = null;
   private String _className = null;
   private ArrayList _closureVars = null;
   private boolean _needsSortRecordFactory = false;

   public boolean inInnerClass() {
      return this._className != null;
   }

   public Closure getParentClosure() {
      return null;
   }

   public String getInnerClassName() {
      return this._className;
   }

   public void addVariable(VariableRefBase variableRef) {
      if (this._closureVars == null) {
         this._closureVars = new ArrayList();
      }

      if (!this._closureVars.contains(variableRef)) {
         this._closureVars.add(variableRef);
         this._needsSortRecordFactory = true;
      }

   }

   private void setInnerClassName(String className) {
      this._className = className;
   }

   public void parseContents(Parser parser) {
      SyntaxTreeNode parent = this.getParent();
      if (!(parent instanceof ApplyTemplates) && !(parent instanceof ForEach)) {
         this.reportError(this, parser, "STRAY_SORT_ERR", (String)null);
      } else {
         this._select = parser.parseExpression(this, "select", "string(.)");
         String val = this.getAttribute("order");
         if (val.length() == 0) {
            val = "ascending";
         }

         this._order = AttributeValue.create(this, val, parser);
         val = this.getAttribute("data-type");
         if (val.length() == 0) {
            try {
               Type type = this._select.typeCheck(parser.getSymbolTable());
               if (type instanceof IntType) {
                  val = "number";
               } else {
                  val = "text";
               }
            } catch (TypeCheckError var5) {
               val = "text";
            }
         }

         this._dataType = AttributeValue.create(this, val, parser);
         this._lang = this.getAttribute("lang");
         val = this.getAttribute("case-order");
         this._caseOrder = AttributeValue.create(this, val, parser);
      }
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      Type tselect = this._select.typeCheck(stable);
      if (!(tselect instanceof StringType)) {
         this._select = new CastExpr(this._select, Type.String);
      }

      this._order.typeCheck(stable);
      this._caseOrder.typeCheck(stable);
      this._dataType.typeCheck(stable);
      return Type.Void;
   }

   public void translateSortType(ClassGenerator classGen, MethodGenerator methodGen) {
      this._dataType.translate(classGen, methodGen);
   }

   public void translateSortOrder(ClassGenerator classGen, MethodGenerator methodGen) {
      this._order.translate(classGen, methodGen);
   }

   public void translateCaseOrder(ClassGenerator classGen, MethodGenerator methodGen) {
      this._caseOrder.translate(classGen, methodGen);
   }

   public void translateLang(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append((CompoundInstruction)(new PUSH(cpg, this._lang)));
   }

   public void translateSelect(ClassGenerator classGen, MethodGenerator methodGen) {
      this._select.translate(classGen, methodGen);
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
   }

   public static void translateSortIterator(ClassGenerator classGen, MethodGenerator methodGen, Expression nodeSet, Vector sortObjects) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int init = cpg.addMethodref("org.apache.xalan.xsltc.dom.SortingIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xalan/xsltc/dom/NodeSortRecordFactory;)V");
      LocalVariableGen nodesTemp = methodGen.addLocalVariable("sort_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), il.getEnd(), (InstructionHandle)null);
      LocalVariableGen sortRecordFactoryTemp = methodGen.addLocalVariable("sort_tmp2", Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/NodeSortRecordFactory;"), il.getEnd(), (InstructionHandle)null);
      if (nodeSet == null) {
         int children = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getAxisIterator", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
         il.append(methodGen.loadDOM());
         il.append((CompoundInstruction)(new PUSH(cpg, 3)));
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(children, 2)));
      } else {
         nodeSet.translate(classGen, methodGen);
      }

      il.append((org.apache.bcel.generic.Instruction)(new ASTORE(nodesTemp.getIndex())));
      compileSortRecordFactory(sortObjects, classGen, methodGen);
      il.append((org.apache.bcel.generic.Instruction)(new ASTORE(sortRecordFactoryTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.SortingIterator"))));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(nodesTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(sortRecordFactoryTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(init)));
   }

   public static void compileSortRecordFactory(Vector sortObjects, ClassGenerator classGen, MethodGenerator methodGen) {
      String sortRecordClass = compileSortRecord(sortObjects, classGen, methodGen);
      boolean needsSortRecordFactory = false;
      int nsorts = sortObjects.size();

      for(int i = 0; i < nsorts; ++i) {
         Sort sort = (Sort)sortObjects.elementAt(i);
         needsSortRecordFactory |= sort._needsSortRecordFactory;
      }

      String sortRecordFactoryClass = "org/apache/xalan/xsltc/dom/NodeSortRecordFactory";
      if (needsSortRecordFactory) {
         sortRecordFactoryClass = compileSortRecordFactory(sortObjects, classGen, methodGen, sortRecordClass);
      }

      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      LocalVariableGen sortOrderTemp = methodGen.addLocalVariable("sort_order_tmp", Util.getJCRefType("[Ljava/lang/String;"), il.getEnd(), (InstructionHandle)null);
      il.append((CompoundInstruction)(new PUSH(cpg, nsorts)));
      il.append((org.apache.bcel.generic.Instruction)(new ANEWARRAY(cpg.addClass("java.lang.String"))));

      for(int level = 0; level < nsorts; ++level) {
         Sort sort = (Sort)sortObjects.elementAt(level);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((CompoundInstruction)(new PUSH(cpg, level)));
         sort.translateSortOrder(classGen, methodGen);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.AASTORE);
      }

      il.append((org.apache.bcel.generic.Instruction)(new ASTORE(sortOrderTemp.getIndex())));
      LocalVariableGen sortTypeTemp = methodGen.addLocalVariable("sort_type_tmp", Util.getJCRefType("[Ljava/lang/String;"), il.getEnd(), (InstructionHandle)null);
      il.append((CompoundInstruction)(new PUSH(cpg, nsorts)));
      il.append((org.apache.bcel.generic.Instruction)(new ANEWARRAY(cpg.addClass("java.lang.String"))));

      for(int level = 0; level < nsorts; ++level) {
         Sort sort = (Sort)sortObjects.elementAt(level);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((CompoundInstruction)(new PUSH(cpg, level)));
         sort.translateSortType(classGen, methodGen);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.AASTORE);
      }

      il.append((org.apache.bcel.generic.Instruction)(new ASTORE(sortTypeTemp.getIndex())));
      LocalVariableGen sortLangTemp = methodGen.addLocalVariable("sort_lang_tmp", Util.getJCRefType("[Ljava/lang/String;"), il.getEnd(), (InstructionHandle)null);
      il.append((CompoundInstruction)(new PUSH(cpg, nsorts)));
      il.append((org.apache.bcel.generic.Instruction)(new ANEWARRAY(cpg.addClass("java.lang.String"))));

      for(int level = 0; level < nsorts; ++level) {
         Sort sort = (Sort)sortObjects.elementAt(level);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((CompoundInstruction)(new PUSH(cpg, level)));
         sort.translateLang(classGen, methodGen);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.AASTORE);
      }

      il.append((org.apache.bcel.generic.Instruction)(new ASTORE(sortLangTemp.getIndex())));
      LocalVariableGen sortCaseOrderTemp = methodGen.addLocalVariable("sort_case_order_tmp", Util.getJCRefType("[Ljava/lang/String;"), il.getEnd(), (InstructionHandle)null);
      il.append((CompoundInstruction)(new PUSH(cpg, nsorts)));
      il.append((org.apache.bcel.generic.Instruction)(new ANEWARRAY(cpg.addClass("java.lang.String"))));

      for(int level = 0; level < nsorts; ++level) {
         Sort sort = (Sort)sortObjects.elementAt(level);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((CompoundInstruction)(new PUSH(cpg, level)));
         sort.translateCaseOrder(classGen, methodGen);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.AASTORE);
      }

      il.append((org.apache.bcel.generic.Instruction)(new ASTORE(sortCaseOrderTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass(sortRecordFactoryClass))));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append(methodGen.loadDOM());
      il.append((CompoundInstruction)(new PUSH(cpg, sortRecordClass)));
      il.append(classGen.loadTranslet());
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(sortOrderTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(sortTypeTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(sortLangTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(sortCaseOrderTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(cpg.addMethodref(sortRecordFactoryClass, "<init>", "(Lorg/apache/xalan/xsltc/DOM;Ljava/lang/String;Lorg/apache/xalan/xsltc/Translet;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V"))));
      ArrayList dups = new ArrayList();

      for(int j = 0; j < nsorts; ++j) {
         Sort sort = (Sort)sortObjects.get(j);
         int length = sort._closureVars == null ? 0 : sort._closureVars.size();

         for(int i = 0; i < length; ++i) {
            VariableRefBase varRef = (VariableRefBase)sort._closureVars.get(i);
            if (!dups.contains(varRef)) {
               VariableBase var = varRef.getVariable();
               il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
               il.append(var.loadInstruction());
               il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(cpg.addFieldref(sortRecordFactoryClass, var.getEscapedName(), var.getType().toSignature()))));
               dups.add(varRef);
            }
         }
      }

   }

   public static String compileSortRecordFactory(Vector sortObjects, ClassGenerator classGen, MethodGenerator methodGen, String sortRecordClass) {
      XSLTC xsltc = ((Sort)sortObjects.firstElement()).getXSLTC();
      String className = xsltc.getHelperClassName();
      NodeSortRecordFactGenerator sortRecordFactory = new NodeSortRecordFactGenerator(className, "org/apache/xalan/xsltc/dom/NodeSortRecordFactory", className + ".java", 49, new String[0], classGen.getStylesheet());
      ConstantPoolGen cpg = sortRecordFactory.getConstantPool();
      int nsorts = sortObjects.size();
      ArrayList dups = new ArrayList();

      for(int j = 0; j < nsorts; ++j) {
         Sort sort = (Sort)sortObjects.get(j);
         int length = sort._closureVars == null ? 0 : sort._closureVars.size();

         for(int i = 0; i < length; ++i) {
            VariableRefBase varRef = (VariableRefBase)sort._closureVars.get(i);
            if (!dups.contains(varRef)) {
               VariableBase var = varRef.getVariable();
               sortRecordFactory.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), (org.apache.bcel.classfile.Attribute[])null, cpg.getConstantPool()));
               dups.add(varRef);
            }
         }
      }

      org.apache.bcel.generic.Type[] argTypes = new org.apache.bcel.generic.Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Ljava/lang/String;"), Util.getJCRefType("Lorg/apache/xalan/xsltc/Translet;"), Util.getJCRefType("[Ljava/lang/String;"), Util.getJCRefType("[Ljava/lang/String;"), Util.getJCRefType("[Ljava/lang/String;"), Util.getJCRefType("[Ljava/lang/String;")};
      String[] argNames = new String[]{"document", "className", "translet", "order", "type", "lang", "case_order"};
      InstructionList il = new InstructionList();
      MethodGenerator constructor = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, argTypes, argNames, "<init>", className, il, cpg);
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ALOAD_0);
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ALOAD_1);
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ALOAD_2);
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(3)));
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(4)));
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(5)));
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(6)));
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(7)));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(cpg.addMethodref("org/apache/xalan/xsltc/dom/NodeSortRecordFactory", "<init>", "(Lorg/apache/xalan/xsltc/DOM;Ljava/lang/String;Lorg/apache/xalan/xsltc/Translet;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V"))));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.RETURN);
      il = new InstructionList();
      MethodGenerator makeNodeSortRecord = new MethodGenerator(1, Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/NodeSortRecord;"), new org.apache.bcel.generic.Type[]{org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT}, new String[]{"node", "last"}, "makeNodeSortRecord", className, il, cpg);
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ALOAD_0);
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ILOAD_1);
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ILOAD_2);
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(cpg.addMethodref("org/apache/xalan/xsltc/dom/NodeSortRecordFactory", "makeNodeSortRecord", "(II)Lorg/apache/xalan/xsltc/dom/NodeSortRecord;"))));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append((org.apache.bcel.generic.Instruction)(new CHECKCAST(cpg.addClass(sortRecordClass))));
      int ndups = dups.size();

      for(int i = 0; i < ndups; ++i) {
         VariableRefBase varRef = (VariableRefBase)dups.get(i);
         VariableBase var = varRef.getVariable();
         Type varType = var.getType();
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ALOAD_0);
         il.append((org.apache.bcel.generic.Instruction)(new GETFIELD(cpg.addFieldref(className, var.getEscapedName(), varType.toSignature()))));
         il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(cpg.addFieldref(sortRecordClass, var.getEscapedName(), varType.toSignature()))));
      }

      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.POP);
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ARETURN);
      constructor.setMaxLocals();
      constructor.setMaxStack();
      sortRecordFactory.addMethod(constructor.getMethod());
      makeNodeSortRecord.setMaxLocals();
      makeNodeSortRecord.setMaxStack();
      sortRecordFactory.addMethod(makeNodeSortRecord.getMethod());
      xsltc.dumpClass(sortRecordFactory.getJavaClass());
      return className;
   }

   private static String compileSortRecord(Vector sortObjects, ClassGenerator classGen, MethodGenerator methodGen) {
      XSLTC xsltc = ((Sort)sortObjects.firstElement()).getXSLTC();
      String className = xsltc.getHelperClassName();
      NodeSortRecordGenerator sortRecord = new NodeSortRecordGenerator(className, "org.apache.xalan.xsltc.dom.NodeSortRecord", "sort$0.java", 49, new String[0], classGen.getStylesheet());
      ConstantPoolGen cpg = sortRecord.getConstantPool();
      int nsorts = sortObjects.size();
      ArrayList dups = new ArrayList();

      for(int j = 0; j < nsorts; ++j) {
         Sort sort = (Sort)sortObjects.get(j);
         sort.setInnerClassName(className);
         int length = sort._closureVars == null ? 0 : sort._closureVars.size();

         for(int i = 0; i < length; ++i) {
            VariableRefBase varRef = (VariableRefBase)sort._closureVars.get(i);
            if (!dups.contains(varRef)) {
               VariableBase var = varRef.getVariable();
               sortRecord.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), (org.apache.bcel.classfile.Attribute[])null, cpg.getConstantPool()));
               dups.add(varRef);
            }
         }
      }

      Method init = compileInit(sortObjects, sortRecord, cpg, className);
      Method extract = compileExtract(sortObjects, sortRecord, cpg, className);
      sortRecord.addMethod(init);
      sortRecord.addMethod(extract);
      xsltc.dumpClass(sortRecord.getJavaClass());
      return className;
   }

   private static Method compileInit(Vector sortObjects, NodeSortRecordGenerator sortRecord, ConstantPoolGen cpg, String className) {
      InstructionList il = new InstructionList();
      MethodGenerator init = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, (org.apache.bcel.generic.Type[])null, (String[])null, "<init>", className, il, cpg);
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ALOAD_0);
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(cpg.addMethodref("org.apache.xalan.xsltc.dom.NodeSortRecord", "<init>", "()V"))));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.RETURN);
      init.stripAttributes(true);
      init.setMaxLocals();
      init.setMaxStack();
      return init.getMethod();
   }

   private static Method compileExtract(Vector sortObjects, NodeSortRecordGenerator sortRecord, ConstantPoolGen cpg, String className) {
      InstructionList il = new InstructionList();
      CompareGenerator extractMethod = new CompareGenerator(17, org.apache.bcel.generic.Type.STRING, new org.apache.bcel.generic.Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT, Util.getJCRefType("Lorg/apache/xalan/xsltc/runtime/AbstractTranslet;"), org.apache.bcel.generic.Type.INT}, new String[]{"dom", "current", "level", "translet", "last"}, "extractValueFromDOM", className, il, cpg);
      int levels = sortObjects.size();
      int[] match = new int[levels];
      InstructionHandle[] target = new InstructionHandle[levels];
      InstructionHandle tblswitch = null;
      if (levels > 1) {
         il.append((org.apache.bcel.generic.Instruction)(new ILOAD(extractMethod.getLocalIndex("level"))));
         tblswitch = il.append((org.apache.bcel.generic.Instruction)(new NOP()));
      }

      for(int level = 0; level < levels; ++level) {
         match[level] = level;
         Sort sort = (Sort)sortObjects.elementAt(level);
         target[level] = il.append(InstructionConstants.NOP);
         sort.translateSelect(sortRecord, extractMethod);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ARETURN);
      }

      if (levels > 1) {
         InstructionHandle defaultTarget = il.append((CompoundInstruction)(new PUSH(cpg, "")));
         il.insert((InstructionHandle)tblswitch, (BranchInstruction)(new TABLESWITCH(match, target, defaultTarget)));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ARETURN);
      }

      extractMethod.stripAttributes(true);
      extractMethod.setMaxLocals();
      extractMethod.setMaxStack();
      extractMethod.removeNOPs();
      return extractMethod.getMethod();
   }
}
