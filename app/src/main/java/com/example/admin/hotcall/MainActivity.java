package com.example.admin.hotcall;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.example.admin.hotcall.common.DBHelper;
import com.example.admin.hotcall.common.Utils;
import com.example.admin.hotcall.loader.AsyncResponse;
import com.example.admin.hotcall.loader.ContactsJob;
import com.example.admin.hotcall.obj.Contact;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener, AsyncResponse {
    private static final int MENU_DELETE = 1;
    private static final int MENU_UPDATE = 2;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private String logTag;
    private int buttonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        logTag = Utils.getApplicationName(this);
        generateView();
    }

    private void generateView() {
        TableLayout tl = new TableLayout(this);
        tl.setPadding(10, 10, 10, 10);
        setContentView(tl);
        List<Contact> Contacts = dbHelper.selectAll(db);
        tl.addView(createTableRow(createButton(1, Contacts), createButton(2, Contacts)));
        tl.addView(createTableRow(createButton(3, Contacts), createButton(4, Contacts)));
    }

    private View createTableRow(Button btn1, Button btn2) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        tr.addView(btn1);
        tr.addView(btn2);
        return tr;
    }

    private Button createButton(int id, List<Contact> contacts) {
        Button btn = new Button(this);
        Contact contact = findContact(id, contacts);
        btn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        if (contact != null) {
            btn.setText(String.format("%s\n%s", contact.getName(), contact.getNumber()));
            registerForContextMenu(btn);
        } else {
            btn.setText(getString(R.string.addcontact));
            btn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
        }
        btn.setId(id);
        btn.setOnClickListener(this);
        return btn;
    }

    private Contact findContact(int id, List<Contact> contacts) {
        for (Contact contact : contacts) {
            if (contact.getId() == id) {
                return contact;
            }
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        buttonID = view.getId();
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, Utils.PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            new ContactsJob(data.getData(), getContentResolver(), this).execute(buttonID);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_DELETE, 0, "Удалить");
        menu.add(0, MENU_UPDATE, 0, "Другой контакт...");
    }

    @Override
    public void processContacts(Contact contact) {
        if (contact == null) {
            return;
        }
        dbHelper.insert(db, contact);
        drawButton(contact);
    }

    private void drawButton(Contact contact) {
        int idContact = contact.getId();
        Button btn = (Button) findViewById(idContact);
        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        btn.setText(String.format("%s\n%s", contact.getName(), contact.getNumber()));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                dbHelper.delete(db);
                drawButton(null);
                break;
            case MENU_UPDATE:
                break;
        }
        return super.onContextItemSelected(item);
    }


}