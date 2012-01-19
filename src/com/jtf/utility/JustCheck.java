package com.jtf.utility;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jtf.utility.R;

public class JustCheck extends Activity{
    /** Called when the activity is first created. */
    private JFUtility cmd;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        setContentView(R.layout.utility_main);
        
        final Button button = (Button) findViewById(R.id.btnSearch);      
        cmd = new JFUtility();
        
        button.setOnClickListener(new View.OnClickListener() {
        	Boolean yes = true;
            public void onClick(View v) {
                // Perform action on click            	
            	
//            	if (yes){
//            		shellCmd();
//            		yes = false;
//            	}else{
//            		pkgExplorerCmd();
//            		yes = true;
//            	}
            	EditText test = (EditText) findViewById(R.id.txt_UserSearch);
            	String cmd = test.getText().toString();
            	test.setText(shellCmd(cmd));
            }
        });
    }
    
    public String shellCmd(String shellCmd) {
//    	final TextView txtDisplay = (TextView)findViewById(R.id.tv);
    	String command = shellCmd;
        String args = "/";
        String result = cmd.executeCmd(command, args);        
        return  result;		
	}

	private String pkgExplorerCmd() {
//		final TextView txtDisplay = (TextView)findViewById(R.id.tv);
        String pkgList = cmd.pkgExplorer(this);
        return pkgList;	
	}
	
}