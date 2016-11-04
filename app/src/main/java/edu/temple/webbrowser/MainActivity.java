package edu.temple.webbrowser;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements WebPageFragment.UrlListener {
    private EditText urlField;
    private String url;
    private FragmentArrayList fragments;
    private Button goButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri data = getIntent().getData();

        fragments = new FragmentArrayList();
        fragments.add(WebPageFragment.newInstance());

        replaceFragment(fragments.get());



        urlField = (EditText)findViewById(R.id.URL);
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
        goButton = (Button)findViewById(R.id.go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = urlField.getText().toString();
                ((WebPageFragment)fragments.get()).goTo(url);
                hideKeyboard();

            }
        });

        if(data != null) {
            String url = data.toString();
            ((WebPageFragment)fragments.get()).goTo(url);
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.web_container, fragment)
                .commit();
        fm.executePendingTransactions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                Fragment f = fragments.getPrevious();
                if(f != null) {
                    urlField.setText("");
                    replaceFragment(f);
                } else {
                    Toast.makeText(MainActivity.this, "No Previous Tabs", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_next:
                Fragment f2 = fragments.getNext();
                if(f2 != null) {
                    urlField.setText("");
                    replaceFragment(f2);
                } else {
                    Toast.makeText(MainActivity.this, "No Further Tabs", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_new:
                loadNewFragment();
                return true;

            case R.id.action_javascript_toggle:
                ((WebPageFragment)fragments.get()).toggleJS();
                return true;

            case R.id.action_close_tab:
                Fragment f3 = fragments.removeCurrent();
                if(f3 == null){
                    loadNewFragment();
                } else {
                    replaceFragment(f3);
                }


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void loadNewFragment(){
        fragments.add(WebPageFragment.newInstance());
        replaceFragment(fragments.get());
        urlField.setText("");
    }

    @Override
    public void updateURL(String url) {
        urlField.setText(url);
    }

    private void hideKeyboard(){
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e){

        }
    }

    @Override
    public void toggledJS(boolean js){
        if(js){
            Toast.makeText(MainActivity.this, R.string.js_enabled, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, R.string.js_disabled, Toast.LENGTH_SHORT).show();
        }
    }
}
