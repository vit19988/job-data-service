package com.peeerawit.jobdataservice.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BaseController {
    public static Map<String, String> combineListsToMap(List<String> keys, List<String> values) {
        if (keys == null || values == null) {
            return null;
        }

        return IntStream.range(0, keys.size())
          .boxed()
          .collect(Collectors.toMap(keys :: get, values :: get));
    }
}
