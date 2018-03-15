package com.amallya.twittermvvm.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SampleModel {


    Long id;

	private String name;

	public SampleModel() {
		super();
	}

	// Parse model from JSON
	public SampleModel(JSONObject object){
		super();

		try {
			this.name = object.getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Getters
	public String getName() {
		return name;
	}

	// Setters
	public void setName(String name) {
		this.name = name;
	}

	// Record Finders
	public static SampleModel byId(long id) {
		//return new Select().from(SampleModel.class).where(SampleModel_Table.id.eq(id)).querySingle();
		  return null;
	}

	public static List<SampleModel> recentItems() {
		//return new Select().from(SampleModel.class).orderBy(SampleModel_Table.id, false).limit(300).queryList();
		return null;
	}
}
