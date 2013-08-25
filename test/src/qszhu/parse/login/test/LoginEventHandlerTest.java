
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
import qszhu.parse.login.LoginEventHandler;
import qszhu.parse.login.LoginListener;
import qszhu.parse.login.UserBackend;
import qszhu.parse.login.UserBackend.Callback;
import qszhu.parse.login.event.CancelLoginEvent;
import qszhu.parse.login.event.LoginEvent;

public class LoginEventHandlerTest extends AndroidTestCase {

    private LoginListener mMockListener;
    private UserBackend mMockBackend;
    private LoginEventHandler mHandler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMockListener = mock(LoginListener.class);
        mMockBackend = mock(UserBackend.class);
        mHandler = new LoginEventHandler(getContext(), mMockListener, mMockBackend);
        LoginEventBus.register(mHandler);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        LoginEventBus.unregister(mHandler);
    }

    public void testCancelLogin() {
        LoginEventBus.post(new CancelLoginEvent());

        verify(mMockListener).onLoginCancelled();
        verifyNoMoreInteractions(mMockListener);
    }

    public void testLoginBackend() {
        LoginEventBus.post(new LoginEvent("foo", "bar"));

        verify(mMockBackend).login(eq("foo"), eq("bar"), any(Callback.class));
        verifyNoMoreInteractions(mMockBackend);
    }

    public void testLoginValidationError() throws Exception {
        doThrow(new Exception()).when(mMockListener).onLogin(anyString(), anyString());

        LoginEventBus.post(new LoginEvent("foo", "bar"));

        verify(mMockListener).onLogin("foo", "bar");
        verifyNoMoreInteractions(mMockListener);
    }

    private void setBackendLoginResult(UserBackend backend, final boolean success) {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Callback callback = (Callback) invocation.getArguments()[2];
                if (success) {
                    callback.success(null);
                } else {
                    callback.error(new Exception());
                }
                return null;
            }
        }).when(backend).login(anyString(), anyString(), any(Callback.class));
    }

    public void testLoginSuccess() throws Exception {
        setBackendLoginResult(mMockBackend, true);

        LoginEventBus.post(new LoginEvent("foo", "bar"));

        verify(mMockListener).onLogin("foo", "bar");
        verify(mMockListener).onLoginCompleted(any());
        verifyNoMoreInteractions(mMockListener);
    }

    public void testLoginError() throws Exception {
        setBackendLoginResult(mMockBackend, false);

        LoginEventBus.post(new LoginEvent("foo", "bar"));

        verify(mMockListener).onLogin("foo", "bar");
        verify(mMockListener).onLoginError(any(Exception.class));
        verifyNoMoreInteractions(mMockListener);
    }

}
