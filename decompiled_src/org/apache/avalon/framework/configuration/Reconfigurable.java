package org.apache.avalon.framework.configuration;

public interface Reconfigurable extends Configurable {
   void reconfigure(Configuration var1) throws ConfigurationException;
}
