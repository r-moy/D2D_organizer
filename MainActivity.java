package com.example.raymondmoy.activitiestodo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import java.io.*;
import java.net.URL;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;




public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String FILE = "ListInfo.dat"; //the file name that the data will end up being stored to.
    private String apiURL = "https://dog.ceo/dog-api/documentation/random";
    private EditText text; //This is the text editor to implement your toDoList task to
    private Button button; //This is the button that we use to add items to in the to-do list
    private ListView list; //This is to the change the order of the list.
    private ArrayList<String> items; //this is to hold all of the items that you have in the to-do list
    private ArrayAdapter<String> arrayAdapter; //this is the arrayAdapter that will polish the look of the list in our listview function.
    private String returnUrl = "https://i.imgur.com/yNwH3jV.jpg";
    private ImageView imageView;
    private RequestQueue queue;

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.edit_text); //Setting the instance variable equal text equal to the ID that we created for the variable.
        button = findViewById(R.id.add_a_button); //Setting the button variable equal to the ID for the button that we created.
        list = findViewById(R.id.list_items); //Setting the list variable equal to the ID that we created for it.
        imageView = findViewById(R.id.Image);
        items = readData(this);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        button.setOnClickListener((View.OnClickListener) this);
        list.setAdapter(arrayAdapter);
        queue = Volley.newRequestQueue(this);
        callAPI();
        Picasso.with(getBaseContext()).load(returnUrl).resize(1700,1700).into(imageView);
        button.setOnClickListener(this);
        list.setOnItemClickListener(this);
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_a_button: //the button that would end up switching data because.
                String newItem = text.getText().toString(); //This gets the String version of whatever the user types into the box
                arrayAdapter.add(newItem); //this adds the item to the list of items that we have to do
                text.setText(""); //This readjusts the text and makes it equal to
                writeData(items, this);
                Toast.makeText(this, "Item has been successfully added to the list", Toast.LENGTH_SHORT).show(); //This will end up showing on the bottom of the screen
                // that the user successfully added the needed input.

                break;
        }
    }
    public void onItemClick(AdapterView<?> bruh, View view, int position, long id) {
        items.remove(position);
        arrayAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Congratulations! You finished your task.", Toast.LENGTH_SHORT).show();


    }



    public static void writeData(ArrayList<String> lists, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILE, Context.MODE_PRIVATE);
            ObjectOutputStream obj = new ObjectOutputStream(fileOutputStream);
            obj.writeObject(lists); //put the ArrayList through the ObjectOutputStream.
            obj.close();          //it would crash without this because the Output would continue printing infinitively.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This reads in the data and returns an Array List with all the items.
     *
     * @param context
     * @return
     */
    public static ArrayList<String> readData(Context context) {
        ArrayList<String> listOfStuff = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(FILE);
            ObjectInputStream obj = new ObjectInputStream(fileInputStream);
            listOfStuff = (ArrayList<String>) obj.readObject();
        } catch (FileNotFoundException e) {
            listOfStuff = new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace(); //this tells us where the code went wrong and helps to debug the probelm
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listOfStuff;
    }
    private void callAPI() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    returnUrl = response.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print("Error!!!!");
                System.out.print(error.toString());
            }
        });
        queue.add(request);
    }

}

