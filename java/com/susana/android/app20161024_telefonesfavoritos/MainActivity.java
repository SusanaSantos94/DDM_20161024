package com.susana.android.app20161024_telefonesfavoritos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Declarar Membros de Dados
    static final int Num_Digitos_Dum_Telefone_Portugues = 9;

    Button mBtnEntrarTelefoneFavorito;
    EditText mEtEntrarTelefoneFavorito;

    Set<Long> mSetTelefonesFavoritos;

    View.OnClickListener mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito;


    void personalizarEditTextParaEntradaDeInteiros(
            EditText et,    // EditText que será personalizado
            int iMaxDigitos     // Numero Max. de digitos a aceitar no EditText
    ) {
        // et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);    // aceita tudo
        // et.setInputType(InputType.TYPE_CLASS_PHONE);                 // aceita virgulas
        et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);            // só aceita digitos de 0 a 9

        /* Este filtro estabelece o tamanho maximo do input*/
        InputFilter[] filtros = new InputFilter[1];
        filtros[0] = new InputFilter.LengthFilter(iMaxDigitos);

        /* TODO: Tentar manter NO_SUGGESTIONS mas ainda assim só aceitar digitos */

        et.setFilters(filtros);
    } //personalizarEditTextParaEntradaDeInteiros


    Boolean adicionarTelefoneFavorito() {
        /* 1. Consultar a EditText que tem o telefone a adicionar
        Garantir que é um inteiro. Se tudo estiver ok, guardar no Set  */

        String strTelCandidado = mEtEntrarTelefoneFavorito.getText().toString();
        try {
            Long novoTelefone = Long.parseLong(strTelCandidado);
            mSetTelefonesFavoritos.add(novoTelefone);
            // return true;  //se acontecesse estaria errado: ainda há + para fazer
        } catch (Exception e) {
            String strCorreuMalPorque = e.getMessage().toString();
            //mUtilFeedback(strCorreuMalPorque);
            return false;
        }

        /* 2. Como Set ficará modificado, se houver elementos de interface que dependem dos
        seus valores, esses elementos deveriam ser actualizados em runtime. */

        // TODO: ainda temos interface para mudar

        return false;
    } //adicionarTelefoneFavorito


    void init(){
        // Não podemos fazer Set<Long>() pq Ser é uma classe abstracta
        mSetTelefonesFavoritos = new HashSet<Long>();
        mBtnEntrarTelefoneFavorito = (Button) findViewById(R.id.id_btnEntrarTelefoneFavorito);
        mEtEntrarTelefoneFavorito = (EditText) findViewById(R.id.id_etEntrarTelefoneFavorito);

        personalizarEditTextParaEntradaDeInteiros(
                mEtEntrarTelefoneFavorito,
                Num_Digitos_Dum_Telefone_Portugues);

        mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarTelefoneFavorito();
            } //onClick
        }; // mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito -- onClickListener

        mBtnEntrarTelefoneFavorito.setOnClickListener(mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito);
    } //init


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    } //onCreate
}
