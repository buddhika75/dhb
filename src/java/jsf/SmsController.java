/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import lk.mobitel.esms.session.*;
import lk.mobitel.esms.message.*;
import lk.mobitel.esms.ws.*;

/**
 *
 * @author Buddhika
 */
@Named(value = "smsController")
@SessionScoped
public class SmsController implements Serializable {

    private String userName;
    private String password;
    private String userAlias;
    private String number;
    private String message;
    private String output;

    /**
     * Creates a new instance of SmsController
     */
    public SmsController() {
    }
    
    public void sendSms() {
        if (userName == null || password == null || userAlias == null || number == null || message == null) {
            output="failure";
            return ;
        }
        //create user object
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);

        //initiate session
        SessionManager sm = SessionManager.getInstance();
        System.out.println("Logging in.....");
        if (!sm.isSession()) {
            sm.login(user);
        }
        System.out.println("Logged in!");

        

        //create SmsMessage object 
        SmsMessage msg = new SmsMessage();
        msg.setMessage(message);
        System.out.println("Message length: " + msg.getMessage().length());
        msg.setSender(userAlias);
        msg.getRecipients().add(number);
        msg.setMessageType(0);

        //send sms
        try {
            SMSManager smsMgr = new SMSManager();
            int ret = smsMgr.sendMessage(msg);
            if(ret==200){
                output = "success";
            }else{
                output = "failure";
            }
            return ;
        } catch (NullSessionException ex) {
            System.out.println("ex = " + ex);
            return ;
        }
    }


    public void sendSms(String txt, String no) {
        userName="esmsusr_sr8";
        password="t2l51j";
        userAlias="0712576476";
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);

        //initiate session
        SessionManager sm = SessionManager.getInstance();
        System.out.println("Logging in.....");
        if (!sm.isSession()) {
            sm.login(user);
        }
        System.out.println("Logged in!");

       

        //create SmsMessage object 
        SmsMessage msg = new SmsMessage();
        msg.setSender(userAlias);
        msg.setMessage(txt);
        System.out.println("Message length: " + msg.getMessage().length());
        msg.getRecipients().add(no);

        //send sms
        try {
            SMSManager smsMgr = new SMSManager();
            int ret = smsMgr.sendMessage(msg);
            if(ret==200){
                output = "success";
            }else{
                output = "failure";
            }
            return ;
        } catch (NullSessionException ex) {
            System.out.println("ex = " + ex);
            return ;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

}
