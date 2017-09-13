package lab.ecologies.hybrid.alterwear;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private I2C_Enabled_Commands reader;

    private String TAG = MainActivity.class.getSimpleName();

    private Tag tag = null;
    private Activity main;
    private TextView textView = null;
    private TextView record = null;
    private EditText editText_message = null;
    private String strKey = "3969956a568c92252638524453df1091";
    private NxpNfcLib libInstance = null; // TapLinx Library
    private NdefMessage message = null;
    private ProgressDialog dialog;
    private View v;


    private void initializeLibrary() {
        libInstance = NxpNfcLib.getInstance();
        libInstance.registerActivity(this, strKey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = this;

        textView = (TextView) findViewById(R.id.mainTextView);
        record = (TextView) findViewById(R.id.record);
        editText_message = (EditText) findViewById(R.id.editText_message);
        v = findViewById(R.id.demoLayout);
        initializeLibrary();

        Button btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "Message from Edit Text: " + editText_message.getText().toString());
                    message = createNdefTextMessage(editText_message.getText().toString());
                    if (message != null) {
                        dialog = new ProgressDialog(MainActivity.this);
                        dialog.setMessage("Tag NFC Tag please");
                        dialog.show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        libInstance.startForeGroundDispatch();
        super.onResume();
    }

    @Override
    protected void onPause() {
        libInstance.stopForeGroundDispatch();
        super.onPause();
    }

    @Override
    public void onNewIntent (final Intent intent) {
        Log.d( TAG, "onNewIntent");
        // It is the time to write the tag
        Tag currentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        try {
            reader = I2C_Enabled_Commands.get(currentTag);
            if (reader == null) {
                String message = "The Tag could not be identified or this NFC device does not "
                        + "support the NFC Forum commands needed to access this tag";
                String title = "Communication failed";
                showAlert(message, title);
            } else {
                reader.connect();
            }

            Ntag_Get_Version.Prod prod = reader.getProduct();
            Log.d(TAG, "prod: " + prod);

            if (reader != null) {
                // NDEF Message to write in the tag
                NdefMessage msg = null;
                String wtf = editText_message.getText().toString();
                Log.d(TAG, "newIntent, wtf: " + wtf);
                Log.d(TAG, "newIntent, editText contents: " + editText_message.getText().toString()+ ", global var message: " + message.describeContents());
                msg = createNdefTextMessage(editText_message.getText().toString());
                // Time statistics to return
                long timeNdefWrite = 0;
                long RegTimeOutStart = System.currentTimeMillis();

                // Write the NDEF using NfcA commands to avoid problems when dealing with protected tags
                // Calling reader close / connect resets the authenticated status
                record.setText(new String(msg.getRecords()[0].getPayload(), "UTF-8"));
                reader.writeNDEF(msg, null);

                timeNdefWrite = System.currentTimeMillis() - RegTimeOutStart;
                dialog.dismiss();
                Toast.makeText(main, "time to write: " + timeNdefWrite, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        if (message != null) {
            nfcMger.writeTag(currentTag, message);
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Tag written", Toast.LENGTH_SHORT).show();
        }
        else {
            // Handle intent

        }

        */
        super.onNewIntent( intent );
    }

    private void doNdef() throws IOException {
        // NDEF Message to write in the tag
        NdefMessage msg = null;
        msg = createNdefTextMessage(editText_message.getText().toString());
        try {
            // Time statistics to return
            long timeNdefWrite = 0;
            long RegTimeOutStart = System.currentTimeMillis();

            // Write the NDEF using NfcA commands to avoid problems when dealing with protected tags
            // Calling reader close / connect resets the authenticated status
            Log.d(TAG, "doNdef, about to writeNDEF w/ this msg:" + msg.describeContents());
            reader.writeNDEF(msg, null);

            timeNdefWrite = System.currentTimeMillis() - RegTimeOutStart;
            dialog.dismiss();
            Toast.makeText(main, "time to write: " + timeNdefWrite, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(main, "write tag failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // https://www.mifare.net/wp-content/uploads/2016/08/AN11876-Starting-Development-with-TapLinx-SDK.pdf
    private void cardLogic (final Intent intent) {
        CardType cardType = libInstance.getCardType( intent );
        Log.d( TAG, "Card type found: " + cardType.getTagName());
        textView.setText("Card type found: " + cardType.getTagName());


        if (CardType.NTagI2CPlus1K== cardType) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            //IMFClassicEV1 objClassic = ClassicFactory.getInstance().getClassicEV1( MifareClassic.get( tag));

            /*
            INTAGI2Cplus nTagI2C = NTagFactory.getInstance().getNTAGI2CPlus1K(Ndef.get(tag));
            if ( ! nTagI2C.getReader().isConnected()) {
                nTagI2C.getReader().connect();
            }
            NdefMessage msg = null;
            try {
                msg = createNdefTextMessage(message.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
    */
            //nTagI2C.writeNDEF(msg);
        }

    }

    private NdefMessage createNdefTextMessage(String text) throws UnsupportedEncodingException {
        Log.d(TAG, "crateNdefTextMessage, text: " + text);
        if (text.length() == 0) {
            return null;
        }
        byte[] textBytes = text.getBytes();
        byte[] langBytes = "en".getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[((langLength + 1) + textLength)];
        payload[0] = (byte) langLength;
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, langLength + 1, textLength);
        return new NdefMessage(new NdefRecord[]{new NdefRecord((short) 1, NdefRecord.RTD_TEXT, new byte[0], payload)});
    }

    private void showAlert(final String message, final String title) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(main)
                        .setMessage(message)
                        .setTitle(title)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                }).show();
            }
        });

    }
}
