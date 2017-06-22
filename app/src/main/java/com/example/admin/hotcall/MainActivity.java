package com.example.admin.hotcall;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import com.example.admin.hotcall.common.DBHelper;
import com.example.admin.hotcall.common.Utils;
import com.example.admin.hotcall.loader.ContactsJob;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnLongClickListener {
    private DBHelper dbHelper;
    private String logTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        logTag = Utils.getApplicationName(this);
        generateView();
    }

    private void generateView() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(Utils.TABLE, null, null, null, null, null, null);
        if (!c.moveToFirst()) {
            createEmptyLayout();
            Log.d(logTag, "0 rows, generate view");


        } else {
            do {
                Log.d(logTag,
                        "ID = " + c.getInt(c.getColumnIndex("id")) +
                                ", name = " + c.getString(c.getColumnIndex("name")) +
                                ", number = " + c.getString(c.getColumnIndex("number")));
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
    }

    private void createEmptyLayout() {
        TableLayout tl = new TableLayout(this);
        tl.setPadding(10, 10, 10, 10);
        setContentView(tl);
        tl.addView(createEmptyTableRow(1, 2));
        tl.addView(createEmptyTableRow(3, 4));
    }

    private View createEmptyTableRow(int... ids) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        for (int id : ids) {
            tr.addView(createEmptyButton(id));
        }
        return tr;
    }

    @Override
    public void onClick(View view) {
        //stub
        Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(), "%s %d", getString(R.string.app_name), view.getId()), Toast.LENGTH_LONG).show();
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, Utils.PICK_CONTACT_REQUEST);
    }


    public void on(View view) {
        Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(), "%s %d", getString(R.string.app_name), view.getId()), Toast.LENGTH_LONG).show();
    }

    private Button createEmptyButton(int id) {
        Button btn = new Button(this);
        btn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        btn.setText(getString(R.string.addcontact));
        btn.setId(id);


        btn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
        btn.setOnClickListener(this);
        btn.setOnLongClickListener(this);
        return btn;
    }

    @Override
    public boolean onLongClick(View view) {
        Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(), "%s %d", "Long Click Pressed !!!", view.getId()), Toast.LENGTH_LONG).show();
        return true;
    }

    public void functionCall(String number) {
        //This is the UI thread
        //You can do whatever you with your number
        Toast.makeText(this, "This is the number: " + number, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            ContentResolver contentResolver = getContentResolver();
            ContactsJob job = new ContactsJob(this,contentResolver, contactUri);
            job.execute();


            // We only need the NUMBER column, because there will be only one row in the result
//            String[] projection = {Phone.NUMBER};
//            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
//            cursor.moveToFirst();
//            String number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
//            Log.d(logTag, number);

        }
    }


}