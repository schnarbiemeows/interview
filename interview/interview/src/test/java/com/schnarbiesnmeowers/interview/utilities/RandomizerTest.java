package com.schnarbiesnmeowers.interview.utilities;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * class to test the Randomizer class
 * @author Dylan I. Kessler
 *
 */
//@RunWith(SpringRunner.class)
public class RandomizerTest {

	@Test
	public void testMethods() {
	    assertNotNull(Randomizer.randomString(10));
		assertNotNull(Randomizer.randomInt(10)) ;
		assertNotNull(Randomizer.randomLong(10)) ;
		assertNotNull(Randomizer.randomFloat(10F)) ;
		assertNotNull(Randomizer.randomDouble(10));
		assertNotNull(Randomizer.randomBigDecimal("10"));
		assertNotNull(Randomizer.randomBigInteger("10"));
		assertNotNull(Randomizer.randomDate()) ;
		assertNotNull(Randomizer.randomBoolean()) ;
		assertNotNull(Randomizer.randomTimestamp(1000)) ;
		assertNotNull(Randomizer.randomTime(1000)) ;
		assertNotNull(Randomizer.randomBytes(10)) ;
	}
}
