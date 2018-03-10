package com.example.admin.hotcall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import com.example.admin.hotcall.Button.RelativeLayoutButton;
import com.example.admin.hotcall.common.DBHelper;
import com.example.admin.hotcall.common.Utils;
import com.example.admin.hotcall.loader.AsyncResponse;
import com.example.admin.hotcall.loader.ContactsJob;
import com.example.admin.hotcall.obj.Contact;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private static final int MENU_DELETE = 1;
    private static final int MENU_UPDATE = 2;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private Set<RelativeLayoutButton> buttons = new LinkedHashSet<>();

    private DBHelper dbHelper;
    private String logTag;
    private int buttonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        dbHelper.setDB(dbHelper.getWritableDatabase());
        logTag = Utils.getApplicationName(this);
        setContentView(R.layout.main);

        List<Contact> contacts = dbHelper.selectAll();
        buttons.add(new RelativeLayoutButton(this, R.id.button1));
        buttons.add(new RelativeLayoutButton(this, R.id.button2));
        buttons.add(new RelativeLayoutButton(this, R.id.button3));
        buttons.add(new RelativeLayoutButton(this, R.id.button4));

//        dbHelper.selectAll().stream()

//        button1.setText(R.id.button_text, String.format("%s\n%s", contacts.get(0).getName(), contacts.get(0).getNumber()));
//
//
//        for (int i = 0; i < 4; i++) {
//
//
//            RelativeLayoutButton button = new RelativeLayoutButton(this, R.id.button + i);
//
//
//            button.setText(btn.getId(), String.format("%s\n%s", contacts.getName(), contact.getNumber()));
//            if (contact.getId() == id) {
//                return contact;
//            }
//        }
//        return null;
//
//        drawButton(btn, findContact(id, contacts));
//        generateView();
    }

    private void generateView() {
        TableLayout tl = new TableLayout(this);
        tl.setPadding(10, 10, 10, 10);
        setContentView(tl);
        List<Contact> Contacts = dbHelper.selectAll();
        tl.addView(createTableRow(createButton(1, Contacts), createButton(2, Contacts)));
        tl.addView(createTableRow(createButton(3, Contacts), createButton(4, Contacts)));
    }

    private View createTableRow(RelativeLayoutButton btn1, RelativeLayoutButton btn2) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        tr.addView(btn1);
        tr.addView(btn2);
        return tr;
    }

    private RelativeLayoutButton createButton(int id, List<Contact> contacts) {
        RelativeLayoutButton btn = new RelativeLayoutButton(this, id);
//        Button btn = new Button(this);
        btn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));

        btn.setId(id);
        drawButton(btn, findContact(id, contacts));
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

    private void chooseContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
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
        buttonID = v.getId();
        menu.add(0, MENU_UPDATE, 0, "Другой контакт...");
        menu.add(0, MENU_DELETE, 0, "Удалить");
    }

    @Override
    public void processContacts(Contact contact) {
        if (contact == null) {
            return;
        }
        List<Contact> contacts = dbHelper.selectAll();
        if (contacts.contains(contact)) {
            Toast.makeText(this, "Такой контакт уже существует", Toast.LENGTH_LONG).show();
        } else {
            dbHelper.insert(contact);
            drawButton((RelativeLayoutButton) findViewById(buttonID), contact);
        }
    }

    private void drawButton(RelativeLayoutButton btn, Contact contact) {
        if (btn == null) {
            return;
        }

        if (contact != null) {
            btn.setText(btn.getId(), String.format("%s\n%s", contact.getName(), contact.getNumber()));
//            Drawable top = new BitmapDrawable(contact.getPhoto());
            int width = btn.getMeasuredWidth();
            int height = btn.getMeasuredHeight();
            Log.i(logTag, String.valueOf(width));
            Log.i(logTag, String.valueOf(height));

//            top.setBounds(0, 0, 300, 300);
//            btn.setCompoundDrawables(null, top, null, null);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonID = v.getId();
                    makeCall();
                }
            });
            registerForContextMenu(btn);
        } else {
            btn.setText(btn.getId(), getString(R.string.addcontact));
//            btn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_add, 0, 0, 0);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonID = v.getId();
                    chooseContact();
                }
            });
            unregisterForContextMenu(btn);
        }
    }

    private void makeCall() {
        Log.i(logTag, "Make call");
        Contact contact = dbHelper.getContact(buttonID);
        if (contact == null) {
            return;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.putExtra("com.android.phone.extra.slot", 0); //For sim 1
        callIntent.setData(Uri.parse("tel:" + contact.getNumber()));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            try {
                startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    makeCall();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                dbHelper.delete(buttonID);
                drawButton((RelativeLayoutButton) findViewById(buttonID), null);
                break;
            case MENU_UPDATE:
                chooseContact();
                break;
        }
        return super.onContextItemSelected(item);
    }
}