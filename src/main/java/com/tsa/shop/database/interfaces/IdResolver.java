package com.tsa.shop.database.interfaces;

import java.io.Serializable;

public interface IdResolver {
    Serializable resolveId(Serializable id);
}
