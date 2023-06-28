package org.seekhlo.nfc;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {
    private NfcAdapter nfcAdapter;
    private TextView textView;
    private TextView editText;

    private TextView retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        // Get the default NFC adapter for the device
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        retry = findViewById(R.id.retry);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Reading NFC");
                editText.setText("        ");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Enable NFC dispatch for the activity
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
            Toast.makeText(this, "NFC Reader Found", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "Device does not have NFC reader", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Disable NFC dispatch for the activity
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }

    }

    @Override
    public void onTagDiscovered(Tag tag) {
        MifareUltralight mifare = MifareUltralight.get(tag);
            String tagData = tag.toString();
            byte[] tagId = tag.getId();
            String tagIdHex = byteArrayToHexString(tagId);
            Log.d("TAG", "Hex are: " + tagIdHex);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("NFC Scanned");
                editText.setText(tagIdHex);
                retry.setVisibility(View.VISIBLE);
            }
        });
    }

    private String byteArrayToHexString(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : byteArray) {
            stringBuilder.append(String.format("%02X", b));
        }
        return stringBuilder.toString();
    }
}
