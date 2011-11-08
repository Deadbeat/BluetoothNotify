package com.deadbeat.bluetoothalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
 * Receive notification on device bootup, and start BTNotifyService
 */
public class StartAtBootServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Check received intent
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.d("BluetoothNotify", "--> Received BOOT_COMPLETED");
			Intent svcIntent = new Intent(context, com.deadbeat.bluetoothnotifylib.BTNotifyService.class);
			context.startService(svcIntent);
		}
	}
}
