package net.coobird.thumbnailator.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This enum lists properties that affect the behavior of Thumbnailator.
 * Most are used for enabling or disabling workarounds.
 *
 * <h2>Implementation note</h2>
 * The values for properties listed here will be read from the system
 * properties via {@link System#getProperty(String)}, therefore, can be set
 * via the {@code -D} options when invoking the JVM.
 * Alternatively, they can be included in {@code thumbnailator.properties}
 * on the classpath (of the current thread's class loader.)
 *
 * <h2>Disclaimer</h2>
 * Properties listed here are not part of Thumbnailator's public API.
 * Therefore, code invoking Thumbnailator should not depend on these
 * workarounds always being present.
 */
public enum Configurations {
    /**
     * Disables the Exif workaround.
     * (<a href="https://github.com/coobird/thumbnailator/issues/108">Issue #108</a>)
     * <br>
     * Property name: {@code thumbnailator.disableExifWorkaround}
     * <p>
     * The default JPEG reader bundled with the JRE cannot handle JPEG images
     * that doesn't have a JFIF APP0 marker segment as the first one.
     * Some JPEG images have Exif (APP1) as the first one.
     * <p>
     * A workaround was introduced to resolve issue #108 by capturing the
     * Exif data from the bitstream rather than relying on the JPEG reader.
     * <p>
     * Disabling this workaround will prevent Thumbnailator from properly
     * identifying the image orientation of JPEG images which have Exif as
     * the first marker.
     * Therefore, unless the default behavior causes issues, disabling this
     * workaround is not recommended.
     */
    DISABLE_EXIF_WORKAROUND("thumbnailator.disableExifWorkaround"),

    /**
     * Enable debug logging to standard error.
     * This will enable debug logging throughout Thumbnailator.
     * <br>
     * Property name: {@code thumbnailator.debugLog}
     */
    DEBUG_LOG("thumbnailator.debugLog"),

    /**
     * Enable debug logging (to standard error) specifically for the
     * {@link #DISABLE_EXIF_WORKAROUND Exif workaround}.
     * <br>
     * Property name: {@code thumbnailator.debugLog.exifWorkaround}
     */
    DEBUG_LOG_EXIF_WORKAROUND("thumbnailator.debugLog.exifWorkaround"),

    /**
     * Enable a workaround to conserve memory when handling large image files.
     * (<a href="https://github.com/coobird/thumbnailator/issues/69">Issue #69</a>)
     * <p>
     * It is known that {@link OutOfMemoryError}s can occur when handling
     * large images in Thumbnailator.
     * <p>
     * To fundamentally address the issue requires some dramatic design changes
     * to the core parts of Thumbnailator which would take some time and would
     * affect many internal parts of the library.
     * Such changes would take time before being implemented, a workaround has
     * been implemented in Thumbnailator 0.4.8 to reduce the likeliness of
     * {@code OutOfMemoryError}s.
     * <p>
     * The workaround is not enabled by default, as it can negatively affect
     * the quality of the final image. It has also not been extensively
     * tested and will not necessarily prevent {@code OutOfMemoryError}s.
     * <p>
     * When the workaround enabled, a smaller version of the source image will
     * be used to reduce memory usage, under the following conditions:
     * <ul>
     *   <li>Both height and width have dimensions larger than 1800 pixels</li>
     *   <li>The expected memory size of the source image will take up more
     *       than 1/4 of the available JVM free memory</li>
     * </ul>
     */
    CONSERVE_MEMORY_WORKAROUND("thumbnailator.conserveMemoryWorkaround")
    ;

    private final String key;
    private static final Properties properties;

    static {
        properties = new Properties();
        init();
    }

    /**
     * This method will initialize configurations from
     * {@code thumbnailator.properties} on the classpath.
     * <p>
     * Implementation note: {@code thumbnailator.properties} is searched from
     *                      the class loader associated with the current
     *                      thread, rather than the system class loader.
     * <p>
     * This is intended only to be called from tests.
     */
    static void init() {
        properties.clear();

        InputStream resourceIs = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("thumbnailator.properties");

        if (resourceIs != null) {
            try {
                properties.load(resourceIs);
                resourceIs.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while reading thumbnailator.properties", e);
            }
        }
    }

    /**
     * Clears the internal cache of properties.
     * <p>
     * This is intended only to be called from tests.
     */
    static void clear() {
        properties.clear();
    }

    Configurations(String key) {
        this.key = key;
    }

    /**
     * Returns whether the specified configuration is enabled or not.
     * @return  {@code true} if the configuration is enabled, {@code false}
     *          otherwise.
     */
    public boolean getBoolean() {
        String propertyValue = properties.getProperty(key);
        if (propertyValue != null) {
            return Boolean.parseBoolean(propertyValue);
        }
        return Boolean.getBoolean(key);
    }
}
