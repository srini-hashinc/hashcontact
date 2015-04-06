package com.ocr.contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import edu.sfsu.cs.orange.ocr2.CaptureActivity;
import edu.sfsu.cs.orange.ocr2.R;

public class GetContactdetails extends Activity {
	final int CONTEXT_MENU_Name = 1;
	final int CONTEXT_MENU_Number = 2;
	final int CONTEXT_MENU_Work = 3;
	final int CONTEXT_MENU_Address = 4;
	final int CONTEXT_MENU_Company = 5;
	final int CONTEXT_MENU_Website = 6;
	final int CONTEXT_MENU_Email = 7;

	EditText name, number, company, work, address, website, email;
	Button b;

	ArrayAdapter<String> arrayAdapter;
	ListView lv;
	String tex = null;
	String selectedFromList = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		Intent i = getIntent();
		ArrayList<String> ar = new ArrayList<String>();
		ar = i.getStringArrayListExtra("Result");
		String Result = "";
		for (String obj : ar) {
			Result += obj;

		}
		lv = (ListView) findViewById(R.id.your_list_view_id);
		name = (EditText) findViewById(R.id.Person_name);
		number = (EditText) findViewById(R.id.Phone_no);
		company = (EditText) findViewById(R.id.Company);
		work = (EditText) findViewById(R.id.Work);
		address = (EditText) findViewById(R.id.Address);
		email = (EditText) findViewById(R.id.Email);
		website = (EditText) findViewById(R.id.Website);
		b = (Button) findViewById(R.id.Save_contact);

		registerForContextMenu(lv);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				openContextMenu(myView);
			}
		});
		String str = Result;
		List<String> elephantList = Arrays.asList(str.split("\n"));
		for (int i1=0;i1 < elephantList.size();i1++)
		{
		  Toast.makeText(this, elephantList.get(i1).toString(), 2000).show();
		}

		arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, elephantList);

		lv.setAdapter(arrayAdapter);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
				int rawContactID = ops.size();
				ops.add(ContentProviderOperation
						.newInsert(ContactsContract.RawContacts.CONTENT_URI)
						.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,
								null)
						.withValue(ContactsContract.RawContacts.ACCOUNT_NAME,
								null).build());

				ops.add(ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
								rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE,
								StructuredName.CONTENT_ITEM_TYPE)
						.withValue(StructuredName.DISPLAY_NAME, name.getText().toString()).build());

				System.out.println("added name");

				// Insert address
				ops.add(ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
								rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE,
								CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
						.withValue(CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS,
								address.getText().toString())
						.withValue(CommonDataKinds.StructuredPostal.TYPE,
								CommonDataKinds.StructuredPostal.TYPE_WORK).build());

				System.out.println("added address");

				// Insert mobile number
				ops.add(ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
								rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE,
								Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, number.getText().toString())
						.withValue(Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
						.build());

				System.out.println("added mobileNo");

				// Insert companyNo
				ops.add(ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
								rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE,
								Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, work.getText().toString())
						.withValue(Phone.TYPE, CommonDataKinds.Phone.TYPE_WORK).build());

				System.out.println("added companyNo");

//				// Insert email
				ops.add(ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
								rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE,
								Email.CONTENT_ITEM_TYPE)
						.withValue(Email.ADDRESS, email.getText().toString())
						.withValue(Email.TYPE, Email.TYPE_WORK).build());

//				System.out.println("added email");
				ops.add(ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
								rawContactID)
						.withValue(
								ContactsContract.Data.MIMETYPE,
								ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
						.withValue(
								ContactsContract.CommonDataKinds.Organization.COMPANY,
								work.getText().toString())
						.withValue(ContactsContract.CommonDataKinds.Organization.TYPE,
								ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
						.withValue(ContactsContract.CommonDataKinds.Organization.TITLE,
								work.getText().toString())
						.withValue(ContactsContract.CommonDataKinds.Organization.TYPE,
								ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
						.build());
//
//				// Insert website
				ops.add(ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
								rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE,
								CommonDataKinds.Website.CONTENT_ITEM_TYPE)
						.withValue(CommonDataKinds.Website.URL, website.getText().toString())
						.withValue(CommonDataKinds.Website.TYPE,
								CommonDataKinds.Website.TYPE_WORK).build());

				System.out.println("added website");ops.add(ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
								rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE,
								CommonDataKinds.Website.CONTENT_ITEM_TYPE)
						.withValue(CommonDataKinds.Website.URL, website.getText().toString())
						.withValue(CommonDataKinds.Website.TYPE,
								CommonDataKinds.Website.TYPE_WORK).build());

				// Asking the Contact provider to create a new contact
				try {
					getContentResolver().applyBatch(ContactsContract.AUTHORITY,
							ops);
				} catch (Exception e) {
					e.printStackTrace();
					
				}
				Toast.makeText(getApplicationContext(), "Contact Saved ", 2000).show();
//				Intent i=new Intent(GetContactdetails.this,CaptureActivity.class);
//				startActivity(i);
//				finish();
			}
		});
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		String selectedFromList = (String) (lv
				.getItemAtPosition(menuInfo.position));
		switch (item.getItemId()) {

		case CONTEXT_MENU_Name: {
			name.setText(selectedFromList);
		}
			break;
		case CONTEXT_MENU_Number: {
			number.setText(selectedFromList);
		}
			break;
		case CONTEXT_MENU_Company: {
			company.setText(selectedFromList);
		}
			break;
		case CONTEXT_MENU_Address: {
			address.setText(selectedFromList);
		}
			break;
		case CONTEXT_MENU_Website: {
			website.setText(selectedFromList);
		}
			break;
		case CONTEXT_MENU_Email: {
			email.setText(selectedFromList);
		}
			break;
		case CONTEXT_MENU_Work: {
			work.setText(selectedFromList);
		}
			break;
		}

		return super.onContextItemSelected(item);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		// Context menu
		menu.setHeaderTitle("My Context Menu");
		menu.add(Menu.NONE, CONTEXT_MENU_Name, Menu.NONE, "Name");
		menu.add(Menu.NONE, CONTEXT_MENU_Number, Menu.NONE, "Number");
		menu.add(Menu.NONE, CONTEXT_MENU_Work, Menu.NONE, "Work");
		menu.add(Menu.NONE, CONTEXT_MENU_Address, Menu.NONE, "Address");
		menu.add(Menu.NONE, CONTEXT_MENU_Company, Menu.NONE, "Company");
		menu.add(Menu.NONE, CONTEXT_MENU_Website, Menu.NONE, "Website");
		menu.add(Menu.NONE, CONTEXT_MENU_Email, Menu.NONE, "Email");
	}
}
