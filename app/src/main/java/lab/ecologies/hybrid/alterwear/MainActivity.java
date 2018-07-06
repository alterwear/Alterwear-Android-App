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
import android.widget.TextView;
import android.widget.Toast;

import com.nxp.nfclib.NxpNfcLib;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private I2C_Enabled_Commands reader;

    private String TAG = MainActivity.class.getSimpleName();

    private Activity main;
    private TextView record = null;
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

        record = (TextView) findViewById(R.id.record);
        v = findViewById(R.id.demoLayout);
        initializeLibrary();

        Button btn_send_zero = (Button) findViewById(R.id.btn_send_zero);
        btn_send_zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "Send zero");
                    message = createNdefTextMessage("0");
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

        Button btn_send_one = (Button) findViewById(R.id.btn_send_one);
        btn_send_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "Send one");
                    message = createNdefTextMessage("1");
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

        Button btn_send_two = (Button) findViewById(R.id.btn_send_two);
        btn_send_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "Send two");
                    message = createNdefTextMessage("2");
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

        Button btn_send_three = (Button) findViewById(R.id.btn_send_three);
        btn_send_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "Send three");
                    message = createNdefTextMessage("3");
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

        Button btn_send_long_text = (Button) findViewById(R.id.btn_long_text);
        btn_send_long_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "Send a long message in text");
                    message = createNdefTextMessage("Sell your cleverness and buy bewilderment.");
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

        Button btn_read_eeprom = (Button) findViewById(R.id.btn_read_eeprom);
        btn_read_eeprom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "read eeprom...");
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

        //This might be useful: http://www.codexpedia.com/android/android-nfc-read-and-write-example/
        // this too: https://www.survivingwithandroid.com/2016/01/nfc-tag-writer-android.html
        
        /*
        if (intent == null) return;

        // next chunk mostly from here: https://www.survivingwithandroid.com/2015/03/android-nfc-app-android-nfc-tutorial.html
        String type = intent.getType();
        String action = intent.getAction();
        Log.d(TAG, "type: " + type + ", action: " + action);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Log.d(TAG, "Action NDEF Found");
            Parcelable[] parcs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            for (Parcelable p : parcs) {
                NdefRecord[] records = message.getRecords();
                for (NdefRecord ndefRecord : records) {
                    short tnf = ndefRecord.getTnf();
                    Log.d(TAG, "tnf: " + tnf);
                    // here we handle the payload.
                    byte[] payload = ndefRecord.getPayload();
                    byte status = payload[0];
                    Log.d(TAG, "status: " + status);
                    int enc = status & 0x80; // Bit mask 7th bit 1
                    String encString = null;
                    if (enc == 0)
                        encString = "UTF-8";
                    else
                        encString = "UTF-16";

                    int ianaLength =  status && 0x3F; // Bit mask bit 5..0
                    Log.d(TAG, "ianaLength: " + ianaLength);
                    try {
                        String content = new String(payload, ianaLength + 1,
                                payload.length - 1 - ianaLength, encString);
                        Log.d(TAG, content);
                        record.setText(content);
                    }
                    catch(Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
        */
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

                // Time statistics to return
                long timeNdefWrite = 0;
                long RegTimeOutStart = System.currentTimeMillis();

                // Write the NDEF using NfcA commands to avoid problems when dealing with protected tags
                // Calling reader close / connect resets the authenticated status
                record.setText(new String(message.getRecords()[0].getPayload(), "UTF-8"));
                // second arg is the "writeEEPROMlistener". Since it's null here, noone gets notified.
                // I'm not sure if that means we don't hear about success or not.
                reader.writeNDEF(message, null);

                timeNdefWrite = System.currentTimeMillis() - RegTimeOutStart;
                dialog.dismiss();
                Toast.makeText(main, "time to write: " + timeNdefWrite, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onNewIntent( intent );
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
