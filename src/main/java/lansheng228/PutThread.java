package lansheng228;


import cn.hutool.core.lang.Pair;
import com.ibm.etcd.client.EtcdClient;
import lombok.extern.slf4j.Slf4j;

import static com.ibm.etcd.client.KeyUtils.bs;

@Slf4j
public class PutThread extends Thread {
    private final EtcdClient client;
    private final Pair<String, String> keyValuePair;

    public PutThread(EtcdClient client, Pair<String, String> keyValuePair) {
        this.client = client;
        this.keyValuePair = keyValuePair;
    }

    public synchronized void run() {
        try {
            String key = keyValuePair.getKey();
            String value = keyValuePair.getValue();
            client.getKvClient()
                    .put(bs(key), bs(value))
                    .sync();
            log.info("PUT!  key:\t" + key + "\tvalue:\t" + value);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}


