package com.jake.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 描述：
 *
 * @author jakechen
 * @since 2016/12/23 15:12
 */

public class CameraDemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, CameraDemoActivity.class));
        }
    }
}
