package org.apache.bcel.verifier;

public abstract class NativeVerifier {
   private NativeVerifier() {
   }

   public static void main(String[] args) {
      if (args.length != 1) {
         System.out.println("Verifier front-end: need exactly one argument.");
         System.exit(1);
      }

      int dotclasspos = args[0].lastIndexOf(".class");
      if (dotclasspos != -1) {
         args[0] = args[0].substring(0, dotclasspos);
      }

      args[0] = args[0].replace('/', '.');

      try {
         Class.forName(args[0]);
      } catch (ExceptionInInitializerError var6) {
         System.out.println("NativeVerifier: ExceptionInInitializerError encountered on '" + args[0] + "'.");
         System.out.println(var6);
         System.exit(1);
      } catch (LinkageError var7) {
         System.out.println("NativeVerifier: LinkageError encountered on '" + args[0] + "'.");
         System.out.println(var7);
         System.exit(1);
      } catch (ClassNotFoundException var8) {
         System.out.println("NativeVerifier: FILE NOT FOUND: '" + args[0] + "'.");
         System.exit(1);
      } catch (Throwable var9) {
         System.out.println("NativeVerifier: Unspecified verification error on'" + args[0] + "'.");
         System.exit(1);
      }

      System.out.println("NativeVerifier: Class file '" + args[0] + "' seems to be okay.");
      System.exit(0);
   }
}
