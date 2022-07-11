package com.catapp.scanthecat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

                    System.out.println(resultCats);

                    /*
                    Gson gson = new Gson();
                    // Convert JSON File to Java Object
                    Boek[] boeken = gson.fromJson(finalResultNUR[0], Boek[].class);

                    //check het aantal resultaten
                    int countResults = boeken.length;
                    // dismiss the progress dialog after receiving data from API
                    progressDialog.dismiss();
                    //Resetten van de url omdat ie anders shit achter elkaar blijft plakken.
                    myUrl = "http://85.215.228.51:8080/";

                    switch(countResults) {
                        case 0:
                            Toast.makeText(MainActivity.this, "Geen resultaten", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //Ga direct naar displaybookresult, geen lijst nodig
                            Intent intentResult = new Intent(MainActivity.this, DisplayBookResultActivity.class);
                            intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentResult.putExtra("result", boeken[0]);
                            getApplicationContext().startActivity(intentResult);
                            //https://coderedirect.com/questions/513788/android-asynctask-start-new-activity-in-onpostexecute
                            break;
                        default:
                            //Meerdere results, dus resultlijst tonen
                            Intent intentResultList = new Intent(MainActivity.this, DisplayResultListActivity.class);
                            intentResultList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentResultList.putExtra("result", boeken);
                            intentResultList.putExtra("afkomst", "MainActivity");
                            getApplicationContext().startActivity(intentResultList);
                            break;
                    }
                    */
                }
            });
        }
    }
}

//ToDo Cat object van maken
//{"length": "Medium to Large", "origin": "Great Britain", "image_link": "https://api-ninjas.com/images/cats/british_longhair.jpg", "family_friendly": 4, "shedding": 4, "general_health": 3, "playfulness": 3, "meowing": 3, "children_friendly": 4, "stranger_friendly": 4, "grooming": 2, "intelligence": 4, "other_pets_friendly": 3, "min_weight": 9.0, "max_weight": 18.0, "min_life_expectancy": 15.0, "max_life_expectancy": 17.0, "name": "British Longhair"},