/**
 * @author optimus
 */
package android.custommapcomponent.utilities;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * Just a sample activity to fulfill the use case where user can switch
 * to another activity on click of button present in balloon
 * 
 */
public class NewFormActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(android.custommapcomponent.R.layout.new_form);
	}

}
