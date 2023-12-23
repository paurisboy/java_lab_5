package org.lab;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for the {@link Injector} class.
 */
public class TestInjection extends TestCase {

    /**
     * Constructs a new test case with the given name.
     *
     * @param testName The name of the test case.
     */
    public TestInjection(String testName) {
        super(testName);
    }

    /**
     * Creates a test suite containing all test cases in this class.
     *
     * @return The test suite containing all test cases.
     */
    public static Test suite() {
        return new TestSuite(TestInjection.class);
    }

    /**
     * Tests the {@link Injector#inject(Object)} method to ensure that it correctly injects
     * dependencies into fields annotated with {@link AutoInjectable}.
     */
    public void testInject_ShouldInjectDependencies_WhenFieldsAnnotatedWithAutoInjectable() {
        Injector injector = new Injector();
        SomeBean someBean = new SomeBean();

        SomeBean result = injector.inject(someBean);

        assertNotNull(result);
        assertNotNull(result.getField1());
        assertNotNull(result.getField2());
        assertTrue(result.getField1() instanceof SomeInterface);
        assertTrue(result.getField2() instanceof SomeOtherInterface);
    }

    /**
     * Tests the {@link Injector#inject(Object)} method to ensure that it injects the correct
     * implementations when the properties file is configured.
     */
    public void testInject_ShouldInjectCorrectImplementations_WhenPropertiesFileIsConfigured() {

        Injector injector = new Injector();
        SomeBean someBean = new SomeBean();

        SomeBean result = injector.inject(someBean);

        assertNotNull(result);
        assertEquals("org.lab.SomeImpl", result.getField1().getClass().getName());
        assertEquals("org.lab.SoDoer", result.getField2().getClass().getName());
    }
}