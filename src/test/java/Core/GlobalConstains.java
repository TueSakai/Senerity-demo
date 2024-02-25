package Core;
import java.io.*;

public class GlobalConstains {
    public static int LONG_TIME = 30;
    public static int SHORT_TIME = 5;
    public static final String PORTAL_PAGE_URL = "https://demo.nopcommerce.com/";
    public static final String ADMIN_PAGE_URL = "https://admin-demo.nopcommerce.com/";

    public static final String OS_NAME = System.getProperty("os.name");

    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String UPLOAD_FILE = PROJECT_PATH + File.separator + "uploadFiles" + File.separator;
    public static final String BROWSER_LOG = PROJECT_PATH + File.separator + "browserLogs";
    public static final String DOWNLOAD_FILE = PROJECT_PATH + File.separator + "downloadFiles";


}
