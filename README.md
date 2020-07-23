# XCNotification

## Sumarry
- this is a demo of notification tool based eventbus. [Key Code](https://github.com/jackleemeta/XCNotification/tree/master/app/src/main/java/com/xc/xcnotification/XCNotification)
- it supports to bind object to avoid that infinite observers receive notification. Once the object is binded, posting notification must be with the same object, otherwise, the observer will not receive the notification.
- it uses lamda and block, it's brief and simple.

## Usage

```
/// add observer for notification
private void addXCNotificationObservers() {
    XCNotificationCenter.defaultCenter().add(
            this,
            XCNotificationName.name(XCNotificationConstant.kEvent1),
            this,
            (notification) -> {
                System.out.println("received notification");
            });
}
```

```
/// post notification
private void postXCNotification() {
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            XCNotificationCenter.defaultCenter().post(
                    XCNotificationName.name(XCNotificationConstant.kEvent1),
                    "aUserInfo",
                    MainActivity.this);
        }
    }, 8000);
}
```

