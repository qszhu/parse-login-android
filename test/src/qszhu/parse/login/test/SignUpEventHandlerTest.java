
package qszhu.parse.login.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.test.AndroidTestCase;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import qszhu.parse.login.LoginEventBus;
import qszhu.parse.login.SignUpEventHandler;
import qszhu.parse.login.SignUpListener;
import qszhu.parse.login.UserBackend;
import qszhu.parse.login.UserBackend.Callback;
import qszhu.parse.login.event.CancelSignUpEvent;
import qszhu.parse.login.event.SignUpEvent;

public class SignUpEventHandlerTest extends AndroidTestCase {

    private SignUpListener mMockListener;
    private UserBackend mMockBackend;
    private SignUpEventHandler mHandler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMockListener = mock(SignUpListener.class);
        mMockBackend = mock(UserBackend.class);
        mHandler = new SignUpEventHandler(getContext(), mMockListener, mMockBackend);
        LoginEventBus.register(mHandler);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        LoginEventBus.unregister(mHandler);
    }

    public void testCancelSignUp() {
        LoginEventBus.post(new CancelSignUpEvent());

        verify(mMockListener).onSignUpCancelled();
        verifyNoMoreInteractions(mMockListener);
    }

    public void testSignUpBackend() {
        LoginEventBus.post(new SignUpEvent("foo", "bar", "foo@bar.com"));

        verify(mMockBackend).signUp(eq("foo"), eq("bar"), eq("foo@bar.com"), any(Callback.class));
        verifyNoMoreInteractions(mMockBackend);
    }

    public void testSignUpValidationError() throws Exception {
        doThrow(new Exception()).when(mMockListener)
                .onSignUp(anyString(), anyString(), anyString());

        LoginEventBus.post(new SignUpEvent("foo", "bar", "foo@bar.com"));

        verify(mMockListener).onSignUp("foo", "bar", "foo@bar.com");
        verifyNoMoreInteractions(mMockListener);
    }

    private void setBackendSignUpResult(UserBackend backend, final boolean success) {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Callback callback = (Callback) invocation.getArguments()[3];
                if (success) {
                    callback.success(null);
                } else {
                    callback.error(new Exception());
                }
                return null;
            }
        }).when(backend).signUp(anyString(), anyString(), anyString(), any(Callback.class));
    }

    public void testSignUpSuccess() throws Exception {
        setBackendSignUpResult(mMockBackend, true);

        LoginEventBus.post(new SignUpEvent("foo", "bar", "foo@bar.com"));

        verify(mMockListener).onSignUp("foo", "bar", "foo@bar.com");
        verify(mMockListener).onSignUpCompleted("foo", "bar");
        verifyNoMoreInteractions(mMockListener);
    }

    public void testSignUpError() throws Exception {
        setBackendSignUpResult(mMockBackend, false);

        LoginEventBus.post(new SignUpEvent("foo", "bar", "foo@bar.com"));

        verify(mMockListener).onSignUp("foo", "bar", "foo@bar.com");
        verify(mMockListener).onSignUpError(any(Exception.class));
        verifyNoMoreInteractions(mMockListener);
    }

}
