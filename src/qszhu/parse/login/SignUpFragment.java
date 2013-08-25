
package qszhu.parse.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SignUpFragment extends Fragment {

    private SignUpView mSignUpView;

    private SignUpListener mSignUpListener;
    private UserBackend mUserBackend;
    private SignUpEventHandler mEventHandler;

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
        if (mEventHandler == null) {
            if (mSignUpListener != null && mUserBackend != null) {
                mEventHandler = new SignUpEventHandler(activity, mSignUpListener, mUserBackend);
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
