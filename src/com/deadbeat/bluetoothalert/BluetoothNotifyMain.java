package com.deadbeat.bluetoothalert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.deadbeat.bluetoothnotifylib.AppDetector;
import com.deadbeat.bluetoothnotifylib.BluetoothNotifyWorker;

public class BluetoothNotifyMain extends Activity {

	private final int KILLSVC = Menu.FIRST + 1;
	private BluetoothNotifyWorker worker;

	public BluetoothNotifyWorker getWorker() {
		return this.worker;
	}

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle btNotify) {
		super.onCreate(btNotify);
		setWorker(new BluetoothNotifyWorker(this));
		getWorker().doLog("-------------------------------");
		getWorker().doLog("--> " + getWorker().getTimestamp() + " Client starting up");

		// Check for free version installed
		// If installed, we can not continue to run because of service conflict
		AppDetector detect = new AppDetector();
		if (detect.isAppInstalled(this, "com.deadbeat.bluetoothalertfree") == true) {
			getWorker().shutdownOnConflict();
		}

		// Start service
		getWorker().startBTNotifyService();

		// Display device list
		setContentView(R.layout.deviceselect);
		getWorker().getBTDevices();
		getWorker().buildDeviceListView(detect.getVersion(this, this.getClass().getPackage().getName()));
	}

	/*
	 * Create options menu Menu includes one option to shutdown app and kill
	 * service
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, this.KILLSVC, 0, "Stop Bluetooth Notify Service (Not Recommended)");
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * Option selected
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case KILLSVC:
			// Prompt user for confirmation before shutting down
			DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int selectedButton) {
					switch (selectedButton) {
					case DialogInterface.BUTTON_POSITIVE:
						// User is an Idiot, and said "Yes"
						// Kill service
						Intent svc = new Intent(getBaseContext(), com.deadbeat.bluetoothnotifylib.BTNotifyService.class);
						stopService(svc);
						finish();
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						// User said "No".
						break;
					}

				}
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Are you sure you want to stop the service?\n\n"
							+ "Without BluetoothNotify Service, you will not recieve ANY device notifications.\n\n"
							+ "NOTE: The service will be restarted when you re-open this application.")
					.setPositiveButton("Yes", confirmListener).setNegativeButton("No", confirmListener).show();
		}
		return super.onOptionsItemSelected(item);
	}

	public void setWorker(BluetoothNotifyWorker worker) {
		this.worker = worker;
	}
}
