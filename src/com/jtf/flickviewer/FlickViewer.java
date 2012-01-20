package com.jtf.flickviewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import com.jtf.utility.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


public class FlickViewer extends Activity{
    /** Called when the activity is first created. */	
	final private static String API_KEY="ed185ed8fe75fb37da6436b2c546b1a0";
	final private static String FLICKR_API_LINK="http://api.flickr.com/services/rest/";
	final private static String IMAGE_SIZE_SMALL="s";
	final private static String IMAGE_SIZE_TUMB="t";
	final private static String IMAGE_SIZE_MED="m";
	final private static String IMAGE_SIZE_BIG="z";
	final private static String IMAGE_SIZE_FULL="b";
	final private static String IMAGE_SIZE_ORIG="b";
	
	final private String TAG = "FlickViewer";
	// for contain the image link and drawable objects
	final private Map<Integer, String> drawableMap = new HashMap<Integer, String>();
    private Vector<Object> v;
	
	private static int noOfPage=1;
	final private int noOfImage=20;
	private GridView grdView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_grid_main);
        v = new Vector<Object>(25);
             
        final EditText txtUserName = (EditText) findViewById(R.id.txt_UserSearch);
        final Button btnSearch= (Button)findViewById(R.id.btnSearch);
//        final FlickImageView imgView= (FlickImageView) findViewById(R.id.image_view);
        grdView = (GridView) findViewById(R.id.myGrid);

//        final FlickImageView imgView = new FlickImageView(this);
        
//        String strUserName="jabed"; 
//		String strUserID = searchFlickr(strUserName); 
//		getImageList(strUserID);
//        final GridView grdView= (GridView) findViewById(R.id.myGrid);
//        grdView.setAdapter(new ImageAdapter(this));
        
        btnSearch.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v){
        		
        		String strUserName= txtUserName.getText().toString();
        		String strUserID = searchFlickr(strUserName); 
//        		txtUserName.setText(strUserID);
        		getImageList(strUserID);
        		grdView.setAdapter(new ImageAdapter(getApplicationContext()));
        		//grdView.	        		
        	}        	
        }        
        );
        
        grdView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
            	try{
           		Intent intent = new Intent(FlickViewer.this, FlickImageView.class);
           		// setContentView(R.layout.image_full_screen);           	
            	String bigPhotoLink = drawableMap.get(position).toString();
//            	Drawable drawable = Drawable.createFromStream(fetch(bigPhotoLink), "src");
            	Toast.makeText(FlickViewer.this, "" + bigPhotoLink, Toast.LENGTH_SHORT).show();
            	intent.putExtra("photo_link", bigPhotoLink);
            	startActivity(intent);
            	}catch(Exception e){
//            		Toast.makeText(FlickViewer.this, "" + e.toString(), Toast.LENGTH_LONG).show();
            		Log.d(TAG, e.toString());
            	}
            }
        });

        grdView.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
//                    	Toast.makeText(FlickViewer.this, "Test"+position,Toast.LENGTH_SHORT).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        
                    }
                });

        
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
//		Toast.makeText(FlickViewer.this, "" + keyCode, Toast.LENGTH_LONG).show();
//		if(event.KEYCODE_BACK == keyCode)
//		{
//			setContentView(R.layout.main);
//			return true;
//		}
		return super.onKeyDown(keyCode, event);
	}



	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}
    
    /**
     * 
     * @param strUN
     * @return
     */
    
    private String searchFlickr(String strUN){
    	String strFeed=FLICKR_API_LINK + "?method=flickr.people.findByUsername&api_key=" + API_KEY + "&username=" + strUN;
    	Log.i(TAG, strFeed);
    	URL url;
    	String nsid="";
    	try{
    		url = new URL(strFeed);
    		URLConnection conn = url.openConnection();
    		HttpURLConnection httpConn = (HttpURLConnection)conn;
    		    		
    		int responseCode= httpConn.getResponseCode();
    		Log.i(TAG, "httpConn.getResponseCode()" + responseCode);
    		if(HttpURLConnection.HTTP_OK == responseCode){
    			InputStream in = httpConn.getInputStream();
    			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    			DocumentBuilder db = dbf.newDocumentBuilder();
    			Document dom = db.parse(in);      
    		    Element docEle = dom.getDocumentElement();
    		    NodeList ndLst = docEle.getElementsByTagName("rsp");
    		    
    		    if (ndLst != null && ndLst.getLength() > 0) {
    		    	for (int i = 0 ; i < ndLst.getLength(); i++) {
    		    		Element entry = (Element)ndLst.item(i);
    		    		Element ensid = (Element)entry.getElementsByTagName("user").item(0);
    		    		nsid = ensid.getAttribute("id");
//    		    		System.out.println(nsid);
    		    		Log.i(TAG, nsid);
    		    	}
    		    }
    		       		       		    
    		}
    		return nsid;   		
    	}
    	catch(MalformedURLException e){
    		Log.e(TAG, e.toString());
    		return "";
    	}
    	catch(Exception e){
    		Log.e(TAG, e.toString());
    		return "";	
    	}
    	
    }
    
    private void getImageList(String strUserID){
    	URL url;
    	String nsid = strUserID;
    	String strFeed = FLICKR_API_LINK + "?method=flickr.people.getPublicPhotos&api_key=" 
    					+ API_KEY 
    					+ "&user_id=" + nsid
    					+ "&per_page=" + noOfImage
    					+ "&page=" + noOfPage;
    	
    	v.clear();
    	drawableMap.clear();
    	Log.i(TAG, strFeed);
    	try{
    		url = new URL(strFeed);
    		URLConnection conn = url.openConnection();
    		HttpURLConnection httpConn = (HttpURLConnection)conn;
    		    		
    		int responseCode= httpConn.getResponseCode();
    		if(HttpURLConnection.HTTP_OK == responseCode){
    			InputStream in = httpConn.getInputStream();
    			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    			DocumentBuilder db = dbf.newDocumentBuilder();
    			Document dom = db.parse(in);      
    		    Element docEle = dom.getDocumentElement();
    		    NodeList ndLst = docEle.getElementsByTagName("photo");
    		    
    		    if (ndLst != null && ndLst.getLength() > 0) {
    		    	for (int i = 0 ; i < ndLst.getLength(); i++) {
    		    		Element entry = (Element)ndLst.item(i);
    		    		Element ensid = (Element)entry.getElementsByTagName("photo").item(0);
    		    		String photoID = ensid.getAttribute("id");
    		    		String secret = ensid.getAttribute("secret");
    		    		String farm_id = ensid.getAttribute("farm");
    		    		String server = ensid.getAttribute("server");
    		    		String title = ensid.getAttribute("title");
    		    		
    		    		String photoLink = "http://farm" 
    		    							+ farm_id
    		    							+ ".static.flickr.com/"
    		    							+ server
    		    							+ "/"
    		    							+ photoID
    		    							+ "_"
    		    							+ secret
    		    							+ "_"
    		    							+ IMAGE_SIZE_TUMB
    		    							+ ".jpg";
    		    		
    		    		String bigPhotoLink = "http://farm" 
							+ farm_id
							+ ".static.flickr.com/"
							+ server
							+ "/"
							+ photoID
							+ "_"
							+ secret
							+ "_"
							+ IMAGE_SIZE_FULL
							+ ".jpg";
    		    		  		    		    		    		
    		    		InputStream is = new ImageDownloader().execute(photoLink).get();
    		    		Drawable drawable = Drawable.createFromStream(is, "src");
    		    		v.add(drawable);
    		    		drawableMap.put(i, bigPhotoLink);
    		    		grdView.invalidate();
    		    	}
    		    }
    		       		       		    
    		}
    	   		
    	}
    	catch(MalformedURLException e){
    		e.toString();
    	}
    	catch(Exception e){
    		e.toString();
    	}
    	//Get the images and update the grid
        
        
    }
    
    class ImageDownloader extends AsyncTask<String, Integer, InputStream> {

		@Override
		protected InputStream doInBackground(String... params) {
			try{
				InputStream is = fetch(params[0]);
				return is;				
			}catch (MalformedURLException e) {
				Log.e(TAG, e.toString());
				return null;
			}catch (Exception e) {
				Log.e(TAG, e.toString());
				return null;
			}			
		}
		
		protected void onProgressUpdate(Integer... values) { //
				super.onProgressUpdate(values);
			}
			// Called once the background activity has completed
			@Override
		protected void onPostExecute(InputStream result) { //
//	    		Drawable drawable = Drawable.createFromStream(result, "src");
//	    		v.add(drawable);
//	    		grdView.setAdapter(new ImageAdapter(getApplicationContext()));
			}
    	
    }
    
    public InputStream fetch(String urlString) throws MalformedURLException, IOException {
//    	DefaultHttpClient httpClient = new DefaultHttpClient();
//    	HttpGet request = new HttpGet(urlString);
//    	HttpResponse response = httpClient.execute(request);
//    	return response.getEntity().getContent();
        URL aURL = new URL(urlString);
        URLConnection conn = aURL.openConnection();
        conn.connect();        
        return conn.getInputStream();
    }
    
    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;            
        }

        public int getCount() {
            return v.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(60, 60));
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageDrawable((Drawable)v.get(position));

            return imageView;
        }

        private Context mContext;
      
    }
  
}