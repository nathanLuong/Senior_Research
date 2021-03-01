package com.example.senior_research;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class PostFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_post, null);
        final TableLayout table = (TableLayout)view.findViewById(R.id.table);
        Button addExercise = (Button)view.findViewById(R.id.buttonAdd);
        final Button upload = (Button)view.findViewById(R.id.Upload);
        final EditText title = (EditText) view.findViewById(R.id.title);
        final EditText notes = (EditText) view.findViewById(R.id.notes);
        final int i=20;
        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow toAdd = new TableRow(getActivity());
                EditText e = new EditText(getContext());
                e.setId(i);
                e.setHint("Exercise");
                toAdd.addView(e);
                EditText s = new EditText(getContext());
                s.setId(i+1);
                s.setHint("Sets");
                toAdd.addView(s);
                TextView x = new TextView(getContext());
                x.setId(i+2);
                x.setText("x");
                toAdd.addView(x);
                EditText r = new EditText(getContext());
                r.setId(i);
                r.setHint("Reps");
                toAdd.addView(r);
                table.addView(toAdd);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                for(int i=0; i<table.getChildCount(); i++){
                    for(int j=0; j<4; j++) {
                        if(j!=2)
                        list.add(getTableLayoutCell(table, i, j));
                    }
                }
                String uploadString = "";
                for(int i=0; i<list.size(); i++){
                    uploadString+=(list.get(i).toString() + ", ");
                }
                System.out.println(uploadString);


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Workouts");

                String postid = reference.push().getKey();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("postid", postid);
                hashMap.put("title", title.getText().toString());
                hashMap.put("workout", uploadString);
                hashMap.put("notes", notes.getText().toString());
                hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                reference.child(postid).setValue(hashMap);
                Toast.makeText(getContext(), "Successfully Uploaded Workout", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }
    public String getTableLayoutCell(TableLayout layout, int rowNo, int columnNo) {
        if (rowNo >= layout.getChildCount()) return null;
        TableRow row = (TableRow) layout.getChildAt(rowNo);

        if (columnNo >= row.getChildCount()) return null;
        return ((EditText)row.getChildAt(columnNo)).getText().toString();

    }
}
