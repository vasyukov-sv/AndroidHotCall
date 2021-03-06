package com.example.admin.hotcall.mappers;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import com.example.admin.hotcall.Button.RelativeLayoutButton;
import com.example.admin.hotcall.R;
import com.example.admin.hotcall.obj.Contact;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.hotcall.common.Utils.getHumanPhone;

public class RButton4 {
    private final List<ButtonMapper> buttons = new ArrayList<>();
    private MyIntent myIntent;

    private static Contact getItemByIndex(List<Contact> list, int id) {
        for (Contact a : list) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    public RButton4 constructRelButtons(MyIntent intent, List<Contact> contacts) {
        this.myIntent = intent;

        //create
        buttons.add(createRButton(0, R.id.button1, getItemByIndex(contacts, 0)));
        buttons.add(createRButton(1, R.id.button2, getItemByIndex(contacts, 1)));
        buttons.add(createRButton(2, R.id.button3, getItemByIndex(contacts, 2)));
        buttons.add(createRButton(3, R.id.button4, getItemByIndex(contacts, 3)));
        return this;
    }

    public ButtonMapper findButtonById(final int id) {
        for (ButtonMapper buttonMapper : buttons) {
            if (buttonMapper.getRelativeLayoutButton().getId() == id) {
                return buttonMapper;
            }
        }
        return null;
    }

    private ButtonMapper findButtonByContactId(final int id) {
        for (ButtonMapper buttonMapper : buttons) {
            if (buttonMapper.getId() == id) {
                return buttonMapper;
            }
        }
        return null;
    }

    private ButtonMapper createRButton(int id, int buttonId, Contact contact) {
        return setButtonContext(new ButtonMapper(id, new RelativeLayoutButton((Context) myIntent, buttonId), contact));
    }

    private ButtonMapper setButtonContext(ButtonMapper buttonMapper) {
        Contact contact = buttonMapper.getContact();
        RelativeLayoutButton relativeLayoutButton = buttonMapper.getRelativeLayoutButton();

        if (contact != null) {
            relativeLayoutButton.setText(R.id.button_text, String.format("%s\n%s", contact.getName(), getHumanPhone(contact.getNumber())));
            relativeLayoutButton.setOnClickListener(getListener());
            relativeLayoutButton.setImageDrawable(R.id.button_image, new BitmapDrawable(myIntent.getContext().getResources(), contact.getPhoto()));
            myIntent.registerForContextMenu(relativeLayoutButton);
        } else {
            relativeLayoutButton.setText(R.id.button_text, myIntent.getContext().getString(R.string.addcontact));
            relativeLayoutButton.setText(R.id.button_info, "");
            relativeLayoutButton.setOnClickListener(getChooseListener());
            relativeLayoutButton.setImageResource(R.id.button_image, android.R.drawable.ic_input_add);
            myIntent.unregisterForContextMenu(relativeLayoutButton);
        }
        return buttonMapper;
    }

    private View.OnClickListener getListener() {
        return v -> myIntent.makeCall(v);
    }

    private View.OnClickListener getChooseListener() {
        return v -> myIntent.chooseContact(v);
    }

    public void update(ButtonMapper buttonMapper, Contact contact) {
        setButtonContext(buttonMapper.setContact(contact));
    }

    public List<Contact> getAllContacts() {
        List<Contact> list = new ArrayList<>();
        for (ButtonMapper button : buttons) {
            Contact contact = button.getContact();
            if (contact != null) {
                list.add(contact);
            }
        }
        return list;
    }

    public List<ButtonMapper> getAllButtonMapper() {
        List<ButtonMapper> list = new ArrayList<>();
        for (ButtonMapper button : buttons) {
            Contact contact = button.getContact();
            if (contact != null) {
                list.add(button);
            }
        }
        return list;
    }

    public void updateDuration(Contact contact) {
        ButtonMapper buttonMapper = findButtonByContactId(contact.getId());
        if (contact.getDuration() != null) {
            buttonMapper.getRelativeLayoutButton().setText(R.id.button_info, String.format("%s - %s", contact.getDuration().getIncomingCall(), contact.getDuration().getOutgoingCall()));
        }
    }
}
