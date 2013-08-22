
package qszhu.parse.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import qszhu.parse.login.event.LoginEvent;
import qszhu.parse.login.event.ShowSignUpFormEvent;

public class LoginView extends FrameLayout implements OnClickListener {

    private EditText mLoginUsername, mLoginPassword;
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

}
