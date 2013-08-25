
package qszhu.parse.login;

import android.content.Context;

import com.squareup.otto.Subscribe;

import qszhu.parse.login.event.CancelSignUpEvent;
import qszhu.parse.login.event.HideProgressEvent;
import qszhu.parse.login.event.ShowProgressEvent;
import qszhu.parse.login.event.SignUpEvent;
import qszhu.parse.login.event.ValidationErrorEvent;

public class SignUpEventHandler {

    private Context mContext;
    private SignUpListener mSignUpListener;
    private UserBackend mUserBackend;

    public SignUpEventHandler(Context context, SignUpListener listener, UserBackend backend) {
        mContext = context;
        mSignUpListener = listener;
        mUserBackend = backend;
    }

    @Subscribe
    public void cancelSignUp(CancelSignUpEvent event) {
        mSignUpListener.onSignUpCancelled();
    }

    @Subscribe
    public void signUp(SignUpEvent event) {
        final String username = event.getUsername();
        final String password = event.getPassword();
        final String email = event.getEmail();

        try {
            mSignUpListener.onSignUp(username, password, email);
        } catch (Exception e) {
            LoginEventBus.post(new ValidationErrorEvent(e));
            return;
        }

        LoginEventBus.post(new ShowProgressEvent(mContext.getString(R.string.progress_signing_up)));

        mUserBackend.signUp(username, password, email, new UserBackend.Callback() {
            @Override
            public void success(Object result) {
                LoginEventBus.post(new HideProgressEvent());
                mSignUpListener.onSignUpCompleted(username, password);
            }

            @Override
            public void error(Exception e) {
                LoginEventBus.post(new HideProgressEvent());
                mSignUpListener.onSignUpError(e);
            }
        });
    }

}
