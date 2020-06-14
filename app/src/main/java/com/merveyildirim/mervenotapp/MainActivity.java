package com.merveyildirim.mervenotapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button kaydet;
    Button goster;
    Button sil;
    Button guncelle;
    EditText ad;
    EditText soyad;
    EditText numara;
    EditText sehir;
    TextView bilgiler;
    private veritabani v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v1=new veritabani(this);

        kaydet=(Button) findViewById(R.id.buttonkayit);
        goster=(Button) findViewById(R.id.buttongoster);
        sil=(Button) findViewById(R.id.buttonsil);
        guncelle=(Button) findViewById(R.id.buttonguncelle);
        ad=(EditText)findViewById(R.id.editTextad);
        soyad=(EditText)findViewById(R.id.editTextsoyad);
        numara=(EditText)findViewById(R.id.editTextnumara);
        sehir=(EditText)findViewById(R.id.editTextsehir);
        bilgiler=(TextView)findViewById(R.id.textViewbilgiler);

        bilgiler.setMovementMethod(new ScrollingMovementMethod());

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    kayitekle(ad.getText().toString(),soyad.getText().toString(),numara.getText().toString(),sehir.getText().toString());
                    Toast.makeText(getApplicationContext(), "Kişi kaydedildi.", Toast.LENGTH_LONG).show();
                }
                finally
                {
                    v1.close();

                }
            }
        });
        Cursor cursor = kayitgetir();
        kayitgoster(cursor);
        goster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = kayitgetir();
                kayitgoster(cursor);
            }
        });

        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Uyarı");
                builder.setMessage("Kayıt silinsin mi?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet, sil.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        silme(ad.getText().toString());
                        Toast.makeText(getApplicationContext(), "Kişi adres defterinden silindi.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });

        guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Guncelle(ad.getText().toString(),soyad.getText().toString(),numara.getText().toString(),sehir.getText().toString());
                Toast.makeText(getApplicationContext(), "Kişi güncellendi.", Toast.LENGTH_LONG).show();

            }
        });
    }

    public  void Guncelle(String ad, String soyad, String numara,String sehir)
    {
        SQLiteDatabase db=v1.getWritableDatabase();
        ContentValues cvGuncelle=new ContentValues();
        cvGuncelle.put("ad",ad);
        cvGuncelle.put("soyad",soyad);
        cvGuncelle.put("numara",numara);
        cvGuncelle.put("sehir",sehir);
        db.update("kisibilgi",cvGuncelle,"ad"+"=?",new String[]{ad});
        db.close();
    }


    private void silme(String adi)
    {
        SQLiteDatabase db=v1.getReadableDatabase();
        db.delete("kisibilgi","ad"+"=?",new String[]{adi});
    }

    private void kayitekle(String adi,String soyadi,String numarasi,String sehri)
    {
        SQLiteDatabase db=v1.getWritableDatabase();
        ContentValues veriler=new ContentValues();
        veriler.put("ad",adi);
        veriler.put("soyad",soyadi);
        veriler.put("numara",numarasi);
        veriler.put("sehir",sehri);
        db.insertOrThrow("kisibilgi",null,veriler);
    }
    private String[] sutunlar={"ad","soyad","numara","sehir"};
    private Cursor kayitgetir()
    {
        SQLiteDatabase db = v1.getReadableDatabase();
        Cursor okunanlar = db.query("kisibilgi", sutunlar, null, null, null, null, null);
        return okunanlar;
    }
    private void kayitgoster(Cursor goster)
    {
        StringBuilder builder = new StringBuilder();
        while( goster.moveToNext()) {
            String add =  goster.getString( goster.getColumnIndex("ad"));
            String soyadd =  goster.getString(( goster.getColumnIndex("soyad")));
            String sehirr=  goster.getString(( goster.getColumnIndex("sehir")));
            String numaraa =  goster.getString(( goster.getColumnIndex("numara")));
            builder.append("ad: ").append(add+"\n" );
            builder.append("soyad:").append(soyadd+"\n");
            builder.append("şehir: ").append(sehirr+"\n");
            builder.append("numara: ").append(numaraa+"\n");
            builder.append("------------- ").append("\n");
        }
        TextView text = (TextView)findViewById(R.id.textViewbilgiler);
        text.setText(builder);
    }


}