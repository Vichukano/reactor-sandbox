package ru.vichukano.reactor;

import java.util.concurrent.Flow;

public class PublisherImpl implements Flow.Publisher<String> {
    private final Flow.Subscription subscription = new Flow.Subscription() {

        @Override
        public void request(long l) {
            System.out.println("Request:" + l);
        }

        @Override
        public void cancel() {
            System.out.println("Cancel subscription");
        }
    };


    @Override
    public void subscribe(Flow.Subscriber<? super String> subscriber) {
        subscriber.onSubscribe(subscription);
    }
}
