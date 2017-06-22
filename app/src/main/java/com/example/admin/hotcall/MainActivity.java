package com.example.admin.hotcall;

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
import com.example.admin.hotcall.loader.AsyncResponse;
import com.example.admin.hotcall.loader.ContactsJob;
import com.example.admin.hotcall.obj.Contact;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnLongClickListener, AsyncResponse {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private String logTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        logTag = Utils.getApplicationName(this);
        generateView();
    }

    private void generateView() {
        Cursor c = db.query(Utils.TABLE, null, null, null, null, null, null);
        if (!c.moveToFirst()) {
            createEmptyLayout();
            Log.i(logTag, "0 rows, generate view");


        } else {
            do {
                Log.i(logTag,
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
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.putExtra("idbutton", view.getId());
        pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, Utils.PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            new ContactsJob(data.getData(), getContentResolver(), this).execute(data.getIntExtra("idbutton", 1));
        }
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


    @Override
    public void processContacts(Contact contact) {
//        dbHelper.insert(db, contact);
        drawButton(contact);
    }

    private void drawButton(Contact contact) {
        int idContact = contact.getId();
        Button btn = (Button) findViewById(idContact);
        btn.setText(String.format("%s\n%s", contact.getName(), contact.getNumber()));
    }

}