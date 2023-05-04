package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.annotation.Column;
import com.tsa.shop.orm.annotation.Entity;
import com.tsa.shop.orm.annotation.Id;
import com.tsa.shop.orm.annotation.Table;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultEntityClassMetaTest {
    @Entity
    @Table
    private static class AddressForTest {
        @Id
        private String id;
        @Column
        private String name;
        @Column
        private String category;
    }

    private static final class StreetForTest {
    }

    @Entity
    private static final class MailForTest extends AddressForTest {
        @Column(name = "mail-price")
        private String price;
        @Column(name = "mail-amount")
        private String amount;
    }

    @Entity
    private static class WithTwoIdAnnotations {
        @Id
        private String id;
        @Id
        private String name;
    }

    @Entity
    private static class IdAnnotationsIsAbsent {
        private String id;
    }

    @Entity
    private static class HierarchyHasTwoIdAnnotation extends WithTwoIdAnnotations {
        private String number;
    }

    @Test
    void shouldThrowRuntimeExceptionWhenAnnotationEntityIsAbsent() {
        Class<?> classToParse = StreetForTest.class;
        assertThrows(IllegalArgumentException.class, () -> new DefaultEntityClassMeta(classToParse));
    }

    @Test
    void shouldReturnFieldsAccordingAnnotationColumn() {
        List<String> expectedFields =
                List.of(
                        "private java.lang.String com.tsa.shop.orm.impl.DefaultEntityClassMetaTest$AddressForTest.name",
                        "private java.lang.String com.tsa.shop.orm.impl.DefaultEntityClassMetaTest$AddressForTest.category"
                );

        Class<?> classToParse = AddressForTest.class;
        EntityClassMeta meta = new DefaultEntityClassMeta(classToParse);

        List<Field> resultFields = meta.getFields();

        assertEquals(2, resultFields.size());
        assertEquals(expectedFields.get(0), resultFields.get(0).toString());
        assertEquals(expectedFields.get(1), resultFields.get(1).toString());

    }

    @Test
    void shouldReturnFieldsFromHierarchyAccordingColumnAnnotation() {
        List<String> expectedFields =
                List.of(
                        "private java.lang.String com.tsa.shop.orm.impl.DefaultEntityClassMetaTest$AddressForTest.name",
                        "private java.lang.String com.tsa.shop.orm.impl.DefaultEntityClassMetaTest$AddressForTest.category",
                        "private java.lang.String com.tsa.shop.orm.impl.DefaultEntityClassMetaTest$MailForTest.price",
                        "private java.lang.String com.tsa.shop.orm.impl.DefaultEntityClassMetaTest$MailForTest.amount"
                );

        Class<?> classToParse = MailForTest.class;
        EntityClassMeta meta = new DefaultEntityClassMeta(classToParse);

        List<Field> resultFields = meta.getFields();

        assertEquals(4, resultFields.size());
        assertEquals(expectedFields.get(0), resultFields.get(0).toString());
        assertEquals(expectedFields.get(1), resultFields.get(1).toString());
        assertEquals(expectedFields.get(2), resultFields.get(2).toString());
        assertEquals(expectedFields.get(3), resultFields.get(3).toString());
    }

    @Test
    void shouldThrowExceptionWhenEntityHasMoreThenOneIdAnnotation() {
        String expectedExceptionMessage = "The class: [com.tsa.shop.orm.impl.DefaultEntityClassMetaTest$WithTwoIdAnnotations] has [2]: [fields: id, name] Id annotation";

        Class<?> classToParse = WithTwoIdAnnotations.class;

        RuntimeException e = assertThrows(RuntimeException.class, () -> new DefaultEntityClassMeta(classToParse));
        assertEquals(expectedExceptionMessage, e.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEntityHierarchyHasMoreThenOneIdAnnotation() {
        String expectedExceptionMessage = "The class: [com.tsa.shop.orm.impl.DefaultEntityClassMetaTest$HierarchyHasTwoIdAnnotation] has [2]: [fields: id, name] Id annotation";

        Class<?> classToParse = HierarchyHasTwoIdAnnotation.class;

        RuntimeException e = assertThrows(RuntimeException.class, () -> new DefaultEntityClassMeta(classToParse));
        assertEquals(expectedExceptionMessage, e.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenEntityIdAnnotationIsAbsent() {
        String expectedExceptionMessage = "The class: [com.tsa.shop.orm.impl.DefaultEntityClassMetaTest$IdAnnotationsIsAbsent] does not have Id annotation";

        Class<?> classToParse = IdAnnotationsIsAbsent.class;

        RuntimeException e = assertThrows(RuntimeException.class, () -> new DefaultEntityClassMeta(classToParse));
        assertEquals(expectedExceptionMessage, e.getMessage());
    }

    @Test
    void shouldReturnStringOfEntityFieldInKeyValueFormat() {
        String expected = "key=creationdate, value=date; key=product_id, value=id; key=product_price, value=price; key=product_name, value=name";

        Class<?> classToParse = Product.class;
        EntityClassMeta meta = new DefaultEntityClassMeta(classToParse);

        String result = meta.getAllFields().stream()
                .map(entry -> "key=%s, value=%s%n".formatted(entry.getKey(), entry.getValue().getName()).trim())
                .collect(Collectors.joining("; "));

        assertEquals(expected, result);
    }
}
