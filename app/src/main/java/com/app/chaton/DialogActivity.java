package com.app.chaton;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.app.chaton.API_helpers.CallService;
import com.app.chaton.API_helpers.MessageResponseObject;
import com.app.chaton.API_helpers.RequestHelper;
import com.app.chaton.API_helpers.RequestObject;
import com.app.chaton.API_helpers.ServiceGenerator;
import com.app.chaton.Utils.PreferenceHelper;
import com.app.chaton.Utils.ToastHelper;
import com.baoyz.widget.PullRefreshLayout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class DialogActivity extends AppCompatActivity implements PullRefreshLayout.OnRefreshListener{

    private CallService callService;
    private PreferenceHelper preferenceHelper;
    private RequestHelper requestHelper;

    private SlidingMenu rightPanel;

    private DialogListFragmnet dialogListFragmnet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callService = ServiceGenerator.createService(CallService.class);
        preferenceHelper = new PreferenceHelper(getSharedPreferences(
                getResources().getString(R.string.PREFERENCE_FILE), MODE_PRIVATE));

        dialogListFragmnet = new DialogListFragmnet();
        dialogListFragmnet.setOnRefreshListener(this);
        uploadDialogsList();

        setContentView(R.layout.main);

        rightPanel = new SlidingMenu(this);
        rightPanel.setMode(SlidingMenu.RIGHT);
        rightPanel.setFadeEnabled(true);
        rightPanel.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        rightPanel.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        rightPanel.setBehindOffset(120);
        rightPanel.setMenu(R.layout.menu_frame);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rightMenuFrame, dialogListFragmnet)
                .commit();
    }

    @Override
    public void onRefresh() {
        uploadDialogsList();
    }

    @Override
    public void onBackPressed() {
        if (rightPanel.isMenuShowing()) rightPanel.showContent();
        else super.onBackPressed();
    }

    private void uploadDialogsList() {
        requestHelper = new RequestHelper() {
            @Override
            public void onStatusOk(MessageResponseObject response) {
                try {
                    dialogListFragmnet.stopProgress();
                } catch (NullPointerException e) {
                    Log.d("myLogs", "stoppping null");
                }
                dialogListFragmnet.setDialogsList(response.getData());
                dialogListFragmnet.getView().findViewById(R.id.dialogsList).setVisibility(View.VISIBLE);
                dialogListFragmnet.getView().findViewById(R.id.progressView).setVisibility(View.GONE);
            }

            @Override
            public void onStatusServerError(MessageResponseObject response) {
                ToastHelper.makeToast("Server error");
            }

            @Override
            public void onFail(Throwable t) {
                ToastHelper.makeToast(t.toString());
                t.printStackTrace();
            }
        };

        Long id = preferenceHelper.getId();
        String secret_key = preferenceHelper.getSecretKey();

        requestHelper.getDialogs(callService, new RequestObject(new Object(), id, secret_key), id, secret_key);
    }
}
