package ru.taptm.shurikus.androidruntimepermissiontutorial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1234;
    private ListView mListContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonReadContacts = (Button) findViewById(R.id.btn_permission_read_contacts);
        mListContacts = (ListView) findViewById(R.id.list_contacts);

        buttonReadContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContacts();
            }
        });
    }

    public void getContacts(){
        //Проверяем есть ли у нас разрешение на чтение контактов
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                //Здесь выводим сообщение в котором поясняем почему необходимы разрешение
                Toast.makeText(this,"Need for show contacts. Please turn on permission at [Setting]>[Permission]", Toast.LENGTH_SHORT).show();
            } else {
                //Запрашиваем разрешение
                ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
            return;
        }

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if(phones == null){
            return;
        }

        List<String> nameList = new ArrayList<>();
        while (phones.moveToNext()) {
            String user = "";
            user = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            nameList.add(user);
        }
        phones.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1
                , android.R.id.text1, nameList);
        mListContacts.setAdapter(adapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Здесь мы получаем разрешение и снова вызываем getContacts()
                    getContacts();
                } else {
                    Toast.makeText(this, "Read contacts permission is denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


}
