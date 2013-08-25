
package qszhu.parse.login.test;

import android.test.AndroidTestCase;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import qszhu.parse.login.LoginEventBus;
import qszhu.parse.login.LoginView;
import qszhu.parse.login.R;
import qszhu.parse.login.event.LoginEvent;
import qszhu.parse.login.event.ShowSignUpFormEvent;

public class LoginViewTest extends AndroidTestCase {

    private MockEventHandler mEventHandler;
    private LoginView mLoginView;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLoginView = (LoginView) LayoutInflater.from(getContext()).inflate(R.layout.login, null);

        mEventHandler = new MockEventHandler();
        LoginEventBus.register(mEventHandler);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        LoginEventBus.unregister(mEventHandler);
    }

    public void testTriggerLogin() {
        ((EditText) mLoginView.findViewById(R.id.login_username)).setText("foo");
        ((EditText) mLoginView.findViewById(R.id.login_password)).setText("bar");
        ((Button) mLoginView.findViewById(R.id.login_button)).performClick();

        LoginEvent ev = mEventHandler.getEvent();
        assertEquals("foo", ev.getUsername());
        assertEquals("bar", ev.getPassword());
    }

    public void testShowSignUp() {
        ((Button) mLoginView.findViewById(R.id.sign_up_button)).performClick();

        assertTrue(mEventHandler.getEvent() instanceof ShowSignUpFormEvent);
    }

}
