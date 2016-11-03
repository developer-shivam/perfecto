#perfecto - Network request with ease.

[![License](https://img.shields.io/badge/License-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## Use Case for "GET" Request
```java
Perfecto.with(MainActivity.this)
	.fromUrl("http://www.google.com")
	.ofTypeGet()
	.connect(new OnNetworkRequest() {

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(String response) {
            Log.d("Response", response);
        }

        @Override
        public void onFailure(int responseCode, String responseMessage, String errorStream) {
            Log.d("Response code", String.valueOf(responseCode));
            Log.d("Response message", responseMessage);
            Log.d("Error stream", errorStream);
        }
	});
```

## Integration
Add these lines in build.gradle at project leve
```
repositories {
	jcenter()
	maven { url "https://jitpack.io" }
}
```

Add these lines in build.gradle at app level
```
dependencies {
    compile 'com.github.developer-shivam:perfecto:1.0.0'
}
```

## License
Copyright (c) 2016 Shivam Satija

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
