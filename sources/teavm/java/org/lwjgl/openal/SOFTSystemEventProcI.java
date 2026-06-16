package org.lwjgl.openal;

import org.lwjgl.system.CallbackI;

public interface SOFTSystemEventProcI extends CallbackI {
    void invoke(long device, int eventType, int deviceType, long event, int length, long message);
}
