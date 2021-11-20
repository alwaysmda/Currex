package billing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

interface IABReceiverCommunicator {
    void onNewBroadcastReceived(Intent intent);
}

public class IABReceiver extends BroadcastReceiver {

    private static final Object observerLock = new Object();
    private static List<IABReceiverCommunicator> observers = new ArrayList<>();

    static void addObserver(IABReceiverCommunicator communicator) {
        synchronized (observerLock) {
            observers.add(communicator);
        }
    }

    static void removeObserver(IABReceiverCommunicator communicator) {
        synchronized (observerLock) {
            observers.remove(communicator);
        }
    }

    private static void notifyObservers(Intent intent) {
        synchronized (observerLock) {
            for (IABReceiverCommunicator observer : observers) {
                observer.onNewBroadcastReceived(intent);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(intent.getAction() + ".iab");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            sendIntent.putExtras(bundle);
        }
        notifyObservers(sendIntent);
    }
}
