package com.xc.xcnotification.XCNotification;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 【通知命名】构造类
 */
public class XCNotificationName {

    /**
     * 工厂方法
     */
    public static XCNotificationName name(String name) {
        XCNotificationName nameObject = cachedNotificationNames.get(name);
        if (nameObject != null) {
            return nameObject;
        }
        nameObject = new XCNotificationName(name);
        cachedNotificationNames.put(name, nameObject);
        return nameObject;
    }

    private XCNotificationName() {}

    private XCNotificationName(String name) {
        this.name = name;
    }

    private String name;
    private static final Map<String, XCNotificationName> cachedNotificationNames = new ConcurrentHashMap<>();
}
