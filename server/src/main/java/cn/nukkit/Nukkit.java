package cn.nukkit;

import cn.nukkit.natives.NativeLibraryLoader;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.ServerKiller;
import cn.nukkit.utils.Utils;
import lombok.extern.log4j.Log4j2;

/**
 * `_   _       _    _    _ _ | \ | |     | |  | |  (_) | |  \| |_   _| | _| | ___| |_ | . ` | | | | |/ / |/ / | __| | |\  | |_| |   <|   <| | |_ |_| \_|\__,_|_|\_\_|\_\_|\__|
 */

/**
 * Nukkit启动类，包含{@code main}函数。<br>
 * The launcher class of Nukkit, including the {@code main} function.
 *
 * @author MagicDroidX(code) @ Nukkit Project
 * @author 粉鞋大妈(javadoc) @ Nukkit Project
 * @since Nukkit 1.0 | Nukkit API 1.0.0
 */
@Log4j2
public class Nukkit {

    public final static String VERSION = "1.0dev";

    public final static String API_VERSION = "1.0.0";

    public final static String CODENAME = "蘋果(Apple)派(Pie)";

    public final static String MINECRAFT_VERSION = "v0.15.10 alpha";

    public final static String MINECRAFT_VERSION_NETWORK = "0.15.10";

    public final static String PATH = System.getProperty("user.dir") + "/";

    public final static String DATA_PATH = System.getProperty("user.dir") + "/";

    public final static String PLUGIN_PATH = DATA_PATH + "plugins";

    public static final long START_TIME = System.currentTimeMillis();

    public static boolean ANSI = true;

    public static boolean shortTitle = false;

    public static int DEBUG = 1;


    public static void main(String[] args) {
        if (!NativeLibraryLoader.loadLibrary(Thread.currentThread().getContextClassLoader(), "nukkit")) {
            System.out.println("Couldn't load libnukkit for architecture " + System.getProperty("os.arch"));
            System.out.println("Please try compiling the JAR yourself to run Nukkit.");

            System.exit(1);
        }

        //Shorter title for windows 8/2012
        if (Utils.isWindows()) {
            shortTitle = true;
        }

        //启动参数
        for (String arg : args) {
            if (arg.equals("disable-ansi")) {
                ANSI = false;
                break;
            }
        }

        MainLogger logger = new MainLogger(DATA_PATH + "server.log");

        try {
            if (ANSI) {
                System.out.print((char) 0x1b + "]0;Starting Nukkit Server For Minecraft: PE" + (char) 0x07);
            }

            new Server(logger, PATH, DATA_PATH, PLUGIN_PATH);
        } catch (Exception e) {
            logger.logException(e);
        }

        if (ANSI) {
            System.out.print((char) 0x1b + "]0;Stopping Server..." + (char) 0x07);
        }

        logger.info("Stopping other threads");

        for (Thread thread : java.lang.Thread.getAllStackTraces().keySet()) {
            if (!(thread instanceof InterruptibleThread)) {
                continue;
            }

            logger.debug("Stopping " + thread.getClass().getSimpleName() + " thread");

            if (thread.isAlive()) {
                thread.interrupt();
            }
        }

        ServerKiller killer = new ServerKiller(8);

        killer.start();

        logger.shutdown();
        logger.interrupt();

        if (ANSI) {
            System.out.print((char) 0x1b + "]0;Server Stopped" + (char) 0x07);
        }

        System.exit(0);
    }


}