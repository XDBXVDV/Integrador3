package com.integrador.toishan;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class CarritoFuncionalTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }



    @Test
    @DisplayName("Debe calcular IGV correctamente al seleccionar Factura")
    void testCalculoFactura() throws InterruptedException {
        // 1. Inyectar un producto al carrito vía LocalStorage para el test
        // (Esto evita tener que navegar por toda la tienda en cada test)
        driver.get("http://127.0.0.1:5500/front/html/login.html");

        String scriptCarrito = "localStorage.setItem('carrito', JSON.stringify([{idProducto:1, nombre:'Repuesto Test', precio:100.00, cantidad:1, imagen:'test.jpg', stock:10}]));";
        ((JavascriptExecutor) driver).executeScript(scriptCarrito);

        // 2. Loguearse para tener acceso
        driver.findElement(By.id("usuarioLogin")).sendKeys("ADMIN");
        driver.findElement(By.id("contrasenaLogin")).sendKeys("ADMIN");
        driver.findElement(By.id("btnIngresar")).click();

        // 3. Ir al carrito
        driver.get("http://127.0.0.1:5500/front/html/carrito.html");

        // 4. Verificar subtotal inicial (S/ 100.00)
        WebElement subtotal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subtotalCart")));
        assertEquals("100.00", subtotal.getText());

        // 5. Cambiar a FACTURA y verificar IGV
        WebElement radioFactura = driver.findElement(By.id("rFactura"));
        radioFactura.click();

        // Esperamos a que la fila del IGV sea visible (tu JS cambia el display)
        WebElement filaIGV = driver.findElement(By.id("filaIGV"));
        wait.until(ExpectedConditions.attributeContains(filaIGV, "style", "flex"));

        WebElement igvValue = driver.findElement(By.id("igvCart"));
        WebElement totalValue = driver.findElement(By.id("totalCart"));

        assertEquals("18.00", igvValue.getText(), "El IGV no es el 18%");
        assertEquals("118.00", totalValue.getText(), "El total no incluye el IGV");
    }

    @Test
    @DisplayName("Debe validar longitud de RUC para Factura")
    void testValidacionRUC() {
        // 1. Ir al login
        driver.get("http://127.0.0.1:5500/front/html/login.html");

        // 2. IMPORTANTE: Inyectar un producto para que el carrito NO esté vacío
        String scriptCarrito = "localStorage.setItem('carrito', JSON.stringify([{idProducto:1, nombre:'Producto Test', precio:50.00, cantidad:1, stock:10}]));";
        ((JavascriptExecutor) driver).executeScript(scriptCarrito);

        // 3. Login rápido
        driver.findElement(By.id("usuarioLogin")).sendKeys("CLIENTE");
        driver.findElement(By.id("contrasenaLogin")).sendKeys("CLIENTE");
        driver.findElement(By.id("btnIngresar")).click();

        // 4. Ir al carrito
        driver.get("http://127.0.0.1:5500/front/html/carrito.html");

        // 5. Seleccionar Factura e ingresar RUC corto
        wait.until(ExpectedConditions.elementToBeClickable(By.id("rFactura"))).click();

        WebElement inputDoc = driver.findElement(By.id("nroDocumento"));
        inputDoc.clear();
        inputDoc.sendKeys("123"); // RUC inválido

        // 6. Clic en Pagar
        driver.findElement(By.id("btnPagar")).click();

        // 7. Verificar la Alerta de JS
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String textoAlerta = alert.getText();

            // Validamos que el mensaje sea el correcto
            assertTrue(textoAlerta.contains("RUC inválido"), "El mensaje de la alerta fue: " + textoAlerta);

            alert.accept(); // Cerrar la alerta
        } catch (TimeoutException e) {
            fail("No apareció ninguna alerta de validación de RUC.");
        }
    }
}