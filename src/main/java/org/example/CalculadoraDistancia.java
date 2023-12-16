package org.example;

public class CalculadoraDistancia {
    public static double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {

        // Raio médio da Terra em quilômetros
        final double raioTerra = 6371.0;

        // Converte as coordenadas de graus para radianos
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // Diferença de coordenadas
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        // Fórmula de Haversine
        double a = Math.sin(dlat / 2.0) * Math.sin(dlat / 2.0) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dlon / 2.0) * Math.sin(dlon / 2.0);

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        // Distância em quilômetros
        return raioTerra * c;
    }
}
