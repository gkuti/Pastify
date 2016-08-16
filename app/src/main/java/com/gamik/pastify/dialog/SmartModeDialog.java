package com.gamik.pastify.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gamik.pastify.R;
import com.gamik.pastify.activity.OverLayIconActivity;
import com.gamik.pastify.callback.DialogCallback;
import com.gamik.pastify.model.DataItem;
import com.gamik.pastify.store.DataStore;
import com.gamik.pastify.store.Store;
import com.gamik.pastify.util.Date;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;

public class SmartModeDialog extends StandOutWindow {

    private AlertDialog dialog;

    private TextView placeHolder;

    private TextView value;

    private TextView label;

    private Button cancelButton;

    private Button addButton;

    private TextInputLayout inputLayoutPlaceHolder;

    private TextInputLayout inputLayoutValue;

    private int operation = 0;

    private int id;

    private Store store;

    private DataStore dataStore;

    private String placeHolderText;

    private View view;

//        inputLayoutPlaceHolder = (TextInputLayout) rootView.findViewById(R.id.input_layout_name);
//        inputLayoutValue = (TextInputLayout) rootView.findViewById(R.id.input_layout_description);
//        placeHolder = (TextView) rootView.findViewById(R.id.placeholder);
//        value = (TextView) rootView.findViewById(R.id.value);
//        label = (TextView) rootView.findViewById(R.id.label);
//        //cancelButton = (Button) rootView.findViewById(R.id.btn_cancel_purchase);
//        placeHolder.addTextChangedListener(new MyTextWatcher(placeHolder));
//        value.addTextChangedListener(new MyTextWatcher(value));
//        //addButton = (Button) rootView.findViewById(R.id.btn_add_purchase);
//        if (!valueText.equals("")) {
//            operation = 1;
//            placeHolder.setText(placeHolderText);
//            value.setText(valueText);
//            label.setText("EDIT ITEM");
//        }
//    }
//
//    private DataItem getDataFromViews() {
//        DataItem item = new DataItem(placeHolder.getText().toString(), value.getText().toString(), id, 0, Date.getDate(), 0);
//        return item;
//    }
//
//    private boolean validateName() {
//        if (placeHolder.getText().toString().trim().isEmpty()) {
//            inputLayoutPlaceHolder.setError("Item name cannot be empty");
//            return false;
//        } else {
//            if (operation == 1 && !placeHolderText.equals(placeHolder.getText().toString())) {
//                Cursor cursor = store.getData(placeHolder.getText().toString().trim());
//                if (cursor.getCount() > 0) {
//                    inputLayoutPlaceHolder.setError("PlaceHolder already exist");
//                    return false;
//                }
//            } else if (operation == 0) {
//                Cursor cursor = store.getData(placeHolder.getText().toString().trim());
//                if (cursor.getCount() > 0) {
//                    inputLayoutPlaceHolder.setError("PlaceHolder already exist");
//                    return false;
//                }
//            } else
//                inputLayoutValue.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//    private boolean validateDescription() {
//        String description = value.getText().toString().trim();
//        if (description.isEmpty()) {
//            inputLayoutValue.setError("Item description cannot be empty");
//            return false;
//        } else {
//            inputLayoutValue.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//    private void submitForm() {
//        int check = 0;
//        if (validateName()) {
//            check++;
//        }
//        if (validateDescription()) {
//            check++;
//        }
//        if (check == 2) {
//            Store store = new Store(getContext());
//            DataItem dataItem = getDataFromViews();
//            if (operation == 1) {
//                store.updateDataById(dataItem.getPlaceHolder(), dataItem.getValue(), id);
//            } else {
//                store.saveData(dataItem.getPlaceHolder(), dataItem.getValue(), dataItem.getDate());
//            }
//        }
//    }

    @Override
    public String getAppName() {
        return null;
    }

    @Override
    public int getAppIcon() {
        return 0;
    }

    @Override
    public void createAndAttachView(int id, FrameLayout frame) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.smart_mode_dialog, frame, true);
        Button cancelButton = (Button) view.findViewById(R.id.button_cancel);
        Button okButton = (Button) view.findViewById(R.id.button_ok);
        EditText valueEditText = (EditText) view.findViewById(R.id.value);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        valueEditText.setText(clipboardManager.getText().toString());
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //StandOutWindow.closeAll(getBaseContext(), SmartModeDialog.class);
                closeAll();
            }
        });
    }

    @Override
    public int getThemeStyle() {
        return R.style.AppTheme;
    }

    @Override
    public StandOutLayoutParams getParams(int id, Window window) {
        dataStore = new DataStore(getBaseContext());
        int height = (int) (Integer.valueOf(dataStore.getData("height")) * 0.5);
        int width = (int) (Integer.valueOf(dataStore.getData("width")) * 0.66);
        return new StandOutLayoutParams(id, width, height,
                StandOutLayoutParams.CENTER, StandOutLayoutParams.CENTER);
    }

    @Override
    public int getFlags(int id) {
        return super.getFlags(id)
                | StandOutFlags.FLAG_WINDOW_FOCUSABLE_DISABLE | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    @Override
    public String getPersistentNotificationMessage(int id) {
        return "Click to close the OverLayListActivity";
    }

    @Override
    public Intent getPersistentNotificationIntent(int id) {
        return StandOutWindow.getCloseIntent(this, SmartModeDialog.class, id);
    }
//    private class MyTextWatcher implements TextWatcher {
//
//        private View view;
//
//        private MyTextWatcher(View view) {
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//                case R.id.placeholder:
//                    validateName();
//                    break;
//                case R.id.value:
//                    validateDescription();
//                    break;
//            }
//        }
//    }
}

