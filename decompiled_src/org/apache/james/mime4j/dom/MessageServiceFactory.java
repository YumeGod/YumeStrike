package org.apache.james.mime4j.dom;

import org.apache.james.mime4j.MimeException;

public abstract class MessageServiceFactory {
   public static MessageServiceFactory newInstance() throws MimeException {
      return (MessageServiceFactory)ServiceLoader.load(MessageServiceFactory.class);
   }

   public abstract MessageBuilder newMessageBuilder();

   public abstract MessageWriter newMessageWriter();

   public abstract void setAttribute(String var1, Object var2) throws IllegalArgumentException;
}
