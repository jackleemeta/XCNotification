package com.xc.xcnotification.XCNotification;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import de.greenrobot.event.EventBus;

/**
 * 通知中心
 *
 * 1.过滤未监听消息
 * 2.可绑定Object
 */
public class XCNotificationCenter {

    /**
     * 获取单例
     */
    public static final XCNotificationCenter defaultCenter() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 发送消息
     *
     * @param name 通知名
     */
    public void post(XCNotificationName name) {
        post(name, null, null);
    }

    /**
     * 发送消息
     *
     * @param name 通知名
     * @param userInfo 消息体
     * @param object 绑定对象
     */
    public void post(XCNotificationName name,
                     @Nullable Object userInfo,
                     @Nullable Object object) {

        final Map notification = new HashMap();
        notification.put("name", name);
        notification.put("userInfo", userInfo);
        notification.put("object", object);

        eventBus.post(notification);
    }

    /**
     * 添加监听
     *
     * @param observer 监听者
     * @param name 通知名
     * @param object 绑定对象
     * @param handler 句柄
     */
    public void add(Object observer,
                    XCNotificationName name,
                    Object object,
                    @NonNull XCNotificationCenterHandler handler) {

        final EventBus eb = eventBus;
        if (!eb.isRegistered(this)) {
            eb.register(this);
        }

        final ArrayList<Map> cachedNotificationsOfName = getCachedNotificationsOfName(name);

        for (Map item : cachedNotificationsOfName) {
            final Object itemObserver = item.get("observer");
            final Object itemObject = item.get("object");
            if (itemObserver.equals(observer) && itemObject.equals(object)) return;
        }

        final Map map = new HashMap<>();

        map.put("name", name);
        map.put("observer", observer);
        map.put("object", object);
        map.put("handler", handler);

        cachedNotificationsOfName.add(map);
    }

    /**
     * 移除【监听者】对所有通知的监听
     *
     * @param observer 监听者
     */
    public void remove(Object observer) {
        remove(observer,
                null,
                null);
    }

    /**
     * 移除【监听者】对某通知的监听
     *
     * observer必传
     *
     * @param observer 监听者
     * @param name 通知名
     * @param object 绑定对象
     */
    public void remove(Object observer,
                       @Nullable XCNotificationName name,
                       @Nullable Object object) {

        if (observer == null) throw new RuntimeException("observer can not be null");

        for (Map.Entry<XCNotificationName, ArrayList> entry : cachedNotifications.entrySet()) {

            final XCNotificationName cachedName = entry.getKey(); // 已缓存通知名
            final ArrayList<Map> cachedArrayList = entry.getValue();

            final ArrayList<Map> tempArrayList = cachedArrayList; // 临时数组

            for (Map cachedNotification : tempArrayList) {
                final Object cachedObserver = cachedNotification.get("observer");

                if (cachedObserver.equals(observer)) {
                    final Object cachedObject = cachedNotification.get("object");
                    if (name == null ||
                            cachedName.equals(name)) {
                        if (object == null || object.equals(cachedObject)) {
                            cachedArrayList.remove(cachedNotification);
                            Log.i("NotificationCenter", "移除监听成功");
                        }

                    }
                }

            }

        }
    }


    /**
     * ---------------------------------------------------------Private Methods---------------------------------------------------------
     */

    public void onEventMainThread(Map notification) {

        final XCNotificationName name = (XCNotificationName) notification.get("name");
        final Object object = notification.get("object");

        final ArrayList<Map> cachedNotificationsOfName = getCachedNotificationsOfName(name);
        final ArrayList<Map> tempAry = cachedNotificationsOfName;

        for (Map item : tempAry) {
            final Object cachedObject = item.get("object");
            if (cachedObject == null || cachedObject.equals(object)) {
                final XCNotificationCenterHandler handler = (XCNotificationCenterHandler) item.get("handler");
                handler.handler(notification);
            }
        }
    };

    private ArrayList<Map> getCachedNotificationsOfName(XCNotificationName name) {
        ArrayList<Map> list = cachedNotifications.get(name);
        if (list == null) {
            list = new ArrayList();
            cachedNotifications.put(name, list);
        }
        return list;
    }

    private XCNotificationCenter() {}

    private static class SingletonHolder {
        private static final XCNotificationCenter INSTANCE = new XCNotificationCenter();
    }

    private final EventBus eventBus = EventBus.builder().build();
    private final Map<XCNotificationName, ArrayList> cachedNotifications = new HashMap<>();
}
