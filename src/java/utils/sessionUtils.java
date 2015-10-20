package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class sessionUtils {
     public static String getUsePlayerName (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("playerName") : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
}
