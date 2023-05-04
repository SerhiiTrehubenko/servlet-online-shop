package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.annotation.*;
import com.tsa.shop.orm.interfaces.EntityClassMeta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public class DefaultEntityClassMeta implements EntityClassMeta {
    private static final int ALLOWED_ID_QUANTITY = 1;
    private static final int ID_COLUMN = 0;
    private final Class<?> classToParse;

    private final Map<OrmAnnotations, List<Field>> fieldsByAnnotation = new HashMap<>(2);
    private final Map<String, Field> rawColumns = new HashMap<>();

    public DefaultEntityClassMeta(Class<?> classToParse) {
        this.classToParse = classToParse;
        requiredIncomeClassNotNull();
        requiredEntityAnnotation();

        parseClass();

        requiredOnlyOneIdAnnotation();
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
                .forEach(this::setFields);
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

    private void setFields(Field field) {
        setField(OrmAnnotations.ID, field);
        setField(OrmAnnotations.COLUMN, field);
    }

    private void setField(OrmAnnotations enumeration, Field field) {
        Class<? extends Annotation> annotationToFind = enumeration.annotation;
        Annotation fieldAnnotation = field.getAnnotation(annotationToFind);
        if (Objects.nonNull(fieldAnnotation)) {
            fieldsByAnnotation.computeIfAbsent(enumeration, key -> new ArrayList<>()).add(field);
            rawColumns.putIfAbsent(enumeration.getName(field), field);
        }
    }

    private void requiredOnlyOneIdAnnotation() {
        List<Field> idField = getId();
        int numberIdColumns;
        if (Objects.isNull(idField)) {
            throw new RuntimeException("The class: [%s] does not have Id annotation".formatted(classToParse.getName()));
        } else if ((numberIdColumns = idField.size()) != ALLOWED_ID_QUANTITY) {
            throw new RuntimeException("The class: [%s] has [%d]: [fields: %s] Id annotation"
                    .formatted(
                            classToParse.getName(),
                            numberIdColumns,
                            String.join(", ", idField.stream().map(Field::getName).toList())
                    )
            );
        }
    }

    private List<Field> getId() {
        return fieldsByAnnotation.get(OrmAnnotations.ID);
    }

    @Override
    public Field getIdField() {
        List<Field> idColumns = getId();
        return idColumns.get(ID_COLUMN);
    }

    @Override
    public Class<?> getClassToParse() {
        return classToParse;
    }

    @Override
    public Set<Map.Entry<String, Field>> getAllFields() {
        return rawColumns.entrySet();
    }

    @Override
    public List<Field> getFields() {
        return fieldsByAnnotation.get(OrmAnnotations.COLUMN);
    }
}
