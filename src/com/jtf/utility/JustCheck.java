package com.jtf.utility;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jtf.utility.R;

public class JustCheck extends Activity {
    /** Called when the activity is first created. */
    private JFUtility cmd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        cmd = new JFUtility();

        String command = "cat";
        String args = "/system/build.prop";
        String result = cmd.executeCmd(command, args);
        TextView txtDisplay= (TextView)findViewById(R.id.tv);
        String pkgList = cmd.pkgExplorer(this);
        txtDisplay.setText(result);

    }

}