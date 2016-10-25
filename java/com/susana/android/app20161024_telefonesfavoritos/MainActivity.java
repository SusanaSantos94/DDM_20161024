package com.susana.android.app20161024_telefonesfavoritos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Declarar Membros de Dados
    static final int Num_Digitos_Dum_Telefone_Portugues = 9;
    static final int Telefones_Favoritos_Code = 123;

    Button mBtnEntrarTelefoneFavorito;
    EditText mEtEntrarTelefoneFavorito;
    Context mContext;
    LinearLayout mLlTelefonesFavoritos;

    Set<Long> mSetTelefonesFavoritos;

    View.OnClickListener mTratadorDeClicksNoButtonParaEntrarTelefoneFavorito;

    // ---------------------------------------------------------------------------------------------

    public boolean utilPedirQualquerPermissaoEmRuntime(String strPermissao){
       // XML: android.permission.CALL_PHONE;
       // JAVA: Manifest.permission.CALL_PHONE;

        /* Começar por verificar se em ocasiões anterior o user já não autorizou
        a app para a permissão em causa */

        int iResultadoDaConsula = ContextCompat.checkSelfPermission(
                mContext,
                strPermissao
        );

        boolean bAutorizada = iResultadoDaConsula == PackageManager.PERMISSION_GRANTED;

        if(bAutorizada){
            // app já estava previamente autorizada
            mUtilFeedback("App já está autorizada");
            return true;
        } // if --> já autorizado
        else {
            // app não estava previamente autorizada
            try {
                ActivityCompat.requestPermissions(
                        this,       // MainActivity.this
                        new String[]{strPermissao},            // array de permissões
                        Telefones_Favoritos_Code            // código numerico da nossa app
                );
                // TODO: Implementar um callback quando a resposta do user estiver disponivel
                return true;  // fomos capazes de fazer o pedido
            } catch (Exception e) {
                String strErroAoPedirPermissoes = e.getMessage().toString();
                mUtilFeedback(strErroAoPedirPermissoes);
                return false; //fomos incapazes de fazer o pedido
            }
        } // else --> não estava autorizada
    } //utilPedirQualquerPermissaoEmRuntime

    // ---------------------------------------------------------------------------------------------


    public boolean utilMarcarNumeroDeTelefone (String strNumero){
        return utilDialPhoneNumber(strNumero);
    }

    public boolean utilDialPhoneNumber(String strNumero){

        /* O código abixo servir apenas explicitar paralelos entre abrir um url
        externamente e marcar um número

        Intent abrirUrlComOSistema = new Intent(Intent.ACTION_VIEW);
        Uri url = Uri.parse("http://www.dn.pt");
        abrirUrlComOSistema.setData(url);
        // Confere uma "Activity Stack" própria ao componente
        abrirUrlComOSistema.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(abrirUrlComOSistema);

        */

        Intent telefonar = new Intent(Intent.ACTION_CALL);
        Uri dadosParaTelefonar = Uri.parse("Tel: " + strNumero);
        telefonar.setData(dadosParaTelefonar);
        try {
            startActivity(telefonar);
            return true;
        } catch (SecurityException e) {
            mUtilFeedback("Sem permissões runtime para telefonar");
            return false;
        }
    } //utilDialPhoneNumber


    void personalizarEditTextParaEntradaDeInteiros(
            EditText et,    // EditText que será personalizado
            int iMaxDigitos     // Numero Max. de digitos a aceitar no EditText
    ) {
        // et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);    // aceita tudo
        // et.setInputType(InputType.TYPE_CLASS_PHONE);                 // aceita virgulas

        /*
        Como aceitar numeros com virgula
        et.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);

        Como aceitar numeros com apenas com digitos de 0-9
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        */
        et.setInputType(InputType.TYPE_CLASS_NUMBER);

        /* Este filtro estabelece o tamanho maximo do input*/
        InputFilter[] filtros = new InputFilter[1];
        filtros[0] = new InputFilter.LengthFilter(iMaxDigitos);

        /* TODO: Tentar manter NO_SUGGESTIONS mas ainda assim só aceitar digitos */

        et.setFilters(filtros);
    } //personalizarEditTextParaEntradaDeInteiros


    public void mUtilFeedback(String mensagem){
        //  Por defeito defeito duração Long
        Toast t = Toast.makeText(
                mContext,
                mensagem,
                Toast.LENGTH_LONG
        );
        t.show();
    } // mUtilFeedback

    public void mUtilFeedback(String mensagem, int duracao){
        duracao = duracao < 30 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;

        Toast t = Toast.makeText(
                mContext,
                mensagem,
                Toast.LENGTH_SHORT
        );
        t.show();
    } //mUtilFeedback


    boolean adicionarTelefoneFavorito() {
        /* 1. Consultar a EditText que tem o telefone a adicionar
        Garantir que é um inteiro. Se tudo estiver ok, guardar no Set  */

        String strTelCandidado = mEtEntrarTelefoneFavorito.getText().toString();
        try {
            Long novoTelefone = Long.parseLong(strTelCandidado);
            mSetTelefonesFavoritos.add(novoTelefone);
            // return true;  //se acontecesse estaria errado: ainda há + para fazer
        } catch (Exception e) {
            String strCorreuMalPorque = e.getMessage().toString();
            mUtilFeedback(strCorreuMalPorque);
            return false;
        }

        /* 2. Como Set ficará modificado, se houver elementos de interface que dependem dos
        seus valores, esses elementos deveriam ser actualizados em runtime. */

        // TODO: ainda temos interface para mudar

        /* Já temos interface (mLlTelefonesFavoritos) */
        actualizarButtonsParaTelefonar();
        return false;
    } //adicionarTelefoneFavorito


    void actualizarButtonsParaTelefonar(){
        Long[] numerosDeTelefonesEnquantoArraysDeLongs = mSetTelefonesFavoritos.toArray(
                new Long[mSetTelefonesFavoritos.toArray().length]
        );

        // TODO: limpar o contentor antes de acrecentar Buttons

        for (
                Long n : numerosDeTelefonesEnquantoArraysDeLongs
        ){
           Button b = new Button(mContext);
            b.setText(n.toString());
            b.setOnClickListener(metodoQueRespondeAClicksEmNumeroDeTelefones);
            mLlTelefonesFavoritos.addView(b);
        }
    } //actualizarButtonsParaTelefonar

    View.OnClickListener metodoQueRespondeAClicksEmNumeroDeTelefones = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view instanceof Button){
                Button b = (Button) view;
                String numero = b.getText().toString();
                utilDialPhoneNumber(numero);
            } //if
        } //onClick
    };

    void init(){
        mContext = MainActivity.this;
        utilPedirQualquerPermissaoEmRuntime(Manifest.permission.CALL_PHONE);

        // Não podemos fazer Set<Long>() pq Ser é uma classe abstracta
        mSetTelefonesFavoritos = new HashSet<Long>();
        mBtnEntrarTelefoneFavorito = (Button) findViewById(R.id.id_btnEntrarTelefoneFavorito);
        mEtEntrarTelefoneFavorito = (EditText) findViewById(R.id.id_etEntrarTelefoneFavorito);
        mLlTelefonesFavoritos = (LinearLayout) findViewById(R.id.id_llTelefonesFavoritos);

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

        // utilDialPhoneNumber("123");
    } //init


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    } //onCreate
}
