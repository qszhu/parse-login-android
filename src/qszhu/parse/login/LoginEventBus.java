
package qszhu.parse.login;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class LoginEventBus {

    private static final Bus sBus = new Bus(ThreadEnforcer.ANY);

    public static void post(Object ev) {
        sBus.post(ev);
    }

    public static void register(Object subscriber) {
        sBus.register(subscriber);
    }

    public static void unregister(Object subscriber) {
        sBus.unregister(subscriber);
    }

}
