/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

/**
 *
 * @author Envy
 */
public class City {
    
    private final String cityName;
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
