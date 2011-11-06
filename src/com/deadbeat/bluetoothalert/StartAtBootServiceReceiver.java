package com.deadbeat.bluetoothalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.deadbeat.bluetoothnotifylib.Globals;

/*
 * Receive notification on device bootup, and start BTNotifyService
 */
public class StartAtBootServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Check received intent
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.d("BluetoothNotify", "--> Received BOOT_COMPLETED");

			// Set global variables
			Globals globals = new Globals();
			globals.setFreeVersion(false);
			globals.setLoggingEnabled(true);

			// Launch service
			Intent svcIntent = new Intent(context, com.deadbeat.bluetoothnotifylib.BTNotifyService.class);
			svcIntent.putExtra("Globals", globals);
			context.startService(svcIntent);
		}
	}
}
