package clima.clima;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.github.pwittchen.swipe.library.rx2.SwipeListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class select_lang extends AppCompatActivity {

    TextView choose_lang_text;
    public MaterialSpinner spinner;
    String selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lang);
        choose_lang_text = findViewById(R.id.select_lang);
         spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Bangla (India)", "English (India)", "English (United States)", "Hindi (India)", "Italian (Italy)", "Nepali (Nepal)", "Slovak (Slovakia)", "Thai (Thailand)");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selected = item.toString();
                Toast.makeText(select_lang.this, selected, Toast.LENGTH_SHORT).show();

            }
        });

        Swipe swipe = new Swipe();
        swipe.setListener(new SwipeListener() {
            @Override
            public void onSwipingLeft(MotionEvent event) {

            }

            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                return false;
            }

            @Override
            public void onSwipingRight(MotionEvent event) {
                Intent intent=new Intent(select_lang.this,Current_weather.class);
                intent.putExtra("KEY",selected);
                startActivity(intent);


            }

            @Override
            public boolean onSwipedRight(MotionEvent event) {
                Intent intent=new Intent(select_lang.this,Current_weather.class);
                intent.putExtra("KEY",selected);
                startActivity(intent);

                return true;
            }

            @Override
            public void onSwipingUp(MotionEvent event) {

            }

            @Override
            public boolean onSwipedUp(MotionEvent event) {
                return false;
            }

            @Override
            public void onSwipingDown(MotionEvent event) {

            }

            @Override
            public boolean onSwipedDown(MotionEvent event) {
                return false;
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(select_lang.this,Current_weather.class);
        intent.putExtra("KEY",selected);
        startActivity(intent);
    }


}
