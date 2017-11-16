package com.example.adm.pantallas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adm on 09/06/2017.
 */

public class Tab1Configuracion extends Fragment {
    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private Tab1ListViewAdapter listViewAdapter;
    private Tab1GridViewAdapter gridViewAdapter;
    private List<Tab1Product> productList;
    private int currentViewMode = 0;

    private Switch switch1;

    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;

    View rootView;
    Principal principal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.main_menu_tab1configuracion, container, false);

        principal = new Principal();

        //principal.getSharedPrefernces();

        switch1=(Switch) rootView.findViewById(R.id.switch1);


        stubList = (ViewStub) rootView.findViewById(R.id.stub_list);
        stubGrid = (ViewStub) rootView.findViewById(R.id.stub_grid);

        //Inflate ViewStub before get view

        stubList.inflate();
        stubGrid.inflate();

        listView = (ListView) rootView.findViewById(R.id.mylistview);
        gridView = (GridView) rootView.findViewById(R.id.mygridview);

        //get list of product
        getTab1ProductList();

        listView.setOnItemClickListener(onItemClick);
        gridView.setOnItemClickListener(onItemClick);

        switchView();

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(VIEW_MODE_LISTVIEW == currentViewMode) {
                    currentViewMode = VIEW_MODE_GRIDVIEW;
                } else {
                    currentViewMode = VIEW_MODE_LISTVIEW;
                }
                //Switch view
                switchView();
            }
        });

        return rootView;
    }



    private void switchView() {

        if(VIEW_MODE_LISTVIEW == currentViewMode) {
            //Display listview
            stubList.setVisibility(View.VISIBLE);
            //Hide gridview
            stubGrid.setVisibility(View.GONE);
        } else {
            //Hide listview
            stubList.setVisibility(View.GONE);
            //Display gridview
            stubGrid.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }

    private void setAdapters() {
        if(VIEW_MODE_LISTVIEW == currentViewMode) {
            listViewAdapter = new Tab1ListViewAdapter(this.getContext(), R.layout.main_menu_tab1_list_item, productList);
            listView.setAdapter(listViewAdapter);
        } else {
            gridViewAdapter = new Tab1GridViewAdapter(this.getContext(), R.layout.main_menu_tab1_list_item, productList);
            gridView.setAdapter(gridViewAdapter);
        }
    }

    public List<Tab1Product> getTab1ProductList() {
        //pseudo code to get product, replace your code to get real product here
        productList = new ArrayList<>();

        productList.add(new Tab1Product(R.drawable.ic_story, "Historias", "Descarga nuevas historias"));
        productList.add(new Tab1Product(R.drawable.ic_configuration, "Configuracion", "Configura la aplicacion para satisfacer tus gustos"));
        productList.add(new Tab1Product(R.drawable.ic_headphones, "Estadisticas", "Conoce toda tu información de tus salidas a correr"));
        productList.add(new Tab1Product(R.drawable.ic_friends, "Añadir Amigos", "Añade amigos nuevos para ver sus estadísticas y correr carreras"));
        productList.add(new Tab1Product(R.drawable.ic_user, "Perfil", "Actualiza tus datos del perfil"));
        productList.add(new Tab1Product(R.drawable.ic_configuration_screen, "Pantalla", "This is description 5"));
        productList.add(new Tab1Product(R.drawable.ic_run, "Cerrar Sesion", "Cerrar tu sesión actual."));


/*        productList.add(new Tab1Product(R.drawable.ic_view_grid, "Title 6", "This is description 6"));
        productList.add(new Tab1Product(R.drawable.ic_view_list, "Title 7", "This is description 7"));
        productList.add(new Tab1Product(R.drawable.ic_tree, "Title 8", "This is description 8"));
        productList.add(new Tab1Product(R.drawable.ic_feliz, "Title 9", "This is description 9"));
        productList.add(new Tab1Product(R.drawable.ic_feliz, "Title 10", "This is description 10"));*/

        return productList;
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Do any thing when user click to item
            Log.d("PRINCIPAL" , productList.get(position).getTitle() + " - " + productList.get(position).getDescription());
            Toast.makeText(getContext(), "HOLA"/*productList.get(position).getTitle() + " - " + productList.get(position).getDescription()*/,Toast.LENGTH_SHORT);
            Class[] classes = {Principal.class, Configuracion.class, Estadisticas.class, anadir_amigos.class,Perfil.class, ConfiguracionPantalla.class, Historias.class,Login.class };
            int i=0;
            if(productList.get(position).getTitle().equals("Configuracion"))
                i=1;
            if(productList.get(position).getTitle().contains("Amigos"))
                i=3;
            if(productList.get(position).getTitle().equals("Perfil"))
                i=4;
            if(productList.get(position).getTitle().equals("Pantalla"))
                i=5;
            if(productList.get(position).getTitle().equals("Historias"))
                i=6;

            if(productList.get(position).getTitle().equals("Cerrar Sesion")){
                getContext().getSharedPreferences("infoUsuario", Context.MODE_PRIVATE).edit().clear().commit();
                i=7;
            }

            if(productList.get(position).getTitle().equals("Estadisticas"))
            {
                i=2;

                SharedPreferences sharedPreferences =  getContext().getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
                String id1= sharedPreferences.getString("id","");

                //Log.d("intent", id1);

                Intent intent = new Intent(getContext(), Estadisticas.class);
                intent.putExtra("id", id1);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getContext(), classes[i]);
                startActivity(intent);
            }




        }
    };

/*    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_1:
                if(VIEW_MODE_LISTVIEW == currentViewMode) {
                    currentViewMode = VIEW_MODE_GRIDVIEW;
                } else {
                    currentViewMode = VIEW_MODE_LISTVIEW;
                }
                //Switch view
                switchView();

                break;
        }
        return true;
    }
}
