AndroidFileUploadWithPinProgress
================================

Android sample app which upload files to server ( Using IntentService &amp; Pin Progress )

Put upload.php file in htdocs & manually create uploads folder where all files will be saved.

Make sure to change the endpoint path to your local machine ip address.

```java
final UploadRequest request = new UploadRequest(MyActivity.this,
                "123",
                "http://192.168.1.127/upload.php");
```
