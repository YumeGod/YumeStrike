package org.apache.xmlgraphics.image.loader.cache;

public interface ExpirationPolicy {
   boolean isExpired(TimeStampProvider var1, long var2);
}
