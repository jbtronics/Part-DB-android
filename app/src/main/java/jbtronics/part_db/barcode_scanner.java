/*
 *     Part-DB-android: An Android Barcode Scanner for Part-DB
 *     Copyright (C) 2016 by Jan Boehmer
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jbtronics.part_db;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class barcode_scanner extends AppCompatActivity {


    public boolean openinbrowser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
    }

    //Methods for Action Bar Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_action_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.action_scan_browser:
                scanBarcode(null);
                openinbrowser = true;
                break;

            case R.id.action_scan_internal:
                scanBarcode(null);
                openinbrowser = false;
                break;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
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

                if(openinbrowser) {
                    //Open Browser with Part Details
                    openPartBrowser(pid);
                }
                else {
                    openPartInternal(pid);
                }

            } else if (resultCode == RESULT_CANCELED) {
                Context context = getApplicationContext();
                CharSequence text = getString(R.string.scanning_error);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    public void openPartBrowser(String pid)
    {
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        String server_url = sPref.getString("part_db_url", "");
        String show_file  = sPref.getString("part_db_show_part_file","show_part_info.php?pid=");

        //Ensure that server_url always ends with /
        server_url = server_url.trim();
        if(server_url.charAt(server_url.length()-1) != '/')
        {
           server_url = server_url + "/";
        }

        //Concatenate Strings to the URL
        //String url = "http://ras.pi/part-db/show_part_info.php?pid=";
        String url = server_url + show_file;
        url = url + pid;

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void openPartInternal(String pid)
    {
        Intent i = new Intent(this, PartDetails.class);
        i.putExtra("pid",pid);
        startActivity(i);
    }

}
