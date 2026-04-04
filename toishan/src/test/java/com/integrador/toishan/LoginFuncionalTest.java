package com.integrador.toishan;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginFuncionalTest {

    private WebDriver driver;
    private WebDriverWait wait;



    @BeforeEach
    void setupTest() {
        // Silencia los logs innecesarios de Selenium en la consola
        System.setProperty("webdriver.chrome.silentOutput", "true");
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.OFF);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        // Esto ayuda a evitar errores de compatibilidad de protocolo
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Un poco más de tiempo
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testLoginExitoso() {
        driver.get("http://127.0.0.1:5500/front/html/login.html"); // Tu URL local

        driver.findElement(By.id("usuarioLogin")).sendKeys("ADMIN");
        driver.findElement(By.id("contrasenaLogin")).sendKeys("ADMIN");
        driver.findElement(By.id("btnIngresar")).click();

        // Esperamos a que la URL cambie al index
        wait.until(ExpectedConditions.urlContains("index.html"));

        assertTrue(driver.getCurrentUrl().contains("index.html"));
    }
}