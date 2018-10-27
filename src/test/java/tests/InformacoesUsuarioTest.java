package tests;

import static org.junit.Assert.*;

import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import suporte.Generator;
import suporte.Screenshot;
import suporte.Web;

import java.util.concurrent.TimeUnit;

//@RunWith(DataDrivenTestRunner.class)
//@DataLoader(filePaths = "InformacoesUsuarioPageObjectsTest.csv")
public class InformacoesUsuarioTest {
    private WebDriver navegador;

    @Rule
    public TestName test = new TestName();

    @Before
    public void setUp(){
        navegador = Web.createChrome();


        WebElement formularioSigninBox = navegador.findElement(By.id("signinbox"));
        formularioSigninBox.findElement(By.name("login")).sendKeys("julio0001");
        formularioSigninBox.findElement(By.name("password")).sendKeys("123456");
        navegador.findElement(By.linkText("SIGN IN")).click();

        navegador.findElement(By.className("me")).click();
        navegador.findElement(By.linkText("MORE DATA ABOUT YOU")).click();
    }

    @Test
    public void testAdicionarUmaInformacaoAdiconalDoUsuario(@Param(name="tipo")String tipo, @Param(name="contato")String contato, @Param(name="mensagem")String mensagemEsperada){

        navegador.findElement(By.xpath("//button[@data-target=\"addmoredata\"]")).click();

        WebElement popUpAddMoreData = navegador.findElement(By.id("addmoredata"));
        WebElement campoType = popUpAddMoreData.findElement(By.name("type"));
        new Select(campoType).selectByVisibleText(tipo);
        popUpAddMoreData.findElement(By.name("contact")).sendKeys(contato);
        popUpAddMoreData.findElement(By.linkText("SAVE")).click();

        WebElement validaMsg = navegador.findElement(By.id("toast-container"));
        String msgPopup = validaMsg.getText();
        assertEquals(mensagemEsperada, msgPopup);
    }

    //@Test
    public void removerUmContatoDeUmUsuario(){

        navegador.findElement(By.xpath("//span[text()=\"444444\"]/following-sibling::a")).click();
        //Confirmar a janela javascript
        navegador.switchTo().alert().accept();

        WebElement validaMsg = navegador.findElement(By.id("toast-container"));
        String msgPopup = validaMsg.getText();
        assertEquals("Rest in peace, dear phone!",msgPopup);

        //Salvando Screenshot
        String screenshotArquivo = "C:\\Users\\msouza7\\Estudo\\test-report\\taskit\\" + Generator.dataHoraParaArquivo()+"_"+test.getMethodName()+".png";
        Screenshot.tirar(navegador,screenshotArquivo );

        //Aguardar ate 10 segundos para que a janela desapareça
        WebDriverWait aguardar = new WebDriverWait(navegador, 10);
        aguardar.until(ExpectedConditions.stalenessOf(validaMsg));

        navegador.findElement(By.linkText("Logout")).click();
    }

    @After
    public void tearDown(){
        navegador.quit();
    }
}
