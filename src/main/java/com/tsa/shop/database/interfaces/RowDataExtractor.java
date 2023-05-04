package com.tsa.shop.database.interfaces;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Map;

public interface RowDataExtractor {
    Map<Field, Serializable> getValues(ResultSet resultSetWithData);
}
