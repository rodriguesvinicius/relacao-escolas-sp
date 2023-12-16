package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.math3.util.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapsUtils {

    private static int quantidade = 0;

    public static Pair<String, String> getCoordinates(String search) {

        ++quantidade;

        System.out.println("getCordinates foi chamado: " + quantidade);

        try {
            System.out.println("start processing query: " + search);

            // Configuração do WebDriver usando WebDriverManager
            WebDriverManager.chromedriver().setup();

            // Configurações para executar o Chrome em modo headless
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");

            // Inicialização do WebDriver (no caso, usando o Chrome)
            WebDriver driver = new ChromeDriver(options);

            // URL do Google Maps
            String mapsUrl = "https://www.google.com/maps";

            // Navega para o Google Maps
            driver.get(mapsUrl);

            // Aguarde até que o campo de pesquisa esteja presente
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10000));
            WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));

            // Obtenha a URL antes de iniciar a pesquisa
            String initialUrl = driver.getCurrentUrl();

            // Insira a consulta no campo de pesquisa
            searchBox.sendKeys(search);

            System.out.println("aq" + searchBox.getAttribute("searchboxinput"));

            // Aguarde até que o botão de pesquisa esteja presente e visível
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='searchbox-searchbutton']")));

            // Realize a pesquisa e aguarde até que a URL mude
            searchButton.click();
            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(initialUrl)));

            // Obtenha a URL atual
            String currentUrl = driver.getCurrentUrl();

            System.out.println(currentUrl);

            // Extrai as coordenadas da URL
            String[] coordinates = extractCoordinatesFromUrl(currentUrl);

            // Imprime as coordenadas
            System.out.println("Latitude: " + coordinates[0]);
            System.out.println("Longitude: " + coordinates[1]);

            // Adiciona um tempo de espera de 5 segundos antes de fechar o navegador
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Fecha o navegador
            driver.quit();
            System.out.println("finished processing query: " + search);

            return Pair.create(coordinates[0], coordinates[1]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static String[] extractCoordinatesFromUrl(String url) {
        // Padrão regex para extrair as coordenadas da URL
        Pattern pattern = Pattern.compile("@(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)");
        Matcher matcher = pattern.matcher(url);

        // Verifica se o padrão foi encontrado na URL
        if (matcher.find()) {
            // Extrai as coordenadas de latitude e longitude
            String latitude = matcher.group(1);
            String longitude = matcher.group(2);

            return new String[]{latitude, longitude};
        } else {
            // Se não encontrar, retorna um array vazio ou trata de outra forma adequada ao seu caso
            return new String[]{"N/A", "N/A"};
        }
    }
}