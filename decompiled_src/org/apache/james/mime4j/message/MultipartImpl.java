package org.apache.james.mime4j.message;

import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

public class MultipartImpl extends AbstractMultipart {
   private ByteSequence preamble = null;
   private transient String preambleStrCache = null;
   private transient boolean preambleComputed = false;
   private ByteSequence epilogue;
   private transient String epilogueStrCache;
   private transient boolean epilogueComputed = false;

   public MultipartImpl(String subType) {
      super(subType);
      this.preambleComputed = true;
      this.epilogue = null;
      this.epilogueStrCache = null;
      this.epilogueComputed = true;
   }

   public ByteSequence getPreambleRaw() {
      return this.preamble;
   }

   public void setPreambleRaw(ByteSequence preamble) {
      this.preamble = preamble;
      this.preambleStrCache = null;
      this.preambleComputed = false;
   }

   public String getPreamble() {
      if (!this.preambleComputed) {
         this.preambleStrCache = this.preamble != null ? ContentUtil.decode(this.preamble) : null;
         this.preambleComputed = true;
      }

      return this.preambleStrCache;
   }

   public void setPreamble(String preamble) {
      this.preamble = preamble != null ? ContentUtil.encode(preamble) : null;
      this.preambleStrCache = preamble;
      this.preambleComputed = true;
   }

   public ByteSequence getEpilogueRaw() {
      return this.epilogue;
   }

   public void setEpilogueRaw(ByteSequence epilogue) {
      this.epilogue = epilogue;
      this.epilogueStrCache = null;
      this.epilogueComputed = false;
   }

   public String getEpilogue() {
      if (!this.epilogueComputed) {
         this.epilogueStrCache = this.epilogue != null ? ContentUtil.decode(this.epilogue) : null;
         this.epilogueComputed = true;
      }

      return this.epilogueStrCache;
   }

   public void setEpilogue(String epilogue) {
      this.epilogue = epilogue != null ? ContentUtil.encode(epilogue) : null;
      this.epilogueStrCache = epilogue;
      this.epilogueComputed = true;
   }
}
