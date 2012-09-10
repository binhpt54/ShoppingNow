package com.vinova_g12.shoppingnow.services;
import com.example.shoppingnow.R;
import com.vinova_g12.shoppingnow_app.AddNew;
import com.vinova_g12.shoppingnow_app.MainActivity;

import android.R.bool;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyAlarmService extends Service {
	
	public int id;
	public String SERVICE_ID="start_id";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG)
				.show();
		Log.w("Create service", "");
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG)
				.show();
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG)
				.show();
		Log.w("Start service", "");
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Hello";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText,
				when);

		Context context = getApplicationContext();
		CharSequence contentTitle = "My notification";
		CharSequence contentText = "Hello World!";
		
		//Put id to MainActivity
		Intent notificationIntent = new Intent(MyAlarmService.this,
				MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(SERVICE_ID, id);
		bundle.putString("type", "service");
		notificationIntent.putExtras(bundle);
		
		PendingIntent contentIntent = PendingIntent.getActivity(
				MyAlarmService.this.getBaseContext(), 0, notificationIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		notification.setLatestEventInfo(context, contentTitle,
				contentText, contentIntent);

		final int HELLO_ID = 1;

		mNotificationManager.notify(HELLO_ID, notification);
		
	}
	//Receive Id from AddNew
	/*@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{
	     super.onStartCommand(intent, flags, startId);
	     intent.getIntExtra("service_id",id);
	     Log.w("Service receive ID ", id +"");
	     return 0;
	}*/

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG)
				.show();
		return super.onUnbind(intent);
	}

	
}