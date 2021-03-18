package terminal_heat_sink.keyboardpickershortcut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView countDown;

    final Handler handler = new Handler();
    private InputMethodManager imeManager;
    private int current;
    private int counter = 0;

    final Runnable r = new Runnable() {
        public void run() {
            imeManager.showInputMethodPicker();
            handler.postDelayed(checker, 500);
        }
    };

    final Runnable checker = new Runnable() {
        public void run() {
            counter++;
            if(counter > 50){ //5 seconds
                Log.i("checker", "stopping after long wait");
                handler.removeCallbacks(checker);
                finish();
                return;
            }
            countDown.setText("Closing in "+((5000-(counter*100))/1000)+"s");
            int check_for_new_id = 0;
            try {
                check_for_new_id = imeManager.getCurrentInputMethodSubtype().getNameResId();
            } catch (Exception e){

            }
            //Log.i("checker", "new id is "+ check_for_new_id);
            if(check_for_new_id == current){
                handler.postDelayed(checker, 100);
            }else{
                Log.i("checker", "detected input change");
                finish();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imeManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        LinearLayout main = findViewById(R.id.main);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        countDown = findViewById(R.id.timeout);
        countDown.setText("Closing in 5s");

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            current = imeManager.getCurrentInputMethodSubtype().getNameResId();
        }catch (Exception e){
            current = 0;
        }
        //Log.i("current input method is: ", ""+ current);
        handler.postDelayed(r, 200);

    }

}