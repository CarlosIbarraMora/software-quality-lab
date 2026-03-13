package mx.edu.cetys.Software_Quality_Lab.poc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.springframework.test.util.AssertionErrors.*;

public class ParametrizedCalculatorTest {

    @DisplayName("Test Sum of two parameters with CSV Source")
    @ParameterizedTest
    @CsvSource({
            "10 ,10,20",
            "0,0,0,5",
            "-5,5,0",
            "'1','2','3'" //char type is valid
    })
    //@Test Suspicious
    void testSumWithCsvSource(int a, int b, int expected){
        //Arrange done

        //Act
        var sum = a+b;
        assertEquals("",expected,sum);
    }

    String nullString = null;
    @DisplayName("Validate String not empty") //null o vacío
    @ParameterizedTest()
    @ValueSource(strings = {
            "hello",
            "world",
            "no estoy vacío",
            "ll"
    })
    void testValidateStringNotEmpty(String values){
        var isNotEmpty = !values.isEmpty();
        assertTrue("",isNotEmpty);
    }


    @DisplayName("Validate double of an integer with Method Source")
    @ParameterizedTest
    @MethodSource("provideNumbers")
    void testDouble(int a, int expected){
        var doubleValue = a * 2;
        assertEquals("",expected,doubleValue);
    }

    @DisplayName("Validate Pets is older tan 10 years old")
    @ParameterizedTest
    @MethodSource("providePets")
    void testDouble(Pet pet, boolean expected){
        var isOlderThanTen= pet.age()>10;
        assertEquals("",expected,isOlderThanTen);
    }

    public static Stream<Object> provideNumbers(){
        return Stream.of(
                new Object[] {2,4},
                new Object[]{5,10}//,
                //new Object[]{"Hola","HolaHola"} // string type
        );
    }
    public static Stream<Object> providePets(){
        return Stream.of(
                new Object[] {new Pet("Milaneso","azul",5,"Corgi"),false},
                new Object[] {new Pet("Frijol","cafe",10,"Chihuahita"),false},
                new Object[] {new Pet("Tostada","cafe",15,"Xolo"),true}
                //new Object[]{"Hola","HolaHola"} // string type
        );
    }

    //POJO - Plain Old Java Object: Clase con getters y setters
    //Records - POJO inmutable sin BoilerPlate
    private record Pet(String name, String color, int age, String race){};
}
