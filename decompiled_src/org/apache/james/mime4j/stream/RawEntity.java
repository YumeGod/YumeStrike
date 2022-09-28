package org.apache.james.mime4j.stream;

import java.io.InputStream;

public class RawEntity implements EntityStateMachine {
   private final InputStream stream;
   private EntityState state;

   RawEntity(InputStream stream) {
      this.stream = stream;
      this.state = EntityState.T_RAW_ENTITY;
   }

   public EntityState getState() {
      return this.state;
   }

   public void setRecursionMode(RecursionMode recursionMode) {
   }

   public EntityStateMachine advance() {
      this.state = EntityState.T_END_OF_STREAM;
      return null;
   }

   public InputStream getContentStream() {
      return this.stream;
   }

   public BodyDescriptor getBodyDescriptor() {
      return null;
   }

   public RawField getField() {
      return null;
   }

   public String getFieldName() {
      return null;
   }

   public String getFieldValue() {
      return null;
   }

   public InputStream getDecodedContentStream() throws IllegalStateException {
      throw new IllegalStateException("Raw entity does not support stream decoding");
   }
}
