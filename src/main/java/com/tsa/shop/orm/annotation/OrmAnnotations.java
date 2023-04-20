package com.tsa.shop.orm.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;

public enum OrmAnnotations {
    ENTITY(Entity.class) {
        @Override
        String getColumnNameFrom(Annotation fieldAnnotation) {
            throw new RuntimeException("The Entity Annotation does not have properties");
        }
    },
    ID(Id.class) {
        @Override
        String getColumnNameFrom(Annotation fieldAnnotation) {
            return ((Id) fieldAnnotation).name();
        }
    },
    TABLE(Table.class) {
        @Override
        String getColumnNameFrom(Annotation fieldAnnotation) {
            return ((Table) fieldAnnotation).name();
        }
    },
    COLUMN(Column.class) {
        @Override
        String getColumnNameFrom(Annotation fieldAnnotation) {
            return ((Column) fieldAnnotation).name();
        }
    };

    public static final String EMPTY = "default-empty-column-name";

    public final Class<? extends Annotation> annotation;

    OrmAnnotations(Class<? extends Annotation> annotationClass) {
        annotation = annotationClass;
    }

    public boolean isAbsent(Class<?> classToCheck) {
        return !classToCheck.isAnnotationPresent(annotation);
    }

    public String getName(Field field) {
        Annotation fieldAnnotation = field.getAnnotation(annotation);
        String resultName = resolveName(fieldAnnotation);

        return Objects.isNull(resultName) ? field.getName() : resultName;
    }

    public String getName(Class<?> incomeClass) {
        Annotation fieldAnnotation = incomeClass.getAnnotation(annotation);
        String resultName = resolveName(fieldAnnotation);

        return (
                Objects.isNull(resultName) ? incomeClass.getSimpleName() : resultName
        ).toLowerCase();
    }

    private String resolveName(Annotation fieldAnnotation) {
        if (Objects.isNull(fieldAnnotation)) {
            return null;
        }
        String nameFromAnnotation = getColumnNameFrom(fieldAnnotation);

        return EMPTY.equals(nameFromAnnotation) ? null : nameFromAnnotation;
    }

    abstract String getColumnNameFrom(Annotation fieldAnnotation);
}
