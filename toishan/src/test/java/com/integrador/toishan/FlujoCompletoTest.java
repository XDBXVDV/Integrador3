package com.integrador.toishan;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlujoCompletoTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-features=SafeBrowsingPasswordCheck");
        options.addArguments("--disable-autofill-keyboard-accessory-view");
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }



    @Test
    @DisplayName("Flujo E2E: Login -> Agregar Real -> Pagar")
    void testFlujoCompletoCompra() {

        driver.get("http://127.0.0.1:5500/front/html/login.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usuarioLogin"))).sendKeys("CLIENTE");
        driver.findElement(By.id("contrasenaLogin")).sendKeys("CLIENTE");
        driver.findElement(By.id("btnIngresar")).click();

        wait.until(ExpectedConditions.urlContains("index.html"));

        WebElement btnVer = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Ver producto')]")));
        btnVer.click();

        wait.until(ExpectedConditions.urlContains("detalle_producto.html"));

        WebElement btnAgregar = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Agregar al carrito')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnAgregar);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnAgregar);

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alertExito = driver.switchTo().alert();
        System.out.println("Alerta confirmada: " + alertExito.getText());
        alertExito.accept();

        driver.get("http://127.0.0.1:5500/front/html/carrito.html");

        WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalCart")));

        WebElement radioFactura = driver.findElement(By.id("rFactura"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radioFactura);

        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id("totalCart"), "0.00")));
        String montoFinal = driver.findElement(By.id("totalCart")).getText();

        System.out.println("Monto final en carrito: S/ " + montoFinal);
        assertNotEquals("0.00", montoFinal);
    }
}