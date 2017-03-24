package org.ilay;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TestSessionInitNotifierSupplier implements SessionInitNotifier, Supplier<SessionInitNotifier> {

    private List<SessionInitListener> sessionInitListeners = new ArrayList<>();

    @Override
    public SessionInitNotifier get() {
        return this;
    }

    @Override
    public void addSessionInitListener(SessionInitListener listener) {
        sessionInitListeners.add(listener);
    }

    public void newSession() throws ServiceException {
        for (SessionInitListener sessionInitListener : sessionInitListeners) {
            sessionInitListener.sessionInit(null);
        }
    }
}