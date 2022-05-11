package mk.ukim.finki.wp.schedulerspringbootproject.Config;

public class Constants {

    //email region start
    public static final String EMAIL_FROM = "wp.workscheduler@gmail.com";

    public static final String EMAIL_TITLE_UPDATE = "Booking Updated!";
    public static final String EMAIL_BODY_UPDATE = "Your booking has been updated.\nStatus: ";

    public static final String EMAIL_TITLE_WELCOME = "My Work Scheduler";
    public static final String EMAIL_BODY_WELCOME_ADMIN = "Welcome!\nYou are now registered on Work Scheduler as an administrator.\n";

    public static final String EMAIL_TITLE_WELCOME_EMPLOYEE_PASSWORD = "My Work Scheduler Password";
    public static final String EMAIL_BODY_WELCOME_EMPLOYEE = "Welcome!\nYou are now registered on Work Scheduler.\nTo login use the following generated password ";
    public static final String EMAIL_BODY_CHANGE_PASSWORD = "Please change the password once you log in for security reasons.";
    //email region end

    //controller model attributes region start
    public static final String HAS_ERROR = "hasError";
    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    public static final String BODY_CONTENT = "bodyContent";

    public static final String OFFICES = "offices";

    public static final String DESKS = "desks";
    public static final String SELECTED_DESK = "selectedDesk";

    public static final String EMPLOYEES = "employees";
    public static final String SELECTED_EMPLOYEE = "selectedEmployee";
    public static final String USER = "user";

    public static final String BOOKINGS = "bookings";
    public static final String REQUESTS = "requests";
    public static final String COMPANIES = "companies";
    public static final String CURRENT_DATE = "currentDate";
    //controller model attributes region end

    public static final String ALPHA_NUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

}
