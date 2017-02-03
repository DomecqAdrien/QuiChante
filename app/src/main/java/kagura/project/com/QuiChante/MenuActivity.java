package kagura.project.com.QuiChante;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MenuActivity extends AppCompatActivity {

    Intent intentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //noinspection ConstantConditions
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_menu);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Quitter")
                .setMessage("Êtes vous sûrs de vouloir quitter l'application ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Non", null).show();
    }

    public void onClickYear(View view) {
        intentGame = new Intent(this, GameActivity.class);
        switch (view.getId()){
            case R.id.button50:
                intentGame.putExtra("year", 50);
                this.startActivity(intentGame);
                break;
            case R.id.button60:
                intentGame.putExtra("year", 60);
                this.startActivity(intentGame);
                break;
            case R.id.button70:
                intentGame.putExtra("year", 70);
                this.startActivity(intentGame);
                break;
            case R.id.button80:
                intentGame.putExtra("year", 80);
                this.startActivity(intentGame);
                break;
        }
    }
}
