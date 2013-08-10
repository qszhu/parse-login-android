
package qszhu.parse.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity implements UserBackend, LoginListener, SignUpListener {

    private static String TAG = LoginActivity.class.getCanonicalName();

    private EditText mLoginUsername, mLoginPassword;
    private EditText mSignUpUsername, mSignUpPassword, mSignUpEmail;
    private View mLoginFormView, mSignUpFormView, mStatusView;
    private TextView mStatusMessage;

    private UserBackend mUserBackend;
    private LoginListener mLoginListener;
    private SignUpListener mSignUpListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI entries
        mLoginFormView = findViewById(R.id.login_form);
        mLoginUsername = (EditText) findViewById(R.id.login_username);
        mLoginPassword = (EditText) findViewById(R.id.login_password);

        mSignUpFormView = findViewById(R.id.sign_up_form);
        mSignUpUsername = (EditText) findViewById(R.id.sign_up_username);
        mSignUpPassword = (EditText) findViewById(R.id.sign_up_password);
        mSignUpEmail = (EditText) findViewById(R.id.sign_up_email);

        mStatusView = findViewById(R.id.login_status);
        mStatusMessage = (TextView) findViewById(R.id.login_status_message);

        // Events
        mLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == R.id.action_login
                        || actionId == EditorInfo.IME_NULL) {
                    login();
                    return true;
                }
                return false;
            }
        });
        mSignUpEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == R.id.action_sign_up
                        || actionId == EditorInfo.IME_NULL) {
                    signUp();
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.log_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.show_sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchView(mLoginFormView, mSignUpFormView);
            }
        });
        findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        // callbacks
        setUserBackend(this);
        setLoginListener(this);
        setSignUpListener(this);
    }

    public void setUserBackend(UserBackend backend) {
        mUserBackend = backend;
    }

    public void setLoginListener(LoginListener listener) {
        mLoginListener = listener;
    }

    public void setSignUpListener(SignUpListener listener) {
        mSignUpListener = listener;
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
    public boolean onLogin(String username, String password) {
        mLoginUsername.setError(null);
        mLoginPassword.setError(null);
        if (TextUtils.isEmpty(username)) {
            mLoginUsername.setError(getString(R.string.error_username_required));
            mLoginUsername.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            mLoginPassword.setError(getString(R.string.error_password_required));
            mLoginPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onLoginCompleted(Object user) {
        finish();
    }

    @Override
    public void onLoginError(Exception e) {
        Log.d(TAG, e.getMessage());
        showErrorDialog(R.string.error, R.string.login_error);
    }

    @Override
    public void onLoginCancelled() {
    }

    @Override
    public boolean onSignUp(String username, String password, String email) {
        mSignUpUsername.setError(null);
        mSignUpPassword.setError(null);
        if (TextUtils.isEmpty(username)) {
            mSignUpUsername.setError(getString(R.string.error_username_required));
            mSignUpUsername.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            mSignUpPassword.setError(getString(R.string.error_password_required));
            mSignUpPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onSignUpCompleted() {
        final String username = String.valueOf(mSignUpUsername.getText());
        final String password = String.valueOf(mSignUpPassword.getText());
        login(username, password);
    }

    @Override
    public void onSignUpError(Exception e) {
        Log.d(TAG, e.getMessage());
        showErrorDialog(R.string.error, R.string.sign_up_error);
    }

    @Override
    public void onSignUpCancelled() {
    }

    @Override
    public void onBackPressed() {
        if (mSignUpFormView.getVisibility() == View.VISIBLE) {
            switchView(mSignUpFormView, mLoginFormView);
            mSignUpListener.onSignUpCancelled();
            return;
        }
        mLoginListener.onLoginCancelled();
        super.onBackPressed();
    }

    private void signUp() {
        final String username = String.valueOf(mSignUpUsername.getText());
        final String password = String.valueOf(mSignUpPassword.getText());
        final String email = String.valueOf(mSignUpEmail.getText());

        if (!mSignUpListener.onSignUp(username, password, email)) {
            return;
        }

        mStatusMessage.setText(R.string.login_progress_signing_up);
        switchView(mSignUpFormView, mStatusView);

        mUserBackend.signUp(username, password, email, new UserBackend.Callback() {
            @Override
            public void success(Object result) {
                mSignUpListener.onSignUpCompleted();
            }

            @Override
            public void error(Exception e) {
                switchView(mStatusView, mSignUpFormView);
                mSignUpListener.onSignUpError(e);
            }
        });
    }

    private void login() {
        final String username = String.valueOf(mLoginUsername.getText());
        final String password = String.valueOf(mLoginPassword.getText());
        login(username, password);
    }

    private void login(String username, String password) {
        if (!mLoginListener.onLogin(username, password)) {
            return;
        }

        mStatusMessage.setText(R.string.login_progress_logging_in);
        switchView(mLoginFormView, mStatusView);

        mUserBackend.login(username, password, new UserBackend.Callback() {
            @Override
            public void success(Object result) {
                switchView(mStatusView, mLoginFormView);
                mLoginListener.onLoginCompleted(result);
            }

            @Override
            public void error(Exception e) {
                switchView(mStatusView, mLoginFormView);
                mLoginListener.onLoginError(e);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void switchView(final View viewToHide, final View viewToShow) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            viewToShow.setVisibility(View.VISIBLE);
            viewToShow.animate()
                    .setDuration(shortAnimTime)
                    .alpha(1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewToShow.setVisibility(View.VISIBLE);
                        }
                    });

            viewToHide.setVisibility(View.VISIBLE);
            viewToHide.animate()
                    .setDuration(shortAnimTime)
                    .alpha(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewToHide.setVisibility(View.GONE);
                        }
                    });
        } else {
            viewToShow.setVisibility(View.VISIBLE);
            viewToHide.setVisibility(View.GONE);
        }
    }

    protected final void showErrorDialog(int title, int message) {
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

}
