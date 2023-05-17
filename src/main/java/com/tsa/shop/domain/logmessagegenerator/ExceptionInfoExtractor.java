package com.tsa.shop.domain.logmessagegenerator;

public interface ExceptionInfoExtractor {
    Throwable getBaseCause(Throwable exception);

    String getLineOfOccurrence(Throwable baseCause, String occurrenceClass);

    String getLineOfOccurrence(Throwable baseCause);

    String getHierarchicalMessageFrom(Throwable exception);

    void setTemplate(String template);
}
