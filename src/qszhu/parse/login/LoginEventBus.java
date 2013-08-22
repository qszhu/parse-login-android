
package qszhu.parse.login;

import com.squareup.otto.Bus;

public class LoginEventBus {

    private static final Bus sBus = new Bus();

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
