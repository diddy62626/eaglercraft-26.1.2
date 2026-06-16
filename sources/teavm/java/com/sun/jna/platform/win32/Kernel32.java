package com.sun.jna.platform.win32;

public interface Kernel32 {
    Kernel32 INSTANCE = null;

    int GetCurrentProcessId();
    int GetCurrentThreadId();
    long GetTickCount();
    int GetLastError();
    void SetLastError(int code);
}
