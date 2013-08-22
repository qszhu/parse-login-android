
package qszhu.parse.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.squareup.otto.Subscribe;

import qszhu.parse.login.event.HideProgressEvent;
import qszhu.parse.login.event.LoginEvent;
import qszhu.parse.login.event.ShowProgressEvent;
import qszhu.parse.login.event.ShowSignUpFormEvent;

public class LoginView extends ViewSwitcher implements OnClickListener {

    private EditText mLoginUsername, mLoginPassword;
    private TextView mProgressMessage;
    private Button mLoginButton, mSignUpButton;

    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mLoginUsername = (EditText) findViewById(R.id.login_username);
        mLoginPassword = (EditText) findViewById(R.id.login_password);
        mProgressMessage = (TextView) findViewById(R.id.progress_message);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);

        mLoginButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginButton) {
            login();
            return;
        }
        if (v == mSignUpButton) {
            showSignUpForm();
            return;
        }
    }

    private void login() {
        final String username = String.valueOf(mLoginUsername.getText());
        final String password = String.valueOf(mLoginPassword.getText());

        LoginEventBus.post(new LoginEvent(username, password));
    }

    private void showSignUpForm() {
        LoginEventBus.post(new ShowSignUpFormEvent());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LoginEventBus.register(mEventHandler);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LoginEventBus.unregister(mEventHandler);
    }

    public class EventHandler {
        @Subscribe
        public void showProgress(ShowProgressEvent event) {
            mProgressMessage.setText(event.getMessage());
            showNext();
        }

        @Subscribe
        public void hideProgress(HideProgressEvent event) {
            showNext();
        }
    }

    private EventHandler mEventHandler = new EventHandler();

}
