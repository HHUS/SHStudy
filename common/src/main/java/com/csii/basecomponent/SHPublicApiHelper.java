package com.csii.basecomponent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * autor : sunhao
 * time  : 2018/06/05  07:30
 * desc  :这里面提供register和unRegister的注册和注销静态接口
 * 把类映射到moduleApi里面，getModdule，可以去除相应的类名取出ElModuleApi接口，用于之后的方法调用
 */

public class SHPublicApiHelper {

    private static Map<Class<? extends SHModuleApi>, SHModuleApi> moduleApi = new HashMap<>();

    public static <T extends SHModuleApi> T getModeleApi(Class<T> clazz) {

        if (clazz == null) {
            return null;
        }

        SHModuleApi api = null;
        if (moduleApi.containsKey(clazz)) {
            api = moduleApi.get(clazz);
        }

        return (T) api;

    }

    public static void register(Class<? extends SHModuleApi> clazz, SHModuleApi api) {
        if (moduleApi.containsKey(clazz)) {
            return;
        }

        moduleApi.put(clazz, api);
    }

    public static void registerAll(Map<Class<? extends SHModuleApi>, SHModuleApi> allMap) {
        if (allMap != null) {
            return;
        }

        moduleApi.putAll(allMap);
    }

    public static void unRegister(Class<? extends SHModuleApi> clazz) {
        if (moduleApi.containsKey(clazz)) {
            moduleApi.remove(clazz);
        }
    }

    public static void unRegisterAll(Map<Class<? extends SHModuleApi>, SHModuleApi> allMap) {
        if (allMap != null) {
            Set<Class<? extends SHModuleApi>> keySet = allMap.keySet();
            Iterator<Class<? extends SHModuleApi>> iterable = keySet.iterator();

            while (iterable.hasNext()) {
                unRegister(iterable.next());
            }
        }

        moduleApi.putAll(allMap);
    }

}
