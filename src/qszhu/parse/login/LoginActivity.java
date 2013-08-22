
package qszhu.parse.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.squareup.otto.Subscribe;

import qszhu.parse.login.event.CancelLoginEvent;
import qszhu.parse.login.event.CancelSignUpEvent;
import qszhu.parse.login.event.HideProgressEvent;
import qszhu.parse.login.event.LoginEvent;
import qszhu.parse.login.event.ShowProgressEvent;
import qszhu.parse.login.event.ShowSignUpFormEvent;
import qszhu.parse.login.event.ValidationErrorEvent;

public class LoginActivity extends FragmentActivity implements UserBackend, LoginListener,
        SignUpListener {

    private LoginFragment mLoginFragment;
    private SignUpFragment mSignUpFragment;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginFragment = new LoginFragment();
        mSignUpFragment = new SignUpFragment();
        mCurrentFragment = mLoginFragment;

        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, mCurrentFragment)
                .commit();

        // Callbacks
        setUserBackend(this);
        setLoginListener(this);
        setSignUpListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginEventBus.register(mEventHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoginEventBus.unregister(mEventHandler);
    }

    public void setUserBackend(UserBackend backend) {
        mLoginFragment.setUserBackend(backend);
        mSignUpFragment.setUserBackend(backend);
    }

    public void setLoginListener(LoginListener listener) {
        mLoginFragment.setLoginListener(listener);
    }

    public void setSignUpListener(SignUpListener listener) {
        mSignUpFragment.setSignUpListener(listener);
    }

    @Override
    public void login(String username, String password,
            final UserBackend.Callback callback) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null) {
                    callback.error(e);
                } else {
                    callback.success(parseUser);
                }
            }
        });
    }

    @Override
    public void signUp(String username, String password, String email,
            final UserBackend.Callback callback) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    callback.error(e);
                } else {
                    callback.success(null);
                }
            }
        });
    }

    @Override
    public String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case 100:
                return getString(R.string.error_network);
            case 202:
                return getString(R.string.error_duplicate_user);
            case 203:
                return getString(R.string.error_duplicate_email);
            default:
                return getString(R.string.login_error);
        }
    }

    private void handleError(Exception e) {
        ParseException pe = (ParseException) e;
        showErrorDialog(getErrorMessage(pe.getCode()));
    }

    @Override
    public void onLogin(String username, String password) throws Exception {
        if (TextUtils.isEmpty(username)) {
            throw new Exception(getString(R.string.error_username_required));
        }
        if (TextUtils.isEmpty(password)) {
            throw new Exception(getString(R.string.error_password_required));
        }
    }

    @Override
    public void onLoginCompleted(Object user) {
        finish();
    }

    @Override
    public void onLoginError(Exception e) {
        handleError(e);
    }

    @Override
    public void onLoginCancelled() {
    }

    @Override
    public void onSignUp(String username, String password, String email) throws Exception {
        if (TextUtils.isEmpty(username)) {
            throw new Exception(getString(R.string.error_username_required));
        }
        if (TextUtils.isEmpty(password)) {
            throw new Exception(getString(R.string.error_password_required));
        }
    }

    @Override
    public void onSignUpCompleted(String username, String password) {
        popFragment();
        LoginEventBus.post(new LoginEvent(username, password));
    }

    @Override
    public void onSignUpError(Exception e) {
        handleError(e);
    }

    @Override
    public void onSignUpCancelled() {
    }

    @Override
    public void onBackPressed() {
        if (mSignUpFragment.isVisible()) {
            LoginEventBus.post(new CancelSignUpEvent());
            mCurrentFragment = mLoginFragment;
        } else {
            LoginEventBus.post(new CancelLoginEvent());
        }
        super.onBackPressed();
    }

    private void showErrorDialog(String message) {
        showErrorDialog(getString(R.string.error), message);
    }

    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void pushFragment(Fragment frag, boolean replace) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(android.R.id.content, frag);
        if (!replace) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    private void popFragment() {
        getSupportFragmentManager().popBackStack();
    }

    public class EventHandler {
        @Subscribe
        public void onShowProgress(ShowProgressEvent ev) {
            pushFragment(ProgressFragment.getInstance(ev.getMessage()), true);
        }

        @Subscribe
        public void onHideProgress(HideProgressEvent ev) {
            pushFragment(mCurrentFragment, true);
        }

        @Subscribe
        public void onShowSignUpForm(ShowSignUpFormEvent ev) {
            mCurrentFragment = mSignUpFragment;
            pushFragment(mSignUpFragment, false);
        }

        @Subscribe
        public void onValidationError(ValidationErrorEvent ev) {
            showErrorDialog(ev.getException().getMessage());
        }
    }

    private EventHandler mEventHandler = new EventHandler();

}
