package com.example.admin.hotcall.obj;

import com.example.admin.hotcall.Button.RelativeLayoutButton;

public class ButtonMapper {

    private final RelativeLayoutButton relativeLayoutButton;
    private Contact contact;
    private int id;

    public ButtonMapper(int id, RelativeLayoutButton relativeLayoutButton, Contact contact) {
        this.relativeLayoutButton = relativeLayoutButton;
        this.contact = contact;
//        setButtonContext();
        this.id = id;
    }

    public int getId() {
        return id;
    }

//    private void setButtonContext() {
//        if (contact != null) {
//            relativeLayoutButton.setText(R.id.button_text, String.format("%s\n%s", contact.getName(), getHumanPhone(contact.getNumber())));
//            if (contact.getDuration() != null) {
//                relativeLayoutButton.setText(R.id.button_info, String.format("%s\n%s", contact.getDuration().getIncomingCall(), contact.getDuration().getOutgoingCall()));
//            }
//            relativeLayoutButton.setOnClickListener(getListener());
//            relativeLayoutButton.setImageDrawable(R.id.button_image, new BitmapDrawable(myIntent.getContext().getResources(), contact.getPhoto()));
//            myIntent.registerForContextMenu(relativeLayoutButton);
//        } else {
//            relativeLayoutButton.setText(R.id.button_text, myIntent.getContext().getString(R.string.addcontact));
//            relativeLayoutButton.setText(R.id.button_info, "");
//            relativeLayoutButton.setOnClickListener(getChooseListener());
//            relativeLayoutButton.setImageResource(R.id.button_image, android.R.drawable.ic_input_add);
//            myIntent.unregisterForContextMenu(relativeLayoutButton);
//        }
//    }

    public RelativeLayoutButton getRelativeLayoutButton() {
        return relativeLayoutButton;
    }

//    private View.OnClickListener getListener() {
//        return v -> myIntent.makeCall(v);
//    }
//
//    private View.OnClickListener getChooseListener() {
//        return v -> myIntent.chooseContact(v);
//    }

//    public void update(Contact contact) {
//        setContact(contact);
//        setButtonContext();
//    }

    public Contact getContact() {
        return contact;
    }

    public ButtonMapper setContact(Contact contact) {
        this.contact = contact;
        return this;
    }
}
