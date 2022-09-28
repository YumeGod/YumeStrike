package org.apache.bcel.generic;

public interface Visitor {
   void visitStackInstruction(StackInstruction var1);

   void visitLocalVariableInstruction(LocalVariableInstruction var1);

   void visitBranchInstruction(BranchInstruction var1);

   void visitLoadClass(LoadClass var1);

   void visitFieldInstruction(FieldInstruction var1);

   void visitIfInstruction(IfInstruction var1);

   void visitConversionInstruction(ConversionInstruction var1);

   void visitPopInstruction(PopInstruction var1);

   void visitStoreInstruction(StoreInstruction var1);

   void visitTypedInstruction(TypedInstruction var1);

   void visitSelect(Select var1);

   void visitJsrInstruction(JsrInstruction var1);

   void visitGotoInstruction(GotoInstruction var1);

   void visitUnconditionalBranch(UnconditionalBranch var1);

   void visitPushInstruction(PushInstruction var1);

   void visitArithmeticInstruction(ArithmeticInstruction var1);

   void visitCPInstruction(CPInstruction var1);

   void visitInvokeInstruction(InvokeInstruction var1);

   void visitArrayInstruction(ArrayInstruction var1);

   void visitAllocationInstruction(AllocationInstruction var1);

   void visitReturnInstruction(ReturnInstruction var1);

   void visitFieldOrMethod(FieldOrMethod var1);

   void visitConstantPushInstruction(ConstantPushInstruction var1);

   void visitExceptionThrower(ExceptionThrower var1);

   void visitLoadInstruction(LoadInstruction var1);

   void visitVariableLengthInstruction(VariableLengthInstruction var1);

   void visitStackProducer(StackProducer var1);

   void visitStackConsumer(StackConsumer var1);

   void visitACONST_NULL(ACONST_NULL var1);

   void visitGETSTATIC(GETSTATIC var1);

   void visitIF_ICMPLT(IF_ICMPLT var1);

   void visitMONITOREXIT(MONITOREXIT var1);

   void visitIFLT(IFLT var1);

   void visitLSTORE(LSTORE var1);

   void visitPOP2(POP2 var1);

   void visitBASTORE(BASTORE var1);

   void visitISTORE(ISTORE var1);

   void visitCHECKCAST(CHECKCAST var1);

   void visitFCMPG(FCMPG var1);

   void visitI2F(I2F var1);

   void visitATHROW(ATHROW var1);

   void visitDCMPL(DCMPL var1);

   void visitARRAYLENGTH(ARRAYLENGTH var1);

   void visitDUP(DUP var1);

   void visitINVOKESTATIC(INVOKESTATIC var1);

   void visitLCONST(LCONST var1);

   void visitDREM(DREM var1);

   void visitIFGE(IFGE var1);

   void visitCALOAD(CALOAD var1);

   void visitLASTORE(LASTORE var1);

   void visitI2D(I2D var1);

   void visitDADD(DADD var1);

   void visitINVOKESPECIAL(INVOKESPECIAL var1);

   void visitIAND(IAND var1);

   void visitPUTFIELD(PUTFIELD var1);

   void visitILOAD(ILOAD var1);

   void visitDLOAD(DLOAD var1);

   void visitDCONST(DCONST var1);

   void visitNEW(NEW var1);

   void visitIFNULL(IFNULL var1);

   void visitLSUB(LSUB var1);

   void visitL2I(L2I var1);

   void visitISHR(ISHR var1);

   void visitTABLESWITCH(TABLESWITCH var1);

   void visitIINC(IINC var1);

   void visitDRETURN(DRETURN var1);

   void visitFSTORE(FSTORE var1);

   void visitDASTORE(DASTORE var1);

   void visitIALOAD(IALOAD var1);

   void visitDDIV(DDIV var1);

   void visitIF_ICMPGE(IF_ICMPGE var1);

   void visitLAND(LAND var1);

   void visitIDIV(IDIV var1);

   void visitLOR(LOR var1);

   void visitCASTORE(CASTORE var1);

   void visitFREM(FREM var1);

   void visitLDC(LDC var1);

   void visitBIPUSH(BIPUSH var1);

   void visitDSTORE(DSTORE var1);

   void visitF2L(F2L var1);

   void visitFMUL(FMUL var1);

   void visitLLOAD(LLOAD var1);

   void visitJSR(JSR var1);

   void visitFSUB(FSUB var1);

   void visitSASTORE(SASTORE var1);

   void visitALOAD(ALOAD var1);

   void visitDUP2_X2(DUP2_X2 var1);

   void visitRETURN(RETURN var1);

   void visitDALOAD(DALOAD var1);

   void visitSIPUSH(SIPUSH var1);

   void visitDSUB(DSUB var1);

   void visitL2F(L2F var1);

   void visitIF_ICMPGT(IF_ICMPGT var1);

   void visitF2D(F2D var1);

   void visitI2L(I2L var1);

   void visitIF_ACMPNE(IF_ACMPNE var1);

   void visitPOP(POP var1);

   void visitI2S(I2S var1);

   void visitIFEQ(IFEQ var1);

   void visitSWAP(SWAP var1);

   void visitIOR(IOR var1);

   void visitIREM(IREM var1);

   void visitIASTORE(IASTORE var1);

   void visitNEWARRAY(NEWARRAY var1);

   void visitINVOKEINTERFACE(INVOKEINTERFACE var1);

   void visitINEG(INEG var1);

   void visitLCMP(LCMP var1);

   void visitJSR_W(JSR_W var1);

   void visitMULTIANEWARRAY(MULTIANEWARRAY var1);

   void visitDUP_X2(DUP_X2 var1);

   void visitSALOAD(SALOAD var1);

   void visitIFNONNULL(IFNONNULL var1);

   void visitDMUL(DMUL var1);

   void visitIFNE(IFNE var1);

   void visitIF_ICMPLE(IF_ICMPLE var1);

   void visitLDC2_W(LDC2_W var1);

   void visitGETFIELD(GETFIELD var1);

   void visitLADD(LADD var1);

   void visitNOP(NOP var1);

   void visitFALOAD(FALOAD var1);

   void visitINSTANCEOF(INSTANCEOF var1);

   void visitIFLE(IFLE var1);

   void visitLXOR(LXOR var1);

   void visitLRETURN(LRETURN var1);

   void visitFCONST(FCONST var1);

   void visitIUSHR(IUSHR var1);

   void visitBALOAD(BALOAD var1);

   void visitDUP2(DUP2 var1);

   void visitIF_ACMPEQ(IF_ACMPEQ var1);

   void visitIMPDEP1(IMPDEP1 var1);

   void visitMONITORENTER(MONITORENTER var1);

   void visitLSHL(LSHL var1);

   void visitDCMPG(DCMPG var1);

   void visitD2L(D2L var1);

   void visitIMPDEP2(IMPDEP2 var1);

   void visitL2D(L2D var1);

   void visitRET(RET var1);

   void visitIFGT(IFGT var1);

   void visitIXOR(IXOR var1);

   void visitINVOKEVIRTUAL(INVOKEVIRTUAL var1);

   void visitFASTORE(FASTORE var1);

   void visitIRETURN(IRETURN var1);

   void visitIF_ICMPNE(IF_ICMPNE var1);

   void visitFLOAD(FLOAD var1);

   void visitLDIV(LDIV var1);

   void visitPUTSTATIC(PUTSTATIC var1);

   void visitAALOAD(AALOAD var1);

   void visitD2I(D2I var1);

   void visitIF_ICMPEQ(IF_ICMPEQ var1);

   void visitAASTORE(AASTORE var1);

   void visitARETURN(ARETURN var1);

   void visitDUP2_X1(DUP2_X1 var1);

   void visitFNEG(FNEG var1);

   void visitGOTO_W(GOTO_W var1);

   void visitD2F(D2F var1);

   void visitGOTO(GOTO var1);

   void visitISUB(ISUB var1);

   void visitF2I(F2I var1);

   void visitDNEG(DNEG var1);

   void visitICONST(ICONST var1);

   void visitFDIV(FDIV var1);

   void visitI2B(I2B var1);

   void visitLNEG(LNEG var1);

   void visitLREM(LREM var1);

   void visitIMUL(IMUL var1);

   void visitIADD(IADD var1);

   void visitLSHR(LSHR var1);

   void visitLOOKUPSWITCH(LOOKUPSWITCH var1);

   void visitDUP_X1(DUP_X1 var1);

   void visitFCMPL(FCMPL var1);

   void visitI2C(I2C var1);

   void visitLMUL(LMUL var1);

   void visitLUSHR(LUSHR var1);

   void visitISHL(ISHL var1);

   void visitLALOAD(LALOAD var1);

   void visitASTORE(ASTORE var1);

   void visitANEWARRAY(ANEWARRAY var1);

   void visitFRETURN(FRETURN var1);

   void visitFADD(FADD var1);

   void visitBREAKPOINT(BREAKPOINT var1);
}
