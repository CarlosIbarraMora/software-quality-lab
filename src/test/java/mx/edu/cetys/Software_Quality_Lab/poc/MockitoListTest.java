package mx.edu.cetys.Software_Quality_Lab.poc;

import mx.edu.mx.Software_Quality_Lab.validators.EmailValidatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MockitoListTest {

    @Mock
    List<String> mockList = new ArrayList<>();

    @Mock
    EmailValidatorService emailValidatorServiceMock;

    @Test
    void shouldReturnCustomSizeWhenMocked() {
        //Arrange done

        //Define Mock behavior
        when(mockList.size()).thenReturn(67)
                .thenReturn(1)
                .thenReturn(2)
                .thenThrow(new RuntimeException())
                .thenReturn(0);

        //Act
        //var size = mockList.size()
        //Assert
        assertEquals(67, mockList.size());
        assertEquals(1, mockList.size());
        assertEquals(2, mockList.size());
        assertThrows(RuntimeException.class, () -> mockList.size());
        assertEquals(0, mockList.size());
    }

    @Test
    void shouldMockListGetWithParameters() {
        //Define Mock behavior
        when(mockList.get(0)).thenReturn("Hello")
                            .thenReturn("Carlos")
                            .thenReturn("Charlie");
        when(mockList.get(1)).thenReturn("World");


        assertEquals("Hello", mockList.get(0));
        assertEquals("World", mockList.get(1));
        assertEquals("Carlos", mockList.get(0));
        assertEquals("Charlie", mockList.get(0));

    }

    @Test
    void mockEmailValidatorWithArgumentMatchers(){
        //Define Mock behavior
        when(emailValidatorServiceMock.isValid(anyString()))
                                        .thenReturn(true);
        when(emailValidatorServiceMock.isValid(isNull()))
                                        .thenReturn(false)
                                        .thenReturn(true);

        //Assert
        assertTrue(emailValidatorServiceMock.isValid("aaeeiioouu"));
        assertFalse(emailValidatorServiceMock.isValid(null));
        assertTrue(emailValidatorServiceMock.isValid(null));
        assertTrue(emailValidatorServiceMock.isValid(null));
    }
}
