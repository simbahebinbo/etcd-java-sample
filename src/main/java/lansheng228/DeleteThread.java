package lansheng228;


import com.ibm.etcd.api.DeleteRangeResponse;
import com.ibm.etcd.client.EtcdClient;
import lombok.extern.slf4j.Slf4j;

import static com.ibm.etcd.client.KeyUtils.bs;

@Slf4j
public class DeleteThread extends Thread {
    private final EtcdClient client;
    private final String key;

    public DeleteThread(EtcdClient client, String key) {
        this.client = client;
        this.key = key;
    }

    public synchronized void run() {
        try {
            DeleteRangeResponse deleteResponse = client.getKvClient()
                    .delete(bs(key))
                    .sync();
            long num = deleteResponse.getDeleted();
            log.info("DELETE!  key:\t" + key + "\tnum:\t" + num);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}


