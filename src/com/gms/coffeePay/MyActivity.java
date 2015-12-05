package com.gms.coffeePay;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Vector;

public class MyActivity extends Activity {
    ListView listView;
    Vector <Recurso> recursos ;
    ArrayAdapter<String> adapter;
    SQLiteDatabase db;

    private void mostrarOrdenados(){
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        Vector <Recurso> recursos2 ;
        recursos2=new Vector();
        int totalPagos=0;
        boolean incluido;
        for (int i=0;i<recursos.size();i++){
            totalPagos=totalPagos+recursos.elementAt(i).pagos;
        }
        recursos2.add(recursos.elementAt(0));
        for (int i=1;i<recursos.size();i++){
            incluido=false;
            for (int j=0;j<recursos2.size() && !incluido;j++){
                if (recursos.elementAt(i).orden(totalPagos)<recursos2.elementAt(j).orden(totalPagos)){
                    recursos2.add(j,new Recurso(recursos.elementAt(i)));
                    incluido=true;
                }
            }
                if (!incluido){
                    recursos2.add(recursos2.size(),new Recurso(recursos.elementAt(i)));
                }
        }
        recursos=(Vector <Recurso>) recursos2.clone();
        for (int i=0;i<recursos.size();i++){
            if (i==0) {
                adapter.add("Paga: "+recursos.elementAt(i).toString());
            }
            else {adapter.add(recursos.elementAt(i).toString());}
        }
        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            recursos= new Vector();



            // Get ListView object from xml
            listView = (ListView) findViewById(R.id.list);
            db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS pagadores(pagos INTEGER,prioridad INTEGER,nombre VARCHAR,ausencias INTEGER);");

            Cursor c=db.rawQuery("SELECT * FROM pagadores;", null);
            if(c.getCount()>0)
            {
                c.moveToFirst();
                for (int i=0; i<c.getCount();i++){
                    recursos.add(new Recurso(c.getInt(0),c.getInt(1),c.getString(2),c.getInt(3)));
                    c.moveToNext();
                }
            }
            else {//inicializacion
                reiniciarValores();
            }


            mostrarOrdenados();
        //setOnItemLongClickListener
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;

                // Show Alert
                recursos.elementAt(itemPosition).ausencias++;
                Toast.makeText(getApplicationContext(), "Anotada la ausencia de " + recursos.elementAt(itemPosition), Toast.LENGTH_SHORT).show();

                mostrarOrdenados();
                restaurarBD();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;

                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                recursos.elementAt(itemPosition).pagos++;
                Toast.makeText(getApplicationContext(), "Anotado el pago de " + recursos.elementAt(itemPosition), Toast.LENGTH_SHORT).show();
                mostrarOrdenados();
                    restaurarBD();
                }

            });
        }
    private void restaurarBD()
    {
        db.execSQL("DROP TABLE pagadores;");
        db.execSQL("CREATE TABLE IF NOT EXISTS pagadores(pagos INTEGER,prioridad INTEGER,nombre VARCHAR,ausencias INTEGER);");
        for (int i=0;i<recursos.size();i++) {
            db.execSQL("INSERT INTO pagadores VALUES('" + recursos.elementAt(i).pagos + "','" + recursos.elementAt(i).prioridad + "','" + recursos.elementAt(i).nombre + "','"+ recursos.elementAt(i).ausencias + "');");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("trampa");
        menu.add("anadir");
        menu.add("reiniciar");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        if (item.getTitle()=="reiniciar")
        {
            reiniciarValores();
        }
        if (item.getTitle()=="anadir")
        {
            //todo
            reiniciarValores();
        }
        return super.onOptionsItemSelected(item);
    }
    public void reiniciarValores()
    {
        recursos.clear();
        recursos.add(new Recurso(1,"Guillermo"));
        recursos.add(new Recurso(2,"Aitor"));
        recursos.add(new Recurso(3,"Maria"));
        recursos.add(new Recurso(4,"Ander"));
        recursos.add(new Recurso(5,"Dani"));
        mostrarOrdenados();
        restaurarBD();
    }
    /*private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        Introduce editNameDialog = new Introduce();
        editNameDialog.show(fm, "fragment_edit_name");
    }*/
    }
