
package qszhu.parse.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import qszhu.parse.login.event.CancelLoginEvent;
import qszhu.parse.login.event.HideProgressEvent;
import qszhu.parse.login.event.LoginEvent;
import qszhu.parse.login.event.ShowProgressEvent;
import qszhu.parse.login.event.ValidationErrorEvent;

public class LoginFragment extends Fragment {

    private LoginView mLoginView;

    private LoginListener mLoginListener;
    private UserBackend mUserBackend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLoginView = (LoginView) inflater.inflate(R.layout.login, container, false);
        return mLoginView;
    }

    public void setLoginListener(LoginListener listener) {
        mLoginListener = listener;
    }

    public void setUserBackend(UserBackend backend) {
        mUserBackend = backend;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LoginEventBus.register(mEventHandler);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LoginEventBus.unregister(mEventHandler);
    }

    public class EventHandler {
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

            LoginEventBus.post(new ShowProgressEvent(getActivity().getString(
                    R.string.progress_logging_in)));

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

    private EventHandler mEventHandler = new EventHandler();
}
