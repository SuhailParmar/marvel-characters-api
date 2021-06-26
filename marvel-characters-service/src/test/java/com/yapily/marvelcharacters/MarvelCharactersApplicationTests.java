package com.yapily.marvelcharacters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MarvelCharactersApplicationTests {

	@Test
	void testMd5Digest() {
		String d = MD5Utils.digest("hello", "world", "hello");
		// The expected hash contains the '0's we expect
		Assertions.assertEquals("04a3d22cc58005ae60e84985a6f6c557", d);
	}
}
