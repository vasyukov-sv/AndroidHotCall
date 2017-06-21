package com.example.admin.hotcall;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import com.example.admin.hotcall.Common.DBHelper;
import com.example.admin.hotcall.Common.Utils;

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
}