
package qszhu.parse.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import qszhu.parse.login.event.SignUpEvent;

public class SignUpView extends FrameLayout implements OnClickListener {

    private EditText mSignUpUsername, mSignUpPassword, mSignUpEmail;
    private Button mSignUpButton;

    public SignUpView(Context context) {
        super(context);
    }

    public SignUpView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mSignUpUsername = (EditText) findViewById(R.id.sign_up_username);
        mSignUpPassword = (EditText) findViewById(R.id.sign_up_password);
        mSignUpEmail = (EditText) findViewById(R.id.sign_up_email);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);

        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSignUpButton) {
            signUp();
            return;
        }
    }

    private void signUp() {
        final String username = String.valueOf(mSignUpUsername.getText());
        final String password = String.valueOf(mSignUpPassword.getText());
        final String email = String.valueOf(mSignUpEmail.getText());

        LoginEventBus.post(new SignUpEvent(username, password, email));
    }

}
