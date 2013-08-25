
package qszhu.parse.login;

import android.content.Context;

import com.squareup.otto.Subscribe;

import qszhu.parse.login.event.CancelLoginEvent;
import qszhu.parse.login.event.HideProgressEvent;
import qszhu.parse.login.event.LoginEvent;
import qszhu.parse.login.event.ShowProgressEvent;
import qszhu.parse.login.event.ValidationErrorEvent;

public class LoginEventHandler {

    private Context mContext;
    private LoginListener mLoginListener;
    private UserBackend mUserBackend;

    public LoginEventHandler(Context context, LoginListener listener, UserBackend backend) {
        mContext = context;
        mLoginListener = listener;
        mUserBackend = backend;
    }

    @Subscribe
    public void cancelLogin(CancelLoginEvent event) {
        if (mLoginListener == null) {
            return;
        }
        mLoginListener.onLoginCancelled();
    }

    @Subscribe
    public void login(LoginEvent event) {
        if (mLoginListener == null || mUserBackend == null) {
            return;
        }

        final String username = event.getUsername();
        final String password = event.getPassword();

        try {
            mLoginListener.onLogin(username, password);
        } catch (Exception e) {
            LoginEventBus.post(new ValidationErrorEvent(e));
            return;
        }

        LoginEventBus.post(new ShowProgressEvent(mContext.getString(R.string.progress_logging_in)));

        mUserBackend.login(username, password, new UserBackend.Callback() {
            @Override
            public void success(Object result) {
                LoginEventBus.post(new HideProgressEvent());
                mLoginListener.onLoginCompleted(result);
            }

            @Override
            public void error(Exception e) {
                LoginEventBus.post(new HideProgressEvent());
                mLoginListener.onLoginError(e);
            }
        });
    }

}
