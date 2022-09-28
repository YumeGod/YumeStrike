package org.apache.james.mime4j.message;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Multipart;

public abstract class AbstractMultipart implements Multipart {
   protected List bodyParts = new LinkedList();
   private Entity parent = null;
   private String subType;

   public AbstractMultipart(String subType) {
      this.subType = subType;
   }

   public String getSubType() {
      return this.subType;
   }

   public void setSubType(String subType) {
      this.subType = subType;
   }

   public Entity getParent() {
      return this.parent;
   }

   public void setParent(Entity parent) {
      this.parent = parent;
      Iterator i$ = this.bodyParts.iterator();

      while(i$.hasNext()) {
         Entity bodyPart = (Entity)i$.next();
         bodyPart.setParent(parent);
      }

   }

   public int getCount() {
      return this.bodyParts.size();
   }

   public List getBodyParts() {
      return Collections.unmodifiableList(this.bodyParts);
   }

   public void setBodyParts(List bodyParts) {
      this.bodyParts = bodyParts;
      Iterator i$ = bodyParts.iterator();

      while(i$.hasNext()) {
         Entity bodyPart = (Entity)i$.next();
         bodyPart.setParent(this.parent);
      }

   }

   public void addBodyPart(Entity bodyPart) {
      if (bodyPart == null) {
         throw new IllegalArgumentException();
      } else {
         this.bodyParts.add(bodyPart);
         bodyPart.setParent(this.parent);
      }
   }

   public void addBodyPart(Entity bodyPart, int index) {
      if (bodyPart == null) {
         throw new IllegalArgumentException();
      } else {
         this.bodyParts.add(index, bodyPart);
         bodyPart.setParent(this.parent);
      }
   }

   public Entity removeBodyPart(int index) {
      Entity bodyPart = (Entity)this.bodyParts.remove(index);
      bodyPart.setParent((Entity)null);
      return bodyPart;
   }

   public Entity replaceBodyPart(Entity bodyPart, int index) {
      if (bodyPart == null) {
         throw new IllegalArgumentException();
      } else {
         Entity replacedEntity = (Entity)this.bodyParts.set(index, bodyPart);
         if (bodyPart == replacedEntity) {
            throw new IllegalArgumentException("Cannot replace body part with itself");
         } else {
            bodyPart.setParent(this.parent);
            replacedEntity.setParent((Entity)null);
            return replacedEntity;
         }
      }
   }

   public abstract String getPreamble();

   public abstract void setPreamble(String var1);

   public abstract String getEpilogue();

   public abstract void setEpilogue(String var1);

   public void dispose() {
      Iterator i$ = this.bodyParts.iterator();

      while(i$.hasNext()) {
         Entity bodyPart = (Entity)i$.next();
         bodyPart.dispose();
      }

   }
}
