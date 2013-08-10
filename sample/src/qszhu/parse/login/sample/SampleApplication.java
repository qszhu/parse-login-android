
package qszhu.parse.login.sample;

import android.app.Application;

import com.parse.Parse;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // provide application id and client key here
        Parse.initialize(this, getString(R.string.parse_application_id),
                getString(R.string.parse_client_key));
    }

}
