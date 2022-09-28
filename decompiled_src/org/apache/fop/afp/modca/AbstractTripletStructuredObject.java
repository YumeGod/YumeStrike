package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.fop.afp.modca.triplets.AbstractTriplet;
import org.apache.fop.afp.modca.triplets.CommentTriplet;
import org.apache.fop.afp.modca.triplets.FullyQualifiedNameTriplet;
import org.apache.fop.afp.modca.triplets.ObjectClassificationTriplet;
import org.apache.fop.afp.modca.triplets.Triplet;

public class AbstractTripletStructuredObject extends AbstractStructuredObject {
   protected List triplets = new ArrayList();

   protected int getTripletDataLength() {
      int dataLength = 0;
      AbstractTriplet triplet;
      if (this.hasTriplets()) {
         for(Iterator it = this.triplets.iterator(); it.hasNext(); dataLength += triplet.getDataLength()) {
            triplet = (AbstractTriplet)it.next();
         }
      }

      return dataLength;
   }

   public boolean hasTriplets() {
      return this.triplets.size() > 0;
   }

   protected void writeTriplets(OutputStream os) throws IOException {
      if (this.hasTriplets()) {
         this.writeObjects(this.triplets, os);
         this.triplets = null;
      }

   }

   private AbstractTriplet getTriplet(byte tripletId) {
      Iterator it = this.getTriplets().iterator();

      AbstractTriplet triplet;
      do {
         if (!it.hasNext()) {
            return null;
         }

         triplet = (AbstractTriplet)it.next();
      } while(triplet.getId() != tripletId);

      return triplet;
   }

   public boolean hasTriplet(byte tripletId) {
      return this.getTriplet(tripletId) != null;
   }

   protected void addTriplet(Triplet triplet) {
      this.triplets.add(triplet);
   }

   public void addTriplets(Collection tripletCollection) {
      if (tripletCollection != null) {
         this.triplets.addAll(tripletCollection);
      }

   }

   protected List getTriplets() {
      return this.triplets;
   }

   public void setFullyQualifiedName(byte fqnType, byte fqnFormat, String fqName) {
      this.addTriplet(new FullyQualifiedNameTriplet(fqnType, fqnFormat, fqName));
   }

   public String getFullyQualifiedName() {
      FullyQualifiedNameTriplet fqNameTriplet = (FullyQualifiedNameTriplet)this.getTriplet((byte)2);
      if (fqNameTriplet != null) {
         return fqNameTriplet.getFullyQualifiedName();
      } else {
         log.warn(this + " has no fully qualified name");
         return null;
      }
   }

   public void setObjectClassification(byte objectClass, Registry.ObjectType objectType, boolean dataInContainer, boolean containerHasOEG, boolean dataInOCD) {
      this.addTriplet(new ObjectClassificationTriplet(objectClass, objectType, dataInContainer, containerHasOEG, dataInOCD));
   }

   public void setComment(String commentString) {
      this.addTriplet(new CommentTriplet((byte)101, commentString));
   }
}
