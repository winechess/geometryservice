package com.dit.suumuagent.geometryservice.helpers.comparators;

public class Comparator {
//
//
//    public static <T> Boolean partialUpdate(T entity, T actual, Set<String> fieldsToCompare, Boolean ignoreNulls) throws NoSuchFieldException, IllegalAccessException {
//
//        if (entity == null || actual == null) throw new IllegalArgumentException("Object can not be null.");
//
//        Class<?> cl = entity.getClass();
//        Boolean updatedFlag = false;
//
//        for (String fieldName : fieldsToCompare) {
//
//            Field field = cl.getDeclaredField(fieldName);
//            field.setAccessible(true);
//            Object origValue = field.get(entity);
//            Object actualValue = field.get(actual);
//
//            if (actualValue == null && ignoreNulls) {
//                continue;
//            } else {
//                if (EqualsBuilder.reflectionEquals(origValue, actualValue)) {
//                    continue;
//                } else {
//                    field.set(origValue, actualValue);
//                    updatedFlag = true;
//                }
//            }
//        }
//
//        return updatedFlag;
//    }
}