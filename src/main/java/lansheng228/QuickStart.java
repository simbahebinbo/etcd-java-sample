package lansheng228;

import cn.hutool.core.lang.Pair;
import com.ibm.etcd.client.EtcdClient;
import lansheng228.common.CommonConstant;
import lansheng228.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 快速开始
 */
@Slf4j
public class QuickStart {

    public static void main(String[] args) {

        try {
            String endpoints = CommonConstant.NODE_URL;

            //创建连接etcd客户端

            EtcdClient client = EtcdClient.forEndpoints(endpoints).withPlainText().build();

            log.info("client created");


            String customKey = RandomStringUtils.randomAlphabetic(10);
            String customValue = RandomStringUtils.randomAlphabetic(10);
            String customNewValue = RandomStringUtils.randomAlphabetic(10);

            log.info("customKey:\t" + customKey + "\tcustomValue:\t" + customValue + "\tcustomNewValue:\t" + customNewValue);

            Pair<String, String> customKeyValuePair = Pair.of(customKey, customValue);
            Pair<String, String> customNewKeyValuePair = Pair.of(customKey, customNewValue);
            (new WatchThread(client, customKey)).start();
            TimeUtil.sleep(1000);
            (new PutThread(client, customKeyValuePair)).start();
            TimeUtil.sleep(1000);
            (new PutThread(client, customNewKeyValuePair)).start();
            TimeUtil.sleep(1000);
            (new GetThread(client, customKey)).start();
            TimeUtil.sleep(1000);
            (new DeleteThread(client, customKey)).start();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
