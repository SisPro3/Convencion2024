package com.abarrotescasavargas.convencion.Staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.abarrotescasavargas.convencion.Gerencia.Reuniones.ActivityReuniones;
import com.abarrotescasavargas.convencion.R;
import com.abarrotescasavargas.convencion.Staff.Comidas.ActivityAddComidas;
import com.abarrotescasavargas.convencion.Staff.Registrados.ActivityRegistrados;

public class ActivityStaff extends AppCompatActivity {
    private ImageButton addComidas,registrados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setup();
        events();
    }
    private void setup()
    {
        addComidas= findViewById(R.id.comidas);
        registrados= findViewById(R.id.personas);

    }
    private void events() {
        addComidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityStaff.this, ActivityAddComidas.class);
                startActivity(intent);
            }
        });
        registrados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityStaff.this, ActivityRegistrados.class);
                startActivity(intent);
            }
        });

    }

}