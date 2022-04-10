package ru.vichukano.reactor;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Flow.Publisher<String> publisher = new PublisherImpl();
        Flow.Subscriber<String> subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
        SubmissionPublisher<String> sp = new SubmissionPublisher<>();
        sp.subscribe(subscriber);
        sp.submit("boo");
        Thread.sleep(1000L);
    }
}
