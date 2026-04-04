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

        // Desactiva la comprobación de contraseñas seguras y filtradas
        options.addArguments("--disable-features=SafeBrowsingPasswordCheck");
        options.addArguments("--disable-autofill-keyboard-accessory-view");

        // Esto evita que Chrome intente "ayudarte" con las contraseñas
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
        // 1. LOGIN
        driver.get("http://127.0.0.1:5500/front/html/login.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usuarioLogin"))).sendKeys("CLIENTE");
        driver.findElement(By.id("contrasenaLogin")).sendKeys("CLIENTE");
        driver.findElement(By.id("btnIngresar")).click();

        // 2. IR AL INDEX Y SELECCIONAR PRODUCTO
        wait.until(ExpectedConditions.urlContains("index.html"));
        // Esperamos a que los productos carguen de la API
        WebElement btnVer = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Ver producto')]")));
        btnVer.click();

        // 3. AGREGAR AL CARRITO (Aquí estaba el fallo)
        wait.until(ExpectedConditions.urlContains("detalle_producto.html"));

        // Aseguramos que el botón de agregar exista y le damos clic con JS para evitar bloqueos
        WebElement btnAgregar = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Agregar al carrito')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnAgregar);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnAgregar);

        // 4. MANEJAR EL ALERT DE "Se agregaron unidades" (Obligatorio para que Selenium siga)
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alertExito = driver.switchTo().alert();
        System.out.println("Alerta confirmada: " + alertExito.getText());
        alertExito.accept();

        // 5. AHORA SÍ, IR AL CARRITO
        driver.get("http://127.0.0.1:5500/front/html/carrito.html");

        // 6. BYPASS DEL POP-UP DE CONTRASEÑA EN EL CARRITO
        // Esperamos que el elemento totalCart exista (si no hay alert de vacío, esto pasará)
        WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalCart")));

        // Si queremos cambiar a Factura, usamos JS para saltar el aviso de Chrome
        WebElement radioFactura = driver.findElement(By.id("rFactura"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radioFactura);

        // Validamos que el total no sea cero
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id("totalCart"), "0.00")));
        String montoFinal = driver.findElement(By.id("totalCart")).getText();

        System.out.println("Monto final en carrito: S/ " + montoFinal);
        assertNotEquals("0.00", montoFinal);
    }
}