package org.apache.james.mime4j.dom;

import java.util.List;

public interface Multipart extends Body {
   String getSubType();

   int getCount();

   List getBodyParts();

   void setBodyParts(List var1);

   void addBodyPart(Entity var1);

   void addBodyPart(Entity var1, int var2);

   Entity removeBodyPart(int var1);

   Entity replaceBodyPart(Entity var1, int var2);

   String getPreamble();

   void setPreamble(String var1);

   String getEpilogue();

   void setEpilogue(String var1);
}
