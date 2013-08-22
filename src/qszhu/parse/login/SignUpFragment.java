
package qszhu.parse.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import qszhu.parse.login.event.CancelSignUpEvent;
import qszhu.parse.login.event.HideProgressEvent;
import qszhu.parse.login.event.ShowProgressEvent;
import qszhu.parse.login.event.SignUpEvent;
import qszhu.parse.login.event.ValidationErrorEvent;

public class SignUpFragment extends Fragment {

    private SignUpView mSignUpView;

    private SignUpListener mSignUpListener;
    private UserBackend mUserBackend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSignUpView = (SignUpView) inflater.inflate(R.layout.sign_up, container, false);
        return mSignUpView;
    }

    public void setSignUpListener(SignUpListener listener) {
        mSignUpListener = listener;
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
        public void cancelSignUp(CancelSignUpEvent event) {
            if (mSignUpListener == null) {
                return;
            }
            mSignUpListener.onSignUpCancelled();
        }

        @Subscribe
        public void signUp(SignUpEvent event) {
            if (mSignUpListener == null || mUserBackend == null) {
                return;
            }

            final String username = event.getUsername();
            final String password = event.getPassword();
            final String email = event.getEmail();

            try {
                mSignUpListener.onSignUp(username, password, email);
            } catch (Exception e) {
                LoginEventBus.post(new ValidationErrorEvent(e));
                return;
            }

            LoginEventBus.post(new ShowProgressEvent(getActivity().getString(
                    R.string.progress_signing_up)));

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

    private EventHandler mEventHandler = new EventHandler();
}
