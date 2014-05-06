package com.jsp.lh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DBLayout.FoodDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import entities.FoodEntity;

import android.app.ListActivity;

public class FoodDetailsActivity extends ListActivity {

	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";

	FoodDatabase fd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_details);

		// final ListView listview = (ListView) findViewById(R.id.listview);

		fd = new FoodDatabase(FoodDetailsActivity.this);
		fd.open();
		ArrayList<FoodEntity> foods = fd.getAllFoods();
		fd.close();

		List<Map<String, String>> foodItems = convertToListItems(foods);

		final ListAdapter listAdapter = createListAdapter(foods);
		setListAdapter(listAdapter);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.food_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	private ListAdapter createListAdapter(final ArrayList<FoodEntity> foods) {
		final String[] fromMapKey = new String[] { TEXT1, TEXT2 };
		final int[] toLayoutId = new int[] { android.R.id.text1,
				android.R.id.text2 };
		final List<Map<String, String>> list = convertToListItems(foods);

		return new SimpleAdapter(this, list,
				android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);
	}

	private List<Map<String, String>> convertToListItems(
			ArrayList<FoodEntity> foods) {
		final List<Map<String, String>> listItem = new ArrayList<Map<String, String>>(
				foods.size());

		for (final FoodEntity food : foods) {
			final Map<String, String> listItemMap = new HashMap<String, String>();
			listItemMap.put(TEXT1, food.getName());
			listItemMap.put(TEXT2, food.getCalories() + " calories");
			listItem.add(Collections.unmodifiableMap(listItemMap));
		}

		return Collections.unmodifiableList(listItem);
	}
}
