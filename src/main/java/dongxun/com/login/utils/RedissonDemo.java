package dongxun.com.login.utils;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonDemo {
	
	public static void main(String[] args) {
		 redisson();
	}
	
	public static void redisson() {
		Config config = new Config();
		config.useSingleServer()
		.setAddress("redis://39.107.67.13:6379")
		.setConnectTimeout(5000)
		.setDatabase(0);
		RedissonClient redisson = Redisson.create(config);
		RLock lock = redisson.getLock("anyLock");
		// 最常见的使用方法
		lock.lock();
		lock.unlock();
	}

}
