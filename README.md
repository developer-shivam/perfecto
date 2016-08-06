#Perfecto - Networking library

##Use Case for "GET" Request
```java
				
		Perfecto.with(context)
            .fromUrl("https://www.google.co.in")
            .ofTypeGet()
            .connect(new OnRequestComplete() {
               
                @Override
                public void onSuccess(String response) {
                    Log.d("Response", response);    
                }

                @Override
                public void onFailure(String error) {
                    /**
                    *  This will return the errorStream(), responseCode and responseMessage
                    *  Use Log to get the error.
                    */
                      
                }
            });
```


