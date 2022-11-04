package lansheng228;


import com.google.common.base.Charsets;
import com.ibm.etcd.api.RangeResponse;
import com.ibm.etcd.client.EtcdClient;
import lombok.extern.slf4j.Slf4j;

import static com.ibm.etcd.client.KeyUtils.bs;

@Slf4j
public class GetThread extends Thread {
    private final EtcdClient client;
    private final String key;

    //specify the kv revision
    private final Long rev = 0L;


    public GetThread(EtcdClient client, String key) {
        this.client = client;
        this.key = key;
    }

    public synchronized void run() {
        try {
            RangeResponse rr = client.getKvClient()
                    .get(bs(key)).sync();

            if (rr.getKvsList().isEmpty()) {
                // key does not exist
                log.warn("key is not exist");
                return;
            }

            String value = rr.getKvsList().get(0).getValue().toString(Charsets.UTF_8);
            log.info("GET!  key:\t" + key + "\tvalue:\t" + value);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}


