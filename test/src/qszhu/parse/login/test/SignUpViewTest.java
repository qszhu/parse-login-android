
package qszhu.parse.login.test;

import android.test.AndroidTestCase;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import qszhu.parse.login.LoginEventBus;
import qszhu.parse.login.R;
import qszhu.parse.login.SignUpView;
import qszhu.parse.login.event.SignUpEvent;

public class SignUpViewTest extends AndroidTestCase {

    private MockEventHandler mEventHandler;
    private SignUpView mSignUpView;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSignUpView = (SignUpView) LayoutInflater.from(getContext())
                .inflate(R.layout.sign_up, null);

        mEventHandler = new MockEventHandler();
        LoginEventBus.register(mEventHandler);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        LoginEventBus.unregister(mEventHandler);
    }

    public void testTriggerSignUp() {
        ((EditText) mSignUpView.findViewById(R.id.sign_up_username)).setText("foo");
        ((EditText) mSignUpView.findViewById(R.id.sign_up_password)).setText("bar");
        ((EditText) mSignUpView.findViewById(R.id.sign_up_email)).setText("foo@bar.com");
        ((Button) mSignUpView.findViewById(R.id.sign_up_button)).performClick();

        SignUpEvent ev = mEventHandler.getEvent();
        assertEquals("foo", ev.getUsername());
        assertEquals("bar", ev.getPassword());
        assertEquals("foo@bar.com", ev.getEmail());
    }

}
