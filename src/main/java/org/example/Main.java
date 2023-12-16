package org.example;

import org.apache.commons.math3.util.Pair;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    private static List<Escola> escolasError = new ArrayList<>();

    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String... args) {

        try {

            System.out.println("Started processing " + LocalDateTime.now());

            List<Escola> escolas = new ArrayList<>(ExcelUtils.readSchoolList());

            System.out.println(escolas.size());
            int tamanhoDoLote = 2;

            List<List<Escola>> lotes = quebrarEmLotes(escolas, tamanhoDoLote);

//            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//            int numeroDeLotes = 0;
//
//            for (List<Escola> lote : lotes) {
//                executorService.execute(() -> {
//                    processEscola(lote);
//                });
//                ++numeroDeLotes;
//                System.out.println("Numero de lotes iniciados: " + numeroDeLotes + "/" + lotes.size());
//            }
//
//            executorService.shutdown();
//            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            Collections.sort(escolas);
            ExcelUtils.createExcel(escolas);

            System.out.println("Finished processing " + LocalDateTime.now());
        } catch (Exception e) {
            System.out.println("Houve um erro ao realizar a chamada " + e.getMessage());
        }
    }

    private static void processEscola(List<Escola> escolas) {
        for (Escola escola : escolas) {
            try {
                Pair<String, String> pair = MapsUtils.getCoordinates(escola.getEndereco()
                        + ", " + escola.getNumero() + "," +
                        escola.getTipoesc() + " " + escola.getNomes() + "," + "CEP" + escola.getCep());
                escola.setLatitude(Double.parseDouble(pair.getFirst()));
                escola.setLongitude(Double.parseDouble(pair.getSecond()));
            } catch (Exception e) {
                escolasError.add(escola);
                System.out.println("Houve um erro ao processar essa escola: " + escola.getNomes());
            }
        }
    }

    private static <T> List<List<T>> quebrarEmLotes(List<T> listaOriginal, int tamanhoDoLote) {
        List<List<T>> lotes = new ArrayList<>();
        for (int i = 0; i < listaOriginal.size(); i += tamanhoDoLote) {
            int fim = Math.min(i + tamanhoDoLote, listaOriginal.size());
            List<T> lote = listaOriginal.subList(i, fim);
            lotes.add(lote);
        }
        return lotes;
    }
}