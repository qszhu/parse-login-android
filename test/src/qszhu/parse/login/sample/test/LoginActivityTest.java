
package qszhu.parse.login.sample.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;
import com.parse.ParseException;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import qszhu.parse.login.LoginActivity;
import qszhu.parse.login.LoginListener;
import qszhu.parse.login.R;
import qszhu.parse.login.SignUpListener;
import qszhu.parse.login.UserBackend;
import qszhu.parse.login.UserBackend.Callback;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

//    /**
//     * should show the login form initially
//     */
//    public void testViewVisibility() {
//        assertEquals(solo.getView(R.id.login_form).getVisibility(), View.VISIBLE);
//        assertEquals(solo.getView(R.id.sign_up_form).getVisibility(), View.GONE);
//    }
//
//    /**
//     * should reveal the sign up form when clicked
//     */
//    public void testShowSignUpView() {
//        solo.clickOnButton("Sign up");
//        solo.sleep(1000);
//        assertEquals(solo.getView(R.id.login_form).getVisibility(), View.GONE);
//        assertEquals(solo.getView(R.id.sign_up_form).getVisibility(), View.VISIBLE);
//    }
//
//    /**
//     * should reveal the login form when back from sign up form
//     */
//    public void testBackFromSignUp() {
//        solo.clickOnButton("Sign up");
//        solo.sleep(1000);
//        solo.goBack();
//        solo.sleep(1000);
//        assertEquals(solo.getView(R.id.login_form).getVisibility(), View.VISIBLE);
//        assertEquals(solo.getView(R.id.sign_up_form).getVisibility(), View.GONE);
//    }
//
//    private void login(String username, String password) {
//        solo.enterText((EditText) solo.getView(R.id.login_username), username);
//        solo.enterText((EditText) solo.getView(R.id.login_password), password);
//        solo.clickOnButton("Log in");
//    }
//
//    /**
//     * should invoke backend when login
//     */
//    public void testLogin() {
//        UserBackend backend = mock(UserBackend.class);
//        getActivity().setUserBackend(backend);
//
//        login("foo", "bar");
//
//        verify(backend).login(eq("foo"), eq("bar"), any(Callback.class));
//        verifyNoMoreInteractions(backend);
//    }
//
//    /**
//     * should invoke cancel callback when cancel login
//     */
//    public void testLoginCancel() {
//        LoginListener listener = mock(LoginListener.class);
//        getActivity().setLoginListener(listener);
//
//        solo.goBack();
//
//        verify(listener).onLoginCancelled();
//        verifyNoMoreInteractions(listener);
//    }
//
//    private LoginListener getMockLoginListener(boolean proceed) {
//        LoginListener listener = mock(LoginListener.class);
//        when(listener.onLogin(anyString(), anyString())).thenReturn(proceed);
//        return listener;
//    }
//
//    /**
//     * should not proceed if onLogin() returns false
//     */
//    public void testLoginBlock() {
//        LoginListener listener = getMockLoginListener(false);
//        getActivity().setLoginListener(listener);
//
//        login("foo", "bar");
//
//        verify(listener).onLogin("foo", "bar");
//        verifyNoMoreInteractions(listener);
//    }
//
//    private UserBackend getMockLoginBackend(final boolean success, final int errorCode) {
//        UserBackend backend = mock(UserBackend.class);
//        doAnswer(new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                Callback callback = (Callback) invocation.getArguments()[2];
//                if (success) {
//                    callback.success(null);
//                } else {
//                    ParseException e = new ParseException(errorCode, "error");
//                    callback.error(e);
//                }
//                return null;
//            }
//        }).when(backend).login(anyString(), anyString(), any(Callback.class));
//        return backend;
//    }
//
//    /**
//     * should invoke success callback when login succeeded
//     */
//    public void testLoginSuccess() {
//        UserBackend backend = getMockLoginBackend(true, 0);
//        getActivity().setUserBackend(backend);
//        LoginListener listener = getMockLoginListener(true);
//        getActivity().setLoginListener(listener);
//
//        login("foo", "bar");
//
//        verify(listener).onLogin("foo", "bar");
//        verify(listener).onLoginCompleted(any());
//        verifyNoMoreInteractions(listener);
//    }
//
//    /**
//     * should invoke error callback when login failed
//     */
//    public void testLoginError() {
//        UserBackend backend = getMockLoginBackend(false, 0);
//        getActivity().setUserBackend(backend);
//        LoginListener listener = getMockLoginListener(true);
//        getActivity().setLoginListener(listener);
//
//        login("foo", "bar");
//
//        verify(listener).onLogin("foo", "bar");
//        verify(listener).onLoginError(any(Exception.class));
//        verifyNoMoreInteractions(listener);
//    }
//
//    private void signUp(String username, String password, String email) {
//        solo.clickOnButton("Sign up");
//        solo.sleep(1000);
//        solo.enterText((EditText) solo.getView(R.id.sign_up_username), username);
//        solo.enterText((EditText) solo.getView(R.id.sign_up_password), password);
//        solo.enterText((EditText) solo.getView(R.id.sign_up_email), email);
//        solo.clickOnButton("Sign up");
//    }
//
//    /**
//     * should invoke backend when sign up
//     */
//    public void testSignUp() {
//        UserBackend backend = mock(UserBackend.class);
//        getActivity().setUserBackend(backend);
//
//        signUp("foo", "bar", "foo@bar.com");
//
//        verify(backend).signUp(eq("foo"), eq("bar"), eq("foo@bar.com"), any(Callback.class));
//    }
//
//    /**
//     * should invoke cancel callback when cancel sign up
//     */
//    public void testSignUpCancel() {
//        SignUpListener listener = mock(SignUpListener.class);
//        getActivity().setSignUpListener(listener);
//
//        solo.clickOnButton("Sign up");
//        solo.sleep(1000);
//        solo.goBack();
//
//        verify(listener).onSignUpCancelled();
//        verifyNoMoreInteractions(listener);
//    }
//
//    private SignUpListener getMockSignUpListener(boolean proceed) {
//        SignUpListener listener = mock(SignUpListener.class);
//        when(listener.onSignUp(anyString(), anyString(), anyString())).thenReturn(proceed);
//        return listener;
//    }
//
//    /**
//     * should not proceed sign up when onSignUp() returns false
//     */
//    public void testSignUpBlock() {
//        SignUpListener listener = getMockSignUpListener(false);
//        getActivity().setSignUpListener(listener);
//
//        signUp("foo", "bar", "foo@bar.com");
//
//        verify(listener).onSignUp("foo", "bar", "foo@bar.com");
//        verifyNoMoreInteractions(listener);
//    }
//
//    private UserBackend getMockSignUpBackend(final boolean success, final int errorCode) {
//        UserBackend backend = mock(UserBackend.class);
//        doAnswer(new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                Callback callback = (Callback) invocation.getArguments()[3];
//                if (success) {
//                    callback.success(null);
//                } else {
//                    ParseException e = new ParseException(errorCode, "error");
//                    callback.error(e);
//                }
//                return null;
//            }
//        }).when(backend).signUp(anyString(), anyString(), anyString(), any(Callback.class));
//        return backend;
//    }
//
//    /**
//     * should invoke success callback when sign up succeeded
//     */
//    public void testSignUpSuccess() {
//        UserBackend backend = getMockSignUpBackend(true, 0);
//        getActivity().setUserBackend(backend);
//        SignUpListener listener = getMockSignUpListener(true);
//        getActivity().setSignUpListener(listener);
//
//        signUp("foo", "bar", "foo@bar.com");
//
//        verify(listener).onSignUp("foo", "bar", "foo@bar.com");
//        verify(listener).onSignUpCompleted();
//        verifyNoMoreInteractions(listener);
//    }
//
//    /**
//     * should invoke error callback when sign up failed
//     */
//    public void testSignUpError() {
//        UserBackend backend = getMockSignUpBackend(false, 0);
//        getActivity().setUserBackend(backend);
//        SignUpListener listener = getMockSignUpListener(true);
//        getActivity().setSignUpListener(listener);
//
//        signUp("foo", "bar", "foo@bar.com");
//
//        verify(listener).onSignUp("foo", "bar", "foo@bar.com");
//        verify(listener).onSignUpError(any(Exception.class));
//        verifyNoMoreInteractions(listener);
//    }
//
//    /**
//     * should invoke backend when login failed
//     */
//    public void testLoginGetError() {
//        UserBackend backend = getMockLoginBackend(false, 400);
//        getActivity().setUserBackend(backend);
//
//        login("foo", "bar");
//        verify(backend).getErrorMessage(400);
//    }
//
//    /**
//     * should invoke backend when sign up failed
//     */
//    public void testSignUpGetError() {
//        UserBackend backend = getMockSignUpBackend(false, 401);
//        getActivity().setUserBackend(backend);
//
//        signUp("foo", "bar", "foo@bar.com");
//        verify(backend).getErrorMessage(401);
//    }

}
