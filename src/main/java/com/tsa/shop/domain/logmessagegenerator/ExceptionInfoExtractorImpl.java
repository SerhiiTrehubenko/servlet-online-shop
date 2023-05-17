package com.tsa.shop.domain.logmessagegenerator;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExceptionInfoExtractorImpl implements ExceptionInfoExtractor {
    private static String TEMPLATE_MESSAGE;

    @Override
    public Throwable getBaseCause(Throwable throwable) {
        Throwable cause = null;
        if (throwable.getCause() == null) {
            cause = throwable;
        }
        if (throwable.getCause() != null) {
            cause = getBaseCause(throwable.getCause());
        }
        return cause;
    }

    @Override
    public String getLineOfOccurrence(Throwable e, String classOccurrence) {
        var stack = e.getStackTrace();

        return Arrays.stream(stack)
                .map(StackTraceElement::toString)
                .filter(line -> line.contains(classOccurrence))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public String getLineOfOccurrence(Throwable baseCause) {
        return baseCause.getStackTrace()[0].toString();
    }

    @Override
    public String getHierarchicalMessageFrom(Throwable exception) {
        Objects.requireNonNull(TEMPLATE_MESSAGE);
        CauseLevel causeLevel = getCauseLevel(exception);
        return Stream.iterate(causeLevel, Objects::nonNull, CauseLevel::getNext)
                .map((cause) -> TEMPLATE_MESSAGE.formatted(cause.getExceptionName(), cause.getMessage()))
                .collect(Collectors.joining("\n"));
    }


    private CauseLevel getCauseLevel(Throwable throwable) {
        CauseLevel causeLevel = new CauseLevel();
        causeLevel.setExceptionName(getFullName(throwable));
        causeLevel.setMessage(throwable.getMessage());

        if (hierarchyContinue(throwable)) {
            causeLevel.setCauseLevel(getCauseLevel(throwable.getCause()));
        }
        return causeLevel;
    }

    String getFullName(Throwable throwable) {
        return throwable.getClass().getName();
    }

    boolean hierarchyContinue(Throwable throwable) {
        return throwable.getCause() != null;
    }

    @Override
    public void setTemplate(String template) {
        TEMPLATE_MESSAGE = template;
    }

    private static class CauseLevel {
        private String exceptionName;
        private String message;
        private CauseLevel causeLevel;

        public void setExceptionName(String exceptionName) {
            this.exceptionName = exceptionName;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setCauseLevel(CauseLevel causeLevel) {
            this.causeLevel = causeLevel;
        }

        public String getExceptionName() {
            return exceptionName;
        }

        public String getMessage() {
            return message;
        }

        public CauseLevel getNext() {
            return causeLevel;
        }
    }

}
