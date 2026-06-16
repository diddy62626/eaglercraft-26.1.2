package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.script.ScriptManager;

/**
 * EaglerCraft stub for log4j2 Configuration interface.
 */
public interface Configuration {
    org.apache.logging.log4j.core.LoggerContext getLoggerContext();
    default String getConfigurationSource() { return "EaglerCraft"; }
    default void setupScriptManager(ScriptManager scriptManager) {}
}
