import java.awt.Color;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Particle;
import edu.uwm.cs351.ParticleSeq;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;



public class TestParticleSeq extends LockedTestCase {

	private ParticleSeq s;
	Particle b1 = new Particle(new Point(1,1), new Vector(1,1), 1, Color.BLACK);
	Particle b2 = new Particle(new Point(2,2), new Vector(2,2), 2, Color.BLUE);
	Particle b3 = new Particle(new Point(3,3), new Vector(3,3), 3, Color.GREEN);
	Particle b4 = new Particle(new Point(4,4), new Vector(4,4), 4, Color.YELLOW);
	Particle b5 = new Particle(new Point(5,5), new Vector(5,5), 5, Color.RED);
	
	Particle b[] = { null, b1, b2, b3, b4, b5 };
	
	// Using the above array
	// convert a Particle result to an integer:
	// 0 = null, 1 = b1, 2 = b2 etc.
	// if the expression causes an error, the index is -1.
	// If the ball is not in the array, the result "NONE OF THE ABOVE" is -2.
	int ix(Supplier<Particle> p) {
		try {
			Particle ball = p.get();
			if (ball == null) return 0;
			for (int i=0; i < b.length; ++i) {
				if (ball == b[i]) return i;
			}
			return -2;
		} catch (RuntimeException ex) {
			return -1;
		}
	}
	
	@Override
	public void setUp() {
		s = new ParticleSeq();
		try {
			assert 3/((int)b1.getPosition().x()-1) == 42 : "OK";
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (ArithmeticException ex) {
			return;
		}
	}

	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}
	
	// This test tests many things.
	// We recommend that you run the UnlockTests to unlock all its tests at once.
	public void test() {
		assertEquals(Ti(887770454),s.size());
		// In the following, if a Particle index is called for ("ix"), answer:
		// 0 if the result will be null
		// 1 if the result will be the Particle b1
		// 2 if the result will be the Particle b2
		// 3,4,5 etc.
		// -1 if the code will crash with an exception
		// -2 none of the above (some other ball)
		assertEquals(Ti(491728531),ix(() -> s.getCurrent()));
		s.addBefore(b1);
		assertEquals(Ti(1854083915),ix(() -> s.getCurrent()));
		s.addAfter(null);
		assertEquals(Ti(236938571),ix(() -> s.getCurrent()));
		s.addBefore(b3);
		assertEquals(Ti(710084632),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(565750963),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(371995505),ix(() -> s.getCurrent()));
		s.addBefore(new Particle(b2.getPosition(),b2.getVelocity(),2,Color.BLUE));
		assertEquals(Ti(221507525),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(1390748355),ix(() -> s.getCurrent()));
		testcont(s);		
		testremove(s);
	}
	
	// Continuation of test:
	private void testcont(ParticleSeq s) {
		s.start();
		s.advance();
		s.advance();
		assertNull(s.getCurrent());
		// At this point s is [b1,b3,*null,clone(b2)] with * marking current
		ParticleSeq s2 = new ParticleSeq();
		assertEquals(true,s2.atEnd());
		// In the following, if a Particle index is called for ("ix"), answer:
		// 0 if the result will be null
		// 1 if the result will be the Particle b1
		// 2 if the result will be the Particle b2, etc for 3, 4, 5
		// -1 if the code will crash with an exception
		// -2 none of the above (some other ball)
		s2.addBefore(b4);
		assertEquals(false,s2.atEnd());
		s2.addAfter(b5);
		assertEquals(Ti(1860957759),ix(() -> s2.getCurrent()));
		s.addAll(s2);
		assertEquals(Ti(566769707),s.size());
		// What does addAll() say about what is current afterwards?
		assertEquals(Ti(1505728679),ix(()->s.getCurrent()));
		assertEquals(Ti(1459925108),ix(() -> s2.getCurrent()));
		s.advance();
		assertEquals(Ti(914153475),ix(()->s.getCurrent()));
		s.advance();
		assertEquals(Ti(1794508226),ix(()->s.getCurrent()));
	}
	
	// continuation of test
	private void testremove(ParticleSeq s) {
		// s is [b1, b3, null, clone(b2), *b4, b5] where * marks current element
		// In the following, if a Particle index is called for ("ix"), answer:
		// 0 if the result will be null
		// 1 if the result will be the Particle b1
		// 2 if the result will be the Particle b2, etc for 3, 4, 5
		// -1 if the code will crash with an exception
		// -2 none of the above (some other ball)
		s.removeCurrent();
		assertEquals(Ti(742051504),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(1393501683),ix(() -> s.getCurrent()));
		assertEquals(Tb(1809270213),s.atEnd());
		s.removeCurrent();
		assertEquals(Tb(1429063010),s.atEnd());
		assertEquals(Ti(1957337308),ix(() -> s.getCurrent()));
		s.start();
		s.removeCurrent();
		assertFalse(s.isCurrent());
		s.advance();
		assertEquals(Ti(868270416),ix(() -> s.getCurrent()));
	}
	
	public void test00() {
		assertEquals(0,s.size());
	}
	
	public void test01() {
		assertFalse(s.isCurrent());
	}
	
	public void test02() {
		assertTrue(s.atEnd());
	}
	
	public void test03() {
		assertException(IllegalStateException.class,() -> {s.getCurrent();});		
	}
	
	public void test04() {
		assertException(IllegalStateException.class, () -> {s.advance();});
	}
	
	public void test05() {
		assertException(IllegalStateException.class, () -> {s.removeCurrent();});
	}
	
	public void test06() {
		s.start();
		assertFalse(s.isCurrent());
	}
	
	public void test10() {
		s.addBefore(null);
		assertEquals(1,s.size());
		assertTrue(s.isCurrent());
		assertNull(s.getCurrent());
		assertFalse(s.atEnd());
	}
	
	public void test11() {
		s.addBefore(b2);
		assertEquals(1,s.size());
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		assertFalse(s.atEnd());
	}
	
	public void test12() {
		s.addAfter(b3);
		assertEquals(1,s.size());
		assertTrue(s.isCurrent());
		assertSame(b3,s.getCurrent());
		assertFalse(s.atEnd());
	}
	
	public void test13() {
		s.addAfter(b1);
		assertEquals(1,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		assertFalse(s.atEnd());
		s.advance();
		assertEquals(1,s.size());
		assertFalse(s.isCurrent());
		assertTrue(s.atEnd());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		assertEquals(1,s.size());
		assertFalse(s.atEnd());
	}
	
	public void test14() {
		s.addBefore(b1);
		assertEquals(1,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		assertFalse(s.atEnd());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		assertSame(b1,s.getCurrent());
		assertFalse(s.atEnd());
		s.advance();
		assertEquals(1,s.size());
		assertFalse(s.isCurrent());
		assertTrue(s.atEnd());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		assertEquals(1,s.size());
		assertFalse(s.atEnd());
	}
	
	public void test15() {
		s.addBefore(b4);
		s.removeCurrent();
		assertFalse(s.isCurrent());
		assertTrue(s.atEnd());
		assertEquals(0,s.size());
	}
	
	public void test16() {
		s.addAfter(b5);
		s.start();
		s.removeCurrent();
		assertFalse(s.isCurrent());
		assertTrue(s.atEnd());
		assertEquals(0,s.size());
	}

	public void test17() {
		s.addBefore(b1);
		s.advance();
		assertFalse(s.isCurrent());
		assertTrue(s.atEnd());
		assertEquals(1,s.size());
		assertException(IllegalStateException.class,() -> { s.removeCurrent(); });
	}
	
	public void test18() {
		s.addAfter(b2);
		s.advance();
		assertException(IllegalStateException.class, () -> { s.advance(); });
	}
	
	public void test19() {
		s.addBefore(b3);
		s.advance();
		assertException(IllegalStateException.class,() -> { s.getCurrent(); });
	}
	
	public void test20() {
		s.addAfter(b1);
		s.addBefore(b2);
		assertEquals(2,s.size());
		assertFalse(s.atEnd());
		assertSame(b2,s.getCurrent());
	}
	
	public void test21() {
		s.addBefore(b3);
		s.addBefore(b4);
		s.advance();
		assertEquals(2,s.size());
		assertFalse(s.atEnd());
		assertSame(b3,s.getCurrent());
	}
	
	public void test22() {
		s.addBefore(b1);
		s.addAfter(b2);
		assertEquals(2,s.size());
		assertFalse(s.atEnd());
		assertSame(b2,s.getCurrent());
	}
	
	public void test23() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.advance();
		assertEquals(2,s.size());
		assertTrue(s.atEnd());
		assertFalse(s.isCurrent());
	}
	
	public void test24() {
		s.addAfter(b1);
		s.addAfter(b2);
		assertSame(b2,s.getCurrent());
		assertFalse(s.atEnd());
	}
	
	public void test25() {
		s.addBefore(b3);
		s.addAfter(b4);
		s.start();
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b4,s.getCurrent());
	}
	
	public void test26() {
		s.addAfter(b5);
		s.addBefore(b1);
		s.removeCurrent();
		assertFalse(s.isCurrent());
		assertFalse(s.atEnd());
	}
	
	public void test27() {
		s.addBefore(b2);
		s.addAfter(b3);
		s.removeCurrent();
		assertFalse(s.isCurrent());
		assertTrue(s.atEnd());
	}
	
	public void test28() {
		s.addBefore(b4);
		s.addBefore(b5);
		s.removeCurrent();
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b4,s.getCurrent());
	}
	
	public void test29() {
		s.addAfter(b1);
		s.addBefore(b2);
		s.removeCurrent();
		s.advance();
		s.removeCurrent();
		assertFalse(s.isCurrent());
		assertTrue(s.atEnd());
		assertEquals(0,s.size());
	}
	
	public void test30() {
		s.addBefore(b1);
		s.addBefore(null);
		s.addBefore(b2);
		assertSame(b2,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertNull(s.getCurrent());
	}
	
	public void test31() {
		s.addBefore(b3);
		s.addBefore(b4);
		s.addAfter(b5);
		s.advance();
		assertSame(b3,s.getCurrent());
		assertEquals(3,s.size());
	}
	
	public void test32() {
		s.addBefore(b1);
		s.addAfter(b2);
		s.advance();
		assertTrue(s.atEnd());
		s.addAfter(b3);
		assertSame(b3,s.getCurrent());
	}
	
	public void test33() {
		s.addAfter(b4);
		s.addAfter(b5);
		s.addAfter(null);
		assertFalse(s.atEnd());
		assertNull(s.getCurrent());
		assertEquals(3,s.size());
	}
	
	public void test34() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.addBefore(b3);
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertTrue(s.atEnd());
		assertException(IllegalStateException.class,() -> { s.getCurrent(); });
	}
	
	public void test35() {
		s.addAfter(null);
		s.addBefore(b4);
		s.addAfter(b5);
		s.advance();
		assertNull(s.getCurrent());
		assertFalse(s.atEnd());
		s.removeCurrent();
		assertTrue(s.atEnd());
	}
	
	public void test36() {
		s.addAfter(b4);
		s.addAfter(b5);
		s.start();
		s.addAfter(b1);
		s.start();
		s.removeCurrent();
		s.advance();
		assertSame(b1,s.getCurrent());
	}
	
	public void test37() {
		s.addAfter(b2);
		s.addBefore(b3);
		s.addBefore(b4);
		s.removeCurrent();
		assertFalse(s.isCurrent());
		s.advance();
		s.advance();
		s.removeCurrent();
		assertEquals(1,s.size());
	}
	
	public void test38() {
		s.addAfter(b5);
		s.addBefore(b1);
		s.removeCurrent();
		s.addAfter(b2);
		s.advance();
		assertSame(b5,s.getCurrent());
	}
	
	public void test39() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.addAfter(b3);
		s.addAfter(b4);
		s.addAfter(b5);
		assertSame(b5,s.getCurrent());
		s.addAfter(b1);
		s.addAfter(b2);
		s.addAfter(b3);
		s.addAfter(b4);
		s.addAfter(b5);
		s.addAfter(b1);
		s.addAfter(b2);
		assertEquals(12,s.size());
		s.removeCurrent();
		assertFalse(s.isCurrent());
		s.start();
		s.removeCurrent();
		assertEquals(10,s.size());
		s.start();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}

	public void test40() {
		ParticleSeq se = new ParticleSeq();
		s.addAll(se);
		assertEquals(0,s.size());
	}

	public void test41() {
		ParticleSeq se = new ParticleSeq();
		s.addBefore(b1);
		s.addAll(se);
		assertEquals(b1,s.getCurrent());
	}

	public void test42() {
		ParticleSeq se = new ParticleSeq();
		s.addAfter(b2);
		s.advance();
		s.addAll(se);
		assertFalse(s.isCurrent());
	}

	public void test43() {
		ParticleSeq se = new ParticleSeq();
		s.addBefore(b3);
		s.addAfter(b4);
		s.addAll(se);
		assertSame(b4,s.getCurrent());
	}

	public void test44() {
		ParticleSeq se = new ParticleSeq();
		se.addBefore(b1);
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertTrue(se.isCurrent());
		assertEquals(1,s.size());
		assertEquals(1,se.size());
		s.start();
		assertSame(b1,s.getCurrent());
		assertSame(b1,se.getCurrent());
	}
	
	public void test45() {
		ParticleSeq se = new ParticleSeq();
		se.addAfter(b1);
		s.addAfter(b2);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}
	
	public void test46() {
		ParticleSeq se = new ParticleSeq();
		se.addAfter(b1);
		s.addAfter(b2);
		s.advance();
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertTrue(se.isCurrent());
		assertSame(b1,se.getCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}
	
	public void test47() {
		ParticleSeq se = new ParticleSeq();
		se.addAfter(b1);
		se.advance();
		s.addAfter(b3);
		s.addBefore(b2);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertFalse(se.isCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());	
	}
	
	public void test48() {
		ParticleSeq se = new ParticleSeq();
		se.addAfter(b1);
		s.addAfter(b2);
		s.addAfter(b3);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
	}
	
	public void test49() {
		ParticleSeq se = new ParticleSeq();
		se.addAfter(b1);
		s.addAfter(b2);
		s.addAfter(b3);
		s.advance();
		assertFalse(s.isCurrent());
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(b1,se.getCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}

	public void test50() {
		ParticleSeq se = new ParticleSeq();
		se.addAfter(b2);
		se.addBefore(b1);	
		s.addAfter(b4);
		s.addBefore(b3);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
	}

	public void test51() {
		ParticleSeq se = new ParticleSeq();
		se.addAfter(b2);
		se.addBefore(b1);
		se.advance();
		s.addAfter(b3);
		s.addAfter(b4);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(b2,se.getCurrent()); se.advance();
		assertFalse(se.isCurrent());
		// check s
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(b3,s.getCurrent());
	}

	public void test52() {
		ParticleSeq se = new ParticleSeq();
		se.addBefore(b2);
		se.addBefore(b1);
		se.advance();
		se.advance();
		s.addAfter(b3);
		s.addAfter(b4);
		s.advance();
		assertFalse(s.isCurrent());
		assertFalse(se.isCurrent());
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertFalse(se.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		s.start();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
	}

	public void test53() {
		ParticleSeq se = new ParticleSeq();
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		se.addAfter(b3);
		se.addAfter(b4);
		se.addAfter(b5);
		// se has 24 elements
		s.addAfter(b1);
		s.addAfter(b2);
		s.addAll(se);
		assertEquals(26,s.size());
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		s.addAll(se);
		assertEquals(50,s.size());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent());
	}
	
	public void test60() {
		s.addAll(s);
		assertFalse(s.isCurrent());
		assertEquals(0,s.size());
	}
	
	
	public void test61() {
		s.addAfter(b1);
		s.addAll(s);
		assertEquals(2,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test62() {
		s.addAfter(b1);
		s.advance();
		s.addAll(s);
		assertEquals(2,s.size());
		assertFalse(s.isCurrent());
	}
	
	public void test63() {
		s.addAfter(b1);
		s.removeCurrent();
		assertEquals(0,s.size());
		assertFalse(s.isCurrent());
	}
	
	public void test64() {
		s.addAfter(b2);
		s.addBefore(b1);
		s.addAll(s);
		assertEquals(4,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}
	
	public void test65() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.addAll(s);
		assertEquals(4,s.size());
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}

	public void test66() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.advance();
		assertFalse(s.isCurrent());
		s.addAll(s);
		assertFalse(s.isCurrent());
		assertEquals(4,s.size());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}

	public void test67() {
		s.addAfter(b1);
		s.addAfter(b2);
		s.addAll(s);
		s.removeCurrent();
		s.addBefore(b3);
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		s.advance();
		s.addAll(s);
		assertEquals(8,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}
	
	public void test68() {
		ParticleSeq se = new ParticleSeq();
		se.addAfter(b1);
		se.addAfter(b2);	
		s.addAfter(b3);
		s.addAfter(b4);
		s.addAll(se);
		s.advance();
		s.addAfter(b5);
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		assertEquals(5,s.size());
		assertEquals(2,se.size());
		assertSame(b2,se.getCurrent());
		se.advance();
		assertFalse(se.isCurrent());
		se.start();
		assertSame(b1,se.getCurrent());
	}
	
	public void test70() {
		ParticleSeq c = s.clone();
		assertFalse(c.isCurrent());
		assertEquals(0, c.size());
	}
	
	public void test71() {
		s.addAfter(b1);
		ParticleSeq c = s.clone();
		
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b1,c.getCurrent()); c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
	}
	
	public void test72() {
		s.addAfter(b1);
		s.advance();
		ParticleSeq c = s.clone();
		
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
	}

	public void test73() {
		s.addAfter(b1);
		s.addAfter(b2);
		ParticleSeq c = s.clone();
		
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b2,s.getCurrent());
		assertSame(b2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
	}
	
	public void test74() {
		s.addBefore(b2);
		s.addBefore(b1);
		s.advance();
		s.addAfter(b3);
		ParticleSeq c = s.clone();
		assertSame(b3,s.getCurrent());
		assertSame(b3,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		s.start();
		c.start();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b1,s.getCurrent());
		assertSame(b1,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b2,s.getCurrent());
		assertSame(b2,c.getCurrent());
	}
	
	public void test75() {
		s.addBefore(b1);
		ParticleSeq c = s.clone();
		s.addBefore(b2);
		assertSame(b2,s.getCurrent());
		assertSame(b1,c.getCurrent());
		c = s.clone();
		s.addBefore(b3);
		assertSame(b3,s.getCurrent());
		assertSame(b2,c.getCurrent());
		assertEquals(2,c.size());
	}
	
	public void test76() {
		s.addAfter(b1);
		s.addAfter(b3);
		s.addBefore(b2);
		s.removeCurrent();
		
		ParticleSeq c = s.clone();
		
		assertEquals(2,c.size());
		
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		
		s.advance();
		c.advance();
		
		assertSame(b3,s.getCurrent());
		assertSame(b3,c.getCurrent());
	}

	public void test77() {
		s.addAfter(b4);
		s.addBefore(b5);
		s.addAfter(b1);
		s.removeCurrent();
		
		ParticleSeq c = s.clone();
		assertFalse(s.isCurrent());
		c.advance();
		assertSame(b4,c.getCurrent());
	}
	
	public void test78() {
		s.addBefore(b2);
		s.addAfter(b3);
		
		ParticleSeq c = s.clone();
		assertEquals(b3,c.getCurrent());
		c.advance();
		assertTrue(c.atEnd());
	}
	
	public void test79() {
		s.addBefore(b4);
		s.addBefore(b5);
		
		ParticleSeq c = s.clone();
		c.removeCurrent();
		
		assertFalse(c.isCurrent());
		assertEquals(b5,s.getCurrent());
	}
	
	public void test88() {
		s.addAfter(b1);
		s.addAfter(b2);
		
		ParticleSeq c = s.clone();
		s.addBefore(b3);
		c.addBefore(b4);
		
		assertSame(b3,s.getCurrent());
		assertSame(b4,c.getCurrent());
		s.advance();
		c.advance();
		assertSame(b2,s.getCurrent());
		assertSame(b2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		
		s.start();
		c.start();
		assertSame(b1,s.getCurrent());
		assertSame(b1,c.getCurrent());
		s.advance();
		c.advance();
		assertSame(b3,s.getCurrent());
		assertSame(b4,c.getCurrent());
	}

}
