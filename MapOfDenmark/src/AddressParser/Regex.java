/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AddressParser;

/**
 * A enum class with the different regExes.
 * @author Alex
 */
public enum Regex {
    
    //STREETNAME("[\\sA-Øa-ø'-]{1,}"), // the old regex for streetnames
    STREETNAME("[^,]*"),
    STREETNUMBER("[1-9]\\d{0,2}"),
    STREETLETTER("^([a-ø]|[A-Ø])?(\\s|(?=,))"),//("([a-ø]|[A-Ø]){0,1}(?<=(e|s)) "),//("([a-ø]|[A-Ø])?( |\\s)?"),
    FLOOR("(\\d{1,2}[. -]*(sal| {2}|\\.)|(stuen |st |st. ))"),//("\\d{1,2}[. -]*(sal| {2}|\\.)"),   
    ZIPCODE("\\b[0-9]{4}\\b"),
    CITY("\\D*");
    
 
    Regex(String regex)
    {
        this.regex = regex;
    } 
    
    private final String regex;
    
    public String getRegex(){
        return regex;
    }
    
    }
