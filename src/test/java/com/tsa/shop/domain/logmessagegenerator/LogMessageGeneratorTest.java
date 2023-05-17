package com.tsa.shop.domain.logmessagegenerator;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LogMessageGeneratorTest {
    private final ExceptionInfoExtractor parser = new ExceptionInfoExtractorImpl();
    private final LogMessageGenerator messageGeneratorSut = new LogMessageGeneratorImpl(parser);

    void simulateIOExceptionInInputStream() {
        try(var input = new FileInputStream("notExist.txt")) {
            input.readAllBytes();
        } catch (Exception e) {
            throw new WebServerException("has IOException in hierarchy", e, HttpStatus.NOT_FOUND, this);
        }
    }

    void simulateCauseHierarchyTwoLevel() {
        try(var input = new FileInputStream("notExist.txt")) {
            input.readAllBytes();
        } catch (Exception e) {
            throw new WebServerException("has IOException in hierarchy", new Exception("Exception", e), HttpStatus.NOT_FOUND, this);
        }
    }

    @Test
    void shouldReturnLogMessageFromWebserverExceptionInnerExceptionsIsAbsent() {
//        ARRANGE
        String expectedMessage = """
                occurrence code line: com.tsa.shop.domain.logmessagegenerator.LogMessageGeneratorTest.shouldReturnLogMessageFromWebserverExceptionInnerExceptionsIsAbsent(LogMessageGeneratorTest.java:40)
                \tCause: - com.tsa.shop.servlets.exceptions.WebServerException: [no inner exceptions];
                """;

        WebServerException exception = new WebServerException("no inner exceptions", HttpStatus.NOT_FOUND, this);

//        ACT
        String resultMessage = messageGeneratorSut.getMessageFrom(exception);

//        ASSERT
        assertEquals(expectedMessage, resultMessage);
    }

    @Test
    void shouldReturnLogMessageFromWebserverExceptionCauseHierarchyHasOneLevel() {
//        ARRANGE
        String expectedMessage = "occurrence code line: com.tsa.shop.domain.logmessagegenerator.LogMessageGeneratorTest.simulateIOExceptionInInputStream(LogMessageGeneratorTest.java:17)\n" +
                "\tCause: - com.tsa.shop.servlets.exceptions.WebServerException: [has IOException in hierarchy];\n" +
                "\t\tCause: - java.io.FileNotFoundException: [notExist.txt (Не удается найти указанный файл)];\n";
//        ACT
        try {
            simulateIOExceptionInInputStream();
        } catch (WebServerException e) {
            String resultMessage = messageGeneratorSut.getMessageFrom(e);
//        ASSERT
            assertEquals(expectedMessage, resultMessage);
        }
    }

    @Test
    void shouldReturnLogMessageFromWebserverExceptionCauseHierarchyHasTwoLevels() {
        String expectedMessage = "occurrence code line: com.tsa.shop.domain.logmessagegenerator.LogMessageGeneratorTest.simulateCauseHierarchyTwoLevel(LogMessageGeneratorTest.java:25)\n" +
                "\tCause: - com.tsa.shop.servlets.exceptions.WebServerException: [has IOException in hierarchy];\n" +
                "\t\tCause: - java.lang.Exception: [Exception];\n" +
                "\t\t\tCause: - java.io.FileNotFoundException: [notExist.txt (Не удается найти указанный файл)];\n";
//        ACT
        try {
            simulateCauseHierarchyTwoLevel();
        } catch (WebServerException e) {
            String resultMessage = messageGeneratorSut.getMessageFrom(e);
//        ASSERT
            assertEquals(expectedMessage, resultMessage);
        }

    }

    @Test
    void shouldReturnValidMessageFromRuntimeException() {
//        ARRANGE
        String expected = """
                occurrence code line: com.tsa.shop.domain.logmessagegenerator.LogMessageGeneratorTest.shouldReturnValidMessageFromRuntimeException(LogMessageGeneratorTest.java:90)
                \tCause: - java.lang.RuntimeException: [Runtime exception];
                """;

        RuntimeException exception = new RuntimeException("Runtime exception");
//      ACT
        String result = messageGeneratorSut.getMessageFrom(exception);
//      ASSERT
        assertEquals(expected, result);
    }
}
