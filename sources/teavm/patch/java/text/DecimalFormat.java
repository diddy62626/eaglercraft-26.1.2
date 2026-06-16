package java.text;
public class DecimalFormat extends NumberFormat {
    public DecimalFormat() {}
    public DecimalFormat(String pattern) {}
    public StringBuffer format(double number, StringBuffer result, java.text.FieldPosition fieldPosition) { return result.append(number); }
    public StringBuffer format(long number, StringBuffer result, java.text.FieldPosition fieldPosition) { return result.append(number); }
    public Number parse(String text, java.text.ParsePosition pos) { return 0; }
    public void setDecimalFormatSymbols(DecimalFormatSymbols symbols) {}
    public DecimalFormatSymbols getDecimalFormatSymbols() { return new DecimalFormatSymbols(); }
}