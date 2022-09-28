package org.apache.avalon.framework;

import java.util.Map;

public abstract class ValuedEnum extends Enum {
   private final int m_value;

   protected ValuedEnum(String name, int value) {
      this(name, value, (Map)null);
   }

   protected ValuedEnum(String name, int value, Map map) {
      super(name, map);
      this.m_value = value;
   }

   public final int getValue() {
      return this.m_value;
   }

   public final boolean isEqualTo(ValuedEnum other) {
      return this.m_value == other.m_value;
   }

   public final boolean isGreaterThan(ValuedEnum other) {
      return this.m_value > other.m_value;
   }

   public final boolean isGreaterThanOrEqual(ValuedEnum other) {
      return this.m_value >= other.m_value;
   }

   public final boolean isLessThan(ValuedEnum other) {
      return this.m_value < other.m_value;
   }

   public final boolean isLessThanOrEqual(ValuedEnum other) {
      return this.m_value <= other.m_value;
   }

   public String toString() {
      return this.getClass().getName() + "[" + this.getName() + "=" + this.m_value + "]";
   }
}
