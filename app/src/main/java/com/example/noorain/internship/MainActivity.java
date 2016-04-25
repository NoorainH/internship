package com.example.noorain.internship;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import com.example.noorain.internship.Fragment.Fragment1;
import com.example.noorain.internship.Fragment.Fragment2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText name_editText,email_editText,phone_editText,address_editText;
    String name,email,phone,address,id;
    ProgressDialog progress ;
    static String httpurl = "http://collegeaid.orgfree.com/";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0 : return  Fragment1.newInstance(position+1);
                case 1 : return  Fragment2.newInstance(position+1);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Register";
                case 1:
                    return "User Details";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    class PostRequest extends AsyncTask<Object,Object,String>{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.has("error")){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage(jsonObject.getString("error"))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            }).show();
                    return;
                }
                if(jsonObject.has("id")){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Success")
                            .setMessage("Your registration id is : "+jsonObject.getString("id"))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete

                                }
                            }).show();
                    return;
                }
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage("Some problem occured.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        }).show();

            } catch (JSONException e) {
                e.printStackTrace();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage("Some problem occured.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        }).show();

            }

        }

        @Override
        protected String doInBackground(Object... params) {
            try{
                URL url = new URL(httpurl+"postData.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                String data = URLEncoder.encode("name", "UTF-8")
                        + "=" + URLEncoder.encode(name, "UTF-8");

                data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                        + URLEncoder.encode(email, "UTF-8");

                data += "&" + URLEncoder.encode("phone", "UTF-8")
                        + "=" + URLEncoder.encode(phone, "UTF-8");

                data += "&" + URLEncoder.encode("address", "UTF-8")
                        + "=" + URLEncoder.encode(address, "UTF-8");



                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                int res = conn.getResponseCode();
                Log.d("res code :"," "+res);
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
//                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                String output = responseOutput.toString();
                Log.d("output : ",output);
                br.close();

                progress.dismiss();
                return output;


            }catch (Exception e){
                e.printStackTrace();
                progress.dismiss();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Registering.. ");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }
    }
    class GetRequest extends AsyncTask<Object,Object,String>{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                final JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.has("error")){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage(jsonObject.getString("error"))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    TextView output_textView = (TextView)findViewById(R.id.output_text);
                                    try{

                                        output_textView.setText(jsonObject.getString("error"));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }).show();
                    return;
                }
                if(jsonObject.has("id")){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Success")
                            .setMessage("Details are  : "+jsonObject.toString())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    TextView output_textView = (TextView)findViewById(R.id.output_text);
                                    try{
                                        String out = "\nName      : "+jsonObject.getString("name");
                                                out+="\nEmail     : "+jsonObject.getString("email");
                                                out+="\nPhone     : "+jsonObject.getString("phone");
                                                out+="\naddress   : "+jsonObject.getString("address");
                                        output_textView.setText(out);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }).show();
                    return;
                }
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage("Some problem occured.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                TextView output_textView = (TextView)findViewById(R.id.output_text);
                                try{

                                    output_textView.setText("Some problem occured.");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).show();

            } catch (JSONException e) {
                e.printStackTrace();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage("Some problem occured.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                TextView output_textView = (TextView)findViewById(R.id.output_text);
                                try{

                                    output_textView.setText("Some problem occured.");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).show();

            }

        }

        @Override
        protected String doInBackground(Object... params) {
            try{
                URL url = new URL(httpurl+"getData.php?id="+id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoOutput(false);

                conn.connect();

                int res = conn.getResponseCode();
                Log.d("res code :"," "+res);
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
//                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                String output = responseOutput.toString();
                Log.d("output : ",output);
                br.close();

                progress.dismiss();
                return output;


            }catch (Exception e){
                e.printStackTrace();
                progress.dismiss();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Fetching Data.. ");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }
    }
    public void register(View view){
        if(name_editText == null){
            name_editText = (EditText)findViewById(R.id.input_name);
        }
        if(email_editText == null){
            email_editText = (EditText)findViewById(R.id.input_email);
        }
        if(phone_editText == null){
            phone_editText = (EditText)findViewById(R.id.input_phone);
        }
        if(address_editText == null){
            address_editText = (EditText)findViewById(R.id.input_address);
        }

        name = name_editText.getText().toString();
        email = email_editText.getText().toString();
        phone = phone_editText.getText().toString();
        address = address_editText.getText().toString();

        if(name.length()==0 || validateLetters(name)){
            name_editText.setError("Please Enter Correct Name");
            return;
        }

        if(email.length()==0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_editText.setError("Please Enter Correct Email");
            return;
        }

        if(phone.length()!=10 || !android.util.Patterns.PHONE.matcher(phone).matches()){
            phone_editText.setError("Please Enter Correct Phone Number");
            return;

        }

        if(address.length()==0){
            phone_editText.setError("Please Enter Address");
            return;
        }
        new PostRequest().execute(null,null,null);
    }

    public static boolean validateLetters(String txt) {

        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return !matcher.find();

    }

    public void getData(View view){
        EditText id_editText = (EditText)findViewById(R.id.input_id);
        id = id_editText.getText().toString();
        new GetRequest().execute(null,null,null);
    }

}
