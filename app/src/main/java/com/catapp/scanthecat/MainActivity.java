package com.catapp.scanthecat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends MenuActivity {

    private EditText editTextSearchCat;
    private Button searchCatButton;
    private String myUrl = "https://api.api-ninjas.com/v1/cats?name=";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextSearchCat = findViewById(R.id.searchCatEditText);
        searchCatButton = findViewById(R.id.searchCatButton);

        searchCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Haal de waarde op uit het zoekveld
                String catType = editTextSearchCat.getText().toString();

                if(catType.equals("")) {
                    Toast.makeText(MainActivity.this, "No input for search", Toast.LENGTH_SHORT).show();
                } else if(catType.length() < 3) {
                    Toast.makeText(MainActivity.this, "At least 3 characters needed for search", Toast.LENGTH_SHORT).show();
                } else {
                    //Plak deze achter de API call url en gooi daarna de titel weer leeg (anders problemen met heen en terugnavigeren), anders plakt ie meerdere titels achter elkaar
                    myUrl = myUrl + catType;
                    catType = null;

                    showProgressDialog();

                    // create object of GetResultsAsync class and execute it
                    GetResultsAsync getResultsAsync = new GetResultsAsync();
                    Thread thread = new Thread(getResultsAsync);
                    thread.start();
                }

            }
        });
    }

    public void showProgressDialog() {
        // display a progress dialog to show the user what is happening
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Find cats...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public class GetResultsAsync implements Runnable {

        @Override
        public void run() {

            // Fetch data from the API in the background.
            String result = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(myUrl);
                    //open a URL connection

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("X-Api-Key", "6P95jdveVV+eAo4s1ub3hw==N82UH1hbB5J1wkQw");

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();

                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();

                    }
                    // return the data to runOnUITrhead method

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            final String resultCats = result;
            runOnUiThread(new Runnable() {
                public void run() {
                    // do onPostExecute stuff GUI
                    Gson gson = new Gson();
                    // Convert JSON File to Java Object
                    Cat[] cats = gson.fromJson(resultCats, Cat[].class);

                    //check het aantal resultaten
                    int countResults = cats.length;
                    // dismiss the progress dialog after receiving data from API
                    progressDialog.dismiss();
                    //Resetten van de url omdat ie anders shit achter elkaar blijft plakken.
                    myUrl = "https://api.api-ninjas.com/v1/cats?name=";

                    switch(countResults) {
                        case 0:
                            Toast.makeText(MainActivity.this, "No results", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //Ga direct naar displaycatresult, geen lijst nodig
                            Intent intentResult = new Intent(MainActivity.this, DisplayCatResultActivity.class);
                            intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentResult.putExtra("result", cats[0]);
                            getApplicationContext().startActivity(intentResult);
                            //https://coderedirect.com/questions/513788/android-asynctask-start-new-activity-in-onpostexecute

                            break;
                        default:
                            //Meerdere results, dus resultlijst tonen
                            Intent intentResultList = new Intent(MainActivity.this, DisplayCatListActivity.class);
                            intentResultList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentResultList.putExtra("result", cats);
                            getApplicationContext().startActivity(intentResultList);

                            break;
                    }
                }
            });
        }
    }
}