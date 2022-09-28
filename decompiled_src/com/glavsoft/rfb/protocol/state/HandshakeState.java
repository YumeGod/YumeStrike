package com.glavsoft.rfb.protocol.state;

import com.glavsoft.exceptions.TransportException;
import com.glavsoft.exceptions.UnsupportedProtocolVersionException;
import com.glavsoft.rfb.protocol.ProtocolContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandshakeState extends ProtocolState {
   public static final String PROTOCOL_VERSION_3_8 = "3.8";
   public static final String PROTOCOL_VERSION_3_7 = "3.7";
   public static final String PROTOCOL_VERSION_3_3 = "3.3";
   private static final int PROTOCOL_STRING_LENGTH = 12;
   private static final String PROTOCOL_STRING_REGEXP = "^RFB (\\d\\d\\d).(\\d\\d\\d)\n$";
   private static final int MIN_SUPPORTED_VERSION_MAJOR = 3;
   private static final int MIN_SUPPORTED_VERSION_MINOR = 3;
   private static final int MAX_SUPPORTED_VERSION_MAJOR = 3;
   private static final int MAX_SUPPORTED_VERSION_MINOR = 8;

   public HandshakeState(ProtocolContext context) {
      super(context);
   }

   public boolean next() throws UnsupportedProtocolVersionException, TransportException {
      this.handshake();
      return true;
   }

   private void handshake() throws TransportException, UnsupportedProtocolVersionException {
      this.logger.info("Waiting to receive protocol string");
      String protocolString = this.reader.readString(12);
      this.logger.info("Server sent protocol string: " + protocolString.substring(0, protocolString.length() - 1));
      Pattern pattern = Pattern.compile("^RFB (\\d\\d\\d).(\\d\\d\\d)\n$");
      Matcher matcher = pattern.matcher(protocolString);
      if (!matcher.matches()) {
         throw new UnsupportedProtocolVersionException("Unsupported protocol version: " + protocolString);
      } else {
         int major = Integer.parseInt(matcher.group(1));
         int minor = Integer.parseInt(matcher.group(2));
         if (major >= 3 && (3 != major || minor >= 3)) {
            if (major > 3) {
               major = 3;
               minor = 8;
            }

            byte minor;
            if (minor >= 3 && minor < 7) {
               this.changeStateTo(new SecurityType33State(this.context));
               this.context.setProtocolVersion("3.3");
               minor = 3;
            } else if (7 == minor) {
               this.changeStateTo(new SecurityType37State(this.context));
               this.context.setProtocolVersion("3.7");
               minor = 7;
            } else {
               if (minor < 8) {
                  throw new UnsupportedProtocolVersionException("Unsupported protocol version: " + protocolString);
               }

               this.changeStateTo(new SecurityTypeState(this.context));
               this.context.setProtocolVersion("3.8");
               minor = 8;
            }

            this.writer.write(("RFB 00" + major + ".00" + minor + "\n").getBytes());
            this.logger.info("Set protocol version to: " + this.context.getProtocolVersion());
         } else {
            throw new UnsupportedProtocolVersionException("Unsupported protocol version: " + major + "." + minor);
         }
      }
   }
}
