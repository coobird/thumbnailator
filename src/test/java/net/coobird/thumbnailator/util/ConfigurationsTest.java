package net.coobird.thumbnailator.util;

import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class ConfigurationsTest {

    @Ignore
    private static class Base {
        @Before
        public void resetConfiguration() {
            Configurations.init();
        }

        @AfterClass
        public static void clearConfiguration() {
            Configurations.clear();
        }
    }

    @RunWith(AllTruePropertiesTest.CustomRunner.class)
    public static class AllTruePropertiesTest extends Base {
        @Test
        public void test() {
            for (Configurations config : Configurations.values()) {
                assertTrue(config.getBoolean());
            }
        }

        public static class CustomRunner extends ThreadContextClassLoaderReplacingClassRunner {
            public CustomRunner(Class<?> testClass) throws InitializationError {
                super(testClass);
            }

            protected ClassLoader getCustomClassLoader() {
                return new PropertiesFileRedirectingClassLoader("Configurations/all_true.txt");
            }
        }
    }

    @RunWith(AllFalsePropertiesTest.CustomRunner.class)
    public static class AllFalsePropertiesTest extends Base {
        @Test
        public void test() {
            for (Configurations config : Configurations.values()) {
                assertFalse(config.getBoolean());
            }
        }

        public static class CustomRunner extends ThreadContextClassLoaderReplacingClassRunner {
            public CustomRunner(Class<?> testClass) throws InitializationError {
                super(testClass);
            }

            protected ClassLoader getCustomClassLoader() {
                return new PropertiesFileRedirectingClassLoader("Configurations/all_false.txt");
            }
        }
    }

    public abstract static class ThreadContextClassLoaderReplacingClassRunner extends BlockJUnit4ClassRunner {
        public ThreadContextClassLoaderReplacingClassRunner(Class<?> testClass) throws InitializationError {
            super(testClass);
        }

        protected abstract ClassLoader getCustomClassLoader();

        @Override
        public void run(RunNotifier notifier) {
            Thread.currentThread().setContextClassLoader(getCustomClassLoader());
            super.run(notifier);
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
        }
    }

    @Ignore
    public static class PropertiesFileRedirectingClassLoader extends URLClassLoader {
        private final String resourceForPropertiesFile;

        public PropertiesFileRedirectingClassLoader(String resourceForPropertiesFile) {
            super(new URL[] {});
            this.resourceForPropertiesFile = resourceForPropertiesFile;
        }

        @Override
        public URL getResource(String name) {
            if (name.startsWith("thumbnailator.properties")) {
                return ClassLoader.getSystemClassLoader().getResource(resourceForPropertiesFile);
            }
            return super.getResource(name);
        }
    }
}
