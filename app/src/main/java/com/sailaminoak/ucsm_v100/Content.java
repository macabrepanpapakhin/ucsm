package com.sailaminoak.ucsm_v100;

import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.Toolbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Content#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Content extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Content() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Content.
     */
    // TODO: Rename and change types and number of parameters
    public static Content newInstance(String param1, String param2) {
        Content fragment = new Content();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    GridView gridView;
   // private DrawerLayout drawerLayout;

    String[] gridviewdata={"Mentor's Contact","Student's Contacts","Schedule","number four","number five","number six","number seven","number eight"};
    int[] images={R.drawable.teacher,R.drawable.boy,R.drawable.schdule,R.drawable.airplane,R.drawable.arrow,R.drawable.ic_delete_black_24dp,R.drawable.brain,R.drawable.ic_image_black_24dp};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getContext(), "welcome from working", Toast.LENGTH_SHORT).show();
        View view=inflater.inflate(R.layout.fragment_content, container, false);
        gridView=view.findViewById(R.id.grid_view);
        MainAdaper mainAdaper=new MainAdaper(getContext(),gridviewdata,images);
        gridView.setAdapter(mainAdaper);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Toast.makeText(getContext(),gridviewdata[position],Toast.LENGTH_SHORT).show();
             if(position==0){
                 Intent intent=new Intent(getActivity(),MainActivity2.class);
                 startActivity(intent); }
             else{
                 Intent intent=new Intent(getActivity(),MainActivity2.class);
                 startActivity(intent);
             }
         }
     });



        return view;
    }
}