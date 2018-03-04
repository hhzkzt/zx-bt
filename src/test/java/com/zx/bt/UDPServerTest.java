package com.zx.bt;

import com.zx.bt.entity.InfoHash;
import com.zx.bt.repository.InfoHashRepository;
import com.zx.bt.socket.TCPClient1;
import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

/**
 * author:ZhengXing
 * datetime:2018-02-13 14:04
 * 测试UDP
 */
@Slf4j
public class UDPServerTest extends BtApplicationTests{

	@Autowired
	private InfoHashRepository infoHashRepository;

	@Autowired
	private TCPClient1 tcpClient1;


	@Test
	@SneakyThrows
	public void test1() {
		RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
				.withinRange('0', '9').build();
		String a = "-ZX0001-" + randomStringGenerator.generate(12);
		byte[] peerId = a.getBytes(CharsetUtil.ISO_8859_1);
		InfoHash one = infoHashRepository.findOne(1787L);
		List<InfoHash> all = Collections.singletonList(one);
//		List<InfoHash> all = infoHashRepository.findAll();
		all.stream().forEach(infoHash -> {
			String peerAddress = infoHash.getPeerAddress();
			String[] addArr = StringUtils.split(peerAddress, ";");
			for (String s : addArr) {
				String[] ipPort = s.split(":");
//				log.info("ip:{},ports:{},infoHash:{}",ipPort[0],Integer.parseInt(ipPort[1]),infoHash.getInfoHash());
				tcpClient1.connection(new InetSocketAddress(ipPort[0],Integer.parseInt(ipPort[1])),
						infoHash.getInfoHash(), peerId);
			}

		});






		Thread.sleep(100000000);
	}




}