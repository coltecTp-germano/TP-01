package coltectp.github.io.movieme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //TODO(4) Inicializar os spinners (olhar o PetsApp)

        Spinner spinnerG = (Spinner) findViewById(R.id.generos);
        Spinner spinnerA = (Spinner) findViewById(R.id.idades);


        // Spinner de gêneros

        ArrayAdapter<CharSequence> adapterG = ArrayAdapter.createFromResource(this,
                R.array.genres, android.R.layout.simple_spinner_item);

        adapterG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerG.setAdapter(adapterG);
        spinnerG.setPrompt("Selecione um gênero");


        // Spinner de faixa etária

        ArrayAdapter<CharSequence> adapterA = ArrayAdapter.createFromResource(this,
                R.array.genres, android.R.layout.simple_spinner_item);

        adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerA.setAdapter(adapterA);
        spinnerA.setPrompt("Selecione uma faixa etária");
    }
}
