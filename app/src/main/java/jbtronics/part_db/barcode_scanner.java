package jbtronics.part_db;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class barcode_scanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
    }


    public void scanBarcode(View view)
    {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.setPackage("com.google.zxing.client.android");
        intent.putExtra("SCAN_FORMATS", "EAN_8");                           //Scan only for EAN8
        intent.putExtra("SAVE_HISTORY",false);
        intent.putExtra("RESULT_DISPLAY_DURATION_MS", 0L);                   //Return the Data via Intent instantly

        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                //Generate Part ID from Barcode Result
                String pid = contents.substring(0,contents.length() - 1);


                //Output Part ID
                Context context = getApplicationContext();
                CharSequence text = getString(R.string.scanning_success);
                text = text + " " + pid;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //Open Browser with Part Details
                openPart(pid);

            } else if (resultCode == RESULT_CANCELED) {
                Context context = getApplicationContext();
                CharSequence text = getString(R.string.scanning_error);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    public void openPart(String pid)
    {
        String url = "http://ras.pi/part-db/show_part_info.php?pid=";

        url = url + pid;

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
