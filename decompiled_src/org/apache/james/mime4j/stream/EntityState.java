package org.apache.james.mime4j.stream;

public enum EntityState {
   T_START_MESSAGE,
   T_END_MESSAGE,
   T_RAW_ENTITY,
   T_START_HEADER,
   T_FIELD,
   T_END_HEADER,
   T_START_MULTIPART,
   T_END_MULTIPART,
   T_PREAMBLE,
   T_EPILOGUE,
   T_START_BODYPART,
   T_END_BODYPART,
   T_BODY,
   T_END_OF_STREAM;
}
