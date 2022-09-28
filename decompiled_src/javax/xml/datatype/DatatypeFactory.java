package javax.xml.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

public abstract class DatatypeFactory {
   public static final String DATATYPEFACTORY_PROPERTY = "javax.xml.datatype.DatatypeFactory";
   public static final String DATATYPEFACTORY_IMPLEMENTATION_CLASS = "org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl";

   protected DatatypeFactory() {
   }

   public static DatatypeFactory newInstance() throws DatatypeConfigurationException {
      try {
         return (DatatypeFactory)FactoryFinder.find("javax.xml.datatype.DatatypeFactory", "org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl");
      } catch (FactoryFinder.ConfigurationError var1) {
         throw new DatatypeConfigurationException(var1.getMessage(), var1.getException());
      }
   }

   public abstract Duration newDuration(String var1);

   public abstract Duration newDuration(long var1);

   public abstract Duration newDuration(boolean var1, BigInteger var2, BigInteger var3, BigInteger var4, BigInteger var5, BigInteger var6, BigDecimal var7);

   public Duration newDuration(boolean var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      BigInteger var8 = var2 != Integer.MIN_VALUE ? BigInteger.valueOf((long)var2) : null;
      BigInteger var9 = var3 != Integer.MIN_VALUE ? BigInteger.valueOf((long)var3) : null;
      BigInteger var10 = var4 != Integer.MIN_VALUE ? BigInteger.valueOf((long)var4) : null;
      BigInteger var11 = var5 != Integer.MIN_VALUE ? BigInteger.valueOf((long)var5) : null;
      BigInteger var12 = var6 != Integer.MIN_VALUE ? BigInteger.valueOf((long)var6) : null;
      BigDecimal var13 = var7 != Integer.MIN_VALUE ? BigDecimal.valueOf((long)var7) : null;
      return this.newDuration(var1, var8, var9, var10, var11, var12, var13);
   }

   public Duration newDurationDayTime(String var1) {
      return this.newDuration(var1);
   }

   public Duration newDurationDayTime(long var1) {
      return this.newDuration(var1);
   }

   public Duration newDurationDayTime(boolean var1, BigInteger var2, BigInteger var3, BigInteger var4, BigInteger var5) {
      return this.newDuration(var1, (BigInteger)null, (BigInteger)null, var2, var3, var4, var5 != null ? new BigDecimal(var5) : null);
   }

   public Duration newDurationDayTime(boolean var1, int var2, int var3, int var4, int var5) {
      return this.newDurationDayTime(var1, BigInteger.valueOf((long)var2), BigInteger.valueOf((long)var3), BigInteger.valueOf((long)var4), BigInteger.valueOf((long)var5));
   }

   public Duration newDurationYearMonth(String var1) {
      return this.newDuration(var1);
   }

   public Duration newDurationYearMonth(long var1) {
      return this.newDuration(var1);
   }

   public Duration newDurationYearMonth(boolean var1, BigInteger var2, BigInteger var3) {
      return this.newDuration(var1, var2, var3, (BigInteger)null, (BigInteger)null, (BigInteger)null, (BigDecimal)null);
   }

   public Duration newDurationYearMonth(boolean var1, int var2, int var3) {
      return this.newDurationYearMonth(var1, BigInteger.valueOf((long)var2), BigInteger.valueOf((long)var3));
   }

   public abstract XMLGregorianCalendar newXMLGregorianCalendar();

   public abstract XMLGregorianCalendar newXMLGregorianCalendar(String var1);

   public abstract XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar var1);

   public abstract XMLGregorianCalendar newXMLGregorianCalendar(BigInteger var1, int var2, int var3, int var4, int var5, int var6, BigDecimal var7, int var8);

   public XMLGregorianCalendar newXMLGregorianCalendar(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      BigInteger var9 = var1 != Integer.MIN_VALUE ? BigInteger.valueOf((long)var1) : null;
      BigDecimal var10 = null;
      if (var7 != Integer.MIN_VALUE) {
         if (var7 < 0 || var7 > 1000) {
            throw new IllegalArgumentException("javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendar(int year, int month, int day, int hour, int minute, int second, int millisecond, int timezone)with invalid millisecond: " + var7);
         }

         var10 = BigDecimal.valueOf((long)var7).movePointLeft(3);
      }

      return this.newXMLGregorianCalendar(var9, var2, var3, var4, var5, var6, var10, var8);
   }

   public XMLGregorianCalendar newXMLGregorianCalendarDate(int var1, int var2, int var3, int var4) {
      return this.newXMLGregorianCalendar(var1, var2, var3, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, var4);
   }

   public XMLGregorianCalendar newXMLGregorianCalendarTime(int var1, int var2, int var3, int var4) {
      return this.newXMLGregorianCalendar(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, var1, var2, var3, Integer.MIN_VALUE, var4);
   }

   public XMLGregorianCalendar newXMLGregorianCalendarTime(int var1, int var2, int var3, BigDecimal var4, int var5) {
      return this.newXMLGregorianCalendar((BigInteger)null, Integer.MIN_VALUE, Integer.MIN_VALUE, var1, var2, var3, var4, var5);
   }

   public XMLGregorianCalendar newXMLGregorianCalendarTime(int var1, int var2, int var3, int var4, int var5) {
      BigDecimal var6 = null;
      if (var4 != Integer.MIN_VALUE) {
         if (var4 < 0 || var4 > 1000) {
            throw new IllegalArgumentException("javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendarTime(int hours, int minutes, int seconds, int milliseconds, int timezone)with invalid milliseconds: " + var4);
         }

         var6 = BigDecimal.valueOf((long)var4).movePointLeft(3);
      }

      return this.newXMLGregorianCalendarTime(var1, var2, var3, var6, var5);
   }
}
