package developer.shivam.perfecto;

public interface OnNetworkRequest {

    /**
     * This method is use to do stuff like
     *  initializing a progress dialog or another loader view.
     */
    void onStart();

    /**
     * This method is called automatically when the network
     *  request is completed with 200 error
     */
    void onSuccess(String response);

    /**
     * This method is called is called when request is
     *  not completed due to some error or bad request
     */
    void onFailure(int responseCode, String responseMessage, String errorStream);

}
