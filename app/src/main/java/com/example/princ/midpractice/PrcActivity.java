package com.example.princ.midpractice;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PrcActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prc);

        relativeLayout=findViewById(R.id.prc);
        fab=findViewById(R.id.fab);

        Snackbar.make(relativeLayout, "Welcome", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PrcActivity.this, "I am fab", Toast.LENGTH_SHORT).show();
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Click", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
            }
        });
    }
}
