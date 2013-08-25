
package qszhu.parse.login.test;

import com.squareup.otto.DeadEvent;
import com.squareup.otto.Subscribe;

public class MockEventHandler {
    private Object mEvent;

    @Subscribe
    public void handler(DeadEvent e) {
        mEvent = e.event;
    }

    public <T> T getEvent() {
        return (T) mEvent;
    }
}
