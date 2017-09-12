package lab.ecologies.hybrid.alterwear;

import android.app.ProgressDialog;
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

import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.ntag.NTagFactory;
import com.nxp.nfclib.ntag.NTagI2C;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private TextView textView = null;
    private String strKey = "3969956a568c92252638524453df1091";
    private NxpNfcLib libInstance = null; // TapLinx Library
    private NdefMessage message = null;
    private ProgressDialog dialog;

    private void initializeLibrary() {
        libInstance = NxpNfcLib.getInstance();
        libInstance.registerActivity(this, strKey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.mainTextView);
        initializeLibrary();

        Button btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
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
        cardLogic( intent );
        super.onNewIntent( intent );
    }

    private void cardLogic (final Intent intent) {
        CardType cardType = libInstance.getCardType( intent );
        Log.d( TAG, "Card type found: " + cardType.getTagName());
        textView.setText("Card type found: " + cardType.getTagName());

        if (CardType.NTagI2C1K == cardType) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NTagI2C nTagI2C = NTagFactory.getInstance().getNTAGI2C1K( NTagI2C);
            if ( ! nTagI2C.getReader().isConnected()) {
                nTagI2C.getReader().connect();
            }
            NdefMessage msg = null;
            try {
                msg = createNdefTextMessage(message.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //nTagI2C.writeNDEF(msg);
        }

    }

    private NdefMessage createNdefTextMessage(String text) throws UnsupportedEncodingException {
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
}
