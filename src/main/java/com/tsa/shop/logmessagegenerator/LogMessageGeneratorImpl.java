package com.tsa.shop.logmessagegenerator;

import com.tsa.shop.exceptions.WebServerException;

public class LogMessageGeneratorImpl implements LogMessageGenerator {

    private final static String TEMPLATE_MESSAGE = "Cause: - %s: [%s];";
    private final static String CAUSE_LINE_TEMPLATE = "occurrence code line: ";

    public LogMessageGeneratorImpl(ExceptionInfoExtractor parser) {
        this.parser = parser;
    }

    private final ExceptionInfoExtractor parser;

    @Override
    public String getMessageFrom(RuntimeException exception) {
        String occurrenceLine = parser.getLineOfOccurrence(exception);
        return getMessage(occurrenceLine, exception);
    }

    @Override
    public String getMessageFrom(WebServerException exception) {
        Throwable baseCause = parser.getBaseCause(exception);
        String occurrenceLine = parser.getLineOfOccurrence(baseCause, exception.getOccurrenceClass());
        return getMessage(occurrenceLine, exception);
    }

    private String getMessage(String occurrenceLine, Throwable exception) {
        parser.setTemplate(TEMPLATE_MESSAGE);
        String causeLevelsImpl = parser.getHierarchicalMessageFrom(exception);
        return generateMessage(occurrenceLine, causeLevelsImpl);
    }

    private String generateMessage(String occurrenceLine, String hierarchicalMessage) {
        return CAUSE_LINE_TEMPLATE + occurrenceLine + "\n"
                + formatMessage(hierarchicalMessage);
    }

    private String formatMessage(String raw) {
        String tab = "\t";
        String[] splited = raw.split("\n");
        StringBuilder result = new StringBuilder();
        int count = 1;
        for (String s : splited) {
            result
                    .append(tab.repeat(count))
                    .append(s)
                    .append("\n");
            count++;
        }
        return result.toString();
    }
}
