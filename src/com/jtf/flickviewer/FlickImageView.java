package com.jtf.flickviewer;

import com.jtf.utility.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class FlickImageView extends Activity {
	final private static String TAG = "FlickImageView";
	private FlickViewer photoBig = new FlickViewer();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_full_screen);
        try{
            String bigPhotoLink = getIntent().getStringExtra("photo_link");
            Drawable drawable = Drawable.createFromStream(photoBig.fetch(bigPhotoLink), "src");        
        	final ImageView imgView= (ImageView) findViewById(R.id.fullscreen_image_view);
        	imgView.setImageDrawable(drawable);        	
        }catch(Exception e){
        	Log.e(TAG, e.toString());
        }   
    }
}
