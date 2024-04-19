package com.example.modernfurniture;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LevelAdapter2 extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] itemname;
	private final String[] contact;
	private final String[] other;
	public LevelAdapter2(Activity context, String[] itemname, String[] contact, String[] other) {
		super(context, R.layout.levellist2, itemname);
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.itemname=itemname;
		this.contact=contact;
		this.other=other;

	}
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.levellist2, null,true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.person_name);
		TextView contact1 = (TextView) rowView.findViewById(R.id.person_contact);
		TextView other1 = (TextView) rowView.findViewById(R.id.person_other);


		txtTitle.setText(itemname[position]);
		contact1.setText(contact[position]);
		other1.setText(other[position]);


		return rowView;
		
	};
}
