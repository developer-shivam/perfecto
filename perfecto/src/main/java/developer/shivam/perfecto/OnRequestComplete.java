package developer.shivam.perfecto;

public interface OnRequestComplete {

    /**
     * This method is called automatically when the network
     *  request is completed with 200 error
     */
    public void onComplete(String response);

    /**
     * This method is called is called when request is
     *  not completed due to some fault
     */
    public void onFailure(String error);

}
