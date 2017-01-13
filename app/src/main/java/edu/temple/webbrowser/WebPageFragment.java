package edu.temple.webbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;


public class WebPageFragment extends Fragment {
    private WebView webView;
    private String stored_url;
    private EditText urlField;
    private String url;
    private Button goButton;
    private boolean javaScriptEnabled;


    private UrlListener mListener;

    public WebPageFragment() {
        // Required empty public constructor
    }


    public static WebPageFragment newInstance() {
        WebPageFragment fragment = new WebPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        javaScriptEnabled = false;
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Get the layout
        View v = inflater.inflate(R.layout.fragment_web_page, container, false);

        //Set URL EditText so that pressing enter acts like pressing GO
        urlField = (EditText)v.findViewById(R.id.URL);
        urlField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO){
                    goButton.performClick();
                    return true;
                }
                return false;
            }
        });
        //Initialize GO Button
        goButton = (Button)v.findViewById(R.id.go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = urlField.getText().toString();
                goTo(url);
                mListener.hideKeyboard();

            }
        });

        //Set a WebViewClient to handle the WebView
        MyWebViewClient client = new MyWebViewClient();
        webView = (WebView)v.findViewById(R.id.webView);
        webView.setWebViewClient(client);
        //Re-load the page if a site was previously displayed
        if(stored_url != null){
            goTo(stored_url);
        }
        //Log.d("onCreateView", "YO THE WEBVIEW IS NOT NULL");
        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof UrlListener) {
            mListener = (UrlListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UrlListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //MainActivity calls this to navigate to a URL in the WebView
    public void goTo(String url){
        stored_url = url;
        //Log.d("URL", stored_url);
        //Make sure that a protocol is chosen; if not, append one
        try{
            new URL(stored_url);
        } catch (MalformedURLException e){
            stored_url = "http://" + stored_url;
        }
        if(webView == null){
            //webView was null when app started by intent - onCreateView not being called - fixed now??
            //Log.d("NullView", "Web View Is Null Yo");
        } else {
            webView.loadUrl(stored_url);
            urlField.setText(stored_url);
        }
    }

    //Enable and disable Javascript based on user input in MainAvtivity
    public void toggleJS(){
        if(javaScriptEnabled){
            javaScriptEnabled = false;
        } else {
            javaScriptEnabled = true;
        }
        webView.getSettings().setJavaScriptEnabled(javaScriptEnabled);
        //goTo(stored_url);
        mListener.toggledJS(javaScriptEnabled);
    }

    public interface UrlListener {
        //void updateURL(String url);
        void toggledJS(boolean js);
        void hideKeyboard();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //super.shouldOverrideUrlLoading(view, url);
            stored_url = url;
            //Update the URL EditText when the URL is fixed (i.e. protocol appended)
            urlField.setText(stored_url);
            return false;
        }
    }
}
