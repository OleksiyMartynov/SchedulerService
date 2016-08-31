package com.beastpotato.cast.chromcastscheduler.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.beastpotato.cast.chromcastscheduler.managers.CastManager;
import com.beastpotato.cast.chromcastscheduler.managers.DatabaseManager;
import com.beastpotato.cast.chromcastscheduler.models.ScheduledItem;

public class MyIntentService extends IntentService {
    public static final String ACTION_RUN_ITEM = "action_run_item";
    public static final String EXTRA_ITEM_ID = "extra_item_id";

    public MyIntentService() {
        super("MyIntentService");
    }

    public static void startActionRunItem(Context context, int itemId) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_RUN_ITEM);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RUN_ITEM.equals(action)) {
                final int param1 = intent.getIntExtra(EXTRA_ITEM_ID, -1);
                handleActionRunItem(param1);
            }
        }
    }

    private void handleActionRunItem(int itemId) {
        if (itemId != -1) {
            Context context = getApplicationContext();
            ScheduledItem item = DatabaseManager.getInstance(context).getScheduledItem(itemId);
            try {
                CastManager.getInstance(context).playVideo(context, item.url, item.deviceId);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to run scheduled item.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}