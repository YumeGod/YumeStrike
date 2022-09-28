package org.apache.bcel.generic;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.bcel.classfile.Utility;

public class InstructionHandle implements Serializable {
   InstructionHandle next;
   InstructionHandle prev;
   Instruction instruction;
   protected int i_position = -1;
   private HashSet targeters;
   private HashMap attributes;
   private static InstructionHandle ih_list = null;
   // $FF: synthetic field
   static Class class$org$apache$bcel$generic$BranchHandle;

   public final InstructionHandle getNext() {
      return this.next;
   }

   public final InstructionHandle getPrev() {
      return this.prev;
   }

   public final Instruction getInstruction() {
      return this.instruction;
   }

   public void setInstruction(Instruction i) {
      if (i == null) {
         throw new ClassGenException("Assigning null to handle");
      } else if (this.getClass() != (class$org$apache$bcel$generic$BranchHandle == null ? (class$org$apache$bcel$generic$BranchHandle = class$("org.apache.bcel.generic.BranchHandle")) : class$org$apache$bcel$generic$BranchHandle) && i instanceof BranchInstruction) {
         throw new ClassGenException("Assigning branch instruction " + i + " to plain handle");
      } else {
         if (this.instruction != null) {
            this.instruction.dispose();
         }

         this.instruction = i;
      }
   }

   public Instruction swapInstruction(Instruction i) {
      Instruction oldInstruction = this.instruction;
      this.instruction = i;
      return oldInstruction;
   }

   protected InstructionHandle(Instruction i) {
      this.setInstruction(i);
   }

   static final InstructionHandle getInstructionHandle(Instruction i) {
      if (ih_list == null) {
         return new InstructionHandle(i);
      } else {
         InstructionHandle ih = ih_list;
         ih_list = ih.next;
         ih.setInstruction(i);
         return ih;
      }
   }

   protected int updatePosition(int offset, int max_offset) {
      this.i_position += offset;
      return 0;
   }

   public int getPosition() {
      return this.i_position;
   }

   void setPosition(int pos) {
      this.i_position = pos;
   }

   protected void addHandle() {
      this.next = ih_list;
      ih_list = this;
   }

   void dispose() {
      this.next = this.prev = null;
      this.instruction.dispose();
      this.instruction = null;
      this.i_position = -1;
      this.attributes = null;
      this.removeAllTargeters();
      this.addHandle();
   }

   public void removeAllTargeters() {
      if (this.targeters != null) {
         this.targeters.clear();
      }

   }

   public void removeTargeter(InstructionTargeter t) {
      this.targeters.remove(t);
   }

   public void addTargeter(InstructionTargeter t) {
      if (this.targeters == null) {
         this.targeters = new HashSet();
      }

      this.targeters.add(t);
   }

   public boolean hasTargeters() {
      return this.targeters != null && this.targeters.size() > 0;
   }

   public InstructionTargeter[] getTargeters() {
      if (!this.hasTargeters()) {
         return null;
      } else {
         InstructionTargeter[] t = new InstructionTargeter[this.targeters.size()];
         this.targeters.toArray(t);
         return t;
      }
   }

   public String toString(boolean verbose) {
      return Utility.format(this.i_position, 4, false, ' ') + ": " + this.instruction.toString(verbose);
   }

   public String toString() {
      return this.toString(true);
   }

   public void addAttribute(Object key, Object attr) {
      if (this.attributes == null) {
         this.attributes = new HashMap(3);
      }

      this.attributes.put(key, attr);
   }

   public void removeAttribute(Object key) {
      if (this.attributes != null) {
         this.attributes.remove(key);
      }

   }

   public Object getAttribute(Object key) {
      return this.attributes != null ? this.attributes.get(key) : null;
   }

   public Collection getAttributes() {
      return this.attributes.values();
   }

   public void accept(Visitor v) {
      this.instruction.accept(v);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
