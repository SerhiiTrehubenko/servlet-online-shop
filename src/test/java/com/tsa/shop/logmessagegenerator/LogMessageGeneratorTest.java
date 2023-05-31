package com.tsa.shop.logmessagegenerator;

import com.tsa.shop.domain.HttpStatus;
import com.tsa.shop.domain.WebServerException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
        String expectedMessage = "occurrence code line: com.tsa.shop.logmessagegenerator.LogMessageGeneratorTest.shouldReturnLogMessageFromWebserverExceptionInnerExceptionsIsAbsent(LogMessageGeneratorTest.java:39)\n" +
                "\tCause: - com.tsa.shop.domain.WebServerException: [no inner exceptions];\n";

        WebServerException exception = new WebServerException("no inner exceptions", HttpStatus.NOT_FOUND, this);

//        ACT
        String resultMessage = messageGeneratorSut.getMessageFrom(exception);

//        ASSERT
        assertEquals(expectedMessage, resultMessage);
    }

    @Test
    void shouldReturnLogMessageFromWebserverExceptionCauseHierarchyHasOneLevel() {
//        ARRANGE
        String expectedMessage = "occurrence code line: com.tsa.shop.logmessagegenerator.LogMessageGeneratorTest.simulateIOExceptionInInputStream(LogMessageGeneratorTest.java:18)\n" +
                "\tCause: - com.tsa.shop.domain.WebServerException: [has IOException in hierarchy];\n" +
                "\t\tCause: - java.io.FileNotFoundException: [notExist.txt";
//        ACT
        try {
            simulateIOExceptionInInputStream();
        } catch (WebServerException e) {
            String resultMessage = messageGeneratorSut.getMessageFrom(e);
//        ASSERT
            assertTrue(resultMessage.contains(expectedMessage));
        }
    }

    @Test
    void shouldReturnLogMessageFromWebserverExceptionCauseHierarchyHasTwoLevels() {
        String expectedMessage = "occurrence code line: com.tsa.shop.logmessagegenerator.LogMessageGeneratorTest.simulateCauseHierarchyTwoLevel(LogMessageGeneratorTest.java:26)\n" +
                "\tCause: - com.tsa.shop.domain.WebServerException: [has IOException in hierarchy];\n" +
                "\t\tCause: - java.lang.Exception: [Exception];\n" +
                "\t\t\tCause: - java.io.FileNotFoundException: [notExist.txt";
//        ACT
        try {
            simulateCauseHierarchyTwoLevel();
        } catch (WebServerException e) {
            String resultMessage = messageGeneratorSut.getMessageFrom(e);
//        ASSERT
            assertTrue(resultMessage.contains(expectedMessage));
        }

    }

    @Test
    void shouldReturnValidMessageFromRuntimeException() {
//        ARRANGE
        String expected = "occurrence code line: com.tsa.shop.logmessagegenerator.LogMessageGeneratorTest.shouldReturnValidMessageFromRuntimeException(LogMessageGeneratorTest.java:87)\n" +
                "\tCause: - java.lang.RuntimeException: [Runtime exception];\n";

        RuntimeException exception = new RuntimeException("Runtime exception");
//      ACT
        String result = messageGeneratorSut.getMessageFrom(exception);
//      ASSERT
        assertEquals(expected, result);
    }
}
