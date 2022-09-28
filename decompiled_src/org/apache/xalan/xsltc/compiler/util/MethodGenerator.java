package org.apache.xalan.xsltc.compiler.util;

import java.util.Hashtable;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.xalan.xsltc.compiler.Constants;
import org.apache.xalan.xsltc.compiler.Pattern;

public class MethodGenerator extends MethodGen implements Constants {
   protected static final int INVALID_INDEX = -1;
   private static final String START_ELEMENT_SIG = "(Ljava/lang/String;)V";
   private static final String END_ELEMENT_SIG = "(Ljava/lang/String;)V";
   private InstructionList _mapTypeSub;
   private static final int DOM_INDEX = 1;
   private static final int ITERATOR_INDEX = 2;
   private static final int HANDLER_INDEX = 3;
   private Instruction _iloadCurrent;
   private Instruction _istoreCurrent;
   private final Instruction _astoreHandler = new ASTORE(3);
   private final Instruction _aloadHandler = new ALOAD(3);
   private final Instruction _astoreIterator = new ASTORE(2);
   private final Instruction _aloadIterator = new ALOAD(2);
   private final Instruction _aloadDom = new ALOAD(1);
   private final Instruction _astoreDom = new ASTORE(1);
   private final Instruction _startElement;
   private final Instruction _endElement;
   private final Instruction _startDocument;
   private final Instruction _endDocument;
   private final Instruction _attribute;
   private final Instruction _uniqueAttribute;
   private final Instruction _namespace;
   private final Instruction _setStartNode;
   private final Instruction _reset;
   private final Instruction _nextNode;
   private SlotAllocator _slotAllocator;
   private boolean _allocatorInit = false;
   private Hashtable _preCompiled = new Hashtable();

   public MethodGenerator(int access_flags, org.apache.bcel.generic.Type return_type, org.apache.bcel.generic.Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cpg) {
      super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cpg);
      int startElement = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "startElement", "(Ljava/lang/String;)V");
      this._startElement = new INVOKEINTERFACE(startElement, 2);
      int endElement = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "endElement", "(Ljava/lang/String;)V");
      this._endElement = new INVOKEINTERFACE(endElement, 2);
      int attribute = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "addAttribute", "(Ljava/lang/String;Ljava/lang/String;)V");
      this._attribute = new INVOKEINTERFACE(attribute, 3);
      int uniqueAttribute = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "addUniqueAttribute", "(Ljava/lang/String;Ljava/lang/String;I)V");
      this._uniqueAttribute = new INVOKEINTERFACE(uniqueAttribute, 4);
      int namespace = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "namespaceAfterStartElement", "(Ljava/lang/String;Ljava/lang/String;)V");
      this._namespace = new INVOKEINTERFACE(namespace, 3);
      int index = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "startDocument", "()V");
      this._startDocument = new INVOKEINTERFACE(index, 1);
      index = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "endDocument", "()V");
      this._endDocument = new INVOKEINTERFACE(index, 1);
      index = cpg.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "setStartNode", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
      this._setStartNode = new INVOKEINTERFACE(index, 2);
      index = cpg.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "reset", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
      this._reset = new INVOKEINTERFACE(index, 1);
      index = cpg.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "next", "()I");
      this._nextNode = new INVOKEINTERFACE(index, 1);
      this._slotAllocator = new SlotAllocator();
      this._slotAllocator.initialize(this.getLocalVariables());
      this._allocatorInit = true;
   }

   public LocalVariableGen addLocalVariable(String name, org.apache.bcel.generic.Type type, InstructionHandle start, InstructionHandle end) {
      return this._allocatorInit ? this.addLocalVariable2(name, type, start) : super.addLocalVariable(name, type, start, end);
   }

   public LocalVariableGen addLocalVariable2(String name, org.apache.bcel.generic.Type type, InstructionHandle start) {
      return super.addLocalVariable(name, type, this._slotAllocator.allocateSlot(type), start, (InstructionHandle)null);
   }

   public void removeLocalVariable(LocalVariableGen lvg) {
      this._slotAllocator.releaseSlot(lvg);
      super.removeLocalVariable(lvg);
   }

   public Instruction loadDOM() {
      return this._aloadDom;
   }

   public Instruction storeDOM() {
      return this._astoreDom;
   }

   public Instruction storeHandler() {
      return this._astoreHandler;
   }

   public Instruction loadHandler() {
      return this._aloadHandler;
   }

   public Instruction storeIterator() {
      return this._astoreIterator;
   }

   public Instruction loadIterator() {
      return this._aloadIterator;
   }

   public final Instruction setStartNode() {
      return this._setStartNode;
   }

   public final Instruction reset() {
      return this._reset;
   }

   public final Instruction nextNode() {
      return this._nextNode;
   }

   public final Instruction startElement() {
      return this._startElement;
   }

   public final Instruction endElement() {
      return this._endElement;
   }

   public final Instruction startDocument() {
      return this._startDocument;
   }

   public final Instruction endDocument() {
      return this._endDocument;
   }

   public final Instruction attribute() {
      return this._attribute;
   }

   public final Instruction uniqueAttribute() {
      return this._uniqueAttribute;
   }

   public final Instruction namespace() {
      return this._namespace;
   }

   public Instruction loadCurrentNode() {
      if (this._iloadCurrent == null) {
         int idx = this.getLocalIndex("current");
         if (idx > 0) {
            this._iloadCurrent = new ILOAD(idx);
         } else {
            this._iloadCurrent = new ICONST(0);
         }
      }

      return this._iloadCurrent;
   }

   public Instruction storeCurrentNode() {
      return this._istoreCurrent != null ? this._istoreCurrent : (this._istoreCurrent = new ISTORE(this.getLocalIndex("current")));
   }

   public Instruction loadContextNode() {
      return this.loadCurrentNode();
   }

   public Instruction storeContextNode() {
      return this.storeCurrentNode();
   }

   public int getLocalIndex(String name) {
      return this.getLocalVariable(name).getIndex();
   }

   public LocalVariableGen getLocalVariable(String name) {
      LocalVariableGen[] vars = this.getLocalVariables();

      for(int i = 0; i < vars.length; ++i) {
         if (vars[i].getName().equals(name)) {
            return vars[i];
         }
      }

      return null;
   }

   public void setMaxLocals() {
      int maxLocals = super.getMaxLocals();
      LocalVariableGen[] localVars = super.getLocalVariables();
      if (localVars != null && localVars.length > maxLocals) {
         maxLocals = localVars.length;
      }

      if (maxLocals < 5) {
         maxLocals = 5;
      }

      super.setMaxLocals(maxLocals);
   }

   public void addInstructionList(Pattern pattern, InstructionList ilist) {
      this._preCompiled.put(pattern, ilist);
   }

   public InstructionList getInstructionList(Pattern pattern) {
      return (InstructionList)this._preCompiled.get(pattern);
   }
}
