package com.tsa.shop.domain.argsparser;

import com.tsa.shop.argsparser.impl.DefaultEnvironmentVariablesContext;
import com.tsa.shop.argsparser.impl.DefaultArgsParser;
import com.tsa.shop.argsparser.interfaces.ArgsParser;
import com.tsa.shop.argsparser.enums.Property;
import com.tsa.shop.argsparser.interfaces.EnvironmentVariablesContext;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultArgsParserTest {
    private final static String WHITE_SPACE_DELIMITER = " ";

    private final EnvironmentVariablesContext contextEmpty = new DefaultEnvironmentVariablesContext(Property.PORT, Property.FILE_PROPERTY);
    private final ArgsParser argsParser = new DefaultArgsParser(contextEmpty);

    @Test
    void shouldReturnDefaultPortProperty() {
        String expectedProperties = "3001";

        EnvironmentVariablesContext contextEmpty = new DefaultEnvironmentVariablesContext(Property.PORT);
        ArgsParser argsParser = new DefaultArgsParser(contextEmpty);

        argsParser.parse();

        EnvironmentVariablesContext contextFull = argsParser.getContext();
        Serializable resultProperties = contextFull.getProperty(Property.PORT);

        assertEquals(expectedProperties, Objects.toString(resultProperties));
    }

    @Test
    void shouldReturnCustomPortProperty() {
        String[] incomeArgs = "-p 4005".split(WHITE_SPACE_DELIMITER);

        String expectedProperties = "4005";

        EnvironmentVariablesContext contextEmpty = new DefaultEnvironmentVariablesContext(Property.PORT);
        ArgsParser argsParser = new DefaultArgsParser(contextEmpty);

        argsParser.parse(incomeArgs);

        EnvironmentVariablesContext contextFull = argsParser.getContext();
        Serializable resultProperties = contextFull.getProperty(Property.PORT);

        assertEquals(expectedProperties, Objects.toString(resultProperties));
    }

    @Test
    void shouldReturnDefaultApplicationPropertyFile() {
        String expectedProperties = "{password=password, dbName=shop, userName=postgres, dbUrl=jdbc:postgresql://localhost:5432}";

        EnvironmentVariablesContext contextEmpty = new DefaultEnvironmentVariablesContext(Property.FILE_PROPERTY);
        ArgsParser argsParser = new DefaultArgsParser(contextEmpty);
        argsParser.parse();

        EnvironmentVariablesContext contextFull = argsParser.getContext();
        Serializable resultProperties = contextFull.getProperty(Property.FILE_PROPERTY);

        assertEquals(expectedProperties, Objects.toString(resultProperties));
    }

    @Test
    void shouldReturnCustomApplicationPropertyFile() {
        String[] incomeArgs = "-f test-property/application-test.properties".split(WHITE_SPACE_DELIMITER);
        String expectedProperties = "{password=password-test, dbName=shop-test, userName=postgres-test, dbUrl=jdbc:postgresql://localhost:5432-test}";

        EnvironmentVariablesContext contextEmpty = new DefaultEnvironmentVariablesContext(Property.FILE_PROPERTY);
        ArgsParser argsParser = new DefaultArgsParser(contextEmpty);
        argsParser.parse(incomeArgs);

        EnvironmentVariablesContext contextFull = argsParser.getContext();
        Serializable resultProperties = contextFull.getProperty(Property.FILE_PROPERTY);

        assertEquals(expectedProperties, Objects.toString(resultProperties));
    }

    @Test
    void shouldReturnDefaultPortAndFilePropertiesWhenArgsEmpty() {
        String expectedPort = "3001";
        String expectedFile = "{password=password, dbName=shop, userName=postgres, dbUrl=jdbc:postgresql://localhost:5432}";

        argsParser.parse();

        EnvironmentVariablesContext contextFull = argsParser.getContext();

        Serializable resultPort = contextFull.getProperty(Property.PORT);
        Serializable resultFile = contextFull.getProperty(Property.FILE_PROPERTY);

        assertEquals(expectedPort, Objects.toString(resultPort));
        assertEquals(expectedFile, Objects.toString(resultFile));
    }

    @Test
    void shouldReturnDefaultPortAndCustomFilePropertiesWhenArgsHasFileProperty() {
        String[] incomeArgs = "-f test-property/application-test.properties".split(WHITE_SPACE_DELIMITER);

        String expectedPort = "3001";
        String expectedFile = "{password=password-test, dbName=shop-test, userName=postgres-test, dbUrl=jdbc:postgresql://localhost:5432-test}";

        argsParser.parse(incomeArgs);

        EnvironmentVariablesContext contextFull = argsParser.getContext();

        Serializable resultPort = contextFull.getProperty(Property.PORT);
        Serializable resultFile = contextFull.getProperty(Property.FILE_PROPERTY);

        assertEquals(expectedPort, Objects.toString(resultPort));
        assertEquals(expectedFile, Objects.toString(resultFile));
    }

    @Test
    void shouldReturnCustomPortAndDefaultFilePropertiesWhenArgsHasPort() {
        String[] incomeArgs = "-p 4005".split(WHITE_SPACE_DELIMITER);

        String expectedPort = "4005";
        String expectedFile = "{password=password, dbName=shop, userName=postgres, dbUrl=jdbc:postgresql://localhost:5432}";

        argsParser.parse(incomeArgs);

        EnvironmentVariablesContext contextFull = argsParser.getContext();

        Serializable resultPort = contextFull.getProperty(Property.PORT);
        Serializable resultFile = contextFull.getProperty(Property.FILE_PROPERTY);

        assertEquals(expectedPort, Objects.toString(resultPort));
        assertEquals(expectedFile, Objects.toString(resultFile));
    }

    @Test
    void shouldThrowExceptionWhenProvidedInvalidTag() {
        String[] incomeArgs = "-div c:/src/main/resources/application.properties".split(WHITE_SPACE_DELIMITER);
        String expectedMessage = "The Provided tad: [-div] does not comply to incomeTag scheme";

        String message = assertThrows(RuntimeException.class, () -> argsParser.parse(incomeArgs)).getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    void shouldThrowExceptionWhenProvidedTagDoesNotHaveValue() {
        String[] incomeArgs = "-p".split(WHITE_SPACE_DELIMITER);
        String expectedMessage = "Provided arg tag: [-p] does not have a value";

        String message = assertThrows(RuntimeException.class, () -> argsParser.parse(incomeArgs)).getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    void shouldThrowExceptionWhenPathToPropertyFileInvalid() {
        String[] incomeArgs = "-f hello".split(WHITE_SPACE_DELIMITER);
        String expectedMessage = "The is no an [application.property] file by provided path [hello]\n" +
                " CAUSE: java.lang.NullPointerException: inStream parameter is null";

        String message = assertThrows(RuntimeException.class, () -> argsParser.parse(incomeArgs)).getMessage();

        assertEquals(expectedMessage, message);
    }

    @Test
    void shouldThrowExceptionWhenProvidedPortValueCannotBeParsedToInteger() {
        String[] incomeArgs = "-p thousand".split(WHITE_SPACE_DELIMITER);
        String expectedMessage = "Provided port value: [thousand] is not an integer";

        String message = assertThrows(RuntimeException.class, () -> argsParser.parse(incomeArgs)).getMessage();

        assertEquals(expectedMessage, message);
    }
}
