package de.javasoft.util;

public interface IVersion {
   int getMajor();

   int getMinor();

   int getRevision();

   int getBuild();

   String toString();
}
