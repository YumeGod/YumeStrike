package org.apache.avalon.framework.parameters;

public interface Reparameterizable extends Parameterizable {
   void reparameterize(Parameters var1) throws ParameterException;
}
