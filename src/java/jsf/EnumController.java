/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import entity.Gender;
import entity.RoomType;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author divudi_lk
 */
@Named(value = "enumController")
@ApplicationScoped
public class EnumController {

    /**
     * Creates a new instance of EnumController
     */
    public EnumController() {
    }
    
    public Gender[] getGender(){
        return Gender.values();
    }   
    
     public RoomType[] getRoomTypes(){
        return RoomType.values();
    } 
    
    
    
}
