package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mDescription_tv, mAlsoKnownAs_tv, mIngredients_tv, mOrigin_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Show the back button on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mDescription_tv = findViewById(R.id.description_tv);
        mAlsoKnownAs_tv = findViewById(R.id.also_known_tv);
        mIngredients_tv = findViewById(R.id.ingredients_tv);
        mOrigin_tv = findViewById(R.id.origin_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        // Populate the UI
        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(getResources().getDrawable(R.drawable.ic_placeholder_image))
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Navigate back to main Activity
        if(item.getItemId() == android.R.id.home)
        {
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateUI(Sandwich sandwich)
    {
        mDescription_tv.setText(sandwich.getDescription());

        String origin = sandwich.getPlaceOfOrigin();
        // Check for empty string and show
        // or hide the value
        if(TextUtils.isEmpty(origin))
        {
            TextView origin_tv = findViewById(R.id.origin_label_tv);
            origin_tv.setVisibility(View.GONE);
        }
        mOrigin_tv.setText(sandwich.getPlaceOfOrigin());

        // Build a string from all the known names of sandwiches
        // from the list
        StringBuilder alsoKnownBuilder = new StringBuilder();
        List<String> alsoKnownList = sandwich.getAlsoKnownAs();
        if(alsoKnownList.size()>0)
        {
            for(int i=0;i<alsoKnownList.size();i++)
            {
                alsoKnownBuilder.append(alsoKnownList.get(i));
                // if its not the last item append a comma
                if(!(alsoKnownList.size() == i+1)) alsoKnownBuilder.append(" , ");
            }

            mAlsoKnownAs_tv.setText(alsoKnownBuilder.toString());
        }
        else
        {
            //If its empty don't show at all
            // Hide the label textView as well
            mAlsoKnownAs_tv.setVisibility(View.INVISIBLE);
            TextView alsoKnown_tv = findViewById(R.id.also_known_label_tv);
            alsoKnown_tv.setVisibility(View.GONE);
        }

        // Build a string from all the ingredients
        // from the list
        StringBuilder ingredientsBuilder = new StringBuilder();
        List<String> ingredientsList = sandwich.getIngredients();
        if(ingredientsList.size()>0)
        {
            for(int i=0;i<ingredientsList.size();i++)
            {
                ingredientsBuilder.append(ingredientsList.get(i));
                // if its not the last item append a comma
                if(!(ingredientsList.size() == i+1)) ingredientsBuilder.append(" , ");

            }
            mIngredients_tv.setText(ingredientsBuilder.toString());
        }
        else
        {
            //If its empty don't show at all
            // Hide the label textView as well
            mIngredients_tv.setVisibility(View.INVISIBLE);
            TextView ingredients_tv = findViewById(R.id.ingredients_label_tv);
            ingredients_tv.setVisibility(View.GONE);
        }
    }
}
