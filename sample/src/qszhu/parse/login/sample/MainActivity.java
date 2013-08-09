
package qszhu.parse.login.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.parse.Parse;
import com.parse.ParseUser;

import qszhu.parse.login.LoginActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // provide application id and client key here
        Parse.initialize(this, getString(R.string.parse_application_id),
                getString(R.string.parse_client_key));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for testing purpose, remove this line in production
        ParseUser.logOut();

        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
