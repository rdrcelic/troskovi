package com.rdrcelic.troskovi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class TroskoviResult extends ArrayList<Map<String, Object>> {

    public static TroskoviResult createResult(String key, Object value) {
        TroskoviResult result = new TroskoviResult();
        result.add(Collections.singletonMap(key, value));
        return result;
    }
}
