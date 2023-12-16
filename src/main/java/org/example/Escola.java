package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Escola implements Comparable<Escola> {

    private String dre;
    private String codesc;
    private String tipoesc;
    private String nomes;
    private String nomescofi;
    private String ceu;
    private String diretoria;
    private String subpref;
    private String endereco;
    private String numero;
    private String bairro;
    private String cep;
    private String tel1;
    private String tel2;
    private String fax;
    private String situacao;
    private String coddist;
    private String distrito;
    private String setor;
    private String codinep;
    private String cdCie;
    private String eh;
    private String fxEtaria;
    private String dtCriacao;
    private String atoCriacao;
    private String domCriacao;
    private String dtIniConv;
    private String dtAutoriza;
    private String dtExtincao;
    private String nomeAnt;
    private String rede;
    private double latitude;
    private double longitude;
    private String database;
    private double minhaLatitude;
    private double minhaLongitude;


    public Escola() {
        this.minhaLatitude = -23.547248521674685;
        this.minhaLongitude = -46.60573153485834;
    }

    @Override
    public int compareTo(Escola outraEscola) {
        double distanciaThis = CalculadoraDistancia.calcularDistancia(
                minhaLatitude, minhaLongitude, this.getLatitude(), this.getLongitude());
        double distanciaOutra = CalculadoraDistancia.calcularDistancia(
                outraEscola.getMinhaLatitude(), outraEscola.getMinhaLongitude(),
                outraEscola.getLatitude(), outraEscola.getLongitude());
        return Double.compare(distanciaThis, distanciaOutra);
    }

}
