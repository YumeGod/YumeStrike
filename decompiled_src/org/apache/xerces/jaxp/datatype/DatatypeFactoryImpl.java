package org.apache.xerces.jaxp.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

public class DatatypeFactoryImpl extends DatatypeFactory {
   public Duration newDuration(String var1) {
      return new DurationImpl(var1);
   }

   public Duration newDuration(long var1) {
      return new DurationImpl(var1);
   }

   public Duration newDuration(boolean var1, BigInteger var2, BigInteger var3, BigInteger var4, BigInteger var5, BigInteger var6, BigDecimal var7) {
      return new DurationImpl(var1, var2, var3, var4, var5, var6, var7);
   }

   public XMLGregorianCalendar newXMLGregorianCalendar() {
      return new XMLGregorianCalendarImpl();
   }

   public XMLGregorianCalendar newXMLGregorianCalendar(String var1) {
      return new XMLGregorianCalendarImpl(var1);
   }

   public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar var1) {
      return new XMLGregorianCalendarImpl(var1);
   }

   public XMLGregorianCalendar newXMLGregorianCalendar(BigInteger var1, int var2, int var3, int var4, int var5, int var6, BigDecimal var7, int var8) {
      return new XMLGregorianCalendarImpl(var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
