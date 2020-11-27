package com.googlecode.yatspec.cucumber;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Grab an application context if it's available so that it can be used in classes
 * that are initialised outside of Spring
 * <p>
 * TODO - maybe this could be automatically loaded via autoconfiguration
 */
@Component
public class SpringBridge implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * This may be null if there is no Spring {@link ApplicationContext} or if this method is invoked
     * before the application context has been set.
     *
     * @return The injected applicationContext or null if the class is not loaded within a Spring context.
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBridge.applicationContext = applicationContext;
    }
}
