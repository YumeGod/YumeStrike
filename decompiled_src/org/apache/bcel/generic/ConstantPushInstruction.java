package org.apache.bcel.generic;

public interface ConstantPushInstruction extends PushInstruction, TypedInstruction {
   Number getValue();
}
