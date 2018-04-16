package com.popgroup.encuestasv3;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.popgroup.encuestasv3.DataBase.DBHelper;
import com.popgroup.encuestasv3.FinEncuesta.FinEncuestaActivity;
import com.popgroup.encuestasv3.Model.FotoEncuesta;
import com.popgroup.encuestasv3.Model.RespuestasCuestionario;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jesus.hernandez on 21/12/16.
 *  captura la fotografia para esta version de la aplicacion
 */
public class Fotografia  extends AppCompatActivity {
    public String idEncuesta, encuesta, idTienda, idEstablecimiento, idArchivo, usuario, numPregunta;
    public byte[] byteArray;
    Bundle  bundle;
    Toolbar toolbar;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.btnFoto)
    Button btnFoto;
    @BindView(R.id.btnSiguiente)
    Button btnSiguiente;
    @BindView(R.id.imageView1)
    ImageView imgView1;
    @BindView(R.id.imageView2)
    ImageView imgView2;
    @BindView(R.id.imageView3)
    ImageView imgView3;
    @BindView(R.id.imageView4)
    ImageView imgView4;
    @BindView(R.id.imageView5)
    ImageView imgView5;
    DBHelper mDBHelper;
    Dao dao;
    ArrayList<RespuestasCuestionario> arrayResultados;
    Boolean banderaFotoTomada = false;
    ArrayList<String> arrayFotos , arrayNombrefoto;
    ArrayList<byte []> arrayByte;
    String ba1;
    int numFotos = 0;
    FotoEncuesta fotoEncuesta;
    private String TAG = getClass ().getSimpleName ();
    private int permissionCheck  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotografia);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtTitle.setText ("Fotografia");
        txtTitle.setTextSize(18);
        txtTitle.setTextColor(getBaseContext().getResources().getColor(R.color.colorTextPrimary));
        setSupportActionBar(toolbar);
        bundle= new Bundle();
        Bundle extras = getIntent().getExtras();
        //recivimos las variables
        idEncuesta = extras.getString("idEncuesta").toString();
        encuesta = extras.getString("encuesta").toString();
        idTienda = extras.getString("idTienda").toString();
        idEstablecimiento = extras.getString("idEstablecimiento").toString();
        idArchivo = extras.getString("idArchivo").toString();
        usuario = extras.getString("usuario").toString();
        numPregunta = extras.getString("numPregunta").toString();
        //armamos el bundle de nuevo
        bundle.putString("idEncuesta",idEncuesta);
        bundle.putString("encuesta",encuesta);
        bundle.putString("idTienda",idTienda);
        bundle.putString("idArchivo",idArchivo);
        bundle.putString("idEstablecimiento",idEstablecimiento);
        bundle.putString("usuario",usuario);
        bundle.putString("numPregunta", numPregunta);
        bundle.putString("numRespuesta","0");

        arrayFotos = new ArrayList<>();
        arrayNombrefoto = new ArrayList<>();
        arrayByte= new ArrayList<>();
        fotoEncuesta = new FotoEncuesta().getInstace();

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fotografia.this, FinEncuestaActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permissionCheck==0) {
                    open();
                }else{
                    Toast.makeText(getBaseContext(),"Debe proporcionar permisos para usar la camara ",Toast.LENGTH_LONG).show();
                }
            }
        });
           /* Tomar foto*/
        if(permissionCheck==-1) {
            Toast.makeText(getBaseContext(),"Debe proporcionar permisos para usar la camara ",Toast.LENGTH_LONG).show();
        }
    }

    //Open
    public void open() {

        Intent takePictureIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity (getPackageManager ()) != null) {
            startActivityForResult (takePictureIntent, 1);
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper ();
            mDBHelper = null;
        }
    }

    //ActivityResult Method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (resultCode == RESULT_OK) {
          if (requestCode == 1) {

                try {

                    Bundle extras = data.getExtras ();
                    Bitmap imageBitmap = (Bitmap) extras.get ("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    imageBitmap.compress (Bitmap.CompressFormat.JPEG, 100, bytes);

                    banderaFotoTomada = true;
                    Bitmap newBitmap = redimensionarIMG (imageBitmap, 200, 300);

                    ByteArrayOutputStream mbytes = new ByteArrayOutputStream();
                    Bitmap bitMapEnvio = redimensionarIMG (imageBitmap, 768, 1024);
                    bitMapEnvio.compress(Bitmap.CompressFormat.JPEG,60,mbytes);

                    String nombreFoto =  String.valueOf(System.currentTimeMillis()); // nombre del archivo

                    if (arrayFotos.size() == 0) {
                        imgView1.setImageBitmap(newBitmap);
                    }else if(arrayFotos.size() == 1){
                        imgView2.setImageBitmap(newBitmap);
                    }else if(arrayFotos.size() == 2){
                        imgView3.setImageBitmap(newBitmap);
                    }else if(arrayFotos.size() == 3){
                        imgView4.setImageBitmap(newBitmap);
                    }else if(arrayFotos.size() ==4){
                        imgView5.setImageBitmap(newBitmap);
                    }

                    byteArray = mbytes.toByteArray();
                    ba1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    arrayFotos.add(ba1);
                    arrayNombrefoto.add(nombreFoto);

                    if(arrayFotos.size() == 5){
                        fotoEncuesta.setIdEstablecimiento(idTienda);
                        fotoEncuesta.setIdEncuesta(idEncuesta);
                        fotoEncuesta.setNombre(arrayNombrefoto);
                        fotoEncuesta.setArrayFotos(arrayFotos);
                        btnFoto.setVisibility(View.GONE);
                    }else{
                        fotoEncuesta.setIdEstablecimiento(idTienda);
                        fotoEncuesta.setIdEncuesta(idEncuesta);
                        fotoEncuesta.setNombre(arrayNombrefoto);
                        fotoEncuesta.setArrayFotos(arrayFotos);

                    }

                } catch (Exception e) {
                    Log.e (TAG, "Exception " + e.getMessage ());
                }
          }
       }
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed ();
        try {

            DeleteBuilder<RespuestasCuestionario, Integer> deleteBuilder = dao.deleteBuilder ();
            deleteBuilder.where ().eq ("idpregunta", numPregunta);
            deleteBuilder.delete ();
        } catch (SQLException e) {
            Log.e (TAG, "SQLException" + e.getMessage ());
        }

        Intent intent = new Intent (Fotografia.this, Cuestionario.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras (bundle);
        startActivity (intent);
    }

    @Override
    protected void onResume () {
        super.onResume ();
        permissionCheck = ContextCompat.checkSelfPermission (Fotografia.this, Manifest.permission.CAMERA);
    }

    public Bitmap redimensionarIMG(Bitmap mBitmap, float newWidth, float newHeigth) {
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight =  newHeigth / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menuInicio) {
            //Display Toast
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }else if(id== R.id.menuSalir){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private DBHelper getmDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return mDBHelper;
    }
}
