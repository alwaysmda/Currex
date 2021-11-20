package billing.communication;


import billing.IabResult;

public interface BillingSupportCommunication {
    void onBillingSupportResult(int response);

    void remoteExceptionHappened(IabResult result);
}
