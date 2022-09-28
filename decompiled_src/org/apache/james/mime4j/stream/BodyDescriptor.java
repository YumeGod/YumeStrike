package org.apache.james.mime4j.stream;

public interface BodyDescriptor extends ContentDescriptor {
   String getBoundary();
}
