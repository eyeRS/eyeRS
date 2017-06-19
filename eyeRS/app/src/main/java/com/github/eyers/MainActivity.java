package com.github.eyers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner; //Spinner component
    private Button done; //Used to validate all set up details and proceed to login

    //Array of security questions
    private String[] questions = {

            "What is the name of your junior/primary school?",
            "What is the name of your first pet?",
            "In what year was your father born?",
            "In what city does your nearest sibling stay?",
            "What is the first name of the teacher who gave you your first failing grade?",
            "What was the house number or street name you lived in as a child?",
            "What were the last 4 digits of your childhood mobile number?",
            "In what city or town was your first full time job?",
            "What are the last 5 digits of your ID number?",
            "What time of the day were you born (hh:mm)?"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.spinner = (Spinner) findViewById(R.id.spinnerQtn); //Links to the spinner in the layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, this.questions);

       // this.spinner.setAdapter(spinner); //Sets the SpinnerAdapter used to provide the data which backs this Spinner
       /* this.spinner.setOnItemClickListener(
                new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0){

                    }
                });

*/
    }

    public void doneAction(){ //Executes when the Done button is clicked

    }
}
