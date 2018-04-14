package com.example.admin.hotcall.obj;

import android.content.Context;
import com.example.admin.hotcall.Button.RelativeLayoutButton;
import com.example.admin.hotcall.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.hotcall.common.Utils.getItemByIndex;

public class RButton4 {
    private final List<ButtonMapper> buttons = new ArrayList<>();

    public RButton4 constructRelButtons(MyIntent intent, List<Contact> contacts) {
        ButtonMapper.myIntent = intent;
        buttons.add(createRButton(0, R.id.button1, getItemByIndex(contacts, 0), intent.getContext()));
        buttons.add(createRButton(1, R.id.button2, getItemByIndex(contacts, 1), intent.getContext()));
        buttons.add(createRButton(2, R.id.button3, getItemByIndex(contacts, 2), intent.getContext()));
        buttons.add(createRButton(3, R.id.button4, getItemByIndex(contacts, 3), intent.getContext()));
        return this;
    }


    public ButtonMapper findButtonById(final int id) {
        return buttons.stream().filter(buttonMapper -> buttonMapper.getRelativeLayoutButton().getId() == id).findFirst().orElse(null);
    }

    private ButtonMapper createRButton(int id, int buttonId, Contact contact, Context context) {
        return new ButtonMapper(id, new RelativeLayoutButton(context, buttonId), contact);
    }
}
