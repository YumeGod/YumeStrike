package de.javasoft.io;

import java.util.EventListener;

public interface FileOperationListener extends EventListener {
   boolean processFileOperationEvent(FileOperationEvent var1);
}
