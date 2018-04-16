package com.example.admin.hotcall;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.admin.hotcall.common.DBHelper;
import com.example.admin.hotcall.common.Utils;
import com.example.admin.hotcall.loader.AsyncResponse;
import com.example.admin.hotcall.loader.ContactsJob;
import com.example.admin.hotcall.obj.ButtonMapper;
import com.example.admin.hotcall.obj.Contact;
import com.example.admin.hotcall.obj.MyIntent;
import com.example.admin.hotcall.obj.RButton4;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.hotcall.common.Utils.MY_PERMISSIONS_REQUEST;
import static com.example.admin.hotcall.common.Utils.PERMISSION_REQUEST_CALL;

public class MainActivity extends AppCompatActivity implements AsyncResponse, MyIntent {
    private static final int MENU_DELETE = 1;
    private static final int MENU_UPDATE = 2;

//    private final List<ButtonMapper> buttons = new ArrayList<>();
    private RButton4 buttons;
    private ButtonMapper currButtonMapper;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DBHelper.getInstance(this);
        dbHelper.setDB(dbHelper.getWritableDatabase());
        setContentView(R.layout.main);

        checkPermission();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 2);

        List<Contact> contacts = dbHelper.selectAll();
        buttons = new RButton4().constructRelButtons(this,contacts);
    }

    private void checkPermission() {
        ArrayList<String> arrPerm = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.READ_CONTACTS);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.READ_CALL_LOG);
        }
        if (!arrPerm.isEmpty()) {
            String[] permissions = new String[arrPerm.size()];
            permissions = arrPerm.toArray(permissions);
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
        }
    }

    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void chooseContact(View v) {
//        currButtonMapper = findButtonById(v.getId());
        currButtonMapper = buttons.findButtonById(v.getId());
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
            buttons.update(currButtonMapper,contact);
//            currButtonMapper.update(contact);
        }
    }

    @Override
    public void makeCall(View v) {
        currButtonMapper = buttons.findButtonById(v.getId());
//        currButtonMapper = findButtonById(v.getId());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startCallIntent(currButtonMapper.getContact().getNumber());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CALL);
        }
    }

    private void startCallIntent(final String number) {
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
        currButtonMapper = buttons.findButtonById(v.getId());
//        currButtonMapper = findButtonById(v.getId());
        menu.add(0, MENU_UPDATE, 0, "Другой контакт...");
        menu.add(0, MENU_DELETE, 1, "Удалить");
    }

//    private ButtonMapper findButtonById(final int id) {
//
//        return buttons.stream().filter(buttonMapper -> buttonMapper.getRelativeLayoutButton().getId() == id).findFirst().orElse(null);
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                dbHelper.delete(currButtonMapper.getContact().getId());
//                currButtonMapper.update(null);
                buttons.update(currButtonMapper,null);
                break;
            case MENU_UPDATE:
                chooseContact();
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CALL:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCallIntent(currButtonMapper.getContact().getNumber());
                } else {
                    Toast.makeText(this, "Permission request was denied", Toast.LENGTH_LONG).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        String permission = permissions[i];
                        if (Manifest.permission.READ_CONTACTS.equals(permission)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                // you now have permission
                            }
                        }
                        if (Manifest.permission.READ_CALL_LOG.equals(permission)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                // you now have permission
                            }
                        }
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission request was denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}