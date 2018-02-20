package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String DEBUG_TAG = "#JsonUtils.java";

    public static Sandwich parseSandwichJson(String json)
    {
        Sandwich sandwich = null;

        try
        {
            JSONObject jsonObject = new JSONObject(json);

            JSONObject nameObj = jsonObject.getJSONObject("name");

            JSONArray alsoKnownAsArray = nameObj.getJSONArray("alsoKnownAs");

            JSONArray ingredientsJSONArray = jsonObject.getJSONArray("ingredients");

            List<String> alsoKnownAs = new ArrayList<String>();
            // Check to see if the sandwich has other names
            // and then query through the array
            if(alsoKnownAsArray.length() > 0)
            {

                int length = alsoKnownAsArray.length();
                for(int i=0;i<length;i++)
                {
                    alsoKnownAs.add(alsoKnownAsArray.getString(i));
                }
            }

            String mainName = nameObj.getString("mainName");
            String origin = jsonObject.getString("placeOfOrigin");
            String desc = jsonObject.getString("description");
            String imageURL = jsonObject.getString("image");


            List<String> ingredients = new ArrayList<String>();

            int ingredientsJSONArrayLength = ingredientsJSONArray.length();

            for(int i=0;i<ingredientsJSONArrayLength;i++)
            {
                ingredients.add(ingredientsJSONArray.getString(i));
            }

            sandwich = new Sandwich(mainName,alsoKnownAs,origin,desc,imageURL,ingredients);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(DEBUG_TAG,"Error creating Json Object");
        }

        return sandwich;

    }
}
