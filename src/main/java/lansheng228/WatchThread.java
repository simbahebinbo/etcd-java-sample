package lansheng228;


import com.google.common.base.Charsets;
import com.google.protobuf.ByteString;
import com.ibm.etcd.api.Event;
import com.ibm.etcd.client.EtcdClient;
import com.ibm.etcd.client.kv.KvClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static com.ibm.etcd.client.KeyUtils.bs;

@Slf4j
public class WatchThread extends Thread {
    private final EtcdClient client;
    private final String key;

    //    Revision to start watching
    private final Long rev = 0L;

    //   the maximum number of events to receive
    private final Integer maxEvents = Integer.MAX_VALUE;


    public WatchThread(EtcdClient client, String key) {
        this.client = client;
        this.key = key;
    }

    public synchronized void run() {
        CountDownLatch latch = new CountDownLatch(maxEvents);
        KvClient.WatchIterator watchIterator = null;

        try {
            ByteString watchKey = bs(key);
            watchIterator = client.getKvClient().watch(watchKey).startRevision(rev).start();

            while (watchIterator.hasNext()) {
                Event event = watchIterator.next().getEvents().get(0);
                String type = event.getType().toString();
                String key = Optional.of(event.getKv().getKey()).map(bs -> bs.toString(Charsets.UTF_8)).orElse("");
                String value = Optional.of(event.getKv().getValue()).map(bs -> bs.toString(Charsets.UTF_8)).orElse("");
                log.info("WATCH! type={}, key={}, value={}", type, key, value);
                latch.countDown();
            }

            latch.await();
        } catch (Exception e) {
            if (watchIterator != null) {
                watchIterator.close();
            }
            log.warn(e.getMessage());
        }
    }
}


