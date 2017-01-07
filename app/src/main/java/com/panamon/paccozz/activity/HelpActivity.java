package com.panamon.paccozz.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.freshdesk.hotline.Hotline;
import com.freshdesk.hotline.HotlineConfig;
import com.freshdesk.hotline.HotlineUser;
import com.panamon.paccozz.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        HotlineConfig hlConfig=new HotlineConfig("9d5af2db-ed0a-43d5-826c-a23a6d82db11","249ef5f7-9e48-42b6-b6d0-0dbd8eea0650");

        hlConfig.setVoiceMessagingEnabled(true);
        hlConfig.setCameraCaptureEnabled(true);
        hlConfig.setPictureMessagingEnabled(true);

        Hotline.getInstance(getApplicationContext()).init(hlConfig);

        //Update user information
        HotlineUser user = Hotline.getInstance(getApplicationContext()).getUser();
        user.setName("John Doe").setEmail("john@john.doe").setPhone("001", "2542542544");
        Hotline.getInstance(getApplicationContext()).updateUser(user);

    }
}
