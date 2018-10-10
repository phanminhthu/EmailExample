package danazone.com.emailexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Intent intent = this.getIntent();
        Toast.makeText(this, "" + intent.getStringExtra("jjj"), Toast.LENGTH_SHORT).show();
    }
}
