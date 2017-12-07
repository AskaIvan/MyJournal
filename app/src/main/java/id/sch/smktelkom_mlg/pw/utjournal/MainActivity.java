package id.sch.smktelkom_mlg.pw.utjournal;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    AppBarLayout Appbar;
    CollapsingToolbarLayout collTolbar;
    Toolbar toolbar;

    boolean ExpandedAction = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Appbar = findViewById(R.id.appbar);
        collTolbar = findViewById(R.id.ctolbar);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        Appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 200) {
                    ExpandedAction = false;
                    collTolbar.setTitle("Test One");
                    invalidateOptionsMenu();
                } else {
                    ExpandedAction = true;
                    collTolbar.setTitle("Test Two");
                    invalidateOptionsMenu();
                }
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
