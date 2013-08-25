
package qszhu.parse.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginFragment extends Fragment {

    private LoginView mLoginView;

    private LoginListener mLoginListener;
    private UserBackend mUserBackend;
    private LoginEventHandler mEventHandler;

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
        if (mEventHandler == null) {
            if (mLoginListener != null && mUserBackend != null) {
                mEventHandler = new LoginEventHandler(activity, mLoginListener, mUserBackend);
            }
        }
        if (mEventHandler != null) {
            LoginEventBus.register(mEventHandler);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mEventHandler != null) {
            LoginEventBus.unregister(mEventHandler);
        }
    }

}
