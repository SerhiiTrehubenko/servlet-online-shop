package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.annotation.*;
import com.tsa.shop.orm.interfaces.EntityClassMeta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.*;
import java.util.stream.Stream;

public class DefaultEntityClassMeta implements EntityClassMeta {
    private static final int ALLOWED_ID_QUANTITY = 1;
    private static final int ID_COLUMN = 0;
    private final Class<?> classToParse;

    private final Map<Class<? extends Annotation>, List<Field>> rawColumnsGroupByAnnotation = new HashMap<>();
    private final Map<String, Field> rawColumns = new HashMap<>();

    public DefaultEntityClassMeta(Class<?> classToParse) {
        this.classToParse = classToParse;
        requiredIncomeClassNotNull();
        requiredEntityAnnotation();

        parseClass();

        requiredOnlyOneIdColumn();
    }

    private void requiredIncomeClassNotNull() {
        Objects.requireNonNull(classToParse,
                "During creation of %s provided Entity class was null".formatted(DefaultEntityClassMeta.class.getName()));
    }

    private void requiredEntityAnnotation() {
        if (OrmAnnotations.ENTITY.isAbsent(classToParse)) {
            throw new IllegalArgumentException("Provided class: %s is not an Entity".formatted(classToParse.getName()));
        }
    }

    private void parseClass() {
        final Deque<Class<?>> classesFromHierarchy = getClassesFromHierarchy(classToParse);
        classesFromHierarchy.stream()
                .flatMap(this::streamFieldsFromOneClass)
                .forEach(this::getRawColumns);
    }

    private Deque<Class<?>> getClassesFromHierarchy(Class<?> incomeClass) {
        Deque<Class<?>> superClasses = new ArrayDeque<>();
        if (Objects.nonNull(incomeClass) && !incomeClass.isAssignableFrom(Object.class)) {
            superClasses.addFirst(incomeClass);
            getClassesFromHierarchy(incomeClass.getSuperclass())
                    .forEach(superClasses::addFirst);
        }
        return superClasses;
    }

    private Stream<Field> streamFieldsFromOneClass(Class<?> incomeClass) {
        return Arrays.stream(incomeClass.getDeclaredFields());
    }

    private void getRawColumns(Field field) {
        getRawColumn(OrmAnnotations.ID, field);
        getRawColumn(OrmAnnotations.COLUMN, field);
    }

    private void getRawColumn(OrmAnnotations enumeration, Field field) {
        Class<? extends Annotation> annotationToFind = enumeration.annotation;
        Annotation fieldAnnotation = field.getAnnotation(annotationToFind);
        if (Objects.nonNull(fieldAnnotation)) {
            rawColumnsGroupByAnnotation.computeIfAbsent(annotationToFind, key -> new ArrayList<>()).add(field);
            rawColumns.putIfAbsent(enumeration.getName(field), field);
        }
    }

    private void requiredOnlyOneIdColumn() {
        List<Field> idColumn = getRawIdColumns();
        int numberIdColumns;
        if (Objects.isNull(idColumn)) {
            throw new RuntimeException("The class: [%s] does not have Id annotation".formatted(classToParse.getName()));
        } else if ((numberIdColumns = idColumn.size()) != ALLOWED_ID_QUANTITY) {
            throw new RuntimeException("The class: [%s] has [%d]: [fields: %s] Id annotation"
                    .formatted(
                            classToParse.getName(),
                            numberIdColumns,
                            String.join(", ", idColumn.stream().map(Field::getName).toList())
                    )
            );
        }
    }

    private List<Field> getRawIdColumns() {
        return rawColumnsGroupByAnnotation.get(OrmAnnotations.ID.annotation);
    }

    @Override
    public String getTableName() {
        return OrmAnnotations.TABLE.getName(classToParse);
    }

    @Override
    public List<String> getColumnsNames() {
        List<Field> columnsResult = rawColumnsGroupByAnnotation.get(OrmAnnotations.COLUMN.annotation);
        return
                columnsResult.stream()
                        .map(OrmAnnotations.COLUMN::getName)
                        .toList();
    }

    @Override
    public List<String> getColumnsValues(Object entityToInsert) {
        return getValues(entityToInsert, OrmAnnotations.COLUMN.annotation);
    }

    private List<String> getValues(Object entityToInsert, Class<? extends  Annotation> annotation) {
        List<Field> columns = rawColumnsGroupByAnnotation.get(annotation);
        return
                columns.stream()
                        .peek(field -> field.setAccessible(true))
                        .map(field -> this.getValue(field, entityToInsert))
                        .map(this::resolveSingleQuotes)
                        .toList();
    }

    private Object getValue(Field field, Object entityToInsert) {
        try {
            return field.get(entityToInsert);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String resolveSingleQuotes(Object value) {
        Class<?> valueClass = value.getClass();
        if (String.class.isAssignableFrom(valueClass) || Date.class.isAssignableFrom(valueClass)) {
            return surroundBySingleQuotes(value);
        }
        return String.valueOf(value);
    }

    private String surroundBySingleQuotes(Object value) {
        String valueAsString = String.valueOf(value);
        return "'%s'".formatted(valueAsString);
    }

    @Override
    public String getIdColumnName() {
        Field columnId = getIdRawField();
        return OrmAnnotations.ID.getName(columnId);
    }

    private Field getIdRawField() {
        List<Field> idColumns = getRawIdColumns();
        return idColumns.get(ID_COLUMN);
    }

    @Override
    public Class<?> getClassToParse() {
        return classToParse;
    }

    @Override
    public String getId(Object objectToInsert) {
        List<String> idColumns = getValues(objectToInsert, OrmAnnotations.ID.annotation);
        return idColumns.get(ID_COLUMN);
    }

    @Override
    public Set<Map.Entry<String, Field>> getAllEntityColumns() {
        return rawColumns.entrySet();
    }
}
