package com.gamik.pastify.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.gamik.pastify.R;
import com.gamik.pastify.dialog.AddDialog;
import com.gamik.pastify.dialog.SmartModeDialog;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;

public class OverLayIconActivity extends StandOutWindow {
    ClipboardManager clipboard;
    BroadcastReceiver broadcastReceiver;
    View view;

    @Override
    public String getAppName() {
        return "OverLayListActivity";
    }

    @Override
    public int getAppIcon() {
        return R.drawable.safe;
    }

    @Override
    public void createAndAttachView(int id, FrameLayout frame) {
        // create a new layout from body.xml
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_overlay_icon, frame, true);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ImageView imageView = (ImageView) view.findViewById(R.id.safe);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboard.removePrimaryClipChangedListener(clipChangedListener);
                StandOutWindow
                        .show(getApplicationContext(), OverLayListActivity.class, StandOutWindow.DEFAULT_ID);
            }
        });
        registerReciever();
        clipboard.addPrimaryClipChangedListener(clipChangedListener);
    }

    ClipboardManager.OnPrimaryClipChangedListener clipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            String a = clipboard.getText().toString();
            AddDialog dialog = new AddDialog();
            //dialog.setCallback(dialogCallback);
//            Intent intent = new Intent(view.getContext(), MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            //dialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "additem");
            StandOutWindow.show(getBaseContext(), SmartModeDialog.class, StandOutWindow.DEFAULT_ID);
        }
    };

    // the window will be centered
    @Override
    public StandOutLayoutParams getParams(int id, Window window) {
        return new StandOutLayoutParams(id, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                StandOutLayoutParams.CENTER, StandOutLayoutParams.CENTER);
    }

    // move the window by dragging the view
    @Override
    public int getFlags(int id) {
        return super.getFlags(id) | StandOutFlags.FLAG_BODY_MOVE_ENABLE
                | StandOutFlags.FLAG_WINDOW_FOCUSABLE_DISABLE;
    }

    @Override
    public String getPersistentNotificationMessage(int id) {
        return "Click to close the OverLayListActivity";
    }

    @Override
    public Intent getPersistentNotificationIntent(int id) {
        return StandOutWindow.getCloseIntent(this, OverLayIconActivity.class, id);
    }

    private void registerReciever() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                clipboard.addPrimaryClipChangedListener(clipChangedListener);
            }
        };
        IntentFilter intentFilter = new IntentFilter("smart");
        getBaseContext().registerReceiver(broadcastReceiver, intentFilter);
    }
}
