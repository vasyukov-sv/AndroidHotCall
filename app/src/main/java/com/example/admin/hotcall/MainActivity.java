package com.example.admin.hotcall;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.admin.hotcall.Button.RelativeLayoutButton;
import com.example.admin.hotcall.common.DBHelper;
import com.example.admin.hotcall.common.Utils;
import com.example.admin.hotcall.loader.AsyncResponse;
import com.example.admin.hotcall.loader.ContactsJob;
import com.example.admin.hotcall.obj.ButtonMapper;
import com.example.admin.hotcall.obj.Contact;
import com.example.admin.hotcall.obj.MyIntent;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.hotcall.common.Utils.getItemByIndex;

public class MainActivity extends AppCompatActivity implements AsyncResponse, MyIntent {
    private static final int MENU_DELETE = 1;
    private static final int MENU_UPDATE = 2;

    private final List<ButtonMapper> buttons = new ArrayList<>();
    private ButtonMapper currButtonMapper;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DBHelper.getInstance(this);
        dbHelper.setDB(dbHelper.getWritableDatabase());
        setContentView(R.layout.main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);

        List<Contact> contacts = dbHelper.selectAll();
        ButtonMapper.myIntent = this;
        buttons.add(new ButtonMapper(0, new RelativeLayoutButton(this, R.id.button1), getItemByIndex(contacts, 0)));
        buttons.add(new ButtonMapper(1, new RelativeLayoutButton(this, R.id.button2), getItemByIndex(contacts, 1)));
        buttons.add(new ButtonMapper(2, new RelativeLayoutButton(this, R.id.button3), getItemByIndex(contacts, 2)));
        buttons.add(new ButtonMapper(3, new RelativeLayoutButton(this, R.id.button4), getItemByIndex(contacts, 3)));
    }

    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void chooseContact(View v) {
        currButtonMapper = findButtonById(v.getId());
        chooseContact();
    }


    private void chooseContact() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), Utils.PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            new ContactsJob(data.getData(), getContentResolver(), this).execute(currButtonMapper);
        }
    }


    @Override
    public void processContacts(Contact contact) {
        if (contact == null) {
            return;
        }

        //зачем опять к базе лезть
        List<Contact> contacts = dbHelper.selectAll();
        if (contacts.contains(contact)) {
            Toast.makeText(this, "Такой контакт уже существует", Toast.LENGTH_LONG).show();
        } else {
            dbHelper.insert(contact);
            currButtonMapper.update(contact);
        }
    }

    @Override
    public void makeCall(final String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.putExtra("com.android.phone.extra.slot", 0); //For sim 1
        callIntent.setData(Uri.parse("tel:" + number));
        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        currButtonMapper = findButtonById(v.getId());
        menu.add(0, MENU_UPDATE, 0, "Другой контакт...");
        menu.add(0, MENU_DELETE, 1, "Удалить");
    }

    private ButtonMapper findButtonById(final int id) {

        return buttons.stream().filter(buttonMapper -> buttonMapper.getRelativeLayoutButton().getId() == id).findFirst().orElse(null);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                dbHelper.delete(currButtonMapper.getContact().getId());
                currButtonMapper.update(null);
                break;
            case MENU_UPDATE:
                chooseContact();
                break;
        }
        return super.onContextItemSelected(item);
    }
}