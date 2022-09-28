package org.apache.fop.afp.ptoca;

public interface PtocaConstants {
   byte[] ESCAPE = new byte[]{43, -45};
   byte CHAIN_BIT = 1;
   byte SIA = -62;
   byte SVI = -60;
   byte AMI = -58;
   byte RMI = -56;
   byte AMB = -46;
   byte TRN = -38;
   byte DIR = -28;
   byte DBR = -26;
   byte SEC = -128;
   byte SCFL = -16;
   byte STO = -10;
   byte NOP = -8;
   int TRANSPARENT_DATA_MAX_SIZE = 253;
}
