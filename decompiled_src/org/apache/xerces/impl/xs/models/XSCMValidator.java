package org.apache.xerces.impl.xs.models;

import java.util.Vector;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.xni.QName;

public interface XSCMValidator {
   short FIRST_ERROR = -1;
   short SUBSEQUENT_ERROR = -2;

   int[] startContentModel();

   Object oneTransition(QName var1, int[] var2, SubstitutionGroupHandler var3);

   boolean endContentModel(int[] var1);

   boolean checkUniqueParticleAttribution(SubstitutionGroupHandler var1) throws XMLSchemaException;

   Vector whatCanGoHere(int[] var1);
}
