package ZimmerBiomet.SpineLabel.UI;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    /**/

    public static String testString = "Vis multiaxiale POL4.75 à tête ouverte de 4.5 X 95 MM";
    public static String testString1 = "6.35  MM";
    public static String testRegex = "(\\d+\\s*?)(\\.)(\\s*?\\d+\\s*?)([mMx×X*])";

    public static Pattern p = Pattern.compile(testRegex);

    public static void main(String[] args) throws UnsupportedEncodingException {

        String out = testString.replaceAll("\\p{Z}"," ");
//        String out = testString;
//        System.out.println(out);

//        String out = new String(RegexTest.testString.getBytes("UTF-8"));

//        System.out.println(out);

        Matcher matcher = RegexTest.p.matcher(out);

        System.out.println(matcher.find());


/*

* */
    }

}
