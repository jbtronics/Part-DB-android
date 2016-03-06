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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import jbtronics.part_db_classes.Part;

public class PartDetails extends AppCompatActivity {

    public String part_id = "";
    public Part part;

    //Views
    private TextView text_name, text_desc, text_instock, text_mininstock, text_comment, text_category, text_footprint, text_storelocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_details);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b != null){
            if(!b.getString("pid").equals("")) {
                part_id = b.getString("pid");
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "No Part ID were given", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Get Views
        text_name = (TextView) findViewById(R.id.detail_text_name);
        text_desc = (TextView) findViewById(R.id.detail_text_desc);
        text_instock = (TextView) findViewById(R.id.detail_text_instock);
        text_mininstock = (TextView) findViewById(R.id.detail_text_mininstock);
        text_comment = (TextView) findViewById(R.id.detail_text_comment);
        text_category = (TextView) findViewById(R.id.detail_text_category);
        text_footprint = (TextView) findViewById(R.id.detail_text_footprint);
        text_storelocation = (TextView) findViewById(R.id.detail_text_storelocation);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        downloadPartDetails();

    }

    void downloadPartDetails()
    {
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        String server_url = sPref.getString("part_db_url", "");
        //Ensure that server_url always ends with /
        server_url = server_url.trim();
        if(server_url.charAt(server_url.length()-1) != '/')
        {
            server_url = server_url + "/";
        }

        String d = DownloadString(server_url + "show_data.php?pid=" + part_id);
        String[] datas = d.split("@<p>\n");

        int index = -1;
        //ignore Line showing Data Format
        for(int n=0;n<datas.length;n++)
        {
            if(!datas[n].contains("PID@NAME@"))
            {
               index = n;
            }
        }

        if(index == -1)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "No Part found", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
        else
        {
            part = new Part(datas[index]);
            applyPart(part);
        }


    }

    void applyPart(Part p)
    {
        text_name.setText(p.getName().trim());
        text_desc.setText(p.getDescription());
        text_instock.setText(String.valueOf(p.getInstock()));
        text_mininstock.setText(String.valueOf(p.getMininstock()));
        text_comment.setText(p.getComment());
        text_category.setText(p.getCategory());
        text_footprint.setText(p.getFootprint());
        text_storelocation.setText(p.getStorelocation());
    }

    public String DownloadString(String url_str) {
        try {
            URL url = new URL(url_str); //you can write here any link

            //Define InputStreams to read from the URLConnection.
            InputStream is = url.openStream();

            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            //We create an array of bytes
            byte[] data = new byte[50];
            int current;

            while((current = bis.read(data,0,data.length)) != -1){
                buffer.write(data,0,current);
            }

            return buffer.toString();

        } catch (IOException e) {
            Log.d("Part-DB", "Error: " + e);
            Toast toast = Toast.makeText(getApplicationContext(), "Error downloading!", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

        return "";
    }

}
