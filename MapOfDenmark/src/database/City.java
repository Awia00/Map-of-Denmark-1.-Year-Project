/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

/**
 *
 * @author Christian Grøn
 */
public class City {
    // The name of the city.
    private final String cityName;
    // The postal code of the city.
    private final Integer postalCode;
    
    public City(String cityName, Integer postalCode)
    {
        this.cityName = cityName;
        this.postalCode = postalCode;
    }
    
    public String getCityName()
    {
        return cityName;
    }
    
    public Integer getPostalCode()
    {
        return postalCode;
    }
}
