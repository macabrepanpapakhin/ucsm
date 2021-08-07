package com.sailaminoak.ucsm_v100;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
    DrawerLayout drawerLayout;
    SharedPreferences sharedPreferences;
    Button course,knowledge;
    NavigationView navigationView;

    String[] gridviewdata={"Mentor","Student","Canteen","Profile","Club","Map","Schedule","Top Graders"};
    int[] images={R.drawable.teachercolor,R.drawable.studentcolor,R.drawable.canteencolor,R.drawable.me,R.drawable.club,R.drawable.map,R.drawable.schdule,R.drawable.topgrader};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Toast.makeText(getContext(), "welcome from working", Toast.LENGTH_SHORT).show();
        View view=inflater.inflate(R.layout.fragment_content, container, false);
        Toolbar toolbar=view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.teacher);
        course=view.findViewById(R.id.courses);
        knowledge=view.findViewById(R.id.knowledgeSharing);
        knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),KnowledgeSharing.class));
            }
        });
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(new Intent(getActivity(),Courses.class));
            }
        });
        sharedPreferences =this.getActivity().getSharedPreferences("UCSM", Context.MODE_PRIVATE);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        drawerLayout=view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle=new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.open_navigation,R.string.close_navigation);
       // (AppCompatActivity)(getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        navigationView=view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_about:DisplayToast("about page");
                    break;
                    case R.id.nav_setting:DisplayToast("setting page");

                    break;
                    case R.id.nav_gpa:DisplayToast("gpa page");

                    break;
                    case R.id.nav_share:DisplayToast("share software");

                    break;
                    default:DisplayToast("Under Development");break;
                }
                return false;
            }

        });
        gridView=view.findViewById(R.id.grid_view);
        MainAdaper mainAdaper=new MainAdaper(getContext(),gridviewdata,images);
        gridView.setAdapter(mainAdaper);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //Toast.makeText(getContext(),gridviewdata[position],Toast.LENGTH_SHORT).show();
             if(position==0){
                 Intent intent=new Intent(getActivity(),MentorInformation.class);
                 startActivity(intent);
             }
             else if(position==1){
                 startActivity(new Intent(getActivity(),StudentInformation.class));

             }else if(position==3){
                 Intent intent=new Intent(getActivity(),Profile.class);
                 startActivity(intent);
             }else if(position==2){
                 Intent intent=new Intent(getActivity(),CanteenInformation.class);
                 startActivity(intent);
             }else if(position==4){
                 Intent intent=new Intent(getActivity(),ClubInformation.class);
                 startActivity(intent);
             }
             else if(position==6){
                 getActivity().finish();
                 startActivity(new Intent(getActivity(),ViewingSchedule.class));
             }
             else{
                 Intent intent=new Intent(getActivity(),MainActivity2.class);
                 startActivity(intent);
             }
         }
     });



        return view;
    }
    void DisplayToast(String msg){
        Toast.makeText(this.getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}